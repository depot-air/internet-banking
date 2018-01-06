package com.dwidasa.interlink.medium;

/**
 * @author prayugo
 */
public interface Transporter {

	/**
	 * prepare configuration and start the service
	 * @return just return true if starting process was success and vice versa
	 */
	public boolean startup();
	
    /**
     * shutdown the Service instance
     */
	public void shutdown();
	
    /**
     * send a message to external application that connected with our system.
     * @param Msg interface message contains of business process data
     * @return always return true whenever operation succeed and vice versa
     */
    public boolean transmit( String Msg );

//    public boolean connectSspp();
//    public boolean connectItm();
}
