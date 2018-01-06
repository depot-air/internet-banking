package com.dwidasa.ib.pages.dompetku;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;
/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 13/07/11
 * Time: 15:42
 */

public class DompetkuOtherInput {
    @Property
    private String transferTypeValue;

    @Inject
    private SessionManager sessionManager;
    
    void setupRender() {
        transferTypeValue = "cashOut";
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    Object onSuccess() {
		if (transferTypeValue.equals("cashOut")) {
			return TarikTunaiDompetkuInput.class;
		} 
		else if (transferTypeValue.equals("topUp")) {
			return TopUpDompetkuInput.class;
		} 
//		else if (transferTypeValue.equals("transferToken")) {
//			return TransferTokenDompetkuInput.class;
//		}

        return null;
    }
}