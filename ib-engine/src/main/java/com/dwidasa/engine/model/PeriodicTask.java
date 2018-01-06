package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

public class PeriodicTask extends BaseObject implements Serializable {
    private static final long serialVersionUID = 520119236281549026L;

    private String periodType;
    private String taskName;
    private String className;
    private Integer executionOrder;
    private String running;
    private String status;
    private Date lastSuccessDate;
    private String trace;

    public PeriodicTask() {
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

    public String getRunning() {
        return running;
    }

    public void setRunning(String running) {
        this.running = running;
    }

    public Date getLastSuccessDate() {
        return lastSuccessDate;
    }

    public void setLastSuccessDate(Date lastSuccessDate) {
        this.lastSuccessDate = lastSuccessDate;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PeriodicTask)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        PeriodicTask that = (PeriodicTask) o;
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
