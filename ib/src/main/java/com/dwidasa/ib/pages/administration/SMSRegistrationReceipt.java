package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.model.view.SmsRegistrationView;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 30/10/12
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
public class SMSRegistrationReceipt
{
    @Property
    @Persist
    private SmsRegistrationView smsRegistrationView;

    public void setSMSRegistrationView(SmsRegistrationView smsRegistrationView){
        this.smsRegistrationView = smsRegistrationView;
    }
    
    public SmsRegistrationView getSmsRegistrationReceipt() {
        return smsRegistrationView;
    }
    
    @DiscardAfter
    Object onSuccess() {
        return TransactionHistoryResult.class;
    }

}
