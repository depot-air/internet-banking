package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
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

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:10
 */
public class PlnPaymentConfirm {
    @Property(write = false)
    @Persist
    private PlnPaymentView plnPaymentView;

    @Property
    private int tokenType;

    @Property
    private String paidPeriods;

    @Property
    private String paidPeriod1;

    @Property
    private String paidPeriod2;

    @Property
    private String paidPeriod3;

    @Property
    private String paidPeriod4;
    
    @Property
    private String totalBill;
    
    @InjectPage
    private PlnPaymentReceipt plnPaymentReceipt;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CacheManager cacheManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Property
    private TokenView tokenView;

    @Inject
    private OtpManager otpManager;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat("dd MMMM yyyy", threadLocale.getLocale());
    
    public void setPlnPaymentView(PlnPaymentView plnPaymentView) {
        this.plnPaymentView = plnPaymentView;
    }

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (plnPaymentView == null) {
            return PlnPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(PlnPaymentConfirm.class.toString());
        paidPeriods = plnPaymentView.getPaidPeriods(threadLocale.getLocale());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM yy");
        int base = 102;
        int len = 111;
        if (plnPaymentView.getAmount1() != null && plnPaymentView.getAmount1().doubleValue() > 0D) {
        	totalBill = "" + 1;
        	try {
				Date dpaidPeriod1 = sdf1.parse(plnPaymentView.getBit48().substring(base, base + 6));
				paidPeriod1 = sdf2.format(dpaidPeriod1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
        if (plnPaymentView.getAmount2() != null && plnPaymentView.getAmount2().doubleValue() > 0D) {
        	totalBill = "" + 2;
        	try        	{
				Date dpaidPeriod2 = sdf1.parse(plnPaymentView.getBit48().substring(base + len, base + len + 6));
				paidPeriod2 = sdf2.format(dpaidPeriod2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }  
        if (plnPaymentView.getAmount3() != null && plnPaymentView.getAmount3().doubleValue() > 0D) {
        	totalBill = "" + 3;
        	try {
				Date dpaidPeriod3 = sdf1.parse(plnPaymentView.getBit48().substring(base + 2 * len, base + 2 * len + 6));
				paidPeriod3 = sdf2.format(dpaidPeriod3);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
        if (plnPaymentView.getAmount4() != null && plnPaymentView.getAmount4().doubleValue() > 0D) {
        	totalBill = "" + 4;
        	int totalOuts = Integer.parseInt(plnPaymentView.getBit48().substring(1, 3));
        	if (totalOuts > 4) {
        		totalBill +=  " dari " + totalOuts + " bulan";
        	}
			try {
				Date dpaidPeriod4 = sdf1.parse(plnPaymentView.getBit48().substring(base + 3 * len, base + 3 * len + 6));
				paidPeriod4 = sdf2.format(dpaidPeriod4);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        } 
        //pv.getBillPeriods().add(sdf2.parse(bit48.substring(base + i * len, base + i * len + 6)));
        setTokenType();
    }

    @DiscardAfter
    public Object onSuccess() {
        plnPaymentReceipt.setPlnPaymentView(plnPaymentView);
        return plnPaymentReceipt;
    }

    void onValidateFromForm() {
        try {
        	if (otpManager.validateToken(plnPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
        		setPlnPaymentView((PlnPaymentView) paymentService.execute(plnPaymentView));
        	}
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
        if (plnPaymentView.getSave() && plnPaymentView.getInputType().equalsIgnoreCase("M")) {
            try {
                paymentService.register(plnPaymentView.transform());
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }
}
