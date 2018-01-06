package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 4:19 PM
 */
public class KioskPrinter extends BaseObject implements Serializable {
    private static final Long serialVersionUID = 1218649883933373944L;

    private Long id;
    private Long mKioskTerminalId;
    private Double strukCounter;
    private Long strukMaxId;
    private Long a4Counter;
    private Long a4CounterTape;
    private Long a4MaxId;
    private Long ssppCounterTape;
    private Long ssppMaxId;

    private String strukDesc;
    private String a4Desc;
    private String a4TapeDesc;
    private String ssppDesc;

    private KioskTerminal kioskTerminal;
    private String kioskTerminalDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getmKioskTerminalId() {
        return mKioskTerminalId;
    }

    public void setmKioskTerminalId(Long mKioskTerminalId) {
        this.mKioskTerminalId = mKioskTerminalId;
    }

    public KioskTerminal getKioskTerminal() {
        return kioskTerminal;
    }

    public void setKioskTerminal(KioskTerminal kioskTerminal) {
        this.kioskTerminal = kioskTerminal;
    }

    public String getKioskTerminalDesc() {
        return kioskTerminalDesc;
    }

    public void setKioskTerminalDesc(String kioskTerminalDesc) {
        this.kioskTerminalDesc = kioskTerminalDesc;
    }

    public Double getStrukCounter() {
        return strukCounter;
    }

    public void setStrukCounter(Double strukCounter) {
        this.strukCounter = strukCounter;
    }

    public Long getStrukMaxId() {
        return strukMaxId;
    }

    public void setStrukMaxId(Long strukMaxId) {
        this.strukMaxId = strukMaxId;
    }

    public Long getA4Counter() {
        return a4Counter;
    }

    public void setA4Counter(Long a4Counter) {
        this.a4Counter = a4Counter;
    }

    public Long getA4MaxId() {
        return a4MaxId;
    }

    public void setA4MaxId(Long a4MaxId) {
        this.a4MaxId = a4MaxId;
    }

    public Long getA4CounterTape() {
        return a4CounterTape;
    }

    public void setA4CounterTape(Long a4CounterTape) {
        this.a4CounterTape = a4CounterTape;
    }

    public Long getSsppCounterTape() {
        return ssppCounterTape;
    }

    public void setSsppCounterTape(Long ssppCounterTape) {
        this.ssppCounterTape = ssppCounterTape;
    }

    public Long getSsppMaxId() {
        return ssppMaxId;
    }

    public void setSsppMaxId(Long ssppMaxId) {
        this.ssppMaxId = ssppMaxId;
    }

    public String getStrukDesc() {
        return strukDesc;
    }

    public void setStrukDesc(String strukDesc) {
        this.strukDesc = strukDesc;
    }

    public String getA4Desc() {
        return a4Desc;
    }

    public void setA4Desc(String a4Desc) {
        this.a4Desc = a4Desc;
    }

    public String getA4TapeDesc() {
        return a4TapeDesc;
    }

    public void setA4TapeDesc(String a4TapeDesc) {
        this.a4TapeDesc = a4TapeDesc;
    }

    public String getSsppDesc() {
        return ssppDesc;
    }

    public void setSsppDesc(String ssppDesc) {
        this.ssppDesc = ssppDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KioskPrinter)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        KioskPrinter that = (KioskPrinter) o;
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
        return (kioskTerminal != null) ? kioskTerminal.getDescription() : "";
    }

}