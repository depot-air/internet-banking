package com.dwidasa.ib.pages.account;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.view.*;
import com.dwidasa.ib.pages.delima.CashInDelimaReceipt;
import com.dwidasa.ib.pages.delima.CashOutDelimaReceipt;
import com.dwidasa.ib.pages.payment.WaterPaymentReceipt;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.pages.purchase.MncLifeStatusPurchaseConfirm;
import com.dwidasa.ib.pages.purchase.MncLifeStatusPurchaseReceipt;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class TransactionStatusResult {
    @Property(write = false)
    @Persist
    private AccountView accountView;

    @InjectComponent
    private Form form;

    @Property(write = false)
    @Persist
    private Date startDate;

    @Property(write = false)
    @Persist
    private Date endDate;

    @Property(write = false)
    @Persist
    private String status;

    @Property
    private String scrStatus;

    @Property
    private String scrType;
    
    @Property
    private String scrTypeStatus;

    @Inject
    private AccountService accountService;

    @Inject
    private ThreadLocale threadLocale;
    
    @InjectPage
    private MncLifeStatusPurchaseConfirm mncLifeStatusPurchaseConfirm;

    @InjectPage
    private CashInDelimaReceipt cashInDelimaReceipt;

    @InjectPage
    private CashOutDelimaReceipt cashOutDelimaReceipt;

    @Property
    private NumberFormat formatter = NumberFormat.getNumberInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, threadLocale.getLocale());

    @Property
    private EvenOdd evenOdd;

    @Property
    private List<AccountStatementView> asvList;

    @Property
    private AccountStatementView asv;

    @Inject
    private Messages messages;
    
    @Inject
    private TransactionDataService transactionDataService;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Property
    private final ValueEncoder<AccountStatementView> encoder = new ValueEncoder<AccountStatementView>() {
        public String toClient(AccountStatementView value) {
            return String.valueOf(value.getAccountNumber());
        }

        public AccountStatementView toValue(String clientValue) {
            return new AccountStatementView();
        }
    };

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRowStatus() {
        if (asv.getStatus().equals(0)) {
            return messages.get("failed");
        }
        else if (asv.getStatus().equals(1)) {
            return messages.get("succeed");
        }
        else if (asv.getStatus().equals(2)) {
            return messages.get("pending");
        }

        return messages.get("canceled");
    }

    Object onActivate() {
        if (accountView == null) {
            return TransactionStatus.class;
        }
        return null;
    }

    void setupRender() {
        evenOdd = new EvenOdd();
        evenOdd.setEven(true);

        scrStatus = messages.get(status);
        if (accountView.getTransactionType().equals(com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE)) {
            scrType = messages.get("cellular");
        }
        else if (accountView.getTransactionType().equals(com.dwidasa.engine.Constants.TRANSFER_CODE)) {
            scrType = messages.get("transfer");
        }
        else if (accountView.getTransactionType().equals(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE)) {
            scrType = messages.get("otransfer");
        }
        else if(accountView.getTransactionType().equals(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian)){
        	scrType = messages.get("mncLife");
        	scrTypeStatus = messages.get("mncLife");
        }
        else if (accountView.getTransactionType().equals(com.dwidasa.engine.Constants.CASHIN_DELIMA_CODE)) {
            scrType = messages.get("cashIn");
        }
        else if (accountView.getTransactionType().equals(com.dwidasa.engine.Constants.CASHOUT_DELIMA_CODE)) {
            scrType = messages.get("cashOut");
        }
        try {
            asvList = accountService.getTransactionStatus(accountView, status, startDate, endDate);
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }
    
    
    Object onViewDetail(Long id) {
		//System.out.println("HOHJOHOHOHOHOH "+StatusHistory);
		if (id == null) return null;
    	
		TransactionData transactionData = transactionDataService.getByTransactionFk(id, accountView.getAccountNumber());
    	if (transactionData == null) return null;

        transactionId = id;
    	if (transactionData.getClassName().endsWith("MncLifePurchaseView")) {
    		MncLifePurchaseView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), MncLifePurchaseView.class);
    		//mncLifeStatusPurchaseConfirm.setMncLifeStatusConfirm(view);
    		mncLifeStatusPurchaseConfirm.setFromHistory(true);
    		//mncLifeStatusPurchaseConfirm.setFromStatus(true);
	    	return mncLifeStatusPurchaseConfirm;
        } else if (transactionData.getClassName().endsWith("CashInDelimaView")) {
            CashInDelimaView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), CashInDelimaView.class);
            cashInDelimaReceipt.setCashInDelimaView(view);
            cashInDelimaReceipt.setFromHistory(true);
            return cashInDelimaReceipt;
        } else if (transactionData.getClassName().endsWith("CashOutDelimaView")) {
           CashOutDelimaView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), CashOutDelimaView.class);
           cashOutDelimaReceipt.setCashOutDelimaView(view);
           cashOutDelimaReceipt.setFromHistory(true);
           return cashOutDelimaReceipt;
        }
        return null;
    }

    @DiscardAfter
    public Object onSelectedFromBack() {
        return TransactionStatus.class;
    }
}
