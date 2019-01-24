package com.malush.account.step_definitions;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.malush.account.repository.AccountRepository;
import com.malush.account.repository.UserRepository;
import com.malush.account.requests.ApiPath;
import com.malush.account.requests.CreateAccountRequest;
import com.malush.account.requests.Headers;
import cucumber.api.java8.En;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.math.BigDecimal;
import org.apache.http.HttpStatus;

public class CommonAccountSteps implements En {

  protected LoginSteps login;

  protected Response response;
  protected CreateAccountRequest createAccountRequest;
  protected long ledgerAccountId;
  protected long clientCreditAccountId;
  protected long clientDebitAccountId;
  private int numberOfAccounts;

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
      ledgerAccountId = 0L;
      clientCreditAccountId = 0L;
      clientDebitAccountId = 0L;
      numberOfAccounts = 0;
    });

    Given("the user inserts account details: {string} and {string}", (String nameOnAccount, String currency) -> {
      createAccountRequest = createAccountRequestBody(nameOnAccount, currency);
    });

    Then("the access to account operations is forbidden", () -> {
      response.then().statusCode(HttpStatus.SC_FORBIDDEN);
      assertThat(response.jsonPath().get("message"), is("Access Denied"));
    });

    Given("the account with account details: {string} and {string} already exists", (String nameOnAccount, String currencyId) -> {
      clientCreditAccountId =
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
      clientCreditAccountId = 0;
    });

    Then("the account cannot be found", () -> {
      response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    });

    Given("the {string} account exists with balance of {string} {string} and balance status {string}",
        (String accountType, String balance, String currencyId, String balanceStatus) -> {

      long accountId =
        given().
          contentType(ContentType.JSON).
          header(Headers.ACCESS_TOKEN, Headers.BEARER + login.accessToken).
          body(createAccountRequestBody(balance, currencyId, accountType, balanceStatus)).
        when().
          post(ApiPath.ACCOUNTS).
        then().
          statusCode(HttpStatus.SC_CREATED).extract().jsonPath().getLong("id");

      setAccountIdForType(accountType, balanceStatus, accountId);
    });

    Then("the account operation fails because of internal server error", () -> {
      response.then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    });

  }



  protected CreateAccountRequest createAccountRequestBody(String nameOnAccount, String currency) {
    return new CreateAccountRequest(
        nameOnAccount.equals("null") ? null : nameOnAccount,
        currency.equals("null") ? null : currency
    );
  }

  protected CreateAccountRequest createAccountRequestBody(String balance, String currencyId, String accountType, String balanceStatus) {
    validateAccountType(accountType);
    CreateAccountRequest account = new CreateAccountRequest(accountType + "account" + numberOfAccounts++, currencyId);
    account.setAccountType(accountType.toUpperCase());
    account.setBalance(new BigDecimal(balance));
    account.setBalanceStatus(balanceStatus);
    return account;
  }

  protected enum AccountType {
    LEDGER ("ledger"), CLIENT("client");

    private final String value;

    private AccountType(String value) {
      this.value = value;
    }

    protected String getValue(){
      return value;
    }
  }

  protected void validateAccountType(String accountType) {
    if (accountType.equalsIgnoreCase(AccountType.CLIENT.value)) return;
    else if (accountType.equalsIgnoreCase(AccountType.LEDGER.value)) return;
    else
      fail("Wrong test parameter. The provided account type should be one of the defined types in: " + AccountType.class.getName());
  }

  private void validateBalanceStatus(String balanceStatus) {
    if(!balanceStatus.equals("DR") && !balanceStatus.equals("CR"))
      fail("Wrong test parameter. The provided balance status is not correct.");
  }

  private void setAccountIdForType(String accountType, String balanceStatus, long accountId) {
    validateAccountType(accountType);
    validateBalanceStatus(balanceStatus);

    if (accountType.equalsIgnoreCase(AccountType.CLIENT.getValue()) && balanceStatus.equals("CR"))
      clientCreditAccountId = accountId;
    else if (accountType.equalsIgnoreCase(AccountType.CLIENT.getValue()) && balanceStatus.equals("DR"))
      clientDebitAccountId = accountId;
    else
      ledgerAccountId = accountId;
  }

  private long getAccountIdForType(String accountType) {
    validateAccountType(accountType);

    if (accountType.equalsIgnoreCase(AccountType.CLIENT.getValue()))
      return clientCreditAccountId;
    else
      return ledgerAccountId;
  }


  protected JsonPath getAccount(String accountType) {
    return
      given().
        contentType(ContentType.JSON).
        header(Headers.ACCESS_TOKEN, Headers.BEARER + login.accessToken).
      when().
        get(ApiPath.ACCOUNTS + "/" + getAccountIdForType(accountType)).
      then().extract().jsonPath();
  }



}
