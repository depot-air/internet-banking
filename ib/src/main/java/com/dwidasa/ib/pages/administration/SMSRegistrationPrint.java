package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.model.view.SmsRegistrationView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.SMSService;
import com.dwidasa.engine.util.EngineUtils;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 30/10/12
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
public class SMSRegistrationPrint
{
    @InjectPage
    private SMSRegistrationReceipt smsRegistrationReceipt;

    @Property
    private SmsRegistrationView smsRegistrationView;

    @ActivationRequestParameter
    private String reprint;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private SMSService smsService;

    void setupRender() {
        if (reprint != null) {
        	smsRegistrationView = (SmsRegistrationView) EngineUtils.deserialize(
        			transactionDataService.getByTransactionFk(transactionId));
        	smsService.reprint(smsRegistrationView, transactionId);
        	return;
        }

        smsRegistrationView = smsRegistrationReceipt.getSmsRegistrationReceipt();

    }

}
