package com.dwidasa.interlink.utility;

import org.apache.log4j.Logger;


/**
 * @author prayugo
 */
public class MonitorService extends Thread {

    private long periode;
    private boolean active;

    private Monitor monitor; 

    private Logger logger = Logger.getLogger( MonitorService.class );

    /**
     * default constructor for MonitorService.
     * @param argMonitor the owner to be notified
     * @param argPeriode time period in which the monitor notify owner ( in seconds )
     */
    public MonitorService( Monitor argMonitor, long argPeriode ) {
    
    	logger.info( "initialization( Monitor, long )." );
    	
    	monitor = argMonitor;
        periode = ( 1000 * argPeriode );
    }

    /**
     * main activities of this instance
     */
    public void run() {
    
        while( active ) {
        
            try {
            
            	sleep( periode ); 
            }
            catch( InterruptedException e ) { 
            	
            }

            // -- notify the owner now
            if( active ) { 
            
            	monitor.monitor();
            }
        }
    }

	/**
	 * start the monitor now
	 */
	public void startMonitor() {
		
		active = true;
		start();
	}
	
	/**
	 * stop the running monitor
	 */
	public void stopMonitor() {
		
		active = false;
		interrupt();
	}
	
}
