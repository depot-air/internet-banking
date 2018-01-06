package com.dwidasa.engine.service.task;

import java.util.Date;

import com.dwidasa.engine.dao.HousekeepingDao;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;

public class HousekeepingTask implements Executable {
	private final CacheManager cacheManager;
	private final HousekeepingDao housekeepingDao;
	
	public HousekeepingTask() {
		cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
		housekeepingDao = (HousekeepingDao) ServiceLocator.getService("housekeepingDao");
	}

	@Override
	public void execute(Date processingDate, Long userId) throws Exception {
		int days = Integer.parseInt(cacheManager.getParameter("HOUSEKEEPING_DAYS").getParameterValue());
		
		//delete data in database 
		//housekeepingDao.runHousekeeping(days);

	}

	@Override
	public void cleanup(Date processingDate) {
		// do nothing
		
	}

}
