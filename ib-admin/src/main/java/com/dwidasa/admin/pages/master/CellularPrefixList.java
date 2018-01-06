package com.dwidasa.admin.pages.master;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.admin.view.CellularPrefixView;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.CellularPrefixService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/11/11
 * Time: 14:14
 */
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
@Import(library = "context:bprks/js/master/CellularPrefixList.js")
public class CellularPrefixList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private CellularPrefixView row;

    @Property
    @Persist
    private Long billerProductId;
    
    @Property
    @Persist
    private String searchCriteria;
    
    @Property
    @Persist
    private String inputPrefix;

    @InjectPage
    private CellularPrefixDetail cellularPrefixDetail;

    @Inject
    private CellularPrefixService cellularPrefixService;

    @Inject
    private VersionService versionService;

    @Property
    private SelectModel productNameModel;

    @InjectComponent
    private Zone productZone;

    @SuppressWarnings("unchecked")
    void setupRender() {
    	buildTransactionTypeModel();
    	if (transactionType == null || transactionType.equals("")) {
    		transactionType = com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE;
    	}
    	buildBillerProductNameModel(transactionType);
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        List<String> orders = new ArrayList<String>();
        orders.add("prefix");

    	if (searchCriteria == null) {
    		restrictions.add("1=?");
    		values.add(0);
    	} else if ("select".equals(searchCriteria)) {
    		if (billerProductId != null) {
                restrictions.add("m_biller_product_id =?");
                values.add(billerProductId);
            }
    	} else if ("input".equals(searchCriteria)) {
    		restrictions.add("prefix like ?");
            values.add("%" + inputPrefix + "%");
    	}

        Transformer t = TransformerFactory.getTransformer(CellularPrefix.class.getSimpleName());
        dataSource = new BaseDataSource(CellularPrefixView.class, Constants.PAGE_SIZE, restrictions, values, t, orders);
        
    }

	private void buildBillerProductNameModel(String transactionType) {
		List<BillerProduct> bpList = billerProductService.getAllByTransactionType(transactionType);
		List<OptionModel> optionModelList = new ArrayList<OptionModel>();
	    for (BillerProduct bp : bpList) {
	    	optionModelList.add(new OptionModelImpl(bp.getProductName(), bp.getId()));
	    }
	    productNameModel = new SelectModelImpl(null, optionModelList);
	}
		
    @DiscardAfter
    void onSelectedFromReset() {
    	transactionType = null;
        billerProductId = null;
    }

    Object onSelectedFromAdd() {
        cellularPrefixDetail.setId(null);
        cellularPrefixDetail.setTransactionType(transactionType);
        cellularPrefixDetail.setBillerProductId(billerProductId);
        return cellularPrefixDetail;
    }
    
    @Inject
    private SessionManager sessionManager;

    void onActionFromDelete(Long id) {
        CellularPrefix cp = new CellularPrefix();
        cp.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(cp, cellularPrefixService, userId);
    }
    
    @Persist
    @Property
    private String transactionType;
    
    @Property
    private SelectModel transactionTypeModel;   
    
    @Inject
    private Messages messages;
    
    private void buildTransactionTypeModel() {
		List<OptionModel> optionModelList = new ArrayList<OptionModel>();
		optionModelList.add(new OptionModelImpl(messages.get("prepaid"), com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE));
		optionModelList.add(new OptionModelImpl(messages.get("postpaid"), com.dwidasa.engine.Constants.TELCO_PAYMENT_CODE));
		optionModelList.add(new OptionModelImpl(messages.get("telkom"), com.dwidasa.engine.Constants.TELKOM_PAYMENT_CODE));
		transactionTypeModel = new SelectModelImpl(null, optionModelList);
	}
    
    @Inject
    private BillerProductService billerProductService;
    
    Object onValueChangedFromTransactionType(String transactionType) {
    	buildBillerProductNameModel(transactionType);
    	return productZone.getBody();
    }
    
    @Inject
	private JavaScriptSupport javaScriptSupport;
	
	@AfterRender
    public void afterRender() {
    	javaScriptSupport.addScript("new AutoChecker();");
    	javaScriptSupport.addScript(InitializationPriority.LATE, "if ($('inputSearch').checked) { $('inputPrefix').focus(); }");
	}
	
	@InjectComponent
	private Form form;
	
	private boolean isSearchButton;
	
	void onSelectedFromSearch() {
		isSearchButton = true;
	}
	
	void onValidateFromForm() {
		if (isSearchButton && "input".equals(searchCriteria)) {
			if (inputPrefix == null || inputPrefix.trim().length() == 0) {
				form.recordError(messages.get("inputPrefixRequired"));
			}
		}
	}
}
