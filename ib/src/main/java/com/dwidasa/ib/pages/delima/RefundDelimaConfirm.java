package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.RefundDelimaView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.ib.services.OtpManager;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/26/11
 * Time: 00:09 am
 */
public class RefundDelimaConfirm {
    @Persist
    private RefundDelimaView refundDelimaView;
    @Property
    private BigDecimal total;
    @Inject
    private CacheManager cacheManager;
    @Property
    private int tokenType;
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
    @Inject
    private ThreadLocale threadLocale;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    @Property
    private DateFormat mediumDate = new SimpleDateFormat(com.dwidasa.ib.Constants.MEDIUM_FORMAT,
            threadLocale.getLocale());


    void setTotal() {
        total = refundDelimaView.getAmount().add(refundDelimaView.getProviderFee());
    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (refundDelimaView == null) {
            return RefundDelimaInput.class;
        }
        return null;
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void setupRender() {
        setTotal();
        setTokenType();
        refundDelimaView.setTransactionType(Constants.REFUND_DELIMA_CODE);
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(refundDelimaView.getCustomerId(), this.getClass().getSimpleName(),
                    tokenView)) {
                refundDelimaView = (RefundDelimaView) delimaService.execute(refundDelimaView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        refundDelimaReceipt.setRefundDelimaView(refundDelimaView);
        return refundDelimaReceipt;
    }

    @DiscardAfter
    public Object onSelectedFromCancel() {
        return RefundDelimaInput.class;
    }

    public RefundDelimaView getRefundDelimaView() {
        return refundDelimaView;
    }

    public void setRefundDelimaView(RefundDelimaView refundDelimaView) {
        this.refundDelimaView = refundDelimaView;
    }
}
