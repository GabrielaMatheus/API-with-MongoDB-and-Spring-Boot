package projeto.locadora.locadora.controller.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcessorioForm {

    private String id;

    @NotBlank
    private String doc;

    private String nome;

    private Double valor;
}
