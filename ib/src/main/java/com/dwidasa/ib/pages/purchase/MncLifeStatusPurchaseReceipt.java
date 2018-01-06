package com.dwidasa.ib.pages.purchase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.TransactionDao;
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
import com.dwidasa.ib.pages.account.TransactionStatus;
import com.dwidasa.ib.pages.account.TransactionStatusResult;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/08/11
 * Time: 11:14
 */
public class MncLifeStatusPurchaseReceipt {
    @Persist
    private MncLifePurchaseView mncLifePurchaseView;

    @Inject
    private TransactionDao transactionDao;
   
    @Inject
    private TransactionDataService transactionDataService;

    @Property
    private String vGspRef;

    @Property
    private String vTokenNumber;
    
    @InjectPage
    private TransactionStatus transactionStatus;

    @Inject
    private PurchaseService purchaseService;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    
    @Persist
    private String noKtp;

    @Property
    private String status;
    
    @Inject
    private TransactionService transactionService;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private Messages messages;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
    public void setupRender(){
    	sessionManager.setSessionLastPage(MncLifePurchaseView.class.toString());
    	sessionManager.setSmsTokenSent(false);
//    	
//    	if(isFromStatus()){
//    		prosesCekStatus();
//    	}
//    	
        if (mncLifePurchaseView.getResponseCode() != null && mncLifePurchaseView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }
    
    private void prosesCekStatus() {
		
    	try {
             	TransactionData transactionData = transactionDataService.getByTransactionFk(transactionId);
            	mncLifePurchaseView = (MncLifePurchaseView) EngineUtils.deserialize(transactionData);
            	mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Cek_Status);
                //purchaseService.reprint(plnPurchaseView, transactionId);
                try {
                	purchaseService.reprint(mncLifePurchaseView, transactionId);
                	//plnPurchaseView = (PlnPurchaseView) kioskReprintService.reprint(plnPurchaseView);
                } catch (BusinessException e) {            	
                    //form.recordError(e.getFullMessage());
                    return;
                } catch (Exception e) {            	
                    //form.recordError(Constants.EXCEPTION_OVERRIDE_MESSAGE);
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
                
        } catch (BusinessException e) {
            //form.recordError(e.getFullMessage());
        }
    	
	}

	public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTanggalLahir()).toString();
    }
    
    public String getFormatTglAwal(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis1()).toString();
    }
    
    public String getFormatTglAkhir(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis1()).toString();
    }
    
    
    public String getFormatTglAwal2(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis2()).toString();
    }
    
    public String getFormatTglAkhir2(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis2()).toString();
    }
    
    public String getFormatTglAwal3(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis3()).toString();
    }
    
    public String getFormatTglAkhir3(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis3()).toString();
    }
    
    public String getFormatTglAwal4(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis4()).toString();
    }
    
    public String getFormatTglAkhir4(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis4()).toString();
    }
    
    public String getFormatTglAwal5(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis5()).toString();
    }
    
    public String getFormatTglAkhir5(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis5()).toString();
    }
    
    public String getFormatTglAwal6(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis6()).toString();
    }
    
    public String getFormatTglAkhir6(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis6()).toString();
    }
    
    public String getFormatTglAwal7(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis7()).toString();
    }
    
    public String getFormatTglAkhir7(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis7()).toString();
    }
    
    public String getFormatTglAwal8(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis8()).toString();
    }
    
    public String getFormatTglAkhir8(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis8()).toString();
    }
    
    public String getFormatTglAwal9(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAwalPolis9()).toString();
    }
    
    public String getFormatTglAkhir9(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.MEDIUMTHREE_FORMAT);
    	return format.format(mncLifePurchaseView.getTglAkhirPolis9()).toString();
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
        return transactionStatus;
    	//reprint
    	//plnPurchaseView.setTransactionType(com.dwidasa.engine.Constants.PLN_PURCHASE_REP_CODE);
        //plnPurchaseView = (PlnPurchaseView) kioskReprintService.reprint(plnPurchaseView);
    }

    public boolean isGsp() {
    	return mncLifePurchaseView.getProviderCode().equals(Constants.GSP.PROVIDER_CODE);
    }
    
    
    public void setNoKtp(String noKtp) {
		this.noKtp = noKtp;
	}
    
    public String getNoKtp() {
		return noKtp;
	}

    
    public String getCustomerReference(String CustomerId){
        List<Transaction> transactions = transactionDao.getCustomerReference(CustomerId);
               String status = "";
         for(Transaction f : transactions) {
	       status += ""+f.getId();
	  }
         
         return status;
               
    }
    
    
    
    public String getStatusHistory(){
    	try {
			
    		long idTransaction = Long.parseLong(getCustomerReference(getNoKtp()));
    		Transaction transaction = transactionService.get(idTransaction);
        	
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
