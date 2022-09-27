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
import projeto.locadora.locadora.controller.form.CarroForm;
import projeto.locadora.locadora.model.Carro;
import projeto.locadora.locadora.service.CarroService;

@RestController
@RequestMapping("/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping
    public Page<Carro> listar(
        @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 5) Pageable paginacao
    ) {
        return carroService.listar(paginacao);
    }

    @GetMapping("/{placa}")
    public Carro listarUm(@PathVariable String placa) throws NotFoundException {
        return carroService.listarUm(placa);
    }

    @PostMapping
    public void persistir(@RequestBody @Valid CarroForm form) throws NotFoundException {
        carroService.persistir(form);
    }

    @PutMapping
    public Carro atualizar(@RequestBody @Valid CarroForm form) throws NotFoundException {
        carroService.atualizar(form);
        return listarUm(form.getPlaca());
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) throws NotFoundException {
        carroService.remover(id);
    }
}
