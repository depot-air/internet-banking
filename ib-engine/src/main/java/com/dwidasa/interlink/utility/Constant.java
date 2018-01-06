package com.dwidasa.interlink.utility;

/**
 * @author prayugo
 */
public final class Constant {

	public static final String MESSAGE_TIMEOUT = "timeout";
	public static final String LINK_TYPE_SOCKET = "socket";
	public static final String LINK_TYPE_DATA_QUEUE = "data-queue";
	public static final String LINK_TYPE_MESSAGE_QUEUE = "message-queue";
	
	public static final String MESAGE_TYPE_ISO8583 = "iso8583";
	public static final String MESAGE_TYPE_OTHER = "user-define";
	public static final String MESAGE_TYPE_XML = "xml";
	public static final String MESAGE_TYPE_FIX_LENGTH = "fixed-length";

	/** socket, data queue and message queue configuration file name */
	public static final String mFile = "/cfg/interlink-medium.xml";
	
	/** interlink base configuration file name */
	public static final String iFile = "/cfg/interlink-config.xml";
	
	/**
	 * check if message is of type network management request
	 * @param argMsg incoming message from other application
	 * @return true if this is a network management request and vice versa
	 */
	public static final boolean isNetworkRequest( String argMsg )
	{
		String mti = argMsg.substring( 0, 4 );

		return mti.equals( "0800" );
	}

	/**
	 * check if message is of type network management response
	 * @param argMsg incoming message from other application
	 * @return true if this is a network management response and vice versa
	 */
	public static final boolean isNetworkResponse( String argMsg )
	{
		String mti = argMsg.substring( 0, 4 );

		return mti.equals( "0810" );
	}
}
