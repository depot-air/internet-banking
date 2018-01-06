package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.engine.service.view.CashOutDelimaViewService;
import com.dwidasa.ib.services.OtpManager;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/25/11
 * Time: 00:03 am
 */
public class CashOutDelimaConfirmSms {
    private static Logger logger = Logger.getLogger(CashOutDelimaConfirmSms.class);

    @Persist
    private CashOutDelimaView cashOutDelimaView;
//    @Property
//    private int tokenType;
//    @Property
//    private TokenView tokenView;
    @Inject
    private CacheManager cacheManager;
    @InjectPage
    private CashOutDelimaReceipt cashOutDelimaReceipt;
    @Inject
    private OtpManager otpManager;
    @Inject
    private CashOutDelimaViewService cashOutDelimaViewService;
    @InjectComponent
    private Form form;
    @Inject
    private ThreadLocale threadLocale;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());


    void setTokenType() {
//        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
//        tokenType = formConfig.getTokenType();
    }

    void onPrepare() {
//        if (tokenView == null) {
//            tokenView = new TokenView();
//        }
    }

    void onActivate() {
        if (cashOutDelimaView == null) {
            cashOutDelimaView = new CashOutDelimaView();
        }
    }

    void setupRender() {
        setTokenType();
        logger.info("before cashOutDelimaView.getTransactionType()=" + cashOutDelimaView.getTransactionType());
        cashOutDelimaView.setTransactionType(Constants.CASHOUT_DELIMA_SMS_VERIFICATION);
        logger.info("after cashOutDelimaView.getTransactionType()=" + cashOutDelimaView.getTransactionType());
    }

    void onValidateFromForm() {
        try {
            cashOutDelimaView = (CashOutDelimaView) cashOutDelimaViewService.smsProcess(cashOutDelimaView);
        } catch (BusinessException e) {
            logger.info("e=" + e.getFullMessage());
            form.recordError(e.getFullMessage());
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        cashOutDelimaReceipt.setCashOutDelimaView(cashOutDelimaView);
        return cashOutDelimaReceipt;
    }


    @DiscardAfter
    public Object onSelectedFromCancel() {
        return CashOutDelimaInput.class;
    }

    public CashOutDelimaView getCashOutDelimaView() {
        return cashOutDelimaView;
    }

    public void setCashOutDelimaView(CashOutDelimaView cashOutDelimaView) {
        this.cashOutDelimaView = cashOutDelimaView;
    }
}
