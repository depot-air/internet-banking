package com.dwidasa.ib.pages.transfer;
// Untuk menu tambah daftar rekening Transfer

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/07/11
 * Time: 7:32
 */
public class RegisterTransferOption {
    @Property
    private String transferTypeValue;

    @Inject
    private SessionManager sessionManager;
    
    void setupRender() {
        transferTypeValue = "bprks";
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }//

    Object onSuccess() {
		if (transferTypeValue.equals("bprks")) {
			return RegisterTransferInput.class;
		} else if (transferTypeValue.equals("treasury")) {
			return RegisterTransferOtherInput.class;
		} else if (transferTypeValue.equals("bankLain")) {
			return RegisterTransferOtherInputAtmB.class;
		} 
//		else if (transferTypeValue.equals("alto")) {
//			return RegisterTransferOtherInputAlto.class;
//		}

        return null;
    }
}
