package com.malush.account.requests;

import java.math.BigDecimal;

public class LoadAccountRequest {

  private BigDecimal amount;
  private String currencyId;

  public LoadAccountRequest(BigDecimal amount, String currencyId) {
    this.amount = amount;
    this.currencyId = currencyId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(String currencyId) {
    this.currencyId = currencyId;
  }
}
