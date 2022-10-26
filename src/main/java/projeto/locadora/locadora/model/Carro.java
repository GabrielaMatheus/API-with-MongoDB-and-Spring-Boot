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
public class Carro {

    @Id
    private String id;

    private String placa;
    private String marca;
    private String modelo;
    private long ano;
    private String cor;
    private Double valor;
}
