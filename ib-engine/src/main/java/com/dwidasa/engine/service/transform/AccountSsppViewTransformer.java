package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AccountSsppView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.LotteryView;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 4/23/12
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountSsppViewTransformer implements Transformer {
    public AccountSsppViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        AccountView av = (AccountView) view;

        transaction.setTransactionType(av.getTransactionType());
        transaction.setMerchantType(av.getMerchantType());
        transaction.setTerminalId(av.getTerminalId());
        transaction.setCardNumber(av.getCardNumber());
        transaction.setFromAccountNumber(av.getAccountNumber());
        transaction.setFromAccountType(av.getAccountType());
        transaction.setToAccountType("00");
        transaction.setTransactionAmount(BigDecimal.ZERO);
        transaction.setCurrencyCode(av.getCurrencyCode());

        return transaction;
    }
}