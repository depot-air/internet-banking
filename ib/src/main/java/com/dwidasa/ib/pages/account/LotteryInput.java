package com.dwidasa.ib.pages.account;

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
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;


public class LotteryInput {
	
	@Property
    @Persist
    private LotteryView lotteryView;
	
    @Property
    private String periodMonth;

    @Property
    private Integer periodYear;
    
    @InjectPage
    private LotteryResult lotteryResult;
    
    @Inject
    private AccountService accountService;

    @Inject
    private SessionManager sessionManager;

    @InjectComponent
    private Form form;

    
    public String getShortPattern() {
        return Constants.SHORT_FORMAT;
    }
    void onActivate(String periodMonth) {
		this.periodMonth = periodMonth;
	}

    String[] monthNames = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY",
    		"AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    void onPrepare() {
    	Calendar cal = Calendar.getInstance();	
        periodYear = cal.get(Calendar.YEAR);
        periodMonth = monthNames[cal.get(Calendar.MONTH)];
    
        if (lotteryView == null) {
            lotteryView = new LotteryView();
        }
	}

    void onValidateFromForm() {
    	try {
	    	AccountInfo ai = sessionManager.getAccountInfo(lotteryView.getAccountNumber());
	        lotteryView.setTransactionType(com.dwidasa.engine.Constants.INQUIRY_LOTTERY);
	 
	        lotteryView.setAccountType(ai.getAccountType());
	        lotteryView.setCardNumber(ai.getCardNumber());
	        lotteryView.setAccountNumber(ai.getAccountNumber());
	        lotteryView.setCurrencyCode(Constants.CURRENCY_CODE);
	        lotteryView.setMerchantType(sessionManager.getDefaultMerchantType());
	        lotteryView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
	        lotteryView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
	 
	        String sMonth = getMonthParam();
	        lotteryView.setPeriodMonth(sMonth);
	        lotteryView.setPeriodYear("" + periodYear);
	        lotteryView=accountService.getLotteryViewPoint(lotteryView);
    	} catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    String getMonthParam() {
    	 String sMonth = "01";
         for(int i = 1; i < monthNames.length; i++) {
         	if (periodMonth.equals(monthNames[i - 1])) {
         		sMonth = (i < 10) ? "0" + i : "" + i;
         		return sMonth;
         	}
         }
         return sMonth;
    }
    
    Object onSuccess() {
        lotteryResult.setLotteryView(lotteryView);        
        return lotteryResult;
    }
    public SelectModel getYearModel() {
        List<OptionModel> optionModels = new ArrayList<OptionModel>();
        SelectModel res = new SelectModelImpl(null, optionModels);
        
        for (int year = Calendar.getInstance().get(Calendar.YEAR)+1; year >= Calendar.getInstance().get(Calendar.YEAR)-5; year--)
        {
                OptionModel temp = new OptionModelImpl(new Integer(year).toString(), year);
                optionModels.add(temp);
          
        }
        return res;
    }

    void pageReset() {
        lotteryView = null;
        lotteryResult.setLotteryView(null);
    }

}
