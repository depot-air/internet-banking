package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/6/12
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class KioskPaper extends BaseObject implements Serializable {
    private static final long serialVersionUID = 1221649883933373944L;

    public static class PRINTER_TYPE {
	    public static final Integer STRUK = 1;
	    public static final Integer MUTASI = 2;
	    public static final Integer SSPP = 3;

	    public static final String STRUK_DESC = "Kertas Struk";
	    public static final String MUTASI_DESC = "Kertas Mutasi";
	    public static final String SSPP_DESC = "SSPP";
    }

    public static class COUNTER_TYPE {
	    public static final Integer STRUK = 1;
	    public static final Integer MUTASI_STRUK = 2;
        public static final Integer MUTASI_PITA = 3;
	    public static final Integer SSPP = 4;
    }
    private Long id;
    private Integer printerType;   //1=Struk, 2=Mutasi, 3=SSPP
    private String description;
    private Long maxPaper;
    private Long maxTape;

    private String printerTypeDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrinterType() {
        return printerType;
    }

    public void setPrinterType(Integer printerType) {
        this.printerType = printerType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMaxPaper() {
        return maxPaper;
    }

    public void setMaxPaper(Long maxPaper) {
        this.maxPaper = maxPaper;
    }

    public Long getMaxTape() {
        return maxTape;
    }

    public void setMaxTape(Long maxTape) {
        this.maxTape = maxTape;
    }

    public String getPrinterTypeDesc() {
        if (printerType == PRINTER_TYPE.STRUK ) return PRINTER_TYPE.STRUK_DESC;
        else if (printerType == PRINTER_TYPE.MUTASI ) return PRINTER_TYPE.MUTASI_DESC;
        return PRINTER_TYPE.SSPP_DESC;
    }

    public void setPrinterTypeDesc(String printerTypeDesc) {
        if (printerType == PRINTER_TYPE.STRUK ) this.printerTypeDesc = PRINTER_TYPE.STRUK_DESC;
        else if (printerType == PRINTER_TYPE.MUTASI ) this.printerTypeDesc = PRINTER_TYPE.MUTASI_DESC;
        else
        this.printerTypeDesc = PRINTER_TYPE.SSPP_DESC;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KioskPaper)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        KioskPaper that = (KioskPaper) o;
        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public String toString() {
        return (getDescription() != null) ? getDescription() : "";
    }
}
