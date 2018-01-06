package com.dwidasa.interlink;

/**
 * @author prayugo
 */
public interface ServiceHook {

	/**
     * this method would be called automatically on incoming 
     * message that needs handled through hook mechanism  
     * @param argMsg interface message contains of business process data 
     */
	public void onMessage( String argMsg );

}
