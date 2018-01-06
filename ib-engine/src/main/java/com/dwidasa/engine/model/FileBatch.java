package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class FileBatch extends BaseObject implements Serializable {
    private static final long serialVersionUID = 7838121615366990119L;

	private String fileName;
	private String fileBin;
	private int status;//0=pending,1=approve,-1=reject,9=inserted
	private Date uploadDate;
	private Date notifDate;
	private Date endDate;
	private Date startDate;

    public FileBatch() {
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

    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileBin() {
		return fileBin;
	}

	public void setFileBin(String fileBin) {
		this.fileBin = fileBin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Date getNotifDate() {
		return notifDate;
	}

	public void setNotifDate(Date notifDate) {
		this.notifDate = notifDate;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof FileBatch)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        FileBatch that = (FileBatch) o;
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
    
    public static enum BatchStatus {

        pending(0, "Pending"),
        approve(1, "Telah Disetujui"),
        reject(-1, "Ditolak"),
        executed(9, "Sudah Eksekusi");
        
        private final int id;
        private final String ket;

        BatchStatus(int id, String ket) {
            this.id = id;
            this.ket = ket;
        }

        public int getId() {
            return id;
        }

        public String getKet() {
            return ket;
        }
    }

    public BatchStatus getStatusEnum() {
        for (BatchStatus bs : BatchStatus.values()) {
            if (bs.getId() == this.status) {
                return bs;
            }
        }
        return null;
    }

    public void setStatusEnum(BatchStatus status) {
        this.status = status.getId();
    }

    public String getStatusNama() {
    	BatchStatus js = getStatusEnum();
        if (js != null) {
            return js.getKet();
        } else {
            return "";
        }
    }
}