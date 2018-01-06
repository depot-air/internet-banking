package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashOutDelimaView;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/10/11
 * Time: 2:00 PM
 */
public class CashOutDelimaViewTransformer implements Transformer {
    public CashOutDelimaViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        CashOutDelimaView cv = (CashOutDelimaView) view;
        transaction.setTransactionType(cv.getTransactionType());
        transaction.setCardNumber(cv.getCardNumber());
        transaction.setTransactionAmount(cv.getAmount());
        transaction.setFeeIndicator(cv.getFeeIndicator());
        transaction.setFee(cv.getFee());
        transaction.setTransactionDate(cv.getTransactionDate());
        transaction.setValueDate(cv.getTransactionDate());
        transaction.setMerchantType(cv.getMerchantType());
        transaction.setTerminalId(cv.getTerminalId());
        transaction.setCurrencyCode(cv.getCurrencyCode());
        transaction.setCustomerReference(cv.getCustomerReference());
        transaction.setFromAccountNumber(cv.getAccountNumber());
        transaction.setFromAccountType(cv.getAccountType());
        transaction.setToAccountType("00");
        transaction.setBillerCode(cv.getBillerCode());
        transaction.setProviderCode(cv.getProviderCode());
        transaction.setProductCode(cv.getProductCode());
        

        transaction.setCreated(new Date());
        transaction.setCreatedby(cv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(cv.getCustomerId());
        return transaction;
    }
}
