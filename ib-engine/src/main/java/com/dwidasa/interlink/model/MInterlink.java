package com.dwidasa.interlink.model;

/**
 * @author prayugo
 */
public class MInterlink {

	private long id;
	private int linkType;
	private long   echoPeriod;
	private long   monitorPeriod;
	private long   timeoutPeriod;
	private String digesterName;
	private String transporterName;
	private String messageType;
	private String isoPackager;
	private int    messageKeyPosition;
	private int    messageKeyLength;
	private String messageKeyElement;
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLinkType() {
		return linkType;
	}

	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

	public long getEchoPeriod() {
		return echoPeriod;
	}

	public void setEchoPeriod(long echoPeriod) {
		this.echoPeriod = echoPeriod;
	}
	
	public boolean isEchoApply() { 
		return echoPeriod > 0; 
	}

	public long getMonitorPeriod() {
		return monitorPeriod;
	}

	public void setMonitorPeriod(long monitorPeriod) {
		this.monitorPeriod = monitorPeriod;
	}

	public long getTimeoutPeriod() {
		return timeoutPeriod;
	}

	public void setTimeoutPeriod(long timeoutPeriod) {
		this.timeoutPeriod = timeoutPeriod;
	}

	public String getDigesterName() {
		return digesterName;
	}

	public void setDigesterName(String digesterName) {
		this.digesterName = digesterName;
	}

	public String getTransporterName() {
		return transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getIsoPackager() {
		return isoPackager;
	}

	public void setIsoPackager(String isoPackager) {
		this.isoPackager = isoPackager;
	}

	public int getMessageKeyPosition() {
		return messageKeyPosition;
	}

	public void setMessageKeyPosition(int messageKeyPosition) {
		this.messageKeyPosition = messageKeyPosition;
	}

	public int getMessageKeyLength() {
		return messageKeyLength;
	}

	public void setMessageKeyLength(int messageKeyLength) {
		this.messageKeyLength = messageKeyLength;
	}

	public String getMessageKeyElement() {
		return messageKeyElement;
	}

	public void setMessageKeyElement(String messageKeyElement) {
		this.messageKeyElement = messageKeyElement;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void dump() {
	
		System.out.println( "id : " + id );
		System.out.println( "linkType : " + linkType );
		System.out.println( "echoPeriod : " + echoPeriod );
		System.out.println( "monitorPeriod : " + monitorPeriod );
		System.out.println( "timeoutPeriod : " + timeoutPeriod );
		System.out.println( "digesterName : " + digesterName );
		System.out.println( "transporterName : " + transporterName );
		System.out.println( "messageType : " + messageType );
		System.out.println( "isoPackager : " + isoPackager );
		System.out.println( "messageKeyPosition : " + messageKeyPosition );
		System.out.println( "messageKeyLength : " + messageKeyLength );
		System.out.println( "messageKeyElement : " + messageKeyElement );
		System.out.println( "description : " + description );
		System.out.println();
	}

}
