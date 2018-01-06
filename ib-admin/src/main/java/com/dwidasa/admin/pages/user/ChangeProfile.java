package com.dwidasa.admin.pages.user;

import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.UserService;

public class ChangeProfile {
	@Property
	private User user;

	@Property
	private String confirmPassword;

	@Property
	private String password;

	@InjectComponent
	private Form form;

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

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAction() {
		if (id != null) {
			return messages.get("edit");
		}

		return messages.get("add");
	}

	public void onValidateFromForm() {
		if (password != null) {
			if (!password.equalsIgnoreCase(confirmPassword)) {
				form.recordError(messages.get("passwordError"));
			}
		}
		user.setUpdated(new Date());
		user.setUpdatedby(sessionManager.getLoggedUser().getId());

		if (password != null && confirmPassword != null) {
			user.setPassword(password);
		}
		userService.saveUser(user);
	}
	
	@InjectPage
	private ViewProfile viewProfile;
	
	@Inject
	private Request request;

	@DiscardAfter
	Object onSuccess() {
		request.getSession(false).setAttribute("msgViewProfile", messages.get("saveSuccess"));
		return ViewProfile.class;
	}

	Object onSelectedFromBack() {
		return ViewProfile.class;
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
}
