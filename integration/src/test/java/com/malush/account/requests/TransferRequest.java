package com.malush.account.requests;

import java.math.BigDecimal;

public class TransferRequest {

  private BigDecimal amount;
  private String currencyId;
  private long debitAccountId;
  private long creditAccountId;

  public TransferRequest(BigDecimal amount, String currencyId, long debitAccountId,
      long creditAccountId) {
    this.amount = amount;
    this.currencyId = currencyId;
    this.debitAccountId = debitAccountId;
    this.creditAccountId = creditAccountId;
  }

  public TransferRequest(){}

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

  public long getDebitAccountId() {
    return debitAccountId;
  }

  public void setDebitAccountId(long debitAccountId) {
    this.debitAccountId = debitAccountId;
  }

  public long getCreditAccountId() {
    return creditAccountId;
  }

  public void setCreditAccountId(long creditAccountId) {
    this.creditAccountId = creditAccountId;
  }
}
