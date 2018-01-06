package com.dwidasa.ib.components;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 21/07/11
 * Time: 10:32
 */
public class Payment {
    @Parameter
    @Property
    private String transactionType;
    
    @Parameter
    @Property
    private String billerCode;

    @Parameter
    @Property
    private String productCode;

    @Parameter
    @Property
    private String providerCode;
    
    @Parameter
    @Property
    private String chooseValue;
    
    @Parameter
    @Property
    private boolean saveBoxValue;
    
    @Parameter
    @Property
    private String customerReference1;
    
    @Parameter
    @Property
    private String customerReference2;
    
    @Property
    private SelectModel providerModel;
    
    @Property
    private SelectModel customerReferenceModel;
    
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;
    
    @Inject
    private Messages messages;
    
    @Property
    private String fromIdMessage;
    
    @Property
    private String providerMessage;
    
    @Inject
    private CacheManager cacheManager;
    
    @Inject
    private PaymentService paymentService;
    
    @Inject
    private SessionManager sessionManager;
    
    void setupRender() {
        setupLabel();
        chooseValue = "fromId";
        buildProviderModel();
        buildCustomerReferenceModel();
    }

    private void buildCustomerReferenceModel() {
        customerReferenceModel = genericSelectModelFactory.create
                (paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(), transactionType, billerCode));
    }

    private void setupLabel() {
        if (transactionType.equalsIgnoreCase(Constants.PLN_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdPln");
            providerMessage = messages.get("billerPln");
        } else if (transactionType.equalsIgnoreCase(Constants.TELKOM_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdTelkom");
            providerMessage = messages.get("billerTelkom");
        } else if (transactionType.equalsIgnoreCase(Constants.INTERNET_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdInternet");
            providerMessage = messages.get("billerInternet");
        } else if (transactionType.equalsIgnoreCase(Constants.CC_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdCredit");
            providerMessage = messages.get("billerCredit");
        } else if (transactionType.equalsIgnoreCase(Constants.TELCO_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdHandphone");
            providerMessage = messages.get("billerHandphone");
        } else if (transactionType.equalsIgnoreCase(Constants.ENTERTAINMENT_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdTv");
            providerMessage = messages.get("billerTv");
        } else if (transactionType.equalsIgnoreCase(Constants.WATER.TRANSACTION_TYPE.POSTING)) {
            fromIdMessage = messages.get("fromIdWater");
            providerMessage = messages.get("billerWater");
        } else if (transactionType.equalsIgnoreCase(Constants.MULTIFINANCE_PAYMENT_CODE)) {
            fromIdMessage = messages.get("fromIdLoan");
            providerMessage = messages.get("billerLoan");
        }
    }

    private void buildProviderModel() {
        providerModel = genericSelectModelFactory.create(
                cacheManager.getProviders(transactionType, billerCode, productCode));
        if (providerModel.getOptions().size() > 0) {
            providerCode = providerModel.getOptions().get(0).getValue().toString();
        }
    }
}
