package projeto.locadora.locadora.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AluguelValor {

    @Id
    private String id;

    private String idAluguel;

    private Double valor;
}
