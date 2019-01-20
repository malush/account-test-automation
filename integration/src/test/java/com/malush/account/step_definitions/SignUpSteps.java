package com.malush.account.step_definitions;

import com.malush.account.requests.SignUpRequest;
import com.malush.account.repository.UserRepository;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import static io.restassured.RestAssured.*;

public class SignUpSteps implements En {

  private Response response;

  public SignUpSteps() {

    After(() -> {
      UserRepository.getRepository().deleteAll();
      response = null;
    });

    When("a user tries to register a new profile with valid data", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(new SignUpRequest("malush", "qwerty123")).
        when().post("/sign-up");
    });

    Then("the user sign up is successful", () -> {
      response.then().statusCode(HttpStatus.SC_CREATED);
    });

    When("the user tries to register a new profile with missing {string}", (String inputData) -> {
      SignUpRequest signUpRequest;
      if(inputData.equals("name"))
        signUpRequest = new SignUpRequest("", "qwerty123");
      else {
        signUpRequest = new SignUpRequest("malush", "");
      }
      response =
          given().
            contentType(ContentType.JSON).
            body(signUpRequest).
          when().post("/sign-up");
    });

    Then("the user sign up fails with Bad Request response", () -> {
      JsonPath jsonPath = response.then().statusCode(HttpStatus.SC_BAD_REQUEST).extract().jsonPath();
      assertThat(jsonPath.get("error"), is("Bad Request"));
    });

    Given("the user already exists in the system", () ->
    {
      given().
        contentType(ContentType.JSON).
        body(new SignUpRequest("malush", "qwerty123")).
      when().post("/sign-up").
      then().statusCode(HttpStatus.SC_CREATED);
    });

    Then("the user sign up fails with response indicating the conflict", () -> {
      response.then().statusCode(HttpStatus.SC_CONFLICT);
    });

  }
}
