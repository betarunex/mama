package com.mama.money.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;

@Component
public class CurrencyUtil {

    public Currency currencyForCountry(final String country) {
        // This is temp hardcoded
        switch (country) {
            case "Malawi":
                return Currency.getInstance("MWK");
            case "Kenya":
                return Currency.getInstance("KES");
        }
        return null;
    }

    public Currency currencyByMsisdnCode(final String msisdn) {
        // This is temp hardcoded
        if (msisdn.startsWith("27")) {
            return Currency.getInstance("ZAR");
        }
        return null;
    }

    public BigDecimal conversionRateForCurrency(final String currency) {
        // This is temp hardcoded
        switch (currency) {
            case "KES":
                return new BigDecimal("6.10");
            case "MWK":
                return new BigDecimal("42.50");
        }
        return null;
    }

    public BigDecimal convertCurrencyAmount(final BigDecimal amount, final BigDecimal rate, final int decimals) {
        final BigDecimal conversionAmount = amount.multiply(rate);
        return conversionAmount.setScale(decimals, BigDecimal.ROUND_UP);
    }
}
