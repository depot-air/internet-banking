package com.dwidasa.admin.pages.system;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.service.task.PeriodicProcessExecutor;

@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.NIGHT_ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class BodConfirm {
	@Persist
	private String strEodDate;
	public String getStrEodDate() {
		return strEodDate;
	}
	public void setStrEodDate(String strEodDate) {
		this.strEodDate = strEodDate;
	}
	
	@Persist
	@Property
	private Date eodDate;

	@Inject
	private Messages messages;
	
	Object onActivate() {
		if (strEodDate == null) {
			return BodList.class;
		}
		return null;
	}
		

	public String getStrMessage() {
		DateFormat format= new SimpleDateFormat(Constants.YMD_FORMAT);
		DateFormat displayFormat= new SimpleDateFormat(Constants.MEDIUM_FORMAT);
		try {
			eodDate = format.parse(strEodDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return messages.get("errorDate");
		} 
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime(eodDate);
		sb.append(messages.get("day" + cal.get(Calendar.DAY_OF_WEEK))).append(" ").append(displayFormat.format(eodDate));
		return String.format(messages.get("confirm"), sb.toString());
	}
	
	public Object onSelectedFromCancel() {
		return BodList.class;
	}
	
	@InjectPage
	private BodStatus eodStatus;
	
	@Inject
	private PeriodicProcessExecutor periodicProcessExecutor;
	
	@InjectComponent
	private Form form;
	
	@Inject
	private SessionManager sessionManager;
	
	public Object onSelectedFromProcess() {
		if (!periodicProcessExecutor.isReady()) {
			form.recordError(messages.get("errorInProgress"));
			return null;
		}
				
		periodicProcessExecutor.initProcess(com.dwidasa.engine.Constants.TASK_PERIOD_TYPE.BOD, sessionManager.getLoggedUser().getId(), eodDate);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(periodicProcessExecutor);
		return eodStatus;
	}
}
