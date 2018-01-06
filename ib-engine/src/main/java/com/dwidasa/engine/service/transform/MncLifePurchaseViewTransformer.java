package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/23/11
 * Time: 6:38 PM
 */
public class MncLifePurchaseViewTransformer implements Transformer {
    public MncLifePurchaseViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        MncLifePurchaseView vpv = (MncLifePurchaseView) view;
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
        transaction.setBit22(vpv.getBit22());
        transaction.setBillerCode(vpv.getBillerCode());
        transaction.setProductCode(vpv.getProductCode());
        transaction.setProviderCode(vpv.getProviderCode());
        transaction.setDenomination(vpv.getDenomination());

        transaction.setCreated(new Date());
        transaction.setCreatedby(vpv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(vpv.getCustomerId());
       
        
        return transaction;
    }
}
