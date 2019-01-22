package com.malush.account.step_definitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.malush.account.repository.AccountRepository;
import com.malush.account.repository.UserRepository;
import com.malush.account.requests.ApiPath;
import com.malush.account.requests.CreateAccountRequest;
import com.malush.account.requests.SignUpRequest;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;
import org.apache.http.HttpStatus;

public class AccountSteps implements En {

  private Response response;
  private String accessToken;
  private CreateAccountRequest createAccountRequest;
  private long accountId;

  public AccountSteps() {

    Before(() -> {
      AccountRepository.getRepository().deleteAll();
      UserRepository.getRepository().deleteAll();
    });

    After(() -> {
      response = null;
      accessToken = null;
      createAccountRequest = null;
      accountId = 0L;
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
      accountId = response.jsonPath().getLong("id");
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
      accountId =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
          body(createAccountRequestBody(nameOnAccount, currencyId)).
        when().
          post(ApiPath.ACCOUNTS).jsonPath().getLong("id");
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

    When("the user tries to get the account", () -> {
      response =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
        when().
          get(ApiPath.ACCOUNTS + "/" + accountId);
    });

    Then("the users account is successfully retrieved", () -> {
      response.then().statusCode(HttpStatus.SC_OK);
    });

    Then("the account has the following details: {string} and {string}", (String nameOnAccount, String currencyId) -> {
      JsonPath jsonPath =  response.then().extract().jsonPath();
      assertThat(jsonPath.get("nameOnAccount"), is(nameOnAccount));
      assertThat(jsonPath.get("currencyId"), is(currencyId));
    });

    Then("the account response contains all required fields", () -> {
      response.
        then().
          body("$", hasKey("id")).
          body("$", hasKey("currencyId")).
          body("$", hasKey("balance")).
          body("$", hasKey("balanceStatus")).
          body("$", hasKey("balanceTimestamp")).
          body("$", hasKey("dateOpened")).
          body("$", hasKey("accountType")).
          body("$", hasKey("nameOnAccount"));
    });

    Given("the request account balance value is {string}", (String balance) -> {
      if(!balance.equals("missing"))
        createAccountRequest.setBalance(new BigDecimal(balance));
    });

    Then("the account balance is {string}", (String balance) -> {
      JsonPath jsonPath =
        given().
          contentType(ContentType.JSON).
          header("X-access-token", "Bearer " + accessToken).
        when().
          get(ApiPath.ACCOUNTS + "/" + accountId).
        then().
          statusCode(HttpStatus.SC_OK).extract().jsonPath();

      if(balance.equals("missing")) {
        assertThat(jsonPath.get("balance"), is("0.00"));
      } else {
        assertThat(jsonPath.get("balance"), is(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_DOWN).toString()));
      }
    });
  }

  private CreateAccountRequest createAccountRequestBody(String nameOnAccount, String currency){
    return new CreateAccountRequest(
        nameOnAccount.equals("null") ? null : nameOnAccount,
        currency.equals("null") ? null : currency
    );
  }
}
