package com.dwidasa.ib.pages.transfer;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.TokenView;
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
 * Date: 7/12/11
 * Time: 00:27 am
 */
public class RegisterTransferOtherInputAlto {
	private static Logger logger = Logger.getLogger( RegisterTransferOtherInputAlto.class );
    @InjectPage
    private RegisterTransferOtherConfirmAtmB registerTransferOtherConfirmAtmB;

    @InjectPage
    private RegisterTransferOtherReceiptAtmB registerTransferOtherReceiptAtmB;

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

    @Persist
    @Property
    private TokenView tokenView;

    @Inject
    private OtpManager otpManager;

    public void buildBillerModel() {
        billerModel = genericSelectModelFactory.create(cacheManager.getBillers(
                com.dwidasa.engine.Constants.ALTO.TT_POSTING)); 
    }

    public void setupRender() {
        if (transferView == null) {
            transferView = new TransferView();
        }
  
        if (tokenView == null) {
            tokenView = new TokenView();
        }
              
       // setTokenType();
        buildBillerModel();
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void onValidateFromForm() {
        try {
        	Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.ALTO.TT_POSTING, transferView.getBillerCode());
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
            transferView.setTransactionType(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY);
            transferView.setProviderCode(com.dwidasa.engine.Constants.ALTO.PROVIDER_CODE);
            transferView.setSenderName(sessionManager.getLoggedCustomerView().getName());
            transferView.setAmount(new BigDecimal("15000"));	//inquiry butuh amount, tdk kedebet
            transferView.setFee(BigDecimal.ZERO);
            transferView.setFeeIndicator("C");
	    	//transferView.setCustomerReference(customerReference1);
        	
        	transferView = transferService.inquiryALTO(transferView);        	
//        	logger.info("transferView=" + transferView.getCustomerReference());
        }catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            System.out.println("ini test debug Other Input Validasi Form");      
            }
    }

    public Object onSuccess() {
    	transferView.setTransactionType(com.dwidasa.engine.Constants.ALTO.TT_POSTING);
        registerTransferOtherConfirmAtmB.setTransferView(transferView);
        return registerTransferOtherConfirmAtmB;
    }

    public void pageReset(){
        transferView = null;
        registerTransferOtherConfirmAtmB.setTransferView(null);
        registerTransferOtherReceiptAtmB.setTransferView(null);
    }
}


