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
        RestAssured.baseURI = "http://localhost:8080/nbd/rest/";
        RestAssured.port = 8080;
        RestAssured.basePath = "/users/clients";
    }

    @Test
    void testCreateClient() {
        String login = RandomStringUtils.randomAlphanumeric(3, 31);
        String json = String.format("""
            {
                "login": "%s",
                "firstName": "Jan",
                "lastName": "Kowalski",
                "phoneNumber": "555555555"
            }
            """,login);

        System.out.println(login);
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
                "phoneNumber": "555555556"
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

        System.out.println(clientId);


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
                "phoneNumber": "555555557"
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
                "phoneNumber": "555555557"
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
                "phoneNumber": "555555558"
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
                "phoneNumber": "555555559"
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

    @Test
    void testActivateClient() {
        String login = RandomStringUtils.randomAlphanumeric(3, 31);
        String clientJson = String.format("""
        {
            "login": "%s",
            "firstName": "Marek",
            "lastName": "Kowalski",
            "phoneNumber": "555555560"
        }
        """, login);

        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post()
                .then()
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", clientId)
                .when()
                .patch("/{id}/activate")
                .then()
                .statusCode(200)
                .body("active", equalTo(true));
    }

    @Test
    void testDeactivateClient() {
        String login = RandomStringUtils.randomAlphanumeric(3, 31);
        String clientJson = String.format("""
        {
            "login": "%s",
            "firstName": "Marek",
            "lastName": "Kowalski",
            "phoneNumber": "555555561"
        }
        """, login);

        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post()
                .then()
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", clientId)
                .when()
                .patch("/{id}/activate")
                .then()
                .statusCode(200)
                .body("active", equalTo(true));

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", clientId)
                .when()
                .patch("/{id}/deactivate")
                .then()
                .statusCode(200)
                .body("active", equalTo(false));
    }

    @Test
    void testActivateNonExistingClient() {
        String nonExistingId = "000000000000000000000000";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", nonExistingId)
                .when()
                .patch("/{id}/activate")
                .then()
                .statusCode(404);
    }

    @Test
    void testDeactivateNonExistingClient() {
        String nonExistingId = "000000000000000000000000";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", nonExistingId)
                .when()
                .patch("/{id}/deactivate")
                .then()
                .statusCode(404);
    }

    @Test
    void testReadNonExistingClient() {
        String nonExistingId = "000000000000000000000000";

        given()
                .pathParam("id", nonExistingId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void testUpdateNonExistingClient() {
        String nonExistingId = "000000000000000000000000";

        String updateJson = """
        {
            "login": "nonexistent",
            "firstName": "Non",
            "lastName": "Existent",
            "phoneNumber": "000000000"
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(updateJson)
                .pathParam("id", nonExistingId)
                .when()
                .put("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void testGetClientByLogin_Positive() {

        String login = RandomStringUtils.randomAlphanumeric(5, 15);
        String clientJson = String.format("""
        {
            "login": "%s",
            "firstName": "Anna",
            "lastName": "Nowak",
            "phoneNumber": "555555556"
        }
        """, login);

        given()
                .contentType(ContentType.JSON)
                .body(clientJson)
                .when()
                .post()
                .then()
                .statusCode(200);

        given()
                .pathParam("login", login)
                .when()
                .get("/by-login/{login}")
                .then()
                .statusCode(200)
                .body("login", equalTo(login))
                .body("firstName", equalTo("Anna"));
    }

    @Test
    void testGetClientByLogin_Negative() {
        String nonExistingLogin = "thislogindoesnotexist123";

        given()
                .pathParam("login", nonExistingLogin)
                .when()
                .get("/clients/by-login/{login}")
                .then()
                .statusCode(404);
    }

    @Test
    void testSearchClients_Positive() {
        String login1 = RandomStringUtils.randomAlphanumeric(5, 10);
        String login2 = RandomStringUtils.randomAlphanumeric(5, 10);

        String clientJson1 = String.format("""
        {
            "login": "%s",
            "firstName": "Anna",
            "lastName": "Nowak",
            "phoneNumber": "555111111"
        }
        """, login1);

        String clientJson2 = String.format("""
        {
            "login": "%s",
            "firstName": "Piotr",
            "lastName": "Kowalski",
            "phoneNumber": "555222222"
        }
        """, login2);

        given()
                .contentType(ContentType.JSON)
                .body(clientJson1)
                .when()
                .post()
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body(clientJson2)
                .when()
                .post()
                .then()
                .statusCode(200);

        String partial = login1.substring(1, 4);

        given()
                .queryParam("q", partial)
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("login", hasItem(login1));
    }

}
