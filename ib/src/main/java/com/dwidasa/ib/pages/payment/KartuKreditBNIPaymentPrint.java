package com.dwidasa.ib.pages.payment;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.KartuKreditBNIPaymentView;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 07/10/11
 * Time: 17:29
 */
public class KartuKreditBNIPaymentPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate2 = new SimpleDateFormat(Constants.SHORT_DATE, threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat listDate = new SimpleDateFormat("MMMyy", threadLocale.getLocale());

    @InjectPage
    private KartuKreditBNIPaymentReceipt KartuKreditBNIPaymentReceipt;

    @Property
    private KartuKreditBNIPaymentView kartuKreditBNIPaymentView;

    @ActivationRequestParameter
    private String reprint;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private PaymentService paymentService;

    void setupRender() {
//        if (reprint != null) {
//        	kartuKreditBNIPaymentView = (kartuKreditBNIPaymentView) EngineUtils.deserialize(
//                    transactionDataService.getByTransactionFk(transactionId));
//        	kartuKreditBNIPaymentView.setTransactionType(com.dwidasa.engine.Constants.TELKOM_PAYMENT_REP_CODE);
//            paymentService.reprint(kartuKreditBNIPaymentView, transactionId);
//            return;
//        }

    	kartuKreditBNIPaymentView = KartuKreditBNIPaymentReceipt.getKartuKreditBNIPaymentView();
    }
    
    public BigDecimal getTotal(){
    	BigDecimal total = kartuKreditBNIPaymentView.getAmount().add(kartuKreditBNIPaymentView.getFee());
    	return total;
    }
    
}
