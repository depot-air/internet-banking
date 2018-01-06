package com.dwidasa.engine.model.view;

/**
 * Result view for all operation in this application.
 *
 * @author rk
 */
public class ResultView {
    /**
     * Status of operation / task, 00 means success otherwise failed.
     */
    protected String responseCode;
    /**
     * Reference number associated with current operation / task.
     */
    protected String referenceNumber;

    public ResultView() {
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
