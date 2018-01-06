package com.dwidasa.ib.custom;

import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.ib.Constants;
import org.apache.tapestry5.ValueEncoder;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/19/11
 * Time: 9:02 PM
 */
public class AccountViewEncoder implements ValueEncoder<AccountView> {
    public AccountViewEncoder() {
    }

    public String toClient(AccountView value) {
        //-- serialized only what we need, no less no more !!
        return value.getAccountNumber() + Constants.SEPARATOR + value.getAccountName() + Constants.SEPARATOR +
                value.getProductName() + Constants.SEPARATOR + value.getCurrencyCode() + Constants.SEPARATOR +
                String.valueOf(value.getAvailableBalance().toString()) + Constants.SEPARATOR +
                value.getCustomerName();
    }

    public AccountView toValue(String clientValue) {
        AccountView av = new AccountView();
        String items[] = clientValue.split(Constants.SEPARATOR);

        av.setAccountNumber(items[0]);
        av.setAccountName(items[1]);
        av.setProductName(items[2]);
        av.setCurrencyCode(items[3]);
        av.setAvailableBalance(new BigDecimal(items[4]));
        av.setCustomerName(items[5]);

        return av;
    }
}
