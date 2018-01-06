package com.dwidasa.admin.pages.system;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.engine.service.CacheManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class ReloadCache {
	@Inject
	private CacheManager cacheManager;
	
	@Inject
    private Client client;
	
	@Inject
	private Request request;
	
	@Inject
	private Messages messages;
	
	void onValidateFromForm() {
		cacheManager.refreshCacheParameter();
        String appIp = cacheManager.getParameter("APP_IP").getParameterValue();
        WebResource webResource = client.resource("http://" + appIp + "/ib/rest/server/reloadCache");
        webResource.get(String.class);
	}
	
	Object onSuccess() {
		request.getSession(false).setAttribute("msgReloadCache", messages.get("reloadSuccess"));
		return null;
	}
	
}
