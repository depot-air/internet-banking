package com.dwidasa.ib.pages.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/1/11
 * Time: 00:52 am
 */
public class DeactivateCardInput {
    @Persist
    private Map<String, List<String>> cardAccountMap;

    @Property
    private AccountView accountView;
    
    @InjectComponent
    private Form form;

    @Property
    private int tokenType;

    @Property
    private List<String> cardModel;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private CacheManager cacheManager;

    @InjectPage
    private DeactivateCardConfirm deactivateCardConfirm;

    @InjectPage
    private DeactivateCardReceipt deactivateCardReceipt;

    @Inject
    private AccountService accountService;

    @Inject
    private Messages message;
    
    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    void onPrepare() {
        
        if (accountView == null) {
            accountView = new AccountView();

            AccountInfo ai = sessionManager.getDefaultAccountInfo();
            accountView.setCardNumber(ai.getCardNumber());
            accountView.setAccountNumber(ai.getAccountNumber());
            accountView.setAccountType(ai.getAccountType());
            accountView.setCurrencyCode(ai.getCurrencyCode());
            accountView.setMerchantType(sessionManager.getDefaultMerchantType());
            accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
            accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        }
        System.out.println("cardAccountMap=" + cardAccountMap);
        if (cardAccountMap == null || cardAccountMap.size() == 0) {
            cardAccountMap = new HashMap<String, List<String>>();
            List<AccountView> avs = null;
            try{
            	avs = accountService.getCards(accountView);
            }catch(BusinessException e){
            	form.recordError(message.get("errorIgateResponse"));
//            	form.recordError(e.getFullMessage());
            }
            
            if(avs != null){
            	for (AccountView av : avs) {
                    if (av.getCardNumber() != null && av.getCardNumber().trim().length() != 0){
                        if (cardAccountMap.get(av.getCardNumber()) == null) {
                            List<String> accounts = new ArrayList<String>();
                            accounts.add(av.getAccountNumber());
                            cardAccountMap.put(av.getCardNumber(), accounts);
                        }
                        else {
                            cardAccountMap.get(av.getCardNumber()).add(av.getAccountNumber());
                        }
                    }

                }	
            }
            
        }

        cardModel = CollectionFactory.newList(cardAccountMap.keySet());
        setTokenType();
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    Object onSuccess() {
        List<String> accs = cardAccountMap.get(accountView.getCardNumber());
        String generated = "";
        for (String acc : accs) {
            generated += String.format("%1$-16s", accountView.getCardNumber());
            generated += String.format("%1$-10s", acc);
        }
        accountView.setGenerated(generated);
        deactivateCardConfirm.setAccountView(accountView);
        return deactivateCardConfirm;
    }

    void pageReset() {
        cardAccountMap = null;
        deactivateCardConfirm.setAccountView(null);
        deactivateCardReceipt.setAccountView(null);
    }
}
