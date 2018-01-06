package com.dwidasa.admin.pages.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.pages.user.ChangeProfile;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.InboxService;
import com.dwidasa.engine.service.UserService;
import com.dwidasa.engine.service.VersionService;

@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class InboxList {
	@Property
	private Inbox inbox;
	
	@Property
	private String title;
	
	@Property
	private String Content;
	
	@Property
    private GridDataSource dataSource;
	

	@Property
    private int pageSize;
	
	@Inject
    private VersionService versionService;
	
	@InjectPage
	private InboxInput changeInboxNotification;
	
	
	@Inject
	private InboxService inboxService;
	
	@Inject
	private InboxCustomerDao customerDao;

	@Inject
	private Messages messages;
	
	@Property
    @Persist
    private String Subject;

	@Persist
	@Property(write = false)
	private Long id;

	@Inject
	private SessionManager sessionManager;

	void onActivate() {
		id = sessionManager.getLoggedUser().getId();
	}

//	void onPrepare() {
//		if (id != null) {
//			inbox = inboxService.get(id);
//		}else{
//			inbox = new Inbox();
//		}
//	}
	
	void setupRender(){
		pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (Subject != null && !Subject.equals("")) {
            restrictions.add("upper(title) like '%' || ? || '%'");
            values.add(Subject.toUpperCase());
        }
        
		if(id == null){
			inbox = new Inbox();
		}
	
		dataSource = new BaseDataSource(Inbox.class, Constants.PAGE_SIZE, restrictions, values);
	}

//	public String getRoleName() {
//		if (user == null || user.getRoleId() == null) return "-";
//		if (Constants.Role.SUPERUSER.equals(user.getRoleId())) {
//			return Constants.RoleName.SUPERUSER;
//		} else if (Constants.Role.ADMIN.equals(user.getRoleId())) {
//			return Constants.RoleName.ADMIN;
//		} else if (Constants.Role.TREASURY.equals(user.getRoleId())) {
//			return Constants.RoleName.TREASURY;
//		} else if (Constants.Role.DAY_ADMIN.equals(user.getRoleId())) {
//			return Constants.RoleName.DAY_ADMIN;
//		} else if (Constants.Role.NIGHT_ADMIN.equals(user.getRoleId())) {
//			return Constants.RoleName.NIGHT_ADMIN;
//		} 
//		return "-";
//	}
	
	Object onSuccess() {
		changeInboxNotification.setId((long)0);
		return changeInboxNotification;
	}
	
	@Persist(PersistenceConstants.FLASH)
	private String messageInfo;
	
	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}
	
	@Environmental
	private JavaScriptSupport renderSupport;
	

	 @DiscardAfter
	    void onActionFromDelete(Long id) {
		 
		 	try {
		 		
		 		
		 		Long userId = sessionManager.getLoggedUser().getId();
		 		customerDao.removeAllById(id);
		        inboxService.remove(id, userId);
		        
				
			} catch (Exception e) {
				
				Long userId = sessionManager.getLoggedUser().getId();
		        inboxService.remove(id, userId);
				// TODO: handle exception
			}
	    	
	    }
         
         
         
	 
}
