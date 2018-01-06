package com.dwidasa.admin.pages.system;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.components.Window;
import com.dwidasa.engine.model.PeriodicProcess;
import com.dwidasa.engine.model.PeriodicTaskHistory;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.PeriodicProcessService;
import com.dwidasa.engine.service.UserService;

@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.NIGHT_ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class BodDetail {
	@Persist
	@Property
	private Long id;
	
	void onActivate(Long id) {
		this.id = id;
	}
	
	Long onPassivate() {
		return id;
	}
	
	@Property
	private String userName;
	
	@Inject
	private PeriodicProcessService periodicProcessService;
	
	@Inject
	private UserService userService;
	
	@Property
	private PeriodicProcess process;
	
	Object onActivate() {
		if (id == null) return BodList.class;
		process = periodicProcessService.get(id);
		if (process == null) return BodList.class;
		User user = userService.get(process.getCreatedby());
		userName = user.getUsername();
		return null;
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
    	long diffDetik = (diff - (diffMinutes * 60000)) / 1000;
    	StringBuffer sb = new StringBuffer(String.valueOf(diffMinutes)).append(" ").append(messages.get("minute")).append(" ");
    	sb.append(String.valueOf(diffDetik)).append(" ").append(messages.get("second"));
    	return sb.toString();
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
    
    public Object onSelectedFromBack() {
    	return BodList.class;
    }
    
    public Object onSelectedFromRefresh() {
    	return null;
    }
    
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;
    
    @Property
    private PeriodicTaskHistory row;
    
    @Component(parameters = {"style=bluelighting", "show=false",
            "modal=true", "title=literal:Trace Info", "width=700", "height=300"})
    private Window window1;
    
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;
        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        if (id != null && !id.equals("")) {
            restrictions.add("h_periodic_process_id =?");
            values.add(id);
        }
        List<String> orders = new ArrayList<String>();
        orders.add("execution_order");
        dataSource = new BaseDataSource(PeriodicTaskHistory.class, Constants.PAGE_SIZE, restrictions, values, null, orders);
    }
    
    public String getStrTrace(String traceInfo) {
    	String[] arrTrace = traceInfo.split("\n");
    	StringBuffer sb = new StringBuffer();
    	for (String trace : arrTrace) {
    		sb.append(trace).append("<br/>");
    	}
    	return sb.toString();
    }
    
    public boolean isInProcess() {
    	if (process == null ) return false;
    	return com.dwidasa.engine.Constants.PERIODIC_STATUS.PROGRESS.equals(process.getStatus());
    }
}
