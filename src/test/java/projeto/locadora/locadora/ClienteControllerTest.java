package projeto.locadora.locadora;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import io.restassured.http.ContentType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import projeto.locadora.locadora.controller.form.AluguelForm;
import projeto.locadora.locadora.controller.form.ClienteForm;
import projeto.locadora.locadora.model.Cliente;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MongoDBConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
public class ClienteControllerTest {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    private static Date createDate(String dateAsString) throws ParseException {
        return formatter.parse(dateAsString);
    }

    public String pegaIdCliente(String cpf) {
        return given().contentType(ContentType.JSON).when().get("/clientes/" + cpf).then().extract().path("id");
    }

    //POST
    @Order(1)
    @Test
    public void givenNoRequiredFieldCPF_WhenPost_Then400() throws Exception {
        ClienteForm clienteForm = new ClienteForm();
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
            .statusCode(400)
            .body("campo", hasItems("cpf"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(2)
    @Test
    public void givenRequiredField_WhenPost_Then200() throws Exception {
        ClienteForm clienteForm = new ClienteForm();
        clienteForm.setCpf("123.123.123-12");
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

    @Order(3)
    @Test
    public void givenAlreadyExistsRequiredField_WhenPost_Then400() throws Exception {
        ClienteForm clienteForm = new ClienteForm();
        clienteForm.setCpf("123.123.123-12");
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
            .statusCode(400)
            .body("erro", is("CPF já cadastrado."));
    }

    //GET
    @Order(4)
    @Test
    public void whenGet_Then200() throws Exception {
        given().when().get("/clientes").then().assertThat().statusCode(200).and().log().all();
    }

    @Order(5)
    @Test
    public void givenCPF_whenGet_Then200() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/clientes/123.123.123-12")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("cpf", is("123.123.123-12"))
            .body("nome", is("nome teste"))
            .body("sobrenome", is("sobrenome teste"))
            .body("dataNascimento", is("20-09-2002"))
            .body("email", is("emailteste@gmail.com"))
            .body("telefone", is("1699999-9999"));
    }

    @Order(6)
    @Test
    public void givenWrongCPF_whenGet_Then400() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/clientes/wrongCPF")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Cliente não encontrado"));
    }

    //PUT

    @Order(7)
    @Test
    public void givenNoRequiredField_WhenPut_Then400() throws Exception {
        ClienteForm clienteForm = new ClienteForm();
        clienteForm.setId("");
        clienteForm.setCpf("123.123.123-12");
        clienteForm.setNome("nome alterado");
        clienteForm.setSobrenome("sobrenome alterado");
        clienteForm.setDataNascimento(createDate("12-11-2004"));
        clienteForm.setEmail("emailteste@gmail.com");
        clienteForm.setTelefone("1699999-9999");

        given()
            .body(clienteForm)
            .contentType(ContentType.JSON)
            .when()
            .put("/carros")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("placa"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(8)
    @Test
    public void givenWrongId_whenPut_then400() throws Exception {
        ClienteForm clienteForm = new ClienteForm();
        clienteForm.setId("wrongIdTest");
        clienteForm.setCpf("123.123.123-12");
        clienteForm.setNome("nome alterado");
        clienteForm.setSobrenome("sobrenome alterado");
        clienteForm.setDataNascimento(createDate("12-11-2004"));
        clienteForm.setEmail("emailteste@gmail.com");
        clienteForm.setTelefone("1699999-9999");

        given()
            .body(clienteForm)
            .contentType(ContentType.JSON)
            .when()
            .put("/clientes")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Cliente não encontrado."));
    }

    @Order(9)
    @Test
    public void givenDataWithRequiredField_WhenPut_Then200() throws Exception {
        ClienteForm clienteForm = new ClienteForm();
        clienteForm.setId(pegaIdCliente("123.123.123-12"));
        clienteForm.setCpf("222.222.222-22");
        clienteForm.setNome("nome alterado");
        clienteForm.setSobrenome("sobrenome alterado");
        clienteForm.setDataNascimento(createDate("12-11-2004"));
        clienteForm.setEmail("emailalterado@gmail.com");
        clienteForm.setTelefone("1698888-8888");

        given()
            .body(clienteForm)
            .contentType(ContentType.JSON)
            .when()
            .put("/clientes")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("cpf", is("222.222.222-22"))
            .body("nome", is("nome alterado"))
            .body("sobrenome", is("sobrenome alterado"))
            .body("dataNascimento", is("12-11-2004"))
            .body("email", is("emailalterado@gmail.com"))
            .body("telefone", is("1698888-8888"));
    }

    //DELETE
    @Order(10)
    @Test
    public void givenWrongId_WhenDelete_Then400() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/clientes/idInvalido")
            .then()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Cliente não encontrado."));
    }

    @Order(11)
    @Test
    public void givenRightId_WhenDelete_Then200() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/clientes/" + pegaIdCliente("222.222.222-22"))
            .then()
            .assertThat()
            .statusCode(200);
    }
}
