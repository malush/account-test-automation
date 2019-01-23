package com.malush.account.step_definitions;

import com.malush.account.requests.ApiPath;
import com.malush.account.requests.Headers;
import com.malush.account.requests.TopUpRequest;
import cucumber.api.java8.En;
import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.apache.http.HttpStatus;

public class TopUpSteps implements En {

  private CommonAccountSteps common;

  private TopUpRequest topUpRequest;

  public TopUpSteps(CommonAccountSteps common) {

    //PicoContainer injection
    this.common = common;

    After(() -> {
      topUpRequest = null;
    });

    Given("the user inserts the amount: {string} and currencyId: {string}", (String amount, String currencyId) -> {
      topUpRequest = createTopUpRequestBody(amount, currencyId);
    });

    When("the user tries to load an account", () -> {
      common.response =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + common.login.accessToken).
          body(topUpRequest).
        when().log().all().
          post(topUpAccountResource());
    });

    Then("the topUp of the users account was successful", () -> {
      common.response.then().statusCode(HttpStatus.SC_OK);
    });
  }

  private TopUpRequest createTopUpRequestBody(String amount, String currencyId){
    return new TopUpRequest(
        amount.equals("null") ? null : new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN),
        currencyId.equals("null") ? null : currencyId
    );
  }

  private String topUpAccountResource() {
    return ApiPath.ACCOUNTS + "/" + common.accountId + ApiPath.LOAD;
  }
}
