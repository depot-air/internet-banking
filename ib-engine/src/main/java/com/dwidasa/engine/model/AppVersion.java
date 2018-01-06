package com.dwidasa.engine.model;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.util.List;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 1/30/12
 * Time: 9:10 AM
 */
public class AppVersion extends BaseObject implements Serializable{

    private static final long serialVersionUID = -849262560068383019L;

    private String deviceType;
    private Long versionId;
    private String version;
    private Integer mandatory;
    private String changes;
    private String url;

    private List<String> listOfChanges;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getMandatory() {
        return mandatory;
    }

    public void setMandatory(Integer mandatory) {
        this.mandatory = mandatory;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getListOfChanges() {
        return listOfChanges;
    }

    public void setListOfChanges(List<String> listOfChanges) {
        this.listOfChanges = listOfChanges;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Version)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Version that = (Version) o;
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
