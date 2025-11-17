package org.nbd;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestIntegrationRentTest {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

    }

    @Test
    void testCreateRent() {
        LocalDate startDate = LocalDate.of(2025, 11, 18);
        String login = RandomStringUtils.randomAlphanumeric(3, 31);
        String houseJson = """
                {
                      "houseNumber": "C14",
                      "price": 550.0,
                      "area": 110.0
                }
                """;
        String clientJson = String.format("""
                {
                    "login": "%s",
                    "firstName": "Kamil",
                    "lastName": "Kowal",
                    "phoneNumber": "555555556",
                    "clientType": 1
                }
                """, login);
        RestAssured.basePath = "/houses";

        String houseId = given()
                .contentType(ContentType.JSON)
                .body(houseJson)
                .when()
                .post()
                .then()
                .extract()
                .path("id");

        RestAssured.basePath = "/clients";
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
                .patch("/{id}/activate");


        RestAssured.basePath = "/rents";
        given()
                .contentType(ContentType.JSON)
                .queryParam("client", clientId)
                .queryParam("house", houseId)
                .queryParam("startTime", startDate.toString())
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("client.id", equalTo(clientId))
                .body("house.id", equalTo(houseId))
                .body("startDate", equalTo(startDate.toString()));
    }

    @Test
    void testCreateRentOnNotAvailableHouse() {
        LocalDate startDate = LocalDate.of(2025, 11, 18);
        String login = RandomStringUtils.randomAlphanumeric(3, 31);
        String houseJson = """
                {
                      "houseNumber": "C14",
                      "price": 550.0,
                      "area": 110.0
                }
                """;
        String client1Json = String.format("""
                {
                    "login": "%s",
                    "firstName": "Kamil",
                    "lastName": "Kowal",
                    "phoneNumber": "555555556",
                    "clientType": 1
                }
                """, login);
        login = RandomStringUtils.randomAlphanumeric(3, 31);
        String client2Json = String.format("""
                {
                    "login": "%s",
                    "firstName": "Kowal",
                    "lastName": "Kamil",
                    "phoneNumber": "555555556",
                    "clientType": 1
                }
                """, login);
        RestAssured.basePath = "/houses";

        String houseId = given()
                .contentType(ContentType.JSON)
                .body(houseJson)
                .when()
                .post()
                .then()
                .extract()
                .path("id");

        RestAssured.basePath = "/clients";

        String client1Id = given()
                .contentType(ContentType.JSON)
                .body(client1Json)
                .when()
                .post()
                .then()
                .extract()
                .path("id");

        String client2Id = given()
                .contentType(ContentType.JSON)
                .body(client2Json)
                .when()
                .post()
                .then()
                .extract()
                .path("id");


        given()
                .contentType(ContentType.JSON)
                .pathParam("id", client1Id)
                .when()
                .patch("/{id}/activate");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", client2Id)
                .when()
                .patch("/{id}/activate");

        RestAssured.basePath = "/rents";
        given()
                .contentType(ContentType.JSON)
                .queryParam("client", client1Id)
                .queryParam("house", houseId)
                .queryParam("startTime", startDate.toString())
                .when()
                .post();

        given()
                .contentType(ContentType.JSON)
                .queryParam("client", client2Id)
                .queryParam("house", houseId)
                .queryParam("startTime", startDate.toString())
                .when()
                .post()
                .then()
                .statusCode(409);
    }
    }

