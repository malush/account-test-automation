package com.malush.account.step_definitions;

import com.malush.account.repository.UserRepository;
import com.malush.account.requests.SignUpRequest;
import cucumber.api.java8.En;
import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class LoginSteps implements En {

  private Response response;

  public LoginSteps() {

    Before(() -> {
      UserRepository.getRepository().deleteAll();
    });

    After(() -> {
      response = null;
    });

    When("the user requests to login and have the full access to the system", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(new SignUpRequest("malush", "qwerty123")).
        when().post("/access-tokens");
    });

    Then("the access is granted", () -> {
      response.then().statusCode(HttpStatus.SC_OK);
      assertNotNull(response.jsonPath().get("access_token"));
    });

    When("a user successfully signs up", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(new SignUpRequest("malush", "qwerty123")).
        when().
          post("/sign-up").
        then().
          statusCode(HttpStatus.SC_CREATED).extract().response();
    });

    Then("the user is immediately granted access to the system", () -> {
      assertNotNull(response.jsonPath().get("access_token"));
    });

    Then("the access is forbidden", () -> {
      response.then().statusCode(HttpStatus.SC_FORBIDDEN);
    });

    When("the user requests to login with a wrong password", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(new SignUpRequest("malush", "wrongPassword")).
        when().
          post("/access_token");
    });

    When("the user requests to login with missing {string}", (String inputData) -> {
      SignUpRequest signUpRequest;
      if(inputData.equals("name"))
        signUpRequest = new SignUpRequest(null, "qwerty123");
      else if (inputData.equals("password")){
        signUpRequest = new SignUpRequest("malush", null);
      } else {
        signUpRequest = new SignUpRequest(null, null);
      }
      response =
        given().
          contentType(ContentType.JSON).
          body(signUpRequest).
        when().post("/access_token");
    });

  }
}
