package com.dwidasa.ib.pages.transferBatch;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.model.BatchContent;
import com.dwidasa.engine.service.BatchContentService;
import com.dwidasa.engine.service.BatchService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.BaseDataSource;
import com.dwidasa.ib.services.SessionManager;

public class TransferBatchContentList {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private BatchContent row;

    @Persist
    @Property(write = false)
    private String transactionType;

    @Persist
    @Property(write = false)
    private Long id;

    @Property
    private Batch batch;

    @Inject
    private BatchService batchService;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @InjectPage
    private TransferBatchContentDetail transferBatchContentDetail;

    @Inject
    private BatchContentService batchContentService;

    void onActivate(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    Object onActivate() {
    	if (id == null) {
    		return TransferBatchList.class;
    	}
    	//check is id belongs to this user?
    	
    	
    	return null;
    }

    void setupRender() {
        pageSize = Constants.PAGE_SIZE;
        List<String> restrictions = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        if (id != null && !id.equals("")) {
            restrictions.add("m_batch_id =?");
            values.add(id);
        }
        dataSource = new BaseDataSource<BatchContent>(BatchContent.class, Constants.PAGE_SIZE, restrictions, values);
    }

    void onPrepare() {
        batch = batchService.get(id);
    }
    
    private boolean isBackButton;
	
	void onSelectedFromBack() {
		isBackButton = true;
	}
	
	void onSelectedFromAdd() {
		isBackButton = false;
	}
	
	@DiscardAfter
	Object onSuccessFromForm() {
		if (isBackButton) {
			return TransferBatchList.class;	
		} else {
	        transferBatchContentDetail.setBatchId(id);
	        return transferBatchContentDetail;			
		}
	}
	
	@Inject
	private SessionManager sessionManager;

    void onActionFromDelete(Long id) {
    	Long userId = sessionManager.getLoggedCustomerView().getId();
        batchContentService.remove(id, userId);
    }
    
    @InjectPage
    private TransferBatchDetail transferBatchDetail;
    
    Object onEditDetail(Long id) {
    	transferBatchDetail.setId(id);
    	return transferBatchDetail;
    }
}
