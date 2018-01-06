package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/4/11
 * Time: 00:51 am
 */
public class DeactivateDeviceInput {
    @Property
    private String token, challenge;
    @Property
    private Long userId;
    @Property
    private SelectModel applicationModel;
    @Inject
    private WebAdministrationService webAdministrationService;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private CacheManager cacheManager;
    @Property
    private int tokenType;
    @Property
    private String deviceId;
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;
    @InjectPage
    private DeactivateDeviceConfirm deactivateDeviceConfirm;

    void builtApplicationModel() {
        userId = sessionManager.getLoggedCustomerView().getId();
        List<CustomerDevice> customerDevices = webAdministrationService.getDevices(userId);
        applicationModel = genericSelectModelFactory.create(customerDevices);

    }

    void setupRender() {
        setTokenType();
        builtApplicationModel();
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    Object onSuccess() {

        deactivateDeviceConfirm.setDeviceId(deviceId);
        return DeactivateDeviceConfirm.class;
    }

}
