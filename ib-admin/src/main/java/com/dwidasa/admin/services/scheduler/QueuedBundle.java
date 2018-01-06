package com.dwidasa.admin.services.scheduler;

import java.text.ParseException;

import org.chenillekit.quartz.services.JobSchedulingBundle;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.task.QueuedTransactionExecutor;

public class QueuedBundle implements JobSchedulingBundle {
	private Trigger trigger;
	private JobDetail jobDetail;
	
	public QueuedBundle(CacheManager cacheManager, QueuedTransactionExecutor executor) {
		try {
			String cronExpression = cacheManager.getParameter("CRON_EXECUTE_TRX").getParameterValue();
			trigger = new CronTrigger("QUEUED_TR", "QUEUED_TRG", cronExpression);
			jobDetail = new JobDetail("QueuedJob", null, QueuedJob.class);
			JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("executor", executor);
			jobDetail.setJobDataMap(jobDataMap);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public JobDetail getJobDetail() {
		return jobDetail;
	}

	@Override
	public String getSchedulerId() {
		return null;
	}

	@Override
	public Trigger getTrigger() {
		return trigger;
	}

}
