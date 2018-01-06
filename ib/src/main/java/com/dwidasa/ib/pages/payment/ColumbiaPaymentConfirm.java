package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.RequestGlobals;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.ColumbiaPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.common.GeneralUtil;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:10
 */
public class ColumbiaPaymentConfirm {
    @Property(write = false)
    @Persist
    private ColumbiaPaymentView columbiaPaymentView;

    @Property
    private int tokenType;

    @InjectPage
    private ColumbiaPaymentReceipt columbiaPaymentReceipt;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CacheManager cacheManager;
    
    @Inject
    private SessionManager sessionManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Property
    private TokenView tokenView;

    @Inject
    private OtpManager otpManager;
    
    @Property
    private String paidPeriods;

    @Property
    private String paidPeriod1;

    @Property
    private String paidPeriod2;

    @Property
    private String paidPeriod3;
    
    @Property
    private String totalBill;
    
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat("dd MMMM yyyy", threadLocale.getLocale());



    public void setColumbiaPaymentView(ColumbiaPaymentView columbiaPaymentView) {
		this.columbiaPaymentView = columbiaPaymentView;
	}

	public Object onActivate() {
        if (columbiaPaymentView == null) {
            return ColumbiaPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
//        paidPeriods = columbiaPaymentView.getPaidPeriods();
//        
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM yy");
        int base = 102;
        int len = 111;
        

        if(columbiaPaymentView.getTransType().equals("C")){
        	columbiaPaymentView.setTransType("Close Payment");
        }else if(columbiaPaymentView.getTransType().equals("O")){
        	columbiaPaymentView.setTransType("Open Payment");
        }
        
        setTokenType();
    }
    
    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
            tokenView.setChallenge(columbiaPaymentView.getCustomerReference());
        }
    }
    

    
    void onValidateFromForm() {
        try {
        	if (otpManager.validateToken(columbiaPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
        		
        		setColumbiaPaymentView((ColumbiaPaymentView) paymentService.execute(columbiaPaymentView));
        	}
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
       
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }
    
    @DiscardAfter
    public Object onSuccess() {
    	columbiaPaymentView.setTransactionDate(new Date());
        columbiaPaymentReceipt.setColumbiaPaymentView(columbiaPaymentView);
        return columbiaPaymentReceipt;
    }
    
//    public String getStrBlTh() {
//    	if (columbiaPaymentView == null || columbiaPaymentView.getBillPeriods() == null) return "";
//    	StringBuilder sbBlTh = new StringBuilder();
//        for (Date period : columbiaPaymentView.getBillPeriods()) {
//        	sbBlTh.append(", ").append(GeneralUtil.fmtMonthYear.print(period.getTime()));
//        }
//        if (sbBlTh.length() > 1) sbBlTh.delete(0, 1);
//        return sbBlTh.toString();
//    }
    
//    public String getMasked(String str) {
//    	return GeneralUtil.getPlnMasked(str);
//    }
    
}
