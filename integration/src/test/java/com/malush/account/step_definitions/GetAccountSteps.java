package com.malush.account.step_definitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.malush.account.requests.ApiPath;
import com.malush.account.requests.Headers;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import java.math.BigDecimal;
import org.apache.http.HttpStatus;

public class GetAccountSteps implements En {

  private CommonAccountSteps common;
  private LoginSteps login;

  public GetAccountSteps(CommonAccountSteps common, LoginSteps login) {

    //PicoContainer injection
    this.common = common;
    this.login = login;

    When("the user tries to get the account", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + login.accessToken).
        when().
          get(ApiPath.ACCOUNTS + "/" + common.accountId);
    });

    Then("the users account is successfully retrieved", () -> {
      common.response.then().statusCode(HttpStatus.SC_OK);
    });

    Then("the account has the following details: {string} and {string}", (String nameOnAccount, String currencyId) -> {
      JsonPath jsonPath =  common.response.then().extract().jsonPath();
      assertThat(jsonPath.get("nameOnAccount"), is(nameOnAccount));
      assertThat(jsonPath.get("currencyId"), is(currencyId));
    });

    Then("the account response contains all required fields", () -> {
      common.response.
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

    Then("the account balance is {string}", (String balance) -> {
      JsonPath jsonPath =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + login.accessToken).
        when().
          get(ApiPath.ACCOUNTS + "/" + common.accountId).
        then().
          statusCode(HttpStatus.SC_OK).extract().jsonPath();

      if(balance.equals("missing")) {
        assertThat(jsonPath.get("balance"), is("0.00"));
      } else {
        assertThat(jsonPath.get("balance"), is(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_DOWN).toString()));
      }
    });

    Then("the created values for {string}, {string} and {string} are correct", (String accountType, String balance, String balanceStatus) -> {
      JsonPath jsonPath =
          given().
            contentType(ContentType.JSON).
            header(Headers.ACCESS_TOKEN, Headers.BEARER + login.accessToken).
          when().
            get(ApiPath.ACCOUNTS + "/" + common.accountId).
          then().
            statusCode(HttpStatus.SC_OK).extract().jsonPath();

      assertThat(jsonPath.get("accountType"), is(accountType));
      assertThat(jsonPath.get("balance"), is(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_DOWN).toString()));
      assertThat(jsonPath.get("balanceStatus"), is(balanceStatus));
    });

  }
}
