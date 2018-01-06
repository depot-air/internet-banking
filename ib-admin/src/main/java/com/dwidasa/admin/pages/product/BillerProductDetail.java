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
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.service.VersionService;

@Import(library = "context:bprks/js/product/BillerProductDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class BillerProductDetail {
	@Inject
	private JavaScriptSupport javaScriptSupport;

    @Property
    private BillerProduct billerProduct;

    @Inject
    private BillerProductService billerProductService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private TransactionTypeService transactionTypeService;
    
    @Property
    private Long transactionTypeId;
    
    @Property
    private String transactionTypeName;
    
    @Property
    private SelectModel transactionTypeModel;
    
    @InjectComponent
    private Zone billerZone;
    
    @Property
    private SelectModel billerModel;
    
    @Inject
    private BillerService billerService;
    
    @Property
    private Long billerId;
    
    public Object onValueChanged(Long transactionTypeId) 
    {
       buildBillerModel(transactionTypeId);
       return billerZone.getBody();
    }
    
    private void buildBillerModel(Long transactionTypeId) {
    	List<OptionModel> modelList = new ArrayList<OptionModel>();	
        List<Biller> billerList = billerService.getByTransactionTypeId(transactionTypeId);
        for (Biller biller : billerList) {
     	   modelList.add(new OptionModelImpl(biller.getBillerName(), biller.getId()));
        }
        billerModel = new SelectModelImpl(null, modelList);
    }

    void setupRender(){
    	javaScriptSupport.addScript(String.format("new DisabledComponent();"));
        buildTransactionTypeModel();
        if (transactionTypeModel.getOptions().size() > 0) {
        	buildBillerModel((Long) transactionTypeModel.getOptions().get(0).getValue());
        } else {
        	buildBillerModel(-1L);
        }
    }

    private void buildTransactionTypeModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<TransactionType> transactionTypeList = transactionTypeService.getAllSortDescription();
    	for (TransactionType transactionType : transactionTypeList) {
    		optionList.add(new OptionModelImpl(transactionType.getTransactionType() + " - " +  transactionType.getDescription(), transactionType.getId()));
    	}
    	transactionTypeModel = new SelectModelImpl(null, optionList);
	}

	void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            billerProduct = billerProductService.get(id);
            billerId = billerProduct.getBillerId();
            Biller biller = billerService.get(billerId);
            transactionTypeId = biller.getTransactionTypeId();
            buildBillerModel(transactionTypeId);
        }
        else {
            billerProduct = new BillerProduct();
        }
    }

    public void setId(Long id) {
        this.id = id;
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

    @DiscardAfter
    Object onSelectedFromAdd() {
        if (billerProduct.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            billerProduct.setCreated(new Date());
            billerProduct.setCreatedby(userId);
            billerProduct.setUpdated(new Date());
            billerProduct.setUpdatedby(userId);
        }
        billerProduct.setBillerId(billerId);
        versionService.versionedSave(billerProduct, billerProductService);
        return BillerProductList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return BillerProductList.class;
    }

}
