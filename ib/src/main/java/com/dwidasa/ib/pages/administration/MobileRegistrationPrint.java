package com.dwidasa.ib.pages.administration;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.view.MobileRegistrationView;
import com.dwidasa.engine.service.TransactionDataService;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 30/10/12
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
public class MobileRegistrationPrint
{
    @InjectPage
    private MobileRegistrationReceipt mobileRegistrationReceipt;

    @Property
    private MobileRegistrationView mobileRegistrationView;

    @ActivationRequestParameter
    private String reprint;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Inject
    private TransactionDataService transactionDataService;


    void setupRender() {

        mobileRegistrationView = mobileRegistrationReceipt.getMobileRegistrationReceipt();

    }

}
