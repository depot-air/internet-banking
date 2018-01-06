package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TransferView;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/13/11
 * Time: 2:01 PM
 */
public class TransferViewTransformer implements Transformer {
    public TransferViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        TransferView tv = (TransferView) view;
        transaction.setTransactionType(tv.getTransactionType());
        transaction.setCardNumber(tv.getCardNumber());
        transaction.setTransactionAmount(tv.getAmount());
        transaction.setFeeIndicator(tv.getFeeIndicator());
        transaction.setFee(tv.getFee());
        transaction.setTransactionDate(tv.getTransactionDate());
        transaction.setValueDate(tv.getValueDate());
        transaction.setMerchantType(tv.getMerchantType());
        transaction.setTerminalId(tv.getTerminalId());
        transaction.setCurrencyCode(tv.getCurrencyCode());
        transaction.setCustomerReference(tv.getCustomerReference());
        //transaction.setFromAccountNumber(tv.getAccountNumber());
        transaction.setFromAccountType("10");	//tv.getAccountType());
        transaction.setToAccountType(tv.getToAccountType());
        transaction.setToAccountNumber(tv.getCustomerReference());
        transaction.setDescription(tv.getDescription());
        transaction.setToBankCode(tv.getBillerCode());
        transaction.setCardData1(tv.getCardData1());
        transaction.setCardData2(tv.getCardData2());
        transaction.setStan(tv.getStan());
        transaction.setBit42(tv.getBit42());
        transaction.setBit43(tv.getBit43());
        transaction.setBit47(tv.getInquiryRefNumber());
        transaction.setTerminalAddress(tv.getTerminalAddress());
        transaction.setBillerCode(tv.getBillerCode());
        transaction.setProviderCode(tv.getProviderCode());

        transaction.setApprovalNumber(tv.getCustRefAtmb());     //temporary ditaruh di status
        transaction.setStatus(tv.getStatus());
        transaction.setCreated(new Date());
        transaction.setCreatedby(tv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(tv.getCustomerId());
        return transaction;
    }
}


