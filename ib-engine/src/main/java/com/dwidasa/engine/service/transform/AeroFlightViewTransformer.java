package com.dwidasa.engine.service.transform;

import java.util.Date;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.BaseView;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/23/11
 * Time: 6:38 PM
 */
public class AeroFlightViewTransformer implements Transformer {
    public AeroFlightViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        AeroFlightView afv = (AeroFlightView) view;
        transaction.setTransactionType(afv.getTransactionType());
        transaction.setCardNumber(afv.getCardNumber());
        transaction.setTransactionAmount(afv.getAmount());
        transaction.setFeeIndicator(afv.getFeeIndicator());
        transaction.setFee(afv.getFee());
        transaction.setTransactionDate(afv.getTransactionDate());
        transaction.setValueDate(afv.getTransactionDate());
        transaction.setMerchantType(afv.getMerchantType());
        transaction.setTerminalId(afv.getTerminalId());
        transaction.setCurrencyCode(afv.getCurrencyCode());
        transaction.setCustomerReference(afv.getCustomerReference());
        transaction.setFromAccountNumber(afv.getAccountNumber());
        transaction.setFromAccountType(afv.getAccountType() != null ? afv.getAccountType() : "10");
        transaction.setToAccountType(afv.getToAccountType() != null ? afv.getToAccountType() : "00");
        transaction.setCardData1(afv.getCardData1());
        transaction.setCardData2(afv.getCardData2());
        transaction.setBit22(afv.getBit22());
        transaction.setBillerCode(afv.getBillerCode());
        transaction.setProductCode(afv.getProductCode());
        transaction.setProviderCode(afv.getProviderCode());

        transaction.setCreated(new Date());
        transaction.setCreatedby(afv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(afv.getCustomerId());
       
        
        return transaction;
    }
}
