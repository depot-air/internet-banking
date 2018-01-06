package com.dwidasa.ib.pages.transfer;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/07/11
 * Time: 0:21
 */
public class TransferOtherConfirm {
    @Persist
    private TransferView transferView;

    @InjectPage
    private TransferOtherReceipt transferOtherReceipt;

    @Inject
    private TransferService transferService;

    @Inject
    private Messages messages;

    @Property
    private int tokenType;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private String providerName;

    @Property
    private TokenView tokenView;

    @Inject
    private OtpManager otpManager;

    @Inject
    private SessionManager sessionManager;
    
    @InjectComponent
    private Form form;

    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public void setupRender() {
        setTokenType();
        providerName = "TREASURY";
        
    }

    public Object onActivate() {
        if (transferView == null) {
            return TransferOtherInput.class;
        }

        return null;
    }

    public TransferView getTransferView() {
        return transferView;
    }

    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            	transferView = (TransferView) transferService.execute(transferView);
                transferService.confirm(transferView);
               
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        transferOtherReceipt.setTransferView(transferView);
        return transferOtherReceipt;
    }
}
