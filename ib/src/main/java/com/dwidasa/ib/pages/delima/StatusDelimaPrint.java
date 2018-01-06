package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.CashInDelimaView;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 10/27/11
 * Time: 00:35 am
 * To change this template use File | Settings | File Templates.
 */
public class StatusDelimaPrint {
     @Inject
    private ThreadLocale threadLocale;
    @Property
    private CashInDelimaView cashInDelimaView;
    @InjectPage
    private StatusDelimaReceipt statusDelimaReceipt;
    @Property
    private String nominalTransfer;
    @Property
    private String biayaTransfer;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    @Property
    private BigDecimal total;
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());
    @Property
    private Date date;
    @Property
    private String subtitle;
    @Inject
    private Messages messages;


    void setupRender() {
        cashInDelimaView = statusDelimaReceipt.getCashInDelimaView();
        setTotal();
        setSubtitle();
        date = new Date();

    }

    void setSubtitle() {
        if (cashInDelimaView.getTransactionType().equals(Constants.CASHOUT_DELIMA_CHK_CODE)) {
            subtitle = messages.get("cashOut");
        } else {
            subtitle = messages.get("refund");
        }

    }

    public void setTotal() {
        total = cashInDelimaView.getAmount().add(cashInDelimaView.getProviderFee());

    }
}
