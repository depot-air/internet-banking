package com.dwidasa.ib.validators;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.validator.AbstractValidator;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/26/11
 * Time: 7:53 PM
 */
public class AcrossField extends AbstractValidator<String, String> {
    public AcrossField() {
        super(String.class, String.class, "across-field");
    }

    public void validate(Field field, String constraintValue, MessageFormatter formatter, String value)
            throws ValidationException {
    }

    public void render(Field field, String constraintValue, MessageFormatter formatter, MarkupWriter writer,
                       FormSupport formSupport) {
        formSupport.addValidation(field, "acrossField", buildMessage(formatter, field), constraintValue);
    }

    private String buildMessage(MessageFormatter formatter, Field field) {
        return formatter.format(field.getLabel());
    }
}
