package com.dwidasa.ib.pages.account;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.InputStream;
import java.text.DecimalFormat;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.Response;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.AccountException;
import com.dwidasa.engine.service.AccountExceptionService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.custom.AttachmentStreamResponse;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;
import com.howardlewisship.tapx.datefield.components.DateField;
import com.sun.xml.bind.CycleRecoverable.Context;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/1/11
 * Time: 8:49 PM
 */
@Import(library = "context:bprks/js/account/AccountStatement.js")
public class AccountStatement {
    @Property
    @Persist
    private AccountView accountView;

    @Property
    private Date startDate;

    @Property
    private Date endDate;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, threadLocale.getLocale());

    @InjectComponent
    private Form form;

    @InjectComponent("startDate")
    private DateField start;

    @Inject
    private Messages messages;

    @Inject
    private AccountService accountService;

    @Inject
    private AccountExceptionService accountExceptionService;
    
    @InjectPage
    private AccountStatementResult accountStatementResult;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private CacheManager cacheManager;

    @Persist
	private List<AccountException> accExceptList;
    
    public String getShortPattern() {
        return Constants.SHORT_FORMAT;
    }

    void onPrepare() {

        if (sessionManager.getLoggedCustomerView().getCustomerType() == com.dwidasa.engine.Constants.CUSTOMER_TYPE.INDIVIDUAL){
            startDate = DateUtils.before(new Date(), 30);
        }
        else {
            startDate = new Date();
        }

    	endDate = new Date();
        if (accountView == null) {
            accountView = new AccountView();
        }
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		System.out.println("not active" +sessionManager.isNotActivatedYet());
    		return EulaWelcome.class;
    	}
    	return null;
    }
    
    public void setupRender(){
    	form.clearErrors();
    }
    public boolean isException(){
    	try{
    		 return cacheManager.isAccountException(sessionManager.getLoggedCustomerView().getAccountNumber());
    	}catch (BusinessException Be) {
    		System.out.println("BE: "+Be);
		}
       catch (Exception e){
           e.printStackTrace();
           System.out.println("E: "+e);
       }
    	return false;
    }
  //uncomment this 
    void onValidateFromForm() {

    	Date now = DateUtils.truncate(new Date());

        if (DateUtils.truncate(startDate).compareTo(DateUtils.truncate(endDate)) > 0) {
            form.recordError(start, messages.get("startDate-acrossField-message"));
        }
        else if (DateUtils.before(new Date(), 31).compareTo(DateUtils.truncate(startDate)) > 0) {
            form.recordError(start, messages.get("startDate-acrossField-message"));
        }
        else if(DateUtils.truncate(endDate).compareTo(now) > 0){
        	form.recordError(start, messages.get("startDate-acrossField-message"));
        }

    }

    Object onSelectedFromNext() {
        AccountInfo ai = sessionManager.getAccountInfo(accountView.getAccountNumber());

        accountView.setCurrencyCode(ai.getCurrencyCode());
        accountView.setAccountType(ai.getAccountType());
        accountView.setAccountNumber(ai.getAccountNumber());
        accountView.setCardNumber(ai.getCardNumber());
        accountView.setCustomerName(sessionManager.getLoggedCustomerView().getName());

        accountView.setMerchantType(sessionManager.getDefaultMerchantType());
        accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());

        accountStatementResult.setAccountView(accountView);
        accountStatementResult.setStartDate(startDate);
        accountStatementResult.setEndDate(endDate);
        accountStatementResult.setReferer(this.getClass().getSimpleName());

        return accountStatementResult;
    }

    void pageReset() {
        accountView = null;
        accountStatementResult.setAccountView(null);
    }
}
