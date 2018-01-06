package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 21/10/11
 * Time: 9:56
 */
public class NonTagListPaymentPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.DATEFORMAT.DDMMMYY, threadLocale.getLocale());

    @InjectPage
    private NonTagListPaymentReceipt nonTagListPaymentReceipt;

    @Property
    private NonTagListPaymentView nonTagListPaymentView;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @ActivationRequestParameter
    private String reprint;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private PaymentService paymentService;

    void setupRender() {
        if (reprint != null) {
        	nonTagListPaymentView = (NonTagListPaymentView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
        	nonTagListPaymentView.setTransactionType(com.dwidasa.engine.Constants.NONTAGLIST_PAYMENT_REP_CODE);
            paymentService.reprint(nonTagListPaymentView, transactionId);
            return;
        }

        nonTagListPaymentView = nonTagListPaymentReceipt.getNonTagListPaymentView();
    }

    @Property
    private String _footer;

    public String[] getFooters()
    {
        String[] words = nonTagListPaymentView.getInformasiStruk().split("\n");        
        return words;
    }
}
