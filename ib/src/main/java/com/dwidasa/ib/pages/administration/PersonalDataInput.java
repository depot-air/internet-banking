package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/8/11
 * Time: 00:07 am
 */
public class PersonalDataInput {
    @Property
    private String emailUser;
    @Inject
    private WebAdministrationService webAdministrationService;
    @Inject
    private SessionManager sessionManager;


    void onValidateFromForm() {
       try{
        webAdministrationService.providePersonalInfo(sessionManager.getLoggedCustomerView().getId(),emailUser,null);
       }
       catch (BusinessException e){
          e.printStackTrace();
       }
    }

    Object onSuccess(){
        return Welcome.class   ;

    }
}
