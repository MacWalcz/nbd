package org.nbd;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestIntegrationClientTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/clients";
    }

    // ---------------------------
    // Testy pozytywne
    // ---------------------------

    @Test
    void testCreateClient() {
        String json = """
            {
                "login": "tdsad",
                "firstName": "Jan",
                "lastName": "Kowalski",
                "phoneNumber": "555555555",
                "clientType": 1
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("login", equalTo("tdsad"))
                .body("firstName", equalTo("Jan"));
    }

    @Test
    void testReadClient() {
        // Najpierw utwórz klienta
        String json = """
            {
                "login": "readuser2",
                "firstName": "Anna",
                "lastName": "Nowak",
                "phoneNumber": "555555556",
                "active": true,
                "clientType": 1
            }
            """;

        String clientId = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .extract()
                .path("id");

        // Pobierz klienta
        given()
                .pathParam("id", clientId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("login", equalTo("readuser2"))
                .body("firstName", equalTo("Anna"));
    }

    @Test
    void testUpdateClient() {
        // Tworzenie klienta
        String json = """
            {
                "login": "updateuser",
                "firstName": "Piotr",
                "lastName": "Zielinski",
                "phoneNumber": "555555557",
                "active": true,
                "clientType": 1
            }
            """;

        String clientId = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .extract()
                .path("id");

        // Aktualizacja
        String updateJson = """
            {
                "login": "updateuser",
                "firstName": "Piotr",
                "lastName": "Kowalski",
                "phoneNumber": "555555557",
                "active": false,
                "clientType": 1
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updateJson)
                .pathParam("id", clientId)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .body("lastName", equalTo("Kowalski"))
                .body("active", equalTo(false));
    }

    // ---------------------------
    // Testy negatywne
    // ---------------------------

    @Test
    void testCreateClientInvalidLogin() {
        // Login za krótki
        String json = """
            {
                "login": "ab",
                "firstName": "Jan",
                "lastName": "Kowalski",
                "phoneNumber": "555555558",
                "active": true,
                "clientType": {
                    "type": "default",
                    "discount": 1.0
                }
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void testDuplicateLogin() {
        String json = """
            {
                "login": "duplicateuser",
                "firstName": "Adam",
                "lastName": "Nowak",
                "phoneNumber": "555555559",
                "active": true,
                "clientType": {
                    "type": "default",
                    "discount": 1.0
                }
            }
            """;

        // pierwszy insert
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(200);

        // drugi insert z tym samym loginem
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(409); // zakładamy, że masz handler dla DuplicateKeyException -> 409
    }

    // Tutaj możesz dodać testy dla zasobów (House / Rent) analogicznie
}
