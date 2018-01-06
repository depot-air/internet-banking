package com.dwidasa.ib.pages.purchase;

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

public class TiketOtherInput {
    @Property
    private String transferTypeValue;

    @Inject
    private SessionManager sessionManager;
    
    void setupRender() {
        transferTypeValue = "TiketKramatDjati";
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    Object onSuccess() {
		if (transferTypeValue.equals("TiketKramatDjati")) {
			return TiketKeretaDjatiPurchaseInput.class;
		} 
		else if (transferTypeValue.equals("tiketux")) {
			return TiketuxInput.class;
		} 

        return null;
    }
}