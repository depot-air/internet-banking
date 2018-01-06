package com.dwidasa.admin.pages.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.service.VersionService;

@Import(library = "context:bprks/js/product/BillerDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class BillerDetail {
	@Inject
	private JavaScriptSupport javaScriptSupport;
	
	@Inject
	private AlertManager alertManager;

    @Property
    private Biller biller;

    @Inject
    private BillerService billerService;

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
    private SelectModel transactionTypeModel;

    void setupRender(){
    	javaScriptSupport.addScript(String.format("new DisabledComponent();"));
        buildTransactionTypeModel();
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
            biller = billerService.get(id);
        }
        else {
            biller = new Biller();
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
        if (biller.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            biller.setCreated(new Date());
            biller.setCreatedby(userId);
            biller.setUpdated(new Date());
            biller.setUpdatedby(userId);
        }

        versionService.versionedSave(biller, billerService);
        alertManager.alert(Duration.TRANSIENT, Severity.INFO, "Data Sudah Disimpan");
        return BillerList.class;
        
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return BillerList.class;
    }

}
