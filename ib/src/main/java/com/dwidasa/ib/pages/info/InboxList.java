package com.dwidasa.ib.pages.info;

import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.InboxCustomer;
import com.dwidasa.engine.service.InboxService;
import com.dwidasa.ib.services.SessionManager;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sisca
 * Date: 9/7/11
 * Time: 9:34 PM
 */
public class InboxList {
    @Property
    private InboxCustomer inboxCustomer;

    @Property
    private Inbox inbox;
    
    @Inject
    private InboxCustomerDao inboxCustomerDao;

    @Property
    @InjectPage
    private DetailsInbox detailsPage;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private InboxService inboxService;
    
    

    @Property
    private String nowDate;
    
    private boolean check;
    
  
    @Property
    private int NoAwal;
    
    
    @Property
    @Persist
    private int TotalPage;
    
    @Persist(PersistenceConstants.SESSION)
    @Property
    private int maxRow;
    
    @Property(write=true)
    @Persist
    private int page;
    
    
    @Property
    @Persist(PersistenceConstants.FLASH)
    private int index;
    
    
    @Property
    private List<InboxCustomer> tabelInboxCustomer;

    public List<InboxCustomer> getAllInbox() {
    	
    	int awal = (maxRow*10)-9;   		
    	int akhir = (maxRow*10);
    
        System.out.println(""+awal);
        System.out.println(""+akhir);
    	
        tabelInboxCustomer = inboxService.getCustomerInboxesAll(sessionManager.getLoggedCustomerView().getId(), awal, akhir, null);
        
        if(tabelInboxCustomer.size() == 0){
        	
        	NoAwal = 1;
        	TotalPage = 1;
        
        }else{
        	
        	NoAwal = 1;
        	TotalPage = (int) Math.ceil(inboxService.getCustomerInboxes(sessionManager.getLoggedCustomerView().getId(), null).size() * 1.0 / 10);
        	
        }
        
        return tabelInboxCustomer;
    }

    @SuppressWarnings("unchecked")
    void setupRender() {
        setAccountViewDate();
    }

    private void setAccountViewDate() {
        Date date = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
        nowDate = sdate.format(date);
        nowDate = nowDate;
    }
    
    
    public String getDateFormat(){
    	SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
    	return sdate.format(inboxCustomer.getInbox().getStartDate());
    }
    

    Object onAction (Long idInbox, long id) {
        detailsPage.onActivate(idInbox, id);
        return detailsPage;
    }
    
    public String getInboxStatus(int Status){
    	if(Status == 0){
    		return "Belum Terbaca";
    	}else
    		return "Sudah Terbaca";
    }
    
    public String getStatus(int Status){
    	if(Status == 0){
    		return "Belum Terbaca";
    	}else
    		return "";
    }
    
    
    public void setCheck(boolean check) {
		if(check){
			
			Long userId = sessionManager.getLoggedCustomerPojo().getId();
			inboxCustomerDao.remove(inboxCustomer.getId(), userId);
		}
	}
    
    public boolean isCheck() {
		return check;
	}
    
    @Property
    private final ValueEncoder<InboxCustomer> encoder = new ValueEncoder<InboxCustomer> () {

		@Override
		public String toClient(InboxCustomer value) {
			return String.valueOf(value.getId());
		}

		@Override
		public InboxCustomer toValue(String clientValue) {
			InboxCustomer obj = new InboxCustomer();
			obj.setId(Long.valueOf(clientValue));
			return obj;
		}
    	
    };
    
    
    Object onActionFromViewWrite(long id) {
        
         return detailsPage;
    }
    
    Object onActionFromViewRead(long id) {
        
         return detailsPage;
    }
    
    
    void onPassivate() {
  	  if (this.maxRow == 0) maxRow = 1;
  	  if (this.page == 0) page = 10;
   	}

      void onActivate() {
      	if (this.maxRow == 0) maxRow = 1;
  	}


  	void onActivate(int maxRow, int curPage) {
  		this.page = maxRow;
  		this.maxRow = curPage;
     
  	}

  	public int getPageNext() {
  		return maxRow+1;
  	}

   
  	public int getPagePrev() {
  		return maxRow-1;
  	}


  	public boolean isFirstPage() {
  		if (maxRow == 1) return true; else return false;
  	}

  	public boolean isLastPage() {
  		if (maxRow == TotalPage) return true; else return false;
  	}

  	public boolean isEnable(){
  		if(maxRow == index)return true; else return false;
  	}

  	public int getNoAwalPag(){
  	
  		return Math.max(1, maxRow - 5);
  	
  	}
  	
  	
  	public int getNoPaging(){
  		
		int begin = Math.max(1, maxRow - 5);
		int end = Math.min(begin + 10, TotalPage);
   
		return end;
	
	}
  	
  	
  	void onActionFromDelete(long id){
  		Long userId = sessionManager.getLoggedCustomerPojo().getId();
  		inboxCustomerDao.remove(id, userId);
    }
  	
  	
  	void onActionFromDeleteUnread(long id){
  		Long userId = sessionManager.getLoggedCustomerPojo().getId();
  		inboxCustomerDao.remove(id, userId);
    }
 
}
