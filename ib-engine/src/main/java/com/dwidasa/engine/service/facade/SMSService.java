package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.model.view.SmsRegistrationView;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 30/10/12
 * Time: 6:30
 * To change this template use File | Settings | File Templates.
 */
public interface SMSService extends BaseTransactionService
{
    public SmsRegistrationView executeSMSRegistration(SmsRegistrationView view);
}
