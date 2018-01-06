package com.dwidasa.ib.pages.transfer;
//untuk receipt Transfer via ATMB ya Om..

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
 * User: HMS
 * Date: 7/12/11
 * Time: 00:56 am
 */
public class RegisterTransferOtherReceiptAtmB {
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

        sessionManager.setSessionLastPage(RegisterTransferOtherReceiptAtmB.class.toString());
    	sessionManager.setSmsTokenSent(false);

        if (transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }

        setTransactionViewDate();
    }

    public Object onActivate() {
        if (transferView == null) {
            return RegisterTransferOtherInput.class;
        }

        return null;
    }

     private void setTransactionViewDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
        nowDate = sdf.format(new Date());

        transferView.setTransactionDate(new Date());
        transferView.setValueDate(new Date());
     }

    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }
}


