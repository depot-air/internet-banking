package com.dwidasa.interlink.medium.queue;

import org.apache.log4j.Logger;

import com.dwidasa.interlink.Listener;
import com.dwidasa.interlink.model.MDataQueue;
import com.ibm.as400.access.AS400;

/**
 * @author prayugo
 */
public class DQProvider {

	private AS400 system;
	private boolean simulate = false;
	
	private MDataQueue model;
	private Listener listener;

	private DQSender sender;
	private DQReceiver receiver;
	private DQSimulator simulator;

    private Logger logger = Logger.getLogger( DQProvider.class );

    /**
	 * default constructor for class DataQueueProvider
	 * @param argListener a message listener 
	 * @param model data queue information
	 */
	public DQProvider( Listener listener, MDataQueue model ) {
	
		logger.info( "DQProvider() initilization" );
		
		this.listener = listener;
		this.model = model;

		this.setup();
	}
	
	/**
	 * setup library and queues
	 */
	private void setup() {
		
		String lib = "/QSYS.LIB/" + model.getLibrary() + ".LIB/";
		String squ = model.getQueueSender() + ".DTAQ";
		String rqu = model.getQueueReceiver() + ".DTAQ";

		model.setLibrary( lib );
		model.setQueueSender( squ );
		model.setQueueReceiver( rqu );
	}
	
	/** 
	 * on startup service of object, prepare object 
	 * initialization and update the initialization status
	 * @return true if startup succeed and vice versa 
	 */
	public boolean startup() {
	
		logger.info( "DQProvider.startup()" );
		
		if( simulate ) {
			
			simulator = new DQSimulator( listener );
			simulator.startup();

			return true;
		}
		
		try {
		
			system = new AS400( model.getSystem(), model.getUsername(), model.getPassword() );
			
			if( createSender() ) {
				
				if( createReceiver() ) {
				
					return true;
				}
				else {

					logger.error( "Unable to create Data Queue Receiver." );
				}
			}
			else {
			
				logger.error( "unable to create Data Queue Sender." );
			}
		}
		catch( Exception e ) {
		
			logger.error( "Unable to connect to AS/400 system", e );
		}
		
		return false;
	}

	/**
	 * shutdown the service by closing unused resources
	 */
	public void shutdown() {
	
		logger.info( "DQProvider.shutdown()." );
		
		if( simulate ) {
			
			simulator.shutdown();
			return;
		}
		
		sender.shutdown();
		receiver.shutdown();
	}

	/**
	 * send a text message into data queue
	 * @param Msg any text message to be sent to data queue
	 */
	public boolean fire( String Msg ) {

		if( simulate ) {
			
			return simulator.simulate( Msg );
		}
		
		return sender.fire( Msg );
	}

    private boolean createSender() {
	
    	logger.info( "DQProvider.createSender()." );
    	
    	sender = new DQSender( system, model );
    	
    	return( sender.startup() );
	}
    
    private boolean createReceiver() {
	
    	logger.info( "DQProvider.createReceiver()." );
    	
    	receiver = new DQReceiver( system, model, listener );
    	
    	return( receiver.startup() );
	}

}
