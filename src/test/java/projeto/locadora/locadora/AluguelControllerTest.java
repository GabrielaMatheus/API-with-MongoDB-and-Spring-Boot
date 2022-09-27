package projeto.locadora.locadora;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasValue;

import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.model.Aluguel;
import projeto.locadora.locadora.model.Carro;
import projeto.locadora.locadora.service.AluguelService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AluguelControllerTest {

    @Autowired
    AluguelService aluguelService;

    public String pegaIdAluguel(String placa) throws NotFoundException {
        List<Aluguel> aluguelList = aluguelService.listarUm(placa);
        Aluguel ultimoAluguel;

        if (aluguelList.size() > 0) {
            ultimoAluguel = aluguelList.get(aluguelList.size() - 1);
            return ultimoAluguel.getId();
        }

        ultimoAluguel = aluguelList.get(aluguelList.size());
        return ultimoAluguel.getId();
    }

    public String calculaDataAluguel(String tipoRetorno) {
        LocalDate dataAtual = LocalDate.now().minusDays(5);
        if (tipoRetorno == "consulta") {
            return dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return dataAtual.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    //POST

    //TESTA SIMULAR ALUGUEL
    @Order(1)
    @Test
    public void givenNoRequiredFieldPlacaInSimular_WhenPost_Then400() throws Exception {
        given()
            .body(
                " {\"acessorios\": [\n" +
                "  \"6331b4c64422f6708a0cd027\",\n" +
                "  \"6331b4fb4422f6708a0cd028\"\n" +
                "  ],\n" +
                " \"cpf\": \"518.878.878-08\",\n" +
                " \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                " \"tempoSolicitado\": 2}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simular")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("placa_carro"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(2)
    @Test
    public void givenNoRequiredFieldCPFInSimular_WhenPost_Then400() throws Exception {
        given()
            .body(
                " { \"placa_carro\": \"fgd9847\",\n" +
                " \"acessorios\": [\n" +
                " \"6331b4c64422f6708a0cd027\",\n" +
                " \"6331b4fb4422f6708a0cd028\"\n" +
                "],\n" +
                " \n" +
                " \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                " \"tempoSolicitado\": 2}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simular")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("cpf"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(3)
    @Test
    public void givenNoRequiredFieldTempoSolicitadoInSimular_WhenPost_Then400() throws Exception {
        given()
            .body(
                "  {\"placa_carro\": \"fgd9847\",\n" +
                " \"acessorios\": [\n" +
                " \"6331b4c64422f6708a0cd027\",\n" +
                " \"6331b4fb4422f6708a0cd028\"\n" +
                "], \"cpf\": \"518.878.878-08\",\n" +
                " \n" +
                " \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\" }"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simular")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("tempoSolicitado"))
            .body("erro", hasItems("não deve ser nulo"));
    }

    @Order(4)
    @Test
    public void givenNoExistPlacaInSimular_WhenPost_Then400() throws Exception {
        given()
            .body(
                "{       \"placa_carro\": \"placaErradaTeste\",\n" +
                "            \"acessorios\": [\n" +
                "                \"6331b4c64422f6708a0cd027\",\n" +
                "                \"6331b4fb4422f6708a0cd028\"\n" +
                "            ],\n" +
                "            \"cpf\": \"111.111.111-11\",\n" +
                "            \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                "            \"tempoSolicitado\": 2\n" +
                "}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simular")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Carro não encontrado"));
    }

    @Order(5)
    @Test
    public void givenNoExistCPFInSimular_WhenPost_Then200() throws Exception {
        //deixa simular sem CPF.

        given()
            .body(
                "{       \"placa_carro\": \"fgd9847\",\n" +
                "            \"acessorios\": [\n" +
                "                \"6331b4c64422f6708a0cd027\",\n" +
                "                \"6331b4fb4422f6708a0cd028\"\n" +
                "            ],\n" +
                "            \"cpf\": \"0\",\n" +
                "            \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                "            \"tempoSolicitado\": 2\n" +
                "}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simular")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body(is("1333.4897333333333"));
    }

    @Order(6)
    @Test
    public void givenRequiredFieldInSimular_WhenPost_Then200() throws Exception {
        given()
            .body(
                "{       \"placa_carro\": \"fgd9847\",\n" +
                "            \"acessorios\": [\n" +
                "                \"6331b4c64422f6708a0cd027\",\n" +
                "                \"6331b4fb4422f6708a0cd028\"\n" +
                "            ],\n" +
                "            \"cpf\": \"111.111.111-11\",\n" +
                "            \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                "            \"tempoSolicitado\": 2\n" +
                "}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simular")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body(is("1333.4897333333333"));
    }

    //TESTA PERSISTIR

    @Order(7)
    @Test
    public void givenNoRequiredFieldPlaca_WhenPost_Then400() throws Exception {
        given()
            .body(
                "{ \"acessorios\": [\n" +
                "  \"6331b4c64422f6708a0cd027\",\n" +
                "  \"6331b4fb4422f6708a0cd028\"\n" +
                "  ],\n" +
                " \"cpf\": \"518.878.878-08\",\n" +
                " \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                " \"tempoSolicitado\": 2}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("placa_carro"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(8)
    @Test
    public void givenNoRequiredFieldCPF_WhenPost_Then400() throws Exception {
        given()
            .body(
                " { \"placa_carro\": \"fgd9847\",\n" +
                " \"acessorios\": [\n" +
                " \"6331b4c64422f6708a0cd027\",\n" +
                " \"6331b4fb4422f6708a0cd028\"\n" +
                "],\n" +
                " \n" +
                " \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                " \"tempoSolicitado\": 2}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("cpf"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(9)
    @Test
    public void givenNoRequiredFieldTempoSolicitado_WhenPost_Then400() throws Exception {
        given()
            .body(
                " {       \"placa_carro\": \"fgd9847\",\n" +
                "            \"acessorios\": [\n" +
                "                \"6331b4c64422f6708a0cd027\",\n" +
                "                \"6331b4fb4422f6708a0cd028\"\n" +
                "            ],\n" +
                "            \"cpf\": \"0\",\n" +
                "            \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\"\n" +
                "            \n" +
                "}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("tempoSolicitado"))
            .body("erro", hasItems("não deve ser nulo"));
    }

    @Order(10)
    @Test
    public void givenNoExistPlaca_WhenPost_Then400() throws Exception {
        given()
            .body(
                "{       \"placa_carro\": \"placaErradaTeste\",\n" +
                "            \"acessorios\": [\n" +
                "                \"6331b4c64422f6708a0cd027\",\n" +
                "                \"6331b4fb4422f6708a0cd028\"\n" +
                "            ],\n" +
                "            \"cpf\": \"111.111.111-11\",\n" +
                "            \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                "            \"tempoSolicitado\": 2\n" +
                "}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Carro não encontrado."));
    }

    @Order(11)
    @Test
    public void givenNoExistCPF_WhenPost_Then400() throws Exception {
        given()
            .body(
                " {       \"placa_carro\": \"fgd9847\",\n" +
                "            \"acessorios\": [\n" +
                "                \"6331b4c64422f6708a0cd027\",\n" +
                "                \"6331b4fb4422f6708a0cd028\"\n" +
                "            ],\n" +
                "            \"cpf\": \"0\",\n" +
                "            \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                "            \"tempoSolicitado\": 2\n" +
                "}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Cliente não encontrado."));
    }

    @Order(12)
    @Test
    public void givenRequiredField_WhenPost_Then200() throws Exception {
        given()
            .body(
                "  {       \"placa_carro\": \"fgd9847\",\n" +
                "            \"acessorios\": [\n" +
                "                \"6331b4c64422f6708a0cd027\",\n" +
                "                \"6331b4fb4422f6708a0cd028\"\n" +
                "            ],\n" +
                "            \"cpf\": \"111.111.111-11\",\n" +
                "            \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                "            \"tempoSolicitado\": 2\n" +
                "}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200);
    }

    @Order(13)
    @Test
    public void givenAlreadyExistAluguel_WhenPost_Then400() throws Exception {
        given()
            .body(
                " {       \"placa_carro\": \"fgd9847\",\n" +
                "            \"acessorios\": [\n" +
                "                \"6331b4c64422f6708a0cd027\",\n" +
                "                \"6331b4fb4422f6708a0cd028\"\n" +
                "            ],\n" +
                "            \"cpf\": \"111.111.111-11\",\n" +
                "            \"dataAluguel\": \"" +
                calculaDataAluguel("") +
                "\",\n" +
                "            \"tempoSolicitado\": 2\n" +
                "}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Carro já alugado. Aguardando devolução."));
    }

    //GET

    @Order(14)
    @org.junit.jupiter.api.Test
    public void whenGet_Then200() throws Exception {
        given().when().get("/alugueis").then().assertThat().statusCode(200).and().log().all();
    }

    @Order(15)
    @org.junit.jupiter.api.Test
    public void givenUniquePlaca_whenGet_Then200() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/alugueis/fgd9847")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("placa_carro", hasItem("fgd9847"))
            .body("acessorios", everyItem(hasItem("6331b4c64422f6708a0cd027")))
            .body("acessorios", everyItem(hasItem("6331b4c64422f6708a0cd027")))
            .body("cpf", hasItem("111.111.111-11"))
            .body("dataAluguel", hasItem(calculaDataAluguel("consulta")))
            .body("dataDevolucao", not(hasValue(notNullValue())))
            .body("tempoSolicitado", hasItem(2));
    }

    //VERIFICA SE SALVOU NO ALUGUEL VALORES

    @Order(16)
    @org.junit.jupiter.api.Test
    public void givenUniqueIdInAluguelValores_whenGet_Then200() throws Exception {
        String idAluguel = pegaIdAluguel("fgd9847");

        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/aluguelValores/" + idAluguel)
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("idAluguel", is(idAluguel))
            .body("valor", is(1333.4897F));
    }

    //SIMULA DEVOLUÇÃO
    @Order(17)
    @Test
    public void givenNoRequiredFieldPlacaCarroInSimularDevolucao_WhenPost_Then400() throws Exception {
        given()
            .body("{}")
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simularDevolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("placa_carro"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(18)
    @Test
    public void givenNoExistPlacaCarroInSimularDevolucao_WhenPost_Then400() throws Exception {
        given()
            .body("{   \"placa_carro\": \"placaErradaTeste\"}")
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simularDevolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Aluguel não encontrado"));
    }

    @Order(19)
    @Test
    public void givenPlacaCarroInSimularDevolucao_WhenPost_Then200() throws Exception {
        given()
            .body("{   \"placa_carro\": \"fgd9847\"}")
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simularDevolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("valorTotal", is("1426,83"))
            .body("valor", is("1333,49"))
            .body("valorMulta", is("93,34"));
    }

    //PERSISTIR DEVOLUÇÃO
    @Order(20)
    @Test
    public void givenNoRequiredFieldPlacaCarro_WhenPost_Then400() throws Exception {
        given()
            .body("{}")
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/devolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("placa_carro"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(21)
    @Test
    public void givenNoExistPlacaCarroInDevolucao_WhenPost_Then400() throws Exception {
        given()
            .body("{   \"placa_carro\": \"placaErradaTeste\"}")
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/devolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Aluguel não encontrado"));
    }

    @Order(22)
    @Test
    public void givenPlacaCarro_WhenPost_Then200() throws Exception {
        given()
            .body("{   \"placa_carro\": \"fgd9847\"}")
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/devolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200);
    }

    @Order(23)
    @Test
    public void givenPlacaCarroAlreadyReturned_WhenPost_Then400() throws Exception {
        given()
            .body("{   \"placa_carro\": \"fgd9847\"}")
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/devolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Aluguel não encontrado"));
    }

    //VERIFICA SE SALVOU NO ALUGUEL VALORES

    @Order(24)
    @org.junit.jupiter.api.Test
    public void givenUniqueIdInAluguelValores_whenGetDevolucao_Then200() throws Exception {
        String idAluguel = pegaIdAluguel("fgd9847");

        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/aluguelValores/" + idAluguel)
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("idAluguel", is(idAluguel))
            .body("valor", is(1426.834F));
    }

    //DELETE
    @Order(25)
    @org.junit.jupiter.api.Test
    public void givenId_whenDelete_Then200() throws Exception {
        String idAluguel = pegaIdAluguel("fgd9847");

        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/alugueis/" + idAluguel)
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200);
    }

    @Order(26)
    @org.junit.jupiter.api.Test
    public void givenUniquePlaca_whenGet_Then200_AndBodyEmpty() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/alugueis/fgd9847")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Aluguel não encontrado."));
    }
}
