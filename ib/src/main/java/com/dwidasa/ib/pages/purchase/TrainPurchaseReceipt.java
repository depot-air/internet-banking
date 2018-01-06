package com.dwidasa.ib.pages.purchase;

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
import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;

public class TrainPurchaseReceipt {
	@Property
	private EvenOdd evenOdd;
	
	@Property
    private String status;
	
	@Persist
	private TrainPurchaseView view;
	
	@Property
	private TrainPassenger passenger;
	
	@Inject
	private TransactionService transactionService;
	
	@Inject
    private Messages messages;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

	public TrainPurchaseView getView() {
		return view;
	}

	public void setView(TrainPurchaseView view) {
		this.view = view;
	}
	
	@Inject
    private ThreadLocale threadLocale;
	
	@Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
	
	@Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
	
	@Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_DATE, threadLocale.getLocale());
	
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
			if (view.getResponseCode() != null && view.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
	            status = messages.get("success");
	        } else {
	            status = messages.get("failed");
	        }
		}
    	
    	
    	return status;
    }
	
	void setupRender() {
		evenOdd = new EvenOdd();
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
    
    @Persist
    private boolean fromHistory;
    
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }
    
    @DiscardAfter
    Object onSuccessFromForm() {
        return TransactionHistoryResult.class;
    }

}
