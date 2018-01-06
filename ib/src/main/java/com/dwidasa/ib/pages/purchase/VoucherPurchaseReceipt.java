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

import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.AccountResource;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/7/11
 * Time: 6:28 PM
 */
public class VoucherPurchaseReceipt {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private String status;

    @Persist
    private VoucherPurchaseView voucherPurchaseView;

    @Inject
    private Messages messages;
    
    @Inject
    private TransactionService transactionService;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    @Inject
    private SessionManager sessionManager;
    
    @Inject
	private ParameterDao parameterDao;
    
  
    public Long getDenomination() {
        return Long.valueOf(voucherPurchaseView.getDenomination());
    }

    public VoucherPurchaseView getVoucherPurchaseView() {
        return voucherPurchaseView;
    }

    public void setVoucherPurchaseView(VoucherPurchaseView voucherPurchaseView) {
        this.voucherPurchaseView = voucherPurchaseView;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(VoucherPurchaseReceipt.class.toString());
    	sessionManager.setSmsTokenSent(false);
        if (voucherPurchaseView.getResponseCode() != null && voucherPurchaseView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }

    public Object onActivate() {
        if (voucherPurchaseView == null) {
            return VoucherPurchaseInput.class;
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

    public boolean isMerchant() {    	
//    	String firstFour = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
//    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
//    	String[] tokens = ip.getParameterValue().split(",");
//    	boolean isMerch = false;
//    	if (tokens.length > 0 ) {
//    		for(int i = 0; i < tokens.length; i++) {
//    			if (firstFour.equals(tokens[i]))
//    				isMerch = true;
//    		}
//    	}
    	return sessionManager.isMerchant();
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
			if (voucherPurchaseView.getResponseCode() != null && voucherPurchaseView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
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
