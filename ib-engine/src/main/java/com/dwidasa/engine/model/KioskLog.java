package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 4:19 PM
 */
public class KioskLog extends BaseObject implements Serializable {
    private static final Long serialVersionUID = 1218649883933373944L;

    private Long id;
    private Long mKioskTerminalId;
    
    private String serviceType;
    private Date serviceTime;
    private String serviceResponse;
    private String description;

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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Date getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getServiceResponse() {
		return serviceResponse;
	}

	public void setServiceResponse(String serviceResponse) {
		this.serviceResponse = serviceResponse;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof KioskLog)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        KioskLog that = (KioskLog) o;
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