package com.dwidasa.ib.components;

import com.dwidasa.engine.Constants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 21/07/11
 * Time: 17:38
 */
public class PaymentReceipt {
    @Parameter
    @Property
    private String referenceNumber;

    @Parameter
    @Property
    private String cardNumber;

    @Parameter
    @Property
    private String accountNumber;

    @Parameter
    @Property
    private String billerName;

    @Parameter
    @Property
    private String providerName;

    @Parameter
    @Property
    private String customerReference;

    @Parameter
    @Property
    private long amount;

    @Parameter
    private String transactionType;

    @Parameter
    private Date transactionDate;

    @Parameter
    @Property
    private String status;

    @Property
    private String dateTime;

    @Property
    private String transactionName;

    @Property
    private String fromIdMessage;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    public void setupRender() {
        setupLabel();
    }

    private void setupLabel() {
        if (transactionType.equalsIgnoreCase(Constants.PLN_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdPln");
            transactionName = messages.get("transactionNamePln");
        } else if (transactionType.equalsIgnoreCase(Constants.TELCO_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdTelkom");
            transactionName = messages.get("transactionNameTelkom");
        } else if (transactionType.equalsIgnoreCase(Constants.INTERNET_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdInternet");
            transactionName = messages.get("transactionNameInternet");
        } else if (transactionType.equalsIgnoreCase(Constants.CC_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdCredit");
            transactionName = messages.get("transactionNameCredit");
        } else if (transactionType.equalsIgnoreCase(Constants.TELCO_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdHandphone");
            transactionName = messages.get("transactionNameHandphone");
        } else if (transactionType.equalsIgnoreCase(Constants.ENTERTAINMENT_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdTv");
            transactionName = messages.get("transactionNameTv");
        } else if (transactionType.equalsIgnoreCase(Constants.WATER.TRANSACTION_TYPE.POSTING)) {
            fromIdMessage = messages.get("fromIdWater");
            transactionName = messages.get("transactionNameWater");
        } else if (transactionType.equalsIgnoreCase(Constants.MULTIFINANCE_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdLoan");
            transactionName = messages.get("transactionNameLoan");
        }
    }
}
