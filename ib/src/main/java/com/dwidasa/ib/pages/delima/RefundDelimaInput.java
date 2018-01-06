package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.RefundDelimaView;
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
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/26/11
 * Time: 00:08 am
 */
public class RefundDelimaInput {
    @Property
    @Persist
    private RefundDelimaView refundDelimaView;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private CacheManager cacheManager;
    @Property
    private int tokenType;
    @InjectPage
    private RefundDelimaConfirm refundDelimaConfirm;
    @Property
    private TokenView tokenView;
    @Inject
    private OtpManager otpManager;
    @Inject
    private DelimaService delimaService;
    @InjectComponent
    private Form form;
    @InjectPage
    private RefundDelimaReceipt refundDelimaReceipt;

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void setupRender() {
        if (refundDelimaView == null) {
            refundDelimaView = new RefundDelimaView();
        }
        if (tokenView == null) {
            tokenView = new TokenView();
        }
        refundDelimaView.setTransactionType(com.dwidasa.engine.Constants.REFUND_DELIMA_CODE);
        setTokenType();
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(refundDelimaView.getCustomerId(),
                    this.getClass().getSimpleName(), tokenView)) {
                setRefundData();
                refundDelimaView.setTransactionType(com.dwidasa.engine.Constants.REFUND_DELIMA_INQ_CODE);
                refundDelimaView = (RefundDelimaView) delimaService.inquiry(refundDelimaView);
                refundDelimaView.setTransactionType(com.dwidasa.engine.Constants.REFUND_DELIMA_CODE);
                delimaService.confirm(refundDelimaView);
            }
        } catch (BusinessException e) {
            refundDelimaView.setTransactionType(com.dwidasa.engine.Constants.REFUND_DELIMA_CODE);
            form.recordError(e.getFullMessage());
        }
    }

    public void setRefundData() {

        refundDelimaView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        refundDelimaView.setAccountType(sessionManager.getAccountType(refundDelimaView.getAccountNumber()));
        refundDelimaView.setCurrencyCode(Constants.CURRENCY_CODE);
        refundDelimaView.setTransactionDate(new Date());
        refundDelimaView.setMerchantType(sessionManager.getDefaultMerchantType());
        refundDelimaView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        refundDelimaView.setToAccountType("00");

    }

    public Object onSuccess() {
        refundDelimaConfirm.setRefundDelimaView(refundDelimaView);
        return RefundDelimaConfirm.class;
    }

    public void pageReset() {
        refundDelimaView = null;
        refundDelimaConfirm.setRefundDelimaView(null);
        refundDelimaReceipt.setRefundDelimaView(null);


    }


}
