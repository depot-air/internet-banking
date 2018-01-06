package com.dwidasa.admin.pages.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.service.VersionService;

@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class BillerList {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private Biller row;

    @Property
    @Persist
    private String billerCode;
    
    @Property
    @Persist
    private String billerName;
    
    @Property
    @Persist
    private Long transactionTypeId;
    
    @Property
    private SelectModel transactionTypeModel;

    @InjectPage
    private BillerDetail billerDetail;

    @Inject
    private BillerService billerService;
    
    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (transactionTypeId != null) {
        	restrictions.add("m_transaction_type_id=?");
            values.add(transactionTypeId);
        }
        
        if (billerCode != null && !billerCode.equals("")) {
            restrictions.add("upper(biller_code) like '%' || ? || '%'");
            values.add(billerCode.toUpperCase());
        }
        if (billerName != null && !billerName.equals("")) {
            restrictions.add("upper(biller_name) like '%' || ? || '%'");
            values.add(billerName.toUpperCase());
        }

        dataSource = new BaseDataSource(Biller.class, Constants.PAGE_SIZE, restrictions, values);
        buildTransactionTypeModel();
        
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
        billerCode = null;
        billerName = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        billerDetail.setId(null);
        return billerDetail;
    }
    
    @Inject
    private VersionService versionService;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private BillerDao billerDao;
    
    @DiscardAfter
    void onActionFromDelete(Long id) {
    	Biller tt = new Biller();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, billerService, userId);
    }
    
    
    @DiscardAfter
    void onActionFromUpdate(Long id, boolean active) {
    	System.out.println("Id "+id);
    	System.out.println("Status "+active);
    	Long userId = sessionManager.getLoggedUser().getId();
    	
    	System.out.println("Status Dari DB "+active );
    	billerDao.updateStatusBiller(id, active, new Date(), userId, new Date(), userId);
    }
    
    @DiscardAfter
    void onActionFromUpdateActive(Long id, boolean active) {
    	System.out.println("Id "+id);
    	System.out.println("Status "+active);
    	Long userId = sessionManager.getLoggedUser().getId();
    	
    	System.out.println("Status Dari DB "+active );
    	billerDao.updateStatusBiller(id, active, new Date(), userId, new Date(), userId);
    }
    
    
}
