package projeto.locadora.locadora.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import javax.validation.constraints.NotBlank;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dataNascimento;

    private String email;

    private String telefone;
}
