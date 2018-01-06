package com.dwidasa.engine.service.transform;

import java.util.Date;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.SmsRegistrationView;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 9/4/12
 */
public class SmsRegistrationViewTransformer implements Transformer {
    public SmsRegistrationViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        SmsRegistrationView srv = (SmsRegistrationView) view;
        transaction.setCardNumber(srv.getCardNumber());
        transaction.setFromAccountNumber(srv.getAccountNumber());
        transaction.setMerchantType(srv.getMerchantType());
        transaction.setTerminalId(srv.getTerminalId());
        transaction.setCurrencyCode(srv.getCurrencyCode());
        transaction.setFromAccountType(srv.getAccountType());   //requested by bu Uma
        transaction.setToAccountType("00");
        transaction.setCustomerReference(srv.getCustomerReference());
        transaction.setTransactionType(srv.getTransactionType());
        transaction.setTransactionDate(srv.getTransactionDate());
        transaction.setValueDate(srv.getTransactionDate());
        transaction.setCardData1(srv.getCardData1());
        transaction.setCardData2(srv.getCardData2());

        transaction.setCreated(new Date());
        transaction.setCreatedby(srv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(srv.getCustomerId());

        return transaction;
    }
}
