package com.dwidasa.ib.pages.account;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
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
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.services.SessionManager;


public class LotteryTransactionResult {
	
	@Property(write = false)
    @Persist
    private LotteryTransactionView lotteryView;
	
    @InjectComponent
    private Form form;
    
    @Property(write = false)
    @Persist
    private String periodMonth;
    
    @Inject
    private AccountService accountService;
    
    @Inject
    private CustomerAccountDao customerAccountDao;
    
   
    @Persist
    private List<LotteryTransactionView> lotteryViews;

    @Property(write = false)
    @Persist
    private Integer periodYear;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @InjectPage
    private LotteryTransactionResultUndian lotteryResultUndian;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @Property
    private EvenOdd evenOdd;

    @Property
    private String customerName;
    
    private BigDecimal totalData;
    
    public void setLotteryView(LotteryTransactionView lotteryView) {
		this.lotteryView = lotteryView;
	}
    
    public void setPeriodMonth(String periodMonth) {
        this.periodMonth = periodMonth;
      	}
    
    public void setPeriodYear(Integer periodYear) {
      this.periodYear = periodYear;
    	}

    Object onActivate() {
    
        if (lotteryView == null) {
        	
        	try {
        		lotteryView = new LotteryTransactionView();
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
    	        lotteryView.setNextAvailableFlag("");

    	        
    	        List< LotteryTransactionView > pvs = accountService.getPortfolioFromIGateUndian(lotteryView, com.dwidasa.engine.Constants.TRANSLATION_LOTTERY);
    	        setLotteryViews(pvs);
    	        
        	} catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    void setupRender() {
        evenOdd = new EvenOdd();
        evenOdd.setEven(true);
        customerName = sessionManager.getLoggedCustomerPojo().getCustomerName();
        if (lotteryView.getTransactionDate() == null) {
        	lotteryView.setTransactionDate(new Date());
        }
        
        
        
    }
    
    @Property
	private final ValueEncoder<LotteryView> encoder = new ValueEncoder<LotteryView>() {
		public String toClient(LotteryView value) {
			return String.valueOf(value.getAccountNumber());
		}

		public LotteryView toValue(String clientValue) {
			return new LotteryView();
		}
	};
	
	public void setLotteryViews(List<LotteryTransactionView> lotteryViews) {
		this.lotteryViews = lotteryViews;
	}
	
	public List<LotteryTransactionView> getLotteryViews() {
		return lotteryViews;
	}
	
	
	//@DiscardAfter
    Object onViewStatement(String accountNumber, String kodeUndian, String jenisUndian) {
    	
    	lotteryView = new LotteryTransactionView();
        AccountInfo ai = sessionManager.getAccountInfo(sessionManager.getLoggedCustomerView().getAccountNumber());
        lotteryView.setTransactionType(com.dwidasa.engine.Constants.INQUIRY_NOMOR_UNDIAN);
 
        lotteryView.setAccountType(ai.getAccountType());
        lotteryView.setCardNumber(ai.getCardNumber());
        lotteryView.setAccountNumber(accountNumber);
        lotteryView.setCurrencyCode(Constants.CURRENCY_CODE);
        lotteryView.setMerchantType(sessionManager.getDefaultMerchantType());
        lotteryView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        lotteryView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        lotteryView.setKodeUndian(kodeUndian);
        lotteryView.setPosisiAwal(BigDecimal.ZERO);
      
        List< LotteryTransactionView > pvs = accountService.getPortfolioFromIGateNomorUndian(lotteryView, com.dwidasa.engine.Constants.TRANSLATION_LOTTERY);
        for(LotteryTransactionView view : pvs){
        	lotteryView.setAccountNumber(view.getAccountNumber());
        	lotteryView.setNamaUndian(view.getNamaUndian());
        	lotteryView.setAwalPeriod(view.getAwalPeriod());
        	lotteryView.setAkhirPeriod(view.getAkhirPeriod());
        	lotteryView.setTotalData(view.getTotalData());
        	setTotalData(view.getTotalData());
        }
        
        if(pvs != null){
        	
        	List<LotteryTransactionView> lotteryTransactionViews = new ArrayList<LotteryTransactionView>();
        	int x = 1;
        	String data = "";
        	for(int i=0; i<pvs.size(); i++){
        		
        		LotteryTransactionView transactionView = pvs.get(i);
        		 if (x == 1) 
        			 data = transactionView.getNomorUndian();
                 else 
                	 data = data + " | " + transactionView.getNomorUndian();
        		 
        		  x = x + 1;
                  if (x == 9)
                  {
                      LotteryTransactionView temp = new LotteryTransactionView();
                      temp.setNomorUndian(data);
                      lotteryTransactionViews.add(temp);
                      x = 1;
                  }
                  else if (i == pvs.size() - 1)
                  {
                      LotteryTransactionView temp = new LotteryTransactionView();
                      temp.setNomorUndian(data);
                      lotteryTransactionViews.add(temp);
                  }
                  
        	}
        	
        	
        	
             lotteryResultUndian.setLotteryView(lotteryView);
             lotteryResultUndian.setAccountNumber(lotteryView.getAccountNumber());
             lotteryResultUndian.setJenisUndian(lotteryView.getNamaUndian());
             lotteryResultUndian.setTglAwalPeriod(lotteryView.getAwalPeriod());
             lotteryResultUndian.setTglAkhirPeriod(lotteryView.getAkhirPeriod());
             lotteryResultUndian.setTotalPerolehan(lotteryView.getTotalData());
             lotteryResultUndian.setLotteryViews(lotteryTransactionViews);
                  
        } 
        
       
        
        	return lotteryResultUndian; 
        
        
    }
    
    public BigDecimal getTotalData() {
		return totalData;
	}
    
    public void setTotalData(BigDecimal totalData) {
		this.totalData = totalData;
	}
    
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
