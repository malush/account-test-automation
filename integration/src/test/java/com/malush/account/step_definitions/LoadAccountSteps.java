package com.malush.account.step_definitions;

import com.malush.account.requests.ApiPath;
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

public class LoadAccountSteps implements En {

  private CommonAccountSteps common;

  private LoadAccountRequest loadAccountRequest;

  public LoadAccountSteps(CommonAccountSteps common) {

    //PicoContainer injection
    this.common = common;

    After(() -> {
      loadAccountRequest = null;
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
          post(loadAccountResource(common.clientCreditAccountId));
    });

    Then("the load account operation is successful", () -> {
      common.response.then().statusCode(HttpStatus.SC_OK);
    });

    Then("the {string} account balance is {string}", (String accountType, String amount) -> {
      JsonPath jsonPath = common.getAccount(accountType);
      assertThat(jsonPath.get("balance"), is(new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN).toString()));
    });

    Then("the {string} account balance status is {string}", (String accountType, String balanceStatus) -> {
      JsonPath jsonPath = common.getAccount(accountType);
      assertThat(jsonPath.get("balanceStatus"), is(balanceStatus));
    });

  }

  private LoadAccountRequest createLoadAccountRequestBody(String amount, String currencyId){
    return new LoadAccountRequest(
        (amount.isEmpty() ||
        amount.equals("null")) ? null : new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN),
        currencyId.equals("null") ? null : currencyId
    );
  }

  private String loadAccountResource(long accountId) {
    return ApiPath.ACCOUNTS + "/" + accountId + ApiPath.LOAD;
  }
}
