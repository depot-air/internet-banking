package com.dwidasa.engine.model.view;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 12/11/11
 * Time: 9:02 PM
 */
public class NonTagListPaymentView extends PaymentView {
    private String areaCode;
    private String transactionCode;
    private String transactionName;
    private Date registrationDate;
    private Date expiredDate;
    private String subscriberId;
    private String subscriberName;
    private String billerReference;
    private String providerReference;
    private String serviceUnit;
    private String unitAddress;
    private String unitPhone;
    private String bit62;
    private String informasiStruk;

    public NonTagListPaymentView() {
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getBillerReference() {
        return billerReference;
    }

    public void setBillerReference(String billerReference) {
        this.billerReference = billerReference;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public void setProviderReference(String providerReference) {
        this.providerReference = providerReference;
    }

    public String getServiceUnit() {
        return serviceUnit;
    }

    public void setServiceUnit(String serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }

    public String getUnitPhone() {
        return unitPhone;
    }

    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone;
    }

    public String getBit62() {
        return bit62;
    }

    public void setBit62(String bit62) {
        this.bit62 = bit62;
    }

    public String getInformasiStruk() {
        return informasiStruk;
    }

    public void setInformasiStruk(String informasiStruk) {
        this.informasiStruk = informasiStruk;
    }
}
