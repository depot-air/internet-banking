package com.dwidasa.engine.service.task;

import java.util.Date;

import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class RefreshCacheTask implements Executable {

	@Override
	public void execute(Date processingDate, Long userId) throws Exception {

//		Client client = (Client) ServiceLocator.getService("client");
//		CacheManager cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
//        cacheManager.initialiazeCache();
//
//        String appIp = cacheManager.getParameter("APP_IP").getParameterValue();
//        WebResource webResource = client.resource("http://" + appIp + "/ib/rest/server/reloadCache");
//        webResource.get(String.class);
	}

	@Override
	public void cleanup(Date processingDate) {
		// TODO Auto-generated method stub
		
	}

}
