package com.dwidasa.engine.service.transform;

import java.math.BigDecimal;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.BaseView;

public class AccountStatementViewTransformer implements Transformer {
    public AccountStatementViewTransformer() {
    }

    public Transaction transformTo(BaseView view, Transaction transaction) {
        AccountStatementView av = (AccountStatementView) view;

        transaction.setTransactionType(av.getTransactionType());
/*        
        transaction.setMerchantType(av.getMerchantType());
        transaction.setTerminalId(av.getTerminalId());
        transaction.setCardNumber(av.getCardNumber());
*/        
        transaction.setFromAccountNumber(av.getAccountNumber());
        transaction.setFromAccountType("10");
        transaction.setToAccountType("00");
        transaction.setTransactionAmount(BigDecimal.ZERO);
        transaction.setCurrencyCode(av.getCurrencyCode());

        return transaction;
    }
}
