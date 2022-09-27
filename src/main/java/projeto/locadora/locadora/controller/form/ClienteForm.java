package projeto.locadora.locadora.controller.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import projeto.locadora.locadora.model.CustomJsonDateDeserializer;

@Data
public class ClienteForm {

    private String id;

    @NotBlank
    private String cpf;

    private String nome;

    private String sobrenome;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date dataNascimento;

    private String email;

    private String telefone;
}
