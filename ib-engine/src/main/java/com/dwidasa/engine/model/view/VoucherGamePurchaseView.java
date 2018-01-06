package com.dwidasa.engine.model.view;

import com.dwidasa.engine.model.CustomerRegister;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 9/27/12
 */
public class VoucherGamePurchaseView extends VoucherPurchaseView {
    private String voucherName;
    private String currencyConversion;
    private String voucherCode;
    private String voucherSerial;
    private String billReference;

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getCurrencyConversion() {
        return currencyConversion;
    }

    public void setCurrencyConversion(String currencyConversion) {
        this.currencyConversion = currencyConversion;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherSerial() {
        return voucherSerial;
    }

    public void setVoucherSerial(String voucherSerial) {
        this.voucherSerial = voucherSerial;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }
}
