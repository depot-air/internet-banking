package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CustomerAccountView;
import com.dwidasa.engine.model.view.PaymentView;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 10:34 AM
 */
public class CustomerAccountViewTransformer implements Transformer {
    public CustomerAccountViewTransformer() {
    }

    /**
     * {@inheritDoc}
     */
    public Transaction transformTo(BaseView view, Transaction transaction) {
        CustomerAccountView pv = (CustomerAccountView) view;
        transaction.setTransactionType(pv.getTransactionType());
        transaction.setCardNumber(pv.getCardNumber());
        transaction.setTransactionAmount(pv.getAmount());
        transaction.setFeeIndicator(pv.getFeeIndicator());
        transaction.setFee(pv.getFee());
        transaction.setTransactionDate(pv.getTransactionDate());
        transaction.setValueDate(pv.getTransactionDate());
        transaction.setMerchantType(pv.getMerchantType());
        transaction.setTerminalId(pv.getTerminalId());
        transaction.setCurrencyCode(pv.getCurrencyCode());
        transaction.setCustomerReference(pv.getCustomerReference());
        transaction.setFromAccountNumber(pv.getAccountNumber());
        transaction.setFromAccountType(pv.getAccountType());
        transaction.setToAccountType(pv.getToAccountType());
        transaction.setCardData1(pv.getCardData1());
        transaction.setCardData2(pv.getCardData2());
//        if (transaction.getTransactionType() != null && (
//                transaction.getTransactionType().equals(Constants.TELCO_PAYMENT_INQ_CODE) ||transaction.getTransactionType().equals(Constants.TELCO_PAYMENT_CODE) || transaction.getTransactionType().equals(Constants.TELCO_PAYMENT_REP_CODE)
//        )) {
//            transaction.setBit22(pv.getBit22());
//        }
        transaction.setBillerCode(pv.getBillerCode());
        transaction.setProductCode(pv.getProductCode());
        transaction.setProviderCode(pv.getProviderCode());

        transaction.setCreated(new Date());
        transaction.setCreatedby(pv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(pv.getCustomerId());

        return transaction;
    }
}
