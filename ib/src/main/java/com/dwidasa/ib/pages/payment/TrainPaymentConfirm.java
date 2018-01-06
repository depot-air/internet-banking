package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/08/11
 * Time: 22:43
 */
public class TrainPaymentConfirm {
    @Property(write = false)
    @Persist
    private TrainPaymentView trainPaymentView;

    @Property
    private TokenView tokenView;

    @InjectComponent
    private Form form;

    @Inject
    private OtpManager otpManager;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CacheManager cacheManager;

    @Property
    private int tokenType;

    @InjectPage
    private TrainPaymentReceipt trainPaymentReceipt;
    
    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    public Object onActivate(){
        if(trainPaymentView == null){
            return TrainPaymentInput.class;
        }
        return null;
    }

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(TrainPaymentConfirm.class.toString());
        setTokenType();
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setTrainPaymentView(TrainPaymentView trainPaymentView) {
        this.trainPaymentView = trainPaymentView;
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(trainPaymentView.getCustomerId(), this.getClass().getSimpleName(),
                    tokenView)) {
                paymentService.execute(trainPaymentView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        trainPaymentReceipt.setTrainPaymentView(trainPaymentView);
        return trainPaymentReceipt;
    }

}
