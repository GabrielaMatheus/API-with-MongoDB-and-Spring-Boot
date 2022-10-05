package projeto.locadora.locadora.controller.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CarroForm {

    private String id;

    @NotBlank
    private String placa;

    private String marca;

    private String modelo;

    private long ano;

    private String cor;

    private Double valor;
}
