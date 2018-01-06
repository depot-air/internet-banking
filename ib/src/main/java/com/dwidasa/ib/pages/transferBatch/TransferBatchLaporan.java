package com.dwidasa.ib.pages.transferBatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.ActivityCustomer;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.service.transform.Transformer;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.BaseDataSource;

public class TransferBatchLaporan {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private Customer row;

    @Property
    @Persist
    private String strSearch;

    @SuppressWarnings("unchecked")
    void setupRender() {
    	List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        pageSize = Constants.PAGE_SIZE;
		/*
		Transformer t = TransformerFactory.getTransformer(TransferBatch);
		if (startDate != null && endDate != null) {
			if (transactionType != null && transactionType.trim().length() > 0) {
				restrictions.add("tac.activity_type=?");
				values.add(transactionType);
			}
			if (strUsername != null && !strUsername.trim().equals("")) {
				restrictions.add("((upper(mc.customer_name) like '%' || ? || '%') or (upper(mc.customer_username) like '%' || ? || '%'))");
		        values.add(strUsername.toUpperCase());
		        values.add(strUsername.toUpperCase());
	    	}
	    	
			restrictions.add("tac.created >= ?");
			values.add(DateUtils.generateStart(startDate));
			restrictions.add("tac.created < ?");
			values.add(DateUtils.generateEnd(endDate));
		    dataSource = new BaseDataSource(ActivityCustomer.class, Constants.PAGE_SIZE, restrictions, values, t);
		} else {
			restrictions.add("tac.id=-1");
			dataSource = new BaseDataSource(ActivityCustomer.class, Constants.PAGE_SIZE, restrictions, values, t);
		}
		
		//set default value for date
		if (startDate == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -30);
			startDate = cal.getTime(); 
		}
		if (endDate == null) {
			endDate = new Date();
		}				
		
    	List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
    	if (strSearch == null || strSearch.trim().equals("")) {
    		restrictions.add("id=-1");
//    		dataSource = new BaseDataSource(Customer.class, Constants.PAGE_SIZE, restrictions, values);
    		return;
    	}
    	
        pageSize = Constants.PAGE_SIZE;
        
//m_customer_id, card_number, account_number, batch_name, batch_description, transfer_type, transaction_date, value_date, reference_number, status
        restrictions.add("(upper(batch_name) like '%' || ? || '%')");
        values.add(strSearch.toUpperCase());

        dataSource = new BaseDataSource(TransferBatch.class, Constants.PAGE_SIZE, restrictions, values);\
        */
    }

    @DiscardAfter
    void onSelectedFromReset() {
        strSearch = null;
    }
    
    @Inject
    private Messages messages;
    
    public String getStrStatus(int status) {
    	if (status == 1) {
    		return messages.get("aktif");	
    	} else {
    		return messages.get("tidakAktif");
    	}	
    }
}
