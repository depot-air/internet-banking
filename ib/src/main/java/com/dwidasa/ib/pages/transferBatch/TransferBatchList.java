package com.dwidasa.ib.pages.transferBatch;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.service.BatchService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.BaseDataSource;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;

public class TransferBatchList{
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private Batch row;

    @Property
    @Persist
    private String billerCode;
    
    @Property
    @Persist
    private String billerName;
    
    @InjectPage
    private TransferBatchDetail transferBatchDetail;
    
    @Inject
    private SessionManager sessionManager;

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();

    	restrictions.add("m_customer_id=?");
        values.add(sessionManager.getLoggedCustomerView().getId());
        
        List<String> orders = new ArrayList<String>();
        orders.add("batch_name");
        dataSource = new BaseDataSource<Batch>(Batch.class, Constants.PAGE_SIZE, restrictions, values, null, orders);
    }
    

    @DiscardAfter
    Object onSuccessFromForm() {
        transferBatchDetail.setId(null);
        return transferBatchDetail;
    }
    
    @Inject
    private BatchService batchService;
    
    @DiscardAfter
    void onActionFromDelete(Long id) {
    	Long userId = sessionManager.getLoggedCustomerView().getId();
        batchService.remove(id, userId);
    }
    
    @InjectPage
    private TransferBatchContentList transferBatchContentList;
    
    Object onViewDetail(Long id) {
    	transferBatchContentList.setId(id);
    	return transferBatchContentList;
    }
}