package com.dwidasa.interlink;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.dao.InterlinkDao;
import com.dwidasa.interlink.medium.Transporter;
import com.dwidasa.interlink.model.MInterlink;
import com.dwidasa.interlink.utility.Constant;
import com.dwidasa.interlink.utility.Monitor;
import com.dwidasa.interlink.utility.MonitorService;

/**
 * notes:
 *  - 1 (one) feature of this class is periodically sending an echo
 *    message to external application. this functionality used to
 *    check network connection.
 *    
 *  - be aware, that any response from our request must be handle properly
 *    and this kind of handle should be sited in Hook Handler.
 *    ( for reply on our echo message )
 *    
 *  - another functionality to be implemented in Hook Handler is
 *    generating network echo reply.
 * 
 * benchmark : 10k in 7 seconds +- 1428
 * @author prayugo
 */
@Component( "serviceCaller" )
public final class ServiceCaller implements Listener, Monitor {
    private ConcurrentMap< String, Caller > caller = new ConcurrentHashMap< String, Caller >();
    private ConcurrentMap< String, Long > timeout = new ConcurrentHashMap< String, Long >();

    private Digester digester; 
    private Transporter transporter;
    
    private ServiceHook hook;
    private MonitorService monitor;

    private MInterlink interlink;
    private boolean monitorInProcess = false;
    
    private long lastEcho = System.currentTimeMillis();
    private Logger logger = Logger.getLogger( ServiceCaller.class );
    
    /**
     * start the ServiceCaller service
     */
	@PostConstruct
    public void startService() {
    	
		logger.info( "ServiceCaller.startService()" );
		
		InterlinkDao dao = ( InterlinkDao ) ServiceLocator.getService( "interlinkDao" );
		interlink = dao.getInterlink();

		digester = getDigester();
		transporter = getTransporter();

		monitor = new MonitorService( this, interlink.getMonitorPeriod() );
		monitor.startMonitor();
	}
    
    /**
     * stop the ServiceCaller service
     */
	@PreDestroy
    public void stopService() {
    	
		logger.info( "ServiceCaller.stopService()" );
		
		monitor.stopMonitor();
		transporter.shutdown();
    }
    
	/**
	 * initialize the message digester class
	 * @return an instance of which implements Digester interface
	 */
	private Digester getDigester() { 
	
		String clazz = interlink.getDigesterName().trim();
		
		try {
		
			Digester d = ( Digester ) Class.forName( clazz ).newInstance();
			d.startup();
			
			return d;
		} 
		catch( Exception e ) {
		
			logger.error( "unable to instantiate message digester : " + clazz, e );
		}
		
		return null;
	}

	/**
	 * initialize the message transporter class
	 * @return an instance of which implements Transporter interface
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Transporter getTransporter() {
	
		String clazz = interlink.getTransporterName().trim();
		
		try {
		
			Class cs = Class.forName( clazz );

			Constructor cr = cs.getConstructor( new Class[]{ Listener.class } );
        	Transporter t = ( Transporter ) cr.newInstance( new Object[] { this } );
			
        	t.startup();
			
			return t;
		} 
		catch( Exception e ) {
		
			logger.error( "unable to instantiate message transporter : " + clazz, e );
		}
		
		return null;
	}

    /**
     * register a hook instance to handle late response as well as
     * handling request message from other application instead of our owns
     * @param argHook a registered ServiceHook instance
     */
    public void registerHook( ServiceHook argHook ) { 
    	
    	hook = argHook; 
    }
    
    /**
     * send message to other application and waits for reply. reply mechanism
	 * will be trigger and handled through method onMessage( String )
     * @param argCaller caller program which want to sending message
     * @param argMsg interface message contains of business process data
	 * @return just return true if process was success and vice versa
	 * @throws Exception throws at least duplicate key exception
     */
    public boolean fireAndWait( Caller argCaller, String argMsg ) throws Exception {

    	String key = "";
    	
    	if( isIsoMsg( argMsg ) ) {												// -- iso message
    		
        	key = digester.getKey( argMsg );
    	} 
    	else if( isJsonMsg( argMsg ) ) {										// -- json message
    		
        	key = "2,3,4,7,11,12,13,18,29,37,39,41,56,102,48";
        	
    	}
    	else {																	// -- portfolio message
    		
    		key = argMsg.substring( 0, 34 );
    	}
    	
    	if( !isJsonMsg( argMsg ) ) {
    		if( caller.containsKey( key ) ) {
    	    	
        		logger.error( "duplicate key not allowed : " + key );
        		
        		throw new Exception( "duplicate key not allowed for key : " + key );
        	}
        	
        	caller.put( key, argCaller );
        	timeout.put( key, new Long( System.currentTimeMillis() ) );
        	
        	boolean r = transporter.transmit( argMsg );

        	if( r == false ) {

    			timeout.remove( key );
    			caller.remove( key );
        	}
        	return r;
    	} else { //jsonMessage
    		if( caller.containsKey( key ) ) {
    	    	logger.error( "duplicate key not allowed : " + key );
        		throw new Exception( "duplicate key not allowed for key : " + key );
        	}
        	caller.put( key, argCaller );
        	timeout.put( key, new Long( System.currentTimeMillis() ) );
        	boolean r = transporter.transmit( argMsg );
        	if( r == false ) {
    			timeout.remove( key );
    			caller.remove( key );
        	}
        	return r;
    	}
    	
    	
    }
    
    /**
     * send a message to other application without requesting a reply
     * @param argMsg interface message contains of business process data
	 * @return just return true if process was success and vice versa
     */
    public boolean fireAndForget( String argMsg ) {
    
        return transporter.transmit( argMsg );
    }

	/**
     * this method would be called automatically on each incoming message  
     * @param argMsg interface message contains of business process data 
     */
    public void onMessage( final String argMsg ) {
    
    	if( Constant.MESAGE_TYPE_ISO8583.equals( interlink.getMessageType() ) ) {
    		if( digester.isNetwork( argMsg ) ) {
    			if( digester.isRequest( argMsg ) ) {

    				String r = digester.crtEchoReply( argMsg );

    				fireAndForget( r );
    			}

    			return;
    		}
		}

    	String key = "";
    	if( isIsoMsg( argMsg ) ) {												// -- iso message
    		
        	key = digester.getKey( argMsg );
    	}
    	else if( isJsonMsg( argMsg ) ) {										// -- json message
    		
        	key = "2,3,4,7,11,12,13,18,29,37,39,41,56,102,48";
        	
    	}
    	else {																	// -- portfolio message
    		
    		key = argMsg.substring( 0, 34 );
    	}
    	
		Caller c = caller.get( key );

		if( ( c != null ) ) {
			synchronized( c ) {
			
				c.setMessage( argMsg );
				c.notify();
			}
		
			timeout.remove( key );											// -- release all resources
			caller.remove( key );
		}
		else {

			// -- treated via hook mechanism. this cases could be
			// -- triggered by several conditions as listed below :
			// --   1. message initiation is come from other application
			// --   2. late response due to timeout. timeout handled internally
			
			if( hook != null ) {
				logger.info("START process message separate thread");
				(new Thread(
					new Runnable() {		
						@Override
						public void run() {
							hook.onMessage( argMsg );							
						}
					}
				)).start();
				logger.info("END process message separate thread");

			}
		}
    }

    /**
     * notification coming from ServiceMonitor class.
     * performing monitoring task for this instance if needed.
     * 
     * check all caller instance in the container for timeout. any 
     * timeout instance shall be returned immediately to the caller
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void monitor() { 
	
		if( monitorInProcess == false ) {
			monitorInProcess = true;

			try {
				
				sendEcho();
				
				ConcurrentMap< String, Long > ctimeout = null;					// -- create a shallow copy for processing
				synchronized( this ) {
				
					ctimeout = new ConcurrentHashMap(timeout);				// -- cloned timeout object
				}
				
				long ct = System.currentTimeMillis();						// -- current time
				Iterator< String > it = ctimeout.keySet().iterator();
			
				while( it.hasNext() ) {
				
					String key = it.next();
					long st = ctimeout.get( key ).longValue();				// -- start time
					//PLN Prepaid timeoutnya berbeda untuk keperluan manual advice
					long timeoutPeriod = interlink.getTimeoutPeriod();
					String transactionType = key.substring(0,2);					
					if (transactionType.equals(Constants.PLN_PURCHASE_CODE)) {
						timeoutPeriod = 125;
					}
					if( ( ( ct-st )/1000 ) > timeoutPeriod ) {
					
						Caller c = caller.get( key );
						
						if( c != null ) {
							synchronized( c ) {
							
								c.setMessage( Constant.MESSAGE_TIMEOUT );
								c.notify();
							}
						}
	
						timeout.remove( key );								// -- release all resources
						caller.remove( key );
					}
				}
			}
			catch( Exception e ) {
				
				logger.error( "ServiceCaller.exception on monitor()", e );
			}
			
			monitorInProcess = false;
		}
	}
	
	/**
	 * periodically send echo message to external application
	 * use this functionality to check network connection
	 */
	private void sendEcho() {
	
		if( interlink.isEchoApply() ) {
			
			long ct = System.currentTimeMillis();
			long et = ( ( ct - lastEcho ) / 1000 );
			
			if( et > interlink.getEchoPeriod() ) {
			
				lastEcho = ct;
				fireAndForget( digester.crtEcho() );
			}
		}
	}
	
	private boolean isIsoMsg( String argMsg ) {
		
		String mti = argMsg.substring( 0, 4 );
		
		if( "0100".equals( mti ) || "0110".equals( mti ) || 
			"0200".equals( mti ) || "0210".equals( mti ) ||
			"0400".equals( mti ) || "0410".equals( mti ) || 
			"0800".equals( mti ) || "0810".equals( mti ) ) {
			
			return true;
		}
		
		return false;
	}

	private boolean isJsonMsg( String argMsg ) {
		
		String mti = argMsg.substring( 0, 4 );
		
		if( "JSRQ".equals( mti ) || "JSRS".equals( mti ) ) {
			
			return true;
		}
		
		return false;
	}
	
}
