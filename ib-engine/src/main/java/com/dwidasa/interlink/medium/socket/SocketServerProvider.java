package com.dwidasa.interlink.medium.socket;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.Listener;
import com.dwidasa.interlink.dao.SocketDao;
import com.dwidasa.interlink.model.MSocket;
import org.apache.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * A convenience class that owns socket server and socket listener
 * Methods in this class delegates their works to those instances.
 * 
 * @author prayugo
 */
public class SocketServerProvider extends Thread implements Connection {

	private MSocket model;
	private ServerSocket socket;
	
	private boolean active = false;
	private boolean disconnect = false;
	
	private Listener listener;
	private SocketDao socketDao;
	private SocketConnection session;
	
	private Logger logger = Logger.getLogger( SocketServerProvider.class );
	
	/**
	 * default constructor for class SocketServerProvider
	 * @param model socket configuration
	 * @param listener handler for socket events
	 */
	public SocketServerProvider( MSocket model, Listener listener ) {
		
		logger.info( "SocketServerProvider() initialization" );
		
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
    	
    	logger.info( "SocketServerProvider.connect()" );
    	
		try
        {
            socket = new ServerSocket( model.getServerPort() );
        	socket.setSoTimeout( 5000 );

            active = true;

            logger.info( "socket server listening on port : " + model.getServerPort() );
            
            start();   
            
            return true;
        }
        catch( Exception e ) {
        
            logger.error( "Unable to start socket server on port : " + model.getServerPort() );
        }

        return false; 	
    }

    /**
     * close the connection with external application
     */
	@Override
    public void disconnect() {
    	
    	logger.info( "SocketServerProvider.disconnect()" );
    	
        active = false;
        disconnect = true;
        
        socketDao.setConnection( model, false );
        
        if( session != null ) {

        	session.disconnect();
        	session = null;
    	}

        interrupt();
    }

    /**
     * implemented method will be automatically called by Transport
     * whenever a connection to external application just connected
     */
	@Override
    public void onConnect() {
    
    	logger.info( "SocketServerProvider.onConnect()" );
    	
    	socketDao.setConnection( model, true );
    }

    /**
     * implemented method will be automatically called by Transport
     * whenever an exception that caused connection to be closed to the
     * external application occurred
     */
	@Override
    public void onDisconnect() {
    	
    	logger.info( "SocketServerProvider.onDisonnect( id )" );

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
     * implemented method of base class Thread
     */
	@Override
    public void run() {
    
        logger.info( "SocketServerProvider.run() server socket started ..." );
        
        Socket s = null; String r = null;

    	while( active ) {
        
            try {
            
                // -- waiting connection from clients : every 5 seconds
                s = socket.accept();

                logger.info( "receives connection from client = " + r );
                
                // -- recheck status after some seconds of sleeps in server socket
                if( active && ( s != null ) ) {
                
                    r = "[ " + s.getInetAddress() + " : " + s.getPort() + " ]";

                    if( session == null ) {
                    
                        SocketHandler handler = ( SocketHandler ) Class.forName( model.getSocketDriver() ).newInstance();
                        handler.setSocket( s );

                        session = new SocketConnection( handler, this, listener );
                        session.start();
        	            
        	            onConnect();
                    }
                    else {
                    
                        logger.error( "Request rejected. Existing connection already established !!" );
                    }
                }
            }
            catch( SocketTimeoutException e ) { }
            catch( Exception e ) {
            
                logger.error( "Exception while receiving incoming connection." );
            }
        }

        logger.info( "SocketServerProvider.run() server socket stopped ..." );

        if( s != null ) {
        	
        	try {
        		s.close();
        	}
        	catch( Exception e ) {
        		
        	}
        	s = null;
        }

        try { 
        	socket.close(); 
        } 
        catch( Exception e ) {
        	
        }
        socket = null;
    }

    /**
     * check the availability of the connection with external application
     * @return always return true whenever connection still exist
     */
    private boolean connected() {
    
        return ( session != null );
    }

}


