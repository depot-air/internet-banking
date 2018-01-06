package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:39
 */
public class TvPaymentView extends PaymentView {
	 /**
     * Number of bill, output field.
     */
    private Integer numOfBill;

    /**
     * Bill period, smaller number for earlier month. Output field.
     */
    private Date billPeriod1;
    private Date billPeriod2;
    private Date billPeriod3;

    /**
     * Amount of bill 1-3, output field.
     */
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal amount3;   

    public Date getBillPeriod1() {
        return billPeriod1;
    }

    public void setBillPeriod1(Date billPeriod1) {
        this.billPeriod1 = billPeriod1;
    }

    public Date getBillPeriod2() {
        return billPeriod2;
    }

    public void setBillPeriod2(Date billPeriod2) {
        this.billPeriod2 = billPeriod2;
    }

    public Date getBillPeriod3() {
        return billPeriod3;
    }

    public void setBillPeriod3(Date billPeriod3) {
        this.billPeriod3 = billPeriod3;
    }

    public Integer getNumOfBill() {
        return numOfBill;
    }

    public void setNumOfBill(Integer numOfBill) {
        this.numOfBill = numOfBill;
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public BigDecimal getAmount3() {
        return amount3;
    }

    public void setAmount3(BigDecimal amount3) {
        this.amount3 = amount3;
    }

}
