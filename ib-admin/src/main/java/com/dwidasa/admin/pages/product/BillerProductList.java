package com.dwidasa.admin.pages.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.service.VersionService;

@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class BillerProductList {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private BillerProduct row;

    @Property
    @Persist
    private Long transactionTypeId;
    
    @Property
    @Persist
    private Long billerId;
    
    @Property
    private SelectModel transactionTypeModel;
    
    @Property
    private SelectModel billerModel;

    @InjectPage
    private BillerProductDetail billerProductDetail;

    @Inject
    private BillerProductService billerProductService;
    
    @Inject
    private BillerService billerService;
    
    @InjectComponent
    private Zone billerZone;
    
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

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
      
        if (billerId != null) {
            restrictions.add("m_biller_id = ?");
            values.add(billerId);
        }

        dataSource = new BaseDataSource(BillerProduct.class, Constants.PAGE_SIZE, restrictions, values);
        buildTransactionTypeModel();
        if (transactionTypeId != null) {
        	buildBillerModel(transactionTypeId);
        } else {
	        if (transactionTypeModel.getOptions().size() > 0) {
	        	buildBillerModel((Long) transactionTypeModel.getOptions().get(0).getValue());
	        } else {
	        	buildBillerModel(-1L);
	        }
        }
    }
    
    @Inject
    private TransactionTypeService transactionTypeService;
    
    private void buildTransactionTypeModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<TransactionType> transactionTypeList = transactionTypeService.getAllInBiller();
    	for (TransactionType transactionType : transactionTypeList) {
    		optionList.add(new OptionModelImpl(transactionType.getDescription(), transactionType.getId()));
    	}
    	transactionTypeModel = new SelectModelImpl(null, optionList);
    }

    @DiscardAfter
    void onSelectedFromReset() {
    	transactionTypeId = null;
        billerId = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        billerProductDetail.setId(null);
        return billerProductDetail;
    }
    
    @Inject
    private VersionService versionService;

    @Inject
    private SessionManager sessionManager;
    
    @DiscardAfter
    void onActionFromDelete(Long id) {
    	BillerProduct tt = new BillerProduct();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, billerProductService, userId);
    }

}
