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
 * User: emil
 * Date: 09/07/11
 * Time: 9:23
 */
public class RegisterTransferConfirm {
    @Property
    private int tokenType;

    @InjectPage
    private RegisterTransferReceipt registerTransferReceipt;

    @InjectComponent
    private Form form;

    @Property(write = false)
    @Persist
    private TransferView transferView;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private TransferService transferService;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private Messages messages;

    @Property
    private TokenView tokenView;

    @Inject
    private OtpManager otpManager;

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    void onValidateFromForm() {
        try {
        	Boolean result = otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView);
        	System.out.println("resultnya : "+result);
            if (result == true ) {
//            	System.out.println("log1");
                transferService.register(transferView.transform());
            }
//            else{
////            	System.out.println("log2 ");
//            	form.recordError("Gagal");
//            }
        } catch (BusinessException e) {
//        	System.out.println("log3 ");
            form.recordError(e.getFullMessage());
        }
    }

    public Object onActivate() {
        if (transferView == null) {
            return RegisterTransferInput.class;
        }

        return null;
    }

    public void setupRender() {
        setTokenType();
        if (transferView == null) {
            setTransferView(new TransferView());
        }
    }

    public void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    @DiscardAfter
    public Object onSuccess() {
        transferView.setResponseCode(Constants.SUCCESS_CODE);
        transferView.setReferenceNumber(ReferenceGenerator.generate());
        registerTransferReceipt.setTransferView(transferView);
        return registerTransferReceipt;
    }

    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }
}
