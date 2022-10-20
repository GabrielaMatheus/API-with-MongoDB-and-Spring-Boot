package projeto.locadora.locadora.controller.form;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import projeto.locadora.locadora.model.AluguelValor;
import projeto.locadora.locadora.repository.AluguelValorRepository;

@Data
public class AluguelValorForm {

    @Autowired
    private AluguelValor aluguelValor;

    private String id;

    @NotNull
    private String idAluguel;

    @NotNull
    private Double valor;

    public AluguelValorForm(String idAluguel, Double valor) {
        this.idAluguel = idAluguel;
        this.valor = valor;
    }

    public static AluguelValor updateValor(
        String idAluguel,
        AluguelValorRepository aluguelValorRepository,
        Double novoValor
    ) {
        AluguelValor aluguelvalor = aluguelValorRepository.getReferenceByIdAluguel(idAluguel);
        aluguelvalor.setValor(novoValor);
        return aluguelvalor;
    }
}
