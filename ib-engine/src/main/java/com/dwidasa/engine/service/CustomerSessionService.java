package com.dwidasa.engine.service;

import com.dwidasa.engine.model.CustomerSession;

public interface CustomerSessionService extends GenericService<CustomerSession, Long> {

	void removeAll();


}
