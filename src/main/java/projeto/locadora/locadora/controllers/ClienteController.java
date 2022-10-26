package projeto.locadora.locadora.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.ClienteForm;
import projeto.locadora.locadora.model.Cliente;
import projeto.locadora.locadora.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public Page<Cliente> listar(
        @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 5) Pageable paginacao
    ) {
        return clienteService.listar(paginacao);
    }

    @GetMapping("/{cpf}")
    public Cliente listarUm(@PathVariable String cpf) throws NotFoundException {
        return clienteService.listarUm(cpf);
    }

    @PostMapping
    public void persistir(@RequestBody @Valid ClienteForm form) throws NotFoundException {
        clienteService.persistir(form);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@RequestBody @Valid ClienteForm form, @PathVariable String id) throws NotFoundException {
        clienteService.atualizar(form, id);
        return listarUm(id);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) throws NotFoundException {
        clienteService.remover(id);
    }
}
