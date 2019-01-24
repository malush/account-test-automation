package com.malush.account.step_definitions;

import com.malush.account.requests.TransferRequest;
import cucumber.api.java8.En;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

public class TransferSteps implements En {

  private CommonAccountSteps common;

  private TransferRequest transferRequest;

  public TransferSteps(CommonAccountSteps common) {


    //PicoContainer injection
    this.common = common;

    After(() -> {
      transferRequest = null;
    });

    Given("the user selects the transfer amount: {string}", (String amount) -> {
      Optional.ofNullable(transferRequest)
          .orElse(new TransferRequest())
          .setAmount(
              (amount.isEmpty() || amount.equals("null")) ?
                  null : new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN)
          );
    });

    Given("the user selects the transfer currency: {string}", (String currencyId) -> {
      Optional.ofNullable(transferRequest)
          .orElse(new TransferRequest())
          .setCurrencyId(currencyId.equals("null") ? null : Currency.getInstance(currencyId).getCurrencyCode());
    });

    Given("the user selects the transfer {string} account", (String cardBalanceStatus) -> {
      transferRequest = Optional.ofNullable(transferRequest)
          .orElse(new TransferRequest());
      if(cardBalanceStatus.equals("credit")){
        transferRequest.setCreditAccountId(common.clientCreditAccountId);
      } else {
        transferRequest.setDebitAccountId(common.clientDebitAccountId);
      }
    });


  }
}
