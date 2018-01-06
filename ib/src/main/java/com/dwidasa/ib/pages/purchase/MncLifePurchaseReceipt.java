package com.dwidasa.ib.pages.purchase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.account.TransactionHistoryResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/08/11
 * Time: 11:14
 */
public class MncLifePurchaseReceipt {
    @Persist
    private MncLifePurchaseView mncLifePurchaseView;

  
    @Property
    private String vGspRef;

    @Property
    private String vTokenNumber;

    @Inject
    private TransactionDataService transactionDataService;
   

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());


    @Property
    private String status;
    
    @Inject
    private PurchaseService purchaseService;
    
    @Inject
    private TransactionService transactionService;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private Messages messages;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    @Persist
    private String StatusTransaksi;
    
    
    public void setStatusTransaksi(String statusTransaksi) {
		StatusTransaksi = statusTransaksi;
	}
    
    public String getStatusTransaksi() {
		return StatusTransaksi;
	}
    
    public void setupRender(){
    	sessionManager.setSessionLastPage(MncLifePurchaseView.class.toString());
    	sessionManager.setSmsTokenSent(false);
    	
    	if(isFromStatus()){
    		//dilakukan ketika tekan proses cetak
    		//prosesReprint();
    	}
    	
        if (mncLifePurchaseView.getResponseCode() != null && mncLifePurchaseView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }
    
    private void prosesReprint() {
		
    	try {
    		
             	TransactionData transactionData = transactionDataService.getByTransactionFk(transactionId);
            	mncLifePurchaseView = (MncLifePurchaseView) EngineUtils.deserialize(transactionData);
            	mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Reprint);
                //purchaseService.reprint(plnPurchaseView, transactionId);
                try {
                	purchaseService.reprint(mncLifePurchaseView, transactionId);
                } catch (BusinessException e) {            	
                   // form.recordError(e.getFullMessage());
                    return;
                } catch (Exception e) {            	
                    //form.recordError(Constants.EXCEPTION_OVERRIDE_MESSAGE);
                    return;
                }		
                //logger.info("after reprint, account=" + plnPurchaseView.getCustomerReference());

//                //update ke table history transaksi
//                mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian);
//                transactionData.setTransactionData(PojoJsonMapper.toJson(mncLifePurchaseView));
//                Transaction transaction = TransformerFactory.getTransformer(mncLifePurchaseView).transformTo(mncLifePurchaseView, new Transaction());            
//                transactionData.setTransaction(transaction);
//                transactionDataService.updateTransactionData(transactionData);	// save(transactionData);
//                
//                Transaction trans = transactionService.get(transactionId);
//                if(getStatusTransaksi().equals("Tunda")){
//                	trans.setStatus(com.dwidasa.engine.Constants.PENDING_STATUS);
//                }else{
//                	trans.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);
//                }
//                //
//                System.out.println("before update transaction, status=" + getStatusTransaksi());
//                transactionService.save(trans);    
          
        } catch (BusinessException e) {
            //form.recordError(e.getFullMessage());
        }
    	
	}

	public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
    	return format.format(mncLifePurchaseView.getTanggalLahir()).toString();
    }
    
    public String getFormatTglAwal(){//
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis1()).toString();
    }
    
    public String getFormatTglAkhir(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis1()).toString();
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
    
    public String getTipeDokumen(){
    	String tipe = "";
    	if(mncLifePurchaseView.getTipeDokumen().equals("P")){
    		tipe = "Polis";
    	}else{
    		tipe = "Ktp";
    	}
    	return tipe;
    }

    public Object onActivate() {
        if (mncLifePurchaseView == null) {
            return MncLifePurchaseView.class;
        }
        return null;
    }

    public MncLifePurchaseView getMncLifePurchaseView() {
        return mncLifePurchaseView;
    }

    public void setMncLifePurchaseView(MncLifePurchaseView mncLifePurchaseView) {
        this.mncLifePurchaseView = mncLifePurchaseView;
    }
    
    @Persist
    private boolean fromHistory;
    
    @Persist
    private boolean fromStatus;
    
    
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }
    
    
    public void setFromStatus(boolean fromStatus) {
		this.fromStatus = fromStatus;
	}
    
    public boolean isFromStatus() {
		return fromStatus;
	}

    public boolean isSuccess() {
    	return status.equals(messages.get("success"));
    }
    public boolean isNotSuccess() {
    	SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    	boolean sameDay =  fmt.format(mncLifePurchaseView.getTransactionDate()).equals(fmt.format(new Date()));
    	return !status.equals(messages.get("success")) && sameDay ;
    }
    @DiscardAfter
    Object onSuccess() {
        return TransactionHistoryResult.class;
    	//reprint
    	//plnPurchaseView.setTransactionType(com.dwidasa.engine.Constants.PLN_PURCHASE_REP_CODE);
        //plnPurchaseView = (PlnPurchaseView) kioskReprintService.reprint(plnPurchaseView);
    }

    public boolean isGsp() {
    	return mncLifePurchaseView.getProviderCode().equals(Constants.GSP.PROVIDER_CODE);
    }
    
    
    public String getStatusDetil(){
    	
    	try {
    		
    		Transaction transaction = transactionService.get(transactionId);
        	
        	if (transaction.getStatus().equals(com.dwidasa.engine.Constants.SUCCEED_STATUS)) {
        		status = messages.get("success");
        	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.PENDING_STATUS)) {
        		status = messages.get("pending");
        	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.FAILED_STATUS)) {
        		status = messages.get("pending");
        	}
        	
		} catch (Exception e) {
			
			if (mncLifePurchaseView.getResponseCode() != null && mncLifePurchaseView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
	            status = messages.get("success");
	        } else {
	            status = messages.get("failed");
	        }
		}
    	
    	
    	return status;
    }

    
    public String getStatusHistory(){
    	try {
			
    		Transaction transaction = transactionService.get(transactionId);
        	
        	if (transaction.getStatus().equals(com.dwidasa.engine.Constants.SUCCEED_STATUS)) {
        		status = "";
        	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.PENDING_STATUS)) {
        		status = messages.get("pending");
        	} else if (transaction.getStatus().equals(com.dwidasa.engine.Constants.FAILED_STATUS)) {
        		status = messages.get("pending");
        	}
        	
		} catch (Exception e) {
			status = "";// TODO: handle exception
		}
    	
    	
    	return status;
    }
   
}
