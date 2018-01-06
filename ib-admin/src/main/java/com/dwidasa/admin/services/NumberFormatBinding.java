package com.dwidasa.admin.services;

import java.lang.annotation.Annotation;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ioc.BaseLocatable;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.FieldTranslatorSource;

public class NumberFormatBinding extends BaseLocatable implements Binding {

	private final Translator<?> translator;
	private final FieldTranslatorSource fieldTranslatorSource;
	private final Field field;
	private final String overrideId;
	private final Messages overrideMessages;
	private final Locale locale;

	private FieldTranslator<?> fieldTranslator;

	public NumberFormatBinding(ComponentResources fieldResources,
			String formatPattern, Location location,
			FieldTranslatorSource fieldTranslatorSource, TypeCoercer typeCoercer) {

		super(location);

		this.translator = createTranslator(fieldResources, formatPattern, typeCoercer);
		this.fieldTranslatorSource = fieldTranslatorSource;
		this.field = (Field) fieldResources.getComponent();
		this.overrideId = fieldResources.getId();
		this.overrideMessages = fieldResources.getContainerMessages();
		this.locale = fieldResources.getLocale();
	}

	public Object get() {
		if (fieldTranslator == null) {
			// Create the FieldTranslator lazily to ensure FormSupport is present
			fieldTranslator = fieldTranslatorSource.createTranslator(
					field, overrideId, overrideMessages, locale, translator);
		}
		return fieldTranslator;
	}

	public void set(Object value) {
		throw new UnsupportedOperationException();
	}

	public Class<?> getBindingType() {
		return get().getClass();
	}

	public boolean isInvariant() {
		return true;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return null;
	}

	private NumberFormat createNumberFormat(String pattern, Locale locale) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
		DecimalFormat format = new DecimalFormat(pattern, symbols);
		if (pattern.indexOf('.') < 0) {
			format.setParseIntegerOnly(true);
		}
		return format;
	}

	@SuppressWarnings("unchecked") 
    private <T extends Number> Translator<?> createTranslator(ComponentResources fieldResources,
            String formatPattern, TypeCoercer typeCoercer ) { 

        NumberFormat numberFormat = createNumberFormat( formatPattern, fieldResources.getLocale() ); 

        Class<T> numberType = fieldResources.getBoundType("value"); 
        if (numberType == null) { 
            throw new IllegalStateException("'value' parameter not bound for " + fieldResources.getId() +"; numeric type unknown."); 
        } 
        return new NumberTranslator<T>( numberType, numberFormat, typeCoercer ); 
    }
}
