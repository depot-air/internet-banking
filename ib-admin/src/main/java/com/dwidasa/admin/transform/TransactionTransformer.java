package com.dwidasa.admin.transform;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dwidasa.admin.view.TransactionView;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;

public class TransactionTransformer implements Transformer {

    public TransactionTransformer() {
    }

    public List transform(List entities) {
        List<TransactionView> result = new ArrayList<TransactionView>();
        for (Object e : entities) {
        	Transaction t = (Transaction) e;
            TransactionView tv = new TransactionView();
            tv.setValueDate(t.getValueDate());
            tv.setFromAccountNumber(t.getFromAccountNumber());
            tv.setTransactionType(t.getTransactionType());
            tv.setTransactionDescription(t.getTransactionTypeModel().getDescription());
            tv.setCustomerReference(t.getCustomerReference());
            tv.setTransactionAmount(t.getTransactionAmount());
            tv.setFee(t.getFee());
            tv.setStatus(t.getStatus());

            if (Constants.FAILED_STATUS.equals(t.getStatus())){
                tv.setFullStatus(t.getStatus() + " (" + t.getResponseCode() + ")");
            }
            else {
                tv.setFullStatus(t.getStatus());
            }

            result.add(tv);
        }

        return result;
    }
}
