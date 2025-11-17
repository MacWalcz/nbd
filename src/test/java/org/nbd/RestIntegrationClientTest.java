package org.nbd;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
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

    @Test
    void testCreateClient() {
        String login = RandomStringUtils.randomAlphanumeric(3, 31);
        String json = String.format("""
            {
                "login": "%s",
                "firstName": "Jan",
                "lastName": "Kowalski",
                "phoneNumber": "555555555",
                "clientType": 1
            }
            """,login);

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("login", equalTo(login))
                .body("firstName", equalTo("Jan"));
    }

    @Test
    void testReadClient() {
        String login = RandomStringUtils.randomAlphanumeric(3, 31);

        String json = String.format("""
            {
                "login": "%s",
                "firstName": "Anna",
                "lastName": "Nowak",
                "phoneNumber": "555555556",
                "active": true,
                "clientType": 1
            }
            """,login);

        String clientId = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .extract()
                .path("id");


        given()
                .pathParam("id", clientId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("login", equalTo(login))
                .body("firstName", equalTo("Anna"));
    }

    @Test
    void testUpdateClient() {
        String login = RandomStringUtils.randomAlphanumeric(3, 31);
        String json = String.format("""
            {
                "login": "%s",
                "firstName": "Piotr",
                "lastName": "Zielinski",
                "phoneNumber": "555555557",
                "active": true,
                "clientType": 1
            }
            """,login);

        String clientId = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .extract()
                .path("id");


        String updateJson = String.format("""
            {
                "login": "%s",
                "firstName": "Piotr",
                "lastName": "Kowalski",
                "phoneNumber": "555555557",
                "active": false,
                "clientType": 1
            }
            """,login);

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


    @Test
    void testCreateClientInvalidLogin() {

        String json = """
            {
                "login": "ab",
                "firstName": "Jan",
                "lastName": "Kowalski",
                "phoneNumber": "555555558",
                "clientType": 1
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
        String login = RandomStringUtils.randomAlphanumeric(3, 31);
        String json = String.format("""
            {
                "login": "%s",
                "firstName": "Adam",
                "lastName": "Nowak",
                "phoneNumber": "555555559",
                 "clientType": 1
            }
            """,login);


        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(200);


        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(409);
    }


}
