package projeto.locadora.locadora.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class AluguelValor {

    @Id
    private String id;

    private String idAluguel;

    private Double valor;

    public AluguelValor(String idAluguel, Double valor) {
        super();
        this.idAluguel = idAluguel;
        this.valor = valor;
    }

    public AluguelValor() {}
}
