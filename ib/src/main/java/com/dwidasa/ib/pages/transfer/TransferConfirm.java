package com.dwidasa.ib.pages.transfer;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TransferScheduleService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/14/11
 * Time: 00:24 am
 */
public class TransferConfirm {
    @Property(write = false)
    @Persist
    private TransferView transferView;

    @Property
    private String nowDate;

    @Property
    private String typeTransfer;

    @Inject
    private TransferService transferService;

    @Inject
    private TransferScheduleService transferScheduleService;
    
    @Inject
    private Messages messages;

    @Property
    private int tokenType;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private OtpManager otpManager;

    @InjectComponent
    private Form form;

    @Property
    private TokenView tokenView;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private ExtendedProperty extendedProperty;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @InjectPage
    private TransferReceipt transferReceipt;

    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }

    public Object onActivate() {
        if (transferView == null) {
            return TransferInput.class;
        }

        return null;
    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    void setupRender() {
    	sessionManager.setSessionLastPage(TransferConfirm.class.toString());
        setTokenType();
        if (transferView.getTransferType() == Constants.TRANSFER_NOW) {
            typeTransfer = messages.get("transferImmediate");
        } else if (transferView.getTransferType() == Constants.TRANSFER_POSTDATE) {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());
            typeTransfer = messages.get("transferPostDate") + " " + sdf.format(transferView.getValueDate());
        } else if (transferView.getTransferType() == Constants.TRANSFER_PERIODIC) {
            typeTransfer = messages.get("transferPeriod");
        }
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            	transferView.setProviderCode(null);
            	transferView = (TransferView) transferService.execute(transferView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    Object onSuccess() {
        transferReceipt.setTransferView(transferView);
        return transferReceipt;
    }
}
