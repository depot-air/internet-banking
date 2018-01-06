package com.dwidasa.admin.services.scheduler;

import java.text.ParseException;

import org.chenillekit.quartz.services.JobSchedulingBundle;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.task.MidnightExecutor;

public class MidnightBundle  implements JobSchedulingBundle {
	private Trigger trigger;
	private JobDetail jobDetail;

	public MidnightBundle(CacheManager cacheManager, MidnightExecutor executor) {
		try {
			String cronExpression = cacheManager.getParameter("CRON_MIDNIGHT").getParameterValue();
			trigger = new CronTrigger("MIDNIGHT_TR", "MIDNIGHT_TRG", cronExpression);
			jobDetail = new JobDetail("MIDNIGHT_JOB", MidnightJob.class);
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
