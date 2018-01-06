package com.dwidasa.ib.pages.administration;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.dwidasa.engine.model.view.MobileRegistrationView;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;

/**
 * Created with IntelliJ IDEA. User: fatakhurah-NB Date: 30/10/12 Time: 11:19 To
 * change this template use File | Settings | File Templates.
 */
public class MobileRegistrationReceipt {
	@Property(write = false)
    @Persist
    private MobileRegistrationView mobileRegistrationView;

	public MobileRegistrationView getMobileRegistrationReceipt() {
		return mobileRegistrationView;
	}

    public void setMobileRegistrationView(MobileRegistrationView mobileRegistrationView) {
        this.mobileRegistrationView = mobileRegistrationView;
	}

	@DiscardAfter
	Object onSuccess() {
		return TransactionHistoryResult.class;
	}

}
