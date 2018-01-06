package com.dwidasa.admin.view;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 12/20/11
 * Time: 00:47 am
 */
public class UserView {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private Integer failedAuthAttempts;
    private Date lastLogin;
    private String status;
    private String roleName;

    public UserView(){

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
