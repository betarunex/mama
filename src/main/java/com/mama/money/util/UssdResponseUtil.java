package com.mama.money.util;

import com.mama.money.model.dtos.UssdResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;

@Component
public class UssdResponseUtil {

    private static String MESSAGE_INFO_WELCOME = "Welcome to Mama Money!";
    private static String MESSAGE_INFO_TRANSACTION_COMPLETE = "Thank you for using MamaMoney!";
    private static String MESSAGE_INFO_TRANSACTION_CANCEL = "Your request has been cancelled.";
    private static String MESSAGE_SELECT_COUNTRY = "Where would you like to send money to?\n1) Kenya\n2) Malawi";
    private static String MESSAGE_SELECT_AMOUNT = "How much money (in %s) would you like to send to %s";
    private static String MESSAGE_SELECT_CONFIRMATION_SEND = "Your person you are sending to will receive: %s %s \n1)Ok";
    private static String MESSAGE_INVALID_OPTION = "Invalid selection.";
    private static String MESSAGE_INVALID_AMOUNT = "Please enter a valid amount.";
    private static String MESSAGE_ERROR_TRANSACTION_FAILED = "Unable to process request";
    private static String MESSAGE_ERROR = "An error has occured. Please Try again later";

    public UssdResponse welcomeSelectDestinationMenu(final String sessionId) {
        //TODO: Can be replace options with dynamic options instead
        final String message = MESSAGE_INFO_WELCOME + " " + MESSAGE_SELECT_COUNTRY;
        return new UssdResponse(sessionId, message);
    }

    public UssdResponse selectAmountMenu(final String sessionId,
                                 final String currencyDisplayName,
                                 final String destination) {
        final String message = String.format(MESSAGE_SELECT_AMOUNT,
                currencyDisplayName,
                destination);
        return new UssdResponse(sessionId, message);
    }

    public UssdResponse confirmTransaction(final String sessionId,
                                 final BigDecimal amountToSend,
                                 final Currency currency) {
        final String message = String.format(MESSAGE_SELECT_CONFIRMATION_SEND,
                amountToSend.toString(),
                currency.getDisplayName());
        return new UssdResponse(sessionId, message);
    }

    public UssdResponse transactionCompleted(final String sessionId) {
        return new UssdResponse(sessionId, MESSAGE_INFO_TRANSACTION_COMPLETE);
    }

    public UssdResponse invalidCountrySelection(final String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append(MESSAGE_INVALID_OPTION);
        sb.append(" ");
        sb.append(MESSAGE_SELECT_COUNTRY);
        return new UssdResponse(sessionId, sb.toString());
    }

    public UssdResponse errorOccured(final String sessionId) {
        return new UssdResponse(sessionId, MESSAGE_ERROR);
    }

    public UssdResponse requestCancel(final String sessionId) {
        return new UssdResponse(sessionId, MESSAGE_INFO_TRANSACTION_CANCEL);
    }

    public UssdResponse invalidAmount(final String sessionId,
                                      final String currencyDisplayName,
                                      final String destination) {
        StringBuilder sb = new StringBuilder();
        sb.append(MESSAGE_INVALID_AMOUNT);
        sb.append(" ");
        sb.append(String.format(MESSAGE_SELECT_AMOUNT,currencyDisplayName,destination));
        return new UssdResponse(sessionId, sb.toString());
    }

    public UssdResponse transactionFailed(final String sessionId) {
        return new UssdResponse(sessionId, MESSAGE_ERROR_TRANSACTION_FAILED);
    }

}
