package projeto.locadora.locadora;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasValue;

import io.restassured.http.ContentType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import projeto.locadora.locadora.config.validation.exceptions.NotFoundException;
import projeto.locadora.locadora.controller.form.AluguelForm;
import projeto.locadora.locadora.controller.form.CarroForm;
import projeto.locadora.locadora.controller.form.ClienteForm;
import projeto.locadora.locadora.model.Aluguel;
import projeto.locadora.locadora.model.AluguelValor;
import projeto.locadora.locadora.service.AluguelService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MongoDBConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
public class AluguelControllerTest {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

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

    public String calculaDataAluguel() throws ParseException {
        return LocalDate.now().minusDays(5).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private static Date createDate(String dateAsString) throws ParseException {
        return formatter.parse(dateAsString);
    }

    public List<String> listaAcessorios() {
        List<String> listaAcessorios = new ArrayList<String>();
        listaAcessorios.add(0, "6331b4c64422f6708a0cd027");
        listaAcessorios.add(1, "6331b4fb4422f6708a0cd028");
        return listaAcessorios;
    }

    //CADASTRA CARRO QUE SERÁ ALUGADO
    @Order(1)
    @Test
    public void giverCarro_WhenPost_Then200() throws Exception {
        CarroForm carroForm = new CarroForm();
        carroForm.setPlaca("fgd9847");
        carroForm.setMarca("honda");
        carroForm.setModelo("civic");
        carroForm.setAno(2021);
        carroForm.setCor("cor teste");
        carroForm.setValor(200000.00);

        given()
            .body(carroForm)
            .contentType(ContentType.JSON)
            .when()
            .post("/carros")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200);
    }

    //CADASTRA CARRO QUE SERÁ ALUGADO
    @Order(2)
    @Test
    public void giverCliente_WhenPost_Then200() throws Exception {
        ClienteForm clienteForm = new ClienteForm();
        clienteForm.setCpf("111.111.111-11");
        clienteForm.setNome("nome teste");
        clienteForm.setSobrenome("sobrenome teste");
        clienteForm.setEmail("emailteste@gmail.com");
        clienteForm.setTelefone("1699999-9999");
        clienteForm.setDataNascimento(createDate("20-09-2002"));

        given()
            .body(clienteForm)
            .contentType(ContentType.JSON)
            .when()
            .post("/clientes")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200);
    }

    //SIMULAR ALUGUEL
    @Order(3)
    @Test
    public void givenNoRequiredFieldPlacaInSimular_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
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

    @Order(4)
    @Test
    public void givenNoRequiredFieldCPFInSimular_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
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

    @Order(5)
    @Test
    public void givenNoRequiredFieldTempoSolicitadoInSimular_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));

        given()
            .body(aluguelForm)
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

    @Order(6)
    @Test
    public void givenNoExistPlacaInSimular_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("placaErradaTeste");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("111.111.111-11");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
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

    @Order(7)
    @Test
    public void givenRightDataInSimular_WhenPost_Then200() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("111.111.111-11");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simular")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body(is("1333.3333333333335"));
    }

    //PERSISTIR ALUGUEL

    @Order(8)
    @Test
    public void givenNoRequiredFieldPlaca_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("518.878.878-06");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
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

    @Order(9)
    @Test
    public void givenNoRequiredFieldCPF_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
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

    @Order(10)
    @Test
    public void givenNoRequiredFieldTempoSolicitado_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));

        given()
            .body(aluguelForm)
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

    @Order(11)
    @Test
    public void givenNoExistPlaca_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("placaErradaTeste");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("111.111.111-11");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
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

    @Order(12)
    @Test
    public void givenNoExistCPF_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("0");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
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

    @Order(13)
    @Test
    public void givenRequiredField_WhenPost_Then200() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("111.111.111-11");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200);
    }

    @Order(14)
    @Test
    public void givenAlreadyExistAluguel_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");
        aluguelForm.setAcessorios(listaAcessorios());
        aluguelForm.setCpf("111.111.111-11");
        aluguelForm.setDataAluguel(createDate(calculaDataAluguel()));
        aluguelForm.setTempoSolicitado(2);

        given()
            .body(aluguelForm)
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

    @Order(15)
    @org.junit.jupiter.api.Test
    public void whenGet_Then200() throws Exception {
        given().when().get("/alugueis").then().assertThat().statusCode(200).and().log().all();
    }

    @Order(16)
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
            .body("dataAluguel", hasItem(calculaDataAluguel()))
            .body("dataDevolucao", not(hasValue(notNullValue())))
            .body("tempoSolicitado", hasItem(2));
    }

    //VERIFICA SE SALVOU NO ALUGUEL VALORES

    @Order(17)
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
            .body("valor", is(1333.3334F));
    }

    //SIMULAR DEVOLUÇÃO
    @Order(18)
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

    @Order(19)
    @Test
    public void givenNoExistPlacaCarroInSimularDevolucao_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("placaErradaTeste");

        given()
            .body(aluguelForm)
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

    @Order(20)
    @Test
    public void givenPlacaCarroInSimularDevolucao_WhenPost_Then200() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");

        given()
            .body(aluguelForm)
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/simularDevolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("valorTotal", is("1426,67"))
            .body("valor", is("1333,33"))
            .body("valorMulta", is("93,33"));
    }

    //PERSISTIR DEVOLUÇÃO
    @Order(21)
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

    //PERSISTIR DEVOLUÇÃO
    @Order(22)
    @Test
    public void givenNoExistPlacaCarroInDevolucao_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("placaErradaTeste");

        given()
            .body(aluguelForm)
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

    @Order(23)
    @Test
    public void givenPlacaCarro_WhenPost_Then200() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");

        given()
            .body(aluguelForm)
            .contentType(ContentType.JSON)
            .when()
            .post("/alugueis/devolucao")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200);
    }

    @Order(24)
    @Test
    public void givenPlacaCarroAlreadyReturned_WhenPost_Then400() throws Exception {
        AluguelForm aluguelForm = new AluguelForm();
        aluguelForm.setPlaca_carro("fgd9847");

        given()
            .body(aluguelForm)
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

    @Order(25)
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
            .body("valor", is(1426.6666F));
    }

    //DELETE
    @Order(26)
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

    @Order(27)
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
