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
import org.springframework.boot.test.context.SpringBootTest;
import projeto.locadora.locadora.model.Cliente;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        given()
            .body(
                "{ \"nome\": \"nome teste\"," +
                "\"sobrenome\": \"sobrenome teste\", " +
                "\"dataNascimento\": \"20-09-2002\"," +
                "\"email\": \"emailteste@gmail.com\"," +
                "\"telefone\": \"1699999-9999\"}"
            )
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
        given()
            .body(
                "{  \"cpf\": \"123.123.123-12\"," +
                "\"nome\": \"nome teste\"," +
                "\"sobrenome\": \"sobrenome teste\", " +
                "\"dataNascimento\": \"20-09-2002\"," +
                "\"email\": \"emailteste@gmail.com\"," +
                "\"telefone\": \"1699999-9999\"}"
            )
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
        given()
            .body(
                "{  \"cpf\": \"123.123.123-12\"," +
                "\"nome\": \"nome teste\"," +
                "\"sobrenome\": \"sobrenome teste\", " +
                "\"dataNascimento\": \"20-09-2002\"," +
                "\"email\": \"emailteste@gmail.com\"," +
                "\"telefone\": \"1699999-9999\"}"
            )
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
        Cliente cliente = new Cliente(
            "",
            "123.123.123-12",
            "Nome teste",
            "Sobrenome teste",
            createDate("12-11-2004"),
            "emailteste@gmail.com",
            "1699999-9999"
        );

        given()
            .body(cliente)
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
        Cliente cliente = new Cliente(
            "wrongIdTest",
            "123.123.123-12",
            "Nome teste",
            "Sobrenome teste",
            createDate("12-11-2004"),
            "emailteste@gmail.com",
            "1699999-9999"
        );

        given()
            .body(cliente)
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
        Cliente cliente = new Cliente(
            pegaIdCliente("123.123.123-12"),
            "222.222.222-22",
            "nome alterado",
            "sobrenome alterado",
            createDate("12-11-2004"),
            "emailalterado@gmail.com",
            "1698888-8888"
        );

        given()
            .body(cliente)
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
