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
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 14:00
 */
public class TelkomPaymentReceipt {
    @Persist
    private TelkomPaymentView telkomPaymentView;

    @Property
    private String status;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    
    @Inject
    private TransactionService transactionService;
    
    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", threadLocale.getLocale());

    @Property
    private DateFormat listDate = new SimpleDateFormat("MMMyy", threadLocale.getLocale());
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    public Object onActivate() {
        if (telkomPaymentView == null) {
            return TelkomPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(TelkomPaymentReceipt.class.toString());
    	sessionManager.setSmsTokenSent(false);
        if (telkomPaymentView.getResponseCode() != null && telkomPaymentView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
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
			if (telkomPaymentView.getResponseCode() != null && telkomPaymentView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
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

    public void setTelkomPaymentView(TelkomPaymentView telkomPaymentView) {
        this.telkomPaymentView = telkomPaymentView;
    }

    public TelkomPaymentView getTelkomPaymentView() {
        return telkomPaymentView;
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
}
