package projeto.locadora.locadora.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor //construtor com argumentos
@NoArgsConstructor //construtor sem argumentos
public class Acessorio {

    @Id
    private String id;

    private String doc;
    private String nome;
    private Double valor;
}
