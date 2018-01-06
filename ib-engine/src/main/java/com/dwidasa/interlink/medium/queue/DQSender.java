package com.dwidasa.interlink.medium.queue;

import org.apache.log4j.Logger;

import com.dwidasa.interlink.model.MDataQueue;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.DataQueue;

/**
 * @author prayugo
 */
public class DQSender {

	private AS400 system;
	
	private DataQueue queue;
	private MDataQueue model;

    private Logger logger = Logger.getLogger( DQSender.class );

	/**
	 * default constructor for class DataQueueSender
	 * 
	 * @param system an AS400 system which data queue sited
	 * @param model a data queue information
	 */
	public DQSender( AS400 system, MDataQueue model ) {
	
		logger.info( "DQSender() initialization" );
		
		this.system = system;
		this.model = model;
	}
	
	/** 
	 * on startup service of object, prepare object 
	 * initialization and update the initialization status 
	 * @return true if operation succeed or vice versa
	 */
	public boolean startup() {
	
		logger.info( "DQSender.startup()." );
		
		try {
		
			queue = new DataQueue( system, model.getLibrary() + model.getQueueSender() );
			
			queue.clear();
			
			return true;
		}
		catch( Exception e ) {
		
			logger.error( "DQSender.startup() exception on data queue = " + model.getQueueSender(), e );
		}

		return false;
	}

	/**
	 * shutdown the service by closing unused resources
	 */
	public void shutdown() {
	
		logger.info( "DQSender.shutdown()." );
	}

	/**
	 * send a text message into data queue
	 * @param Msg any text message to be sent to data queue
	 * @return true if operation succeed or vice versa
	 */
	public boolean fire( String Msg ) {
	
		try {
	
			queue = new DataQueue( system, model.getLibrary() + model.getQueueSender() );
			
			queue.write( Msg );
			
			return true;
		} 
		catch( Exception e ) {
		
            logger.error( "DQSender.fire() exception on data queue = " + model.getQueueSender(), e );
		} 
		
		return false;
	}
	
}
