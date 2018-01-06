package com.dwidasa.ib.pages.account;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.services.SessionManager;


public class LotteryResult {
	
	@Property(write = false)
    @Persist
    private LotteryView lotteryView;

    @InjectComponent
    private Form form;
    
    @Property(write = false)
    @Persist
    private String periodMonth;

    @Property(write = false)
    @Persist
    private Integer periodYear;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @Property
    private EvenOdd evenOdd;

    @Property
    private String customerName;
    
    public void setLotteryView(LotteryView lotteryView) {
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
            return LotteryInput.class;
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

    @DiscardAfter
    public Object onSelectedFromBack() {
        return LotteryInput.class;
    }

}
