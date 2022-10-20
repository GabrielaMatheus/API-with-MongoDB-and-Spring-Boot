package projeto.locadora.locadora;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import io.restassured.http.ContentType;
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
import projeto.locadora.locadora.controller.form.CarroForm;
import projeto.locadora.locadora.model.Carro;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MongoDBConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
public class CarroControllerTest {

    public String pegaIdCarro(String placa) {
        return given().contentType(ContentType.JSON).when().get("/carros/" + placa).then().extract().path("id");
    }

    //POST
    @Order(1)
    @Test
    public void givenNoRequiredField_WhenPost_Then400() throws Exception {
        CarroForm carroForm = new CarroForm();
        carroForm.setMarca("marca teste");
        carroForm.setModelo("modelo teste");
        carroForm.setAno(1231);
        carroForm.setCor("cor teste");
        carroForm.setValor(700000.99);

        given()
            .body(carroForm)
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
        CarroForm carroForm = new CarroForm();
        carroForm.setPlaca("GGG456");
        carroForm.setMarca("marca teste");
        carroForm.setModelo("modelo teste");
        carroForm.setAno(1231);
        carroForm.setCor("cor teste");
        carroForm.setValor(700000.99);

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

    @Order(3)
    @Test
    public void givenAlreadyExistsRequiredField_WhenPost_Then400() throws Exception {
        CarroForm carroForm = new CarroForm();
        carroForm.setPlaca("GGG456");
        carroForm.setMarca("marca teste");
        carroForm.setModelo("modelo teste");
        carroForm.setAno(1231);
        carroForm.setCor("cor teste");
        carroForm.setValor(700000.99);

        given()
            .body(carroForm)
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
            .body("erro", is("Placa não encontrada"));
    }

    //PUT

    @Order(7)
    @Test
    public void givenNoRequiredField_WhenPut_Then400() throws Exception {
        CarroForm carroForm = new CarroForm();
        carroForm.setId("");
        carroForm.setPlaca("GGG456");
        carroForm.setMarca("marca teste");
        carroForm.setModelo("modelo teste");
        carroForm.setAno(1996L);
        carroForm.setCor("cor teste");
        carroForm.setValor(200000.23);

        given()
            .body(carroForm)
            .contentType(ContentType.JSON)
            .when()
            .put("/carros")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Carro não encontrado."));
        //aqui era pra retornar que nao encontrou o id
    }

    @Order(8)
    @Test
    public void givenWrongId_whenPut_then400() throws Exception {
        CarroForm carroForm = new CarroForm();
        carroForm.setId("wrongId111222333");
        carroForm.setPlaca("GGG45678");
        carroForm.setMarca("marca teste");
        carroForm.setModelo("modelo teste");
        carroForm.setAno(1996L);
        carroForm.setCor("cor teste");
        carroForm.setValor(200000.23);

        given()
            .body(carroForm)
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

    //AQUI
    @Order(9)
    @Test
    public void givenDataWithRequiredField_WhenPut_Then200() throws Exception {
        CarroForm carroForm = new CarroForm();
        carroForm.setId(pegaIdCarro("GGG456"));
        carroForm.setPlaca("GGG45678");
        carroForm.setMarca("marca atualizada");
        carroForm.setModelo("modelo atualizado");
        carroForm.setAno(1987L);
        carroForm.setCor("cor atualizada");
        carroForm.setValor(38999.99);

        given()
            .body(carroForm)
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
