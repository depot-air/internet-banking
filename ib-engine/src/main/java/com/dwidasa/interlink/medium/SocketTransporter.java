package com.dwidasa.interlink.medium;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.service.CacheManager;
import org.apache.log4j.Logger;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.Listener;
import com.dwidasa.interlink.dao.InterlinkDao;
import com.dwidasa.interlink.dao.SocketDao;
import com.dwidasa.interlink.medium.socket.Connection;
import com.dwidasa.interlink.medium.socket.SocketClientProvider;
import com.dwidasa.interlink.medium.socket.SocketServerProvider;
import com.dwidasa.interlink.model.MInterlink;
import com.dwidasa.interlink.model.MSocket;

/**
 * @author prayugo
 */
public class SocketTransporter implements Transporter {

	private MSocket model;
	
	private Listener listener;
	private Connection connection;
	
	private SocketDao socketDao;
	private InterlinkDao interlinkDao;
	private ParameterDao parameterDao;

	private Logger logger = Logger.getLogger( SocketTransporter.class );
	
	/**
	 * default constructor for class SocketTransport
	 * @param listener module owner
	 */
	public SocketTransporter( Listener listener ) {
		
		logger.info( "SocketTransporter() initialization." );
		
		this.listener = listener;
		this.socketDao = ( SocketDao ) ServiceLocator.getService( "socketDao" );
		this.interlinkDao = ( InterlinkDao ) ServiceLocator.getService( "interlinkDao" );
        this.parameterDao = ( ParameterDao ) ServiceLocator.getService( "parameterDao" );
	}
	
	/**
	 * prepare configuration and start the service
	 * @return just return true if starting process was success and vice versa
	 */
	@Override
	public boolean startup() {
    	
    	logger.info( "SocketTransporter.connect()." );
    	
    	MInterlink mi = interlinkDao.getInterlink();
    	model = socketDao.getSocket( mi.getId() );
    	
    	socketDao.setConnection( model, false );										// -- init to disconnected
    	
		if( model.getConnectionType() == Connection.Server ) {
    		
			connection = new SocketServerProvider( model, listener );
    	}
    	else {
    		
    		connection = new SocketClientProvider( model, listener );
    	}
    	
    	return connection.connect();
    }

    /**
     * shutdown the Service instance
     */
	@Override
	public void shutdown() {
    	
    	logger.info( "SocketTransporter.disconnect()" );
    	
    	connection.disconnect();
		socketDao.setConnection( model, false );
    }

    /**
	 * transmit a message to the external application
     * @param Msg message to be sent to external application
	 * @return just return true if process was success and vice versa
	 */
	@Override
	public boolean transmit( String Msg ) {
		
		logger.info( "snd = " + Msg );
		
		return connection.transmit( Msg );
	}

//    @Override
//    public boolean connectSspp() {
//
//    	logger.info( "SocketTransporter.connect()." );
//        Parameter ip = parameterDao.get(Constants.MPARAMETER_NAME.SSPP_ITM_IP);
//        Parameter port = parameterDao.get(Constants.MPARAMETER_NAME.SSPP_ITM_PORT);
///*
//        CacheManager cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
//        String ip = cacheManager.getParameter("SSPP_ITM_IP").getParameterValue();
//        String port = cacheManager.getParameter("SSPP_ITM_PORT").getParameterValue();
//*/
//
//    	MSocket model = new MSocket();
//        model.setId(2);
//    	model.setConnectionType(0);
//    	model.setServerAddress(ip.getParameterValue());
//    	model.setServerPort(Integer.parseInt(port.getParameterValue()));
//    	model.setMonitorPeriod(30);
//    	model.setSocketDriver("com.dwidasa.interlink.medium.socket.impl.AHSocket");
//    	model.setDescription("client for sspp server");
//
//    	socketDao.setConnection( model, false );										// -- init to disconnected
//
//		if( model.getConnectionType() == Connection.Server ) {
//
//			connection = new SocketServerProvider( model, listener );
//    	}
//    	else {
//
//    		connection = new SocketClientProvider( model, listener );
//    	}
//
//    	return connection.connect();
//    }

//    @Override
//    public boolean connectItm() {
//
//    	logger.info( "SocketTransporter.connect()." );
//        CacheManager cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
//        String ip = cacheManager.getParameter("SSPP_ITM_IP").getParameterValue();
//        String port = "22";//cacheManager.getParameter("SSPP_ITM_PORT").getParameterValue();
//
//    	MSocket model = new MSocket();
//        model.setId(2);
//    	model.setConnectionType(0);
//    	model.setServerAddress(ip);
//    	model.setServerPort(Integer.parseInt(port));
//    	model.setMonitorPeriod(30);
//    	model.setSocketDriver("com.dwidasa.interlink.medium.socket.impl.AHSocket");
//    	model.setDescription("client for sspp server");
//
//    	socketDao.setConnection( model, false );										// -- init to disconnected
//
//		if( model.getConnectionType() == Connection.Server ) {
//
//			connection = new SocketServerProvider( model, listener );
//    	}
//    	else {
//
//    		connection = new SocketClientProvider( model, listener );
//    	}
//
//    	return connection.connect();
//    }

}
