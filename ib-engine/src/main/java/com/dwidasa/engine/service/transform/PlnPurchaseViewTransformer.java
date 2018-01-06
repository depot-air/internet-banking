package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.PlnPurchaseView;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/9/11
 * Time: 10:40 AM
 */
public class PlnPurchaseViewTransformer implements Transformer {
    public Transaction transformTo(BaseView view, Transaction transaction) {
        PlnPurchaseView vpv = (PlnPurchaseView) view;
        transaction.setTransactionType(vpv.getTransactionType());
        transaction.setCardNumber(vpv.getCardNumber());
        transaction.setTransactionAmount(vpv.getAmount());
        transaction.setFeeIndicator(vpv.getFeeIndicator());
        transaction.setFee(vpv.getFee());
        transaction.setTransactionDate(vpv.getTransactionDate());
        transaction.setValueDate(vpv.getTransactionDate());
        transaction.setMerchantType(vpv.getMerchantType());
        transaction.setTerminalId(vpv.getTerminalId());
        transaction.setCurrencyCode(vpv.getCurrencyCode());
        transaction.setCustomerReference(vpv.getCustomerReference());
        transaction.setFromAccountNumber(vpv.getAccountNumber());
        transaction.setFromAccountType(vpv.getAccountType());
        transaction.setToAccountType(vpv.getToAccountType());
        transaction.setCardData1(vpv.getCardData1());
        transaction.setCardData2(vpv.getCardData2());

        transaction.setFreeData1(vpv.getBit48());
        transaction.setFreeData3(vpv.getBit62());
        transaction.setBillerCode(vpv.getBillerCode());
        transaction.setProductCode(vpv.getProductCode());
        transaction.setProviderCode(vpv.getProviderCode());

        transaction.setCreated(new Date());
        transaction.setCreatedby(vpv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(vpv.getCustomerId());
        return transaction;
    }
}
