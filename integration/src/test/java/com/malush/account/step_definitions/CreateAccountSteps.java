package com.malush.account.step_definitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.malush.account.requests.ApiPath;
import com.malush.account.requests.CreateAccountRequest;
import com.malush.account.requests.Headers;
import com.malush.account.step_definitions.CommonAccountSteps.AccountType;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import java.util.Currency;
import java.util.UUID;
import org.apache.http.HttpStatus;

public class CreateAccountSteps implements En {

  private CommonAccountSteps common;

  public CreateAccountSteps(CommonAccountSteps common) {

    //PicoContainer injection
    this.common = common;

    When("the user tries to create a new account", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
        header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(common.createAccountRequest).
        when().
          post(ApiPath.ACCOUNTS);
    });

    Then("the new account is successfully created", () -> {
      common.response.then().statusCode(HttpStatus.SC_CREATED);
      common.clientCreditAccountId = common.response.jsonPath().getLong("id");
    });

    Then("the account creation fails with Bad Request response", () -> {
      common.response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
      assertThat(common.response.jsonPath().get("error"), is("Bad Request"));
    });

    When("the user tries to create a new account without providing access token", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
          body(common.createAccountRequestBody("test", "EUR")).
        when().
          post(ApiPath.ACCOUNTS);
    });

    When("the user tries to create a new account but provides an invalid token", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + "wrongtoken").
          body(common.createAccountRequestBody("test", "EUR")).
        when().
          post(ApiPath.ACCOUNTS);
    });

    When("the user tries to add a new account for one of the supported currencies: {string}", (String supportedCurrency) -> {
      // need to add a random value since the system doesn't support a single account with multiple currencies
      String nameOnAccount = UUID.randomUUID().toString();
      common.response =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(new CreateAccountRequest(nameOnAccount, Currency.getInstance(supportedCurrency).getCurrencyCode())).
        when().
          post(ApiPath.ACCOUNTS);
    });

    Given("the request account type value is {string}", (String accountType) -> {
      if(!accountType.isEmpty())
        common.createAccountRequest.setAccountType(accountType);
    });

    Given("the request balance status is {string}", (String balanceStatus) -> {
      if(!balanceStatus.isEmpty())
        common.createAccountRequest.setBalanceStatus(balanceStatus);
    });

    Then("the account creation was {string}", (String result) -> {
      if(common.response.then().extract().statusCode() == HttpStatus.SC_CREATED)
        assertThat(result, is("successful"));
      else
        assertThat(result, is("unsuccessful"));
    });

    When("the user tries to create a new ledger account", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(common.createAccountRequestBody("1000.00", "EUR", AccountType.LEDGER.getValue(), "DR")).
        when().
          post(ApiPath.ACCOUNTS);
    });
  }
}
