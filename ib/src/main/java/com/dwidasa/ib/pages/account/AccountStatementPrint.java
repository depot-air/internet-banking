package com.dwidasa.ib.pages.account;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;
import com.dwidasa.ib.common.EvenOdd;

public class AccountStatementPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @InjectPage
    private AccountStatementResult accountStatementResult;

    @Property
    private AccountView accountView;

    @Persist
    @Property
    private List<AccountStatementView> asvList;

    @Persist
    @Property
    private List<AccountStatementView> asvListPaging;

    @Property
    private AccountStatementView asv;
    
    @Property
    private Date startDate;

    @Property
    private Date endDate;

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

    @Property
    private final ValueEncoder<AccountStatementView> encoder = new ValueEncoder<AccountStatementView>() {
        public String toClient(AccountStatementView value) {
            return String.valueOf(value.getAccountNumber());
        }

        public AccountStatementView toValue(String clientValue) {
            return new AccountStatementView();
        }
    };

    @ActivationRequestParameter
    private String reprint;

    @Inject
    private TransactionDataService transactionDataService;

    void setupRender() {
        evenOdd = new EvenOdd();
        evenOdd.setEven(false);       

        accountView = accountStatementResult.getAccountView();
        asvList = accountStatementResult.getAsvList();
        startDate = accountStatementResult.getStartDate();
        endDate = accountStatementResult.getEndDate();
    }
}
