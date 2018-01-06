package com.dwidasa.engine.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.acegisecurity.ui.session.HttpSessionEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.dao.CustomerSessionDao;

public class MyHttpSessionEventPublisher extends HttpSessionEventPublisher {

    @Autowired
    private CustomerSessionDao customerSessionDao;
    
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		super.sessionCreated(event);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		Long customerId = (Long)session.getAttribute("customerId");
//		System.out.println("customerId=" + customerId);
		try {
//			loggingService.logActivity(customerView.getId(), com.dwidasa.engine.Constants.LOGOUT_TYPE, "Logout", "", sessionMgr.getDefaultMerchantType(), customerView.getTerminalId());
	        customerSessionDao.deleteByCustomerId(customerId);
			super.sessionDestroyed(event);
		} catch (Exception e) {
//			System.err.println(e.getMessage());
		}
	}

}
