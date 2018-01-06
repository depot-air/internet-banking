package com.dwidasa.ib.pages.account;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.services.SessionManager;


public class LotteryTransactionResultUndian {

	
	@Property(write = false)
    @Persist
    private LotteryTransactionView lotteryView;
	
	
	
    @InjectComponent
    private Form form;
    
    @Property(write = false)
    @Persist
    private String periodMonth;
    
    @Property
    private int pageSize;
    
    @Inject
    private PurchaseService purchaseService;
  
    @Persist
    private List<LotteryTransactionView> lotteryViews;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private AccountService accountService;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    @Property
    private EvenOdd evenOdd;

    @Property
    private String customerName;
    
    @Persist
    private String accountNumber;
    @Persist
    private String kodeUndian;
    @Persist
    private String jenisUndian;
    @Persist
    private BigDecimal TotalPerolehan;
   
    @Persist
    private Date tglAwalPeriod;
    @Persist
    private Date tglAkhirPeriod;
    
    
    private String kosong;
    
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
    
    public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
    
    public String getAccountNumber() {
		return accountNumber;
	}
    
    public void setKodeUndian(String kodeUndian) {
		this.kodeUndian = kodeUndian;
	}
    
    public String getKodeUndian() {
		return kodeUndian;
	}
    
    public void setJenisUndian(String jenisUndian) {
		this.jenisUndian = jenisUndian;
	}
    
    public String getJenisUndian() {
		return jenisUndian;
	}
    
    
    public void setTotalPerolehan(BigDecimal totalPerolehan) {
		TotalPerolehan = totalPerolehan;
	}
    
    public BigDecimal getTotalPerolehan() {
		return TotalPerolehan;
	}
   
    public void setLotteryView(LotteryTransactionView lotteryView) {
		this.lotteryView = lotteryView;
	}
    
    public void setPeriodMonth(String periodMonth) {
        this.periodMonth = periodMonth;
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
    
    void setupRender() {
    	
    	if(getTotalPerolehan().equals(BigDecimal.ZERO)){
        	form.recordError("Anda tidak memiliki nomor undian periode ini");
        	setKosong("0");
        }else{
        	setKosong("");
        }
    	
    	pageSize = com.dwidasa.ib.Constants.PAGE_SIZE;
        evenOdd = new EvenOdd();
        evenOdd.setEven(true);
        
        
    }
 
//    public String getAwalPeriod(){
//    	SimpleDateFormat format = new SimpleDateFormat("MMMM / yyyy");
//    	return format.format(getBlnTahun()).toString();
//    }
    
    public String getTglAwalPeriode(){
    	SimpleDateFormat format = new SimpleDateFormat(com.dwidasa.ib.Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    	return format.format(getTglAwalPeriod()).toString();
    }
    
    public String getTglAkhirPeriode(){
    	SimpleDateFormat format = new SimpleDateFormat(com.dwidasa.ib.Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    	return format.format(getTglAkhirPeriod()).toString();
    }
    

    public void setLotteryViews(List<LotteryTransactionView> lotteryViews) {
		this.lotteryViews = lotteryViews; 
	}
    
    public List<LotteryTransactionView> getLotteryViews() {
    	return lotteryViews;
	}
    
    public List<LotteryTransactionView> getAllLotteryTransaction(){
    	
    	int awal = (maxRow*10)-10;   		
    	int akhir = (maxRow*10);
    	
    	 System.out.println(""+awal);
         System.out.println(""+akhir);
    	
    	List<LotteryTransactionView> transactionViews = null;
    	try {
    		transactionViews = getLotteryViews().subList(awal, akhir);
		} catch (Exception e) {
			transactionViews = getLotteryViews().subList(awal, (akhir*0)+getLotteryViews().size());// TODO: handle exception
		}
    	
    	
    	if(getLotteryViews().size() == 0){
        	
        	NoAwal = 1;
        	TotalPage = 1;
        
        }else{
        	
        	NoAwal = 1;
            TotalPage = (int) Math.ceil(getLotteryViews().size() * 1.0 / 10);
        	
        }
    	
    	return transactionViews;
    	
    }
    
    
    public void setTglAwalPeriod(Date tglAwalPeriod) {
		this.tglAwalPeriod = tglAwalPeriod;
	}
    
    public Date getTglAwalPeriod() {
		return tglAwalPeriod;
	}
    
    
    public void setTglAkhirPeriod(Date tglAkhirPeriod) {
		this.tglAkhirPeriod = tglAkhirPeriod;
	}
    
    public Date getTglAkhirPeriod() {
		return tglAkhirPeriod;
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
  	
  	@DiscardAfter
  	Object onSelectedFromCancel() {
        return LotteryTransactionResult.class;
    }
  	
  	
  	@Property
	private final ValueEncoder<LotteryTransactionView> encoder = new ValueEncoder<LotteryTransactionView>() {
		public String toClient(LotteryTransactionView value) {
			return String.valueOf(value.getAccountNumber());
		}

		public LotteryTransactionView toValue(String clientValue) {
			return new LotteryTransactionView();
		}
	};
  	
  	public void setKosong(String kosong) {
		this.kosong = kosong;
	}
  	
  	public String getKosong() {
		return kosong;
	}
  
}
