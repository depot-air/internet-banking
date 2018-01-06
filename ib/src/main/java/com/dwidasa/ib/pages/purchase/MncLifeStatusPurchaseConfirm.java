package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
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
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.InboxCustomer;
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
import com.dwidasa.engine.service.facade.KioskCheckStatusService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.impl.view.KioskCheckStatusMessageCustomizer;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.AccountResource;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/27/11
 * Time: 10:24 AM
 */
public class MncLifeStatusPurchaseConfirm {
	private static Logger logger = Logger.getLogger( MncLifeStatusPurchaseConfirm.class );
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private int tokenType;

    @Property
    private MncLifePurchaseView mncLifePurchaseView;
    
 
    @Inject
    private PurchaseService purchaseService;

    @Property
    private String status;
    
    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;
    
    @Property
    private String noKtp;
    
    @Property
    private String namaTertanggung;

    @Inject
    private OtpManager otpManager;

    @Property
    private TokenView tokenView;
    
    @Inject
    private TransactionService transactionService;
    
    @Property
    private Transaction transaction;

    @Inject
    private TransactionDataService transactionDataService;
    
    @Inject
    private TransactionDao transactionDao;
    
    @InjectPage
    private MncLifeStatusPurchaseReceipt mncLifeStatusPurchaseReceipt;
    
    @Inject
    private KioskReprintService kioskReprintService;

    @Inject
    private KioskCheckStatusService kioskCheckStatusService;

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

//    public void setMncLifeStatusConfirm(MncLifePurchaseView mncLifePurchaseView) {
//        this.mncLifePurchaseView = mncLifePurchaseView;
//    }

    public void setupRender() {
    	
    }

    void onPrepare() {
//    	sessionManager.setSessionLastPage(MncLifeStatusPurchaseConfirm.class.toString());
//        if (tokenView == null) {
//            tokenView = new TokenView();
//        }
    }

    public Object onActivate() {
        if (mncLifePurchaseView == null) {
            mncLifePurchaseView = new MncLifePurchaseView();
        }

        return null;
    }
    
//    public String getFormatTgl(){
//    	SimpleDateFormat format = new SimpleDateFormat(Constants.SHORT_FORMAT);
//    	return format.format(mncLifePurchaseView.getTanggalLahir()).toString();
//    }
//    
//    public String getJenisKelamin(){
//    	String jenis = "";
//    	if(mncLifePurchaseView.getJenisKelamin().equals("L")){
//    		jenis = "Laki-laki";
//    	}else{
//    		jenis = "Perempuan";
//    	}
//    	return jenis;
//    }
    
    public String getCustomerReference(String CustomerId){
        List<Transaction> transactions = transactionDao.getCustomerReference(CustomerId);
               String status = "";
         for(Transaction f : transactions) {
	       status += ""+f.getId();
	  }
         
         return status;
               
    }

    private void setMncLifePurchaseViewData() {
    	mncLifePurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	mncLifePurchaseView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
    	mncLifePurchaseView.setAccountType("10");
    	mncLifePurchaseView.setToAccountType("00");
    	String billerCode = cacheManager.getBillers(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian).get(0).getBillerCode();
        mncLifePurchaseView.setBillerCode(billerCode);
        mncLifePurchaseView.setIssuercode(com.dwidasa.engine.Constants.MNCLIFE.PROVIDER_CODE);
        mncLifePurchaseView.setKodeProduk("06D");
        mncLifePurchaseView.setProviderCode(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Provider);
        
        mncLifePurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian);
        mncLifePurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        mncLifePurchaseView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
       
        mncLifePurchaseView.setTransactionDate(new Date());
        mncLifePurchaseView.setAmount(new BigDecimal(45500));
        mncLifePurchaseView.setFee(BigDecimal.ZERO);
        mncLifePurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        mncLifePurchaseView.setTanggalLahir(mncLifePurchaseView.getTanggalLahir());
    }
    
    public void onValidateFromForm() {
        try {
      
//        		if(getCustomerReference(noKtp).equals("")){
//        			
//        			form.recordError("No Ktp Belum Terdaftar");
//        		
//        		}else{
//        			
//        		long idTransaction1 = Long.parseLong(getCustomerReference(noKtp));
//        	    Transaction trans1 = transactionService.get(idTransaction1);
//        	    String statusTransaksi = trans1.getStatus();
//        		
//        	    
//        			
//        	    long idTransaction = Long.parseLong(getCustomerReference(noKtp));
//           	    TransactionData transactionData = transactionDataService.getByTransactionFk(idTransaction);
//            	mncLifePurchaseView = (MncLifePurchaseView) EngineUtils.deserialize(transactionData);
        	
        		setMncLifePurchaseViewData();
            	mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Cek_Status);
            	
//            	if(!namaTertanggung.toString().toLowerCase().equals(mncLifePurchaseView.getNamaTertanggung().toString().toLowerCase())){
//            		
//            		form.recordError("Nama Tertanggung Tidak Sama Dengan No.Identitas KTP");
//            		
//            	}else{
//            	
//                try {
//                	purchaseService.reprint(mncLifePurchaseView, idTransaction);
//                	//plnPurchaseView = (PlnPurchaseView) kioskReprintService.reprint(plnPurchaseView);
//                } catch (BusinessException e) {            	
//                    form.recordError(e.getFullMessage());
//                    return;
//                } catch (Exception e) {            	
//                    form.recordError(Constants.EXCEPTION_OVERRIDE_MESSAGE);
//                    return;
//                }
            	
            	mncLifePurchaseView.setNomorKtp(noKtp);
            	mncLifePurchaseView.setCustomerReference(noKtp);
            	mncLifePurchaseView.setNamaTertanggung(namaTertanggung);
            	
                logger.info("mncLifePurchaseView before=" + mncLifePurchaseView);
                mncLifePurchaseView = (MncLifePurchaseView) kioskCheckStatusService.checkStatus(mncLifePurchaseView);
            	logger.info("mncLifePurchaseView after=" + mncLifePurchaseView);
            	
//                //update ke table history transaksi
//                mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian);
//                transactionData.setTransactionData(PojoJsonMapper.toJson(mncLifePurchaseView));
//                Transaction transaction = TransformerFactory.getTransformer(mncLifePurchaseView).transformTo(mncLifePurchaseView, new Transaction());            
//                transactionData.setTransaction(transaction);
//                //logger.info("before update transaction data, transactionId=" + transactionData.getTransactionId());
//                transactionDataService.save(transactionData);
//                
//                Transaction trans = transactionService.get(idTransaction);
//                System.out.println("before update transaction, status=" + trans.getStatus());
//                if(statusTransaksi.equals("PENDING")){
//                	 trans.setStatus(com.dwidasa.engine.Constants.PENDING_STATUS);
//                }else{
//                	trans.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);
//                }
//               
//                //logger.info("before update transaction, status=" + trans.getStatus());
//                transactionService.save(trans);   
//                
//        		}
//            	
//        		}
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }
     
    

    @DiscardAfter
    public Object onSuccess() {
    	mncLifeStatusPurchaseReceipt.setMncLifePurchaseView(mncLifePurchaseView);
    	mncLifeStatusPurchaseReceipt.setNoKtp(noKtp);
    	mncLifeStatusPurchaseReceipt.setFromHistory(true);
        return mncLifeStatusPurchaseReceipt;
    }

    public boolean isMerchant() {    	
//    	String firstFour = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
//    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
//    	String[] tokens = ip.getParameterValue().split(",");
//    	boolean isMerch = false;
//    	if (tokens.length > 0 ) {
//    		for(int i = 0; i < tokens.length; i++) {
//    			if (firstFour.equals(tokens[i]))
//    				isMerch = true;
//    		}
//    	}
    	return sessionManager.isMerchant();
    }
    
    
   
    

}
