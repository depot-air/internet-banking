package com.dwidasa.ib.mixins;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;

import com.dwidasa.engine.util.RandomGenerator;

public class PerPageToken {
	@Environmental
	private FormSupport fs;

	@Inject
	private Request request;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AlertManager alertManager;
	
	@Inject
	private Messages messages;
	
	@Inject
	private PageRenderLinkSource renderLinkSource;
	
	void setupRender() {
		String token = RandomGenerator.generateHex(8);		
		request.getSession(true).setAttribute("formtoken"+componentResources.getId(), token);	
	}
	
	void afterRender(MarkupWriter writer) {
		Element form = writer.getElement().getElementById(fs.getClientId());
		String token = (String) request.getSession(false).getAttribute("formtoken"+componentResources.getId());
		form.element("input", "type", "hidden", "name", "formtoken", "value", token);
	}
	
	Object onValidate() {
		String tokenKey = "formtoken"+componentResources.getId();
		String pageName = componentResources.getPageName().toUpperCase();
		if (pageName.equals("LOGIN") || pageName.equals("LOGOUT")) return null;
		String clientToken = request.getParameter("formtoken") == null ? "": request.getParameter("formtoken");
		String serverToken = (String) (request.getSession(true).getAttribute(tokenKey) == null ? "" : request.getSession(false).getAttribute(tokenKey));
		
		request.getSession(false).setAttribute(tokenKey, null);
		
		if (!clientToken.equals(serverToken)) {
			alertManager.alert(Duration.SINGLE, Severity.INFO, messages.get("securityWarning"));
			componentResources.discardPersistentFieldChanges();
			Link link = renderLinkSource.createPageRenderLink(componentResources.getPageName());
	        return link;
		}
		return null;
	}
}
