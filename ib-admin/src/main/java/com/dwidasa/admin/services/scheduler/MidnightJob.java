package com.dwidasa.admin.services.scheduler;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;

import com.dwidasa.engine.service.task.MidnightExecutor;

public class MidnightJob  implements StatefulJob {
	private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		MidnightExecutor midnightExecutor = (MidnightExecutor) ctx.getJobDetail().getJobDataMap().get("executor");
		if (!midnightExecutor.isReady()) {
			logger.info("Midnight Job not ready");
			return;
		}
				
		midnightExecutor.initProcess(-1L, new Date());
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(midnightExecutor);
	}
}
