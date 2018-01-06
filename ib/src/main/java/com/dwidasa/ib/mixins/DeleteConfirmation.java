package com.dwidasa.ib.mixins;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(library="DeleteConfirmation.js")
public class DeleteConfirmation {
	@Environmental 
	private JavaScriptSupport js;
	
	@Inject
	private Messages messages;
	
	@AfterRender
    void addConfirmation() {
		js.addScript("new DeleteConfirmation('%s', '%s');", messages.get("confirmDelete"), messages.get("needCheck"));
    }
}
