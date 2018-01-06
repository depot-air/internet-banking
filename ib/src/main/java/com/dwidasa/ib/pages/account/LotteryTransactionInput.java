package com.dwidasa.ib.pages.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.model.view.PortfolioView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;


public class LotteryTransactionInput {
	
	@Property
    @Persist
    private LotteryTransactionView lotteryView;
	
    @Property
    private String periodMonth;

    @Property
    private Integer periodYear;
    
    @Inject
    private PurchaseService purchaseService;
    
    @InjectPage
    private LotteryTransactionResult lotteryResult;
    
    @Inject
    private AccountService accountService;

    @Inject
    private SessionManager sessionManager;
    
    @Property
    private String jenisUndian;

    @InjectComponent
    private Form form;

    @Inject
    private CustomerAccountDao customerAccountDao;
    
    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }
    
    void setupRender(){
    	jenisUndian = "hujanRejeki";
    }

    void onPrepare() { //
    	
        if (lotteryView == null) {
            lotteryView = new LotteryTransactionView();
        }
	}

    void onValidateFromForm() {
    	
    	
    	try {
	    	AccountInfo ai = sessionManager.getAccountInfo(sessionManager.getLoggedCustomerView().getAccountNumber());
	        lotteryView.setTransactionType(com.dwidasa.engine.Constants.INQUIRY_LOTTERY);
	 
	        lotteryView.setAccountType(ai.getAccountType());
	        lotteryView.setCardNumber(ai.getCardNumber());
	        lotteryView.setAccountType("10");
	        lotteryView.setToAccountType("00");
	        lotteryView.setAccountNumber(getAllAccountNumber());
	        lotteryView.setCurrencyCode(Constants.CURRENCY_CODE);
	        lotteryView.setMerchantType(sessionManager.getDefaultMerchantType());
	        lotteryView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
	        lotteryView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
	        
	        lotteryView.setPosisiAwal(BigDecimal.ZERO);
	        lotteryView.setTotalData(BigDecimal.ZERO);
	        lotteryView.setNextAvailableFlag("N");

	        
	        List< LotteryTransactionView > pvs = accountService.getPortfolioFromIGateUndian(lotteryView, com.dwidasa.engine.Constants.TRANSLATION_LOTTERY);
	        //lotteryView.setLotteryTransactionViews(pvs);
	        
    	} catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }
//
    Object onSuccess() {
        lotteryResult.setLotteryView(lotteryView);  
        //lotteryResult.setLotteryViews(lotteryView.getLotteryTransactionViews());
        return lotteryResult;
    }
    
//    void pageReset() {
//        lotteryView = null;
//        lotteryResult.setLotteryView(null);
//    }
    
    public String getAllAccountNumber(){
    	List<CustomerAccount> customerAccounts = customerAccountDao.getAllWithTypeAndCurrency(sessionManager.getLoggedCustomerView().getId());
    	String delimitter = "";
    	for(CustomerAccount account : customerAccounts){
    		if("".equals(delimitter)){
    			delimitter += account.getAccountNumber();
    		}else
    			delimitter += ","+account.getAccountNumber();
    	}
    	
    	return delimitter;
    }

}
