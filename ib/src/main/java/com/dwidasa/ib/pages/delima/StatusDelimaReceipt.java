package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.CashInDelimaView;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/26/11
 * Time: 00:31 am
 */
public class StatusDelimaReceipt {

    @Persist
    private CashInDelimaView cashInDelimaView;
    @Property
    private String delimaTransaction;
    @Inject
    private Messages messages;
    @Inject
    private ThreadLocale threadLocale;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    @Property
    private BigDecimal total;

    public Object onActivate() {
        if (getCashInDelimaView() == null) {
            setCashInDelimaView(new CashInDelimaView());
        }
        return null;
    }

    void setTotal() {
        total = cashInDelimaView.getAmount().add(cashInDelimaView.getProviderFee());
    }

    void setupRender() {
        setTotal();
        if (cashInDelimaView.getTransactionType().equals(Constants.CASHIN_DELIMA_CHK_CODE)) {
            delimaTransaction = messages.get("cashIn");
        } else if (cashInDelimaView.getTransactionType().equals(Constants.CASHOUT_DELIMA_CHK_CODE)) {
            delimaTransaction = messages.get("cashOut");
        } else {
            delimaTransaction = messages.get("refund");
        }
    }
    
    @Persist
    private boolean fromHistory;
    
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return StatusDelimaInput.class;
    }

    public CashInDelimaView getCashInDelimaView() {
        return cashInDelimaView;
    }

    public void setCashInDelimaView(CashInDelimaView cashInDelimaView) {
        this.cashInDelimaView = cashInDelimaView;
    }
}
