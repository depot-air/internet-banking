package com.dwidasa.ib.pages.transfer;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/07/11
 * Time: 15:37
 */
public class TransferOtherReceipt {
	@Property
    private String status;
    
    @Inject
    private TransactionService transactionService;

    @Inject
    private Messages messages;

    @Persist
    private TransferView transferView;

    @Property
    private String nowDate;
    
    @Property
    private String providerName;
    
    @Inject
    private CacheManager cacheManager;

    @Inject
    private TransferService transferService;

    @Inject
    private Locale locale;

    @Inject
    private SessionManager sessionManager;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, locale);

    public Object onActivate() {
        if (transferView == null) {
            return TransferOtherInput.class;
        }

        return null;
    }

    public void setupRender() {
//    	String productCode = cacheManager.getBillerProducts(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE,
//                transferView.getBillerCode()).get(0).getProductCode();
//        providerName = cacheManager.getProviderProduct(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE,
//                transferView.getBillerCode(),
//                productCode, transferView.getProviderCode()).getProvider().getProviderName();
    	sessionManager.setSmsTokenSent(false);
        if (transferView.getPending() != null && transferView.getPending()) {
            status = messages.get("pending");
        }
        else if (transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        }
        else {
            status = messages.get("failed");
        }
        //transferView.setCustomerReference(transferView.getCustRefAtmb());
    }

    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }
    
    public TransferView getTransferView() {
    	return transferView;
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
