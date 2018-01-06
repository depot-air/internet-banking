package com.dwidasa.engine.model.view;

import com.dwidasa.engine.Constants;

import java.util.Date;

import java.util.List;
import com.dwidasa.engine.model.Version;
import com.dwidasa.engine.model.AppVersion;

/**
 * Bundled class about customer related data for view presentation and/or object
 * state storage purpose. Instead of dirtying our POJO, we place transient
 * information here.
 *
 * @author rk
 */
public class CustomerView {
    /**
     * Customer id, output field.
     */
    private Long id;
    /**
     * Customer username, output field.
     */
    private String username;
    /**
     * Customer name, output field.
     */
    private String name;
    /**
     * Customer email, output field.
     */
    private String email;
    /**
     * Customer phone, output field.
     */
    private String phone;
    /**
     * Customer pin, output field.
     */
    private String pin;
    /**
     * Customer default account number, output field.
     */
    private String accountNumber;
    /**
     * Account type, output field.
     */
    private String accountType;

    /**
     * Customer default card number, output field.
     */
    private String cardNumber;

    /**
     * Current session id, output field.
     */
    private String sessionId;
    /**
     * Last login timestamp, output field.
     */
    private Date lastLogin;
    /**
     * Last device id, output field.
     */
    private String lastDeviceId;
    /**
     * Device string such as Android, Blackberry, etc, output field.
     */
    private String device;

    /**
     * Possible value Y / N, Y for first time login.
     */
    private String firstLogin;
    /**
     * Terminal id of currently used device.
     */
    private String terminalId;

    /**
     * Customer type
     * 0 = Individual
     * 1 = Merchant
     */
    private Integer customerType;

    private List<Version> dataVersion;

    private AppVersion appVersion;

    public CustomerView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastDeviceId() {
        return lastDeviceId;
    }

    public void setLastDeviceId(String lastDeviceId) {
        this.lastDeviceId = lastDeviceId;

        if (lastDeviceId == null) {
            device = "IB/Kiosk";
            return;
        }

        if (lastDeviceId.length() == 17 && lastDeviceId.endsWith(Constants.BLACKBERRY)) {
            device = "Blackberry";
        }
        else if (lastDeviceId.length() == 17 && lastDeviceId.endsWith(Constants.ANDROID)) {
            device = "Android";
        }
        else if (lastDeviceId.length() == 17 && lastDeviceId.endsWith(Constants.IPHONE)) {
            device = "iPhone";
        }
        else if(lastDeviceId.length() == 17 && lastDeviceId.endsWith(Constants.BB10)){
        	device = "BB10";
        }
        else {
            device = "IB/Kiosk";
        }
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }


    public String getTerminalId() {
        return (terminalId == null) ? "MBS" : terminalId;	//default = MBS, untuk keperluan tes account merchant
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public List<Version> getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(List<Version> dataVersion) {
        this.dataVersion = dataVersion;
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }
}
