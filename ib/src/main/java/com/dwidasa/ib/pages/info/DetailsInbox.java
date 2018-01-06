package com.dwidasa.ib.pages.info;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.dao.InboxDao;
import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.InboxCustomer;
import com.dwidasa.engine.service.InboxService;
import com.dwidasa.ib.services.SessionManager;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: Sisca
 * Date: 9/8/11
 * Time: 8:38 PM
 */
public class DetailsInbox {
    @Property
    private InboxCustomer inboxCustomer;
   
    @Property(write=false)
    @Persist
    private Inbox inbox;
    
    @Inject
    private InboxDao inboxDao;

//    @PageActivationContext
    @Property
    private Long id;

    @Property(write=false)
    private InboxService inboxService;
    
    @Inject
    private InboxCustomerDao inboxCustomerDao;

    @InjectPage
    private InboxList showAll;
    
    @Inject
    private SessionManager sessionManager;
    
    @Property
    private String nowDate;

//    @OnEvent(component = "backLink")
    public void onActivate(Long idInbox, long id) {
    	
    	
    	
    	if (idInbox == 0) {
    		setAccountViewDate();
            inbox = new Inbox();
        }
        else if(id != 0){
        	//
        	setAccountViewDate();
            inbox = inboxDao.get(idInbox);
            //Ubah Status Menjadi Sudah Dibaca
            //inboxCustomer = inboxCustomerDao.get(id);
            //inboxCustomer.setStatus(1);
            //inboxCustomerDao.save(inboxCustomer);
            Long userId = sessionManager.getLoggedCustomerPojo().getId();
            inboxCustomerDao.updateStatusById(idInbox, userId, 1);
            
        }
        else {
            throw new IllegalStateException("Invalid Request");
        }
    }

    
    public String getDateFormat(){
    	SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
    	return sdate.format(inbox.getStartDate());
    }
    
    private void setAccountViewDate() {
        Date date = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
        nowDate = sdate.format(date);
        nowDate = nowDate;
    }
    
    @DiscardAfter
    Object onSuccess(){
    	return InboxList.class;
    }

}
