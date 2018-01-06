package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.CashInDompetkuView;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/10/11
 * Time: 1:59 PM
 */
public class CashInDompetkuViewTransformer implements Transformer {
    public CashInDompetkuViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        CashInDompetkuView cv = (CashInDompetkuView) view;
        transaction.setTransactionType(cv.getTransactionType());
        transaction.setCardNumber(cv.getCardNumber());
        transaction.setTransactionAmount(cv.getNominalTopUp());
        transaction.setFeeIndicator(cv.getFeeIndicator());
        transaction.setFee(cv.getFee());
        transaction.setTransactionDate(cv.getTransactionDate());
        transaction.setValueDate(cv.getTransactionDate());
        transaction.setMerchantType(cv.getMerchantType());
        transaction.setTerminalId(cv.getTerminalId());
        transaction.setCurrencyCode(cv.getCurrencyCode());
        transaction.setCustomerReference(cv.getCustomerReference());
        transaction.setFromAccountNumber(cv.getAccountNumber());
        transaction.setFromAccountType(cv.getAccountType() == null ? "10" : cv.getAccountType());
        transaction.setToAccountType("00");
        transaction.setCardData1(cv.getCardData1());
        transaction.setCardData2(cv.getCardData2());
        transaction.setProviderCode(cv.getProviderCode());
        transaction.setBillerCode(cv.getBillerCode());
        transaction.setProductCode(cv.getProductCode());
        transaction.setCreated(new Date());
        transaction.setCreatedby(cv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(cv.getCustomerId());
        return transaction;
    }
}
