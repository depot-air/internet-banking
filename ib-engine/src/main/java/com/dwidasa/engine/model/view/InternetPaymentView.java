package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:34
 */
public class InternetPaymentView extends PaymentView {
    /**
     * Speedy flag, output field.
     */
    private String speedyFlag;

    /**
     * Bill amount, output field.
     */
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal amount3;

    /**
     * Bill period, from earliest month to latest month. Output field.
     */
    private List<Date> billPeriods;

    public InternetPaymentView() {
        super();
    }

    public String getSpeedyFlag() {
        return speedyFlag;
    }

    public void setSpeedyFlag(String speedyFlag) {
        this.speedyFlag = speedyFlag;
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

    public List<Date> getBillPeriods() {
        return billPeriods;
    }

    public void setBillPeriods(List<Date> billPeriods) {
        this.billPeriods = billPeriods;
    }

    public String getPaidPeriods(Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMyy", locale);
        String result = "";
        for (Date billPeriod : billPeriods) {
            result += sdf.format(billPeriod) + ",";
        }

        return result.substring(0, result.length() - 1);
    }
}
