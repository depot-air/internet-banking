package com.dwidasa.engine.service.transform;

import java.util.Date;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.BaseView;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/23/11
 * Time: 6:38 PM
 */
public class AeroTicketingViewTransformer implements Transformer {
    public AeroTicketingViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        AeroTicketingView atv = (AeroTicketingView) view;
        transaction.setTransactionType(atv.getTransactionType());
        transaction.setCardNumber(atv.getCardNumber());
        transaction.setTransactionAmount(atv.getTotal());	//getAmount()
        transaction.setFeeIndicator(atv.getFeeIndicator());
        transaction.setFee(atv.getFee());
        transaction.setTransactionDate(atv.getTransactionDate());
        transaction.setValueDate(atv.getTransactionDate());
        transaction.setMerchantType(atv.getMerchantType());
        transaction.setTerminalId(atv.getTerminalId());
        transaction.setCurrencyCode(atv.getCurrencyCode());
        transaction.setCustomerReference(atv.getCustomerReference());
        transaction.setFromAccountNumber(atv.getAccountNumber());
        transaction.setFromAccountType(atv.getAccountType() != null ? atv.getAccountType() : "10");
        transaction.setToAccountType(atv.getToAccountType() != null ? atv.getToAccountType() : "00");
        transaction.setCardData1(atv.getCardData1());
        transaction.setCardData2(atv.getCardData2());
        transaction.setBit22(atv.getBit22());
        transaction.setBillerCode(atv.getBillerCode());
        transaction.setProductCode(atv.getProductCode());
        transaction.setProviderCode(atv.getProviderCode());

        transaction.setCreated(new Date());
        transaction.setCreatedby(atv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(atv.getCustomerId());
       
        
        return transaction;
    }
}
