package com.dwidasa.admin.pages.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.GridSortModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.InboxCustomer;
import com.dwidasa.engine.model.User;
import org.apache.commons.net.ftp.FTP;

@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, Constants.RoleName.DAY_ADMIN, Constants.RoleName.SUPERUSER})
public class InboxCustomerList {
	@Property
    private GridDataSource dataSource;

	@InjectContainer
    private Grid grid;
	
    @Property
    private int pageSize;

    @Property
    private Customer row;

    @Property
    @Persist
    private String strSearch;
    
    @Persist
    private long IdInbox;
    
    @Property
    @Persist
    private String chooseValue;
    
    
    @Property
    private InboxCustomer inboxCustomer;
    
    @Inject
    private InboxCustomerDao customerDao;
    
    private boolean check;
    
    @Inject
    private CustomerDao customerDao2;
    
    @InjectPage
    private InboxList inboxList;
    
    @Inject
	private SessionManager sessionManager;
    
    void onActivate(long id) {
    	setIdInbox(id);
    	
    }

    @SuppressWarnings("unchecked")
    void setupRender() {
    
    }
    
  
    public List<Customer> getAllCustomer() {
    
    	
    	List<Customer> customers = null; 
    	
    	
    	try {
    		if(chooseValue.equalsIgnoreCase("fromMerchant")){
        		
    			try {
					
    				if (strSearch == null || strSearch.trim().equals("")) {
        				
        				customers = customerDao2.getByCustomerIsMerchant(); 
        			}else{
        				customers = customerDao2.getByCustomerIsMerchantByName(strSearch, strSearch); 
        			}
    				
				} catch (Exception e) {
					// TODO: handle exception
				}
    			
    	    	
        		
        	}else if(chooseValue.equalsIgnoreCase("fromIndividual")){
        		
        		try {
					
        			if (strSearch == null || strSearch.trim().equals("")) {
            			
            			customers = customerDao2.getByCustomerIsIndividual();
            			
            		}else{
            		
            			customers = customerDao2.getByCustomerIsIndividualByName(strSearch, strSearch);
            			
            		}
        			
				} catch (Exception e) {
					// TODO: handle exception
				}
        		
        	
        	}else if(chooseValue.equalsIgnoreCase("fromMerchantIndividual")){
        		
        		
        		try {
					
        			if (strSearch == null || strSearch.trim().equals("")) {
            			
            			customers = customerDao2.getAll();
            			
            		}else{
            		
            			customers = customerDao2.getByCustomerByCustomerName(strSearch, strSearch);
            			
            		}
        			
				} catch (Exception e) {
					// TODO: handle exception
				}
        		
        	}
    		
    		 return customers;
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    
       return customers;
        
    }

    @DiscardAfter
    Object onSelectedFromReset() {
        return InboxList.class;
    }
    
    
    @DiscardAfter
    Object onSelectedFromadd() {
        return InboxList.class;
    }
    
    public void setIdInbox(long idInbox) {
		IdInbox = idInbox;
	}
    
    
    public long getIdInbox() {
		return IdInbox;
	}
    
    @Inject
    private Messages messages;
    
    public String getStrStatus(int status) {
    	if (status == 1) {
    		return messages.get("aktif");	
    	} else {
    		return messages.get("tidakAktif");
    	}
    	
    }
    
    
     public String getCheckStatus(long id, long CustomerId){
         List<InboxCustomer> customerList = customerDao.getAllWithInboxId(id, CustomerId, null);
                String status = "";
          for(InboxCustomer f : customerList) {
	       status = "Hapus";
	  }
          
          return status;
                
     }
    

        public boolean isCheck() {
            
		return check;
	}
        
        
        public void setCheck(boolean check) {
			if(check){

                            if(getCheckStatus(getIdInbox(), row.getId()).equals("")){
                                
                                User user = sessionManager.getLoggedUser(); 
				
				 inboxCustomer = new InboxCustomer();
				 inboxCustomer.setInboxId(getIdInbox());
				 inboxCustomer.setCustomerId(row.getId());
				 inboxCustomer.setStatus(0);
				 inboxCustomer.setCreated(new Date());
				 inboxCustomer.setCreatedby(user.getId());
				 inboxCustomer.setUpdated(new Date());
				 inboxCustomer.setUpdatedby(user.getId());
				 customerDao.save(inboxCustomer);
                                 
                            }
				 
			}
			
		}
        
      
        
        @Property
        private final ValueEncoder<Customer> encoder = new ValueEncoder<Customer> () {

    		@Override
    		public String toClient(Customer value) {
    			return String.valueOf(value.getId());
    		}

    		@Override
    		public Customer toValue(String clientValue) {
    			Customer obj = new Customer();
    			obj.setId(Long.valueOf(clientValue));
    			return obj;
    		}
        	
        };
        
      
	    void onActionFromDelete(Long id, Long customerId) {
	    	 customerDao.removeByIdCus(id, customerId);
	    }
        
        
        
}
