package com.dwidasa.interlink.utility;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.dao.InterlinkDao;
import com.dwidasa.interlink.dao.SocketDao;
import com.dwidasa.interlink.model.MInterlink;
import com.dwidasa.interlink.model.MSocket;

/**
 * @author prayugo
 */
public final class InterParser 
{
	// -- list of all object configuration
    private MInterlink interlink;
    private MSocket socket;
    
    /**
     * parse all interlink configuration file
     * @return just return true whenever preparation steps are success and vice versa
     */
    public boolean parse() 
	{
    	InterlinkDao idao = ( InterlinkDao ) ServiceLocator.getService("interlinkDao");
    	SocketDao sdao = ( SocketDao ) ServiceLocator.getService( "socketDao" );
    	
    	interlink = idao.getInterlink();
    	socket = sdao.getSocket( interlink.getId() );
		
    	System.out.println( "[ interlink-configuration ]" );
    	interlink.dump();

    	System.out.println( "[ interlink-socket-configuration ]" );
    	socket.dump();
    	
		return true;
	}
	
	/**
	 * get the main configuration of interlink
	 * @return object contain of interlink configuration
	 */
	public MInterlink getInterObject() { return interlink; }
	
	/**
	 * get the socket configuration of interlink
	 * @return object contain of socket interlink configuration
	 */
	public MSocket getSocketObject() { return socket; }
}
