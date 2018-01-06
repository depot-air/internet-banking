package com.dwidasa.engine.model.airline;

public class PacBook {
	private String pnrIdDepart;
	private String pnrIdReturn;
	private PacRetrieve retrieveDepart;
	private PacRetrieve retrieveReturn;
	
	public String getPnrIdDepart() {
		return pnrIdDepart;
	}
	public void setPnrIdDepart(String pnrIdDepart) {
		this.pnrIdDepart = pnrIdDepart;
	}
	public String getPnrIdReturn() {
		return pnrIdReturn;
	}
	public void setPnrIdReturn(String pnrIdReturn) {
		this.pnrIdReturn = pnrIdReturn;
	}
	public PacRetrieve getRetrieveDepart() {
		return retrieveDepart;
	}
	public void setRetrieveDepart(PacRetrieve retrieveDepart) {
		this.retrieveDepart = retrieveDepart;
	}
	public PacRetrieve getRetrieveReturn() {
		return retrieveReturn;
	}
	public void setRetrieveReturn(PacRetrieve retrieveReturn) {
		this.retrieveReturn = retrieveReturn;
	}	
	
}
