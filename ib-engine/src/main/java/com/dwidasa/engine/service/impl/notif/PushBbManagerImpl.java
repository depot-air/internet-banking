package com.dwidasa.engine.service.impl.notif;

import org.springframework.stereotype.Service;

import com.dwidasa.engine.service.notif.PushBbManager;

@Service("pushBbManager")
public class PushBbManagerImpl implements PushBbManager {

	@Override
	public void doPush(String pushId, String payload, String title) {
		// TODO Auto-generated method stub
		
	}
	
//	@Autowired 
//	private PapService papService;
//	@Value("${pap.bis}")
//	private boolean appBis = true;       
//	@Value("${pap.username}")
//	private String appUname = "3263-5B1602eD40814MMr488ii216872ke254l98";       
//	@Value("${pap.pwd}")
//	private String appPwd = "8nKLVOck";
//	@Value("${pap.dest}")
//	private String appDest = "3263-5B1602eD40814MMr488ii216872ke254l98";
//
//
//	@Override
//	public void doPush(String pushId, String payload, String title) {
//			try {
//				pushBb(pushId, payload, title);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//	}
//	
//	protected void pushBb(String pushId, String payload, String title) throws IOException{
//		IdGenerator idGenerator = new IdGenerator() {
//			@Override
//			public String generateId() {
//				return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//			}
//		}; 
//
//		PushMessageControl pushMessageControl = new PushMessageControl(appBis, idGenerator, appUname, pushId);
//		String msg = payload + " " + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//		
//		Content content = new TextContent(msg , "UTF-8");
//		
//		try {
//			PushResponse pushResponse =  papService.push(
//				appUname, appPwd, appDest, pushMessageControl, content);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//	}

}
