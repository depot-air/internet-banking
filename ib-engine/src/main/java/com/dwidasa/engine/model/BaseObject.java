package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Base model for all POJO. Contain all common field amongst them. This class
 * will force all of its descendant class to implements equals and hashcode method.
 *
 * @author rk
 */
@SuppressWarnings("serial")
public abstract class BaseObject implements Serializable {
    private Long id;
    private Date created;
    private Long createdby;
    private Date updated;
    private Long updatedby;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(Long updatedby) {
        this.updatedby = updatedby;
    }

    /**
     * Compares object equality.
     * @param o object to compare to
     * @return true/false based on equality tests
     */
    public abstract boolean equals(Object o);

    /**
     * Hash value of this object
     * @return hashCode
     */
    public abstract int hashCode();

    /**
     * Value returned will be used by tapestry select component,
     * i.e to be rendered as property value
     * @return String value worth for rendering select component
     */
    public abstract String toString();
}
