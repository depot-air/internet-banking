package com.dwidasa.engine.service.task;

import java.util.Date;

import org.apache.log4j.Logger;

import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class QueuedTransactionTask implements Executable {
    private static Logger logger = Logger.getLogger( QueuedTransactionTask.class );
    private final CacheManager cacheManager;
    private final Client client;
    
    public QueuedTransactionTask() {
    	cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
    	client = (Client) ServiceLocator.getService("client");
    }

    public void execute(Date processingDate, Long userId) throws Exception {    	
        String ibUrl = cacheManager.getParameter("SERVER_IB_URL").getParameterValue();
        logger.info("QueuedTransactionTask ibUrl=" + ibUrl);
        WebResource webResource = client.resource(ibUrl + "/rest/server/executeTransactionQueue");
        webResource.get(String.class);
    }
	public void cleanup(Date processingDate) {
    }
}
