package com.malush.account.step_definitions;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.malush.account.repository.AccountRepository;
import com.malush.account.repository.UserRepository;
import com.malush.account.requests.ApiPath;
import com.malush.account.requests.CreateAccountRequest;
import com.malush.account.requests.Headers;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.math.BigDecimal;
import org.apache.http.HttpStatus;

public class CommonAccountSteps implements En {

  private LoginSteps login;

  protected Response response;
  protected CreateAccountRequest createAccountRequest;
  protected long accountId;

  public CommonAccountSteps(LoginSteps login) {

    //PicoContainer injection
    this.login = login;

    Before(() -> {
      AccountRepository.getRepository().deleteAll();
      UserRepository.getRepository().deleteAll();
    });

    After(() -> {
      response = null;
      createAccountRequest = null;
      accountId = 0L;
    });

    Given("the user inserts account details: {string} and {string}", (String nameOnAccount, String currency) -> {
      createAccountRequest = createAccountRequestBody(nameOnAccount, currency);
    });

    Then("the access to account operations is forbidden", () -> {
      response.then().statusCode(HttpStatus.SC_FORBIDDEN);
      assertThat(response.jsonPath().get("message"), is("Access Denied"));
    });

    Given("the account with account details: {string} and {string} already exists", (String nameOnAccount, String currencyId) -> {
      accountId =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + login.accessToken).
          body(createAccountRequestBody(nameOnAccount, currencyId)).
        when().
          post(ApiPath.ACCOUNTS).jsonPath().getLong("id");
    });

    Given("the request balance value is {string}", (String balance) -> {
      if(!balance.equals("missing") && !balance.isEmpty())
        createAccountRequest.setBalance(new BigDecimal(balance));
    });

    Then("the account operation fails with the response indicating the conflict", () -> {
      response.then().statusCode(HttpStatus.SC_CONFLICT);
    });

    When("the user chooses the wrong account number", () -> {
      accountId = 0;
    });

    Then("the account cannot be found", () -> {
      response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    });



  }

  protected CreateAccountRequest createAccountRequestBody(String nameOnAccount, String currency){
    return new CreateAccountRequest(
        nameOnAccount.equals("null") ? null : nameOnAccount,
        currency.equals("null") ? null : currency
    );
  }

}
