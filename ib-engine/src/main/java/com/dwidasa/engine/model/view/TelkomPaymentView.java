package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:18
 */
public class TelkomPaymentView extends PaymentView {
    /**
     * Number of bill, output field.
     */
    private Integer numOfBill;

    /**
     * Reference number bill 1-3, output field.
     */
    private String ref1;
    private String ref2;
    private String ref3;

    /**
     * Bill period, smaller number for earlier month. Output field.
     */
    private Date billPeriod1;
    private Date billPeriod2;
    private Date billPeriod3;

    /**
     * Outstanding amount of each month, smaller number for earlier month.
     * Output field.
     */
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal amount3;

    public TelkomPaymentView() {
        super();
    }

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

    public Integer getNumOfBill() {
        return numOfBill;
    }

    public void setNumOfBill(Integer numOfBill) {
        this.numOfBill = numOfBill;
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
    
    public String getTglPeriods(Locale locale){
    	
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMyy", locale);
    	
    	if(billPeriod2 == null){
    		return simpleDateFormat.format(billPeriod1);
    	}else if(billPeriod3 == null){
    		return simpleDateFormat.format(billPeriod1)+", "+simpleDateFormat.format(billPeriod2);
    	}else{
    		return simpleDateFormat.format(billPeriod1)+", "+simpleDateFormat.format(billPeriod2)+", "+simpleDateFormat.format(billPeriod3);
    	}
    	
    	
    }
    
}
