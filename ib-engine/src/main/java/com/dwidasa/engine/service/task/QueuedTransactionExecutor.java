package com.dwidasa.engine.service.task;

import java.util.Date;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.MessageMailer;
import com.dwidasa.engine.service.ParameterService;
import com.dwidasa.engine.util.DateUtils;
import com.sun.jersey.api.client.Client;

@Service("queuedTransactionExecutor")
public class QueuedTransactionExecutor extends ParentExecutor implements Callable<String>{
    private static Logger logger = Logger.getLogger( QueuedTransactionExecutor.class );
	@Autowired
	private Client client;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private ParameterService parameterService;
		
	@Autowired
    protected MessageMailer mailer;
	
	public QueuedTransactionExecutor() {
		super("QUEUE");
	}
	
	@Override
	public String call() throws Exception {
		try {
			String currentDate = DateUtils.getYYYYMMDD(new Date());
			logger.info("QueuedTransactionExecutor currentDate=" + currentDate);
			//bila running by scheduler, check apakah tanggal ini sudah di-eksekusi (bila sudah maka tidak usah eksekusi lagi)
			if (getUserId().intValue() == -1) {
				String lastExecute = cacheManager.getParameter("LAST_EXECUTE_QUEUE").getParameterValue();
				if (lastExecute.equals(currentDate)) {
					getProgressInfo().clear();
		        	getProgressInfo().add("Transaksi terjadwal hari ini sudah dijalankan");
		        	getProgressInfo().add("Processing Error");
					return "false";
				}
			}
			logger.info("QueuedTransactionExecutor getUserId().intValue()=" + getUserId().intValue());
			
			//check apakah ada koneksi ke ITM
			String result;
/*			
			try {
				String ibUrl = cacheManager.getParameter("SERVER_IB_URL").getParameterValue();
		        WebResource webResource = client.resource(ibUrl + "/rest/server/isConnected");
		        result = webResource.get(String.class);
		        if (!"{\"status\":1}".equals(result)) {
		        	getProgressInfo().clear();
		        	getProgressInfo().add("Koneksi ke Core Banking sedang terputus");
		        	getProgressInfo().add("Processing Error");
		        	return "false";
		        }
			} catch (Exception ex) {
				
			}
*/			
			result =  super.call();
			logger.info("QueuedTransactionExecutor result=" + result);
			parameterService.setParameterValueByName("LAST_EXECUTE_QUEUE", currentDate);
/*			
			//send email
			String emailAddress = cacheManager.getParameter("BATCH_EMAIL_NOTIFICATION").getParameterValue();
			String[] arrEmail = emailAddress.split(";");
			for (String email : arrEmail) {
				mailer.sendQueuedTransactionEmail(email, process);
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
