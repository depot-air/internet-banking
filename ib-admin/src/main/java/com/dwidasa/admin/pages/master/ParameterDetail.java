package com.dwidasa.admin.pages.master;

import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ParameterService;
import com.dwidasa.engine.service.VersionService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/18/11
 * Time: 10:49 AM
 */
@Import(library = "context:bprks/js/master/ParameterDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class ParameterDetail {
    @Property
    private Parameter parameter;

    @Inject
    private ParameterService parameterService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            parameter = parameterService.get(id);
        }
        else {
            parameter = new Parameter();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubtitle() {
        if (id != null) {
            return messages.get("subtitle-edit");
        }

        return messages.get("subtitle-add");
    }

    public String getAction() {
        if (id != null) {
            return messages.get("edit");
        }

        return messages.get("add");
    }
    
    @Inject
    private VersionService versionService;
    
    @Inject
    private CacheManager cacheManager;
    
    @Inject
    private Client client;

    @DiscardAfter
    Object onSelectedFromAdd() {
        if (parameter.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            parameter.setCreated(new Date());
            parameter.setCreatedby(userId);
            parameter.setUpdated(new Date());
            parameter.setUpdatedby(userId);
        }

        versionService.versionedSave(parameter, parameterService);
        
        cacheManager.refreshCacheParameter();
        String appIp = cacheManager.getParameter("APP_IP").getParameterValue();
        WebResource webResource = client.resource("http://" + appIp + "/ib/rest/server/reloadCache");
        webResource.get(String.class);
        		
        return ParameterList.class;
    }
    
    

    @DiscardAfter
    Object onSelectedFromBack() {
        return ParameterList.class;
    }
    
    @Inject
	private JavaScriptSupport javaScriptSupport;
    
    void setupRender(){
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
    }
}
