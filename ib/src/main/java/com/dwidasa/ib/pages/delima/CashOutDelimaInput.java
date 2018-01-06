package com.dwidasa.ib.pages.delima;


import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/25/11
 * Time: 00:51 am
 */
public class CashOutDelimaInput {

    @Property
    @Persist
    private CashOutDelimaView cashOutDelimaView;
    @Property
    private int tokenType;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private CacheManager cacheManager;
    @InjectPage
    private CashOutDelimaConfirm cashOutDelimaConfirm;
    @Inject
    private ComponentResources componentResources;

    @Property
    private TokenView tokenView;
    @Inject
    private DelimaService delimaService;
    @InjectComponent
    private Form form;
    @InjectPage
    private CashOutDelimaReceipt cashOutDelimaReceipt;


    void setupRender() {

        if (cashOutDelimaView == null) {
            cashOutDelimaView = new CashOutDelimaView();
        }
        if (tokenView == null) {
            cashOutDelimaView = new CashOutDelimaView();
        }
        setTokenType();
        cashOutDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CODE);
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void onValidateFromForm() {
        try {
            setCashOutDelimaData();
            cashOutDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHOUT_DELIMA_INQ_CODE);
            cashOutDelimaView = (CashOutDelimaView) delimaService.inquiry(cashOutDelimaView);
            cashOutDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CODE);
            delimaService.confirm(cashOutDelimaView);
        } catch (BusinessException e) {
            cashOutDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CODE);
            form.recordError(e.getFullMessage());
        }
    }

    public void setCashOutDelimaData() {
        cashOutDelimaView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        cashOutDelimaView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
        cashOutDelimaView.setAccountNumber(sessionManager.getLoggedCustomerView().getAccountNumber());
        cashOutDelimaView.setAccountType(sessionManager.getAccountType(cashOutDelimaView.getAccountNumber()));
        cashOutDelimaView.setCurrencyCode(Constants.CURRENCY_CODE);
        cashOutDelimaView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        cashOutDelimaView.setMerchantType(sessionManager.getDefaultMerchantType());
        cashOutDelimaView.setTransactionDate(new Date());
        cashOutDelimaView.setProviderCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE);
        cashOutDelimaView.setBillerCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE);	//diset karena mandatory
        cashOutDelimaView.setProductCode(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE); //diset karena mandatory
    }


    public Object onSuccess() {
        cashOutDelimaConfirm.setCashOutDelimaView(cashOutDelimaView);
        return cashOutDelimaConfirm;
    }


    public void pageReset() {
        cashOutDelimaView = null;
        cashOutDelimaConfirm.setCashOutDelimaView(null);
        cashOutDelimaReceipt.setCashOutDelimaView(null);
    }


}
