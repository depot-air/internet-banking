package com.dwidasa.ib.pages.transfer;

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
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/12/11
 * Time: 00:03 am
 */
public class RegisterTransferOtherConfirm {
    @Property(write = false)
    @Persist
    private TransferView transferView;

    @Property
    private TokenView tokenView;

    @Inject
    private TransferService transferService;

    @Inject
    private CacheManager cacheManager;

    @Property
    private int tokenType;

    @InjectPage
    private RegisterTransferOtherReceipt registerTransferOtherReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Inject
    private SessionManager sessionManager;
    
    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    void setupRender() {
        tokenView = new TokenView();
        setTokenType();
    }

    public Object onActivate() {
        if (transferView == null) {
            return RegisterTransferOtherInput.class;
        }

        return null;
    }

    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            	transferService.register(transferView.transform());
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

    @DiscardAfter
    public Object onSuccess() {
    	transferView.setResponseCode(Constants.SUCCESS_CODE);
        transferView.setReferenceNumber(ReferenceGenerator.generate());
        registerTransferOtherReceipt.setTransferView(transferView);
        return registerTransferOtherReceipt;
    }
}


