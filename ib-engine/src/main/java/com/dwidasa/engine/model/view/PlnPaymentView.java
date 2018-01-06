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
 * Time: 11:01
 */
public class PlnPaymentView extends PaymentView {
    /**
     * Current meter (SAHLWBP of latest month), output field.
     */
    private String currentMeter;
    /**
     * Previous meter (SLALWBP of earliest month), output field.
     */
    private String previousMeter;
    /**
     * Meter reading date of latest month, output field.
     */
    private Date meterReadingDate;
    /**
     * Bill period, from earliest month to latest month. Output field.
     */
    private List<Date> billPeriods;
    /**
     * Biller reference number, output field.
     */
    private String billerReference;
    /**
     * Power category, output field.
     */
    private String powerCategory;
    /**
     * Number of oustanding bill, output field.
     */
    private Integer outstanding;

    /**
     * Outstanding amount to be paid, getting from RPTAG of each month.
     * First amount dedicated for earliest month. Output field.
     */
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal amount3;
    private BigDecimal amount4;

    /**
     * Penalty fee, sum of RPBK of all month, output field.
     */
    private BigDecimal penaltyFee;

    /**
     * PLN Service Unit Code; output field
     */
    private String unitCode;

    /**
     * PLN Service Unit Phone Number; output field
     */
    private String unitPhone;


    /**
     * Bit #61 of ISO message. Internal field.
     */
    private String bit61;

    private String traceNumber;
    private String informasiStruk;

    //  bsueb stand meter
    private String standMeter;

    // bsueb gspRef
    private String gspRef;

	public PlnPaymentView() {
        super();
    }

    public String getCurrentMeter() {
        return currentMeter;
    }

    public void setCurrentMeter(String currentMeter) {
        this.currentMeter = currentMeter;
    }

    public String getPreviousMeter() {
        return previousMeter;
    }

    public void setPreviousMeter(String previousMeter) {
        this.previousMeter = previousMeter;
    }

    public Date getMeterReadingDate() {
        return meterReadingDate;
    }

    public List<Date> getBillPeriods() {
        return billPeriods;
    }

    public void setBillPeriods(List<Date> billPeriods) {
        this.billPeriods = billPeriods;
    }

    public void setMeterReadingDate(Date meterReadingDate) {
        this.meterReadingDate = meterReadingDate;
    }

    public String getBillerReference() {
        return billerReference;
    }

    public void setBillerReference(String billerReference) {
        this.billerReference = billerReference;
    }

    public String getPowerCategory() {
        return powerCategory;
    }

    public void setPowerCategory(String powerCategory) {
        this.powerCategory = powerCategory;
    }

    public Integer getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(Integer outstanding) {
        this.outstanding = outstanding;
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

    public BigDecimal getAmount4() {
        return amount4;
    }

    public void setAmount4(BigDecimal amount4) {
        this.amount4 = amount4;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitPhone() {
        return unitPhone;
    }

    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone;
    }


    public String getBit61() {
        return bit61;
    }

    public void setBit61(String bit61) {
        this.bit61 = bit61;
    }

    public String getPaidPeriods(Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMyy", locale);
        String result = "";
        for (Date billPeriod : billPeriods) {
            result += sdf.format(billPeriod) + ",";
        }

        return result.substring(0, result.length() - 1);
    }

    public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    public String getInformasiStruk() {
        return informasiStruk;
    }

    public void setInformasiStruk(String informasiStruk) {
        this.informasiStruk = informasiStruk;
    }

    public String getStandMeter()
    {
        String bit48ValueOfBill = bit48.substring(103);
        standMeter = bit48ValueOfBill.substring(63, 71) + " - " + bit48ValueOfBill.substring(71, 79);
        return standMeter;
    }

    public void setStandMeter(String standMeter)
    {
        this.standMeter = standMeter;
    }

    public String getGspRef()
    {
        //gspRef = bit48.substring(4, 36);
        return gspRef;
    }

    public void setGspRef(String gspRef)
    {
        this.gspRef = gspRef;
    }

}
