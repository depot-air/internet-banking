package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/27/11
 * Time: 10:24 AM
 */
public class MncLifeStatusConfirm {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private int tokenType;

    @Property(write = false)
    @Persist
    private MncLifePurchaseView mncLifePurchaseView;

    @Inject
    private PurchaseService purchaseService;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Property
    private TokenView tokenView;
    
    @Inject
    private TransactionService transactionService;

    @Inject
    private TransactionDataService transactionDataService;
    
    @InjectPage
    private MncLifeStatusPurchaseReceipt mncLifeStatusPurchaseReceipt;
    
    @Inject
    private KioskReprintService kioskReprintService;

    @Inject
	private ParameterDao parameterDao;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    @Persist
    private boolean fromHistory;
    
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }
    
    public Long getDenomination() {
        return Long.valueOf(mncLifePurchaseView.getDenomination());
    }

    public void setMncLifeStatusConfirm(MncLifePurchaseView mncLifePurchaseView) {
        this.mncLifePurchaseView = mncLifePurchaseView;
    }

    public void setupRender() {
    	setTokenType();
    }

    void onPrepare() {
    	sessionManager.setSessionLastPage(MncLifeStatusConfirm.class.toString());
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (mncLifePurchaseView == null) {
            return MncLifePurchaseView.class;
        }

        return null;
    }
    
    public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.SHORT_FORMAT);
    	return format.format(mncLifePurchaseView.getTanggalLahir()).toString();
    }
    
    public String getJenisKelamin(){
    	String jenis = "";
    	if(mncLifePurchaseView.getJenisKelamin().equals("L")){
    		jenis = "Laki-laki";
    	}else{
    		jenis = "Perempuan";
    	}
    	return jenis;
    }
    
    public String getValueNoDokumen(){
    	String tipe  = "";
    	if(mncLifePurchaseView.getTipeDokumen().equals("P")){
    		tipe = mncLifePurchaseView.getNomoPolis1();
    	}else{
    		tipe = mncLifePurchaseView.getCustomerReference();
    	}
    	return tipe;
    }
    
    public String getTipeDokumen(){
    	String tipe = "";
    	if(mncLifePurchaseView.getTipeDokumen().equals("P")){
    		tipe = "Polis";
    	}else{
    		tipe = "Ktp";
    	}
    	return tipe;
    }
    
    public void onValidateFromForm() {
        try {
            if (otpManager.validateToken(mncLifePurchaseView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {

            	TransactionData transactionData = transactionDataService.getByTransactionFk(transactionId);
            	mncLifePurchaseView = (MncLifePurchaseView) EngineUtils.deserialize(transactionData);
            	mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Cek_Status);
                //purchaseService.reprint(plnPurchaseView, transactionId);
                try {
                	purchaseService.reprint(mncLifePurchaseView, transactionId);
                	//plnPurchaseView = (PlnPurchaseView) kioskReprintService.reprint(plnPurchaseView);
                } catch (BusinessException e) {            	
                    form.recordError(e.getFullMessage());
                    return;
                } catch (Exception e) {            	
                    form.recordError(Constants.EXCEPTION_OVERRIDE_MESSAGE);
                    return;
                }
                
                //update ke table history transaksi
                mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian);
                transactionData.setTransactionData(PojoJsonMapper.toJson(mncLifePurchaseView));
                Transaction transaction = TransformerFactory.getTransformer(mncLifePurchaseView).transformTo(mncLifePurchaseView, new Transaction());            
                transactionData.setTransaction(transaction);
                //logger.info("before update transaction data, transactionId=" + transactionData.getTransactionId());
                transactionDataService.save(transactionData);
                
                Transaction trans = transactionService.get(transactionId);
                trans.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);
                //logger.info("before update transaction, status=" + trans.getStatus());
                transactionService.save(trans);            
               
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }
    
    

    @DiscardAfter
    public Object onSuccess() {
    	mncLifeStatusPurchaseReceipt.setMncLifePurchaseView(mncLifePurchaseView);
        return mncLifeStatusPurchaseReceipt;
    }

    public boolean isMerchant() {    	
    	String firstFour = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
    	String[] tokens = ip.getParameterValue().split(",");
    	boolean isMerch = false;
    	if (tokens.length > 0 ) {
    		for(int i = 0; i < tokens.length; i++) {
    			if (firstFour.equals(tokens[i]))
    				isMerch = true;
    		}
    	}
    	return isMerch;
    }
    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

}
