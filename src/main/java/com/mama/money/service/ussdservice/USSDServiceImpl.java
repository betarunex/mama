package com.mama.money.service.ussdservice;

import com.mama.money.model.MoneyTransferRequest;
import com.mama.money.model.dtos.UssdRequest;
import com.mama.money.model.dtos.UssdResponse;
import com.mama.money.repository.TransferMoneyDBService;
import com.mama.money.util.CurrencyUtil;
import com.mama.money.util.DestinationUtil;
import com.mama.money.util.UssdResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@Component
public class USSDServiceImpl implements USSDService {

    @Autowired
    private UssdResponseUtil ussdUtil;

    @Autowired
    private DestinationUtil destinationUtil;

    @Autowired
    private CurrencyUtil currencyUtil;

    @Autowired
    private TransferMoneyDBService transferMoneyDBService;

    private final String ACCEPT_TRANSACTION = "1";

    public UssdResponse selectMenu(final UssdRequest request) {
        final MoneyTransferRequest transferReq = transferMoneyDBService.getMoneyTransferRequestForMsisdn(request.getMsisdn());
        final MenuPage menuPage = menuForNextRequest(transferReq, request.getSessionId());
        switch (menuPage) {
            case START_WELCOME_MAMA_MONEY:
                return startSession(request);
            case SELECT_COUNTRY:
                return selectCountryDestination(request, transferReq);
            case SELECT_AMOUNT:
                return selectAmount(request, transferReq);
            case CONFIRM_AMOUNT_CURRENCY:
                return confirmAmountAndCurrency(request, transferReq);
            case INFO_COMPLETION:
                return completeTransaction(request, transferReq);
            case ERROR:
                break;
            case ERROR_INVALID_MENU:
                break;
        }
        //TODO: Error method
        return ussdUtil.errorOccured(request.getSessionId());
    }

    private MenuPage menuForNextRequest(final MoneyTransferRequest req,
                                        final String sessionId) {
        if (req == null || sessionId == null || !sessionId.equals(req.getSessionId())) {
            return MenuPage.START_WELCOME_MAMA_MONEY;
        } else if (req.getCurrencyTo() == null) {
            return MenuPage.SELECT_COUNTRY;
        } else if (req.getInitialAmount() == null) {
            return MenuPage.SELECT_AMOUNT;
        } else if (req.getCurrencyTo() == null || StringUtils.isEmpty(req.getDestination())) {
            return MenuPage.CONFIRM_AMOUNT_CURRENCY;
        } else if (!req.isConfirmed()) {
            return MenuPage.INFO_COMPLETION;
        }

        return MenuPage.ERROR_INVALID_MENU;
    }

    private UssdResponse startSession(final UssdRequest request) {
        final MoneyTransferRequest transferReq = new MoneyTransferRequest();
        transferReq.setMsisdn(request.getMsisdn());
        transferReq.setSessionId(request.getSessionId());
        final Currency originCurrency = currencyUtil.currencyByMsisdnCode(request.getMsisdn());
        transferReq.setCurrencyFrom(originCurrency);
        transferMoneyDBService.updateMoneyTransferRequestForMsisdn(transferReq);
        return ussdUtil.welcomeSelectDestinationMenu(request.getSessionId());
    }

    private UssdResponse selectCountryDestination(final UssdRequest request, final MoneyTransferRequest transferReq) {
        // assume a ZAR currency based on number
        int selectedOption;
        try {
            selectedOption = Integer.parseInt(request.getUserEntry());
        } catch (Exception e) {
            return ussdUtil.invalidCountrySelection(request.getSessionId());
        }

        final String destination = destinationFromMenu(selectedOption);
        if (StringUtils.isEmpty(destination)) {
            return ussdUtil.invalidCountrySelection(request.getSessionId());
        }
        final Currency currencyTo = currencyFromDestination(destination);
        if (currencyTo == null) {
            return ussdUtil.errorOccured(request.getSessionId());
        }
        transferReq.setCurrencyTo(currencyTo);
        transferReq.setDestination(destination);
        transferMoneyDBService.updateMoneyTransferRequestForMsisdn(transferReq);

        return ussdUtil.selectAmountMenu(request.getSessionId(),
                transferReq.getCurrencyFrom().getCurrencyCode(),
                destination);
    }

    private UssdResponse selectAmount(final UssdRequest request, final MoneyTransferRequest transferReq) {
        final BigDecimal amountToSend;
        final Currency currencyReceived;

        try {
            amountToSend = new BigDecimal(request.getUserEntry());
            currencyReceived = transferReq.getCurrencyTo();
            BigDecimal conversionRate = currencyUtil.conversionRateForCurrency(
                    transferReq.getCurrencyTo().getCurrencyCode());
            transferReq.setInitialAmount(amountToSend);
            transferReq.setConversionRate(conversionRate);
            transferReq.setCalculatedAmount(currencyUtil.convertCurrencyAmount(amountToSend,
                    conversionRate,
                    currencyReceived.getDefaultFractionDigits()));
            transferMoneyDBService.updateMoneyTransferRequestForMsisdn(transferReq);
            return ussdUtil.confirmTransaction(request.getSessionId(),
                    transferReq.getCalculatedAmount(),
                    currencyReceived);
        } catch (NumberFormatException e) {
            return ussdUtil.invalidAmount(request.getSessionId(),
                    transferReq.getCurrencyFrom().getCurrencyCode(),
                    transferReq.getDestination());
        } catch (Exception e) {
            return ussdUtil.errorOccured(request.getSessionId());
        }
    }

    private UssdResponse confirmAmountAndCurrency(final UssdRequest request, final MoneyTransferRequest transferReq) {
        return ussdUtil.confirmTransaction(request.getSessionId(),
                transferReq.getCalculatedAmount(),
                transferReq.getCurrencyTo());
    }

    private UssdResponse completeTransaction(final UssdRequest request, final MoneyTransferRequest transferReq) {
        final String userEntry = request.getUserEntry();
        if (!StringUtils.isEmpty(userEntry) && ACCEPT_TRANSACTION.equals(userEntry)) {
            final boolean success = true; // Send payment request to external api. if confirmed, proceed.
            transferMoneyDBService.removeMoneyTransferRequestForMsisdn(transferReq);
            if (success) {
                return ussdUtil.transactionCompleted(request.getSessionId());
            } else {
                return ussdUtil.transactionFailed(request.getSessionId());
            }

        } else {
            final String session = request.getSessionId();
            transferMoneyDBService.removeMoneyTransferRequestForMsisdn(transferReq);
            return ussdUtil.requestCancel(session);
        }

    }

    private String destinationFromMenu(final int optionSelected) {
        return destinationUtil.destinationFromSelection(optionSelected);
    }

    private Currency currencyFromDestination(final String destination) {
        return currencyUtil.currencyForCountry(destination);
    }

    public enum MenuPage {
        START_WELCOME_MAMA_MONEY("START_WELCOME_MAMA_MONEY"),
        SELECT_COUNTRY("SELECT_COUNTRY"),
        SELECT_AMOUNT("SELECT_AMOUNT"),
        CONFIRM_AMOUNT_CURRENCY("CONFIRM_AMOUNT_CURRENCY"),
        INFO_COMPLETION("INFO_COMPLETION"),
        ERROR("ERROR"),
        ERROR_INVALID_MENU("ERROR_INVALID_MENU");

        private final String menuPage;
        private static Map<String, MenuPage> menuPages = new HashMap<>();

        MenuPage(final String menu){
            this.menuPage = menu;
        }

        static {
            for (MenuPage menu : MenuPage.values()) {
                menuPages.put(menu.getMenuPage(), menu);
            }
        }

        public String getMenuPage() {
            return menuPage;
        }

        public static MenuPage get(String menu) {
            final MenuPage page = menuPages.get(menu);
            return page != null ? page : ERROR_INVALID_MENU;
        }

    }
}
