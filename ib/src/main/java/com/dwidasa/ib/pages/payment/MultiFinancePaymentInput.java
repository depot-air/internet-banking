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
import com.dwidasa.engine.model.view.MultiFinancePaymentView;
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
public class MultiFinancePaymentInput {
    @Persist
    @Property
    private MultiFinancePaymentView multiFinancePaymentView;

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
    private MultiFinancePaymentConfirm multiFinancePaymentConfirm;

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
    	if (!sessionManager.getSessionLastPage().equals(MultiFinancePaymentInput.class.toString()) ) {
    		multiFinancePaymentView = null;
    	}
    	sessionManager.setSessionLastPage(MultiFinancePaymentInput.class.toString());
        if (multiFinancePaymentView == null) {
        	multiFinancePaymentView = new MultiFinancePaymentView();
        }
        //setTokenType();
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    private void setPaymentView() {
    	multiFinancePaymentView.setTransactionDate(new Date());
    	multiFinancePaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	multiFinancePaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
    	multiFinancePaymentView.setBillerCode("");
    	multiFinancePaymentView.setBillerName("");
    	multiFinancePaymentView.setProductCode("");
    	multiFinancePaymentView.setProductName("");
    	multiFinancePaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
    	multiFinancePaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
    	multiFinancePaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
    	multiFinancePaymentView.setInputType("M");
    	multiFinancePaymentView.setToAccountType("00");
    	multiFinancePaymentView.setProviderCode("");
    }

    void onValidateFromForm() {
        try {
            if (!form.getHasErrors() ) {
                setPaymentView();
                multiFinancePaymentView.setTransactionType(Constants.MULTIFINANCE_NEW.INQ_MULTI_FINANCE);
                paymentService.inquiry(multiFinancePaymentView);
            }
        } catch (BusinessException e) {
        	multiFinancePaymentView.setTransactionType(Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE);
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
    	multiFinancePaymentConfirm.setTrainPaymentView(multiFinancePaymentView);
    	multiFinancePaymentConfirm.setFinancePaymentViews(multiFinancePaymentView.getFinancePaymentViews());
    	multiFinancePaymentConfirm.setNoKartu(multiFinancePaymentView.getCardNumber());
    	multiFinancePaymentConfirm.setNoRekening(multiFinancePaymentView.getAccountNumber());
    	multiFinancePaymentConfirm.setNomorKontrak(multiFinancePaymentView.getNumKontrak());
    	multiFinancePaymentConfirm.setNamaPelanggan(multiFinancePaymentView.getNamaPelanggan());
    	multiFinancePaymentConfirm.setDeskripsi(multiFinancePaymentView.getDeskripsi());
    	multiFinancePaymentConfirm.setJmlTagihan(multiFinancePaymentView.getJumlahTagihan());
    	multiFinancePaymentConfirm.setBit48(multiFinancePaymentView.getBit48());
        return multiFinancePaymentConfirm;
    }

    void pageReset() {
//        trainPaymentView = null;
//        trainPaymentConfirm.setTrainPaymentView(null);
//        trainPaymentReceipt.setTrainPaymentView(null);
    }
}
