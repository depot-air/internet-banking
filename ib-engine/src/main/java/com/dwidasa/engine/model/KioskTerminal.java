package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/8/12
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class KioskTerminal extends BaseObject implements Serializable {
    private static final long serialVersionUID = 1223649883933373944L;

    private Long id;
    private String terminalId;
    private Long mLocationIdTerminal;
    private Long mLocationIdBranch;
    private Long mUserId;
    private String description;

    private Location locationBranch;
    private Location locationTerminal;
    private User userAdmin;

    private String locationBranchDesc;
    private String locationTerminalDesc;
    private String userAdminDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KioskTerminal)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        KioskTerminal that = (KioskTerminal) o;
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
        return (getTerminalId() != null) ? getTerminalId() : "";
    }

    public Long getmLocationIdTerminal() {
        return mLocationIdTerminal;
    }

    public void setmLocationIdTerminal(Long mLocationIdTerminal) {
        this.mLocationIdTerminal = mLocationIdTerminal;
    }

    public Long getmLocationIdBranch() {
        return mLocationIdBranch;
    }

    public void setmLocationIdBranch(Long mLocationIdBranch) {
        this.mLocationIdBranch = mLocationIdBranch;
    }

    public Long getmUserId() {
        return mUserId;
    }

    public void setmUserId(Long mUserId) {
        this.mUserId = mUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocationBranch() {
        return locationBranch;
    }

    public void setLocationBranch(Location locationBranch) {
        this.locationBranch = locationBranch;
    }

    public Location getLocationTerminal() {
        return locationTerminal;
    }

    public void setLocationTerminal(Location locationTerminal) {
        this.locationTerminal = locationTerminal;
    }

    public User getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(User userAdmin) {
        this.userAdmin = userAdmin;
    }

    public String getLocationBranchDesc() {
        return locationBranchDesc;
    }

    public void setLocationBranchDesc(String locationBranchDesc) {
        this.locationBranchDesc = locationBranchDesc;
    }

    public String getLocationTerminalDesc() {
        return locationTerminalDesc;
    }

    public void setLocationTerminalDesc(String locationTerminalDesc) {
        this.locationTerminalDesc = locationTerminalDesc;
    }

    public String getUserAdminDesc() {
        return userAdminDesc;
    }

    public void setUserAdminDesc(String userAdminDesc) {
        this.userAdminDesc = userAdminDesc;
    }
}
