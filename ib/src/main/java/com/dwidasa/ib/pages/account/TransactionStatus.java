package com.dwidasa.ib.pages.account;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.pages.purchase.MncLifeStatusPurchaseConfirm;
import com.dwidasa.ib.services.AccountResource;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class TransactionStatus {
    @Property
    @Persist
    private AccountView accountView;

//    @Property
//    private Date startDate;
//
//    @Property
//    private Date endDate;

    @Property
    private String status;

    @Inject
    private ThreadLocale threadLocale;
    
    @Inject
	private ParameterDao parameterDao;

    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, threadLocale.getLocale());

    @InjectComponent
    private Form form;
    
    @Inject
    private AccountResource accountResource;

//    @InjectComponent("startDate")
//    private DateField start;

    @Inject
    private Messages messages;

    @Inject
    private AccountService accountService;

    @InjectPage
    private TransactionStatusResult transactionStatusResult;
    
    @InjectPage
    private MncLifeStatusPurchaseConfirm mncLifeStatusPurchaseConfirm;

    @Inject
    private SessionManager sessionManager;

    @Property
    private Map<String, String> transactionTypeModel;

    @Property
    private Map<String, String> transactionStatusModel;

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
//        startDate = new Date();
//        endDate = new Date();
        if (accountView == null) {
            accountView = new AccountView();
        }

        transactionTypeModel = new LinkedHashMap<String, String>();
        transactionTypeModel.put(com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE, messages.get("purchaseTelco"));
        transactionTypeModel.put(com.dwidasa.engine.Constants.PLN_PURCHASE_CODE, messages.get("purchasePln"));
        if (isMerchant()) {
            transactionTypeModel.put(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian, messages.get("mncLife"));
            transactionTypeModel.put(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE, messages.get("delimaCashIn"));
            transactionTypeModel.put(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CODE, messages.get("delimaCashOut"));
            transactionTypeModel.put(com.dwidasa.engine.Constants.CASHTOBANK_POS_CODE, messages.get("delimaCashToBank"));
            transactionTypeModel.put(com.dwidasa.engine.Constants.CASHOUT_ELMO_POS_CODE, messages.get("delimaCashOutElmo"));
		}else{
            transactionTypeModel.put(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE, messages.get("delimaCashIn"));
		}

        transactionStatusModel = new LinkedHashMap<String, String>();
        transactionStatusModel.put("both", messages.get("both"));
        transactionStatusModel.put(com.dwidasa.engine.Constants.PENDING_STATUS, messages.get("pending"));
        transactionStatusModel.put(com.dwidasa.engine.Constants.CANCELED_STATUS, messages.get("canceled"));
    }

    void onValidateFromForm() {
//        if (DateUtils.truncate(startDate).compareTo(DateUtils.truncate(endDate)) > 0) {
//            form.recordError(start, messages.get("startDate-acrossField-message"));
//        }
//        else if (DateUtils.before(new Date(), 31).compareTo(DateUtils.truncate(startDate)) > 0) {
//            form.recordError(start, messages.get("startDate-acrossField-message"));
//        }
    }

    Object onSelectedFromNext() {
        AccountInfo ai = sessionManager.getAccountInfo(accountView.getAccountNumber());

        accountView.setCurrencyCode(ai.getCurrencyCode());
        accountView.setAccountType(ai.getAccountType());
        accountView.setMerchantType(sessionManager.getDefaultMerchantType());
        accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());

        transactionStatusResult.setAccountView(accountView);
        transactionStatusResult.setStatus(status);
        transactionStatusResult.setStartDate(new Date());	//startDate
        transactionStatusResult.setEndDate(new Date());		//endDate

        if(accountView.getTransactionType().equals(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian)){
        	//mncLifeStatusPurchaseConfirm.setMncLifeStatusConfirm(null);
    		mncLifeStatusPurchaseConfirm.setFromHistory(true);
        	return mncLifeStatusPurchaseConfirm;
        }
             
        return transactionStatusResult;
    }

    void pageReset() {
        accountView = null;
        transactionStatusResult.setAccountView(null);
    }
    
    public boolean isMerchant() {    	
//		String firstFour = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
//    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
//    	String[] tokens = ip.getParameterValue().split(",");
//    	boolean isMerch = false;
//    	if (tokens.length > 0 ) {
//    		for(int i = 0; i < tokens.length; i++) {
//    			if (firstFour.equals(tokens[i]))
//    				isMerch = true;
//    		}
//    	}
    	return sessionManager.isMerchant();
    }
    
}
