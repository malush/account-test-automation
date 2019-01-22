package com.malush.account.step_definitions;

import com.malush.account.requests.ApiPath;
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
  private SignUpRequest signUpRequest;

  public SignUpSteps() {

    Before(() -> {
      UserRepository.getRepository().deleteAll();
    });

    After(() -> {
      response = null;
      signUpRequest = null;
    });

    Given("the user inserts {string} and {string}", (String name, String password) -> {
      signUpRequest = new SignUpRequest(name.equals("null") ? null : name, password.equals("null") ? null : password);
    });

    When("the user tries to register a new profile", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(signUpRequest).
        when().post(ApiPath.SIGN_UP);
    });

    Then("the user sign up is successful", () -> {
      response.then().statusCode(HttpStatus.SC_CREATED);
    });

    Then("the user sign up fails with Bad Request response", () -> {
      JsonPath jsonPath = response.then().statusCode(HttpStatus.SC_BAD_REQUEST).extract().jsonPath();
      assertThat(jsonPath.get("error"), is("Bad Request"));
    });

    Given("the user with {string} and {string} already exists in the system", (String name, String password) ->
    {
      signUpRequest = new SignUpRequest(name.equals("null") ? null : name, password.equals("null") ? null : password);
      given().
        contentType(ContentType.JSON).
        body(signUpRequest).log().all().
      when().post(ApiPath.SIGN_UP).
      then().statusCode(HttpStatus.SC_CREATED).log().all();
    });

    Then("the user sign up fails with response indicating the conflict", () -> {
      response.then().statusCode(HttpStatus.SC_CONFLICT);
    });
  }
}
