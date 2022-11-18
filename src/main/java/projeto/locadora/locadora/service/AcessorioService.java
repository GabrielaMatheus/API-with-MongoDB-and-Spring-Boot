package projeto.locadora.locadora.service;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.AcessorioForm;
import projeto.locadora.locadora.controller.form.mappers.AcessorioMapper;
import projeto.locadora.locadora.model.Acessorio;
import projeto.locadora.locadora.model.Carro;
import projeto.locadora.locadora.repository.AcessorioRepository;

@Service
public class AcessorioService {

    @Autowired
    private AcessorioRepository acessorioRepository;


    public Page<Acessorio> listar(Pageable paginacao) {
        return acessorioRepository.findAll(paginacao);
    }

    public Acessorio listarUm(String id) throws NotFoundException {
        return acessorioRepository.findById(id).orElseThrow(() -> new NotFoundException("Acessório não encontrado"));
    }

    public Acessorio listarPeloDoc(String doc) throws NotFoundException {
        return acessorioRepository.findByDoc(doc).orElseThrow(() -> new NotFoundException("Acessório não encontrado"));

    }

    public Map<String,String> persistir(AcessorioForm form) throws NotFoundException {
        Optional<Acessorio> acessorio = acessorioRepository.findByDoc(form.getDoc());
        if (acessorio.isPresent()) {
            throw new NotFoundException("Acessório com esse doc já existente. ");
        }
        Acessorio acessorioSalvo = acessorioRepository.save(AcessorioMapper.INSTANCE.acessorioFormToAcessorio(form));
        return  Map.of("id", acessorioSalvo.getId());
    }

    public void atualizar(AcessorioForm form, String id) throws NotFoundException {
        Acessorio acessorio = acessorioRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Acessório não encontrado."));

        acessorio.setDoc(form.getDoc());
        acessorio.setValor(form.getValor());
        acessorio.setNome(form.getNome());

        acessorioRepository.save(acessorio);
    }

    public void remover(String id) throws NotFoundException {
        Acessorio acessorio = acessorioRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Acessório não encontrado."));
        acessorioRepository.delete(acessorio);
    }
}
