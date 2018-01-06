package com.dwidasa.ib.pages.transfer;

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

public class TransferOtherInput {
    @Property
    private String transferTypeValue;

    @Inject
    private SessionManager sessionManager;
    
    void setupRender() {
        transferTypeValue = "treasury";
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    Object onSuccess() {
		if (transferTypeValue.equals("bankLain")) {
			return TransferAntarBankInput.class;
		} 
//		else if (transferTypeValue.equals("alto")) {
//			return TransferAltoOption.class;
//		} 
		else if (transferTypeValue.equals("treasury")) {
			return TransferTreasuryInput.class;
		}

        return null;
    }
}