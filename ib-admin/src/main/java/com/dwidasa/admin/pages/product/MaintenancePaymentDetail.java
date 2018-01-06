package com.dwidasa.admin.pages.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderProduct;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.engine.service.ProviderProductService;
import com.dwidasa.engine.service.ProviderService;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/15/11
 * Time: 00:37 am
 */
@Import(library = "context:bprks/js/product/MaintenancePaymentDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class MaintenancePaymentDetail {
    @Persist
    @Property
    private String transactionType;

    @InjectComponent
    private Zone productNameZone;

    @Persist
    @Property
    private String productCode;

    @Property
    private String providerCode;

    @Persist
    @Property
    private ProviderProduct providerProduct;

    @Property
    private SelectModel productNameModel;

    @Property
    private SelectModel providerNameModel;

    @Property
    private SelectModel transactionTypeModel;

    @Inject
    private Messages messages;

    @Inject
    private ProviderProductService providerProductService;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private ProviderService providerService;

    @Inject
    private BillerService billerService;

    @Inject
    private BillerProductService billerProductService;

    @Inject
    private TransactionTypeService transactionTypeService;

    @Persist
    @Property
    private Biller biller;

    @Persist
    @Property
    private BillerProduct billerProduct;

    @Persist
    @Property(write = false)
    private Long id;
    
    void onActivate(Long id) {
        this.id = id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    void setupRender() {
    	if (id != null) {
			providerProduct = providerProductService.getWithTransactionType(id);
		 	transactionType = providerProduct.getTransactionType();
		} else {
		 	providerProduct = new ProviderProduct();
		 	transactionType = com.dwidasa.engine.Constants.PLN_PAYMENT_CODE;      
		}
        buildTransactionTypeModel();
        buildProviderNameModel();
        buildBillerProductNameModel(transactionType);
    }

    public Object onValueChangedFromTransactionType(String transactionType) {
        buildBillerProductNameModel(transactionType);
        return productNameZone;
    }

    private void buildTransactionTypeModel() {
    	List<OptionModel> optionModelList = new ArrayList<OptionModel>();
    	optionModelList.add(new OptionModelImpl(messages.get("transfer"), com.dwidasa.engine.Constants.TRANSFER_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("transferOther"), com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentPln"), com.dwidasa.engine.Constants.PLN_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentTelkom"), com.dwidasa.engine.Constants.TELKOM_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentInternet"), com.dwidasa.engine.Constants.INTERNET_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentCellular"), com.dwidasa.engine.Constants.TELCO_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentTv"), com.dwidasa.engine.Constants.ENTERTAINMENT_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentTrain"), com.dwidasa.engine.Constants.TRANSPORTATION_PAYMENT_CODE));
    	transactionTypeModel = new SelectModelImpl(null, optionModelList);
    }
    

    private void buildBillerProductNameModel(String transactionType) {
    	if (transactionType == null || transactionType.equals("")) {
    		productNameModel = new SelectModelImpl(null, new ArrayList<OptionModel>());
    		return;
    	}
    	List<BillerProduct> bpList = billerProductService.getAllByTransactionType(transactionType);
    	List<OptionModel> optionModelList = new ArrayList<OptionModel>();
	    for (BillerProduct bp : bpList) {
	    	optionModelList.add(new OptionModelImpl(bp.getProductName(), bp.getId()));
	    }
	    productNameModel = new SelectModelImpl(null, optionModelList);    	
    }

    private void buildProviderNameModel() {
    	List<Provider> pList = providerService.getAllWithOrder();
    	List<OptionModel> optionModelList = new ArrayList<OptionModel>();
	    for (Provider p : pList) {
	    	optionModelList.add(new OptionModelImpl(p.getProviderName(), p.getId()));
	    }
	    providerNameModel = new SelectModelImpl(null, optionModelList); 
    }


    public String getSubtitle() {
        if (id != null) {
            return messages.get("subtitle-edit");
        }

        return messages.get("subtitle-add");
    }

    public String getAction() {
        if (id != null) {
            return messages.get("edit");
        }

        return messages.get("add");
    }
    
    @Inject
    private VersionService versionService;
    
    public boolean isEdit() {
    	return id != null;
    }
    
    @DiscardAfter
    Object onSelectedFromAdd() {
        Long userId = sessionManager.getLoggedUser().getId();
        
        providerProduct.setUpdated(new Date());
        providerProduct.setUpdatedby(userId);

        if (id == null) {
        	providerProduct.setCreated(new Date());
            providerProduct.setCreatedby(userId);
        }

        versionService.versionedSave(providerProduct, providerProductService);
        return MaintenancePaymentList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return MaintenancePaymentList.class;
    }
}
