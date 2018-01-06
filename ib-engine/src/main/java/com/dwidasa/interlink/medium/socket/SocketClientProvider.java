package com.dwidasa.interlink.medium.socket;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.Listener;
import com.dwidasa.interlink.dao.SocketDao;
import com.dwidasa.interlink.model.MSocket;
import com.dwidasa.interlink.utility.Monitor;
import com.dwidasa.interlink.utility.MonitorService;
import org.apache.log4j.Logger;

import java.net.Socket;

/**
 * @author prayugo
 */
public class SocketClientProvider implements Connection, Monitor {

	private MSocket model;
	
	private Listener listener;
	private SocketConnection session;
	
	private boolean disconnect = false;

	private SocketDao socketDao;
	private MonitorService monitor;
	
	private Logger logger = Logger.getLogger( SocketClientProvider.class  );
	
	/**
	 * default constructor for class SocketClientProvider
	 * @param model socket configuration
	 * @param listener a message handler
	 */
	public SocketClientProvider( MSocket model, Listener listener ) {
		
		logger.info( "SocketClientProvider() initialization" );
		
		this.model = model;
		this.listener = listener;
		
		this.socketDao = ( SocketDao ) ServiceLocator.getService( "socketDao" );
	}
	
    /**
	 * open the connection with external application
	 * @return true if able to connect or vice versa
	 */
	@Override
    public boolean connect() {
    	
    	logger.info( "SocketClientProvider.connect()" );
    	
    	monitor = new MonitorService( this, model.getMonitorPeriod() );
    	monitor.startMonitor();
    	
    	return true;
    }

    /**
     * close the connection with external application
     */
	@Override
    public void disconnect() {
    
    	logger.info( "SocketClientProvider.disconnect()" );
    	
    	disconnect = true;
    	monitor.stopMonitor();
    	
    	socketDao.setConnection( model, false );
    	
    	if( session != null ) {
    		
    		session.disconnect();
    		session = null;
    	}
    }

    /**
     * implemented method will be automatically called by Transport
     * whenever a connection to external application just connected
     */
	@Override
    public void onConnect() {
    	
//    	logger.info( "SocketClientProvider.onConnect()" );
    	
    	socketDao.setConnection( model, true );
    }

    /**
     * implemented method will be automatically called by Transport
     * whenever an exception that caused connection to be closed to the
     * external application occurred
     */
	@Override
    public void onDisconnect() {
    
//    	logger.info( "SocketClientProvider.onDisonnect()" );
    	
    	socketDao.setConnection( model, false );
		
    	if( disconnect == false ) {
        	if( session != null ) {

            	session.disconnect();
            	session = null;
        	}
    	}
    }

    /**
	 * transmit a message to the external application
     * @param Msg message to be sent to external application
	 * @return just return true if process was success and vice versa
	 */
	@Override
	public boolean transmit( String Msg ) {
		
		if( connected() ) {
			
			return session.transmit( Msg );
		}
		
		return false;
	}

	/**
	 * notify implemented class now
	 */
	public void monitor() {
	
		if( !disconnect && !connected() ) {
			
    		try {
/*
                if (model.getServerPort() == 14001) {
                     model.setServerPort(14005);
                }
*/
    			Socket s = new Socket( model.getServerAddress(), model.getServerPort() );
	    		SocketHandler handler = ( SocketHandler ) Class.forName( model.getSocketDriver() ).newInstance();
	    		handler.setSocket( s );

	            session = new SocketConnection( handler, this, listener );
	            session.start();

	            onConnect();
	            
	            logger.info( "connected to [ " + model.getServerAddress() + ":" + model.getServerPort() + " ]" );
    		}
    		catch( Exception e ) {
    		
    			String m = "Unable to create connection to host : " + model.getServerAddress() +
    			           ", port : " + model.getServerPort();
    			
    			logger.error( m + ", message : " + e.getMessage() );
    		}
		}
	}

    /**
     * check the availability of the connection with external application
     * @return always return true whenever connection still exist
     */
    private boolean connected() {
    
        return ( session != null );
    }

}

