package com.dwidasa.ib.common;
import java.io.Serializable;
/**
 * Budled information related to account.
 * @author rk
 */
public class AccountInfo implements Serializable {
    private String accountNumber;
    private String cardNumber;
    private String accountType;
    private String currencyCode;
    private String isDefault;
//    private transient String accountNumber;
//    private transient String cardNumber;
//    private transient String accountType;
//    private transient String currencyCode;
//    private transient String isDefault;
    private static final long serialVersionUID = 1L;
//    private transient SomeObject thisWillNotBeSerialized;
    public AccountInfo() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String aDefault) {
        isDefault = aDefault;
    }
}
