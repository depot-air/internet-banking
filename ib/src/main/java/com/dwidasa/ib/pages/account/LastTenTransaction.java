package com.dwidasa.ib.pages.account;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.pages.Index;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/1/11
 * Time: 8:49 PM
 */
public class LastTenTransaction {
	private static Logger logger = Logger.getLogger( LastTenTransaction.class );
	@Persist
    private AccountView accountView;

    @Property(write = false)
    @Persist
    private String referer;

    @Persist
    private Date startDate;

    @Persist
    private Date endDate;
    
    @Property(write = false)
    @Persist
    private List<AccountStatementView> asvList;
    
    @Property
    private AccountStatementView asv;

    @Inject
    private AccountService accountService;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getNumberInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());

    @Property
    private BigDecimal beginningBalance;

    @Property
    private BigDecimal credit;

    @Property
    private BigDecimal debet;

    @Property
    private BigDecimal endingBalance;

    @Property
    private EvenOdd evenOdd;

	@InjectPage
    private AccountSummary accountSummary;

    @InjectPage
    private AccountStatementResult accountStatementResult;

    @Property
    private final ValueEncoder<AccountStatementView> encoder = new ValueEncoder<AccountStatementView>() {
        public String toClient(AccountStatementView value) {
            return String.valueOf(value.getAccountNumber());
        }

        public AccountStatementView toValue(String clientValue) {
            return new AccountStatementView();
        }
    };

	public void setAsvList(List<AccountStatementView> asvList) {
		this.asvList = asvList;
	}

	public AccountView getAccountView() {
		return accountView;
	}
    
    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

    public void setReferer(String referer) {
        this.referer = referer;
    }

    Object onActivate() {
        if (accountView == null) {
            return Index.class;
        }

        return null;
    }

    void setupRender() {
        evenOdd = new EvenOdd();
        evenOdd.setEven(false);        
        accountView.setCurrencyCode(Constants.CURRENCY_CODE_IDR);	//yg muncul bukan 360
        
        if (asvList != null && asvList.size() > 0) {
        	AccountStatementView firstAsv = asvList.get(0);
        	AccountStatementView lastAsv = asvList.get(asvList.size() - 1);
        	beginningBalance = firstAsv.getTransactionIndicator().equals("D") ? firstAsv.getRunningBalance().add(firstAsv.getAmount()) : firstAsv.getRunningBalance().subtract(firstAsv.getAmount()) ;
        	endingBalance = lastAsv.getRunningBalance();
        	        	
            debet = BigDecimal.ZERO;
            credit = BigDecimal.ZERO;

//            for ( AccountStatementView asv : asvList) {
            for (int i = 0; i < asvList.size(); i ++) {
            	AccountStatementView asv = asvList.get(i);
                if (asv.getTransactionIndicator().equals("D")) {
                    debet = debet.add(asv.getAmount());
                    //asv.setRunningBalance(asv.getRunningBalance().subtract(asv.getAmount()));		//diset di serviceImpl
                    asv.setFormattedAmount("(" + formatter.format(asv.getAmount()) + ")");
                }
                else {
                	String formattedAmount = formatter.format(asv.getAmount());
                    credit = credit.add(asv.getAmount());
                    //asv.setRunningBalance(asv.getRunningBalance().add(asv.getAmount()));			////diset di serviceImpl
                    asv.setFormattedAmount(formattedAmount);
                }
            }     
        }               
    }
    
    @DiscardAfter
    public Object onSelectedFromBack() {
        if (referer.equals(accountSummary.getClass().getSimpleName())) {
            accountSummary.setAccountView(accountView);
            return accountSummary;
        }

        return AccountStatement.class;
    }
    

    public boolean isNotNullDebet() {
    	return debet != null;
    }
}
