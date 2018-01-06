package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.SessionManager;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/25/11
 * Time: 00:48 am
 */
public class CashInDelimaReceipt {
    @Inject
    private ThreadLocale threadLocale;
    @Property
    private String status;
    @Property
    private String nowDate;
    @InjectPage
    private CashInDelimaInput cashInDelimaInput;
    @Persist
    private CashInDelimaView cashInDelimaView;
    @Inject
    private Messages messages;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());
    
  
    
    @Inject
    private TransactionService transactionService;

    @Inject
    private SessionManager sessionManager;
  
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());


    public void setupRender() {
        if (cashInDelimaView.getResponseCode() != null && cashInDelimaView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }

    }

    public Object onActivate() {
        if (cashInDelimaView == null) {
            return cashInDelimaInput;
        }
        return null;
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
    Object onSelectedFromBack() {
        return cashInDelimaInput;
    }

    public CashInDelimaView getCashInDelimaView() {
        return cashInDelimaView;
    }

    public void setCashInDelimaView(CashInDelimaView cashInDelimaView) {
        this.cashInDelimaView = cashInDelimaView;
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
			if (cashInDelimaView.getResponseCode() != null && cashInDelimaView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
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
