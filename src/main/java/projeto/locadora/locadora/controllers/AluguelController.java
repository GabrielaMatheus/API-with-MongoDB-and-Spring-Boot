package projeto.locadora.locadora.controllers;

import java.util.List;
import javax.validation.Valid;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.AluguelForm;
import projeto.locadora.locadora.controller.form.ValorMultaForm;
import projeto.locadora.locadora.model.Aluguel;
import projeto.locadora.locadora.service.AluguelService;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @GetMapping
    public Page<Aluguel> listar(
        @PageableDefault(sort = "id", direction = Direction.ASC, page = 0, size = 5) Pageable paginacao
    ) {
        return aluguelService.listar(paginacao);
    }

    @GetMapping("/{placa}")
    public List<Aluguel> listarUm(@PathVariable String placa) throws NotFoundException {
        return aluguelService.listarUm(placa);
    }

    @RequestMapping(value = "simular", method = RequestMethod.POST)
    public Double simular(@RequestBody @Valid AluguelForm form) throws NotFoundException {
        return aluguelService.valorAluguel(form);
    }

    @RequestMapping(value = "simularDevolucao", method = RequestMethod.POST)
    public JSONObject simularDevolucao(@RequestBody @Valid ValorMultaForm form) throws NotFoundException {
        return aluguelService.valorMulta(form);
    }

    @PostMapping
    public void persistirAluguel(@RequestBody @Valid AluguelForm form) throws NotFoundException {
        aluguelService.persistir(form);
    }

    @RequestMapping(value = "devolucao", method = RequestMethod.POST)
    public void persistirDevolucao(@RequestBody @Valid ValorMultaForm form) throws NotFoundException {
        aluguelService.persistirDevolucao(form);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void remover(@PathVariable String id) throws NotFoundException {
        aluguelService.remover(id);
    }
}
