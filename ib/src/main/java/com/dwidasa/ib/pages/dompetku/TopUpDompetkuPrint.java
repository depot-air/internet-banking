package com.dwidasa.ib.pages.dompetku;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.CashInDompetkuView;
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
public class TopUpDompetkuPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_DATE, threadLocale.getLocale());

    @InjectPage
    private TopUpDompetkuReceipt TopUpDompetkuReceipt;

    @Property
    private CashInDompetkuView cashInDompetkuView;

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
        	cashInDompetkuView = (CashInDompetkuView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
        	cashInDompetkuView.setTransactionType(com.dwidasa.engine.Constants.TRANSPORTATION_PAYMENT_REP_CODE);
            //paymentService.reprint(cashInDompetkuView, transactionId);
            return;
        }

        cashInDompetkuView = TopUpDompetkuReceipt.getCashInDompetkuView();
    }
}
