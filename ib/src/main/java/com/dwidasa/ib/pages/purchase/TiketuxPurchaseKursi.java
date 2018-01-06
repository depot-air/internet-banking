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
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

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
public class TiketuxPurchaseKursi {
	private Logger logger = Logger.getLogger(TiketuxPurchaseKursi.class);
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
    private TiketuxPurchaseRegistration djatiPurchaseRegistration;

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
    private String productCode;
    
    @Persist
    private String vechileCode;
   
    
    @Property
    private String hiddendjati1, hiddendjati2, 
    			   hiddendjati3, hiddendjati4, hiddendjati5, hiddendjati6, hiddendjati7, hiddendjati8, hiddendjati9, hiddendjati10, hiddendjati11, 
    			   hiddendjati12, 
    			   hiddendjati13, 
    			   hiddendjati14, 
    			   hiddendjati15,
    			   hiddendjati16,
    			   hiddendjati17,
    			   hiddendjati18,
    			   hiddendjati19, hiddendjati20,hiddendjati21, 
    			   hiddendjati22, 
    			   hiddendjati23, 
    			   hiddendjati24, 
    			   hiddendjati25,
    			   hiddendjati26,
    			   hiddendjati27, hiddendjati28, hiddendjati29, hiddendjati30,hiddendjati31, 
    			   hiddendjati32, 
    			   hiddendjati33, 
    			   hiddendjati34, 
    			   hiddendjati35,
    			   hiddendjati36,
    			   hiddendjati37,
    			   hiddendjati38,
    			   hiddendjati39, hiddendjati40,hiddendjati41, 
    			   hiddendjati42, 
    			   hiddendjati43, 
    			   hiddendjati44, 
    			   hiddendjati45,
    			   hiddendjati46,
    			   hiddendjati47,
    			   hiddendjati48,
    			   hiddendjati49,
    			   hiddendjati50;
    
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
    
    
    @InjectComponent
    private Zone kursiZone;
//
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    
    
    public void setVechileCode(String vechileCode) {
		this.vechileCode = vechileCode;
	}
    
    public String getVechileCode() {
		return vechileCode;
	}
    
    public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
    
    public String getProductCode() {
		return productCode;
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
    	
    	selectLocation = new LocationListSelect(djatiPurchaseViewLokasi);
    	
    }
    
    void onValueChangedFrombankName(String id){
    	
    	System.out.println("ID : "+StringUtils.substringBefore(id, ":").replace(" ", ""));
    	setTiketKeretaPurchaseViewData();
    	djatiPurchaseView.setTransactionType(Constants.TIKET_KERETA_DJATI.INQ_KERETA_DJATI);
        purchaseService.inquiry(djatiPurchaseView);
    	ajaxResponseRenderer.addRender(kursiZone);
    	
    }
  
    public List<TiketKeretaDjatiPurchaseView> getAllKursi() {
    	
    	List<TiketKeretaDjatiPurchaseView> list;

    	list = getDjatiPurchaseViews();

        return list;
    }
    
    
    public void setTiketKeretaPurchaseViewData() {
    	
		djatiPurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
		djatiPurchaseView.setAccountType(sessionManager.getAccountType(getDariRekening()));
		String billerCode = cacheManager.getBillers(Constants.TIKETUX.INQ_TIKETUX).get(0).getBillerCode();
        //String productCode = cacheManager.getBillerProducts(Constants.TIKET_KERETA_DJATI.POS_KERETA_DJATI, billerCode).get(0).getProductCode();
        djatiPurchaseView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
        djatiPurchaseView.setBillerCode(billerCode);
        djatiPurchaseView.setProductCode(getProductCode());
        djatiPurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        djatiPurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        djatiPurchaseView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        djatiPurchaseView.setProviderName("");
        djatiPurchaseView.setTransactionDate(new Date());
        djatiPurchaseView.setToAccountType("00");
        djatiPurchaseView.setJenisLayanan(Constants.TIKETUX.JENIS_LAYANAN);
        djatiPurchaseView.setTglKeberangkatan(getTglKeberangkatan());
        djatiPurchaseView.setVechileTypeCode(getVechileCode());
        
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
    	sessionManager.setSessionLastPage(TiketuxPurchaseKursi.class.toString());
        chooseValue = "fromId";
        //setTokenType();
        if (djatiPurchaseView == null) {
        	djatiPurchaseView = new TiketKeretaDjatiPurchaseView();
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
   
//   
//    @DiscardAfter
//  	Object onSelectedFromCancel() {
//    	return TiketKeretaDjatiPurchaseInput.class;
//    }
//    
    
    void onValidateFromForm() {
    	
		String tempatKursi = "";
		if (hiddendjati1 != null && hiddendjati1.equals("1"))
			tempatKursi += "1" + ",";
		if (hiddendjati2 != null && hiddendjati2.equals("2"))
			tempatKursi += "2" + ",";
		if (hiddendjati3 != null && hiddendjati3.equals("3"))
			tempatKursi += "3" + ",";
		if (hiddendjati4 != null && hiddendjati4.equals("4"))
			tempatKursi += "4" + ",";
		if (hiddendjati5 != null && hiddendjati5.equals("5"))
			tempatKursi += "5" + ",";
		if (hiddendjati6 != null && hiddendjati6.equals("6"))
			tempatKursi += "6" + ",";
		if (hiddendjati7 != null && hiddendjati7.equals("7"))
			tempatKursi += "7" + ",";
		if (hiddendjati8 != null && hiddendjati8.equals("8"))
			tempatKursi += "8" + ",";
		if (hiddendjati9 != null && hiddendjati9.equals("9"))
			tempatKursi += "9" + ",";
		if (hiddendjati10 != null && hiddendjati10.equals("10"))
			tempatKursi += "10" + ",";

		if (hiddendjati11 != null && hiddendjati11.equals("11"))
			tempatKursi += "11" + ",";
		if (hiddendjati12 != null && hiddendjati12.equals("12"))
			tempatKursi += "12" + ",";
		if (hiddendjati13 != null && hiddendjati13.equals("13"))
			tempatKursi += "13" + ",";
		if (hiddendjati14 != null && hiddendjati14.equals("14"))
			tempatKursi += "14" + ",";
		if (hiddendjati15 != null && hiddendjati15.equals("15"))
			tempatKursi += "15" + ",";
		if (hiddendjati16 != null && hiddendjati16.equals("16"))
			tempatKursi += "16" + ",";
		if (hiddendjati17 != null && hiddendjati17.equals("17"))
			tempatKursi += "17" + ",";
		if (hiddendjati18 != null && hiddendjati18.equals("18"))
			tempatKursi += "18" + ",";
		if (hiddendjati19 != null && hiddendjati19.equals("19"))
			tempatKursi += "19" + ",";
		if (hiddendjati20 != null && hiddendjati20.equals("20"))
			tempatKursi += "20" + ",";

		if (hiddendjati21 != null && hiddendjati21.equals("21"))
			tempatKursi += "21" + ",";
		if (hiddendjati22 != null && hiddendjati22.equals("22"))
			tempatKursi += "22" + ",";
		if (hiddendjati23 != null && hiddendjati23.equals("23"))
			tempatKursi += "23" + ",";
		if (hiddendjati24 != null && hiddendjati24.equals("24"))
			tempatKursi += "24" + ",";
		if (hiddendjati25 != null && hiddendjati25.equals("25"))
			tempatKursi += "25" + ",";
		if (hiddendjati26 != null && hiddendjati26.equals("26"))
			tempatKursi += "26" + ",";
		if (hiddendjati27 != null && hiddendjati27.equals("27"))
			tempatKursi += "27" + ",";
		if (hiddendjati28 != null && hiddendjati28.equals("28"))
			tempatKursi += "28" + ",";
		if (hiddendjati29 != null && hiddendjati29.equals("29"))
			tempatKursi += "29" + ",";
		if (hiddendjati30 != null && hiddendjati30.equals("30"))
			tempatKursi += "30" + ",";

		if (hiddendjati31 != null && hiddendjati31.equals("31"))
			tempatKursi += "31" + ",";
		if (hiddendjati32 != null && hiddendjati32.equals("32"))
			tempatKursi += "32" + ",";
		if (hiddendjati33 != null && hiddendjati33.equals("33"))
			tempatKursi += "33" + ",";
		if (hiddendjati34 != null && hiddendjati34.equals("34"))
			tempatKursi += "34" + ",";
		if (hiddendjati35 != null && hiddendjati35.equals("35"))
			tempatKursi += "35" + ",";
		if (hiddendjati36 != null && hiddendjati36.equals("36"))
			tempatKursi += "36" + ",";
		if (hiddendjati37 != null && hiddendjati37.equals("37"))
			tempatKursi += "37" + ",";
		if (hiddendjati38 != null && hiddendjati38.equals("38"))
			tempatKursi += "38" + ",";
		if (hiddendjati39 != null && hiddendjati39.equals("39"))
			tempatKursi += "39" + ",";
		if (hiddendjati40 != null && hiddendjati40.equals("40"))
			tempatKursi += "40" + ",";

		if (hiddendjati41 != null && hiddendjati41.equals("41"))
			tempatKursi += "41" + ",";
		if (hiddendjati42 != null && hiddendjati42.equals("42"))
			tempatKursi += "42" + ",";
		if (hiddendjati43 != null && hiddendjati43.equals("43"))
			tempatKursi += "43" + ",";
		if (hiddendjati44 != null && hiddendjati44.equals("44"))
			tempatKursi += "44" + ",";
		if (hiddendjati45 != null && hiddendjati45.equals("45"))
			tempatKursi += "45" + ",";
		if (hiddendjati46 != null && hiddendjati46.equals("46"))
			tempatKursi += "46" + ",";
		if (hiddendjati47 != null && hiddendjati47.equals("47"))
			tempatKursi += "47" + ",";
		if (hiddendjati48 != null && hiddendjati48.equals("48"))
			tempatKursi += "48" + ",";
		if (hiddendjati49 != null && hiddendjati49.equals("49"))
			tempatKursi += "49" + ",";
		if (hiddendjati50 != null && hiddendjati50.equals("50"))
			tempatKursi += "50" + ",";
    	
    	try {
    		
    		setTiketRegistration(tempatKursi.substring(0, tempatKursi.length() - 1));
    		
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
				nomor = "0"+tmpt[i];
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
		AccountInfo ai = sessionManager.getAccountInfo(sessionManager.getLoggedCustomerView().getAccountNumber());
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
		String destinationBranch = StringUtils.substringAfter(locationName, ": ").substring(StringUtils.substringAfter(locationName, ": ").lastIndexOf("pkl")).substring(StringUtils.substringAfter(locationName, ": ").substring(StringUtils.substringAfter(locationName, ": ").lastIndexOf("pkl")).lastIndexOf(": ")).replace(": ", "");
		djatiPurchaseRegistration.setDestinationBranch(StringUtils.substringAfter(destinationBranch, ", "));
		djatiPurchaseRegistration.setTransactionId(getTransactionId());
		djatiPurchaseRegistration.setDepartHour(StringUtils.substringBefore(StringUtils.substringAfter(locationName, "(").replace(")", ""), " ,"));
    	djatiPurchaseRegistration.setTotalEmpty(getTotalEmpty());
	}
    
    
    public void setLocation(List<String> location) {
		this.location = location;
	}
    
    public List<String> getLocation() {
		return location;
	}
    

	public Object onSuccess() {
        
        return djatiPurchaseRegistration;
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
    
	
    
    
}
