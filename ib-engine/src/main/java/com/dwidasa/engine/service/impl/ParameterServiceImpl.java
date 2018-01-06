package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:59 PM
 */
@Service("parameterService")
public class ParameterServiceImpl extends GenericServiceImpl<Parameter, Long> implements ParameterService {
	private ParameterDao parameterDao;
	
    @Autowired
    public ParameterServiceImpl(ParameterDao parameterDao) {
        super(parameterDao);
        this.parameterDao = parameterDao;
    }

	@Override
	public String getParameterValueByName(String name) {		
		return parameterDao.getParameterValueByName(name);
	}

	@Override
	public void setParameterValueByName(String name, String value) {
		parameterDao.setParameterValueByName(name, value);
	}
}
