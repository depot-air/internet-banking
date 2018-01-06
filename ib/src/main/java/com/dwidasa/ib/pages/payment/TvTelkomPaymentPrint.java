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

import com.dwidasa.engine.model.view.TvPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 11/10/11
 * Time: 0:49
 */
public class TvTelkomPaymentPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMyy", threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate2 = new SimpleDateFormat(Constants.SHORT_DATE, threadLocale.getLocale());

    @Property
    private DateFormat dateList = new SimpleDateFormat("MMMyy", threadLocale.getLocale());
    
    @Property
    private DateFormat transDate = new SimpleDateFormat("dd-MMM-yy", threadLocale.getLocale());
    
    @InjectPage
    private TvPaymentReceipt tvPaymentReceipt;

    @Property
    private TvPaymentView tvPaymentView;

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
            tvPaymentView = (TvPaymentView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
            tvPaymentView.setTransactionType(com.dwidasa.engine.Constants.ENTERTAINMENT_PAYMENT_REP_CODE);
            paymentService.reprint(tvPaymentView, transactionId);
            return;
        }

        tvPaymentView = tvPaymentReceipt.getTvPaymentView();
        
    }
    
    public boolean isCentrin(){  	
    	if (tvPaymentView.getProductCode().equals("0001"))
    		return true;
    	
    	return false;
    }
    
    public boolean isTelkomVision(){
    	if (tvPaymentView.getProductCode().equals("A07"))
    		return true;
    	
    	return false;
    }

    public boolean isFinnet() {
    	if (tvPaymentView.getProviderCode().equals(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE)) {
    		return true;
    	}
    	return false;
    }
    
}
