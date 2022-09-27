package projeto.locadora.locadora.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public Carro listarUm(String placa) throws NotFoundException {
        return carroRepository.findByPlaca(placa).orElseThrow(() -> new NotFoundException("Placa não encontrada"));
    }

    public void persistir(CarroForm form) throws NotFoundException {
        Optional<Carro> carro = carroRepository.findByPlaca(form.getPlaca());
        if (carro.isPresent()) {
            throw new NotFoundException("Placa já cadastrada.");
        }
        carroRepository.save(CarroMapper.INSTANCE.carroFormToCarro(form));
    }

    public void atualizar(CarroForm form) throws NotFoundException {
        carroRepository.findById(form.getId()).orElseThrow(() -> new NotFoundException("Carro não encontrado."));
        carroRepository.save(CarroMapper.INSTANCE.carroFormToCarro(form));
    }

    public void remover(String id) throws NotFoundException {
        Carro carro = carroRepository.findById(id).orElseThrow(() -> new NotFoundException("Carro não encontrado."));
        carroRepository.delete(carro);
    }
}
