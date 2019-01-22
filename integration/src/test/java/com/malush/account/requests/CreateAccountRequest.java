package com.malush.account.requests;

public class CreateAccountRequest {

  private String nameOnAccount;
  private String currencyId;

  public CreateAccountRequest(String nameOnAccount, String currencyId) {
    this.nameOnAccount = nameOnAccount;
    this.currencyId = currencyId;
  }

  public String getNameOnAccount() {
    return nameOnAccount;
  }

  public void setNameOnAccount(String nameOnAccount) {
    this.nameOnAccount = nameOnAccount;
  }

  public String getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(String currencyId) {
    this.currencyId = currencyId;
  }
}
