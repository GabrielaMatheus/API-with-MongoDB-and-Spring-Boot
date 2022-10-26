package projeto.locadora.locadora.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.ClienteForm;
import projeto.locadora.locadora.controller.form.mappers.ClienteMapper;
import projeto.locadora.locadora.model.Acessorio;
import projeto.locadora.locadora.model.Cliente;
import projeto.locadora.locadora.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Page<Cliente> listar(Pageable paginacao) {
        return clienteRepository.findAll(paginacao);
    }

    public Cliente listarUm(String id) throws NotFoundException {
        return clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
    }

    public void persistir(ClienteForm form) throws NotFoundException {
        Optional<Cliente> cliente = clienteRepository.findByCpf(form.getCpf());
        if (cliente.isPresent()) {
            throw new NotFoundException("CPF já cadastrado.");
        }
        clienteRepository.save(ClienteMapper.INSTANCE.clienteFormToCliente(form));
    }

    public void atualizar(ClienteForm form, String id) throws NotFoundException {
        Cliente cliente = clienteRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        cliente.setCpf(form.getCpf());
        cliente.setNome(form.getNome());
        cliente.setEmail(form.getEmail());
        cliente.setSobrenome(form.getSobrenome());
        cliente.setTelefone(form.getTelefone());
        cliente.setDataNascimento(form.getDataNascimento());

        clienteRepository.save(cliente);
    }

    public void remover(String id) throws NotFoundException {
        Cliente cliente = clienteRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
        clienteRepository.delete(cliente);
    }
}
