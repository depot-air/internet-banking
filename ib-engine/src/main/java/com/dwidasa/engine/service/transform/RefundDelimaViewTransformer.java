package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.RefundDelimaView;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/12/11
 * Time: 4:35 PM
 */
public class RefundDelimaViewTransformer implements Transformer {
    public RefundDelimaViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        RefundDelimaView rv = (RefundDelimaView) view;
        transaction.setTransactionType(rv.getTransactionType());
        transaction.setCardNumber(rv.getCardNumber());
        transaction.setTransactionAmount(rv.getAmount());
        transaction.setFeeIndicator(rv.getFeeIndicator());
        transaction.setFee(rv.getFee());
        transaction.setTransactionDate(rv.getTransactionDate());
        transaction.setValueDate(rv.getTransactionDate());
        transaction.setMerchantType(rv.getMerchantType());
        transaction.setTerminalId(rv.getTerminalId());
        transaction.setCurrencyCode(rv.getCurrencyCode());
        transaction.setCustomerReference(rv.getCustomerReference());
        transaction.setFromAccountNumber(rv.getAccountNumber());
        transaction.setFromAccountType(rv.getAccountType());
        transaction.setToAccountType(rv.getToAccountType());

        transaction.setCreated(new Date());
        transaction.setCreatedby(rv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(rv.getCustomerId());
        return transaction;
    }
}
