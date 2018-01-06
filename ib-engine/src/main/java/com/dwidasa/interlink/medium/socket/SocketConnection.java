package com.dwidasa.interlink.medium.socket;

import com.dwidasa.interlink.Listener;

import org.apache.log4j.Logger;

/**
 * @author prayugo
 */
public class SocketConnection extends Thread {

    private Listener listener;
	private Connection provider;
    private SocketHandler handler;
    
    private boolean connected = true;

    private Logger logger = Logger.getLogger( SocketConnection.class );

    /**
     * default constructor of class SocketConnection
     * @param handler instance of connected socket 
     * @param provider
     * @param listener a message handler
     */
    public SocketConnection( SocketHandler handler, Connection provider, Listener listener ) {
    
//    	logger.info( "SocketConnection() initialization." );
    	
    	this.handler = handler;
        this.provider = provider;
        this.listener = listener;
    }
        
    /**
     * close the connection with external application
     */
    public void disconnect() {
    
//    	logger.info( "SocketConnection.disconnect(), close the connection with external application." );
    	
        connected = false;
        
        handler.shutdown();
        
        interrupt();
    }

    /**
     * transmit a message to the external application
     * @param Msg a message to be sent to the external application
     */
    public boolean transmit( String Msg ) {
    
        try {
        	
            return handler.transmit( Msg );
        }
        catch( Exception e ) {
        
        	if( connected ) {
        	
        		logger.error( "SocketConnection.transmit() exception on transmitting message. " + e.getMessage() );

        		connected = false;
        		provider.onDisconnect();
        	}
        }
        
        return false;
    }

    /**
     * implemented method of base class Thread
     */
    @Override
    public void run() {
 
//    	logger.info( "SocketConnection.run() started." );
    	
    	while( connected ) {
    		
    		try {
    			
    			String Msg = handler.receive();

    			if( ( Msg != null ) && ( Msg.length() > 2 ) ) {

    				logger.info( "rcv = " + Msg );
    				listener.onMessage( Msg );
    			}
    		}
    		catch( Exception e ) {
 	
    			if( connected ) {
 	
    				logger.error( "SocketConnection.run() exception on receiving message. " + e.getMessage() );

    				connected = false;
    				provider.onDisconnect();
    			}
    		}
    	}
    	
//    	logger.info( "SocketConnection.run() stoped." );
    }
    
}
