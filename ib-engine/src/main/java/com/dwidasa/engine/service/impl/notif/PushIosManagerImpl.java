package com.dwidasa.engine.service.impl.notif;

import org.springframework.stereotype.Service;

import com.dwidasa.engine.service.notif.PushIosManager;

@Service("pushIosManager")
public class PushIosManagerImpl implements PushIosManager {

	@Override
	public void doPush(String pushId, String payload, String title) {
		// TODO Auto-generated method stub
		
	}
	
//	@Value("${apn.production-mode}")
//	private boolean productionMode;
//	@Value("${apn.keystore}")
//	private String keystore = "mclears.p12";       
//	@Value("${apn.pwd}")
//	private String pwd = "dwidasa123";
//	
//	@Override
//	public void doPush(String pushId, String payload, String title) {
//		pushApn(pushId, payload, title);
//	}
//	
//	protected PushedNotifications pushApn(String pushId, String payload, String title) {
//        String message = payload; 
//        String device = pushId;
//
//        ClassPathResource cpr = new ClassPathResource(keystore);        
//        
//		try {
//			return Push.alert(title, cpr.getFilename(), pwd, productionMode, device);
//			//return Push.payload(payload, cpr.getFilename(), pwd, productionMode, device);
//		} catch (CommunicationException e) {
//			e.printStackTrace();
//		} catch (KeystoreException e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}

}
