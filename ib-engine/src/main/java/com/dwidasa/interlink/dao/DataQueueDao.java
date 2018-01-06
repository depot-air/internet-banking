package com.dwidasa.interlink.dao;

import com.dwidasa.interlink.model.MDataQueue;

/**
 * @author prayugo
 */
public interface DataQueueDao {

	/**
	 * get data queue configuration
	 * @param code data queue code, either inquiry or transaction
	 * @return data queue configuration
	 */
	public MDataQueue getDataQueue( String code );
	
}
