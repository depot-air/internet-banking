package com.dwidasa.engine.service.transform;

import java.util.Date;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.view.BaseView;

public class TransferBatchTransformer implements Transformer {
    public TransferBatchTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
    	TransferBatch transferBatch = (TransferBatch) view;
//        transaction.setCardNumber(transferBatch.getCardNumber());
        transaction.setFromAccountNumber(transferBatch.getAccountNumber());
        transaction.setToAccountType("00");
//        transaction.setCustomerReference(pv.getCustomerReference());
        transaction.setTransactionDate(transferBatch.getTransactionDate());
        transaction.setValueDate(transferBatch.getTransactionDate());
//        transaction.setCardData1(pv.getCardData1());
//        transaction.setCardData2(pv.getCardData2());

        transaction.setCreated(new Date());
        transaction.setCreatedby(transferBatch.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(transferBatch.getCustomerId());

        return transaction;
    }
}

