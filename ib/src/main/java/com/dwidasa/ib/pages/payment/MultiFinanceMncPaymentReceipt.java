package com.dwidasa.ib.pages.payment;

import java.math.BigDecimal;
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
import com.dwidasa.engine.model.view.MultiFinancePaymentView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/08/11
 * Time: 22:44
 */
public class MultiFinanceMncPaymentReceipt {
	@Persist
    private MultiFinancePaymentView multiFinancePaymentView;

    @Property
    private String status;
    
    @Inject
    private TransactionService transactionService;

  
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    @Inject
    private Messages messages;

    public void setupRender() {
    	sessionManager.setSessionLastPage(MultiFinanceMncPaymentReceipt.class.toString());
    	sessionManager.setSmsTokenSent(false);
        if (multiFinancePaymentView.getResponseCode() != null && multiFinancePaymentView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }

//    public Object onActivate(){
//        if(multiFinancePaymentView == null){
//            return MultiFinancePaymentInput.class;
//        }
//        return null;
//    }

   
    public MultiFinancePaymentView getMultiFinancePaymentView() {
		return multiFinancePaymentView;
	}
    
    public void setMultiFinancePaymentView(
			MultiFinancePaymentView multiFinancePaymentView) {
		this.multiFinancePaymentView = multiFinancePaymentView;
	}
    

    @Persist
    private boolean fromHistory;
    
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = fromHistory;
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
			if (multiFinancePaymentView.getResponseCode() != null && multiFinancePaymentView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
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
    
    public String getMasked(String str) {
     	return EngineUtils.getCardNumberMasked(str);
     }
    
    public BigDecimal getTotal(){
    	
    	BigDecimal total = multiFinancePaymentView.getAmount().add(multiFinancePaymentView.getFee());
    	return total;
    	
    }

}


