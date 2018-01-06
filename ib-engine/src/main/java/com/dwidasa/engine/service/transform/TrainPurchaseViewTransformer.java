package com.dwidasa.engine.service.transform;

import java.util.Date;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TrainPurchaseView;

public class TrainPurchaseViewTransformer implements Transformer {

	@Override
	public Transaction transformTo(BaseView view, Transaction transaction) {
		TrainPurchaseView pv = (TrainPurchaseView) view;
        transaction.setTransactionType(pv.getTransactionType());
        transaction.setCardNumber(pv.getCardNumber());
        transaction.setTransactionAmount(pv.getAmount());
        transaction.setFeeIndicator("D");
        transaction.setFee(pv.getFee());
        transaction.setTransactionDate(pv.getTransactionDate());
        transaction.setValueDate(pv.getTransactionDate());
        transaction.setMerchantType(pv.getMerchantType());
        transaction.setTerminalId(pv.getTerminalId());
        transaction.setCurrencyCode("360");
        transaction.setCustomerReference(pv.getBookingCode());
        transaction.setFromAccountNumber(pv.getAccountNumber());
        transaction.setFromAccountType(pv.getAccountType());
        transaction.setToAccountType("00");
        transaction.setProviderCode("PAC");

        transaction.setCreated(new Date());
        transaction.setCreatedby(pv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(pv.getCustomerId());

        return transaction;
	}
    
}
