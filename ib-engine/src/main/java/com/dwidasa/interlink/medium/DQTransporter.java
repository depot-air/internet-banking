package com.dwidasa.interlink.medium;

import org.apache.log4j.Logger;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.Listener;
import com.dwidasa.interlink.dao.DataQueueDao;
import com.dwidasa.interlink.medium.queue.DQProvider;
import com.dwidasa.interlink.model.MDataQueue;

/**
 * @author prayugo
 */
public class DQTransporter implements Transporter {

	private Listener listener;
	private DQProvider provider;

	private Logger logger = Logger.getLogger( DQTransporter.class );
	
	/**
	 * default constructor for class DQTransporter
	 * @param listener module owner
	 */
	public DQTransporter( Listener listener ) {
		
		logger.info( "DQTransporter() initialization." );
		
		this.listener = listener;
	}

	/**
	 * prepare configuration and start the service
	 * @return just return true if starting process was success and vice versa
	 */
	@Override
	public boolean startup() {
		
		logger.info( "DQTransporter.startup()." );
		
		DataQueueDao dao = ( DataQueueDao ) ServiceLocator.getService( "dataQueueDao" );
		MDataQueue model = dao.getDataQueue( "inquiry" );
		
		provider = new DQProvider( listener, model );
		return provider.startup();
	}
	
    /**
     * shutdown the Service instance
     */
	@Override
	public void shutdown() {
		
    	logger.info( "DQTransporter.shutdown()" );
    	
    	provider.shutdown();
	}
	
    /**
     * send a message to external application that connected with our system.
     * @param Msg interface message contains of business process data
     * @return always return true whenever operation succeed and vice versa
     */
	@Override
    public boolean transmit( String Msg ) {
    	
		logger.info( "snd = " + Msg );
		
		return provider.fire( Msg );
    }

//    @Override
//    public boolean connectSspp() {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public boolean connectItm() {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }

}
