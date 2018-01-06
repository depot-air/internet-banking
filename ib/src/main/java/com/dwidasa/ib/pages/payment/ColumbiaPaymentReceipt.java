/**
 * 
 */
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
import com.dwidasa.engine.model.view.ColumbiaPaymentView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * @author dsi-bandung
 *
 */
public class ColumbiaPaymentReceipt {
	
	@Property
	private String status;

	@Inject
    private Messages messages;
	
	@Inject
    private TransactionService transactionService;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Property
    private String typeTransfer;

    @Property
    private String nowDate;

    @Persist
    private boolean fromHistory;
    
    @Persist
    private ColumbiaPaymentView columbiaPaymentView;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());

    @Inject
    private SessionManager sessionManager;

	public ColumbiaPaymentView getColumbiaPaymentView() {
		return columbiaPaymentView;
	}

	public void setColumbiaPaymentView(ColumbiaPaymentView columbiaPaymentView) {
		this.columbiaPaymentView = columbiaPaymentView;
	}
	
	public Object onActivate() {
        if (columbiaPaymentView == null) {
            return ColumbiaPaymentInput.class;
        }
        return null;
    }

    
    public void setupRender() {
    	sessionManager.setSessionLastPage(PlnPaymentReceipt.class.toString());
    	sessionManager.setSmsTokenSent(false);
    	
        if (columbiaPaymentView.getResponseCode() != null && columbiaPaymentView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }

    
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