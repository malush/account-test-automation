package com.malush.account.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;

@JsonInclude(Include.NON_NULL)
public class CreateAccountRequest {

  private String nameOnAccount;
  private String currencyId;
  private BigDecimal balance;
  private String accountType;
  private String balanceStatus;

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

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getBalanceStatus() {
    return balanceStatus;
  }

  public void setBalanceStatus(String balanceStatus) {
    this.balanceStatus = balanceStatus;
  }
}
