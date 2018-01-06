package com.dwidasa.admin.pages;

import com.dwidasa.admin.annotations.PublicPage;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.UserService;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/13/11
 * Time: 12:30 PM
 */
@PublicPage
public class Login {
    public static final String LOGGED_USER = "com.dwidasa.admin.pages.Login.User";

    @Property
    private String username;

    @Property
    private String password;

    @Persist("flash")
    @Property
    private String error;

    @Component
    private Form loginForm;

    @Inject
    private Messages messages;

    @Inject
    private Request request;

    @Inject
    private UserService userService;

    @Log
    public Object onSubmitFromLoginForm() {
    	if (username == null) return null;
    	username = username.toUpperCase();
        String errorCode = null;
        User user = null;
        try {
            user = userService.authenticate(username, password);
        } catch (BusinessException e) {
            errorCode = e.getErrorCode();
        }

        if (errorCode != null) {
            error = messages.get("error.login");
        }
        else {
            request.getSession(true).setAttribute(LOGGED_USER, user);
        }

        return Index.class;
    }
}
