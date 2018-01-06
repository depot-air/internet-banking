package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
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
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TiketKeretaDjatiPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.pages.popup.BankTransfer;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA. User: emil Date: 09/08/11 Time: 11:13
 */
@Import(library = { "context:bprks/js/purchase/PlnPurchaseInput.js" })
public class TiketuxPurchaseRegistration {
	private Logger logger = Logger
			.getLogger(TiketuxPurchaseRegistration.class);
	@Property
	private String chooseValue;

	@Persist
	private TiketKeretaDjatiPurchaseView tiketKeretaDjatiPurchaseView;
	
	 @Property
	    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

	@Persist
	@Property
	private List<TiketKeretaDjatiPurchaseView> djatiPurchaseViews;

	@Property
	private int tokenType;

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

	@Persist
	private int totalKursi;

	@Persist
	private String detailKursi;

	@Persist
	private String lokasi;
	
	@InjectPage
	private TiketuxPurchaseConfirm djatiPurchaseConfirm;
	
	@Inject
	private Messages message;
	
	
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
	
	public void onPrepare() {

	}

	public Object onActivate() {
		if (sessionManager.isNotActivatedYet()) {
			return EulaWelcome.class;
		}
		return null;
	}

	public void setupRender() {

		sessionManager
				.setSessionLastPage(TiketuxPurchaseRegistration.class
						.toString());
		chooseValue = "fromId";
		System.out.println("HOHOHOHO "+getNoBangkuKedua());
		// setTokenType();
		if (tiketKeretaDjatiPurchaseView == null) {
			tiketKeretaDjatiPurchaseView = new TiketKeretaDjatiPurchaseView();
		}

	}

	void onValidateFromForm() {

        if (form.getHasErrors()) return;
		
	}

	public Object onSuccess() {
		 djatiPurchaseConfirm.setTotalKursi(totalKursi);
		 djatiPurchaseConfirm.setNoKursi1(getNoBangkuPertama());
		 djatiPurchaseConfirm.setNoKursi2(getNoBangkuKedua());
		 djatiPurchaseConfirm.setTiketKeretaDjatiPurchaseView(tiketKeretaDjatiPurchaseView);
		 
		 djatiPurchaseConfirm.setDepartId(getDepartId());
		 djatiPurchaseConfirm.setDepartHour(getDepartHour());
		 djatiPurchaseConfirm.setDepartBranch(getDepartBranch());
		 djatiPurchaseConfirm.setDestinationBranch(getDestinationBranch());
		 djatiPurchaseConfirm.setTransactionId(getTransactionId());
		 djatiPurchaseConfirm.setScheduleId(getScheduleId());
		 djatiPurchaseConfirm.setTotalEmpty(getTotalEmpty());
		 djatiPurchaseConfirm.setDetailKursi(getDetailKursi());
		 String nama = "";
		 if(getJmlNamaPenumpang().length() > 0){
			 nama = tiketKeretaDjatiPurchaseView.getNamaPenumpang1()+", "+tiketKeretaDjatiPurchaseView.getNamaPenumpang2();
		 }else{
			 nama = tiketKeretaDjatiPurchaseView.getNamaPenumpang1();
		 }
		 djatiPurchaseConfirm.setDetailPenumpang(nama);
		 
		 return djatiPurchaseConfirm;
	}

	public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(com.dwidasa.ib.Constants.SHORT_FORMAT);
    	return format.format(tiketKeretaDjatiPurchaseView.getTglKeberangkatan()).toString();
    }
	
	public BigDecimal getHargaTiket(){
		BigDecimal tot = new BigDecimal(totalKursi);
		return tiketKeretaDjatiPurchaseView.getTicketPricePerSheat().multiply(tot);
	}
	
	public void setTiketKeretaDjatiPurchaseView(
			TiketKeretaDjatiPurchaseView tiketKeretaDjatiPurchaseView) {
		this.tiketKeretaDjatiPurchaseView = tiketKeretaDjatiPurchaseView;
	}

	public TiketKeretaDjatiPurchaseView getTiketKeretaDjatiPurchaseView() {
		return tiketKeretaDjatiPurchaseView;
	}

	public void setTotalKursi(int totalKursi) {
		this.totalKursi = totalKursi;
	}

	public int getTotalKursi() {
		return totalKursi;
	}

	public void setDetailKursi(String detailKursi) {
		this.detailKursi = detailKursi;
	}

	public String getDetailKursi() {
		return detailKursi;
	}
	
	public String getLokasi() {
		return lokasi;
	}
	
	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public String getJmlNamaPenumpang(){
		if(totalKursi > 1){
			return "2" ;
		}else
			return "";
	}
	
	
	public String getNoBangkuPertama(){
		String noBangku[] = detailKursi.split(",");
		String bangku = "";
		for(int i=0; i<noBangku.length; i++){
			if("".equals(bangku)){
				bangku += ""+noBangku[i];
			}else{
				//bangku += ""+noBangku[i];
			}
		}
		return bangku;
	}
	
	
	public String getNoBangkuKedua(){
		String noBangku[] = detailKursi.split(",");
		String bangku = "";
		String bangkuTwo = "";
		for(int i=0; i<noBangku.length; i++){
			if("".equals(bangku)){
				bangku += ""+noBangku[i];
			}else{
				bangkuTwo += ""+noBangku[i];
			}
		}
		return bangkuTwo;
	}
	

	public String getDateFieldFormat() {
		return com.dwidasa.ib.Constants.SHORT_FORMAT;
	}

}
