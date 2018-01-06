package com.dwidasa.admin.pages.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.engine.model.Customer;

@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, Constants.RoleName.DAY_ADMIN, Constants.RoleName.SUPERUSER})
public class CustomerBlockToken {
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
    	if (strSearch == null || strSearch.trim().equals("")) {
    		restrictions.add("id=-1");
    		dataSource = new BaseDataSource(Customer.class, Constants.PAGE_SIZE, restrictions, values);
    		return;
    	}
    	
        pageSize = Constants.PAGE_SIZE;

        restrictions.add("(upper(customer_name) like '%' || ? || '%') or (upper(customer_username) like '%' || ? || '%')");
        values.add(strSearch.toUpperCase());
        values.add(strSearch.toUpperCase());

        dataSource = new BaseDataSource(Customer.class, Constants.PAGE_SIZE, restrictions, values);
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
