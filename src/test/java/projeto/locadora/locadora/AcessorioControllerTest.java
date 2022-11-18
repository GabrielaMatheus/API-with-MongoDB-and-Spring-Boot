package projeto.locadora.locadora;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import projeto.locadora.locadora.controller.form.AcessorioForm;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(MongoDBConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext //diz que as configurações de test estão em outra classe
public class AcessorioControllerTest {

    public String pegaIdAcessorio(String docParam) {
        return given().contentType(ContentType.JSON).when().get("/acessorios/filtrar/" + docParam).then().extract().path("id");
    }

    //POST
    @Order(1)
    @org.junit.jupiter.api.Test
    public void givenNoRequiredFieldDoc_WhenPost_Then400() throws Exception {
        AcessorioForm acessorioForm = new AcessorioForm();
        acessorioForm.setNome("acessorio teste");
        acessorioForm.setValor(89.90);

        given()
            .body(acessorioForm)
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
        AcessorioForm acessorioForm = new AcessorioForm();
        acessorioForm.setDoc("numDoc4987");
        acessorioForm.setNome("acessorio teste");
        acessorioForm.setValor(89.90);

        given()
            .body(acessorioForm)
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
        AcessorioForm acessorioForm = new AcessorioForm();
        acessorioForm.setDoc("numDoc4987");
        acessorioForm.setNome("acessorio teste");
        acessorioForm.setValor(89.90);

        given()
            .body(acessorioForm)
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
            .get("/acessorios/"+ pegaIdAcessorio("numDoc4987"))
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
    public void givenNoRequiredFieldDoc_WhenPut_Then405() throws Exception {
        AcessorioForm acessorioForm = new AcessorioForm();
        acessorioForm.setNome("acessorio teste");
        acessorioForm.setValor(56.00);

        given()
            .body(acessorioForm)
            .contentType(ContentType.JSON)
            .when()
            .put("/acessorios")
            .then()
            .log()
            .all()
            .assertThat()
            .statusCode(405);
    }

    @Order(8)
    @org.junit.jupiter.api.Test
    public void givenWrongId_whenPut_then400() throws Exception {
        AcessorioForm acessorioForm = new AcessorioForm();
        acessorioForm.setDoc("numDoc4987");
        acessorioForm.setNome("acessorio teste");
        acessorioForm.setValor(89.90);

        given()
            .body(acessorioForm)
            .contentType(ContentType.JSON)
            .when()
            .put("/acessorios/" + pegaIdAcessorio("wrongId111222333"))
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
        AcessorioForm acessorioForm = new AcessorioForm();
        acessorioForm.setDoc("numDoc4987845");
        acessorioForm.setNome("teste atualizado");
        acessorioForm.setValor(2.6);

        given()
            .body(acessorioForm)
            .contentType(ContentType.JSON)
            .when()
            .put("/acessorios/"+pegaIdAcessorio("numDoc4987"))
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
