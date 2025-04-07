package br.com.apiEventos;

import br.com.apiEventos.entitys.Usuario;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testGivenUsuarioEndpoint() {
        given()
                .when().get("/usuarios")
                .then()
                .statusCode(200);
    }

    @Test
    void testGivenEventosEndpoint() {
        given()
                .when().get("/eventos")
                .then()
                .statusCode(200);
    }

}