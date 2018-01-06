package com.dwidasa.admin.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.Session;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.PublicPage;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.pages.Index;
import com.dwidasa.admin.pages.Login;
import com.dwidasa.engine.model.User;
import com.dwidasa.interlink.utility.Constant;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/13/11
 * Time: 2:59 PM
 */
public class AuthenticationFilter implements ComponentRequestFilter {
    private final PageRenderLinkSource renderLinkSource;
    private final ComponentSource componentSource;
    private final Response response;
    private final Request request;
    private final Map<Long, String> roleMap;

    private String loginPage = Login.class.getSimpleName();
    private String defaultPage = Index.class.getSimpleName();
    

    public AuthenticationFilter(PageRenderLinkSource renderLinkSource, ComponentSource componentSource,
                                Response response, Request request) {
        this.renderLinkSource = renderLinkSource;
        this.componentSource = componentSource;
        this.response = response;
        this.request = request;
        roleMap = new HashMap<Long, String>();
        roleMap.put(Constants.Role.SUPERUSER, Constants.RoleName.SUPERUSER);
        roleMap.put(Constants.Role.ADMIN, Constants.RoleName.ADMIN);
        roleMap.put(Constants.Role.TREASURY, Constants.RoleName.TREASURY);
        roleMap.put(Constants.Role.DAY_ADMIN, Constants.RoleName.DAY_ADMIN);
        roleMap.put(Constants.Role.NIGHT_ADMIN, Constants.RoleName.NIGHT_ADMIN);
    }

    public void handleComponentEvent(ComponentEventRequestParameters parameters,
                                     ComponentRequestHandler handler) throws IOException {
        if (dispatchedToLoginPage(parameters.getActivePageName())) {
            return;
        }
        handler.handleComponentEvent(parameters);
    }

    public void handlePageRender(PageRenderRequestParameters parameters,
                                 ComponentRequestHandler handler) throws IOException {
        if (dispatchedToLoginPage(parameters.getLogicalPageName())) {
            return;
        }
        handler.handlePageRender(parameters);
    }

    private Boolean isLoggedIn() {
        Session session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(Login.LOGGED_USER) != null;
        }

        return Boolean.FALSE;
    }

    private Boolean dispatchedToLoginPage(String pageName) throws IOException {
    	Component page = componentSource.getPage(pageName);
        if (isLoggedIn()) {
            if (pageName.equalsIgnoreCase(loginPage)) {
                Link link = renderLinkSource.createPageRenderLink(defaultPage);
                response.sendRedirect(link);
                return Boolean.TRUE;
            }
            
            if (page.getClass().isAnnotationPresent(com.dwidasa.admin.annotations.Restricted.class)) {
                User user = (User) request.getSession(true).getAttribute(Login.LOGGED_USER);
            	Restricted restricted = page.getClass().getAnnotation(Restricted.class);
            	boolean hasAccess = false;
    			for (String requiredGroup : restricted.groups()) {
    				if (roleMap.get(user.getRoleId()).equals(requiredGroup)) {
						hasAccess = true;
						break;    						
    				}
    			}
    			if (!hasAccess) {
    				Link link = renderLinkSource.createPageRenderLink(defaultPage);
                    response.sendRedirect(link);
                    return Boolean.TRUE;
    			}
            }
            return Boolean.FALSE;
        }

        if (page.getClass().isAnnotationPresent(PublicPage.class)) {
            return Boolean.FALSE;
        }

        Link link = renderLinkSource.createPageRenderLink(loginPage);
        response.sendRedirect(link);
        return Boolean.TRUE;
    }
}
