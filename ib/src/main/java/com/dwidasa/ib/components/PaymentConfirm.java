package com.dwidasa.ib.components;

import com.dwidasa.engine.Constants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 21/07/11
 * Time: 15:27
 */
public class PaymentConfirm {
    @Parameter
    private String transactionType;
    @Parameter
    @Property
    private String cardNumber;
    @Parameter
    @Property
    private String accountNumber;
    @Parameter
    @Property
    private String billerName;
    @Property
    @Parameter
    private String customerReference;
    @Parameter
    @Property
    private long amount;
    @Parameter
    @Property
    private String tokenChallenge;
    @Parameter
    @Property
    private int tokenType;
    @Parameter
    @Property
    private String token;
    @Property
    private String billerMessage;
    @Property
    private String fromIdMessage;
    @Inject
    private Messages messages;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

    public void setupRender() {
        setupLabel();
    }

    private void setupLabel() {
        if (transactionType.equalsIgnoreCase(Constants.PLN_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdPln");
            billerMessage = messages.get("billerPln");
        } else if (transactionType.equalsIgnoreCase(Constants.TELCO_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdTelkom");
            billerMessage = messages.get("billerTelkom");
        } else if (transactionType.equalsIgnoreCase(Constants.INTERNET_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdInternet");
            billerMessage = messages.get("billerInternet");
        } else if (transactionType.equalsIgnoreCase(Constants.CC_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdCredit");
            billerMessage = messages.get("billerCredit");
        } else if (transactionType.equalsIgnoreCase(Constants.TELCO_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdHandphone");
            billerMessage = messages.get("billerHandphone");
        } else if (transactionType.equalsIgnoreCase(Constants.ENTERTAINMENT_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdTv");
            billerMessage = messages.get("billerTv");
        } else if (transactionType.equalsIgnoreCase(Constants.WATER.TRANSACTION_TYPE.POSTING)) {
            fromIdMessage = messages.get("fromIdWater");
            billerMessage = messages.get("billerWater");
        } else if (transactionType.equalsIgnoreCase(Constants.MULTIFINANCE_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdLoan");
            billerMessage = messages.get("billerLoan");
        }
    }
}
