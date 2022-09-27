package projeto.locadora.locadora.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.swing.text.html.Option;
import lombok.NonNull;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.AluguelForm;
import projeto.locadora.locadora.controller.form.AluguelValorForm;
import projeto.locadora.locadora.controller.form.ValorMultaForm;
import projeto.locadora.locadora.controller.form.mappers.AluguelMapper;
import projeto.locadora.locadora.controller.form.mappers.AluguelValorMapper;
import projeto.locadora.locadora.model.Acessorio;
import projeto.locadora.locadora.model.Aluguel;
import projeto.locadora.locadora.model.Carro;
import projeto.locadora.locadora.repository.AcessorioRepository;
import projeto.locadora.locadora.repository.AluguelRepository;
import projeto.locadora.locadora.repository.AluguelValorRepository;
import projeto.locadora.locadora.repository.CarroRepository;
import projeto.locadora.locadora.repository.ClienteRepository;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private AluguelValorRepository aluguelValorRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AcessorioRepository acessorioRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    TimeUnit time = TimeUnit.DAYS;

    public Page<Aluguel> listar(Pageable paginacao) {
        return aluguelRepository.findAll(paginacao);
    }

    public List<Aluguel> listarUm(String placa) throws NotFoundException {
        return aluguelRepository.findByPlaca(placa);
    }

    @Cacheable(value = "simulacao", key = "#form.placa_carro")
    public Double valorAluguel(@NonNull AluguelForm form) throws NotFoundException {
        Double valorCarro = carroRepository
            .findByPlaca(form.getPlaca_carro())
            .orElseThrow(() -> new NotFoundException("Carro não encontrado"))
            .getValor();

        Double somaValoresAcessorios = StreamSupport
            .stream(acessorioRepository.findAllById(form.getAcessorios()).spliterator(), false)
            .map(Acessorio::getValor)
            .collect(Collectors.summingDouble(Double::doubleValue));

        System.out.println(somaValoresAcessorios);

        return ((form.getTempoSolicitado() * (valorCarro + somaValoresAcessorios) / 30) / 10);
    }

    @Cacheable(value = "simulacaoDevolucao", key = "#form.placa_carro")
    @SuppressWarnings("unchecked")
    public JSONObject valorMulta(ValorMultaForm form) throws NotFoundException {
        DecimalFormat salaryFormat = new DecimalFormat("##.00");
        JSONObject jsonObject = new JSONObject();

        Aluguel aluguel = aluguelRepository
            .findByPlacaCarro(form.getPlaca_carro())
            .orElseThrow(() -> new NotFoundException("Aluguel não encontrado"));

        Double valorAluguel = aluguelValorRepository.findByIdAluguel(aluguel.getId()).getValor();

        long diasDiferenca = time.convert(
            (this.calculaDataDevolucao(aluguel).getTime() - aluguel.getDataAluguel().getTime()),
            TimeUnit.MILLISECONDS
        );

        Double multa = valorAluguel * (diasDiferenca / 100.0);
        String valorAluguelConver = salaryFormat.format(valorAluguel);
        String valorMultaConver = salaryFormat.format(multa);
        String novoValorConver = salaryFormat.format(this.calculoMultaAluguel(form));

        jsonObject.put("valor", valorAluguelConver);
        jsonObject.put("valorMulta", valorMultaConver);
        jsonObject.put("valorTotal", novoValorConver);

        return jsonObject;
    }

    public void persistir(AluguelForm form) throws NotFoundException {
        //supondo pela lógica que o último registro é um aluguel ainda não devolvido.
        List<Aluguel> aluguelList = listarUm(form.getPlaca_carro());

        if (aluguelList.size() > 0) {
            Aluguel ultimoAluguel = aluguelList.get(aluguelList.size() - 1);
            if (ultimoAluguel.getDataDevolucao() == null) {
                throw new NotFoundException("Carro já alugado. Aguardando devolução.");
            }
        }

        carroRepository
            .findByPlaca(form.getPlaca_carro())
            .orElseThrow(() -> new NotFoundException("Carro não encontrado."));

        clienteRepository.findByCpf(form.getCpf()).orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        Aluguel aluguel = AluguelMapper.INSTANCE.aluguelFormToAluguel(form);
        aluguelRepository.save(aluguel);

        aluguelValorRepository.save(
            AluguelValorMapper.INSTANCE.aluguelValorFormToAluguelValor(
                new AluguelValorForm(aluguel.getId(), this.valorAluguel(form))
            )
        );
    }

    public void persistirDevolucao(ValorMultaForm form) throws NotFoundException {
        Aluguel aluguel = aluguelRepository
            .findByPlacaCarro(form.getPlaca_carro())
            .orElseThrow(() -> new NotFoundException("Aluguel não encontrado"));

        //salva no AluguelValor
        aluguelValorRepository.save(
            AluguelValorForm.updateValor(aluguel.getId(), aluguelValorRepository, this.calculoMultaAluguel(form))
        );

        //salva no Aluguel
        aluguel.setDataDevolucao(this.calculaDataDevolucao(aluguel));
        aluguelRepository.save(aluguel);
    }

    public Date calculaDataDevolucao(Aluguel aluguel) {
        LocalDate dataTempSol = LocalDate.now().plusDays(aluguel.getTempoSolicitado());
        Date dataDevolucao = Date.from(dataTempSol.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return dataDevolucao;
    }

    public Double calculoMultaAluguel(ValorMultaForm form) throws NotFoundException {
        Aluguel aluguel = aluguelRepository
            .findByPlacaCarro(form.getPlaca_carro())
            .orElseThrow(() -> new NotFoundException("Aluguel não encontrado"));
        Double valorAluguel = aluguelValorRepository.findByIdAluguel(aluguel.getId()).getValor();

        LocalDate dataTempSol = LocalDate.now().plusDays(aluguel.getTempoSolicitado());
        Date dataDevolucao = Date.from(dataTempSol.atStartOfDay(ZoneId.systemDefault()).toInstant());

        long diasDiferenca = time.convert(
            (dataDevolucao.getTime() - aluguel.getDataAluguel().getTime()),
            TimeUnit.MILLISECONDS
        );

        Double novoValor = valorAluguel + (valorAluguel * (diasDiferenca / 100.0));

        return novoValor;
    }

    public void remover(String id) throws NotFoundException {
        Aluguel aluguel = aluguelRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Aluguel não encontrado."));
        aluguelRepository.delete(aluguel);
    }
}
