package com.dwidasa.engine.service.impl.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.Product;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.SmsRegistrationView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.KioskAdministrationService;
import com.dwidasa.engine.service.facade.SMSService;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 30/10/12
 * Time: 6:31
 * To change this template use File | Settings | File Templates.
 */
@Service("smsService")
public class SMSServiceImpl extends BaseTransactionServiceImpl implements SMSService
{
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerAccountDao customerAccountDao;

    @Autowired
    private KioskAdministrationService kioskAdministrationService;


    public SmsRegistrationView executeSMSRegistration(SmsRegistrationView srv)
    {
        if (srv.getCardData2() != null) srv.setCardData2(EngineUtils.getEncryptedPin(srv.getCardData2(), srv.getCardNumber()));
        srv.setTransactionType(com.dwidasa.engine.Constants.SMS_REGISTRATION.TRANS_TYPE);
        srv.setEncryptedTinUssd(EngineUtils.getEncryptedPin(srv.getTinUssd(), srv.getCardNumber()));
        srv.setTransactionDate(new Date());
        Customer cust = customerDao.get(srv.getCustomerId());
        srv.setCifNumber(cust.getCifNumber());
        srv.setReferenceName(cust.getCustomerName());
        List<AccountView> accountViews = accountService.getAccounts(srv.getCustomerId());
        for (int i = 0; i < accountViews.size(); i++) {
            AccountView accountView = accountViews.get(i);
            if (accountView.getProductName() != null && !accountView.getProductName().equals("")) {
                CustomerAccount customerAccount = customerAccountDao.getWithTypeAndProduct
                        (srv.getCustomerId(), accountView.getAccountNumber());
                Product product = customerAccount.getProduct();
                accountView.setProductCode(product.getProductCode());
                accountView.setProductName(product.getProductName());
            }
        }
        srv.setAccountViews(accountViews);
        srv = (SmsRegistrationView) kioskAdministrationService.posting(srv);
        return srv;
    }
}
