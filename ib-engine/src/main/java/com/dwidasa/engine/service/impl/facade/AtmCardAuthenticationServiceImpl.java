package com.dwidasa.engine.service.impl.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.CustomerSessionDao;
import com.dwidasa.engine.service.facade.AtmCardAuthenticationService;
/**
 * 
 * @author gandos
 *
 */
@Service("atmCardAuthenticationService")
public class AtmCardAuthenticationServiceImpl implements AtmCardAuthenticationService {

    @Autowired
    private CustomerSessionDao customerSessionDao;
    
    public Boolean validateCustomerSession(Long customerId, String sessionId) {
        switch (customerSessionDao.validate(customerId, sessionId)) {
            case -1 : throw new BusinessException("IB-1001");
            case 0  : throw new BusinessException("IB-1010");
        }

        return Boolean.TRUE;
    }
    
	@Override
	public boolean authenticate(Long customerId, String deviceId, String sessionId, 
								String cardData, String pin) {
		// TODO Auto-generated method stub
		if(validateCustomerSession(customerId, sessionId))
			return true;
		
		return false;
	}

}
