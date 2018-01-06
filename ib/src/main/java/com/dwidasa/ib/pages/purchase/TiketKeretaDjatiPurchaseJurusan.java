package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TiketKeretaDjatiPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.pages.account.LotteryTransactionResult;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/08/11
 * Time: 11:13
 */
@Import(library = {"context:bprks/js/purchase/PlnPurchaseInput.js"})
public class TiketKeretaDjatiPurchaseJurusan {
	private Logger logger = Logger.getLogger(TiketKeretaDjatiPurchaseJurusan.class);
    @Property
    private String chooseValue;

    @Property
    private String customerReference1;

    @Property
    private String customerReference2;

    @Property
    private boolean saveBoxValue;

    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

    
    @Persist
    private TiketKeretaDjatiPurchaseView tiketKeretaDjatiPurchaseView;
    
    @Persist
    private List<TiketKeretaDjatiPurchaseView> keretaDjatiPurchaseViews;

    @Property
    private int tokenType;

    @Property
    private SelectModel customerReferenceModel;

    @Property
    private SelectModel amountModel;

    @Property
    private SelectModel providerModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private PurchaseService purchaseService;

    @Inject
    private SessionManager sessionManager;

    @InjectPage
    private TiketKeretaDjatiPurchaseKursi djatiPurchaseKursi;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;
    
    
    @Persist
    private String cardNumber;
    
    @Persist
    private String accountNumber;
    
    @Persist
    private Date tglKebarangkatan;
    
    @Persist
    private String dari;
    
    @Persist
    private String tujuan;
    
    
    @Persist
    private List<String> noKursi;
   
    @Persist
    private List<String> Location;
    
    @Property
    private EvenOdd evenOdd;
    
    @Persist
    private String departId;
    
    @Persist
    private String scheduleId;
    
    @Persist
    private String departBranch;
    
    @Persist
    private String destinationBranch;
    
    @Persist
    private String departHour;
    
    @Persist
    private String transactionId;
    
    @Persist
    private String vehicleCode;
    
    
    public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
    
    public String getVehicleCode() {
		return vehicleCode;
	}
    
    
    public void setDepartId(String departId) {
		this.departId = departId;
	}
    
    public String getDepartId() {
		return departId;
	}
    
    public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
    
    public String getScheduleId() {
		return scheduleId;
	}
    
    public void setDepartBranch(String departBranch) {
		this.departBranch = departBranch;
	}
    
    public String getDepartBranch() {
		return departBranch;
	}
    
    public void setDestinationBranch(String destinationBranch) {
		this.destinationBranch = destinationBranch;
	}
    
    public String getDestinationBranch() {
		return destinationBranch;
	}
    
    public void setDepartHour(String departHour) {
		this.departHour = departHour;
	}
    
    public String getDepartHour() {
		return departHour;
	}
    
    public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
    
    public String getTransactionId() {
		return transactionId;
	}
    
    
    
    public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
    
    public String getCardNumber() {
		return cardNumber;
	}
    
    public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
    
    public String getAccountNumber() {
		return accountNumber;
	}
    
    public void setTglKebarangkatan(Date tglKebarangkatan) {
		this.tglKebarangkatan = tglKebarangkatan;
	}
    
    public Date getTglKebarangkatan() {
		return tglKebarangkatan;
	}
    
    public void setDari(String dari) {
		this.dari = dari;
	}
    
    public String getDari() {
		return dari;
	}
    
    public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}
    
    public String getTujuan() {
		return tujuan;
	}
    
    public void setNoKursi(List<String> noKursi) {
		this.noKursi = noKursi;
	}
    
    public List<String> getNoKursi() {
		return noKursi;
	}
    
    public void setLocation(List<String> location) {
		Location = location;
	}
    
    public List<String> getLocation() {
		return Location;
	}
    
    public void onPrepare() {
       
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}    	
    	return null;
    }

    public void setupRender() {

    	evenOdd = new EvenOdd();
        evenOdd.setEven(true);
       
        
    	sessionManager.setSessionLastPage(TiketKeretaDjatiPurchaseJurusan.class.toString());
        chooseValue = "fromId";
        //setTokenType();
        if (tiketKeretaDjatiPurchaseView == null) {
        	tiketKeretaDjatiPurchaseView = new TiketKeretaDjatiPurchaseView();
        }
             
    }

    
    public String getDateFieldFormat() {
		return com.dwidasa.ib.Constants.SHORT_FORMAT;
	}

   
    public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(com.dwidasa.ib.Constants.SHORT_FORMAT);
    	return format.format(getTglKebarangkatan()).toString();
    }
    
    
    public void setKeretaDjatiPurchaseViews(
			List<TiketKeretaDjatiPurchaseView> keretaDjatiPurchaseViews) {
		this.keretaDjatiPurchaseViews = keretaDjatiPurchaseViews;
	}
    
    public List<TiketKeretaDjatiPurchaseView> getKeretaDjatiPurchaseViews() {
		return keretaDjatiPurchaseViews;
	}
    
   
    public void setTiketKeretaDjatiPurchaseView(
			TiketKeretaDjatiPurchaseView tiketKeretaDjatiPurchaseView) {
		this.tiketKeretaDjatiPurchaseView = tiketKeretaDjatiPurchaseView;
	}
    
    public TiketKeretaDjatiPurchaseView getTiketKeretaDjatiPurchaseView() {
		return tiketKeretaDjatiPurchaseView;
	}
    
   
    @DiscardAfter
  	Object onSelectedFromCancel() {
    	keretaDjatiPurchaseViews.clear();
    	return TiketKeretaDjatiPurchaseInput.class;
    }
    
    
  //@DiscardAfter
    Object onViewStatement(String kodeJurusan, BigDecimal amount, int EmptySeat, String departId, int totalSeatAll, int indexCol) {
    	
    	tiketKeretaDjatiPurchaseView = new TiketKeretaDjatiPurchaseView();
    	tiketKeretaDjatiPurchaseView.setKodeJurusan(kodeJurusan);
    	tiketKeretaDjatiPurchaseView.setTicketPricePerSheat(amount);
    	tiketKeretaDjatiPurchaseView.setTotalEmptySheat(EmptySeat);
    	List<TiketKeretaDjatiPurchaseView> djatiPurchaseViews = new ArrayList<TiketKeretaDjatiPurchaseView>();
    	
    	System.out.println("Size "+getNoKursi().size());
    	int x = 1;
    	String data = "";
    	for(int i=0; i<EmptySeat; i++){
    		
    		String noKursi = getNoKursi().get(i);
    		data = ""+i;
           
                  TiketKeretaDjatiPurchaseView temp = new TiketKeretaDjatiPurchaseView();
                  temp.setNoKursi(""+Integer.parseInt(noKursi));
                  djatiPurchaseViews.add(temp);
     
    	}
    	
    	List<TiketKeretaDjatiPurchaseView> djatiPurchaseViewLokasi = new ArrayList<TiketKeretaDjatiPurchaseView>();
    	
    	System.out.println("Size "+getLocation().size());
    	
    	
    	for(int i=0; i<getLocation().size(); i++){
    		
    		String lokasi = getLocation().get(i);
    		int indexLokasi = Integer.parseInt(lokasi.substring(lokasi.lastIndexOf(";")).replace(";", ""));
    		//data = "" + i;
    		if(indexCol == indexLokasi){
    			TiketKeretaDjatiPurchaseView temp = new TiketKeretaDjatiPurchaseView();
    			temp.setLokasiKeberangkatan(StringUtils.substringBefore(lokasi, ";"));
    			djatiPurchaseViewLokasi.add(temp);
    		}
     
    	}
    	
    	djatiPurchaseKursi.setTiketKeretaDjatiPurchaseView(tiketKeretaDjatiPurchaseView);
    	djatiPurchaseKursi.setCardNumber(getCardNumber());
    	djatiPurchaseKursi.setDariRekening(getAccountNumber());
    	djatiPurchaseKursi.setDari(getDari());
    	djatiPurchaseKursi.setTujuan(getTujuan());
    	djatiPurchaseKursi.setKodeJurusan(tiketKeretaDjatiPurchaseView.getKodeJurusan());
    	djatiPurchaseKursi.setHargaTiket(tiketKeretaDjatiPurchaseView.getTicketPricePerSheat());
    	djatiPurchaseKursi.setTglKeberangkatan(getTglKebarangkatan());
    	djatiPurchaseKursi.setDjatiPurchaseViews(djatiPurchaseViews);
    	djatiPurchaseKursi.setDjatiPurchaseViewLokasi(djatiPurchaseViewLokasi);
    	djatiPurchaseKursi.setTotalSeat(totalSeatAll);
    	djatiPurchaseKursi.setDepartId(departId);
    	djatiPurchaseKursi.setScheduleId(getScheduleId());
    	djatiPurchaseKursi.setDepartBranch(getDepartBranch());
    	djatiPurchaseKursi.setDestinationBranch(getDestinationBranch());
    	djatiPurchaseKursi.setTransactionId(getTransactionId());
    	djatiPurchaseKursi.setTotalEmpty(EmptySeat);
    	djatiPurchaseKursi.setVehicleCode(getVehicleCode());
        return djatiPurchaseKursi; 
        
        
    }
    
    @Property
	private final ValueEncoder<TiketKeretaDjatiPurchaseView> encoder = new ValueEncoder<TiketKeretaDjatiPurchaseView>() {
		public String toClient(TiketKeretaDjatiPurchaseView value) {
			return String.valueOf(value.getCustomerId());
		}

		public TiketKeretaDjatiPurchaseView toValue(String clientValue) {
			return new TiketKeretaDjatiPurchaseView();
		}
	};
    
	
	public String getMasked(String str) {
     	return EngineUtils.getCardNumberMasked(str);
     }
    
    
}
