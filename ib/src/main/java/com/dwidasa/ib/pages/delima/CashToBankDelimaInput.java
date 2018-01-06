package com.dwidasa.ib.pages.delima;

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
import com.dwidasa.engine.model.view.CashToBankDelimaView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.engine.util.ListUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/25/11
 * Time: 00:47 am
  */
public class CashToBankDelimaInput {
    @Property
    private int tokenType;

    @Persist
    @Property
    private CashToBankDelimaView cashToBankDelimaView;

    @Property
    private String gender;

    @Property
    private List<String> genderListModel;

    @Property
    private List<String> cardTypeModel;
    @Property
    private List<String> countryModel;

    @Property
    private SelectModel bankModel;
    
    @Inject
    private Messages messages;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private ComponentResources componentResources;

    @InjectPage
    private CashToBankDelimaConfirm cashToBankDelimaConfirm;

    @InjectPage
    private CashToBankDelimaReceipt cashToBankDelimaReceipt;

    @Inject
    private DelimaService delimaService;
    @Property
    private TokenView tokenView;
    @Inject
    private OtpManager otpManager;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @InjectComponent
    private Form form;
    
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
        if (cashToBankDelimaView == null) {
            cashToBankDelimaView = new CashToBankDelimaView();
        }
        cashToBankDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHTOBANK_POS_CODE);
        if (cashToBankDelimaView.getSenderCountry() == null || cashToBankDelimaView.getSenderCountry().equals("")) {
        	cashToBankDelimaView.setSenderCountry(ListUtils.INDONESIA);
        	cashToBankDelimaView.setSenderNationality(ListUtils.INDONESIA);
        }
    }
    
    public void buildBankModel() {
        bankModel = genericSelectModelFactory.create(cacheManager.getBillers( com.dwidasa.engine.Constants.ATMB.TT_POSTING));
        
    }

    void onValidateFromForm() {
        try {
            if(cashToBankDelimaView.getSenderDob() == null)
                form.recordError(messages.get("senderDob-regexp-message"));
//            if(cashToBankDelimaView.getReceiverDob() == null)
//                form.recordError(messages.get("receiverDob-regexp-message"));
            if(form.getHasErrors())
                return;
            if (otpManager.validateToken(cashToBankDelimaView.getCustomerId(),
            this.getClass().getSimpleName(), tokenView)) {
                setCashToBankDelimaViewData();
                cashToBankDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHTOBANK_INQ_CODE);
                cashToBankDelimaView = (CashToBankDelimaView) delimaService.inquiry(cashToBankDelimaView);
                cashToBankDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHTOBANK_POS_CODE);
                delimaService.confirm(cashToBankDelimaView);


            }
        } catch (BusinessException e) {
            cashToBankDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE);
            form.recordError(e.getFullMessage());
        }
    }

    public void setCashToBankDelimaViewData() {
        cashToBankDelimaView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        cashToBankDelimaView.setAccountType(sessionManager.getAccountType(cashToBankDelimaView.getAccountNumber()));
        cashToBankDelimaView.setCurrencyCode(Constants.CURRENCY_CODE);
        cashToBankDelimaView.setMerchantType(sessionManager.getDefaultMerchantType());
        cashToBankDelimaView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        cashToBankDelimaView.setTransactionDate(new Date());
        cashToBankDelimaView.setSenderPhoneNumber("0" + cashToBankDelimaView.getSenderPhoneNumber());
        cashToBankDelimaView.setReceiverPhoneNumber("0" + cashToBankDelimaView.getReceiverPhoneNumber());
        cashToBankDelimaView.setProviderCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE);
        cashToBankDelimaView.setBillerCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE);	//diset karena mandatory
        cashToBankDelimaView.setProductCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE); //diset karena mandatory
    }

    public Object onSuccess() {
        cashToBankDelimaConfirm.setCashToBankDelimaView(cashToBankDelimaView);
        return cashToBankDelimaConfirm;
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
        cashToBankDelimaView = null;
        cashToBankDelimaConfirm.setCashToBankDelimaView(null);
        cashToBankDelimaReceipt.setCashToBankDelimaView(null);
    }

}
