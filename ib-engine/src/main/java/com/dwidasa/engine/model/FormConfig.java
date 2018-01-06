package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/7/11
 * Time: 4:53 PM
 */
public class FormConfig extends BaseObject implements Serializable {
    private static final long serialVersionUID = -636626488828124217L;

    /**
     * Form name is simple name of java page class.
     */
    private String formName;
    /**
     * We will render screen of a form whether token is required or
     * not based on this property value.<br/>
     * Possible value : <br/>
     * 0 = Without token <br/>
     * 1 = Response Only Token <br/>
     * 2 = Token with challenge <br/>
     */
    private Integer tokenType;

    public FormConfig() {
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Integer getTokenType() {
        return tokenType;
    }

    public void setTokenType(Integer tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FormConfig)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        FormConfig that = (FormConfig) o;
        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
}
