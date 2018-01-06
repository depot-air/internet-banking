package com.dwidasa.ib.pages.dompetku;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CellularPrefixDao;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.CashInDompetkuView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/08/11
 * Time: 22:43
 */
public class TopUpDompetkuInput {
    @Persist
    @Property
    private CashInDompetkuView cashInDompetkuView;

    @Property
    private int tokenType;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel billerModel;

    @Inject
    private PaymentService paymentService;

    @InjectPage
    private TopUpDompetkuConfirm topUpDompetkuConfirm;
    
    @Inject
    private CellularPrefixDao cellularPrefixDao;
//
//    @InjectPage
//    private TrainPaymentReceipt trainPaymentReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private OtpManager otpManager;
    
    @Inject
   	private Messages message;

    public void onPrepare() {
        
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(TopUpDompetkuConfirm.class.toString()) ) {
    		cashInDompetkuView = null;
    	}
    	sessionManager.setSessionLastPage(TopUpDompetkuInput.class.toString());
        if (cashInDompetkuView == null) {
        	cashInDompetkuView = new CashInDompetkuView();
        }
        //setTokenType();
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    private void setPaymentView() {
        cashInDompetkuView.setTransactionDate(new Date());
        cashInDompetkuView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        cashInDompetkuView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
//        cashInDompetkuView.setBillerCode(cacheManager.getBillers(Constants.DOMPETKU_CASH_OUT).
//                get(0).getBillerCode());
//        cashInDompetkuView.setBillerName(cacheManager.getBiller(Constants.DOMPETKU_CASH_OUT,
//        		cashInDompetkuView.getBillerCode()).getBillerName());
//        cashInDompetkuView.setProductCode(cacheManager.getBillerProducts(Constants.DOMPETKU_CASH_OUT,
//        		cashInDompetkuView.getBillerCode()).get(0).getProductCode());
//        cashInDompetkuView.setProductName(cacheManager.getBillerProducts(Constants.DOMPETKU_CASH_OUT,
//        		cashInDompetkuView.getBillerCode()).get(0).getProductName());
        cashInDompetkuView.setMerchantType(sessionManager.getDefaultMerchantType());
        cashInDompetkuView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        cashInDompetkuView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        cashInDompetkuView.setInputType("M");
        cashInDompetkuView.setToAccountType("00");
        cashInDompetkuView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
        cashInDompetkuView.setTransactionType(Constants.DOMPETKU_CASH_IN);
    }

    void onValidateFromForm() {
        try {
        	
        	if (cashInDompetkuView.getNominalTopUp().doubleValue() <= 0) {
                form.recordError(messages.get("inputAmount-cannotZero-message"));
            } 
        	else if (cashInDompetkuView.getNominalTopUp().doubleValue() < com.dwidasa.engine.Constants.TRANSFER_AMOUNT.MIN_PER_TRX.doubleValue()) {
                form.recordError(messages.get("inputAmount-minimum-message"));
            } 
            else if (cashInDompetkuView.getNominalTopUp().doubleValue() > com.dwidasa.engine.Constants.TRANSFER_AMOUNT.MAX_PER_TRX.doubleValue()) {
                form.recordError(messages.get("inputAmount-maximum-message"));
            }
        	
        	if (cashInDompetkuView.getMsiSDN() == null) {
                form.recordError(message.get("errorNewPhoneNull"));
            } else if (!cashInDompetkuView.getMsiSDN().matches("[0-9]+")) {
                form.recordError(message.get("formatNewPhoneError"));
            } else if (cashInDompetkuView.getMsiSDN().length() < 8) {
                form.recordError(message.get("lengthNewPhoneError"));
            } 
        	if(!validatePrefix())
                return;
        	
            if (!form.getHasErrors() ) {
                setPaymentView();
            }
        } catch (BusinessException e) {
            //trainPaymentView.setTransactionType(Constants.TRANSPORTATION_PAYMENT_CODE);
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
    	topUpDompetkuConfirm.setCashInDompetkuView(cashInDompetkuView);
        return topUpDompetkuConfirm;
    }

    void pageReset() {
//        trainPaymentView = null;
//        trainPaymentConfirm.setTrainPaymentView(null);
//        trainPaymentReceipt.setTrainPaymentView(null);
    }
    
    private boolean validatePrefix()
    {
        boolean valid = false;
//        List<BillerProduct> billerProducts =
//                cacheManager.getBillerProducts
//                        (com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE, "INDPR");
//        String billerProductId = "";
//        for(BillerProduct billerProduct : billerProducts)
//        {
//            if(billerProduct.getProductCode().equals(billerProduct.getProductCode()))
//            {
//                billerProductId = billerProduct.getId() + "";
//                break;
//            }
//        }
        List<CellularPrefix> cellularPrefixes = cellularPrefixDao.getAllCellularPrefixBiller((long)6);
        if(cellularPrefixes == null)
            form.recordError(message.get("phoneNumberPrefixError"));
        else
        {
            for(CellularPrefix cellularPrefix : cellularPrefixes)
            {
                if(cashInDompetkuView.getMsiSDN().indexOf(cellularPrefix.getPrefix())==0)
                {
                    valid = true;
                    break;
                }
            }
            if(!valid)
                form.recordError(message.get("phoneNumberPrefixError"));
        }
        return valid;
    }
    
    
}
