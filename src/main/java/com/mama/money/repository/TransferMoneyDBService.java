package com.mama.money.repository;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.mama.money.model.MoneyTransferRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransferMoneyDBService {
    final String MONEY_TRANSFER_REQUEST = "moneyTransferRequest";

    private final HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
    private final Map<String, MoneyTransferRequest> moneyTransferMapMsisdn;

    public TransferMoneyDBService() {
        moneyTransferMapMsisdn = hazelcast.getMap(MONEY_TRANSFER_REQUEST);
    }

    public MoneyTransferRequest getMoneyTransferRequestForMsisdn(final String msisdn) {
        return moneyTransferMapMsisdn.get(msisdn);
    }

    public void updateMoneyTransferRequestForMsisdn(final MoneyTransferRequest request) {
        moneyTransferMapMsisdn.put(request.getMsisdn(), request);
    }

    public void removeMoneyTransferRequestForMsisdn(final MoneyTransferRequest request) {
        moneyTransferMapMsisdn.remove(request.getMsisdn(), request);
    }

}
