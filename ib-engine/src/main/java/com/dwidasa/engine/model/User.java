package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.ui.GenericSelectModel;

public class User extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = -6270631138871109025L;

    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private Integer failedAuthAttempts;
    private Date lastLogin;
    private Integer status;

    private Long roleId;      

    private Role role;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFailedAuthAttempts() {
        return failedAuthAttempts;
    }

    public void setFailedAuthAttempts(Integer failedAuthAttempts) {
        this.failedAuthAttempts = failedAuthAttempts;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        User that = (User) o;
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

	@Override
	public String getLabel(Locale locale) {
        return getUsername();
    }

    public String getValue() {
        return getUsername();
	}
}
