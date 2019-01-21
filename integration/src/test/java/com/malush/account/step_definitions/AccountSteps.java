package com.malush.account.step_definitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.malush.account.repository.AccountRepository;
import com.malush.account.repository.UserRepository;
import com.malush.account.requests.CreateAccountRequest;
import com.malush.account.requests.CreateAccountRequest.SupportedCurrencies;
import com.malush.account.requests.SignUpRequest;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class AccountSteps implements En {

  private Response response;
  private String accessToken;
  private CreateAccountRequest createAccountRequest;

  public AccountSteps() {

    Before(() -> {
      AccountRepository.getRepository().deleteAll();
      UserRepository.getRepository().deleteAll();
      createAccountRequest = new CreateAccountRequest("Ivan Malusev", SupportedCurrencies.EUR.getIso4217Code());
    });

    After(() -> {
      response = null;
      accessToken = null;
      createAccountRequest = null;
    });

    And("the user is logged in", () -> {
      accessToken =
        given().
          contentType(ContentType.JSON).
          body(new SignUpRequest("malush", "qwerty123")).
        when().
          post("/access-tokens").
        then().
          extract().jsonPath().get("access_token");
    });

    When("the user tries to create a new account with valid data", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
          body(createAccountRequest).
        when().
          post("/accounts");
    });

    Then("the new account is successfully created", () -> {
      response.then().statusCode(HttpStatus.SC_CREATED);
    });

    When("the user tries to create a new account with missing {string}", (String inputData) -> {
      CreateAccountRequest createAccountRequest;
      if (inputData.equals("nameOnAccount")) {
        createAccountRequest = new CreateAccountRequest(null,
            SupportedCurrencies.EUR.getIso4217Code());
      } else {
        createAccountRequest = new CreateAccountRequest("Ivan Malusev", null);
      }

      response =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
          body(createAccountRequest).
        when().
          post("/accounts");
    });

    Then("the account creation fails with Bad Request response", () -> {
      JsonPath jsonPath = response.then().statusCode(HttpStatus.SC_BAD_REQUEST).extract().jsonPath();
      assertThat(jsonPath.get("error"), is("Bad Request"));
    });

    When("the user tries to create a new account without providing access token", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(createAccountRequest).
        when().
          post("/accounts");
    });

    Then("the access to account resource is forbidden", () -> {
      response.then().statusCode(HttpStatus.SC_FORBIDDEN);
      assertThat(response.jsonPath().get("message"), is("Access Denied"));
    });

    When("the user tries to create a new account but provides an invalid token", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + "wrongtoken").
          body(createAccountRequest).
        when().
          post("/accounts");
    });

    Given("the account already exists in the system", () -> {
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
          body(createAccountRequest).
        when().
          post("/accounts");
    });

    Then("the account creation fails with the response indicating the conflict", () -> {
      response.then().statusCode(HttpStatus.SC_CONFLICT);
    });

  }
}
