package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 23/10/12
 * Time: 14:55
 * To change this template use File | Settings | File Templates.
 */
public class TransferATMBStatusInput
{
    @Property
    @Persist
    private TransferView transferView;

    @InjectComponent
    private Form form;

    @Property
    private String chooseValue;

    @Property
    @Persist
    private String customerReference1;

    @Property
    private String customerReference2;

    @Property
    private boolean saveBoxValue;

    @Property
    private SelectModel fromListModel;

    @Inject
    private TransferService transferService;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private Messages message;

    @Inject
    private AccountService accountService;

    @Property
    @InjectPage
    private TransferATMBStatusReceipt transferATMBStatusReceipt;

    public void setTransferInputsView(TransferView transferView) {
        this.transferView = transferView;

    }

    void setupRender() {
        chooseValue = "fromId";
        /*
        setTokenType();

        if (transferView == null) {
            transferView = new TransferView();

            transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_POSTING);
        }
        */
        buildFromListModel();
    }

    private void buildFromListModel() {
        List<CustomerRegister> customerRegisters = transferService.getRegisters(
                sessionManager.getLoggedCustomerView().getId(),
                com.dwidasa.engine.Constants.ATMB.TT_POSTING,
                transferView.getBillerCode());
        fromListModel = genericSelectModelFactory.create(customerRegisters);
    }

    void onValidateFromForm()
    {
        if (chooseValue.equalsIgnoreCase("fromId"))
        {
           if(customerReference1 == null)
               form.recordError(message.get("customerReference1-requiredIf-message"));
            else
           {
               transferView.setInputType("M");
               transferView.setCustomerReference(customerReference1);
           }
        }
        else
        if(chooseValue.equalsIgnoreCase("fromList"))
        {
            if(customerReference2 == null)
                form.recordError(message.get("customerReference2-requiredIf-message"));
            else
            {
                transferView.setInputType("L");
                transferView.setCustomerReference(customerReference2);
            }
        }
        if(form.getHasErrors())
            return;

        try
        {
            Transaction trans = accountService.getTransferViewForTransferAMTBStatus(transferView);
            if(trans == null)
            {
                form.recordError(message.get("noData"));
                return;
            }
            setTransferAtmbInputData();
            transferService.inquiryATMB(transferView);
            transferView.setReferenceNumber(trans.getReferenceNumber());
            transferView.setValueDate(trans.getValueDate());
            transferView.setTransactionDate(trans.getTransactionDate());
            transferView.setCardNumber(trans.getCardNumber());
            transferView.setTerminalIdView(trans.getTerminalId());
            transferView.setAmount(trans.getTransactionAmount());
            transferView.setFee(trans.getFee());
            transferView.setTransactionStatus(trans.getStatus());
            transferView.setCustomerReference(trans.getCustomerReference());
            transferView.setBillerCode(trans.getToBankCode());

            //transferView.setTransactionStatus(message.get(transferView.getTransactionStatus().toLowerCase()).toUpperCase());
            if(transferView.getSave())
            {
                transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_POSTING);
                transferService.register(transferView.transform());
            }
        }
        catch (BusinessException ex)
        {
            form.recordError(ex.getFullMessage());
        }

    }

    private void setTransferAtmbInputData()
    {
        transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_INQUIRY);
        transferView.setTransactionDate(new Date());
        transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferView.setSenderName(sessionManager.getLoggedCustomerView().getName());
        transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        transferView.setProviderCode(com.dwidasa.engine.Constants.ATMB.PROVIDER_CODE);
        transferView.setMerchantType(sessionManager.getDefaultMerchantType());
        transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setSave(saveBoxValue);
        transferView.setToAccountType("00");
    }



    Object onParseClientFromCustomerReference1(String input)
    {
        if(chooseValue.equalsIgnoreCase("fromList"))
            return "";
        return input;
    }

    @DiscardAfter
    public Object onSelectedFromCancel() {
        return TransferATMBStatus.class;
    }

    @DiscardAfter
    public Object onSuccess()
    {
        transferATMBStatusReceipt.setTransferInputsView(transferView);
        return transferATMBStatusReceipt;
    }





}
