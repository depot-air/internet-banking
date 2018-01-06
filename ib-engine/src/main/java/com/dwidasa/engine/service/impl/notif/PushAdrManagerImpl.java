package com.dwidasa.engine.service.impl.notif;

import org.springframework.stereotype.Service;

import com.dwidasa.engine.service.notif.PushAdrManager;

@Service("pushAdrManager")
public class PushAdrManagerImpl implements PushAdrManager {

	@Override
	public void doPush(String pushId, String payload, String title) {
		// TODO Auto-generated method stub
		
	}
	
//	@Value("${gcm.sender}")
//	private String gcmSender = "AIzaSyDa6Yzt9zS4KFXSfTM8lujrqW5gakSEkBg";       
//
//	@Override
//	public void doPush(String pushId, String payload, String title) {		
//		try {
//			pushGcm(pushId, payload, title);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	protected Result pushGcm(String pushId, String payload, String title) throws IOException{
//		Sender sender = new Sender(gcmSender);
//		//AIzaSyDz6vCq1r-MZQI_cp6-dNw58XWxT4OnnF8
//		//System.out.println("Payload : " + logPush.getPayload());
//        
//		Message message = new Message
//            .Builder()
//            .addData("payload", payload)
//            .addData("title", title)
//            .build();
//        Result result = sender.send(message, pushId, 5);
//        //System.out.println("result = " + result);
//        return result;
//	}

}
