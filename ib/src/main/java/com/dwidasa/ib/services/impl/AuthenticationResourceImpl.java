package com.dwidasa.ib.services.impl;

import java.util.List;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.service.CustomerService;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.AppVersion;
import com.dwidasa.engine.model.Version;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.AppVersionService;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.NativeAuthenticationService;
import com.dwidasa.engine.service.facade.SynchronizationService;
import com.dwidasa.engine.service.facade.WebAuthenticationService;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.AuthenticationResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 10:19 AM
 */
@PublicPage
public class AuthenticationResourceImpl implements AuthenticationResource {
    @Inject
    private NativeAuthenticationService authenticator;

    @Inject
    private SynchronizationService synchronizationService;

    @Inject
    private AppVersionService appVersionService;

    @Inject
    private CustomerService customerService;

    public AuthenticationResourceImpl() {
    }

    public String authenticateCustomer(Long customerId, String deviceId, String token) {
        CustomerView customerView = authenticator.authenticate(customerId, deviceId, token);
        return PojoJsonMapper.toJson(customerView);
    }

    public String authenticateCustomerPost(Long customerId, String deviceId, String token) {
        CustomerView customerView = authenticator.authenticate(customerId, deviceId, token);
        return PojoJsonMapper.toJson(customerView);
    }

    public String requestChallenge(Long customerId, String deviceId) {
        return authenticator.requestChallenge(customerId, deviceId);
    }

    public String requestChallengePost(Long customerId, String deviceId) {
        return authenticator.requestChallenge(customerId, deviceId);
    }

    public String authenticateCustomer(String username, String tin) {
        WebAuthenticationService was = (WebAuthenticationService) ServiceLocator.getService("webAuthenticationService");
        CustomerView cv = was.authenticate(username, tin);

        return PojoJsonMapper.toJson(cv);
    }

    public String authenticateCustomerPost(String username, String tin) {
        WebAuthenticationService was = (WebAuthenticationService) ServiceLocator.getService("webAuthenticationService");
        CustomerView cv = was.authenticate(username, tin);

        return PojoJsonMapper.toJson(cv);
    }

    public String authenticateCustomer2(Long customerId, String deviceId, String token, String challenge, String deviceType, Long versionId) {
        switch (customerService.checkCustomerStatus(customerId)){
            case -1 : throw new BusinessException("IB-1002");
            case 0  : throw new BusinessException("IB-1019");
        }

        authenticator.requestChallenge(customerId, deviceId);
        CustomerView customerView = authenticator.authenticate2(customerId, deviceId, token, challenge);
        List<Version> versions = synchronizationService.getVersions();
        customerView.setDataVersion(versions);
        AppVersion appVersion =  appVersionService.getLatestVersion(deviceType, versionId);
        customerView.setAppVersion(appVersion);

        return PojoJsonMapper.toJson(customerView);
    }
}
