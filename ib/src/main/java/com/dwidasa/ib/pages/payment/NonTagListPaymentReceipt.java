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
import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/08/11
 * Time: 22:44
 */
public class NonTagListPaymentReceipt {
    @Persist
    private NonTagListPaymentView nonTagListPaymentView;

    @Property
    private String status;
    
    @Inject
    private TransactionService transactionService;

 
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Property
    private String vGspRef;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.DATEFORMAT.DDMMMYY,
            threadLocale.getLocale());

    @Inject
    private Messages messages;

    public void setupRender() {
    	sessionManager.setSessionLastPage(NonTagListPaymentReceipt.class.toString());
    	sessionManager.setSmsTokenSent(false);
        if (nonTagListPaymentView.getResponseCode() != null && nonTagListPaymentView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        //System.out.println("HOHOHOHOHO : "+nonTagListPaymentView.getProviderReference());
        vGspRef = nonTagListPaymentView.getProviderReference();
    }

    public Object onActivate(){
        if(nonTagListPaymentView == null){
            return TrainPaymentInput.class;
        }
        return null;
    }

    public void setNonTagListPaymentView(NonTagListPaymentView nonTagListPaymentView) {
        this.nonTagListPaymentView = nonTagListPaymentView;
    }

    public NonTagListPaymentView getNonTagListPaymentView() {
        return nonTagListPaymentView;
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

    @Property
    private String _footer;

    public String[] getFooters()
    {
        String[] words = nonTagListPaymentView.getInformasiStruk().split("\n");        
        return words;
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
