package com.dwidasa.interlink;

import java.util.Hashtable;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.dao.InterlinkDao;
import com.dwidasa.interlink.digester.FixDigester;
import com.dwidasa.interlink.medium.DQTransporter;
import com.dwidasa.interlink.medium.Transporter;
import com.dwidasa.interlink.model.MInterlink;
import com.dwidasa.interlink.utility.Constant;
import com.dwidasa.interlink.utility.Monitor;
import com.dwidasa.interlink.utility.MonitorService;

/**
 * @author prayugo
 */
@Component( "qserviceCaller" )
public class QServiceCaller implements Listener, Monitor {

    private Hashtable< String, Caller > caller = new Hashtable< String, Caller >();
    private Hashtable< String, Long > timeout = new Hashtable< String, Long >();

    private Digester digester; 
    private Transporter transporter;
    
    private MonitorService monitor;

    private MInterlink interlink;
    private boolean monitorInProcess = false;
    
    private Logger logger = Logger.getLogger( ServiceCaller.class );
    
    /**
     * start the ServiceCaller service
     */
	@PostConstruct
    public void startService() {
    	
		logger.info( "ServiceCaller.startService()" );
		
		digester = getDigester();
		transporter = getTransporter();

		InterlinkDao dao = ( InterlinkDao ) ServiceLocator.getService( "interlinkDao" );
		interlink = dao.getInterlink();

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
	
		/*
		 * key get from message header
		 *  01. transaction type. an2
		 *  02. transmission date. n14 yyyyMMddhhmmdss
		 *  03. system trace audit number. n6
		 *  04. retrievel reference number. n12
		 *  
		 *  key pos( 0 -> 34 )
         */
		FixDigester d = new FixDigester( 0, 34 );
		
		return d;
	}

	/**
	 * initialize the message transporter class
	 * @return an instance of which implements Transporter interface
	 */
	public Transporter getTransporter() {
	
		DQTransporter t = new DQTransporter( this );
		
		if( t.startup() ) {
			
			return t;
		}
		
		return null;
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

    	String key = digester.getKey( argMsg );
    	
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
    public void onMessage( String argMsg ) {
    
    	String key = digester.getKey( argMsg );								// -- set response message and notify the caller
		Caller c = caller.get( key );

		if( ( c != null ) ) {
			synchronized( c ) {
			
				c.setMessage( argMsg );
				c.notify();
			}
		
			timeout.remove( key );											// -- release all resources
			caller.remove( key );
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
				
				Hashtable< String, Long > ctimeout = null;					// -- create a shallow copy for processing
				synchronized( this ) {
				
					ctimeout = ( Hashtable ) timeout.clone();				// -- cloned timeout object
				}
				
				long ct = System.currentTimeMillis();						// -- current time
				Iterator< String > it = ctimeout.keySet().iterator();
			
				while( it.hasNext() ) {
				
					String key = it.next();
					long st = ctimeout.get( key ).longValue();				// -- start time
					
					if( ( ( ct-st )/1000 ) > interlink.getTimeoutPeriod() ) {
					
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
				
				logger.error( "QServiceCaller.exception on monitor()", e );
			}
			
			monitorInProcess = false;
		}
	}
	
}