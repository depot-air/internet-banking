package com.dwidasa.admin.services.scheduler;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;

import com.dwidasa.engine.service.task.QueuedTransactionExecutor;

public class QueuedJob implements StatefulJob {
	private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		QueuedTransactionExecutor queueExecutor = (QueuedTransactionExecutor) ctx.getJobDetail().getJobDataMap().get("executor");
		if (!queueExecutor.isReady()) {
			logger.info("Queued Transaction Job not ready");
			return;
		}
				
		queueExecutor.initProcess(-1L, new Date());
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(queueExecutor);
	}

}
