package projeto.locadora.locadora.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Carro {

    @Id
    private String id;

    private String placa;
    private String marca;
    private String modelo;
    private long ano;
    private String cor;
    private Double valor;

    public Carro(String id, String placa, String marca, String modelo, Long ano, String cor, Double valor) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.valor = valor;
    }

    public Carro() {}
}
