package com.dwidasa.admin.services;

import com.dwidasa.admin.pages.Login;
import com.dwidasa.engine.model.User;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/18/11
 * Time: 9:56 AM
 */
public class SessionManager {
    @Inject
    private Request request;

    public User getLoggedUser() {
        return (User) request.getSession(false).getAttribute(Login.LOGGED_USER);
    }
}
