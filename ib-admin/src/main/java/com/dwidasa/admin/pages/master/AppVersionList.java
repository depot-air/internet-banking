package com.dwidasa.admin.pages.master;

import java.util.ArrayList;
import java.util.List;

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
import com.dwidasa.engine.model.AppVersion;
import com.dwidasa.engine.service.AppVersionService;

@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class AppVersionList {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private AppVersion row;

    @Persist
	@Property 
	private String platform;
    
    @InjectPage
    private AppVersionDetail appVersionDetail;

    @Inject
    private AppVersionService appVersionService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (platform != null && !platform.equals("")) {
            restrictions.add("device_type = ?");
            values.add(platform);
        } 

        dataSource = new BaseDataSource(AppVersion.class, Constants.PAGE_SIZE, restrictions, values);
    }

    void onSelectedFromReset() {
        platform = null;
    }
    

    Object onSelectedFromAdd() {
    	appVersionDetail.setId(null);
        return appVersionDetail;
    }
    
    @Inject
    private SessionManager sessionManager;

    void onActionFromDelete(Long id) {
    	Long userId = sessionManager.getLoggedUser().getId();
        appVersionService.remove(id, userId);
    }
    
    @Inject
    private Messages messages;
    
    public String getStrMandatory(int mandatory) {
    	if (mandatory == 1) return messages.get("yes");
    	return messages.get("no");
    }
    
    public String getStrPlatform(String deviceType) {
    	if ("10".equals(deviceType)) return "BlackBerry";
    	if ("11".equals(deviceType)) return "Android";
    	if ("12".equals(deviceType)) return "iPhone";
    	return "Unknown";
    }

}
