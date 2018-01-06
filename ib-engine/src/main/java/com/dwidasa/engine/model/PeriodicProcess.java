package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PeriodicProcess extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5219339720008548183L;

	private String periodType;
	private Date processDate;
	private Date startTime;
	private Date finishTime;
	private String status;
	
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	public Date getProcessDate() {
		return processDate;
	}
	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PeriodicProcess)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        PeriodicProcess that = (PeriodicProcess) o;
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
		return String.valueOf(getId());
	}
	
}
