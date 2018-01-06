package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/11/11
 * Time: 00:43 am
 */
public class AccountStatusReceipt {

    @Property
    private String nowDate;
    @Persist
    private ResultView resultView;
    @Property
    private String status;
    @Inject
    private Messages messages;
    @Property
    private EvenOdd evenOdd;
    @Property
    private CustomerAccount customerAccount;
    @Persist
    private List<String> accountNumber;
    @Inject
    private AccountService accountService;
    @Inject
    private SessionManager sessionManager;
    @Property
    private int index;
    @Property
    private String accountNumberValue;

    private void setTransactionViewDate() {
        Date date = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("dd MMMM yyyy / hh:mm:ss");
        nowDate = sdate.format(date);
        nowDate = nowDate + " WIB";
    }

    void setupRender() {

        if(evenOdd == null){
            evenOdd = new EvenOdd();
        }
        if (resultView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        setTransactionViewDate();
    }

    public ResultView getResultView() {
        return resultView;
    }

    public void setResultView(ResultView resultView) {
        this.resultView = resultView;
    }

    public List<String> getAccountNumber() {
        return accountNumber;
    }

    @DiscardAfter
    Object onSelectedFromBack(){
           return AccountStatusInput.class;
    }

    public void setAccountNumber(List<String> accountNumber) {
        this.accountNumber = accountNumber;
    }
}
