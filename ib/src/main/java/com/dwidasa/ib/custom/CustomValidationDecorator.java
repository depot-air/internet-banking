package com.dwidasa.ib.custom;

import org.apache.tapestry5.BaseValidationDecorator;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/6/11
 * Time: 2:11 PM
 */
public class CustomValidationDecorator extends BaseValidationDecorator {
    private final Environment environment;
    private final MarkupWriter markupWriter;

    public CustomValidationDecorator(Environment environment, MarkupWriter markupWriter) {
        this.environment = environment;
        this.markupWriter = markupWriter;
    }

    @Override
    public void insideField(Field field) {
        ValidationTracker tracker = environment.peekRequired(ValidationTracker.class);

        if (tracker.inError(field)) {
            getElement().addClassName("error_field");
        }
    }

    private Element getElement() {
        return markupWriter.getElement();
    }
}
