package com.dwidasa.interlink.utility;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.Caller;
import com.dwidasa.interlink.QServiceCaller;

/**
 * @author prayugo
 */
@Component( "myQCaller" )
@Scope( "prototype" )
public class MyQCaller implements Caller {

	private String aMsg;
	private boolean reply = false;
	
	private QServiceCaller caller;
	
	private Logger logger = Logger.getLogger( MyQCaller.class );

	/**
	 * default constructor for class MyCaller
	 */
	public MyQCaller() {
		
		logger.info( "MyQCaller() initialization." );
		
		caller = ( QServiceCaller ) ServiceLocator.getService( "qserviceCaller" );
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
                        	
                            wait();
                        } 
                        catch (InterruptedException e) {

                        }
                    }
                }
            }
    	}
    	catch( Exception e ) {
    		
    		logger.error( "MyQCaller.fireAndWait() error", e );
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
