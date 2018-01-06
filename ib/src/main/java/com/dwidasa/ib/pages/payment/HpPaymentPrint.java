package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.HpPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.base.BasePrintPage;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 21/10/11
 * Time: 9:22
 */
public class HpPaymentPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate = new SimpleDateFormat(com.dwidasa.ib.Constants.SHORT_DATE, threadLocale.getLocale());

    @InjectPage
    private HpPaymentReceipt hpPaymentReceipt;

    @Property
    private HpPaymentView hpPaymentView;

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
            hpPaymentView = (HpPaymentView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
            hpPaymentView.setTransactionType(Constants.TELCO_PAYMENT_REP_CODE);
            paymentService.reprint(hpPaymentView, transactionId);
            return;
        }

        hpPaymentView = hpPaymentReceipt.getHpPaymentView();
    }
    
    public boolean isTelkomsel() {    	
    	return (hpPaymentView.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE) && hpPaymentView.getProductCode().equals(Constants.POSTPAID_PRODCODE.TELKOMSEL_KATU_HALO));
    }
}
