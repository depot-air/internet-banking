package com.dwidasa.ib.pages.administration;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;

public class TransferListOption {
    @Property
    private String transferTypeValue;

    void setupRender() {
        transferTypeValue = Constants.TRANSFER_TYPE.OVERBOOKING;
    }
    
    @InjectPage
    private TransferDestinationList transferDestinationList;

    @Inject
    private SessionManager sessionManager;
    
    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    Object onSuccessFromForm() {
        if (transferTypeValue.equals(Constants.TRANSFER_TYPE.OVERBOOKING)) {
        	transferDestinationList.setTransferType(Constants.TRANSFER_TYPE.OVERBOOKING);
        } else if(transferTypeValue.equals(Constants.TRANSFER_TYPE.TREASURY)){
        	transferDestinationList.setTransferType(Constants.TRANSFER_TYPE.TREASURY);
        } else if(transferTypeValue.equals(Constants.TRANSFER_TYPE.ALTO)){
        	transferDestinationList.setTransferType(Constants.TRANSFER_TYPE.ALTO);
        }
        return transferDestinationList;
    }
}
