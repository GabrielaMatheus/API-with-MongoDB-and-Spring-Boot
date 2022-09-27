package projeto.locadora.locadora.controller.form;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValorMultaForm {

    @NotBlank
    private String placa_carro;
}
