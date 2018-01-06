package com.dwidasa.engine.service.transform;

import java.util.Date;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.MobileRegistrationView;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 9/4/12
 */
public class MobileRegistrationViewTransformer implements Transformer {
    public MobileRegistrationViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        MobileRegistrationView mrv = (MobileRegistrationView) view;
        transaction.setCardNumber(mrv.getCardNumber());
        transaction.setFromAccountNumber(mrv.getAccountNumber());
        transaction.setMerchantType(mrv.getMerchantType());
        transaction.setTerminalId(mrv.getTerminalId());
        transaction.setCurrencyCode(mrv.getCurrencyCode());
        transaction.setFromAccountType(mrv.getAccountType()); 
        transaction.setToAccountType("00");
        transaction.setCustomerReference(mrv.getCustomerReference());
        transaction.setTransactionType(mrv.getTransactionType());
        transaction.setTransactionDate(mrv.getTransactionDate());
        transaction.setValueDate(mrv.getTransactionDate());
        transaction.setCardData1(mrv.getCardData1());
        transaction.setCardData2(mrv.getCardData2());
        
        transaction.setFreeData1(mrv.getBit48());

        transaction.setCreated(new Date());
        transaction.setCreatedby(mrv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(mrv.getCustomerId());

        return transaction;
    }
}
