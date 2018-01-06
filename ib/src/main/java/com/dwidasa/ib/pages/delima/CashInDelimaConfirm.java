package com.dwidasa.ib.pages.delima;

import java.math.BigDecimal;
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
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/25/11
 * Time: 00:47 am
 */
public class CashInDelimaConfirm {

    @Property
    private int tokenType;

    @Property
    private TokenView tokenView;

    @Property
    private BigDecimal total;

    @Persist
    private CashInDelimaView cashInDelimaView;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;
    
    @InjectPage
    private CashInDelimaReceipt cashInDelimaReceipt;


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
        total = cashInDelimaView.getAmount().add(cashInDelimaView.getProviderFee());

    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (cashInDelimaView == null) {
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
        cashInDelimaView.setTransactionType(Constants.CASHIN_DELIMA_CODE);
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(cashInDelimaView.getCustomerId(), this.getClass().getSimpleName(),
                    tokenView)) {
                cashInDelimaView = (CashInDelimaView) delimaService.execute(cashInDelimaView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        cashInDelimaReceipt.setCashInDelimaView(cashInDelimaView);
        return cashInDelimaReceipt;
    }

    @DiscardAfter
    public Object onSelectedFromCancel() {
        return CashInDelimaInput.class;
    }


    public CashInDelimaView getCashInDelimaView() {
        return cashInDelimaView;
    }

    public void setCashInDelimaView(CashInDelimaView cashInDelimaView) {
        this.cashInDelimaView = cashInDelimaView;
    }
}
