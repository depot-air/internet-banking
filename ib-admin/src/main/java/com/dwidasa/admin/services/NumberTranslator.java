package com.dwidasa.admin.services;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.FormSupport;

public class NumberTranslator<T extends Number> implements Translator<T> {

	private static final List<Class<?>> INTEGER_TYPES = Arrays
			.asList(new Class<?>[] { Byte.class, Short.class, Integer.class,
					Long.class, BigInteger.class

			});

	private final Class<T> type;
	private final NumberFormat formatter;
	private final TypeCoercer typeCoercer;
	private final String messageKey;

	public NumberTranslator(Class<T> type, NumberFormat formatter,
			TypeCoercer typeCoercer) {
		this.type = type;
		this.formatter = formatter;
		this.typeCoercer = typeCoercer;
		this.messageKey = INTEGER_TYPES.contains(type) ? "integer-format-exception"
				: "number-format-exception";
	}

	public String getName() {
		return "number";
	}

	public Class<T> getType() {
		return type;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public T parseClient(Field field, String value, String message)
			throws ValidationException {
		if (value == null || value.length() == 0) {
			return null;
		}
		ParsePosition pp = new ParsePosition(0);
		Number number = formatter.parse(value, pp);
		// All input characters must be consumed to constitute success.
		if ((number != null) && (pp.getIndex() != value.length())) {
			number = null;
		}
		if (number == null) {
			throw new ValidationException(message);
		}
		return typeCoercer.coerce(number, type);
	}

	public String toClient(T value) {
		return formatter.format(value);
	}

	public void render(Field field, String message, MarkupWriter writer,
			FormSupport formSupport) {
		// empty; no client-side support
	}
}
