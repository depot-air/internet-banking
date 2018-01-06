package com.dwidasa.ib.pages.transfer;

import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/12/11
 * Time: 00:56 am
 */
public class RegisterTransferOtherReceipt {
    @Property(write = false)
    @Persist
    private TransferView transferView;

    @Property
    private String status;

    @Property
    private String nowDate;

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

    void setupRender() {
        sessionManager.setSessionLastPage(RegisterTransferOtherReceipt.class.toString());
        sessionManager.setSmsTokenSent(false);

        if (transferView.getResponseCode() != null && transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {           
        	status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        setTransactionViewDate();
    }

    public Object onActivate() {
        if (transferView == null) {
        	System.out.println("Test debug other Receipt 4");
            return RegisterTransferOtherInput.class;
        }

        return null;
        
    }

     private void setTransactionViewDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
        nowDate = sdf.format(new Date());
        System.out.println("Test debug other Receipt 5");
        transferView.setTransactionDate(new Date());
        transferView.setValueDate(new Date());
     }

    public void setTransferView(TransferView transferView) {
    	System.out.println("Test debug other Receipt 6");
    	System.out.println("test debug 11");
        this.transferView = transferView;
    }
}


