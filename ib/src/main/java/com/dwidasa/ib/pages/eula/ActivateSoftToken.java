package com.dwidasa.ib.pages.eula;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.corelib.components.Form;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class ActivateSoftToken {
    @InjectComponent
    private Form form;

	@InjectPage
	private ActivateSoftToken eulaActivateTransaction;
	
    void beginRender() {

    }

    void onValidateFromForm() {

    }


    @DiscardAfter
    Object onAction() {
        return eulaActivateTransaction;
    }

}
