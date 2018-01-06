package com.dwidasa.ib.pages.transfer;
// UNTUK TRANSFER VIA TEASURY ya Om..

import java.math.BigDecimal;
import java.util.Date;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: HMS
 * Date: 30/07/2012
 * Time: 00:27 am
 */
public class RegisterTransferOtherInput {
    @InjectPage
    private RegisterTransferOtherConfirm registerTransferOtherConfirm;

    @InjectPage
    private RegisterTransferOtherReceipt registerTransferOtherReceipt;

    @Property
    private SelectModel billerModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @Property
    private int tokenType;

    @Property
    @Persist
    private TransferView transferView;

    @Inject
    private TransferService transferService;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Autowired
    private ExtendedProperty extendedProperty;
    
    public void buildBillerModel() {
        billerModel = genericSelectModelFactory.create(cacheManager.getBillers(
                com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE));
        		System.out.println("test debug other input 1");  
    }

    public void setupRender() {
        if (transferView == null) {
            transferView = new TransferView();
            System.out.println("test debug other input 2");
        }

        setTokenType();
        System.out.println("test debug other input 4");
        buildBillerModel();
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
        System.out.println("test debug other input 5");
    }

    void onValidateFromForm() {
        try {
            
            System.out.println("test debug other input 6");
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            System.out.println("Test debug other input 7");
        }
    }

    public Object onSuccess() {
        Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE,
                transferView.getBillerCode());
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setBillerCode(biller.getBillerCode());
        transferView.setBillerName(biller.getBillerName());
        transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
        transferView.setAccountNumber(sessionManager.getLoggedCustomerView().getAccountNumber());
        transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        transferView.setToAccountType("00");
        transferView.setMerchantType(sessionManager.getDefaultMerchantType());
        transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        transferView.setTransactionDate(new Date());
        transferView.setValueDate(new Date());
        transferView.setInputType("M");
        transferView.setAmount(BigDecimal.ONE);
        transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE);
        System.out.println("Test debug other input 8");
        registerTransferOtherConfirm.setTransferView(transferView);
        return registerTransferOtherConfirm;
    }

    public void pageReset(){
    	System.out.println("Test debug other input 9");
        transferView = null;
        registerTransferOtherConfirm.setTransferView(null);       
        registerTransferOtherReceipt.setTransferView(null);
    	System.out.println("Test debug other input 10");
    }
}


