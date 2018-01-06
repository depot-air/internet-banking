package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.payment.PlnPaymentPrint;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/23/11
 * Time: 8:18 PM
 */
public class TransactionHistoryDetail {
    @Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getNumberInstance(locale);

    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, locale);

    @Property
    private String status;

    @Property
    private String transactionName;

    @Property
    @Persist
    private Transaction transaction;

    @Property
    private String showReprint;

    @Property(read = false)
    private String pageName;

    @Inject
    private TransactionService transactionService;

    @Inject
    private TransactionTypeService transactionTypeService;

    @Inject
    private Messages messages;

    @InjectPage
    private PlnPaymentPrint plnPaymentPrint;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    public void onActivate(Long id) {
        transaction = transactionService.get(id);

        if (transaction.getResponseCode().equals(com.dwidasa.engine.Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        }
        else if (transaction.getResponseCode().equals(com.dwidasa.engine.Constants.TIMEOUT_CODE)) {
            status = messages.get("pending");
        }
        else {
            status = messages.get("failed");
        }

        transactionName = transactionTypeService.get(transaction.getTransactionType()).getDescription();
        showReprint = transaction.getResponseCode().equals(com.dwidasa.engine.Constants.TIMEOUT_CODE) ? "true" : "false";
    }

    public String getPageName() {
        String result = "";
        if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.PLN_PAYMENT_CODE)) {
            result = "/payment/plnPaymentPrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.PLN_PURCHASE_CODE)) {
            result = "/purchase/plnPurchasePrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.TELKOM_PAYMENT_CODE)) {
            result = "/payment/telkomPaymentPrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.TRANSPORTATION_PAYMENT_CODE)) {
            result = "/payment/trainPaymentPrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.ENTERTAINMENT_PAYMENT_CODE)) {
            result = "/payment/tvPaymentPrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.INTERNET_PAYMENT_CODE)) {
            result = "/payment/internetPaymentPrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.TELCO_PAYMENT_CODE)) {
            result = "/payment/hpPaymentPrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE)) {
            result = "/delima/cashInDelimaPrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CODE)) {
            result = "/delima/cashOutDelimaPrint";
        }
        else if (transaction.getTransactionType().equals(com.dwidasa.engine.Constants.REFUND_DELIMA_CODE)) {
            result = "/delima/refundDelimaPrint";
        }


        transactionId = transaction.getId();
        return result + "?reprint=1";
    }
}
