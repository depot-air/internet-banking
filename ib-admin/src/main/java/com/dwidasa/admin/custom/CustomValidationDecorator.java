package com.dwidasa.admin.custom;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BaseValidationDecorator;
import org.apache.tapestry5.CSSClassConstants;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.FormSupport;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/6/11
 * Time: 2:11 PM
 */
public final class CustomValidationDecorator extends BaseValidationDecorator {
    private final Environment environment;
    private final MarkupWriter markupWriter;

    public CustomValidationDecorator(Environment environment, Asset spacerAsset, MarkupWriter markupWriter) {
        this.environment = environment;
        this.markupWriter = markupWriter;
    }

    @Override
    public void insideField(Field field) {
//        ValidationTracker tracker = environment.peekRequired(ValidationTracker.class);
//
//        if (tracker.inError(field)) {
//            markupWriter.getElement().addClassName("error_field");
//        }
    }
}
