package projeto.locadora.locadora.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Aluguel {

    @Id
    private String id;

    private String placa_carro;

    private List<String> acessorios;

    private String cpf;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dataAluguel;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date dataDevolucao;

    private Integer tempoSolicitado;

    public Aluguel(
        String placa_carro,
        List<String> acessorios,
        String cpf,
        Date dataAluguel,
        Date dataDevolucao,
        Integer tempoSolicitado
    ) {
        super();
        this.placa_carro = placa_carro;
        this.acessorios = acessorios;
        this.cpf = cpf;
        this.dataAluguel = dataAluguel;
        this.dataDevolucao = dataDevolucao;
        this.tempoSolicitado = tempoSolicitado;
    }

    public Aluguel() {}
}
