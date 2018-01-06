package com.dwidasa.engine.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.ActivityCustomerDao;
import com.dwidasa.engine.model.ActivityCustomer;
import com.dwidasa.engine.service.ActivityCustomerService;
import com.dwidasa.engine.service.TransactionQueueService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.engine.service.task.ExecutorPool;
import com.dwidasa.engine.util.DateUtils;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/23/11
 * Time: 8:36 PM
 */
@Service("activityCustomerService")
public class ActivityCustomerServiceImpl extends GenericServiceImpl<ActivityCustomer, Long> implements ActivityCustomerService {
    private static Logger logger = Logger.getLogger( ActivityCustomerServiceImpl.class );
	private ActivityCustomerDao activityCustomerDao;

	@Autowired
	private TransactionQueueService queueService;

	@Autowired
	private TransferService transferService;

	@Autowired
	private ExecutorPool executorPool;
	
    @Autowired
    public ActivityCustomerServiceImpl(ActivityCustomerDao activityCustomerDao) {
        super(activityCustomerDao);
        this.activityCustomerDao = activityCustomerDao;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public InputStream getCsvStream(Date startDate, Date endDate, String transactionType) {
		//get row count
		List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
		if (transactionType != null && transactionType.trim().length() > 0) {
			restrictions.add("activity_type=?");
			values.add(transactionType);
		}
		restrictions.add("created >= ?");
		values.add(DateUtils.generateStart(startDate));
		restrictions.add("created < ?");
		values.add(DateUtils.generateEnd(endDate));
		int rowCount = activityCustomerDao.getRowCount(restrictions, values.toArray());		
				
		//query one thousand record at a time (don't query all record)
//		final int QUERY_LIMIT = 1000;
		//max. byte array size (5 MB)
		final int SIZE_LIMIT = 5000000; 
		
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream() ;
		PrintWriter pw = new PrintWriter(byteArray);
		int i = 0;
		DateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//write header
		StringBuffer sb = new StringBuffer();
		sb.append("\"Value Date\",");
		sb.append("\"From Account Number\",");
		sb.append("\"Transaction Type\",");
		sb.append("\"Customer Reference\",");
		sb.append("\"Transaction Amount\",");
		sb.append("\"Fee\",");
		sb.append("\"Status\",");
		sb.append("\"Resp. Code\",");
		sb.append("\"Bit 48\",");
		sb.append("\"Bit 62\",");
		pw.println(sb.toString());
		while (i < rowCount) {
			List<ActivityCustomer> activityCustomers = activityCustomerDao.getCurrentPageRows(i, 1000, restrictions, null, values.toArray());
			if (activityCustomers == null) break;
			for (ActivityCustomer ac : activityCustomers) {
				sb.delete(0, sb.length());
				sb.append("\"").append(iso.format(ac.getCustomerId())).append("\"").append(",");
				sb.append("=\"").append(ac.getActivityType()).append("\"").append(",");
				sb.append("\"").append(ac.getActivityData()).append("\"").append(",");
				sb.append("=\"").append(ac.getStrReferenceNumber()).append("\"").append(",");
				sb.append("\"").append(ac.getDeliveryChannel()).append("\"").append(",");
				sb.append("\"").append(ac.getDeliveryChannelId()).append("\"").append(",");
				sb.append("\"").append(ac.getCreated()).append("\"").append(",");
				pw.println(sb.toString());
			}
			i += activityCustomers.size();
			if (byteArray.size() > SIZE_LIMIT)  break;
		} 
		
		pw.flush();
		pw.close();
		return new ByteArrayInputStream(byteArray.toByteArray());
	}
	
}
