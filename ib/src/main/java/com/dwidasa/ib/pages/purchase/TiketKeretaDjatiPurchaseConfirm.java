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
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.pages.popup.BankTransfer;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA. User: emil Date: 09/08/11 Time: 11:13
 */
@Import(library = { "context:bprks/js/purchase/PlnPurchaseInput.js" })
public class TiketKeretaDjatiPurchaseConfirm {
	private Logger logger = Logger
			.getLogger(TiketKeretaDjatiPurchaseConfirm.class);
	@Property
	private String chooseValue;
	
	 @Property
	    private TokenView tokenView;

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
	private String noKursi1;
	
	@Persist
	private String noKursi2;
	
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
    
    @Persist
    private String detailKursi;
    
    @Persist
    private String detailPenumpang;
    
    
    @Persist
    private String vehicleCode;
    
    
    public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
    
    public String getVehicleCode() {
		return vehicleCode;
	}
    
    public void setDetailKursi(String detailKursi) {
		this.detailKursi = detailKursi;
	}
    
    public String getDetailKursi() {
		return detailKursi;
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
    
    public void setDetailPenumpang(String detailPenumpang) {
		this.detailPenumpang = detailPenumpang;
	}
    
    public String getDetailPenumpang() {
		return detailPenumpang;
	}
	
	@InjectPage
	private TiketKeretaDjatiPurchaseReceipt djatiPurchaseReceipt;
	
	public void onPrepare() {
		sessionManager.setSessionLastPage(TiketKeretaDjatiPurchaseConfirm.class.toString());
		if (tokenView == null) {
            tokenView = new TokenView();
        }
	}

	public Object onActivate() {
		if (sessionManager.isNotActivatedYet()) {
			return EulaWelcome.class;
		}
		return null;
	}

	private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }
	
	
	public void setupRender() {

		setTokenType();
		chooseValue = "fromId";
		
	}

	void onValidateFromForm() {

        try {
            if (otpManager.validateToken(sessionManager.getLoggedCustomerView().getId(), this.getClass().getSimpleName(), tokenView)) {
            	
            	setTiketKeretaPurchaseViewData();
            	tiketKeretaDjatiPurchaseView.setTransactionType(Constants.TIKET_KERETA_DJATI.POS_KERETA_DJATI);
            	purchaseService.execute(tiketKeretaDjatiPurchaseView);
            	
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }

        
	}
	
	public void setTiketKeretaPurchaseViewData() {
    	
		tiketKeretaDjatiPurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
		tiketKeretaDjatiPurchaseView.setAccountType(sessionManager.getAccountType(tiketKeretaDjatiPurchaseView.getAccountNumber()));
        String billerCode = cacheManager.getBillers(Constants.TIKET_KERETA_DJATI.POS_KERETA_DJATI).get(0).getBillerCode();
        String productCode = cacheManager.getBillerProducts(Constants.TIKET_KERETA_DJATI.POS_KERETA_DJATI, billerCode).get(0).getProductCode();
        tiketKeretaDjatiPurchaseView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
        tiketKeretaDjatiPurchaseView.setBillerCode(billerCode);
        tiketKeretaDjatiPurchaseView.setProductCode(productCode);
        tiketKeretaDjatiPurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        tiketKeretaDjatiPurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        tiketKeretaDjatiPurchaseView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        tiketKeretaDjatiPurchaseView.setProviderName("");
        tiketKeretaDjatiPurchaseView.setTransactionDate(new Date());
        tiketKeretaDjatiPurchaseView.setToAccountType("00");
        
        tiketKeretaDjatiPurchaseView.setDepartID(getDepartId());
        tiketKeretaDjatiPurchaseView.setDepartHour(getDepartHour());
        tiketKeretaDjatiPurchaseView.setDepartBranch(getDepartBranch());
        tiketKeretaDjatiPurchaseView.setDestinationBranch(getDestinationBranch());
        tiketKeretaDjatiPurchaseView.setTransactionID(getTransactionId());
        tiketKeretaDjatiPurchaseView.setScheduleCodeId(getScheduleId());
        tiketKeretaDjatiPurchaseView.setTotalEmptySheat(getTotalEmpty());
        tiketKeretaDjatiPurchaseView.setSeat(detailKursi);
        tiketKeretaDjatiPurchaseView.setVechileTypeCode(getVehicleCode());
        tiketKeretaDjatiPurchaseView.setName(detailPenumpang);
        
    }

	public Object onSuccess() {
		
		djatiPurchaseReceipt.setTotalKursi(totalKursi);
        djatiPurchaseReceipt.setNoKursi1(getNoKursi1());
        djatiPurchaseReceipt.setNoKursi2(getNoKursi2());
        djatiPurchaseReceipt.setFromHistory(false);
        djatiPurchaseReceipt.setTiketKeretaDjatiPurchaseView(tiketKeretaDjatiPurchaseView);
        return djatiPurchaseReceipt;
        
	}

	public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(com.dwidasa.ib.Constants.SHORT_FORMAT);
    	return format.format(tiketKeretaDjatiPurchaseView.getTglKeberangkatan()).toString();
    }
	

	
	public void setTiketKeretaDjatiPurchaseView(
			TiketKeretaDjatiPurchaseView tiketKeretaDjatiPurchaseView) {
		this.tiketKeretaDjatiPurchaseView = tiketKeretaDjatiPurchaseView;
	}

	public TiketKeretaDjatiPurchaseView getTiketKeretaDjatiPurchaseView() {
		return tiketKeretaDjatiPurchaseView;
	}

	
	public int getTotalKursi() {
		return totalKursi;
	}
	
	public void setTotalKursi(int totalKursi) {
		this.totalKursi = totalKursi;
	}
	
	public String getNoKursi1() {
		return noKursi1;
	}
	
	public void setNoKursi1(String noKursi1) {
		this.noKursi1 = noKursi1;
	}
	
	public String getNoKursi2() {
		return noKursi2;
	}
	
	public void setNoKursi2(String noKursi2) {
		this.noKursi2 = noKursi2;
	}
	
	public String getJurusan(){
		return tiketKeretaDjatiPurchaseView.getDari()+" - "+tiketKeretaDjatiPurchaseView.getTujuan()+" ("+tiketKeretaDjatiPurchaseView.getKodeJurusan()+")";
	}
	
	public String getPenumpang(){
		if(totalKursi > 1){
			return tiketKeretaDjatiPurchaseView.getNamaPenumpang1()+" / "+getNoKursi1()+" ,"+tiketKeretaDjatiPurchaseView.getNamaPenumpang2()+" / "+getNoKursi2();
		}else
			return tiketKeretaDjatiPurchaseView.getNamaPenumpang1()+" / "+getNoKursi1();
	}
	

	public String getDateFieldFormat() {
		return com.dwidasa.ib.Constants.SHORT_FORMAT;
	}
	
	public BigDecimal getHargaTiket(){
		BigDecimal tot = new BigDecimal(totalKursi);
		return tiketKeretaDjatiPurchaseView.getTicketPricePerSheat().multiply(tot);
	}
	
	
}
