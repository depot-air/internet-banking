package com.dwidasa.ib.pages.administration;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/4/11
 * Time: 00:48 am
 */


@Import(library = {
        "context:layout/javascript/deactivateMobileBanking.js"
})
public class DeactivateDeviceConfirm {
    @Persist
    private String deviceId;
    @Property
    private Long userId;
    @Inject
    private SessionManager sessionManager;
    @Property
    private String token, challenge;
    @Property
    private int tokenType;
    @Inject
    private CacheManager cacheManager;
    @Inject
    private OtpManager otpManager;
    @InjectPage
    private DeactivateDeviceReceipt deactivateDeviceReceipt;
    @InjectComponent
    private Form form;
    @Property
    private TokenView tokenView;
    @Inject
    private WebAdministrationService webAdministrationService;
    @Inject
    private Messages messages;

    private ResultView resultView;


    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    void onPrepare(){
        if (tokenView == null){
            tokenView = new TokenView();
        }
    }

    void setupRender() {
        userId = sessionManager.getLoggedCustomerView().getId();
        setTokenType();


    }
    public String getImeNumber() {
        return deviceId.substring(19);
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    void onValidateFromForm() {
        if (tokenView.getToken() == null) {
            form.recordError(messages.get("tokenError"));
        }
//        else if (tokenView.getToken().length() != 8) {
//            form.recordError(messages.get("lengthTokenError"));
//        }
        else if (!tokenView.getToken().matches("[0-9]+")) {
            form.recordError(messages.get("formatTokenError"));
        }
        if (!form.getHasErrors()) {
            if (otpManager.validateToken(sessionManager.getLoggedCustomerView().getId(), this.getClass().getSimpleName(), tokenView)) {
                try {
                    resultView = (ResultView) webAdministrationService.deactivateDevice(sessionManager.getLoggedCustomerView().getId(), deviceId.substring(0, 19).trim());
                    
                } catch (BusinessException e) {
                    form.recordError(e.getFullMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    Object onSuccess() {
        deactivateDeviceReceipt.setDeviceId(deviceId.substring(19));
        deactivateDeviceReceipt.setResultView(resultView);
        return DeactivateDeviceReceipt.class;
    }
}
