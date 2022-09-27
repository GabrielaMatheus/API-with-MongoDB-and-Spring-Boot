package projeto.locadora.locadora.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Acessorio {

    @Id
    private String id;

    private String doc;
    private String nome;
    private Double valor;

    public Acessorio(String id, String doc, String nome, Double valor) {
        this.doc = doc;
        this.id = id;
        this.nome = nome;
        this.valor = valor;
    }

    public Acessorio() {}
}
