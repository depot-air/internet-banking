package com.dwidasa.ib.pages.transfer;

import java.math.BigDecimal;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/07/11
 * Time: 8:35
 */
public class RegisterTransferInput {
    @Persist
    @Property
    private TransferView transferView;

    @Property
    private int tokenType;

    @InjectPage
    private RegisterTransferConfirm registerTransferConfirm;

    @InjectPage
    private RegisterTransferReceipt registerTransferReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private TransferService transferService;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    void onValidateFromForm() {
        try {
            
            transferView = (TransferView) transferService.inquiry(transferView);
            transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_CODE);
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

    public void setupRender() {
       
        setTokenType();
        setTransferViewData();

    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    private void setTransferViewData() {
        if (transferView == null) {
            transferView = new TransferView();
        }
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
        transferView.setAccountNumber(sessionManager.getLoggedCustomerView().getAccountNumber());
        transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        transferView.setBillerCode(cacheManager.getBillers(com.dwidasa.engine.Constants.TRANSFER_CODE)
                .get(0).getBillerCode());
        transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_INQ_CODE);
        transferView.setMerchantType(sessionManager.getDefaultMerchantType());
        transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        transferView.setToAccountType("00");
        transferView.setInputType("M");
        transferView.setAmount(BigDecimal.ONE);
    }

    public Object onSuccess() {
        registerTransferConfirm.setTransferView(transferView);
        return registerTransferConfirm;
    }

    public void pageReset(){
        transferView = null;
        registerTransferConfirm.setTransferView(null);
        registerTransferReceipt.setTransferView(null);
    }
}
