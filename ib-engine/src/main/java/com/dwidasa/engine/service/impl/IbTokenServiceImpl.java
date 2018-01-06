package com.dwidasa.engine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.IbTokenDao;
import com.dwidasa.engine.model.IbToken;
import com.dwidasa.engine.service.IbTokenService;

@Service("ibTokenService")
public class IbTokenServiceImpl extends GenericServiceImpl<IbToken, Long> implements IbTokenService {
	private IbTokenDao ibTokenDao;
	
    @Autowired
    public IbTokenServiceImpl(IbTokenDao ibTokenDao) {
        super(ibTokenDao);
        this.ibTokenDao = ibTokenDao;
    }

	@Override
	public List<IbToken> getAvailableTokens() {
		return ibTokenDao.getAvailableTokens();
	}

	@Override
	public List<IbToken> getAvailableTokens(String serialNumber) {
		return ibTokenDao.getAvailableTokens(serialNumber);
	}
	@Override
	public List<IbToken> getLinkedTokensBySerialNumber(String serialNumber){
		return ibTokenDao.getLinkedTokensBySerialNumber(serialNumber);
	}
	
	@Override
	public List<IbToken> getSelectedTokens() {
		return ibTokenDao.getSelectedTokens();
	}
	
	@Override
	public List<IbToken> getSelectedTokensByCustomerId(Long userId) {
		return ibTokenDao.getSelectedTokensByCustomerId(userId);
	}
	
	@Override
	public IbToken getById(Long id) {
		return ibTokenDao.getById(id);
	}

	@Override
	public void refreshCancelledToken() {
		ibTokenDao.refreshCancelledToken();
	}


}
