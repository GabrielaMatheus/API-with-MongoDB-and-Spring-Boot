package projeto.locadora.locadora.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.AcessorioForm;
import projeto.locadora.locadora.controller.form.mappers.AcessorioMapper;
import projeto.locadora.locadora.model.Acessorio;
import projeto.locadora.locadora.repository.AcessorioRepository;

@Service
public class AcessorioService {

    @Autowired
    private AcessorioRepository acessorioRepository;

    public Page<Acessorio> listar(Pageable paginacao) {
        return acessorioRepository.findAll(paginacao);
    }

    public Acessorio listarUm(String doc) throws NotFoundException {
        return acessorioRepository.findByDoc(doc).orElseThrow(() -> new NotFoundException("Acessório não encontrado"));
    }

    public void persistir(AcessorioForm form) throws NotFoundException {
        Optional<Acessorio> acessorio = acessorioRepository.findByDoc(form.getDoc());
        if (acessorio.isPresent()) {
            throw new NotFoundException("Acessório com esse doc já existente. ");
        }
        acessorioRepository.save(AcessorioMapper.INSTANCE.acessorioFormToAcessorio(form));
    }

    public void atualizar(AcessorioForm form) throws NotFoundException {
        acessorioRepository
            .findById(form.getId())
            .orElseThrow(() -> new NotFoundException("Acessório não encontrado."));
        acessorioRepository.save(AcessorioMapper.INSTANCE.acessorioFormToAcessorio(form));
    }

    public void remover(String id) throws NotFoundException {
        Acessorio acessorio = acessorioRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Acessório não encontrado."));
        acessorioRepository.delete(acessorio);
    }
}
