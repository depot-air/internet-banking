package com.dwidasa.engine.service;

import com.dwidasa.engine.model.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:05 PM
 */
public interface ParameterService extends GenericService<Parameter, Long> {

	/**
	 * Get parameter value by name
	 * @param name
	 * @return
	 */
	String getParameterValueByName(String name);

	/**
	 * Set parameter value by name
	 * @param name
	 * @param value
	 */
	void setParameterValueByName(String name, String value);
}
