package com.dwidasa.interlink.digester;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.Digester;
import com.dwidasa.interlink.dao.InterlinkDao;
import com.dwidasa.interlink.model.MInterlink;

/**
 * @author prayugo
 */
public class FixDigester implements Digester {

	private int start;
	private int length;
	
	/**
	 * default constructor for class FixDigester
	 */
	public FixDigester() {
	
	}
	
	public FixDigester( int start, int length ) {
		
		this.start = start;
		this.length = length;
	}

	/**
	 * determine the key or unique identifier of specified message
	 * @param Msg interface message contains of business process data
	 * @return the unique identifier of specified message
	 */
	public String getKey( String Msg ) {
	
		if( Msg != null ) {
			
			if( Msg.length() > ( start + length ) ) {
				
				return Msg.substring( start, length );
			}
		}
		
		return null;
	}
	
	/**
	 * prepare configuration and start the service
	 * @return just return true if starting process was success and vice versa
	 */
	public boolean startup() {
	
		try {
			
	    	InterlinkDao dao = ( InterlinkDao ) ServiceLocator.getService( "interlinkDao" );
	    	MInterlink model = dao.getInterlink();
			
			start = model.getMessageKeyPosition();
			length = model.getMessageKeyLength();
			
			return true;
		}
		catch( Exception e ) {
			
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * create a new echo message to be sent.
	 * echo message used to monitor network connection
	 * @return new generate echo message
	 */
	public String crtEcho() {

		return null;
	}

	/**
	 * create response for request echo message
	 * @param argMsg incoming message received
	 * @return response message for echo request
	 */
	public String crtEchoReply( String argMsg ) {
	
		return null;
	}
	
	/**
	 * check whether this is an mti of network management
	 * @param Msg internal message representation
	 * @return true if mti is an mti of network management
	 */
	public boolean isNetwork( String argMsg ) {
		
		return false;
	}
	
	/**
	 * check if Mti indicates a request message
	 * @param Msg internal message representation
	 * @return true if Mti indicates a request message
	 */
	public boolean isRequest( String argMsg ) {
		
		return true;
	}

}
