package com.dwidasa.admin.pages.user;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.UserService;

public class ViewProfile {
	@Property
	private User user;

	@Inject
	private UserService userService;

	@Inject
	private Messages messages;

	@Persist
	@Property(write = false)
	private Long id;

	@Inject
	private SessionManager sessionManager;

	void onActivate() {
		id = sessionManager.getLoggedUser().getId();
	}

	void onPrepare() {
		if (id != null) {
			user = userService.get(id);
		}
	}

	public String getRoleName() {
		if (user == null || user.getRoleId() == null) return "-";
		if (Constants.Role.SUPERUSER.equals(user.getRoleId())) {
			return Constants.RoleName.SUPERUSER;
		} else if (Constants.Role.ADMIN.equals(user.getRoleId())) {
			return Constants.RoleName.ADMIN;
		} else if (Constants.Role.TREASURY.equals(user.getRoleId())) {
			return Constants.RoleName.TREASURY;
		} else if (Constants.Role.DAY_ADMIN.equals(user.getRoleId())) {
			return Constants.RoleName.DAY_ADMIN;
		} else if (Constants.Role.NIGHT_ADMIN.equals(user.getRoleId())) {
			return Constants.RoleName.NIGHT_ADMIN;
		} 
		return "-";
	}
	
	Object onSuccess() {
		return ChangeProfile.class;
	}
	
	@Persist(PersistenceConstants.FLASH)
	private String messageInfo;
	
	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}
	
	@Environmental
	private JavaScriptSupport renderSupport;
	
	void setupRender() {
		if (messageInfo != null) {
			renderSupport.addScript("new Effect.Fade('divMessageInfo', { duration:3.0, from:1.0, to:0 });");
		}
	}
}
