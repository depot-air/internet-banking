package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/4/11
 * Time: 00:48 am
 */
public class RequestDeviceCode {

    @Component(id = "requestZone")
    private Zone requestZone;
    @Inject
    private WebAdministrationService webAdministrationService;
    @Inject
    private SessionManager sessionManager;
    private long id;
    @Property
    private String noReference;
    @Property
    private String[] requestLoop;
    @Property
    private String current;
    @Property
    private boolean requestButton;


    void setupReneder() {
        requestLoop = new String[0];
        requestButton = false;
    }


    Object onSuccess() {
        requestButton = true;
        id = sessionManager.getLoggedCustomerView().getId();
        noReference = webAdministrationService.requestDeviceActivationCode(id);
        requestLoop = new String[1];
        return requestZone.getBody();
    }
}
