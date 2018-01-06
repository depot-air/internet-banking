package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/8/11
 * Time: 00:25 am
 */

@Import(library = {
        "context:layout/javascript/deactivateSoftToken.js"
})

public class DeactivateSoftTokenInput {
    @Persist
    private String deviceId;

    @Inject
    private SessionManager sessionManager;
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
    private WebAdministrationService webAdministrationService;
    @Inject
    private Messages messages;
    @InjectPage
    private DeactivateSoftTokenReceipt deactivateSoftTokenReceipt;

    private ResultView resultView;


    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    void setupRender() {
        setTokenType();
    }

    public String getUserId(){
       return sessionManager.getLoggedCustomerView().getUsername() ;
    }


    void onValidateFromForm() {
        if (tokenView.getToken() == null) {
            System.out.println("tokennnnnn =" + tokenView.getToken());
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
                    resultView = webAdministrationService.deactivateSoftToken(sessionManager.getLoggedCustomerView().getId());
                } catch (BusinessException e) {
                    form.recordError(e.getFullMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    @DiscardAfter
    Object onSuccess() {
        deactivateSoftTokenReceipt.setResultView(resultView);
        return DeactivateSoftTokenReceipt.class;
    }
}
