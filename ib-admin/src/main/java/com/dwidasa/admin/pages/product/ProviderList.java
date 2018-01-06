package com.dwidasa.admin.pages.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.dao.ProviderDao;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.service.ProviderService;
import com.dwidasa.engine.service.VersionService;

@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class ProviderList {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private Provider row;

    @Property
    @Persist
    private String providerCode;
    
    @Property
    @Persist
    private String providerName;
    
    @Property
    @Persist
    private String description;

    @InjectPage
    private ProviderDetail providerDetail;

    @Inject
    private ProviderService providerService;
    
    @Inject
    private ProviderDao providerDao;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        List<String> orders = new ArrayList<String>();
        orders.add("provider_code");

        if (providerCode != null && !providerCode.equals("")) {
            restrictions.add("upper(provider_code) like '%' || ? || '%'");
            values.add(providerCode.toUpperCase());
        }
        
        if (providerName != null && !providerName.equals("")) {
            restrictions.add("upper(provider_name) like '%' || ? || '%'");
            values.add(providerName.toUpperCase());
        }
        
        if (description != null && !description.equals("")) {
        	restrictions.add("upper(description) like '%' || ? || '%'");
            values.add(description.toUpperCase());
        }

        dataSource = new BaseDataSource(Provider.class, Constants.PAGE_SIZE, restrictions, values, null, orders);
    }

    @DiscardAfter
    void onSelectedFromReset() {
        providerCode = null;
        providerName = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        providerDetail.setId(null);
        return providerDetail;
    }
    
    @Inject
    private VersionService versionService;

    
    @DiscardAfter
    void onActionFromDelete(Long id) {
    	Provider tt = new Provider();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, providerService, userId);
    }
    
    @Inject
    private Messages messages;
    
    @Inject
    private SessionManager sessionManager;
    
    public String getStrInquiry(String inquiry) {
    	return messages.get("inquiry-" + inquiry);
    }
    
    
    @DiscardAfter
    void onActionFromUpdate(Long id, boolean active) {
    	System.out.println("Id "+id);
    	System.out.println("Status "+active);
    	Long userId = sessionManager.getLoggedUser().getId();
    	
    	System.out.println("Status Dari DB "+active );
    	providerDao.updateStatusBiller(id, active, new Date(), userId, new Date(), userId);
    }
    
    @DiscardAfter
    void onActionFromUpdateActive(Long id, boolean active) {
    	System.out.println("Id "+id);
    	System.out.println("Status "+active);
    	Long userId = sessionManager.getLoggedUser().getId();
    	
    	System.out.println("Status Dari DB "+active );
    	providerDao.updateStatusBiller(id, active, new Date(), userId, new Date(), userId);
    }
    

}
