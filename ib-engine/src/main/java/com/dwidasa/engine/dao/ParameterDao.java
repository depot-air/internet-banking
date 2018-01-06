package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/21/11
 * Time: 6:01 PM
 */
public interface ParameterDao extends GenericDao<Parameter, Long> {
    /**
     * Get parameter object from parameter name
     * @param parameterName parameter name
     * @return parameter object
     */
    public Parameter get(String parameterName);

	/**
	 * Get parameter value by parameter name
	 * @param name
	 * @return
	 */
	public String getParameterValueByName(String name);

	/**
	 * Seet parameter value by parameter name
	 * @param name
	 * @param value
	 */
	public void setParameterValueByName(String name, String value);

}
