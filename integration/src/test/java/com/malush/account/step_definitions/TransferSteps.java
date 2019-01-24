package com.malush.account.step_definitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.malush.account.requests.ApiPath;
import com.malush.account.requests.Headers;
import com.malush.account.requests.TransferRequest;
import com.malush.account.step_definitions.CommonAccountSteps.AccountType;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import org.apache.http.HttpStatus;

public class TransferSteps implements En {

  private CommonAccountSteps common;

  private TransferRequest transferRequest;

  public TransferSteps(CommonAccountSteps common) {


    //PicoContainer injection
    this.common = common;

    Before(()-> {
      transferRequest = new TransferRequest();
    });

    After(() -> {
      transferRequest = null;
    });

    Given("the user selects the transfer amount: {string}", (String amount) -> {
      transferRequest.setAmount(
          (amount.isEmpty() || amount.equals("null")) ?
              null : new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN)
      );
    });

    Given("the user selects the transfer currency: {string}", (String currencyId) -> {
      transferRequest
          .setCurrencyId(currencyId.equals("null") ? null
              : Currency.getInstance(currencyId).getCurrencyCode());
    });

    Given("the user selects the transfer {string} account", (String accountBalanceStatus) -> {
      transferRequest = Optional.ofNullable(transferRequest)
          .orElse(new TransferRequest());
      if (accountBalanceStatus.equals("credit")){
        transferRequest.setCreditAccountId(common.clientCreditAccountId);
      } else {
        transferRequest.setDebitAccountId(common.clientDebitAccountId);
      }
    });

    When("the user tries to transfer between accounts", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(transferRequest).
        when().
          post(ApiPath.TRANSFERS);
    });

    Then("the transfer completed successfully", () -> {
      common.response.then().statusCode(HttpStatus.SC_OK);
    });

    Then("the transfer {string} account balance is {string}", (String accountBalanceStatus, String balance) -> {
      JsonPath jsonPath;
      if (accountBalanceStatus.equalsIgnoreCase("debit")) {
        jsonPath = common.getAccount(AccountType.CLIENT.getValue(), "DR");
      } else if (accountBalanceStatus.equalsIgnoreCase("credit")) {
        jsonPath = common.getAccount(AccountType.CLIENT.getValue(), "CR");
      } else {
        fail("Wrong account balance status value in test");
        return;
      }

      assertThat(jsonPath.get("balance"), is(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_DOWN).toString()));
    });

  }
}
