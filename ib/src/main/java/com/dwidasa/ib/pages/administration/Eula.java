package com.dwidasa.ib.pages.administration;

import com.dwidasa.ib.Constants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/8/11
 * Time: 00:00 am
 */
public class Eula {
    @SessionAttribute(Constants.SELECTED_LANGUAGE)
    @Property
    private String selectedLanguage;

    @Inject
    private Messages messages;

    Object onSuccessFromEula(){
        return PersonalDataInput.class;
    }

    public List<String> getAvailableLanguages() {
        return Arrays.asList(messages.get("indonesian"), messages.get("english"));
    }
}
