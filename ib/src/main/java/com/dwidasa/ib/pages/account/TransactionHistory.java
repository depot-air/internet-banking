package com.dwidasa.ib.pages.account;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.dwidasa.engine.AirConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;
import com.howardlewisship.tapx.datefield.components.DateField;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
@Import(library = "context:bprks/js/account/TransactionHistory_123.js")
public class TransactionHistory {
    @Property
    @Persist
    private AccountView accountView;

    @Property
    private Date startDate;

    @Property
    private Date endDate;

    @Inject
    private Locale locale;

    @Property
    private String periodValue;

    @Property
    private String transactionType;

    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, locale);

    @InjectComponent
    private Form form;

    @InjectComponent("startDate")
    private DateField start;

    @Inject
    private Messages messages;

    @Inject
    private AccountService accountService;

    @InjectPage
    private TransactionHistoryResult transactionHistoryResult;

    @Inject
    private SessionManager sessionManager;

    @Property
    private Map<String, String> transactionTypeModel;

    public String getShortPattern() {
        return Constants.SHORT_FORMAT;
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    void onPrepare() {
        startDate = new Date();
        endDate = new Date();
        if (accountView == null) {
            accountView = new AccountView();
        }

        transactionTypeModel = new LinkedHashMap<String, String>();
        transactionTypeModel.put(com.dwidasa.engine.Constants.WATER.TRANSACTION_TYPE.POSTING, messages.get("paymentWater"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.PLN_PURCHASE_CODE, messages.get("purchasePln"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.PLN_PAYMENT_CODE, messages.get("paymentPln"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.TELKOM_PAYMENT_CODE, messages.get("paymentTelkom"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.KAI_PAYMENT, messages.get("purchaseTrain"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.TRANSPORTATION_PAYMENT_CODE, messages.get("paymentTrain"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.ENTERTAINMENT_PAYMENT_CODE, messages.get("paymentTv"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.INTERNET_PAYMENT_CODE, messages.get("paymentInternet"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.TELCO_PAYMENT_CODE, messages.get("paymentCellular"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE, messages.get("delimaCashIn"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CODE, messages.get("delimaCashOut"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.REFUND_DELIMA_CODE, messages.get("delimaRefund"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian, messages.get("mncLife"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.DOMPETKU_CASH_IN, messages.get("topUp"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.DOMPETKU_CASH_OUT, messages.get("cashOut"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.DOMPETKU_CASH_OUT, messages.get("cashOut"));
        transactionTypeModel.put(AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, messages.get("aeroTicketingInputAero"));
        transactionTypeModel.put(AirConstants.VOLTRAS.TRANSACTION_TYPE.TICKET, messages.get("aeroTicketingInputVoltras"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.TIKET_KERETA_DJATI.POS_KERETA_DJATI, messages.get("ticketKramatDjati"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE, "Multi Finance MTF");
        transactionTypeModel.put(com.dwidasa.engine.Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE_MNC, "Multi Finance MNC");
        transactionTypeModel.put(com.dwidasa.engine.Constants.PAYMENT_KARTU_KREDIT_BNI.PAYMENT_BNI, messages.get("kartuKreditBNI"));
        periodValue = "today";
        transactionType = "all";
    }

    void onValidateFromForm() {
        if (DateUtils.truncate(startDate).compareTo(DateUtils.truncate(endDate)) > 0) {
            form.recordError(start, messages.get("startDate-acrossField-message"));
        }
        else if (DateUtils.before(new Date(), 31).compareTo(DateUtils.truncate(startDate)) > 0) {
            form.recordError(start, messages.get("startDate-acrossField-message"));
        }
    }

    Object onSelectedFromNext() {
        if (periodValue.equals("today")) {
            startDate = new Date();
            endDate = startDate;
        }

        if (transactionType.equals("all")) {
            accountView.setTransactionType(null);
        }

        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transactionHistoryResult.setAccountView(accountView);
        transactionHistoryResult.setStartDate(startDate);
        transactionHistoryResult.setEndDate(endDate);

        return transactionHistoryResult;
    }
}
