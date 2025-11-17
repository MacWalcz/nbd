package org.nbd;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestIntegrationHouseTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/houses";
    }

    @Test
    void testCreateHouse() {
        String json = """
                {
                      "houseNumber": "C14",
                      "price": 550.0,
                      "area": 110.0
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("price", equalTo(550.0));

    }
    @Test
    void testReadHouse() {
        String json = """
                {
                      "houseNumber": "B14",
                      "price": 220.0,
                      "area": 80.0
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


        given()
                .pathParam("id", clientId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("houseNumber", equalTo("B14"));

    }

    @Test
    void testUpdateHouse() {
        String json = """
                {
                      "houseNumber": "A14",
                      "price": 110.0,
                      "area": 160.0
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


        String updateJson = """
                {
                      "houseNumber": "A14",
                      "price": 110.0,
                      "area": 60.0
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
                .body("area", equalTo(60.0));

    }



}

