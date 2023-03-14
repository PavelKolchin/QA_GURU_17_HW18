package test;

import io.qameta.allure.restassured.AllureRestAssured;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.pojo.LoginBodyPojoModel;
import models.pojo.LoginResponsePojoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class RegresTests {

    @Test
    @DisplayName("Successful login check")
    void successfulLoginTest() {

        LoginBodyLombokModel data = new LoginBodyLombokModel();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Get authorization data", () ->
                given()
                        .log().all()
                        .filter(withCustomTemplates())
                        .contentType(JSON)
                        .body(data)
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class));
        step("Get authorization response", () -> {
            assertThat(response.getToken()).isNotNull();
        });
    }

    @Test
    @DisplayName("Unsuccessful registration check")
    void unsuccessfulRegistrationTest() {

        String data = "{ \"email\": \"sydney@fife\" }";

        given()
                .log().all()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Single user first name check")
    void singleUserDataTest() {

        given()
                .log().all()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.first_name", is("Janet"));
    }

    @Test
    @DisplayName("Delayed response check")
    void delayedResponseTest() {

        given()
                .log().all()
                .when()
                .get("https://reqres.in/api/users?delay=3")
                .then()
                .log().all()
                .statusCode(200)
                .body("data[3].first_name", is("Eve"));
    }

    @Test
    @DisplayName("Delete user check")
    void deleteTest() {

        given()
                .log().all()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .statusCode(204);
    }

}
