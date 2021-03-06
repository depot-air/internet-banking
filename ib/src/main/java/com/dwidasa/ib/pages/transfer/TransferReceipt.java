package com.dwidasa.ib.pages.transfer;

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
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/15/11
 * Time: 00:24 am
 */
public class TransferReceipt {
	@Property
    private String status;
    
    @Inject
    private TransactionService transactionService;

    @Inject
    private Messages messages;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Property
    private String typeTransfer;

    @Property
    private String nowDate;

    @Persist
    private TransferView transferView;


    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());

    @Inject
    private SessionManager sessionManager;
    
    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }
    
    public TransferView getTransferView() {
    	return transferView;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(TransferReceipt.class.toString());
    	sessionManager.setSmsTokenSent(false);
        if (transferView.getResponseCode() != null && transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	status = messages.get("success");
        } else {
            status = messages.get("failed");
        }

        if (transferView.getTransferType() == Constants.TRANSFER_NOW) {
            typeTransfer = messages.get("transferImmediate");
        } else if (transferView.getTransferType() == Constants.TRANSFER_POSTDATE) {
            SimpleDateFormat sdate = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
            String nowDate = sdate.format(transferView.getValueDate());
            nowDate = nowDate + " WIB";
            typeTransfer = messages.get("transferPostDate") + " " + nowDate;
            if (!isFromHistory()) status = messages.get("pending");
        } else if (transferView.getTransferType() == Constants.TRANSFER_PERIODIC) {
            typeTransfer = messages.get("transferPeriod");
            if (!isFromHistory()) status = messages.get("pending");
        }
    }

    public Object onActivate() {
        if (transferView == null) {
            return TransferInput.class;
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
    Object onSuccess() {
        return TransactionHistoryResult.class;
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
			if (transferView.getResponseCode() != null && transferView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
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


