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

import com.dwidasa.engine.model.view.WaterPaymentView;
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
public class WaterPaymentPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM/yyyy", threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate2 = new SimpleDateFormat(Constants.SHORT_DATE, threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());

    @InjectPage
    private WaterPaymentReceipt waterPaymentReceipt;

    @Property
    private WaterPaymentView waterPaymentView;

    @ActivationRequestParameter
    private String reprint;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private PaymentService paymentService;

    void setupRender() {
        if (reprint != null) {
        	waterPaymentView = (WaterPaymentView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
        	waterPaymentView.setTransactionType(com.dwidasa.engine.Constants.TELKOM_PAYMENT_REP_CODE); //Masih telkom
            paymentService.reprint(waterPaymentView, transactionId);
            return;
        }

        waterPaymentView = waterPaymentReceipt.getWaterPaymentView();
    }
    
    public boolean isPalyja() {    	
    	return waterPaymentView.getBillerCode().equals(com.dwidasa.engine.Constants.WATER.BILLER_CODE.PALYJA);
    }
    
    public String getBillerName(){
    	
    	System.out.println("BILLER PAM "+waterPaymentView.getBillerName());
    	
    	if(waterPaymentView.getBillerName().equals(com.dwidasa.engine.Constants.WATER.BILLER_NAME.PAMSemarang)){
    		return "PAM Surabaya";
    	}else
    		return "";
    }
    
}
