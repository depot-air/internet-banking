package com.dwidasa.engine.dao;


public interface HousekeepingDao {
	/**
	 * run house keeping 
	 * @param days retention period (delete data older than this value)
	 */
	public void runHousekeeping(int days);
	
}
