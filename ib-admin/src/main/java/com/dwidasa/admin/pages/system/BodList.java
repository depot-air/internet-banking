package com.dwidasa.admin.pages.system;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.engine.model.PeriodicProcess;
import com.dwidasa.engine.service.PeriodicProcessService;
import com.dwidasa.engine.service.VersionService;

@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.NIGHT_ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class BodList {
	public String getDateFieldFormat() {
        return Constants.SHORT_FORMAT;
    }
	
	@Property
	private Date bodDate;
	
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private PeriodicProcess row;

    @Property
    @Persist
    private String accountType;

    @Inject
    private PeriodicProcessService periodicProcessService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (accountType != null && !accountType.equals("")) {
            restrictions.add("upper(account_type) like '%' || ? || '%'");
            values.add(accountType.toUpperCase());
        }

        List<String> orders = new ArrayList<String>();
        orders.add("start_time desc");
        dataSource = new BaseDataSource(PeriodicProcess.class, Constants.PAGE_SIZE, restrictions, values, null, orders);
        
        //eodDate
        bodDate = periodicProcessService.getLastProcessDate(com.dwidasa.engine.Constants.TASK_PERIOD_TYPE.BOD);
        if (bodDate == null) {
        	bodDate = new Date();
        } else {
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(bodDate);
        	cal.add(Calendar.DATE,1);
        	bodDate = cal.getTime();
        }
    }

    @Inject
    private VersionService versionService;
       
    @InjectPage
    private BodConfirm eodConfirm;
    
    Object onSuccess() {
    	DateFormat ymd = new SimpleDateFormat(Constants.YMD_FORMAT);
    	eodConfirm.setStrEodDate(ymd.format(bodDate));
    	return eodConfirm;
    }
    
    private final DateFormat dateFormat = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
    private final DateFormat dateTimeFormat = new SimpleDateFormat(Constants.LONG_FORMAT);
    
    public String getStrDate(Date date) {
    	if (date == null) return "-";
    	return dateFormat.format(date);
    }
    
    public String getStrDateTime(Date dateTime) {
    	if (dateTime == null) return "-";
    	return dateTimeFormat.format(dateTime);
    }
    
    public String getStrDuration(Date startTime, Date endTime) {
    	if (endTime == null || startTime == null) {
    		return "-";
    	}
    	long diff = endTime.getTime() - startTime.getTime();
    	long diffMinutes = diff / (60 * 1000); 
    	return String.valueOf(diffMinutes) + " " + messages.get("minute");
    }
    
    @Inject 
    private Messages messages;
    
    public String getStrStatus(String status) {
    	if (com.dwidasa.engine.Constants.PERIODIC_STATUS.SUCCESS.equals(status)) {
    		return messages.get("success");
    	} else if (com.dwidasa.engine.Constants.PERIODIC_STATUS.PROGRESS.equals(status)) {
    		return messages.get("progress");
    	} else if (com.dwidasa.engine.Constants.PERIODIC_STATUS.WAIT.equals(status)) {
    		return messages.get("wait");
    	} else if (com.dwidasa.engine.Constants.PERIODIC_STATUS.FAIL.equals(status)) {
    		return messages.get("fail");
    	}
    	return "-";
    }
    
    
}
