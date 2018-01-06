package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.CashToBankDelimaView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.*;
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
public class CashToBankDelimaReceipt {
    @Inject
    private ThreadLocale threadLocale;
    @Property
    private String status;
    @Property
    private String nowDate;
    @InjectPage
    private CashToBankDelimaInput cashToBankDelimaInput;
    @Persist
    private CashToBankDelimaView cashToBankDelimaView;
    @Inject
    private Messages messages;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT,
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
        if (cashToBankDelimaView.getResponseCode() != null && cashToBankDelimaView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }

    }

    public Object onActivate() {
        if (cashToBankDelimaView == null) {
            return cashToBankDelimaInput;
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
        return cashToBankDelimaInput;
    }

    public CashToBankDelimaView getCashToBankDelimaView() {
        return cashToBankDelimaView;
    }

    public void setCashToBankDelimaView(CashToBankDelimaView cashToBankDelimaView) {
        this.cashToBankDelimaView = cashToBankDelimaView;
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
			if (cashToBankDelimaView.getResponseCode() != null && cashToBankDelimaView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
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
