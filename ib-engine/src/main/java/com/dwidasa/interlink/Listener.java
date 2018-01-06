package com.dwidasa.interlink;

/**
 * @author prayugo
 */
public interface Listener {

	/**
     * this method would be called automatically on each incoming message  
     * @param argMsg interface message contains of business process data 
     */
    public void onMessage( String argMsg );

}
