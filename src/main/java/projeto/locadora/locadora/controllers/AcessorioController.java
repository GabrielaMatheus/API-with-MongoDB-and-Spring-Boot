package projeto.locadora.locadora.controllers;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.AcessorioForm;
import projeto.locadora.locadora.model.Acessorio;
import projeto.locadora.locadora.service.AcessorioService;

@RestController
@RequestMapping("/acessorios")
public class AcessorioController {

    @Autowired
    private AcessorioService acessorioService;

    @GetMapping
    public Page<Acessorio> listar(
        @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 5) Pageable paginacao
    ) {
        return acessorioService.listar(paginacao);
    }

    @GetMapping("/{doc}")
    public Acessorio listarUm(@PathVariable String doc) throws NotFoundException {
        return acessorioService.listarUm(doc);
    }

    @PostMapping
    public void persistir(@RequestBody @Valid AcessorioForm form) throws NotFoundException {
        acessorioService.persistir(form);
    }

    @PutMapping("/{id}")
    public Acessorio atualizar(@RequestBody @Valid AcessorioForm form, @PathVariable String id)
        throws NotFoundException {
        acessorioService.atualizar(form, id);
        return listarUm(id);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) throws NotFoundException {
        acessorioService.remover(id);
    }
}
