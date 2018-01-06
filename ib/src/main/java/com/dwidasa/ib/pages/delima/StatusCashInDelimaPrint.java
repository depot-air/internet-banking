package com.dwidasa.ib.pages.delima;

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
 * Time: 00:13 am
 */
public class StatusCashInDelimaPrint {
    @Inject
    private ThreadLocale threadLocale;
    @Property
    private CashInDelimaView cashInDelimaView;
    @InjectPage
    private StatusCashInDelimaReceipt statusCashInDelimaReceipt;
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



    void setupRender() {
        cashInDelimaView = statusCashInDelimaReceipt.getCashInDelimaView();
        setTotal();
        date = new Date();

    }

    public void setTotal() {
        total = cashInDelimaView.getAmount().add(cashInDelimaView.getProviderFee());

    }
}
