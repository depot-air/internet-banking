package com.dwidasa.interlink.utility;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.Caller;
import com.dwidasa.interlink.ServiceCaller;

/**
 * @author prayugo
 */
@Component( "myCaller" )
@Scope( "prototype" )
public class MyCaller implements Caller {

	private String aMsg;
	private boolean reply = false;
	
	private ServiceCaller caller;
	
	private Logger logger = Logger.getLogger( MyCaller.class );

	/**
	 * default constructor for class MyCaller
	 */
	public MyCaller() {
		
		logger.info( "MyCaller() initialization." );
		
		caller = ( ServiceCaller ) ServiceLocator.getService( "serviceCaller" );
	}
	
    /**
     * send a message to other application without requesting a reply
     * @param Msg interface message contains of business process data
	 * @return just return true if process was success and vice versa
     */
    public boolean fireAndForget( String Msg ) {
    	
    	return caller.fireAndForget( Msg );
    }

    /**
     * send message to other application and waits for reply. reply mechanism
	 * will be trigger and handled through method onMessage( String )
     * @param Msg interface message contains of business process data
	 * @return response message or original message with response timeout
     */
    public String fireAndWait( String Msg ) {

    	try {
    		
            if( caller.fireAndWait( this, Msg ) ) {
            	
                synchronized( this ) {
                    while( !reply ) {
                        try {
                            wait(5*60000); //5 menit                            
                        } 
                        catch (InterruptedException e) {

                        }
                    }
                }
            }
    	}
    	catch( Exception e ) {
    		
    		logger.error( "MyCaller.fireAndWait() error", e );
    	}
    	
    	return aMsg;
    }

	/**
	 * set the message with reply message received from other application
	 * @param Msg interface message contains of business process data
	 */
	public void setMessage( String Msg ) {
		
		aMsg = Msg;
		reply = true;
	}
			
}
