package com.dwidasa.ib.pages.payment;

import java.util.Date;

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
import com.dwidasa.engine.model.FormConfig;
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
public class TrainPaymentInput {
    @Persist
    @Property
    private TrainPaymentView trainPaymentView;

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
    private TrainPaymentConfirm trainPaymentConfirm;

    @InjectPage
    private TrainPaymentReceipt trainPaymentReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private OtpManager otpManager;

    public void onPrepare() {
        
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(TrainPaymentConfirm.class.toString()) ) {
    		trainPaymentView = null;
    	}
    	sessionManager.setSessionLastPage(TrainPaymentInput.class.toString());
        if (trainPaymentView == null) {
            trainPaymentView = new TrainPaymentView();
        }
        setTokenType();
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    private void setPaymentView() {
        trainPaymentView.setTransactionDate(new Date());
        trainPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        trainPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        trainPaymentView.setBillerCode(cacheManager.getBillers(Constants.TRANSPORTATION_PAYMENT_CODE).
                get(0).getBillerCode());
        trainPaymentView.setBillerName(cacheManager.getBiller(Constants.TRANSPORTATION_PAYMENT_CODE,
                trainPaymentView.getBillerCode()).getBillerName());
        trainPaymentView.setProductCode(cacheManager.getBillerProducts(Constants.TRANSPORTATION_PAYMENT_CODE,
                trainPaymentView.getBillerCode()).get(0).getProductCode());
        trainPaymentView.setProductName(cacheManager.getBillerProducts(Constants.TRANSPORTATION_PAYMENT_CODE,
                trainPaymentView.getBillerCode()).get(0).getProductName());
        trainPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        trainPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        trainPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        trainPaymentView.setInputType("M");
        trainPaymentView.setToAccountType("00");
        trainPaymentView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
    }

    void onValidateFromForm() {
        try {
            if (!form.getHasErrors() ) {
                setPaymentView();
                trainPaymentView.setTransactionType(Constants.TRANSPORTATION_PAYMENT_INQ_CODE);
                paymentService.inquiry(trainPaymentView);
                trainPaymentView.setTransactionType(Constants.TRANSPORTATION_PAYMENT_CODE);
                paymentService.confirm(trainPaymentView);
            }
        } catch (BusinessException e) {
            trainPaymentView.setTransactionType(Constants.TRANSPORTATION_PAYMENT_CODE);
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
        trainPaymentConfirm.setTrainPaymentView(trainPaymentView);
        return trainPaymentConfirm;
    }

    void pageReset() {
//        trainPaymentView = null;
//        trainPaymentConfirm.setTrainPaymentView(null);
//        trainPaymentReceipt.setTrainPaymentView(null);
    }
}
