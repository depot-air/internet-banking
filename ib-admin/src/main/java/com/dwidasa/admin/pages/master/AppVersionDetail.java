package com.dwidasa.admin.pages.master;

import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.AppVersion;
import com.dwidasa.engine.service.AppVersionService;

@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class AppVersionDetail {
	@Property(write = false)
	private Long id;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	Long onPassivate() {
		return id;
	}
	
	void onActivate(Long id) {
		this.id = id;
	}
	
	@Property
	private AppVersion row;
	
	@Inject
	private AppVersionService appVersionService;
	
	void onPrepare() {
        if (id != null) {
            row = appVersionService.get(id);
        }
        else {
            row = new AppVersion();
            row.setMandatory(1);
        }
    }
	
	private boolean isBackButton;
	
	void onSelectedFromBack() {
		isBackButton = true;
	}
	
	void onSelectedFromAdd() {
		isBackButton = false;
	}
	
	@InjectComponent
	private Form form;
	
	@Inject
	private SessionManager sessionManager;
	
	void onValidateFromForm() {
		if (isBackButton) {
			form.clearErrors();
			return;
		}
		Long userId = sessionManager.getLoggedUser().getId();
		Date currentDate = new Date();
		if (row.getId() == null) {
	        row.setCreated(currentDate);
	        row.setCreatedby(userId);
		}
		row.setUpdated(currentDate);
		row.setUpdatedby(userId);
		appVersionService.save(row);
	}
	
	@DiscardAfter
	Object onSuccess() {
		return AppVersionList.class;
	}
	
	@Inject
	private Messages messages;
	
	public String getSubtitle() {
        if (id != null) {
            return messages.get("subtitle-edit");
        }

        return messages.get("subtitle-add");
    }

	
	public String getAction() {
        if (id != null) {
            return messages.get("edit");
        }

        return messages.get("add");
    }


}
