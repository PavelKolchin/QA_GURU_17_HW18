package guru_qa.test;

import guru_qa.models.lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru_qa.specs.ReqresSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class ReqresTests {

    @Test
    @DisplayName("Successful login check")
    void successfulLoginTest() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setEmail("eve.holt@reqres.in");
        loginData.setPassword("cityslicka");

        LoginResponseModel response = step("Get authorization data", () ->
                given(reqresRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/login")
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Get authorization response", () -> {
            assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        });
    }

    @Test
    @DisplayName("Unsuccessful registration check")
    void unsuccessfulRegistrationTest() {

        RegistrationBodyModel regData = new RegistrationBodyModel();
        regData.setEmail("sydney@fife");

        RegistrationResponseModel regResponse = step("Get registration data", () ->
                given(reqresRequestSpec)
                        .body(regData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(regResponseSpec)
                        .extract().as(RegistrationResponseModel.class));

        step("Get registration response", () -> {
            assertThat(regResponse.getError()).isEqualTo("Missing password");
        });
    }

    @Test
    @DisplayName("Single user first name check")
    void singleUserDataTest() {

        SingleUserResponseModel userResponse = step("Get check data", () ->
                given(reqresRequestSpec)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(singleUserResponseSpec)
                        .extract().as(SingleUserResponseModel.class));

        step("Get check response", () -> {
            assertThat(userResponse.getData().getFirstName()).isEqualTo("Janet");
        });
    }

    @Test
    @DisplayName("Delayed response check")
    void delayedResponseTest() {

        DelayedUserResponseModel delayResponse = step("Get check data", () ->
                given(reqresRequestSpec)
                        .when()
                        .get("/users?delay=3")
                        .then()
                        .spec(delayUserResponseSpec)
                        .extract().as(DelayedUserResponseModel.class));

        step("Get check response", () -> {
            assertThat(delayResponse.getData().get(3).getFirstName()).isEqualTo("Eve");
        });
    }

    @Test
    @DisplayName("Delete user check")
    void deleteTest() {

        step("Get check data", () ->
                given(reqresRequestSpec)
                        .when()
                        .delete("/users/2")
                        .then()
                        .spec(deleteUserResponseSpec));
    }

}
