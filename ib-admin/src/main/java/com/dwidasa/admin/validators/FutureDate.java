package com.dwidasa.admin.validators;

import com.dwidasa.engine.util.DateUtils;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.validator.AbstractValidator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/26/11
 * Time: 1:30 PM
 */
public class FutureDate extends AbstractValidator<Void, Date> {
    public FutureDate() {
        super(null, Date.class, "future-date");
    }

    public void validate(Field field, Void constraintValue, MessageFormatter formatter, Date value)
            throws ValidationException {
        if (DateUtils.truncate(value).before(DateUtils.truncate(new Date()))) {
            throw new ValidationException(buildMessage(formatter, field));
        }
    }

    public void render(Field field, Void constraintValue, MessageFormatter formatter, MarkupWriter writer,
                       FormSupport formSupport) {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        formSupport.addValidation(field, "futureDate", buildMessage(formatter, field),
                sdf.format(DateUtils.truncate(new Date())));
    }

    private String buildMessage(MessageFormatter formatter, Field field) {
        return formatter.format(field.getLabel());
    }
}
