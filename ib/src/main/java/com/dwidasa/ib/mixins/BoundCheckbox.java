package com.dwidasa.ib.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library = {"BoundCheckbox.js"})
public class BoundCheckbox {
	@InjectContainer
	private ClientElement container;

	@Parameter
	private Checkbox master;

	@Environmental
	private JavaScriptSupport renderSupport;

	@AfterRender
	void after(MarkupWriter writer) {
		String masterClientId = master == null ? "" : master.getClientId();
		renderSupport.addInitializerCall("boundCheckboxLoad", new JSONObject("clientId", container.getClientId(), "masterId", masterClientId));
	}
}

