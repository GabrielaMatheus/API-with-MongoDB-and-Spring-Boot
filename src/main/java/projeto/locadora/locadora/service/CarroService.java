package projeto.locadora.locadora.service;

import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.CarroForm;
import projeto.locadora.locadora.controller.form.mappers.CarroMapper;
import projeto.locadora.locadora.model.Acessorio;
import projeto.locadora.locadora.model.Carro;
import projeto.locadora.locadora.repository.CarroRepository;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public Page<Carro> listar(Pageable paginacao) {
        Page<Carro> carros = carroRepository.findAll(paginacao);
        return carros;
    }

    public Carro listarUm(String id) throws NotFoundException {
        return carroRepository.findById(id).orElseThrow(() -> new NotFoundException("Placa não encontrada"));
    }

    public Carro listarPelaPlaca(String placa) throws NotFoundException {
        return carroRepository.findByPlaca(placa).orElseThrow(() -> new NotFoundException("Placa não encontrada"));

    }

    public Map<String, String> persistir(CarroForm form) throws NotFoundException {
        Optional<Carro> carro = carroRepository.findByPlaca(form.getPlaca());
        if (carro.isPresent()) {
            throw new NotFoundException("Placa já cadastrada.");
        }
        Carro carroSalvo = carroRepository.save(CarroMapper.INSTANCE.carroFormToCarro(form));
        return  Map.of("id", carroSalvo.getId());


    }

    public void atualizar(CarroForm form, String id) throws NotFoundException {
        Carro carro = carroRepository.findById(id).orElseThrow(() -> new NotFoundException("Carro não encontrado."));

        carro.setAno(form.getAno());
        carro.setCor(form.getCor());
        carro.setPlaca(form.getPlaca());
        carro.setMarca(form.getMarca());
        carro.setModelo(form.getModelo());
        carro.setValor(form.getValor());

        carroRepository.save(carro);
    }

    public void remover(String id) throws NotFoundException {
        Carro carro = carroRepository.findById(id).orElseThrow(() -> new NotFoundException("Carro não encontrado."));
        carroRepository.delete(carro);
    }
}
