package com.dwidasa.engine.model.view;

import com.dwidasa.engine.model.CellularPrefix;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 *
 * Class to simplify CellularPrefix model so that can fix to network limitation
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 2/9/12
 * Time: 3:14 PM
 */
public class CellularPrefixView implements Serializable {

    private static final long serialVersionUID = 4414653027984874752L;

    private Long id;
    private String prefix;
    private Long billerProductId;


    public CellularPrefixView(CellularPrefix prefix){
        this.id = prefix.getId();
        this.prefix = prefix.getPrefix();
        this.billerProductId = prefix.getBillerProductId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getBillerProductId() {
        return billerProductId;
    }

    public void setBillerProductId(Long billerProductId) {
        this.billerProductId = billerProductId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CellularPrefixView)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        CellularPrefixView that = (CellularPrefixView) o;
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
