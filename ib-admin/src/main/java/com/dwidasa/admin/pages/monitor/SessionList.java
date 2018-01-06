package com.dwidasa.admin.pages.monitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.admin.view.CustomerSessionView;
import com.dwidasa.engine.model.CustomerSession;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerSessionService;

@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.DAY_ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class SessionList {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private CustomerSessionView row;

    @Property
    @Persist
    private String strSearch;
    
    @Inject
    private CustomerSessionService customerSessionService;
    
    @Inject
    private CacheManager cacheManager;

    @SuppressWarnings("unchecked")
    void setupRender() {
    	List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        
        //get session time out in seconds
        long sessionTimeOut = Long.valueOf(cacheManager.getParameter("SESSION_TIMEOUT").getParameterValue())*60;
    	
        pageSize = Constants.PAGE_SIZE;
        
        if (strSearch != null && !strSearch.trim().equals("")) {
	        restrictions.add("upper(mc.customer_username) like '%' || ? || '%'");
	        values.add(strSearch.toUpperCase());
        }
        
        restrictions.add("mcs.updated > (now()- interval '" + String.valueOf(sessionTimeOut) + "')");
        restrictions.add("mcs.session_id is not null");

        Transformer t = TransformerFactory.getTransformer(CustomerSession.class.getSimpleName());
        dataSource = new BaseDataSource(CustomerSessionView.class, Constants.PAGE_SIZE, restrictions, values, t);
    }

    @DiscardAfter
    void onSelectedFromReset() {
        strSearch = null;
    }
    
    void onSelectedFromDeleteAll() {
    	customerSessionService.removeAll();
    	strSearch = null;
    }
	
	public String getStrPlatform(String deviceId) {
    	if (deviceId == null) return "";
    	if (deviceId.endsWith("10")) return "BlackBerry";
    	if (deviceId.endsWith("11")) return "Android";
    	if (deviceId.endsWith("12")) return "iPhone";
    	return "";
    }
	
	@Inject
    private SessionManager sessionManager;
	
	void onActionFromDelete(Long id) {
		Long userId = sessionManager.getLoggedUser().getId();
		customerSessionService.remove(id, userId);
	}
	
	public boolean isNotEmpty() {
    	if (dataSource == null) return false;
    	if (dataSource.getAvailableRows() == 0) return false;
    	return true;
    }
}
