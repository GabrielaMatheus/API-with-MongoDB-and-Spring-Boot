package projeto.locadora.locadora;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import projeto.locadora.locadora.model.Acessorio;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AcessorioControllerTest {

    public Object pegaIdAcessorio(String docParam) {
        return given().contentType(ContentType.JSON).when().get("/acessorios/" + docParam).then().extract().path("id");
    }

    //POST
    @Order(1)
    @org.junit.jupiter.api.Test
    public void givenNoRequiredField_WhenPost_Then400() throws Exception {
        given()
            .body("{\"nome\":\"acessorio teste\",\"valor\":89.90}\"")
            .contentType(ContentType.JSON)
            .when()
            .post("/acessorios")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("doc"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(2)
    @org.junit.jupiter.api.Test
    public void givenRequiredField_WhenPost_Then200() throws Exception {
        given()
            .body("{\"nome\":\"acessorio teste\",\"doc\":\"numDoc4987\",\"valor\":89.90}\"")
            .contentType(ContentType.JSON)
            .when()
            .post("/acessorios")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200);
    }

    @Order(3)
    @org.junit.jupiter.api.Test
    public void givenAlreadyExistsRequiredField_WhenPost_Then400() throws Exception {
        given()
            .body("{\"nome\":\"acessorio teste\",\"doc\":\"numDoc4987\",\"valor\":89.90}\"")
            .contentType(ContentType.JSON)
            .when()
            .post("/acessorios")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Acessório com esse doc já existente. "));
    }

    //GET
    @Order(4)
    @org.junit.jupiter.api.Test
    public void whenGet_Then200() throws Exception {
        given().when().get("/acessorios").then().assertThat().statusCode(200).and().log().all();
    }

    @Order(5)
    @org.junit.jupiter.api.Test
    public void givenUniqueDoc_whenGet_Then200() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/acessorios/numDoc4987")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("doc", is("numDoc4987"))
            .body("nome", is("acessorio teste"))
            .body("valor", is(89.9F));
    }

    @Order(6)
    @org.junit.jupiter.api.Test
    public void givenWrongDoc_whenGet_Then400() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/acessorios/wrongDoc")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Acessório não encontrado"));
    }

    //PUT

    @Order(7)
    @org.junit.jupiter.api.Test
    public void givenNoRequiredField_WhenPut_Then400() throws Exception {
        given()
            .body(
                "{\"id\":  \"" +
                pegaIdAcessorio("numDoc4987") +
                "\", \"nome\": \"teste não passando o doc\", \"valor\":56.00}"
            )
            .contentType(ContentType.JSON)
            .when()
            .put("/acessorios")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("campo", hasItems("doc"))
            .body("erro", hasItems("não deve estar em branco"));
    }

    @Order(8)
    @org.junit.jupiter.api.Test
    public void givenWrongId_whenPut_then400() throws Exception {
        Acessorio acessorio = new Acessorio("wrongId111222333", "doc345", "teste", 2.6);

        given()
            .body(acessorio)
            .contentType(ContentType.JSON)
            .when()
            .put("/acessorios")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Acessório não encontrado."));
    }

    @Order(9)
    @org.junit.jupiter.api.Test
    public void givenDataWithRequiredField_WhenPut_Then200() throws Exception {
        Acessorio acessorio = new Acessorio(
            (String) pegaIdAcessorio("numDoc4987"),
            "numDoc4987845",
            "teste atualizado",
            2.6
        );

        given()
            .body(acessorio)
            .contentType(ContentType.JSON)
            .when()
            .put("/acessorios")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(200)
            .body("doc", is("numDoc4987845"))
            .body("nome", is("teste atualizado"))
            .body("valor", is(2.6F));
    }

    //DELETE
    @Order(10)
    @org.junit.jupiter.api.Test
    public void givenWrongId_WhenDelete_Then400() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/acessorios/idInvalido")
            .then()
            .assertThat()
            .statusCode(400)
            .body("erro", is("Acessório não encontrado."));
    }

    @Order(11)
    @org.junit.jupiter.api.Test
    public void givenRightId_WhenDelete_Then200() throws Exception {
        given()
            .contentType(ContentType.JSON)
            .when()
            .delete("/acessorios/" + pegaIdAcessorio("numDoc4987845"))
            .then()
            .assertThat()
            .statusCode(200);
    }
}
