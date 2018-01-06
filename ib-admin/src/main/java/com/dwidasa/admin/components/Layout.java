package com.dwidasa.admin.components;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.services.MenuManager;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Menu;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.CacheManager;

/**
 * Layout component for pages of application admin.
 */
@Import(library = {"context:bprks/js/jquery-1.6.2.min.js", "context:bprks/js/jquery.js",
        "context:bprks/js/script.js", "context:bprks/js/layout.js", "context:bprks/js/validation.js"}, 
        stylesheet = {"context:bprks/css/reset.css", "context:bprks/css/text.css", "context:bprks/css/960.css",
		"context:bprks/css/style.css"})
public class Layout
{
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String group;

    @Inject
    private Messages messages;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private Request request;

    @Property
    private User user;

    @Property
    private String now;

    private List<Menu> roots;
    
    public List<Menu> getRoots() {
        return roots;
    }
    
    @Inject
    private MenuManager menuManager;

    void setupRender() {
        user = sessionManager.getLoggedUser();
        now = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date());
        if (user.getRoleId().equals(com.dwidasa.admin.Constants.Role.SUPERUSER)) {
        	roots = menuManager.getSuperuserMenu();        	
        } else if (user.getRoleId().equals(com.dwidasa.admin.Constants.Role.ADMIN)) {
        	roots = menuManager.getAdminMenu();
        } else if (user.getRoleId().equals(com.dwidasa.admin.Constants.Role.TREASURY)) {
        	roots = menuManager.getTreasuryMenu();
        } else if (user.getRoleId().equals(com.dwidasa.admin.Constants.Role.DAY_ADMIN)) {
        	roots = menuManager.getDayAdminMenu();
        } else if (user.getRoleId().equals(com.dwidasa.admin.Constants.Role.NIGHT_ADMIN)) {
        	roots = menuManager.getNightAdminMenu();
        } 
    	renderSupport.addScript("if ($('alerts') != null) new Effect.Fade('alerts', { duration:3.0, from:1.0, to:0 });");
    }

    void onActionFromLogout() {
        request.getSession(Boolean.FALSE).invalidate();
    }
    
    public boolean isRole(List<Long> roleIds) {
    	if (sessionManager.getLoggedUser() == null) return false;
    	for (Long roleId : roleIds) {
    		if (sessionManager.getLoggedUser().getRoleId().equals(roleId)) {
    			return true;
    		}
    	}
    	return false;
    }
       
	
	@Environmental
	private JavaScriptSupport renderSupport;
	
	
	
}
