package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/26/11
 * Time: 00:30 am
 */

public class StatusDelimaInput {

    @Property
    @Persist
    private CashInDelimaView cashInDelimaView;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private CacheManager cacheManager;
    @Property
    private int tokenType;
    @Property
    private TokenView tokenView;
    @InjectPage
    private StatusDelimaReceipt statusDelimaReceipt;
    @InjectPage
    private StatusCashInDelimaReceipt statusCashInDelimaReceipt;
    @Inject
    private OtpManager otpManager;
    @Inject
    private DelimaService delimaService;
    @InjectComponent
    private Form form;
    @Property
    private List<String> statusType;
    @Inject
    private Messages messages;
    @Property
    private String delimaTransaction;


    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void setStatusType() {
        statusType = new ArrayList<String>();
        statusType.add(messages.get("cashIn"));
        statusType.add(messages.get("cashOut"));
        statusType.add(messages.get("refund"));
    }

    void setupRender() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
        if (cashInDelimaView == null) {
            cashInDelimaView = new CashInDelimaView();
        }
        setTokenType();
        setStatusType();
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(cashInDelimaView.getCustomerId(),
                    this.getClass().getSimpleName(), tokenView)) {
                setStatusData();
                if (delimaTransaction.equals("Cash In")) {
                    cashInDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHIN_DELIMA_CHK_CODE);
                } else if (delimaTransaction.equals("Cash Out")) {
                    cashInDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CHK_CODE);
                } else {
                    cashInDelimaView.setTransactionType(com.dwidasa.engine.Constants.REFUND_DELIMA_CHK_CODE);
                }
                delimaService.checkStatus(cashInDelimaView);
            }

        } catch (BusinessException e) {

            form.recordError(e.getFullMessage());
        }
    }

    public void setStatusData() {
        cashInDelimaView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        cashInDelimaView.setAccountType(sessionManager.getAccountType(cashInDelimaView.getAccountNumber()));
        cashInDelimaView.setCurrencyCode(Constants.CURRENCY_CODE);
        cashInDelimaView.setMerchantType(sessionManager.getDefaultMerchantType());
        cashInDelimaView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
    }

    public Object onSuccess() {

        if(delimaTransaction.equals("Cash In")) {
            statusCashInDelimaReceipt.setCashInDelimaView(cashInDelimaView);
            return StatusCashInDelimaReceipt.class;
        }

        else{
            statusDelimaReceipt.setCashInDelimaView(cashInDelimaView);
            return statusDelimaReceipt;
        }

    }

    public void pageReset() {
        cashInDelimaView = null;
        statusDelimaReceipt.setCashInDelimaView(null);
    }

}
