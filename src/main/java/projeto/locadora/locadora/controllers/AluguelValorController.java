package projeto.locadora.locadora.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.model.AluguelValor;
import projeto.locadora.locadora.service.AluguelValorService;

@RestController
@RequestMapping("/aluguelValores")
public class AluguelValorController {

    @Autowired
    private AluguelValorService aluguelValorService;

    @GetMapping
    @Cacheable(value = "listaAluguelValor")
    public Page<AluguelValor> listar(
        @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 5) Pageable paginacao
    ) {
        return aluguelValorService.listar(paginacao);
    }

    @GetMapping("/{idAluguel}")
    public AluguelValor listarUm(@PathVariable String idAluguel) throws NotFoundException {
        return aluguelValorService.listarUm(idAluguel);
    }
}
