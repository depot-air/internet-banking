package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Inbox extends BaseObject implements Serializable {
    private static final long serialVersionUID = 7838121615366990119L;

	private String content;
	private Date endDate;
	private Date startDate;
	private String title;
	private boolean forAll;
	

    private Set<InboxCustomer> inboxCustomers;

    public Inbox() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<InboxCustomer> getInboxCustomers() {
        return inboxCustomers;
    }

    public void setInboxCustomers(Set<InboxCustomer> inboxCustomers) {
        this.inboxCustomers = inboxCustomers;
    }
    
    public boolean isForAll() {
		return forAll;
	}
    
    public void setForAll(boolean all) {
		this.forAll = all;
	}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Inbox)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Inbox that = (Inbox) o;
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