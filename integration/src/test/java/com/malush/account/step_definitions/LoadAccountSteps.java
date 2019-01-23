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
  private long ledgerAccountId;
  private long clientAccountId;

  public LoadAccountSteps(CommonAccountSteps common) {

    //PicoContainer injection
    this.common = common;

    After(() -> {
      loadAccountRequest = null;
      ledgerAccountId = 0L;
      clientAccountId = 0L;
    });

    Given("the {string} account exists with balance of {string} {string}", (String accountType, String balance, String currencyId) -> {
      long accountId =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(createAccountRequestBody(balance, currencyId, accountType)).
        when().
          post(ApiPath.ACCOUNTS).
        then().
          statusCode(HttpStatus.SC_CREATED).extract().jsonPath().getLong("id");

      setAccountIdForType(accountType, accountId);
    });

    Given("the user input for the amount to load is {string} {string}", (String amount, String currencyId) -> {
      loadAccountRequest = createLoadAccountRequestBody(amount, currencyId);
    });

    When("the user tries to load the client account", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(loadAccountRequest).
        when().
          post(loadAccountResource(clientAccountId));
    });

    Then("the load account operation is successful", () -> {
      common.response.then().statusCode(HttpStatus.SC_OK);
    });

    Then("the {string} account balance is {string}", (String accountType, String amount) -> {
      JsonPath jsonPath =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
        when().
          get(ApiPath.ACCOUNTS + "/" + getAccountIdForType(accountType)).
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

  private CreateAccountRequest createAccountRequestBody(String balance, String currencyId, String accountType) {
    validateAccountType(accountType);
    CreateAccountRequest account = new CreateAccountRequest(accountType + "account", currencyId);
    account.setAccountType(accountType.toUpperCase());
    account.setBalance(new BigDecimal(balance));
    account.setBalanceStatus(accountType.equals(AccountType.LEDGER) ? "DR" : "CR");
    return account;
  }

  private String loadAccountResource(long accountId) {
    return ApiPath.ACCOUNTS + "/" + accountId + ApiPath.LOAD;
  }

  private enum AccountType {
    LEDGER ("ledger"), CLIENT("client");

    private final String value;

    private AccountType(String value) {
      this.value = value;
    }
  }

  private long getAccountIdForType(String accountType) {
    validateAccountType(accountType);

    if (accountType.equalsIgnoreCase(AccountType.CLIENT.value))
      return clientAccountId;
    else
      return ledgerAccountId;
  }

  private void setAccountIdForType(String accountType, long accountId) {
    validateAccountType(accountType);

    if (accountType.equalsIgnoreCase(AccountType.CLIENT.value))
      clientAccountId = accountId;
    else
      ledgerAccountId = accountId;
  }

  private void validateAccountType(String accountType) {
    if (accountType.equalsIgnoreCase(AccountType.CLIENT.value)) return;
    else if (accountType.equalsIgnoreCase(AccountType.LEDGER.value)) return;
    else
      fail("Wrong test parameter. The provided account type should be one of the defined types in: " + AccountType.class.getName());
  }

}
