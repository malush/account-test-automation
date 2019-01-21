package com.malush.account.requests;

import java.util.Currency;

public class CreateAccountRequest {

  private String nameOnAccount;
  private String currencyId;

  public enum SupportedCurrencies {
    EUR(Currency.getInstance("EUR").getCurrencyCode());

    private String iso4217Code;

    private SupportedCurrencies(String code){
      this.iso4217Code = code;
    }

    public String getIso4217Code() {
      return iso4217Code;
    }
  }

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
