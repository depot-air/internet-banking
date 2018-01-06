package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:39
 */
public class WaterPaymentView extends PaymentView {

    private String areaCode;
    private Date dueDate;
    private String dueDateDdMMyyyy;
    private String billReference;
    private Date startService;
    private Date endService;
    private Date billDate;
    private String startServiceDdMMyyyy;
    private String endServiceDdMMyyyy;
    private String billDateYyyyMMdd;
    private BigDecimal penalty;
    private String reserved1;
    private String reserved2;
    private BigDecimal jmlTagihan;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public Date getStartService() {
        return startService;
    }

    public void setStartService(Date startService) {
        this.startService = startService;
    }

    public Date getEndService() {
        return endService;
    }

    public void setEndService(Date endService) {
        this.endService = endService;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }
    
    public BigDecimal getJmlTagihan() {
		return jmlTagihan;
	}
    
    public void setJmlTagihan(BigDecimal jmlTagihan) {
		this.jmlTagihan = jmlTagihan;
	}

    public String getDueDateDdMMyyyy() {
        return dueDateDdMMyyyy;
    }

    public void setDueDateDdMMyyyy(String dueDateDdMMyyyy) {
        this.dueDateDdMMyyyy = dueDateDdMMyyyy;
    }

    public String getStartServiceDdMMyyyy() {
        return startServiceDdMMyyyy;
    }

    public void setStartServiceDdMMyyyy(String startServiceDdMMyyyy) {
        this.startServiceDdMMyyyy = startServiceDdMMyyyy;
    }

    public String getEndServiceDdMMyyyy() {
        return endServiceDdMMyyyy;
    }

    public void setEndServiceDdMMyyyy(String endServiceDdMMyyyy) {
        this.endServiceDdMMyyyy = endServiceDdMMyyyy;
    }

    public String getBillDateYyyyMMdd() {
        return billDateYyyyMMdd;
    }

    public void setBillDateYyyyMMdd(String billDateDdMMyyyy) {
        this.billDateYyyyMMdd = billDateDdMMyyyy;
    }

}
