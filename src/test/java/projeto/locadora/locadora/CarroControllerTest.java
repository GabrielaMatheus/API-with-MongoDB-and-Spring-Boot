package projeto.locadora.locadora;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import projeto.locadora.locadora.model.Acessorio;
import projeto.locadora.locadora.model.Carro;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarroControllerTest {

    public Object pegaIdCarro(String placa) {
        return given().contentType(ContentType.JSON).when().get("/carros/" + placa).then().extract().path("id");
    }

    //POST
    @Order(1)
    @Test
    public void givenNoRequiredField_WhenPost_Then400() throws Exception {
        given()
            .body(
                "{\"marca\": \"marca teste\", \"modelo\": \"modelo\", \"ano\": 1231, \"cor\": \"rosa\", \"valor\": 700000.99}"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/carros")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("placa"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(2)
    @Test
    public void givenRequiredField_WhenPost_Then200() throws Exception {
        given()
            .body(
                "{\"placa\":\"GGG456\",\"marca\": \"marca teste\", \"modelo\": \"modelo teste\", \"ano\": 1231, \"cor\": \"cor teste\", \"valor\": 700000.99 }"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/carros")
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
                "{\"placa\":\"GGG456\"," +
                "\"marca\": \"marca teste\"," +
                " \"modelo\": \"modelo teste\"," +
                " \"ano\": 1231, " +
                "\"cor\": \"cor teste\"," +
                " \"valor\": 700000.99 }"
            )
            .contentType(ContentType.JSON)
            .when()
            .post("/carros")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Placa já cadastrada."));
    }

    //GET
    @Order(4)
    @Test
    public void whenGet_Then200() throws Exception {
        given().when().get("/carros").then().assertThat().statusCode(200).and().log().all();
    }

    @Order(5)
    @Test
    public void givenPlaca_whenGet_Then200() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/carros/GGG456")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("placa", is("GGG456"))
            .body("marca", is("marca teste"))
            .body("modelo", is("modelo teste"))
            .body("cor", is("cor teste"))
            .body("valor", is(700000.99F));
    }

    @Order(6)
    @Test
    public void givenWrongPlaca_whenGet_Then400() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/carros/wrongDoc")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Carro não encontrado"));
    }

    //PUT

    @Order(7)
    @Test
    public void givenNoRequiredField_WhenPut_Then400() throws Exception {
        given()
            .body(
                "{\"id\":  \"" +
                pegaIdCarro("GGG456") +
                "\", " +
                "\"marca\": \"marca teste nao passando o id do carro\", " +
                "\"modelo\": \"modelo teste\"}"
            )
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
        Carro carro = new Carro(
            "wrongId111222333",
            "GGG456",
            "marca teste",
            "modelo teste",
            1987L,
            "cor teste",
            9999.99
        );

        given()
            .body(carro)
            .contentType(ContentType.JSON)
            .when()
            .put("/carros")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Carro não encontrado."));
    }

    @Order(9)
    @Test
    public void givenDataWithRequiredField_WhenPut_Then200() throws Exception {
        Carro carro = new Carro(
            (String) pegaIdCarro("GGG456"),
            "GGG45678",
            "marca atualizada",
            "modelo atualizado",
            1987L,
            "cor atualizada",
            38999.99
        );

        given()
            .body(carro)
            .contentType(ContentType.JSON)
            .when()
            .put("/carros")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("placa", is("GGG45678"))
            .body("marca", is("marca atualizada"))
            .body("modelo", is("modelo atualizado"))
            .body("ano", is(1987))
            .body("cor", is("cor atualizada"))
            .body("valor", is(38999.99F));
    }

    //DELETE
    @Order(10)
    @Test
    public void givenWrongId_WhenDelete_Then400() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/carros/idInvalido")
            .then()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Carro não encontrado."));
    }

    @Order(11)
    @Test
    public void givenRightId_WhenDelete_Then200() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/carros/" + pegaIdCarro("GGG45678"))
            .then()
            .assertThat()
            .statusCode(200);
    }
}
