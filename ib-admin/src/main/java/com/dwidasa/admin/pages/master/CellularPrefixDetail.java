package com.dwidasa.admin.pages.master;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.GenericSelectModelFactory;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CellularPrefixService;
import com.dwidasa.engine.service.VersionService;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/11/11
 * Time: 14:14
 */
@Import(library = "context:bprks/js/master/CellularPrefixDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class CellularPrefixDetail {
    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Persist
    @Property
    private CellularPrefix cellularPrefix;

    @Inject
    private CellularPrefixService cellularPrefixService;

    @Inject
    private VersionService versionService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;

    @Persist
    @Property(write = false)
    private Long billerProductId;
    
    public void setBillerProductId(Long billerProductId) {
    	this.billerProductId = billerProductId;
    }
    
    @Inject
    private CacheManager cacheManager;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel productNameModel;

    void setupRender() {
    	buildTransactionTypeModel();
    	if (transactionType == null || transactionType.equals("")) {
    		transactionType = com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE;
    	}
    	buildBillerProductNameModel(transactionType);
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));

    }

    private void buildBillerProductNameModel(String transactionType) {
		List<BillerProduct> bpList = billerProductService.getAllByTransactionType(transactionType);
		List<OptionModel> optionModelList = new ArrayList<OptionModel>();
	    for (BillerProduct bp : bpList) {
	    	optionModelList.add(new OptionModelImpl(bp.getProductName(), bp.getId()));
	    }
	    productNameModel = new SelectModelImpl(null, optionModelList);
	}

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
    		cellularPrefix = cellularPrefixService.getWithTransactionType(id);
        } else {
            cellularPrefix = new CellularPrefix();
            cellularPrefix.setBillerProductId(billerProductId);
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

    @DiscardAfter
    Object onSelectedFromAdd() {
    	Long userId = sessionManager.getLoggedUser().getId();
    	cellularPrefix.setUpdated(new Date());
        cellularPrefix.setUpdatedby(userId);
        if (cellularPrefix.getId() == null) {
            cellularPrefix.setCreated(new Date());
            cellularPrefix.setCreatedby(userId);
        }
        versionService.versionedSave(cellularPrefix, cellularPrefixService);
        return CellularPrefixList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return CellularPrefixList.class;
    }
    
    @Persist
    @Property(write = false)
    private String transactionType;
    
    public void setTransactionType(String transactionType) {
    	this.transactionType = transactionType;
    }
    
    @Property
    private SelectModel transactionTypeModel;   
    
    private void buildTransactionTypeModel() {
		List<OptionModel> optionModelList = new ArrayList<OptionModel>();
		optionModelList.add(new OptionModelImpl(messages.get("prepaid"), com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE));
		optionModelList.add(new OptionModelImpl(messages.get("postpaid"), com.dwidasa.engine.Constants.TELCO_PAYMENT_CODE));
		optionModelList.add(new OptionModelImpl(messages.get("telkom"), com.dwidasa.engine.Constants.TELKOM_PAYMENT_CODE));
		transactionTypeModel = new SelectModelImpl(null, optionModelList);
	}
    
    @Inject
    private BillerProductService billerProductService;
    
    @InjectComponent
    private Zone productZone;
    
    Object onValueChangedFromTransactionType(String transactionType) {
    	buildBillerProductNameModel(transactionType);
    	return productZone.getBody();
    }
}
