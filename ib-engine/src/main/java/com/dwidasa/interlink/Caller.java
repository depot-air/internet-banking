package com.dwidasa.interlink;

/**
 * @author prayugo
 */
public interface Caller {

	/**
	 * set the message with reply message received from other application
	 * @param argMsg interface message contains of business process data
	 */
	public void setMessage( String argMsg );

}
