package com.malush.account.step_definitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.malush.account.repository.AccountRepository;
import com.malush.account.repository.UserRepository;
import com.malush.account.requests.ApiPath;
import com.malush.account.requests.CreateAccountRequest;
import com.malush.account.requests.CreateAccountRequest.SupportedCurrencies;
import com.malush.account.requests.SignUpRequest;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Currency;
import java.util.UUID;
import org.apache.http.HttpStatus;

public class AccountSteps implements En {

  private Response response;
  private String accessToken;
  private CreateAccountRequest createAccountRequest;

  public AccountSteps() {

    Before(() -> {
      AccountRepository.getRepository().deleteAll();
      UserRepository.getRepository().deleteAll();
    });

    After(() -> {
      response = null;
      accessToken = null;
      createAccountRequest = null;
    });

    Given("the user is logged in", () -> {
      accessToken =
        given().
          contentType(ContentType.JSON).
          body(new SignUpRequest("malush", "qwerty123")).
        when().
          post(ApiPath.SIGN_UP).
        then().
          statusCode(HttpStatus.SC_CREATED).
          extract().jsonPath().get("access_token");
    });

    Given("the user inserts account details: {string} and {string}", (String nameOnAccount, String currency) -> {
      createAccountRequest = createAccountRequestBody(nameOnAccount, currency);
    });

    When("the user tries to create a new account", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
          body(createAccountRequest).
        when().
          post(ApiPath.ACCOUNTS);
    });

    Then("the new account is successfully created", () -> {
      response.then().statusCode(HttpStatus.SC_CREATED);
    });

    Then("the account creation fails with Bad Request response", () -> {
      response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
      assertThat(response.jsonPath().get("error"), is("Bad Request"));
    });

    When("the user tries to create a new account without providing access token", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          body(createAccountRequestBody("test", "EUR")).
        when().
          post(ApiPath.ACCOUNTS);
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
          body(createAccountRequestBody("test", "EUR")).
        when().
          post(ApiPath.ACCOUNTS);
    });

    Given("the account with account details: {string} and {string} already exists", (String nameOnAccount, String currencyId) -> {
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
          body(createAccountRequestBody(nameOnAccount, currencyId)).
        when().
          post(ApiPath.ACCOUNTS);
    });

    Then("the account creation fails with the response indicating the conflict", () -> {
      response.then().statusCode(HttpStatus.SC_CONFLICT);
    });

    When("the user tries to add a new account for one of the supported currencies: {string}", (String supportedCurrency) -> {
      // need to add a random value since the system doesn't support a single account with multiple currencies
      String nameOnAccount = UUID.randomUUID().toString();
      response =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
          body(new CreateAccountRequest(nameOnAccount, Currency.getInstance(supportedCurrency).getCurrencyCode())).
        when().
          post("/accounts");
    });
  }

  private CreateAccountRequest createAccountRequestBody(String nameOnAccount, String currency){
    return new CreateAccountRequest(
        nameOnAccount.equals("null") ? null : nameOnAccount,
        currency.equals("null") ? null : currency
    );
  }
}
