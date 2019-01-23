package com.malush.account.step_definitions;

import com.malush.account.requests.ApiPath;
import com.malush.account.requests.CreateAccountRequest;
import com.malush.account.requests.Headers;
import com.malush.account.requests.LoadAccountRequest;
import cucumber.api.java8.En;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import java.math.BigDecimal;
import org.apache.http.HttpStatus;
import static org.junit.Assert.*;

public class LoadAccountSteps implements En {

  private CommonAccountSteps common;

  private LoadAccountRequest loadAccountRequest;
  private long debitAccountId;
  private long creditAccountId;

  public LoadAccountSteps(CommonAccountSteps common) {

    //PicoContainer injection
    this.common = common;

    After(() -> {
      loadAccountRequest = null;
      debitAccountId = 0L;
      creditAccountId = 0L;
    });

    Given("the debit account exists with {string} {string}", (String balance, String currencyId) -> {
      debitAccountId =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(createAccountRequestBody(balance, currencyId, TestAccountType.DEBIT)).
        when().
          post(ApiPath.ACCOUNTS).
        then().
          statusCode(HttpStatus.SC_CREATED).extract().jsonPath().getLong("id");
    });

    Given("the credit account exists with {string} {string}", (String balance, String currencyId) -> {
      creditAccountId =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(createAccountRequestBody(balance, currencyId, TestAccountType.CREDIT)).
        when().
          post(ApiPath.ACCOUNTS).
        then().
          statusCode(HttpStatus.SC_CREATED).extract().jsonPath().getLong("id");
    });

    Given("the users load account amount input is {string} {string}", (String amount, String currencyId) -> {
      loadAccountRequest = createLoadAccountRequestBody(amount, currencyId);
    });

    When("the user tries to load the credit account", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(loadAccountRequest).
        when().
          post(loadAccountResource(creditAccountId));
    });

    Then("the load account operation was successful", () -> {
      common.response.then().statusCode(HttpStatus.SC_OK);
    });

    Then("the {string} account balance is {string}", (String requestedAccount, String amount) -> {

      long accountId=0L;
      if (requestedAccount.equalsIgnoreCase(TestAccountType.CREDIT.value)) {
        accountId = creditAccountId;
      } else if (requestedAccount.equalsIgnoreCase(TestAccountType.DEBIT.value)) {
        accountId = debitAccountId;
      } else {
        fail("the requested account type should be one of the defined types in: " + TestAccountType.class.getName());
      }

      JsonPath jsonPath =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
        when().
          get(ApiPath.ACCOUNTS + "/" + accountId).
        then().extract().jsonPath();

      assertThat(jsonPath.get("balance"), is(new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN).toString()));
    });
  }

  private LoadAccountRequest createLoadAccountRequestBody(String amount, String currencyId){
    return new LoadAccountRequest(
        amount.equals("null") ? null : new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN),
        currencyId.equals("null") ? null : currencyId
    );
  }

  private CreateAccountRequest createAccountRequestBody(String balance, String currencyId, TestAccountType type) {
    CreateAccountRequest account = new CreateAccountRequest(type.value + "account", currencyId);
    account.setAccountType(type.equals(TestAccountType.DEBIT) ? "LEDGER" : "CLIENT" );
    account.setBalance(new BigDecimal(balance));
    account.setBalanceStatus(type.equals(TestAccountType.DEBIT) ? "DR" : "CR");
    return account;
  }

  private String loadAccountResource(long accountId) {
    return ApiPath.ACCOUNTS + "/" + accountId + ApiPath.LOAD;
  }

  private enum TestAccountType {
    DEBIT ("Debit"), CREDIT("Credit");

    private final String value;

    private TestAccountType(String value) {
      this.value = value;
    }
  }
}
