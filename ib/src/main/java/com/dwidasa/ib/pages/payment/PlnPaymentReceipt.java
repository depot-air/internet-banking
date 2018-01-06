package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:54
 */
public class PlnPaymentReceipt {
    @Persist
    private PlnPaymentView plnPaymentView;

    @Property
    private String status;

    @Property
    private String paidPeriods;

    @Property
    private String vStandMeter;

    @Property
    private String vGspRef;

    @Property
    private String vNoteReceipt;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private TransactionService transactionService;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    public void setPlnPaymentView(PlnPaymentView plnPaymentView) {
        this.plnPaymentView = plnPaymentView;
    }

    public Object onActivate() {
        if (plnPaymentView == null) {
            return PlnPaymentInput.class;
        }
        return null;
    }

    
    public void setupRender() {
    	sessionManager.setSessionLastPage(PlnPaymentReceipt.class.toString());
    	sessionManager.setSmsTokenSent(false);
    	paidPeriods = plnPaymentView.getPaidPeriods(threadLocale.getLocale());
        if (plnPaymentView.getResponseCode() != null && plnPaymentView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        String bit48ValueOfBill = plnPaymentView.getBit48().substring(103);;
        vStandMeter = bit48ValueOfBill.substring(63, 71) + " - " + bit48ValueOfBill.substring(71, 79);
        if (isGsp()) {
    		vGspRef = plnPaymentView.getGspRef();
    	} else {
            vGspRef = plnPaymentView.getBit48().substring(4, 36);    		
    	}

        int nBill = Integer.parseInt(plnPaymentView.getBit48().substring(0, 1));
        int totBill = Integer.parseInt(plnPaymentView.getBit48().substring(2, 4));
        if (totBill > 4) {
            vNoteReceipt = "Anda masih memiliki tunggakan " + (totBill - nBill) + " bulan";
        }
        
        
    }

    public PlnPaymentView getPlnPaymentView() {
        return plnPaymentView;
    }
    
    @Persist
    private boolean fromHistory;
    
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }
    
    @DiscardAfter
    Object onSuccess() {
        return TransactionHistoryResult.class;
    }

    public boolean isGsp() {
    	return plnPaymentView.getProviderCode().equals(Constants.GSP.PROVIDER_CODE);
    }    

    @Property
    private String _footer;

    public String[] getFooters()
    {
    	//informasiStruk mengandung \n, ganti dg <br>
        if (plnPaymentView != null && plnPaymentView.getInformasiStruk() != null ){
            String[] words = plnPaymentView.getInformasiStruk().split("\n");
            return words;
        }
        else {
            return new String[0];
        }

    }
    
    
    public String getStatusDetil(){
    	
    	try {
			
    		Transaction transaction = transactionService.get(transactionId);
        	
        	if (transaction.getStatus().equals(com.dwidasa.engine.Constants.SUCCEED_STATUS)) {
        		status = messages.get("success");
        	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.PENDING_STATUS)) {
        		status = messages.get("pending");
        	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.FAILED_STATUS)) {
        		status = messages.get("pending");
        	}
        	
		} catch (Exception e) {
			if (plnPaymentView.getResponseCode() != null && plnPaymentView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
	            status = messages.get("success");
	        } else {
	            status = messages.get("failed");
	        }
		}
    	
    	
    	return status;
    }

    
    public String getStatusHistory(){
    	try {
			
    		Transaction transaction = transactionService.get(transactionId);
        	
        	if (transaction.getStatus().equals(com.dwidasa.engine.Constants.SUCCEED_STATUS)) {
        		status = "";
        	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.PENDING_STATUS)) {
        		status = messages.get("pending");
        	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.FAILED_STATUS)) {
        		status = messages.get("pending");
        	}
        	
		} catch (Exception e) {
			status = "";// TODO: handle exception
		}
    	
    	
    	return status;
    }
    
}
