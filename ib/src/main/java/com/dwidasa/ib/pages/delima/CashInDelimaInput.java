package com.dwidasa.ib.pages.delima;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.engine.util.ListUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/25/11
 * Time: 00:47 am
  */
public class CashInDelimaInput {
    @Persist @Property private CashInDelimaView cashInDelimaView;
    @Property private int tokenType;
    @Property private String gender;
    @Property private List<String> genderListModel;
    @Property private List<String> cardTypeModel;
    @Property private List<String> countryModel;
    @Property private SelectModel bankModel;
    @Property private TokenView tokenView;
    
    @Inject private Messages messages;
    @Inject private CacheManager cacheManager;
    @Inject private SessionManager sessionManager;
    @Inject private ComponentResources componentResources;
    @Inject private DelimaService delimaService;
    @Inject private OtpManager otpManager;
    @Inject private GenericSelectModelFactory genericSelectModelFactory;

    @InjectPage private CashInDelimaConfirm cashInDelimaConfirm;
    @InjectPage private CashInDelimaReceipt cashInDelimaReceipt;

    @InjectComponent private Form form;
    
    public String getDateFieldFormat() {
        return Constants.SHORT_FORMAT;
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void setupRender() {
        builtGenderModel();
        builtCardTypeModel();
        buildBankModel();
        countryModel = ListUtils.getCountries();
        setTokenType();
        if (tokenView == null) {
            tokenView = new TokenView();
        }
        if (cashInDelimaView == null) {
            cashInDelimaView = new CashInDelimaView();
        }
        cashInDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE);
        if (cashInDelimaView.getSenderCountry() == null || cashInDelimaView.getSenderCountry().equals("")) {
        	cashInDelimaView.setSenderCountry(ListUtils.INDONESIA);
        	cashInDelimaView.setSenderNationality(ListUtils.INDONESIA);
        	cashInDelimaView.setReceiverCountry(ListUtils.INDONESIA);
        	cashInDelimaView.setReceiverNationality(ListUtils.INDONESIA);
        }
    }
    
    public void buildBankModel() {
        bankModel = genericSelectModelFactory.create(cacheManager.getBillers( com.dwidasa.engine.Constants.ATMB.TT_POSTING));
        
    }

    void onValidateFromForm() {
        try {

            if (cashInDelimaView.getAmount().compareTo(BigDecimal.valueOf(1D)) < 0 || cashInDelimaView.getAmount().compareTo(BigDecimal.valueOf(5000000D)) > 0) {
           		form.recordError(messages.get("amount-limit-message"));
           	}
            if(cashInDelimaView.getSenderDob() == null)
                form.recordError(messages.get("senderDob-regexp-message"));
            if(form.getHasErrors())
                return;
            
            setCashInDelimaViewData();
            cashInDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHIN_DELIMA_INQ_CODE);
            cashInDelimaView = (CashInDelimaView) delimaService.inquiry(cashInDelimaView);
            cashInDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE);
//            delimaService.confirm(cashInDelimaView);
            
        } catch (BusinessException e) {
            cashInDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE);
            form.recordError(e.getFullMessage());
        }
    }

    public void setCashInDelimaViewData() {
        cashInDelimaView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        cashInDelimaView.setAccountType(sessionManager.getAccountType(cashInDelimaView.getAccountNumber()));
        cashInDelimaView.setCurrencyCode(Constants.CURRENCY_CODE);
        cashInDelimaView.setMerchantType(sessionManager.getDefaultMerchantType());
        cashInDelimaView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        cashInDelimaView.setTransactionDate(new Date());
        cashInDelimaView.setSenderPhoneNumber("0" + cashInDelimaView.getSenderPhoneNumber());
        cashInDelimaView.setReceiverPhoneNumber("0" + cashInDelimaView.getReceiverPhoneNumber());
        cashInDelimaView.setProviderCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE);
        cashInDelimaView.setBillerCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE);	//diset karena mandatory
        cashInDelimaView.setProductCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE); //diset karena mandatory
    }

    public Object onSuccess() {
        cashInDelimaConfirm.setCashInDelimaView(cashInDelimaView);
        return cashInDelimaConfirm;
    }


    public void builtGenderModel() {
        genderListModel = new ArrayList<String>();
        genderListModel.add(messages.get("male"));
        genderListModel.add(messages.get("female"));

    }

    public void builtCardTypeModel() {
        cardTypeModel = new ArrayList<String>();
        cardTypeModel.add(messages.get("KTP"));
        cardTypeModel.add(messages.get("SIM"));
        cardTypeModel.add(messages.get("Passport"));
    }


    public void pageReset() {
        cashInDelimaView = null;
        cashInDelimaConfirm.setCashInDelimaView(null);
        cashInDelimaReceipt.setCashInDelimaView(null);
    }

}
