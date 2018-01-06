package com.dwidasa.interlink.medium.queue;

import org.apache.log4j.Logger;

import com.dwidasa.interlink.Listener;
import com.dwidasa.interlink.model.MDataQueue;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.DataQueueEntry;

/**
 * @author developprayugoer
 */
public class DQReceiver extends Thread {

	private AS400 system;
	
	private DataQueue queue;
	private MDataQueue model;
	
	private Listener listener;
	private boolean status = true;

    private Logger logger = Logger.getLogger( DQReceiver.class );

	/**
	 * default constructor for class DataQueueReceiver
	 * @param listener message consumer
	 * @param model data queue configuration
	 * @param arg400 an AS400 system which data queue sited
	 * @param @param model a data queue information
	 */
	public DQReceiver( AS400 system, MDataQueue model, Listener listener ) {
	
		logger.info( "Starting DQReceiver() ..." );

		this.listener = listener;
		this.system = system;
		this.model = model;
	}
	
	/**
	 * on startup initialize the instance by connecting to data queue object
	 * and add data queue listener
	 * @return true if operation succeed or vice versa
	 */
	public boolean startup() {
	
		logger.info( "DQReceiver.startup() ..." );
		
		try {
		
			queue = new DataQueue( system, model.getLibrary() + model.getQueueReceiver() );
			
			queue.clear();

			start();
			
			return true;
		}
		catch( Exception e ) {
		
			queue = null;
			
			logger.error( "DQReceiver.startup() exception on data queue = " + model.getQueueReceiver(), e );
		}
		
		return false;
	}

	/**
	 * shutdown the DataQueueReceiver service
	 */
	public void shutdown() {
	
		logger.info( "DQReceiver.shutdown()" );
		
		queue = null;
		status = false;
		
		interrupt();
	}

	/**
	 * on a message in the data queue, notify message listener
	 */
	public void read() {
	
		try {
			
			if( queue == null ) {
				
				queue = new DataQueue( system, model.getLibrary() + model.getQueueReceiver() );
			}
		
			DataQueueEntry qe = queue.read( -1 );
			
			if( ( qe != null ) && ( status == true ) ) {
			
				String data = qe.getString();
				
				if( data != null && data.length() > 2 ) {
				
			    	listener.onMessage( data );
				}
			}
		}
		catch( Exception e ) {
		
			if( status ) {
			
	            queue = null;

	            logger.error( "DQReceiver.read() exception on receiving queue = " + model.getQueueReceiver(), e );
			}
		}
	}
	
	/**
	 * override base method run() in class Thread
	 */
	public void run() {
	
		logger.info( "DQReceiver.run() service started." );
		logger.info( "DQReceiver.run() listening on data queue = " + ( model.getQueueReceiver() ) );
		
		while( status ) {
		
			if( status ) {
			
				read();
			}
		}
		
		logger.info( "DQReceiver.run() service stoped." );
	}
	
}
