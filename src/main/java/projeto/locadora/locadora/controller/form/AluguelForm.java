package projeto.locadora.locadora.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import projeto.locadora.locadora.model.CustomJsonDateDeserializer;

@Data
public class AluguelForm {

    private String id;

    @NotBlank
    private String placa_carro;

    private List<String> acessorios;

    @NotBlank
    private String cpf;

    @NotNull
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dataAluguel;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dataDevolucao;

    @NotNull
    private Integer tempoSolicitado;
}
