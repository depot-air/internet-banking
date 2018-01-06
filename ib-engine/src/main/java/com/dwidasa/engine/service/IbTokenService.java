package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.IbToken;

public interface IbTokenService extends GenericService<IbToken, Long> {
	public List<IbToken> getAvailableTokens();
	public List<IbToken> getAvailableTokens(String serialNumber);
	public List<IbToken> getLinkedTokensBySerialNumber(String serialNumber);
	public List<IbToken> getSelectedTokens();
	public List<IbToken> getSelectedTokensByCustomerId(Long userId);
	public IbToken getById(Long id);
	public void refreshCancelledToken();
}
