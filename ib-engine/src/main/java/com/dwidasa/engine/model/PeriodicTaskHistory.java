package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

public class PeriodicTaskHistory extends BaseObject implements Serializable {
    private static final long serialVersionUID = 6034050410590858094L;

    private Long periodicProcessId;
    private String periodType;
    private String taskName;
    private String className;
    private Integer executionOrder;
    private String status;
    private Date startTime;
    private Date finishTime;
    private String trace;

    public PeriodicTaskHistory() {
    }
    
    public Long getPeriodicProcessId() {
		return periodicProcessId;
	}

	public void setPeriodicProcessId(Long periodicProcessId) {
		this.periodicProcessId = periodicProcessId;
	}

	public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

	public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PeriodicTaskHistory)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        PeriodicTaskHistory that = (PeriodicTaskHistory) o;
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
