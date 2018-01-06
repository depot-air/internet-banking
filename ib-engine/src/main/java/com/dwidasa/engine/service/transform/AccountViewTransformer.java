package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.BaseView;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/20/11
 * Time: 1:44 PM
 */
public class AccountViewTransformer implements Transformer {
    public AccountViewTransformer() {
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
