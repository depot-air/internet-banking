package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.SelectModelVisitor;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.InboxCustomer;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
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
import com.dwidasa.ib.services.LocationListSelect;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import com.dwidasa.ib.services.TransferListSelect;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/08/11
 * Time: 11:13
 */
@Import(library = {"context:bprks/js/purchase/PlnPurchaseInput.js"})
public class TiketKeretaDjatiPurchaseKursi {
	private Logger logger = Logger.getLogger(TiketKeretaDjatiPurchaseKursi.class);
    @Property
    private String chooseValue;

    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

    @Persist
    @Property
    private TiketKeretaDjatiPurchaseView djatiPurchaseView;
    
    @Persist
    private List<TiketKeretaDjatiPurchaseView> djatiPurchaseViews;
    
    @Persist
    private List<TiketKeretaDjatiPurchaseView> djatiPurchaseViewLokasi;
    
    @Property
    private int tokenType;
    
    @InjectPage 
    private TiketKeretaDjatiPurchaseRegistration djatiPurchaseRegistration;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private PurchaseService purchaseService;

    @Inject
    private SessionManager sessionManager;


    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    
    @Property
    private EvenOdd evenOdd;
    
    @Persist
    private String cardNumber;
    
    @Persist
    private String dariRekening;
    
    @Persist
    private String dari;
    
    @Persist
    private String tujuan;
    
    @Persist
    private String kodeJurusan;
    
    @Persist
    private BigDecimal hargaTiket;
    
    @Persist
    private Date tglKeberangkatan;
    
    @Persist
    private List<String> location;
    
    @Property
    private SelectModel selectLocation;
    
    @Property
    private String locationName;
    
    @Persist
    private int TotalSeat;
    
    @Persist
    private String vehicleCode;
   
    
    @Property
    private String selectNomorKursi;
    
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
    private int totalEmpty;
    
    
    
    public List<TiketKeretaDjatiPurchaseView> getAllKursi() {
    	
    	List<TiketKeretaDjatiPurchaseView> list;

    	list = getDjatiPurchaseViews();

        return list;
    }
    
    
    public void onPrepare() {
       
    }

    public Object onActivate() {
    	
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
//    	if (this.maxRow == 0) maxRow = 1;
    	return null;
    }

    public void setupRender() {

    	evenOdd = new EvenOdd();
        evenOdd.setEven(true);
        
        buildLocationModel();
    	sessionManager.setSessionLastPage(TiketKeretaDjatiPurchaseKursi.class.toString());
        chooseValue = "fromId";
        //setTokenType();
        if (djatiPurchaseView == null) {
        	djatiPurchaseView = new TiketKeretaDjatiPurchaseView();
        }
    }


	public Object onSuccess() {
        
        return djatiPurchaseRegistration;
    }
	
	
	public String getAllColAndSeat(){
    	
    	String data = "";
    	
    	for(TiketKeretaDjatiPurchaseView keretaDjatiPurchaseView : getDjatiPurchaseViews()){
    		    		
    		if("".equals(data)){
    			data += ""+Integer.parseInt(keretaDjatiPurchaseView.getNoKursi());
    		}else{
    			data += ","+Integer.parseInt(keretaDjatiPurchaseView.getNoKursi());
    		}
    	}
    	
    	return data;
    }
    
	public Set<String> getAllSeatNotEmpty() {

		String data[] = getAllColAndSeat().split(",");
		final Set<String> setToReturn = new HashSet<String>();
        final Set<String> set1 = new HashSet<String>();
		
		List<String> list = new LinkedList<String>();

		for (int i = 1; i <= getTotalSeat(); i++) {
			list.add(String.valueOf(i));
		}

		for (int i = 0; i < data.length; i++) {
			list.add(String.valueOf(data[i]));
		}
	
		return findDuplicates(list);

	}
	
	//Jumlah Keseluruhan Seat /4 dikarenakan layoutnya 4
	public int getTotalPerShet(){
		return getTotalSeat() / 4;
	}
    
	//Menentukan Tempat Duduk Yang Sudah Ditempatin
    public String getAllColAndSeatNotEmpty(){
    	
    	String data = "";
    	
    	for(String Nomor : getAllSeatNotEmpty()){
    		    		
    		int nomorSeat = Integer.parseInt(Nomor);
    		String nomor = "";
    		if(nomorSeat > 0 && nomorSeat < 10){
    			nomor = "0"+nomorSeat;
    		}else{
    			nomor = ""+nomorSeat;
    		}
    		if("".equals(data)){
    			
				if (nomor.equals("01")) {
					data += ".col1 .seat1";
				} else if (nomor.equals("02")) {
					data += ".col2 .seat1";
				} else if (nomor.equals("03")) {
					data += ".col3 .seat1";
				} else if (nomor.equals("04")) {
					data += ".col4 .seat1";
				} else if (nomor.equals("05")) {
					data += ".col1 .seat2";
				} else if (nomor.equals("06")) {
					data += ".col2 .seat2";
				} else if (nomor.equals("07")) {
					data += ".col3 .seat2";
				} else if (nomor.equals("08")) {
					data += ".col4 .seat2";
				} else if (nomor.equals("09")) {
					data += ".col1 .seat3";
				} else if (nomor.equals("10")) {
					data += ".col2 .seat3";
				}else if (nomor.equals("11")) {
					data += ".col3 .seat3";
				} else if (nomor.equals("12")) {
					data += ".col4 .seat3";
				} else if (nomor.equals("13")) {
					data += ".col1 .seat4";
				} else if (nomor.equals("14")) {
					data += ".col2 .seat4";
				} else if (nomor.equals("15")) {
					data += ".col3 .seat4";
				} else if (nomor.equals("16")) {
					data += ".col4 .seat4";
				} else if (nomor.equals("17")) {
					data += ".col1 .seat5";
				} else if (nomor.equals("18")) {
					data += ".col2 .seat5";
				} else if (nomor.equals("19")) {
					data += ".col3 .seat5";
				} else if (nomor.equals("20")) {
					data += ".col4 .seat5";
				}else if (nomor.equals("21")) {
					data += ".col1 .seat6";
				} else if (nomor.equals("22")) {
					data += ".col2 .seat6";
				} else if (nomor.equals("23")) {
					data += ".col3 .seat6";
				} else if (nomor.equals("24")) {
					data += ".col4 .seat6";
				} else if (nomor.equals("25")) {
					data += ".col1 .seat7";
				} else if (nomor.equals("26")) {
					data += ".col2 .seat7";
				} else if (nomor.equals("27")) {
					data += ".col3 .seat7";
				} else if (nomor.equals("28")) {
					data += ".col4 .seat7";
				} else if (nomor.equals("29")) {
					data += ".col1 .seat8";
				} else if (nomor.equals("30")) {
					data += ".col2 .seat8";
				} else if (nomor.equals("31")) {
					data += ".col3 .seat8";
				} else if (nomor.equals("32")) {
					data += ".col4 .seat8";
				}else if (nomor.equals("33")) {
					data += ".col1 .seat9";
				} else if (nomor.equals("34")) {
					data += ".col2 .seat9";
				} else if (nomor.equals("35")) {
					data += ".col3 .seat9";
				} else if (nomor.equals("36")) {
					data += ".col4 .seat9";
				}
				else if (nomor.equals("37")) {
					data += ".col1 .seat10";
				} else if (nomor.equals("38")) {
					data += ".col2 .seat10";
				} else if (nomor.equals("39")) {
					data += ".col3 .seat10";
				} else if (nomor.equals("40")) {
					data += ".col4 .seat10";
				}
    			
				else if (nomor.equals("41")) {
					data += ".col1 .seat11";
				} else if (nomor.equals("42")) {
					data += ".col2 .seat11";
				} else if (nomor.equals("43")) {
					data += ".col3 .seat11";
				} else if (nomor.equals("44")) {
					data += ".col4 .seat11";
				}
    			
				else if (nomor.equals("45")) {
					data += ".col1 .seat12";
				} else if (nomor.equals("46")) {
					data += ".col2 .seat12";
				} else if (nomor.equals("47")) {
					data += ".col3 .seat12";
				} else if (nomor.equals("48")) {
					data += ".col4 .seat12";
				}
    			
				else if (nomor.equals("49")) {
					data += ".col1 .seat13";
				} else if (nomor.equals("50")) {
					data += ".col2 .seat13";
				} else if (nomor.equals("51")) {
					data += ".col3 .seat13";
				} else if (nomor.equals("52")) {
					data += ".col4 .seat13";
				}
					
    			
    			
    		}else{
    			
    			if(nomor.equals("01")){
        			data += ", .col1 .seat1";
        		}else
				if (nomor.equals("02")) {
					data += ", .col2 .seat1";
				} else if (nomor.equals("03")) {
					data += ", .col3 .seat1";
				} else if (nomor.equals("04")) {
					data += ", .col4 .seat1";
				} else if (nomor.equals("05")) {
					data += ", .col1 .seat2";
				} else if (nomor.equals("06")) {
					data += ", .col2 .seat2";
				} else if (nomor.equals("07")) {
					data += ", .col3 .seat2";
				} else if (nomor.equals("08")) {
					data += ", .col4 .seat2";
				} else if (nomor.equals("09")) {
					data += ", .col1 .seat3";
				} else if (nomor.equals("10")) {
					data += ", .col2 .seat3";
				}else if (nomor.equals("11")) {
					data += ", .col3 .seat3";
				} else if (nomor.equals("12")) {
					data += ", .col4 .seat3";
				} else if (nomor.equals("13")) {
					data += ", .col1 .seat4";
				} else if (nomor.equals("14")) {
					data += ", .col2 .seat4";
				} else if (nomor.equals("15")) {
					data += ", .col3 .seat4";
				} else if (nomor.equals("16")) {
					data += ", .col4 .seat4";
				} else if (nomor.equals("17")) {
					data += ", .col1 .seat5";
				} else if (nomor.equals("18")) {
					data += ", .col2 .seat5";
				} else if (nomor.equals("19")) {
					data += ", .col3 .seat5";
				} else if (nomor.equals("20")) {
					data += ", .col4 .seat5";
				}else if (nomor.equals("21")) {
					data += ", .col1 .seat6";
				} else if (nomor.equals("22")) {
					data += ", .col2 .seat6";
				} else if (nomor.equals("23")) {
					data += ", .col3 .seat6";
				} else if (nomor.equals("24")) {
					data += ", .col4 .seat6";
				} else if (nomor.equals("25")) {
					data += ", .col1 .seat7";
				} else if (nomor.equals("26")) {
					data += ", .col2 .seat7";
				} else if (nomor.equals("27")) {
					data += ", .col3 .seat7";
				} else if (nomor.equals("28")) {
					data += ", .col4 .seat7";
				} else if (nomor.equals("29")) {
					data += ", .col1 .seat8";
				} else if (nomor.equals("30")) {
					data += ", .col2 .seat8";
				} else if (nomor.equals("31")) {
					data += ", .col3 .seat8";
				} else if (nomor.equals("32")) {
					data += ", .col4 .seat8";
				}else if (nomor.equals("33")) {
					data += ", .col1 .seat9";
				} else if (nomor.equals("34")) {
					data += ", .col2 .seat9";
				} else if (nomor.equals("35")) {
					data += ", .col3 .seat9";
				} else if (nomor.equals("36")) {
					data += ", .col4 .seat9";
				}
				else if (nomor.equals("37")) {
					data += ", .col1 .seat10";
				} else if (nomor.equals("38")) {
					data += ", .col2 .seat10";
				} else if (nomor.equals("39")) {
					data += ", .col3 .seat10";
				} else if (nomor.equals("40")) {
					data += ", .col4 .seat10";
				}
    			
				else if (nomor.equals("41")) {
					data += ", .col1 .seat11";
				} else if (nomor.equals("42")) {
					data += ", .col2 .seat11";
				} else if (nomor.equals("43")) {
					data += ", .col3 .seat11";
				} else if (nomor.equals("44")) {
					data += ", .col4 .seat11";
				}
    			
				else if (nomor.equals("45")) {
					data += ", .col1 .seat12";
				} else if (nomor.equals("46")) {
					data += ", .col2 .seat12";
				} else if (nomor.equals("47")) {
					data += ", .col3 .seat12";
				} else if (nomor.equals("48")) {
					data += ", .col4 .seat12";
				}
    			
				else if (nomor.equals("49")) {
					data += ", .col1 .seat13";
				} else if (nomor.equals("50")) {
					data += ", .col2 .seat13";
				} else if (nomor.equals("51")) {
					data += ", .col3 .seat13";
				} else if (nomor.equals("52")) {
					data += ", .col4 .seat13";
				}
    			
    		}
    	}
    	
    	return data;
    }
	
	
	void onValidateFromForm() {
 
		
    	try {
    		
    		setTiketRegistration(selectNomorKursi);
    		
    		
		} catch (Exception e) {
			
			form.recordError(messages.get("kursi-requiredIf-message"));// TODO: handle exception
		}
    	
    }
	
    
    private void setTiketRegistration(String toString) {
		
    	String tmpt[] = toString.split(",");
		String detailKursi = "";
		for(int i=0; i<tmpt.length; i++){
			String nomor = "";
			if(Integer.parseInt(tmpt[i]) > 0 && Integer.parseInt(tmpt[i]) < 10){
				nomor = tmpt[i];
			}else{
				nomor = tmpt[i];
			}
			if("".equals(detailKursi)){
				detailKursi += ""+nomor;
			}else{
				detailKursi += ","+nomor;
			}
		}
		System.out.println("Detail "+detailKursi);
		AccountInfo ai = sessionManager.getAccountInfo(getDariRekening());
		djatiPurchaseView = new TiketKeretaDjatiPurchaseView();
		djatiPurchaseView.setAccountType(ai.getAccountType());
		djatiPurchaseView.setCardNumber(ai.getCardNumber());
		djatiPurchaseView.setAccountNumber(ai.getAccountNumber());
		djatiPurchaseView.setDari(getDari());
		djatiPurchaseView.setTujuan(getTujuan());
		djatiPurchaseView.setKodeJurusan(getKodeJurusan());
		djatiPurchaseView.setTglKeberangkatan(getTglKeberangkatan());
		djatiPurchaseView.setTicketPricePerSheat(getHargaTiket());
		djatiPurchaseView.setScheduleCodeId(StringUtils.substringBefore(locationName, ":").replace(" ", ""));
		djatiPurchaseRegistration.setLokasi(StringUtils.substringBefore(locationName, " pkl").substring(StringUtils.substringBefore(locationName, "pkl").lastIndexOf(":")).replace(": ", ""));
		djatiPurchaseRegistration.setTiketKeretaDjatiPurchaseView(djatiPurchaseView);
		djatiPurchaseRegistration.setDetailKursi(detailKursi);
		djatiPurchaseRegistration.setTotalKursi(tmpt.length);
		djatiPurchaseRegistration.setScheduleId(StringUtils.substringBefore(locationName, ":").replace(" ", ""));
		djatiPurchaseRegistration.setDepartId(getDepartId());
		djatiPurchaseRegistration.setDepartBranch(StringUtils.substringBefore(locationName, " pkl").substring(StringUtils.substringBefore(locationName, "pkl").lastIndexOf(":")).replace(": ", ""));
		djatiPurchaseRegistration.setDestinationBranch("");
		djatiPurchaseRegistration.setTransactionId(getTransactionId());
		djatiPurchaseRegistration.setDepartHour(StringUtils.substringBefore(StringUtils.substringAfter(locationName, "(").replace(")", ""), " ,"));
    	djatiPurchaseRegistration.setTotalEmpty(getTotalEmpty());
    	djatiPurchaseRegistration.setVehicleCode(getVehicleCode());
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
	
	
	public static Set<String> findDuplicates(List<String> listContainingDuplicates) {
		 
        final Set<String> setToReturn = new HashSet<String>();
        final Set<String> set1 = new HashSet<String>();
 
        for (String yourInt : listContainingDuplicates) {
            
            if (!set1.add(yourInt)) {
                setToReturn.add(yourInt);
            }
        }
        
        for(String rmv : setToReturn){
            set1.remove(rmv);
        }
        return set1;
    }
    
	
	
	public void setTotalSeat(int totalSeat) {
		TotalSeat = totalSeat;
	}
    
    public int getTotalSeat() {
		return TotalSeat;
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
    
    public void setTotalEmpty(int totalEmpty) {
		this.totalEmpty = totalEmpty;
	}
    
    public int getTotalEmpty() {
		return totalEmpty;
	}
    
    public void buildLocationModel() {
    	if(getHargaTiket()!=null){
    		selectLocation = new LocationListSelect(djatiPurchaseViewLokasi);
    	}else{
    		djatiPurchaseViewLokasi.clear();
    		selectLocation = new LocationListSelect(djatiPurchaseViewLokasi);
    	}
    }
  
    
    public String getDateFieldFormat() {
		return com.dwidasa.ib.Constants.SHORT_FORMAT;
	}

   
    
    public void setTiketKeretaDjatiPurchaseView(
			TiketKeretaDjatiPurchaseView tiketKeretaDjatiPurchaseView) {
		this.djatiPurchaseView = tiketKeretaDjatiPurchaseView;
	}
    
    public TiketKeretaDjatiPurchaseView getTiketKeretaDjatiPurchaseView() {
		return djatiPurchaseView;
	}
    
    public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(com.dwidasa.ib.Constants.SHORT_FORMAT);
    	return format.format(tglKeberangkatan).toString();
    }
    
    
    public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
    
    public String getCardNumber() {
		return cardNumber;
	}
    
    public void setDariRekening(String dariRekening) {
		this.dariRekening = dariRekening;
	}
    
    public String getDariRekening() {
		return dariRekening;
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
    
    public void setKodeJurusan(String kodeJurusan) {
		this.kodeJurusan = kodeJurusan;
	}
    
    public String getKodeJurusan() {
		return kodeJurusan;
	}
    
    public void setHargaTiket(BigDecimal hargaTiket) {
		this.hargaTiket = hargaTiket;
	}
    
    public BigDecimal getHargaTiket() {
		return hargaTiket;
	}
    
    public void setTglKeberangkatan(Date tglKeberangkatan) {
		this.tglKeberangkatan = tglKeberangkatan;
	}
    
    public Date getTglKeberangkatan() {
		return tglKeberangkatan;
	}
    
    
    public void setDjatiPurchaseViews(
			List<TiketKeretaDjatiPurchaseView> djatiPurchaseViews) {
		this.djatiPurchaseViews = djatiPurchaseViews;
	}
    
    
    public List<TiketKeretaDjatiPurchaseView> getDjatiPurchaseViews() {
		return djatiPurchaseViews;
	}
    
    
    public void setDjatiPurchaseViewLokasi(
			List<TiketKeretaDjatiPurchaseView> djatiPurchaseViewLokasi) {
		this.djatiPurchaseViewLokasi = djatiPurchaseViewLokasi;
	}
    
    public List<TiketKeretaDjatiPurchaseView> getDjatiPurchaseViewLokasi() {
		return djatiPurchaseViewLokasi;
	}
  
    public void setLocation(List<String> location) {
		this.location = location;
	}
    
    public List<String> getLocation() {
		return location;
	}
    
    
    public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
    
    public String getVehicleCode() {
		return vehicleCode;
	}
	
    public String getMasked(String str) {
     	return EngineUtils.getCardNumberMasked(str);
     }
    
}
