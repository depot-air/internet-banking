package com.dwidasa.ib.filter;

import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.pages.Index;
import com.dwidasa.ib.pages.Login;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.*;

import java.io.IOException;

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

    private String loginPage = Login.class.getSimpleName();
    private String defaultPage = Index.class.getSimpleName();

    public AuthenticationFilter(PageRenderLinkSource renderLinkSource, ComponentSource componentSource,
                                Response response, Request request) {
        this.renderLinkSource = renderLinkSource;
        this.componentSource = componentSource;
        this.response = response;
        this.request = request;
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
            return session.getAttribute(Login.LOGGED_CUSTOMER) != null;
        }

        return Boolean.FALSE;
    }

    private Boolean dispatchedToLoginPage(String pageName) throws IOException {
        if (isLoggedIn()) {
            if (pageName.equalsIgnoreCase(loginPage)) {
                Link link = renderLinkSource.createPageRenderLink(defaultPage);
                response.sendRedirect(link);
                return Boolean.TRUE;
            }

            return Boolean.FALSE;
        }

        Component page = componentSource.getPage(pageName);
        if (page.getClass().isAnnotationPresent(PublicPage.class)) {
            return Boolean.FALSE;
        }

        Link link = renderLinkSource.createPageRenderLink(loginPage);
        response.sendRedirect(link);
        return Boolean.TRUE;
    }
}
