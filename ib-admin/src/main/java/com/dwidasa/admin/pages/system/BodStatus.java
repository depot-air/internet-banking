package com.dwidasa.admin.pages.system;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.engine.service.task.PeriodicProcessExecutor;

@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.NIGHT_ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class BodStatus {
	@Inject
	private PeriodicProcessExecutor eodProcess;
	
	@Inject
	private Messages messages;
	
	Object onUpdateZone() {
		JSONObject obj = new JSONObject();
		
		if (eodProcess.getProgressInfo().size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (String str : eodProcess.getProgressInfo()) {
				sb.append(str).append("<br/>");
			}
			
			String lastInformation = eodProcess.getProgressInfo().get(eodProcess.getProgressInfo().size()-1); 
			if (lastInformation.startsWith("Processing Done") || lastInformation.startsWith("Processing Error")) {
				obj.put("stop", 1); //stop auto update
				
			} else {
				obj.put("stop", 0);
			}
			obj.put("content", sb.toString());
		} else {
			obj.put("content", "");
			obj.put("stop", 1); //stop auto update
		}
		return obj;
	}
	
	Object onSelectedFromBack() {
		return BodList.class;
	}
	
	
}
