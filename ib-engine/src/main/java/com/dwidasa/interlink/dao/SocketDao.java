package com.dwidasa.interlink.dao;

import com.dwidasa.interlink.model.MSocket;

/**
 * @author prayugo
 */
public interface SocketDao {

	/**
	 * get interlink socket configuration
	 * @param id m_interlink_id
	 * @return interlink socket configuration
	 */
	public MSocket getSocket( long id );

	/**
	 * update the connection status
	 * @param model socket model
	 * @param status true for connected and vice versa
	 */
	public void setConnection( MSocket model, boolean status );
	
}
