package com.dwidasa.ib.pages.purchase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/5/11
 * Time: 1:49 PM
 */
public class MncLifePurchaseReprint extends BasePrintPage {

    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private String status;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    @Inject
    private Messages messages;
    
    @Inject
    private TransactionService transactionService;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_DATE, threadLocale.getLocale());

    @InjectPage
    private MncLifePurchaseReceipt mnclifepurchaseReceipt;
    
    @ActivationRequestParameter
    private String reprint;

    @Property
    private MncLifePurchaseView mcnMncLifePurchaseView;
	
    void setupRender() {    	
    	mcnMncLifePurchaseView = mnclifepurchaseReceipt.getMncLifePurchaseView();
    }
    
    public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
    	return format.format(mcnMncLifePurchaseView.getTanggalLahir()).toString();
    }
    
    public String getFormatTglAwal(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
    	return format.format(mcnMncLifePurchaseView.getTglAwalPolis1()).toString();
    }
    
    public String getFormatTglAkhir(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
    	return format.format(mcnMncLifePurchaseView.getTglAkhirPolis1()).toString();
    }
    
    public String getJenisKelamin(){
    	String jenis = "";
    	if(mcnMncLifePurchaseView.getJenisKelamin().equals("L")){
    		jenis = "Laki-laki";
    	}else{
    		jenis = "Perempuan";
    	}
    	return jenis;
    }
    
    public String getTipeDokumen(){
    	String tipe = "";
    	if(mcnMncLifePurchaseView.getTipeDokumen().equals("P")){
    		tipe = "Polis";
    	}else{
    		tipe = "Ktp";
    	}
    	return tipe;
    }
    
    
    
    
  
//    public Long getDenomination() {
//        return Long.valueOf(mcnMncLifePurchaseView.getDenomination());
//    }
//
//    public boolean isTelkomsel() {    	
//    	return (voucherPurchaseView.getProviderCode().equals(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE) );
//    }
}
