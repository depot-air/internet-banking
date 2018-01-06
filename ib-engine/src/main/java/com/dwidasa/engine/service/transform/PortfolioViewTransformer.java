package com.dwidasa.engine.service.transform;

import java.util.Date;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.PortfolioView;

public class PortfolioViewTransformer implements Transformer {
    public PortfolioViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        PortfolioView pv = (PortfolioView) view;
        transaction.setCardNumber(pv.getCardNumber());
        transaction.setFromAccountNumber(pv.getAccountNumber());
        transaction.setMerchantType(pv.getMerchantType());
        transaction.setTerminalId(pv.getTerminalId());
        transaction.setCurrencyCode(pv.getCurrencyCode());
        transaction.setFromAccountType(pv.getAccountType());
        transaction.setToAccountType("00");
//        transaction.setCustomerReference(pv.getCustomerReference());
        transaction.setTransactionType(pv.getTransactionType());
        transaction.setTransactionDate(pv.getTransactionDate());
        transaction.setValueDate(pv.getTransactionDate());
//        transaction.setCardData1(pv.getCardData1());
//        transaction.setCardData2(pv.getCardData2());
    	transaction.setDescription("PORTOFOLIO");

        transaction.setCreated(new Date());
        transaction.setCreatedby(pv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(pv.getCustomerId());

        return transaction;
    }
}

