package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.LotteryView;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 1/31/12
 * Time: 9:43 PM
 */
public class LotteryTransactionViewTransformer implements Transformer {
    public LotteryTransactionViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        LotteryView  lv = (LotteryView) view;
        transaction.setCardNumber(lv.getCardNumber());
        transaction.setFromAccountNumber(lv.getAccountNumber());
        transaction.setMerchantType(lv.getMerchantType());
        transaction.setTerminalId(lv.getTerminalId());
        transaction.setCurrencyCode(lv.getCurrencyCode());
        transaction.setFromAccountType("00");   //requested by bu Uma
        transaction.setToAccountType("00");
        //transaction.setFreeData1(lv.getPeriodMonth());
        //transaction.setFreeData2(lv.getPeriodYear());
        
        //transaction.setFreeData3("" + lv.getStartPointNumber());
        //transaction.setFreeData4("" + lv.getEndPointNumber());

        transaction.setTransactionType(lv.getTransactionType());
        transaction.setTransactionDate(lv.getTransactionDate());
        transaction.setValueDate(lv.getTransactionDate());
        transaction.setCardData1(lv.getCardData1());
        transaction.setCardData2(lv.getCardData2());

        transaction.setCreated(new Date());
        transaction.setCreatedby(lv.getCustomerId());
        transaction.setUpdated(new Date());
        transaction.setUpdatedby(lv.getCustomerId());

        return transaction;
    }
}
