package com.dwidasa.ib.pages.transfer;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;


/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/12/11
 * Time: 00:56 am
 */
public class TransferAtmbReceipt {
    @Property(write = false)
    @Persist
    private TransferView transferView;

    @Property
    private String status;
    
    @Inject
    private TransactionService transactionService;

 
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Property
    private String nowDate;
    
    @Inject
    private ComponentResources componentResources;

    @Inject
    private TransferService transferService;

    @Inject
    private Messages messages;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    @Property
    private String note1;

    @Property
    private String note2;

    void setupRender()
    {
    	sessionManager.setSessionLastPage(TransferReceipt.class.toString());
    	sessionManager.setSmsTokenSent(false);
        if(transferView.getTransactionStatus().toUpperCase().equals(com.dwidasa.engine.Constants.SUCCEED_STATUS))
        {
            note1 = messages.get("transferTransSuccess1");
            note2 = messages.get("transferTransSuccess2");
        }
        else
        if(transferView.getTransactionStatus().toUpperCase().equals(com.dwidasa.engine.Constants.PENDING_STATUS))
        {
            note1 = messages.get("transferTransPending1");
            note2 = messages.get("transferTransPending2");
        }
        //setTransactionViewDate();
    }

     private void setTransactionViewDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
        nowDate = sdf.format(new Date());
        transferView.setTransactionDate(new Date());
        transferView.setValueDate(new Date());
     }
     
     public TransferView getTransferReceiptView() {
         return transferView;
     }

     public void setTransferReceiptView(TransferView transferView) {
         this.transferView = transferView;
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

     public boolean isAlto() {    	
     	return transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ALTO.TT_POSTING);
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


