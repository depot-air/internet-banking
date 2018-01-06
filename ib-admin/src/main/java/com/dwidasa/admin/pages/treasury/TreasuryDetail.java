package com.dwidasa.admin.pages.treasury;

import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionStage;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.*;
import com.dwidasa.engine.util.EngineUtils;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/11/11
 * Time: 17:22
 */
@Import(library = "context:bprks/js/treasury/TreasuryDetail.js")
@Restricted(groups={Constants.RoleName.TREASURY,Constants.RoleName.SUPERUSER})
public class TreasuryDetail {
    @Persist
    @Property
    private TransactionStage transactionStage;

    @Property
    private Transaction transaction;

    @Property
    private String bankName;

    @Inject
    private TransactionStageService transactionStageService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private TransactionService transactionService;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.admin.Constants.MEDIUM_FORMAT,
            threadLocale.getLocale());

    @Inject
    private UserService userService;

    @Property
    private User user;

    @Property
    private TransferView transferView;

    @Inject
    private TransactionDataService transactionDataService;

    @Property
    private BigDecimal total;

    @Property
    private boolean handled;

    @Inject
    private CustomerDao customerDao;

    @Property
    private Customer customer;

    @Property
    private boolean failedSucceed = false;

    @Property
    private String userName;

    void onActivate(Long id) {
        this.id = id;
    }

    public void setupRender() {
        transactionStage = transactionStageService.get(id);
        transaction = transactionService.get(transactionStage.getTransactionId());
        bankName = cacheManager.getBiller(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE, transaction.getToBankCode()).getBillerName();
        if (!"DELIVERED".equalsIgnoreCase(transactionStage.getStatus())) {
        	user = userService.get(transactionStage.getUpdatedby());
        }
        transferView = (TransferView) EngineUtils.deserialize(transactionDataService.getByTransactionFk(transactionStage.getTransactionId()));
        customer = customerDao.get(transferView.getCustomerId());
        if (transaction.getFee() == null) {
        	transaction.setFee(BigDecimal.ZERO);
        }
        total = transaction.getFee().add(transaction.getTransactionAmount());

        if (user == null){
            userName = "-";
        }
        else {
            userName = user.getUsername();
        }
        if(transferView.getNews() == null) transferView.setNews("-");
        if(transferView.getBranchName() == null) transferView.setBranchName("-");
        if(transferView.getBranchCity() == null) transferView.setBranchCity("-");


        if (transactionStage.getStatus().equalsIgnoreCase("DELIVERED")) {
            handled = false;
        } else if (transactionStage.getStatus().equalsIgnoreCase("HANDLED")) {
            handled = true;
        } else if (transactionStage.getStatus().equalsIgnoreCase("FAILED") || transactionStage.getStatus().equalsIgnoreCase("SUCCEED")) {
            failedSucceed = true;
        }
    }

    public String getStatus() {
        String status = new String();
        if (transactionStage.getStatus().equalsIgnoreCase("DELIVERED")) {
            status = messages.get("DELIVERED");
        } else if (transactionStage.getStatus().equalsIgnoreCase("HANDLED")) {
            status = messages.get("HANDLED");
        } else if (transactionStage.getStatus().equalsIgnoreCase("SUCCEED")) {
            status = messages.get("SUCCEED");
        } else if (transactionStage.getStatus().equalsIgnoreCase("FAILED")) {
            status = messages.get("FAILED");
        }
        return status;
    }

    @DiscardAfter
    Object onSelectedFromHandledBut() {
        transactionStage.setUpdatedby(sessionManager.getLoggedUser().getId());
        transactionStage.setUpdated(new Date());
        transactionStage.setStatus("HANDLED");
        transactionStageService.save(transactionStage);
        return TreasuryList.class;
    }

    @DiscardAfter
    Object onSelectedFromSuccessBut() {
        transactionStage.setUpdatedby(sessionManager.getLoggedUser().getId());
        transactionStage.setUpdated(new Date());
        transactionStage.setStatus("SUCCEED");
        transactionStageService.save(transactionStage);
        return TreasuryList.class;
    }

    @DiscardAfter
    Object onSelectedFromFailedBut() {
        transactionStage.setUpdatedby(sessionManager.getLoggedUser().getId());
        transactionStage.setUpdated(new Date());
        transactionStage.setStatus("FAILED");
        transactionStageService.save(transactionStage);
        return TreasuryList.class;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return TreasuryList.class;
    }

}
