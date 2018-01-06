package com.dwidasa.interlink.model;

/**
 * @author prayugo
 */
public class MSocket {

	private long id;
	private long interlinkId;
    private int    connectionType;  		// -- connection type 1 <server>, 0 <client>
    private String serverAddress;  			// -- socket server address
    private int    serverPort;  			// -- socket server port
    private long   monitorPeriod;  			// -- socket monitor period in seconds
    private String socketDriver;  			// -- socket driver implementation class
    private String description;  			// -- description
    private String status;					// -- connection status. Y:connected, N: disconnected

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getInterlinkId() {
		return interlinkId;
	}

	public void setInterlinkId(long interlinkId) {
		this.interlinkId = interlinkId;
	}

	public int getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(int connectionType) {
		this.connectionType = connectionType;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public long getMonitorPeriod() {
		return monitorPeriod;
	}

	public void setMonitorPeriod(long monitorPeriod) {
		this.monitorPeriod = monitorPeriod;
	}

	public String getSocketDriver() {
		return socketDriver;
	}

	public void setSocketDriver(String socketDriver) {
		this.socketDriver = socketDriver;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void dump() {

		System.out.println( "id : " + id );
		System.out.println( "interlinkId : " + interlinkId );
		System.out.println( "connectionType : " + connectionType );
		System.out.println( "serverAddress : " + serverAddress );
		System.out.println( "serverPort : " + serverPort );
		System.out.println( "monitorPeriod : " + monitorPeriod );
		System.out.println( "socketDriver : " + socketDriver );
		System.out.println( "description : " + description );
		System.out.println( "status : " + status );
		System.out.println();
	}

} 
