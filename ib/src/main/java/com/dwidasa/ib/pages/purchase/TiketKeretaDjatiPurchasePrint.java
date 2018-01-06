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
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.pages.popup.BankTransfer;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA. User: emil Date: 09/08/11 Time: 11:13
 */
@Import(library = { "context:bprks/js/purchase/PlnPurchaseInput.js" })
public class TiketKeretaDjatiPurchasePrint {
	private Logger logger = Logger
			.getLogger(TiketKeretaDjatiPurchasePrint.class);
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
    private boolean fromHistory;
	
	@InjectPage
    private TiketKeretaDjatiPurchaseReceipt tiketKeretaDjatiPurchaseReceipt;
    
	
	public void onPrepare() {
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

		tiketKeretaDjatiPurchaseView = tiketKeretaDjatiPurchaseReceipt.getTiketKeretaDjatiPurchaseView();

	}

	void onValidateFromForm() {

	}

	public Object onSuccess() {
		return this;
		// return this;
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
	
	public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }
	
	public String getJurusan(){
		return tiketKeretaDjatiPurchaseView.getDari()+" - "+tiketKeretaDjatiPurchaseView.getTujuan();
	}
	
	public String getPenumpang(){
		if(tiketKeretaDjatiPurchaseView.getTotalEmptySheat() > 1){
			return tiketKeretaDjatiPurchaseView.getNoKursiPenumpang1()+" ,"+tiketKeretaDjatiPurchaseView.getNoKursiPenumpang2();
		}else
			return tiketKeretaDjatiPurchaseView.getNoKursiPenumpang1();
		//return "";
	}
	
	public String getLokasiKeberangkatan(){
		return tiketKeretaDjatiPurchaseView.getDepartHour()+", "+tiketKeretaDjatiPurchaseView.getDari()+", "+tiketKeretaDjatiPurchaseView.getDepartBranch();
	}
	
	public BigDecimal getHargaTiket(){
		BigDecimal tot = new BigDecimal(tiketKeretaDjatiPurchaseView.getTotalEmptySheat());
		return tiketKeretaDjatiPurchaseView.getTicketPricePerSheat().multiply(tot);
	}
	

	public String getDateFieldFormat() {
		return com.dwidasa.ib.Constants.SHORT_FORMAT;
	}

}
