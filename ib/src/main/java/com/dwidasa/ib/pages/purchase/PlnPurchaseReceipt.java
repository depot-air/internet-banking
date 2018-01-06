package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/08/11
 * Time: 11:14
 */
public class PlnPurchaseReceipt {
    @Persist
    private PlnPurchaseView plnPurchaseView;

    @Property
    private String status;

    @Property
    private String vGspRef;

    @Property
    private String vTokenNumber;

    @Inject
    private Messages messages;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    @Inject
    private KioskReprintService kioskReprintService;

    @Inject
    private TransactionService transactionService;

    @Inject
    private SessionManager sessionManager;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    public void setupRender(){
    	sessionManager.setSessionLastPage(PlnPurchaseReceipt.class.toString());    
    	sessionManager.setSmsTokenSent(false);
    	if (plnPurchaseView.getResponseCode() != null && plnPurchaseView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
        	if (isFromHistory()) {
        		Transaction transaction = transactionService.get(transactionId);
            	if (transaction.getStatus().equals(com.dwidasa.engine.Constants.SUCCEED_STATUS)) {
            		status = messages.get("success");
            	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.PENDING_STATUS)) {
            		status = messages.get("pending");
            	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.FAILED_STATUS)) {
            		status = messages.get("pending");
            	}
        	} else {
        		status = messages.get("failed");
        	}
        }
    	
    	
    	if (isGsp()) {
    		vGspRef = plnPurchaseView.getBit48().substring(86, 86+32);
    	} else {
            vGspRef = plnPurchaseView.getBit62().substring(86, 86+32);    		
    	}

        String token = plnPurchaseView.getTokenNumber();
        if (token != null && token.length() > 4) {
        	vTokenNumber = token.substring(0, 4) + "  " + token.substring(4, 8) + "  " +
                    token.substring(8, 12) + "  " + token.substring(12, 16);
            if(token.length() >= 20)
                vTokenNumber = vTokenNumber + "  " + token.substring(16, 20);
        }        
    }

    public Object onActivate() {
        if (plnPurchaseView == null) {
            return PlnPurchaseInput.class;
        }

        return null;
    }

    public PlnPurchaseView getPlnPurchaseView() {
        return plnPurchaseView;
    }

    public void setPlnPurchaseView(PlnPurchaseView plnPurchaseView) {
        this.plnPurchaseView = plnPurchaseView;
    }
    
    @Persist
    private boolean fromHistory;
    
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }
    
 
    public boolean isSuccess() {
    	return status.equals(messages.get("success"));
    }
    public boolean isNotSuccess() {
    	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    	boolean sameDay =  fmt.format(plnPurchaseView.getTransactionDate()).equals(fmt.format(new Date()));
    	return !status.equals(messages.get("success")) && sameDay ;
    }
    @DiscardAfter
    Object onSuccess() {
        return TransactionHistoryResult.class;
    	//reprint
    	//plnPurchaseView.setTransactionType(com.dwidasa.engine.Constants.PLN_PURCHASE_REP_CODE);
        //plnPurchaseView = (PlnPurchaseView) kioskReprintService.reprint(plnPurchaseView);
    }

    public boolean isGsp() {
    	return plnPurchaseView.getProviderCode().equals(Constants.GSP.PROVIDER_CODE);
    }

    @Property
    private String _footer;

    public String[] getFooters()
    {
    	String[] words = null;
    	if (plnPurchaseView.getInformasiStruk() != null) {
        	words = plnPurchaseView.getInformasiStruk().split("\n");
    	}
        return words;
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
			if (plnPurchaseView.getResponseCode() != null && plnPurchaseView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
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
    
    public String getcurrencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n).replace("$", "");
    }
}
