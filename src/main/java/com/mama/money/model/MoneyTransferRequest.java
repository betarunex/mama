package com.mama.money.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
public class MoneyTransferRequest implements Serializable {
    private Currency currencyFrom;
    private Currency currencyTo;
    private BigDecimal conversionRate;
    private BigDecimal initialAmount;
    private BigDecimal calculatedAmount;
    private String sessionId;
    private String msisdn;
    private String destination;
    private boolean confirmed = false;

    public MoneyTransferRequest() {
    }

    public MoneyTransferRequest(Currency from, Currency to, BigDecimal amount, BigDecimal rate){
        this.currencyFrom = from;
        this.currencyTo = to;
        this.initialAmount = amount;
        this.conversionRate = rate;
        this.calculatedAmount = this.initialAmount.multiply(rate);
        this.calculatedAmount.setScale(currencyTo.getDefaultFractionDigits());
    }

}
