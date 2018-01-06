package com.dwidasa.interlink.model;

public class MIGateBillerProduct {
	private String billerCode;
    private String productCode;
    private String industrialCode;
    private String iGateBillerCode;
    private String iGateProductCode; 

    public String getBillerCode() {
		return billerCode;
	}
	public void setBillerCode(String billerCode) {
		this.billerCode = billerCode;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getIndustrialCode() {
		return industrialCode;
	}
	public void setIndustrialCode(String industrialCode) {
		this.industrialCode = industrialCode;
	}
	public String getiGateBillerCode() {
		return iGateBillerCode;
	}
	public void setiGateBillerCode(String iGateBillerCode) {
		this.iGateBillerCode = iGateBillerCode;
	}
	public String getiGateProductCode() {
		return iGateProductCode;
	}
	public void setiGateProductCode(String iGateProductCode) {
		this.iGateProductCode = iGateProductCode;
	}
}
