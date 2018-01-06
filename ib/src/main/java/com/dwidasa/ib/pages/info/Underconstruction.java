package com.dwidasa.ib.pages.info;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;

public class Underconstruction {

    @Inject
    private SessionManager sessionManager;


    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

	void setupRender(){
		
	}

}