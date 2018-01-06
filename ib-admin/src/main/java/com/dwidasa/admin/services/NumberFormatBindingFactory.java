package com.dwidasa.admin.services;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.FieldTranslatorSource;

public class NumberFormatBindingFactory implements BindingFactory {

	private final FieldTranslatorSource fieldTranslatorSource;
	private final TypeCoercer typeCoercer;

	public NumberFormatBindingFactory(
			FieldTranslatorSource fieldTranslatorSource, TypeCoercer typeCoercer) {

		this.fieldTranslatorSource = fieldTranslatorSource;
		this.typeCoercer = typeCoercer;
	}

	public Binding newBinding(String description,
			ComponentResources containerResources,
			ComponentResources fieldResources, String expression,
			Location location) {

		return new NumberFormatBinding(fieldResources, expression, location,
				fieldTranslatorSource, typeCoercer);
	}
}
