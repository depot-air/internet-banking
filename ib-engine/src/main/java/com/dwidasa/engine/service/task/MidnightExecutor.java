package com.dwidasa.engine.service.task;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.MessageMailer;
import com.dwidasa.engine.service.ParameterService;
import com.dwidasa.engine.util.EngineUtils;

@Service("midnightExecutor")
public class MidnightExecutor extends ParentExecutor implements Callable<String>{
	
	@Autowired
    protected MessageMailer mailer;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private ParameterService parameterService;
	
	public MidnightExecutor() {
		super("MIDNI");
	}
	
	@Override
	public String call() throws Exception {
		try {
			String currentDate = EngineUtils.fmtDateYMD.print(System.currentTimeMillis());
			//bila running by scheduler, check apakah tanggal ini sudah di-eksekusi (bila sudah maka tidak usah eksekusi lagi)
			if (getUserId().intValue() == -1) {
				String lastExecute = parameterService.getParameterValueByName("LAST_MIDNIGHT_JOB");
				if (lastExecute.equals(currentDate)) {
					getProgressInfo().clear();
		        	getProgressInfo().add("Midnight Job hari ini sudah dijalankan");
		        	getProgressInfo().add("Processing Error");
					return "false";
				}
			}
			
			String result =  super.call();
			
			parameterService.setParameterValueByName("LAST_MIDNIGHT_JOB", currentDate);
			/*
			//send email
			String emailAddress = cacheManager.getParameter("BATCH_EMAIL_NOTIFICATION").getParameterValue();
			String[] arrEmail = emailAddress.split(";");
			for (String email : arrEmail) {
				mailer.sendMidnightEmail(email, process);
			}
			*/
			return result;
		} catch (Exception e) {
			getProgressInfo().clear();
        	getProgressInfo().add("Error: " + e.getMessage());
        	getProgressInfo().add("Processing Error");
			throw e;
		}
	}
}
