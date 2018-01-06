package com.dwidasa.engine.service;

import com.dwidasa.engine.service.facade.RegistrationService;
import org.apache.log4j.Logger;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.IsoBitmap;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.util.iso.IsoDigester;
import com.dwidasa.interlink.ServiceCaller;
import com.dwidasa.interlink.ServiceHook;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/27/11
 * Time: 11:33 AM
 */
public class MessageReceiverService implements ServiceHook {
    private static Logger logger = Logger.getLogger( MessageReceiverService.class );
    private final AccountService accountService;
    private final IsoBitmapService isoBitmapService;
    private final RegistrationService registrationService;

    private MessageReceiverService() {
    	ServiceCaller caller = ( ServiceCaller ) ServiceLocator.getService( "serviceCaller" );
        caller.registerHook(this);
        accountService = (AccountService) ServiceLocator.getService("accountService");
        isoBitmapService = (IsoBitmapService) ServiceLocator.getService("isoBitmapService");
        registrationService = (RegistrationService) ServiceLocator.getService("registrationService");
    }

    public void onMessage(String argMsg) {
        Transaction t = new Transaction();
        try {
            IsoDigester.getInstance().digestResponse(t, argMsg);   
            if (!(t.getTransactionType().equals(Constants.REGISTRATION_CODE))) {
                return;
            }

            logger.info("START registerEBanking()");
            registrationService.registerEBanking(t);
            logger.info("END registerEBanking()");

            /*
        	IsoBitmap isoBitmap = isoBitmapService.getByTransactionType(com.dwidasa.engine.Constants.REGISTRATION_CODE);
            if (isoBitmap != null && !isoBitmap.getBitmap().contains("56")) {
            	accountService.registerEBanking(t);
            } else {
            	accountService.registerEBankingIGate(t);
            }
            */
            t.setResponseCode(Constants.SUCCESS_CODE);
        } catch (Exception e) {
        	logger.error("error prosess di hook", e);
            t.setResponseCode("11");
        }

        t.setFromAccountType("00");
        t.setToAccountType("00");
        t.setTranslationCode("01601");
        CommLink link = new MxCommLink(t, argMsg);
        link.replyMessage();
    }
}
