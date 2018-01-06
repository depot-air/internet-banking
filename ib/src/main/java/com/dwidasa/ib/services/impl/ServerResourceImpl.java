package com.dwidasa.ib.services.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.ServerResource;

@PublicPage
public class ServerResourceImpl implements ServerResource {
    private static Logger logger = Logger.getLogger( ServerResourceImpl.class );
	@Inject
	private CacheManager cacheManager;
	
	@Inject
    private TransactionService transactionService;

	public String reloadCache(HttpServletRequest request) {
		String ip = cacheManager.getParameter("ADMIN_IP").getParameterValue();
		if (request.getRemoteAddr().equals(ip)) {
			cacheManager.initialiazeCache();
		}
		return Constants.OK;
	}

    public String ping(HttpServletRequest request) {
        return Constants.OK;
    }

    public String ping2(HttpServletRequest request) {
        return Constants.OK;
    }

	public String executeTransactionQueue(HttpServletRequest request) {
		String ip = cacheManager.getParameter("ADMIN_IP").getParameterValue();
		if (request.getRemoteAddr().equals(ip)) {
			try {
				transactionService.executeTransactionQueue(new Date());
			} catch (BusinessException e) {
				logger.info("Error ketika transfer on queue, " + e.getFullMessage());
			} catch (Exception e) {
				logger.info("Exeption " + e.getMessage());
			}
		}
		return Constants.OK;
	}

}
