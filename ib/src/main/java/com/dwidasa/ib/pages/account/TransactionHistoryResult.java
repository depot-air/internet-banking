package com.dwidasa.ib.pages.account;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.dwidasa.engine.model.view.*;
import com.dwidasa.ib.pages.delima.CashOutDelimaReceipt;
//import com.dwidasa.ib.pages.delima.CashOutElmoDelimaReceipt;
//import com.dwidasa.ib.pages.delima.CashToBankDelimaReceipt;
import com.dwidasa.ib.pages.purchase.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.CashInDompetkuView;
import com.dwidasa.engine.model.view.HpPaymentView;
import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.model.view.TvPaymentView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.model.view.WaterPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.common.GeneralUtil;
import com.dwidasa.ib.pages.delima.CashInDelimaReceipt;
import com.dwidasa.ib.pages.dompetku.TarikTunaiDompetkuReceipt;
import com.dwidasa.ib.pages.dompetku.TopUpDompetkuConfirm;
import com.dwidasa.ib.pages.dompetku.TopUpDompetkuReceipt;
import com.dwidasa.ib.pages.dompetku.TransferTokenDompetkuReceipt;
import com.dwidasa.ib.pages.payment.HpPaymentReceipt;
import com.dwidasa.ib.pages.payment.InternetPaymentReceipt;
import com.dwidasa.ib.pages.payment.KartuKreditBNIPaymentReceipt;
import com.dwidasa.ib.pages.payment.MultiFinanceMncPaymentReceipt;
import com.dwidasa.ib.pages.payment.MultiFinancePaymentReceipt;
import com.dwidasa.ib.pages.payment.NonTagListPaymentReceipt;
import com.dwidasa.ib.pages.payment.PlnPaymentReceipt;
import com.dwidasa.ib.pages.payment.TelkomPaymentReceipt;
import com.dwidasa.ib.pages.payment.TrainPaymentReceipt;
import com.dwidasa.ib.pages.payment.TvPaymentReceipt;
import com.dwidasa.ib.pages.payment.WaterPaymentReceipt;
import com.dwidasa.ib.pages.transfer.TransferAtmbReceipt;
import com.dwidasa.ib.pages.transfer.TransferOtherReceipt;
import com.dwidasa.ib.pages.transfer.TransferReceipt;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class TransactionHistoryResult {
	private static Logger logger = Logger.getLogger( TransactionHistoryResult.class );
	@Property(write = false)
    @Persist
    private AccountView accountView;

    @Property(write = false)
    @Persist
    private Date startDate;

    @Property(write = false)
    @Persist
    private Date endDate;

    @Inject
    private AccountService accountService;

    @Inject
    private Locale locale;

    @Property
    private DateFormat customDate = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, locale);

    @Property
    private EvenOdd evenOdd;

    @Property
    private List<AccountStatementView> asvList;

    @Property
    private AccountStatementView asv;

    @Inject
    private Messages messages;

    @Inject
    private TransactionService transactionService;
    
    @Inject
    private TransactionDataService transactionDataService;
    
//    @Persist
//    @Property
//    private boolean StatusHistory;

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    Object onActivate() {
        if (accountView == null) {
            return TransactionHistory.class;
        }

        return null;
    }

    void setupRender() {
        evenOdd = new EvenOdd();
        evenOdd.setEven(true);

        asvList = accountService.getTransactionHistory(accountView, startDate, endDate);
        //-- save to do this because transaction history service doesn't need this
        accountView.setCurrencyCode("IDR");
    }

    @DiscardAfter
    Object onSuccess() {
        return TransactionHistory.class;
    }
    
    public String getStrDateTime(Date date) {
		if (date == null) return " - ";
		return GeneralUtil.fmtDateTimess.print(date.getTime());
	}
	
    public String getRowStatus() {
        if (asv.getStatus().equals(0)) {
            return messages.get("failed");
        }
        else if (asv.getStatus().equals(1)) {
            return messages.get("succeed");
        }
        else if (asv.getStatus().equals(2)) {
        	return messages.get("pending");
        }
        return messages.get("canceled");
    }
	
	public String getStrTransactionType(String transactionType) {
		return messages.get("transaction" + transactionType);
	}
	
	@Inject
	private ThreadLocale threadLocale;

	@SuppressWarnings("unused")
	@Property
	private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
	
	public boolean isMultipleDays() {
		if (startDate == null || endDate == null) return false;
		if (endDate.after(startDate)) return true;
		return false;
	}
	
	@InjectPage private TransferReceipt transferReceipt;
    @InjectPage private TransferOtherReceipt transferOtherReceipt;
    @InjectPage private PlnPurchaseReceipt plnPurchaseReceipt;
    @InjectPage private VoucherPurchaseReceipt voucherPurchaseReceipt;
    @InjectPage private MncLifePurchaseConfirm mnclifepurchaseConfirm;
    @InjectPage private MncLifePurchaseReceipt mncLifePurchaseReceipt ;
    @InjectPage private TransferTokenDompetkuReceipt transferTokenDompetkuReceipt;
    @InjectPage private TarikTunaiDompetkuReceipt tarikTunaiDompetkuReceipt;
    @InjectPage private TopUpDompetkuReceipt topUpDompetkuReceipt;
    @InjectPage private TopUpDompetkuConfirm upDompetkuConfirm;
    @InjectPage private PlnPaymentReceipt plnPaymentReceipt;
    @InjectPage private TelkomPaymentReceipt telkomPaymentReceipt;
    @InjectPage private InternetPaymentReceipt internetPaymentReceipt;
    @InjectPage private HpPaymentReceipt hpPaymentReceipt;
    @InjectPage private TvPaymentReceipt tvPaymentReceipt;
    @InjectPage private TrainPaymentReceipt trainPaymentReceipt;
    @InjectPage private TrainPurchaseReceipt trainPurchaseReceipt;
    @InjectPage private TransferAtmbReceipt transferAtmbReceipt;
    @InjectPage private NonTagListPaymentReceipt nonTagListPaymentReceipt;
    @InjectPage private CashInDelimaReceipt cashInDelimaReceipt;
    @InjectPage private WaterPaymentReceipt waterPaymentReceipt;
    @InjectPage private CashOutDelimaReceipt cashOutDelimaReceipt;
    @InjectPage private TiketKeretaDjatiPurchaseReceipt tiketKeretaDjatiPurchaseReceipt;
    @InjectPage private MultiFinancePaymentReceipt multiFinancePaymentReceipt;
    @InjectPage private MultiFinanceMncPaymentReceipt multiFinanceMncPaymentReceipt;
    @InjectPage private KartuKreditBNIPaymentReceipt kartuKreditBNIPaymentReceipt;
    //@InjectPage private CashToBankDelimaReceipt cashToBankDelimaReceipt;
    //@InjectPage private CashOutElmoDelimaReceipt cashOutElmoDelimaReceipt;
    @InjectPage private AeroTicketingFlightResult aeroTicketingFlightResult;
    
    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;
    
	Object onViewDetail(Long id, String status) {
		//System.out.println("HOHJOHOHOHOHOH "+status);
		if (id == null) return null;
    	logger.info("id=" + id + " accountView.getAccountNumber()=" + accountView.getAccountNumber());
		TransactionData transactionData = transactionDataService.getByTransactionFk(id, accountView.getAccountNumber());
    	if (transactionData == null) return null;

        transactionId = id;
    	if (transactionData.getClassName().endsWith("TransferView")) {
	    	TransferView tv = PojoJsonMapper.fromJson(transactionData.getTransactionData(), TransferView.class);
	    	if (tv.getTransactionType().equals(com.dwidasa.engine.Constants.TRANSFER_CODE)) {
	    		transferReceipt.setTransferView(tv);
		    	transferReceipt.setFromHistory(true);
		    	return transferReceipt;
            }
            else if (tv.getTransactionType().equals(com.dwidasa.engine.Constants.ATMB.TT_POSTING))
            {
                transferAtmbReceipt.setTransferReceiptView(tv);
                transferAtmbReceipt.setFromHistory(true);
                return transferAtmbReceipt;
            }
            else {
	    		transferOtherReceipt.setTransferView(tv);
	    		transferOtherReceipt.setFromHistory(true);
	    		return transferOtherReceipt;
	    	}
    	} else if (transactionData.getClassName().endsWith("PlnPurchaseView")) {
    		PlnPurchaseView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), PlnPurchaseView.class);
	    	plnPurchaseReceipt.setPlnPurchaseView(view);
	    	plnPurchaseReceipt.setFromHistory(true);
	    	return plnPurchaseReceipt;
    	} else if (transactionData.getClassName().endsWith("VoucherPurchaseView")) {
    		VoucherPurchaseView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), VoucherPurchaseView.class);
	    	voucherPurchaseReceipt.setVoucherPurchaseView(view);
	    	voucherPurchaseReceipt.setFromHistory(true);
	    	return voucherPurchaseReceipt;
    	} else if (transactionData.getClassName().endsWith("PlnPaymentView")) {
    		PlnPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), PlnPaymentView.class);
    		//plnPaymentReceipt.setStatusHistory(StatusHistory);
	    	plnPaymentReceipt.setPlnPaymentView(view);
	    	plnPaymentReceipt.setFromHistory(true);
	    	return plnPaymentReceipt;
    	} else if (transactionData.getClassName().endsWith("TelkomPaymentView")) {
    		TelkomPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), TelkomPaymentView.class);
    		//System.out.println("HOHOHOHOHOHOHOHOH : "+getRowStatus());
    		//telkomPaymentReceipt.setStatusHistory(StatusHistory);
	    	telkomPaymentReceipt.setTelkomPaymentView(view);
	    	telkomPaymentReceipt.setFromHistory(true);
	    	return telkomPaymentReceipt;
    	} else if (transactionData.getClassName().endsWith("InternetPaymentView")) {
    		InternetPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), InternetPaymentView.class);
	    	internetPaymentReceipt.setInternetPaymentView(view);
	    	internetPaymentReceipt.setFromHistory(true);
	    	return internetPaymentReceipt;
    	} else if (transactionData.getClassName().endsWith("HpPaymentView")) {
    		HpPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), HpPaymentView.class);
	    	hpPaymentReceipt.setHpPaymentView(view);
	    	hpPaymentReceipt.setFromHistory(true);
	    	return hpPaymentReceipt;
    	} else if (transactionData.getClassName().endsWith("TvPaymentView")) {
    		TvPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), TvPaymentView.class);
	    	tvPaymentReceipt.setTvPaymentView(view);
	    	tvPaymentReceipt.setFromHistory(true);
	    	return tvPaymentReceipt;
    	} else if (transactionData.getClassName().endsWith("TrainPaymentView")) {
    		TrainPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), TrainPaymentView.class);
	    	trainPaymentReceipt.setTrainPaymentView(view);
	    	trainPaymentReceipt.setFromHistory(true);
	    	return trainPaymentReceipt;
    	} else if (transactionData.getClassName().endsWith("TrainPurchaseView")) {
    		TrainPurchaseView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), TrainPurchaseView.class);
	    	trainPurchaseReceipt.setView(view);
	    	trainPurchaseReceipt.setFromHistory(true);
	    	return trainPurchaseReceipt;
    	} else if (transactionData.getClassName().endsWith("NonTagListPaymentView")) {
    		NonTagListPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), NonTagListPaymentView.class);
    		nonTagListPaymentReceipt.setNonTagListPaymentView(view);
    		nonTagListPaymentReceipt.setFromHistory(true);
    		return nonTagListPaymentReceipt;
    	} else if (transactionData.getClassName().endsWith("CashInDelimaView")) {
    		CashInDelimaView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), CashInDelimaView.class);
    		cashInDelimaReceipt.setCashInDelimaView(view);
    		cashInDelimaReceipt.setFromHistory(true);
    		return cashInDelimaReceipt;
    	} else if (transactionData.getClassName().endsWith("CashOutDelimaView")) {
            CashOutDelimaView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), CashOutDelimaView.class);
            cashOutDelimaReceipt.setCashOutDelimaView(view);
            cashOutDelimaReceipt.setFromHistory(true);
            return cashOutDelimaReceipt;
        } 
//    	else if (transactionData.getClassName().endsWith("CashToBankDelimaView")) {
//            CashToBankDelimaView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), CashToBankDelimaView.class);
//            cashToBankDelimaReceipt.setCashToBankDelimaView(view);
//            cashToBankDelimaReceipt.setFromHistory(true);
//            return cashToBankDelimaReceipt;
//        } 
//        else if (transactionData.getClassName().endsWith("CashOutElmoDelimaView")) {
//            CashOutElmoDelimaView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), CashOutElmoDelimaView.class);
//            cashOutElmoDelimaReceipt.setCashOutElmoDelimaView(view);
//            cashOutElmoDelimaReceipt.setFromHistory(true);
//            return cashOutElmoDelimaReceipt;
//    	} 
        else if (transactionData.getClassName().endsWith("WaterPaymentView")) {
    		WaterPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), WaterPaymentView.class);
    		waterPaymentReceipt.setWaterPaymentView(view);
    		waterPaymentReceipt.setFromHistory(true);
    		return waterPaymentReceipt;
    	} else if(transactionData.getClassName().endsWith("MncLifePurchaseView")){
    		MncLifePurchaseView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), MncLifePurchaseView.class);
    		mncLifePurchaseReceipt.setMncLifePurchaseView(view);
    		mncLifePurchaseReceipt.setFromHistory(true);
    		mncLifePurchaseReceipt.setFromStatus(true);
    		mncLifePurchaseReceipt.setStatusTransaksi(status);
    		return mncLifePurchaseReceipt;
        } 
    	else if (transactionData.getClassName().endsWith("AeroTicketingView")) {
            Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
            AeroTicketingView view = gson.fromJson(transactionData.getTransactionData(), AeroTicketingView.class);
            aeroTicketingFlightResult.setAeroTicketingView(view);
//            aeroTicketingFlightResult.setFromHistory(true);
            return aeroTicketingFlightResult;
    	}
    	else if (transactionData.getClassName().endsWith("TiketKeretaDjatiPurchaseView")) {
    	      TiketKeretaDjatiPurchaseView view = (TiketKeretaDjatiPurchaseView)PojoJsonMapper.fromJson(transactionData.getTransactionData(), TiketKeretaDjatiPurchaseView.class);
    	      tiketKeretaDjatiPurchaseReceipt.setTiketKeretaDjatiPurchaseView(view);
    	      tiketKeretaDjatiPurchaseReceipt.setTotalKursi(view.getTotalEmptySheat());
    	      tiketKeretaDjatiPurchaseReceipt.setFromHistory(true);
    	      tiketKeretaDjatiPurchaseReceipt.setStatusTransaksi(status);
    	      return tiketKeretaDjatiPurchaseReceipt;
    	}
    	
    	else if(transactionData.getClassName().endsWith("CashInDompetkuView")){
    		CashInDompetkuView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), CashInDompetkuView.class);
    		
    		
    		if(com.dwidasa.engine.Constants.DOMPETKU_CASH_OUT.equals(view.getTransactionType())){
    			
    			tarikTunaiDompetkuReceipt.setCashInDompetkuView(view);
    			tarikTunaiDompetkuReceipt.setFromHistory(true);
    			return tarikTunaiDompetkuReceipt;
    			
    		}else 
    			if(com.dwidasa.engine.Constants.DOMPETKU_CASH_IN.equals(view.getTransactionType())){
        			
    				topUpDompetkuReceipt.setCashInDompetkuView(view);
        			topUpDompetkuReceipt.setFromHistory(true);
        			return topUpDompetkuReceipt;
        			
        		}
    			
    	   if(com.dwidasa.engine.Constants.DOMPETKU_TRANSFER_TOKEN.equals(view.getTransactionType())){
    			
    		   transferTokenDompetkuReceipt.setCashInDompetkuView(view);
    		   transferTokenDompetkuReceipt.setFromHistory(true);
       		   return transferTokenDompetkuReceipt;
    			
    		}
    		
    	}
    	
    	else if(transactionData.getClassName().endsWith("MultiFinancePaymentView")){
    		MultiFinancePaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), MultiFinancePaymentView.class);
    		
    		
    		if(com.dwidasa.engine.Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE.equals(view.getTransactionType())){
    			
    			multiFinancePaymentReceipt.setMultiFinancePaymentView(view);
    			multiFinancePaymentReceipt.setFromHistory(true);
    			return multiFinancePaymentReceipt;
    			
    		}else 
    			if(com.dwidasa.engine.Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE_MNC.equals(view.getTransactionType())){
        			
    				multiFinanceMncPaymentReceipt.setMultiFinancePaymentView(view);
    				multiFinanceMncPaymentReceipt.setFromHistory(true);
        			return multiFinanceMncPaymentReceipt;
        			
        		}
    	}
    	
    	else if(transactionData.getClassName().endsWith("KartuKreditBNIPaymentView")){
    
    		KartuKreditBNIPaymentView view = PojoJsonMapper.fromJson(transactionData.getTransactionData(), KartuKreditBNIPaymentView.class);
    		kartuKreditBNIPaymentReceipt.setKartuKreditBNIPaymentView(view);
    		kartuKreditBNIPaymentReceipt.setFromHistory(true);
    		return kartuKreditBNIPaymentReceipt;
    	}
    	
    	return null;
	}
    public static void main (String[] args) {
        String str = "{\"totalAdult\":1,\"aeroCustomer\":{\"value\":\"DIDI SUGI 628152233445    \",\"title\":null,\"customerName\":\"DIDI SUGI\",\"customerPhone\":\"628152233445    \",\"customerEmail\":\"imam.baihaqi1999@gmail.com\",\"lastName\":null,\"firstName\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"totalChildren\":1,\"totalInfant\":0,\"isCheckingDepart\":null,\"departureFlight\":{\"airlineId\":\"GA\",\"sessionId\":null,\"airlineName\":\"Garuda\",\"flightDate\":\"10 07 2014 05:35:00\",\"etaConnecting\":\"10 07 2014 12:05:00\",\"isConnectingFlight\":true,\"aeroConnectingFlight\":{\"connectingFlightEta\":\"11 07 2014 09:20:00\",\"connectingFlightFrom\":\"SUB\",\"connectingFlightTo\":\"BPN\",\"connectingFlightDate\":\"11 07 2014 06:45:00\",\"connectingFlightFno\":\"GA 350\",\"connectingFlightEtd\":\"11 07 2014 06:45:00\",\"connectingFlightClassname\":\"H\",\"connectingFlightPrice\":711400,\"connectingFlightSeat\":9,\"throughFare\":false,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"aeroConnectingFlight2\":{\"connectingFlightEta\":\"11 07 2014 12:05:00\",\"connectingFlightFrom\":\"BPN\",\"connectingFlightTo\":\"TRK\",\"connectingFlightDate\":\"11 07 2014 11:05:00\",\"connectingFlightFno\":\"GA 668\",\"connectingFlightEtd\":\"11 07 2014 11:05:00\",\"connectingFlightClassname\":\"H\",\"connectingFlightPrice\":745500,\"connectingFlightSeat\":8,\"throughFare\":false,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"aeroFlightClasses\":[{\"value\":null,\"className\":\"T\",\"avaliableSeat\":9,\"price\":1542100,\"classId\":\"T\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"Q\",\"avaliableSeat\":9,\"price\":1812700,\"classId\":\"Q\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"N\",\"avaliableSeat\":9,\"price\":1976600,\"classId\":\"N\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"K\",\"avaliableSeat\":9,\"price\":2066800,\"classId\":\"K\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"M\",\"avaliableSeat\":9,\"price\":2076700,\"classId\":\"M\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"B\",\"avaliableSeat\":9,\"price\":2116300,\"classId\":\"B\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"Y\",\"avaliableSeat\":9,\"price\":2146000,\"classId\":\"Y\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"D\",\"avaliableSeat\":2,\"price\":5551600,\"classId\":\"D\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"C\",\"avaliableSeat\":9,\"price\":5835400,\"classId\":\"C\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":null,\"className\":\"J\",\"avaliableSeat\":9,\"price\":6401900,\"classId\":\"J\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null}],\"selectedClassId\":\"H\",\"selectedClass\":{\"value\":null,\"className\":\"T\",\"avaliableSeat\":9,\"price\":1542100,\"classId\":\"H\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"flightNumber\":\"GA 326\",\"arrivalAirportFullName\":null,\"departureAirportFullName\":null,\"arrivalAirportCode\":\"SUB\",\"isDepartureFlight\":true,\"adultPassengerSummary\":{\"value\":\"Adult 1\",\"total\":1542100,\"service\":null,\"basic\":1542100,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Adult\",\"pax\":1,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"childPassengerSummary\":{\"value\":\"Child 1\",\"total\":1542100,\"service\":null,\"basic\":1542100,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Child\",\"pax\":1,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"infantPassengerSummary\":{\"value\":\"Infant 0\",\"total\":1542100,\"service\":null,\"basic\":1542100,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Infant\",\"pax\":0,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"departureAirportCode\":\"CGK\",\"ticketPrice\":1542100,\"flightType\":\"connecting\",\"etd\":\"14 07 2014 21:06:00\",\"eta\":\"14 07 2014 21:06:00\",\"via\":\"-\",\"comission\":null,\"bookInfo\":{\"value\":\"8FKHYB\",\"status\":\"BOOKED\",\"airlineCode\":null,\"agentInsurance\":0.0,\"publishFare\":1491000.0,\"publishInsurance\":0.0,\"agentFare\":1442600.0,\"bookCode\":\"8FKHYB\",\"timeLimit\":\"24 06 2014 17:08:02\",\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"responseCode\":null,\"total\":null,\"description\":null,\"currencyCode\":null,\"customerId\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"VOLT\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"02 07 2014 12:16:46\",\"productName\":null,\"feeIndicator\":null,\"referenceName\":null,\"billerName\":null,\"billerCode\":null,\"referenceNumber\":null,\"toAccountType\":null,\"customerReference\":null,\"accountNumber\":\"1574087868\",\"traceNumber\":null,\"providerName\":null,\"transactionDateString\":\"02/07/2014 12:16 \",\"transactionType\":\"9m\",\"save\":null,\"inputType\":null},\"departAirlineName\":null,\"returnFlight\":{\"airlineId\":\"GA\",\"sessionId\":null,\"airlineName\":\"Lionair\",\"flightDate\":\"22 07 2014 06:00:00\",\"etaConnecting\":\"22 07 2014 08:55:00\",\"isConnectingFlight\":true,\"aeroConnectingFlight\":{\"connectingFlightEta\":\"22 07 2014 20:55:00\",\"connectingFlightFrom\":\"BPN\",\"connectingFlightTo\":\"MDC\",\"connectingFlightDate\":\"22 07 2014 19:15:00\",\"connectingFlightFno\":\"GA 688\",\"connectingFlightEtd\":\"22 07 2014 19:15:00\",\"connectingFlightClassname\":\"T\",\"connectingFlightPrice\":1096400,\"connectingFlightSeat\":9,\"throughFare\":false,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"aeroConnectingFlight2\":{\"connectingFlightEta\":\"23 07 2014 08:25:00\",\"connectingFlightFrom\":\"MDC\",\"connectingFlightTo\":\"CGK\",\"connectingFlightDate\":\"23 07 2014 06:15:00\",\"connectingFlightFno\":\"GA 607\",\"connectingFlightEtd\":\"23 07 2014 06:15:00\",\"connectingFlightClassname\":\"H\",\"connectingFlightPrice\":1484900,\"connectingFlightSeat\":9,\"throughFare\":false,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"aeroFlightClasses\":[{\"value\":null,\"className\":\"Y\",\"avaliableSeat\":7,\"price\":2121400,\"classId\":\"Y\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null}],\"selectedClassId\":\"T\",\"selectedClass\":{\"value\":null,\"className\":\"Y\",\"avaliableSeat\":7,\"price\":2121400,\"classId\":\"T\",\"classLabel\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"flightNumber\":\"GA 669\",\"arrivalAirportFullName\":null,\"departureAirportFullName\":null,\"arrivalAirportCode\":\"BPN\",\"isDepartureFlight\":false,\"adultPassengerSummary\":{\"value\":\"Adult 1\",\"total\":2121400,\"service\":null,\"basic\":2121400,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Adult\",\"pax\":1,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"childPassengerSummary\":{\"value\":\"Child 1\",\"total\":2121400,\"service\":null,\"basic\":2121400,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Child\",\"pax\":1,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"infantPassengerSummary\":{\"value\":\"Infant 0\",\"total\":2121400,\"service\":null,\"basic\":2121400,\"tax\":null,\"iwjr\":null,\"passengerType\":\"Infant\",\"pax\":0,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"departureAirportCode\":\"TRK\",\"ticketPrice\":2121400,\"flightType\":\"direct\",\"etd\":\"25 07 2014 15:19:00\",\"eta\":\"26 07 2014 00:40:00\",\"via\":\"-\",\"comission\":null,\"bookInfo\":{\"value\":\"8FKHYB\",\"status\":\"BOOKED\",\"airlineCode\":null,\"agentInsurance\":null,\"publishFare\":null,\"publishInsurance\":null,\"agentFare\":null,\"bookCode\":\"8FKHYB\",\"timeLimit\":\"24 06 2014 17:08:02\",\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},\"responseCode\":null,\"total\":null,\"description\":null,\"currencyCode\":null,\"customerId\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"VOLT\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"02 07 2014 12:16:46\",\"productName\":null,\"feeIndicator\":null,\"referenceName\":null,\"billerName\":null,\"billerCode\":null,\"referenceNumber\":null,\"toAccountType\":null,\"customerReference\":null,\"accountNumber\":\"1574087868\",\"traceNumber\":null,\"providerName\":null,\"transactionDateString\":\"02/07/2014 12:16 \",\"transactionType\":\"9m\",\"save\":null,\"inputType\":null},\"returnAirlineName\":null,\"sessionId\":null,\"isDepartOnly\":false,\"departAirlineCode\":\"GA\",\"returnAirlineCode\":\"GA\",\"departureAirportCode\":\"CGK\",\"departureAirportName\":\"JAKARTA\",\"destinationAirportCode\":\"SUB\",\"destinationAirportName\":\"SURABAYA\",\"ticketPrice\":3663500,\"ticketComission\":0,\"assuranceComission\":0,\"agentMargin\":0,\"totalAgentComission\":0,\"assurancePrice\":0,\"totalAgentPrice\":0,\"totalCustomerPrice\":0,\"departDate\":\"11 07 2014 00:00:00\",\"returnDate\":\"15 07 2014 00:00:00\",\"agentPrice\":3663500,\"passengers\":[{\"value\":\"DIDI SUGI\",\"parent\":\"SUGI,DIDI\",\"country\":\"INDONESIA\",\"passengerType\":\"Adult\",\"passengerTitle\":\"Mr\",\"passengerFirstName\":\"DIDI\",\"passengerLastName\":\"SUGI\",\"passengerIdCard\":\"SIM00001\",\"passengerDob\":\"13 06 1983 00:00:00\",\"expirePaspor\":null,\"countryPaspor\":null,\"paspor\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null},{\"value\":\"ISAL FAISAL\",\"parent\":\"FAISAL,ISAL\",\"country\":\"INDONESIA\",\"passengerType\":\"Child\",\"passengerTitle\":\"Mstr\",\"passengerFirstName\":\"ISAL\",\"passengerLastName\":\"FAISAL\",\"passengerIdCard\":null,\"passengerDob\":\"18 06 2005 00:00:00\",\"expirePaspor\":null,\"countryPaspor\":null,\"paspor\":null,\"id\":null,\"createdby\":null,\"created\":null,\"updatedby\":null,\"updated\":null}],\"bookId\":null,\"pacBook\":{\"pnrIdDepart\":\"920686\",\"pnrIdReturn\":\"920686\",\"retrieveDepart\":{\"status\":\"CANCELLED\",\"airlineCode\":\"GA\",\"bookingCode\":\"8SN229\",\"agentInsurance\":0.0,\"publishFare\":735800.0,\"publishInsurance\":0.0,\"agentFare\":712000.0,\"timeLimit\":\"02 07 2014 09:13:38\"},\"retrieveReturn\":null},\"responseCode\":null,\"total\":2045600,\"description\":null,\"currencyCode\":\"360\",\"customerId\":102114,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"I1516288\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"02 07 2014 12:16:46\",\"productName\":null,\"feeIndicator\":null,\"referenceName\":null,\"billerName\":null,\"billerCode\":null,\"referenceNumber\":null,\"toAccountType\":\"00\",\"customerReference\":null,\"accountNumber\":\"1574087868\",\"traceNumber\":null,\"providerName\":null,\"transactionDateString\":\"02/07/2014 12:16 \",\"transactionType\":\"9p\",\"save\":false,\"inputType\":null}";
        System.out.println("str=" + str);
        //AeroTicketingView view = PojoJsonMapper.fromJson(str, AeroTicketingView.class);
        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
        AeroTicketingView view = gson.fromJson(str, AeroTicketingView.class);
        System.out.println("view=" + view);
    }
}