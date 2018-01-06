package com.dwidasa.admin.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class DisplayMessage {
	@Inject
	private Request request;
	
	@Inject
	private JavaScriptSupport renderSupport;
	
	@Inject
	private ComponentResources componentResources;
	
	void beginRender(MarkupWriter markupWriter) {
		Session session = request.getSession(false);
		if (session == null) return;
		String pageName = componentResources.getPage().getClass().getSimpleName();
		String message = (String) session.getAttribute("msg" + pageName);
		if (message == null) return;
		markupWriter.element("div", "id", "divMessageInfo");
		markupWriter.write(message);
		markupWriter.end();
		request.getSession(false).setAttribute("msg" + pageName, null);
		renderSupport.addScript("new Effect.Fade('divMessageInfo', { duration:3.0, from:1.0, to:0 });");
	}
	
}
