package projeto.locadora.locadora.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.model.Acessorio;
import projeto.locadora.locadora.model.AluguelValor;
import projeto.locadora.locadora.repository.AluguelValorRepository;

@Service
public class AluguelValorService {

    @Autowired
    private AluguelValorRepository aluguelValorRepository;

    public Page<AluguelValor> listar(Pageable paginacao) {
        return aluguelValorRepository.findAll(paginacao);
    }

    public AluguelValor listarUm(String idAluguel) throws NotFoundException {
        Optional<AluguelValor> converteParaOptional = Optional.ofNullable(
            aluguelValorRepository.findByIdAluguel(idAluguel)
        );
        return converteParaOptional.orElseThrow(() -> new NotFoundException("Aluguel não encontrado."));
    }
}
