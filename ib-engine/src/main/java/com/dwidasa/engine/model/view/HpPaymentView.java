package com.dwidasa.engine.model.view;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:38
 */
public class HpPaymentView extends PaymentView {
    /**
     * Number of bill, output field.
     */
    private Integer numOfBill;

    /**
     * Amount of bill 1-3, output field.
     */
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal amount3;

    /**
     * Reference number bill 1-3, output field.
     */
    private String ref1;
    private String ref2;
    private String ref3;

    public HpPaymentView() {
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

    public String getRef1() {
        return ref1;
    }

    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    public String getRef2() {
        return ref2;
    }

    public void setRef2(String ref2) {
        this.ref2 = ref2;
    }

    public String getRef3() {
        return ref3;
    }

    public void setRef3(String ref3) {
        this.ref3 = ref3;
    }

}
