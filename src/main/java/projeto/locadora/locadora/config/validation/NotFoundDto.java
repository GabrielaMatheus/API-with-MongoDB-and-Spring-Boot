package projeto.locadora.locadora.config.validation;

public class NotFoundDto {

    private String erro;

    public NotFoundDto(String message) {
        this.erro = message;
    }

    public String getErro() {
        return erro;
    }
}
