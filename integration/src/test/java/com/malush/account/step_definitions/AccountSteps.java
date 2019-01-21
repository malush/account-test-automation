package com.malush.account.step_definitions;

import static io.restassured.RestAssured.given;

import com.malush.account.repository.AccountRepository;
import com.malush.account.repository.UserRepository;
import com.malush.account.requests.CreateAccountRequest;
import com.malush.account.requests.CreateAccountRequest.SupportedCurrencies;
import com.malush.account.requests.SignUpRequest;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

public class AccountSteps implements En {

  private Response response;
  private String accessToken;

  public AccountSteps() {

    Before(() -> {
      AccountRepository.getRepository().deleteAll();
      UserRepository.getRepository().deleteAll();
    });

    After(() -> {
      response = null;
      accessToken = null;
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

    When("the user requests to create a new account with valid data", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
          body(new CreateAccountRequest("Ivan Malusev", SupportedCurrencies.EUR.getIso4217Code())).
        when().
          post("/accounts");
    });

    Then("the new account is successfully created", () -> {
      response.then().statusCode(HttpStatus.SC_CREATED);
    });
  }

}
