package com.dwidasa.engine.dto;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/15/11
 * Time: 7:38 PM
 */
public class RegistrationDto {
    private long incomingId;
    private Date incomingDate;
    private String requestString;
    private int channelId;
    private String ipv4Address;
    private String responseString;
    private int requestType;
    private int responseType;

    private String userId;
    private String tokenId;
    private String activationCode;

    public RegistrationDto() {
    }

    public long getIncomingId() {
        return incomingId;
    }

    public void setIncomingId(long incomingId) {
        this.incomingId = incomingId;
    }

    public Date getIncomingDate() {
        return incomingDate;
    }

    public void setIncomingDate(Date incomingDate) {
        this.incomingDate = incomingDate;
    }

    public String getRequestString() {
        return requestString;
    }

    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getResponseType() {
        return responseType;
    }

    public void setResponseType(int responseType) {
        this.responseType = responseType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
