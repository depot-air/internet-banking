package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.CashToBankDelimaView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
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
 * Date: 7/25/11
 * Time: 00:47 am
 */
public class CashToBankDelimaConfirm {

    @Property
    private int tokenType;

    @Property
    private TokenView tokenView;

    @Property
    private BigDecimal total;

    @Persist
    private CashToBankDelimaView cashToBankDelimaView;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;
    
    @InjectPage
    private CashToBankDelimaReceipt cashToBankDelimaReceipt;


    @Inject
    private DelimaService delimaService;

    @Inject
    private OtpManager otpManager;

    @Inject
    private ThreadLocale threadLocale;

    @InjectComponent
    private Form form;

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(com.dwidasa.ib.Constants.MEDIUM_FORMAT,
            threadLocale.getLocale());
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());


    public void setTotal() {
        total = cashToBankDelimaView.getAmount().add(cashToBankDelimaView.getProviderFee());

    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (cashToBankDelimaView == null) {
            return CashInDelimaInput.class;
        }
        return null;
    }


    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    void setupRender() {
        setTotal();
        setTokenType();
        cashToBankDelimaView.setTransactionType(Constants.CASHIN_DELIMA_CODE);
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(cashToBankDelimaView.getCustomerId(), this.getClass().getSimpleName(),
                    tokenView)) {
                cashToBankDelimaView = (CashToBankDelimaView) delimaService.execute(cashToBankDelimaView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        cashToBankDelimaReceipt.setCashToBankDelimaView(cashToBankDelimaView);
        return cashToBankDelimaReceipt;
    }

    @DiscardAfter
    public Object onSelectedFromCancel() {
        return CashInDelimaInput.class;
    }


    public CashToBankDelimaView getCashToBankDelimaView() {
        return cashToBankDelimaView;
    }

    public void setCashToBankDelimaView(CashToBankDelimaView cashToBankDelimaView) {
        this.cashToBankDelimaView = cashToBankDelimaView;
    }
}
