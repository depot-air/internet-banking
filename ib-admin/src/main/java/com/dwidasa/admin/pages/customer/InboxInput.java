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
import com.dwidasa.admin.pages.user.ViewProfile;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.InboxCustomer;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.InboxService;
import com.dwidasa.engine.service.UserService;
import com.dwidasa.engine.service.VersionService;
import java.util.Date;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;

@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class InboxInput {
	@Property
	private Inbox inbox;
	
        
        @Property
        @Persist
        private String title;
        
        @Property
        @Persist
        private String Content;
        
        @Property
        private boolean all;
	
        @Property
        private InboxCustomer inboxCustomer;
        
        @Inject
        private InboxCustomerDao customerDao;
        
        @Inject
        private CustomerDao customerDao2;
        
	@Property
    private GridDataSource dataSource;
	
	@InjectPage
	private InboxCustomerList inboxCustomerList;
	

	@Property
    private int pageSize;
	
	@Inject
    private VersionService versionService;
	
	
	@Inject
	private InboxService inboxService;

	@Inject
	private Messages messages;
	
	@Property
    @Persist
    private String Subject;

	@Persist
	private Long id;

	@Inject
	private SessionManager sessionManager;
	
	@Inject
	private CustomerDeviceDao customerDeviceDao; 
	
	
	@Property
	private String chooseValue;
        
        
    @Component(id="title", parameters={"value=inbox.title"})
    private TextField titleText;
       
    @Component(id="Content", parameters={"value=inbox.content"})
    private TextArea ContentText;
    
//    @Component(id="all", parameters={"value=inbox.forAll"})
//    private Checkbox allCheck;
    

	void onActivate(long id) {
		setId(id);
		
        if (id == 0) {
        	//this.id = (long)0;
        	
            inbox = new Inbox();
            
        }
        else if(id != 0){
       	
            inbox = inboxService.get(id);
        }
        else {
            throw new IllegalStateException("Invalid Request");
        }
	}
	
//	void onActivate(){
//		chooseValue = "fromMerchant";
//	}

	void onPrepare() {
         
            if (inbox == null) {
                inbox = new Inbox();
            }
	}
	
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public Long getId() {
			return id;
		}
        
        void onValidateFromForm() {
            
        	
        	
            
            
        }
	
	Object onSuccess() {
		
		try {
    		
    		boolean isNew = (getId() == 0);
            
    		List<Customer> customerDevices = null;
            User user = sessionManager.getLoggedUser();
            
            if(isNew){
//                
//            
            customerDevices = customerDao2.getAllActiveCustomer();
            
            inbox.setStartDate(new Date());
            inbox.setEndDate(new Date());
            inbox.setCreated(new Date());
            inbox.setCreatedby(user.getId());
            inbox.setUpdated(new Date());
            inbox.setUpdatedby(user.getId());
            inbox.setForAll(false);
            inboxService.save(inbox);
            
            if(inbox.isForAll() == true){
            	
            for(Customer cd : customerDevices){
            	 //System.out.println("HOHOHOHOHOHOH "+cd.getId());
            	 
            	 addInboxCustomer(inbox.getId(), cd.getId(), 0, new Date(), 
            			 		  user.getId(), new Date(), user.getId());
				 
				 
            }
            
            //return InboxList.class;
            
            }
            
            if(chooseValue.equalsIgnoreCase("fromMerchant")){
            	
            	customerDevices = customerDao2.getByCustomerIsMerchant();
            	
            	for(Customer c: customerDevices){
            		
            		addInboxCustomer(inbox.getId(), c.getId(), 0, new Date(), 
            				         user.getId(), new Date(), user.getId());
            		
            	}
            	
            }
            
            if(chooseValue.equalsIgnoreCase("fromIndividual")){
            	
            	customerDevices = customerDao2.getByCustomerIsIndividual();
            	
            	for(Customer c: customerDevices){
            		
            		addInboxCustomer(inbox.getId(), c.getId(), 0, new Date(), 
   				         user.getId(), new Date(), user.getId());
            		
            	}
            	
            }
            
            if(chooseValue.equalsIgnoreCase("fromMerchantIndividual")){
            	
            	customerDevices = customerDao2.getAllActiveCustomer();
            	
            	for(Customer c: customerDevices){
            		
            		addInboxCustomer(inbox.getId(), c.getId(), 0, new Date(), 
   				         user.getId(), new Date(), user.getId());
            		
            	}
            	
            }
            
            //inboxCustomerList.setIdInbox(inbox.getId());
            //return inboxCustomerList;
       
            }else{
            	
            	customerDevices = customerDao2.getAllActiveCustomer();
            	
            	inbox.setId(getId());
            	inbox.setStartDate(new Date());
                inbox.setEndDate(new Date());
                inbox.setCreated(new Date());
                inbox.setCreatedby(user.getId());
                inbox.setUpdated(new Date());
                inbox.setUpdatedby(user.getId());
                inbox.setForAll(false);
                inboxService.save(inbox);
                
            	if(inbox.isForAll() == true){
                    for(Customer cd : customerDevices){
                    	 //System.out.println("HOHOHOHOHOHOH "+cd.getId());
                    	
        				 inboxCustomer = new InboxCustomer();
        				 inboxCustomer.setInboxId(getId());
        				 inboxCustomer.setCustomerId(cd.getId());
        				 inboxCustomer.setStatus(0);
        				 inboxCustomer.setCreated(new Date());
        				 inboxCustomer.setCreatedby(user.getId());
        				 inboxCustomer.setUpdated(new Date());
        				 inboxCustomer.setUpdatedby(user.getId());
        				 customerDao.save(inboxCustomer);
        				 
                    }
                    
                    //return InboxList.class;
                }
            	
            	if(chooseValue.equalsIgnoreCase("fromMerchant")){
                	
                	customerDevices = customerDao2.getByCustomerIsMerchant();
                	
                	for(Customer c: customerDevices){
                		
                		addInboxCustomer(inbox.getId(), c.getId(), 0, new Date(), 
                				         user.getId(), new Date(), user.getId());
                		
                	}
                	
                }
                
                if(chooseValue.equalsIgnoreCase("fromIndividual")){
                	
                	customerDevices = customerDao2.getByCustomerIsIndividual();
                	
                	for(Customer c: customerDevices){
                		
                		addInboxCustomer(inbox.getId(), c.getId(), 0, new Date(), 
       				         user.getId(), new Date(), user.getId());
                		
                	}
                	
                }
                
                if(chooseValue.equalsIgnoreCase("fromMerchantIndividual")){
                	
                	customerDevices = customerDao2.getAllActiveCustomer();
                	
                	for(Customer c: customerDevices){
                		
                		addInboxCustomer(inbox.getId(), c.getId(), 0, new Date(), 
       				         user.getId(), new Date(), user.getId());
                		
                	}
                	
                }
                
            	
            	//inboxCustomerList.setIdInbox(getId());
                //return inboxCustomerList;
                
            }
            
		} catch (Exception e) {
			System.out.println("HOHOHOHOHOHOHOH : "+e.toString());
		}
		
		return InboxList.class;
	}
	
	
	private void addInboxCustomer(Long inboxId, Long CustomerId, int Status, Date Create,
			Long CreatedBy, Date Updated, Long UpdatedBy) {
		
		 inboxCustomer = new InboxCustomer();
		 inboxCustomer.setInboxId(inboxId);
		 inboxCustomer.setCustomerId(CustomerId);
		 inboxCustomer.setStatus(Status);
		 inboxCustomer.setCreated(Create);
		 inboxCustomer.setCreatedby(CreatedBy);
		 inboxCustomer.setUpdated(Updated);
		 inboxCustomer.setUpdatedby(UpdatedBy);
		 customerDao.save(inboxCustomer);
		
	}

	Object onSelectedFromBack() {
		return InboxList.class;
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
	

}
