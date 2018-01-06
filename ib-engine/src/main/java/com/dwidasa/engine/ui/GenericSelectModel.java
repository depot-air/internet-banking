package com.dwidasa.engine.ui;

import java.util.Locale;

/**
 * Interface to define selection model property, e.g. label and value
 */
public interface GenericSelectModel {
    /**
     * Get label of implementation class to be rendered as label property
     * of select component
     * @param locale application selected locale
     * @return label string
     */
    public String getLabel(Locale locale);

    /**
     * Get value of implementation class to be rendered as value property
     * of select component
     * @return value string
     */
    public String getValue();
}
