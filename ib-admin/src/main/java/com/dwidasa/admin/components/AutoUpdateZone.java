package com.dwidasa.admin.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = {"AutoUpdateZone.js"})
public class AutoUpdateZone {
	@Environmental
	private JavaScriptSupport renderSupport;
	@Inject
	private ComponentResources componentResources;
	
	@AfterRender
	public void afterRender() {
		Link link = componentResources.createEventLink("updateZone");
		renderSupport.addScript(String.format("new AutoUpdateZone('%s', '%s', '%s');",
			"dataholder", "datadisplay", link.toString()));
	}
}
