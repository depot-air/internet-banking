package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import com.howardlewisship.tapx.datefield.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class EulaEmailInput {
	
	@InjectPage
	private EulaAgreement eulaAgreement;
	
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

//    @InjectComponent("startDate")
//    private DateField start;

    @Inject
    private Messages messages;

    @Inject
    private AccountService accountService;

//    @InjectPage
//    private TransactionHistoryResult transactionHistoryResult;

    @Inject
    private SessionManager sessionManager;

    @Property
    private Map<String, String> transactionTypeModel;

    public String getShortPattern() {
        return Constants.SHORT_FORMAT;
    }

    void onPrepare() {
        startDate = new Date();
        endDate = new Date();
        if (accountView == null) {
            accountView = new AccountView();
        }

        transactionTypeModel = new LinkedHashMap<String, String>();
        transactionTypeModel.put(com.dwidasa.engine.Constants.PLN_PURCHASE_CODE, messages.get("purchasePln"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.PLN_PAYMENT_CODE, messages.get("paymentPln"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.TELKOM_PAYMENT_CODE, messages.get("paymentTelkom"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.TRANSPORTATION_PAYMENT_CODE,
                messages.get("paymentTrain"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.ENTERTAINMENT_PAYMENT_CODE,
                messages.get("paymentTv"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.INTERNET_PAYMENT_CODE,
                messages.get("paymentInternet"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.TELCO_PAYMENT_CODE,
                messages.get("paymentCellular"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE,
                messages.get("delimaCashIn"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CODE,
                messages.get("delimaCashOut"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.REFUND_DELIMA_CODE,
                messages.get("delimaRefund"));

        periodValue = "today";
        transactionType = "all";
    }

    void onValidateFromForm() {
//        if (DateUtils.truncate(startDate).compareTo(DateUtils.truncate(endDate)) > 0) {
//            form.recordError(start, messages.get("startDate-acrossField-message"));
//        }
//        else if (DateUtils.before(new Date(), 31).compareTo(DateUtils.truncate(startDate)) > 0) {
//            form.recordError(start, messages.get("startDate-acrossField-message"));
//        }
    }

//    Object onSelectedFromNext() {
//        if (periodValue.equals("today")) {
//            startDate = new Date();
//            endDate = startDate;
//        }
//
//        if (transactionType.equals("all")) {
//            accountView.setTransactionType(null);
//        }
//
//        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
//        transactionHistoryResult.setAccountView(accountView);
//        transactionHistoryResult.setStartDate(startDate);
//        transactionHistoryResult.setEndDate(endDate);
//
//        return transactionHistoryResult;
//    }
    
    public Object onSuccess() {
    	return eulaAgreement;
    }
}
