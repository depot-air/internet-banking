package com.dwidasa.ib.pages.purchase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.train.ApiGetSchedule;
import com.dwidasa.engine.model.train.TrainChooseSeat;
import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.train.TrainSchedule;
import com.dwidasa.engine.model.train.TrainSubclass;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.TrainStationService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

@Import(stylesheet = {"context:bprks/css/train/select2.css","context:bprks/css/style_train.css",
		"context:bprks/css/train/ui-lightness/jquery-ui-1.10.4.custom.min.css"}, 
		library={
			"context:bprks/js/aero/jquery.js", "context:bprks/js/aero/jquery-ui.js",
			"context:bprks/js/aero/select2.min.js", 			
			"context:bprks/js/purchase/trainfLSScript.js"})
public class TrainPurchaseConfirm {
	@Persist
	@Property(write = false)
	private TrainPurchaseView view;
	
	@Property
	private TrainPassenger passenger;
	
	public void setView(TrainPurchaseView view) {
		this.view = view;
	}

	@Inject
	private TrainStationService trainStationService;
	
	@InjectComponent
	private Form form;
	
	@Inject
	private PurchaseService purchaseService;
	
	@Inject
	private OtpManager otpManager;
	
	@Property
    private TokenView tokenView;
	
	@Property
    private int tokenType;
	
	@Inject
	private SessionManager sessionManager;
	
	private boolean isBackButton;
	
	private boolean isChangeSeatButton;
	
	public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }
	
	public void setupRender() {
    	sessionManager.setSessionLastPage(TrainPurchaseConfirm.class.toString());
        setTokenType();
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }
    
    void onSelectedFromChangeSeat() {
    	isChangeSeatButton = true;
    }
	
	void onSelectedFromBack() {
		isBackButton = true;
	}
	
	@InjectPage
	private TrainPurchaseChangeSeat trainPurchaseChangeSeat;
	
	private TrainChooseSeat trainChooseSeat;	
	
	void onValidateFromForm() {
		try {
			if (isBackButton) {
				//cancel book
				form.clearErrors();
				cancelBook(view);
				return;
			}
			
			if (isChangeSeatButton) {
				form.clearErrors();
				trainChooseSeat = inquirySeat(view);
				return;
			}
			
			if (form.getHasErrors()) {
				return;
			}
			
            if (otpManager.validateToken(view.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            	view.setTransactionDate(new Date());
            	view.setTransactionType(com.dwidasa.engine.Constants.KAI_PAYMENT);
            	if ("LOCAL".equals(TrainPurchaseInput.MODE)) {
            		view.setResponseCode("00");
            		view.setReferenceNumber(ReferenceGenerator.generate());
            	} else {
            		purchaseService.execute(view);
            	}
            }
		}  catch (BusinessException e) {
			form.recordError(e.getFullMessage());
            e.printStackTrace();
            return;
		} catch (Exception e) {
			form.recordError(e.getMessage());
			e.printStackTrace();
			return;
		} 
	}
	
	private TrainChooseSeat inquirySeat(TrainPurchaseView view) {
		String json = "";
		if ("LOCAL".equals(TrainPurchaseInput.MODE)) {
//			json = "{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"train_no\": \"1331\", \"dep_date\": \"20140611\", \"seat_null\": [[\"KERETA_EKS\",1,[[1,\"A\",\"A\",0],[1,\"B\",\"A\",0],[1,\"C\",\"A\",0],[1,\"D\",\"A\",0],[2,\"A\",\"A\",0],[2,\"B\",\"A\",0],[2,\"C\",\"A\",0],[2,\"D\",\"A\",0],[3,\"A\",\"A\",0],[3,\"B\",\"A\",0],[3,\"C\",\"A\",0],[3,\"D\",\"A\",0],[4,\"A\",\"A\",0],[4,\"B\",\"A\",0],[4,\"C\",\"A\",0],[4,\"D\",\"A\",0],[5,\"A\",\"A\",0],[5,\"B\",\"A\",0],[5,\"C\",\"A\",0],[5,\"D\",\"A\",0],[6,\"A\",\"A\",0],[6,\"B\",\"A\",0],[6,\"C\",\"A\",0],[6,\"D\",\"A\",0],[7,\"A\",\"A\",0],[7,\"B\",\"A\",0],[7,\"C\",\"A\",0],[7,\"D\",\"A\",0],[8,\"A\",\"A\",0],[8,\"B\",\"A\",0],[8,\"C\",\"A\",0],[8,\"D\",\"A\",0],[9,\"A\",\"A\",0],[9,\"B\",\"A\",0],[9,\"C\",\"A\",0],[9,\"D\",\"A\",0],[10,\"A\",\"A\",0],[10,\"B\",\"A\",0],[10,\"C\",\"A\",0],[10,\"D\",\"A\",0],[11,\"A\",\"A\",0],[11,\"B\",\"A\",0],[11,\"C\",\"A\",0],[11,\"D\",\"A\",0],[12,\"A\",\"A\",0],[12,\"B\",\"A\",0],[12,\"C\",\"A\",0],[12,\"D\",\"A\",0],[13,\"A\",\"A\",0],[13,\"B\",\"A\",0],[13,\"C\",\"A\",0],[13,\"D\",\"A\",0],[14,\"A\",\"A\",0],[14,\"B\",\"A\",0],[14,\"C\",\"A\",0],[14,\"D\",\"A\",0],[15,\"A\",\"A\",0],[15,\"B\",\"A\",0],[15,\"C\",\"A\",0],[15,\"D\",\"A\",0],[16,\"A\",\"A\",0],[16,\"B\",\"A\",0],[16,\"C\",\"A\",0],[16,\"D\",\"A\",0],[17,\"A\",\"A\",0],[17,\"B\",\"A\",0],[17,\"C\",\"A\",0],[17,\"D\",\"A\",0],[18,\"A\",\"A\",0],[18,\"B\",\"A\",0],[18,\"C\",\"A\",0],[18,\"D\",\"A\",0],[19,\"A\",\"A\",0],[19,\"B\",\"A\",0],[19,\"C\",\"A\",0],[19,\"D\",\"A\",0],[20,\"A\",\"A\",0],[20,\"B\",\"A\",0],[20,\"C\",\"A\",0],[20,\"D\",\"A\",0]]],[\"KERETA_EKS\",2,[[1,\"A\",\"A\",0],[1,\"B\",\"A\",0],[1,\"C\",\"A\",0],[1,\"D\",\"A\",0],[2,\"A\",\"A\",0],[2,\"B\",\"A\",0],[2,\"C\",\"A\",0],[2,\"D\",\"A\",0],[3,\"A\",\"A\",0],[3,\"B\",\"A\",0],[3,\"C\",\"A\",0],[3,\"D\",\"A\",0],[4,\"A\",\"A\",0],[4,\"B\",\"A\",0],[4,\"C\",\"A\",0],[4,\"D\",\"A\",0],[5,\"A\",\"A\",0],[5,\"B\",\"A\",0],[5,\"C\",\"A\",0],[5,\"D\",\"A\",0],[6,\"A\",\"A\",0],[6,\"B\",\"A\",0],[6,\"C\",\"A\",0],[6,\"D\",\"A\",0],[7,\"A\",\"A\",0],[7,\"B\",\"A\",0],[7,\"C\",\"A\",0],[7,\"D\",\"A\",0],[8,\"A\",\"A\",0],[8,\"B\",\"A\",0],[8,\"C\",\"A\",0],[8,\"D\",\"A\",0],[9,\"A\",\"A\",0],[9,\"B\",\"A\",0],[9,\"C\",\"A\",0],[9,\"D\",\"A\",0],[10,\"A\",\"A\",0],[10,\"B\",\"A\",0],[10,\"C\",\"A\",0],[10,\"D\",\"A\",0],[11,\"A\",\"A\",0],[11,\"B\",\"A\",0],[11,\"C\",\"A\",0],[11,\"D\",\"A\",0],[12,\"A\",\"A\",0],[12,\"B\",\"A\",0],[12,\"C\",\"A\",0],[12,\"D\",\"A\",0],[13,\"A\",\"A\",0],[13,\"B\",\"A\",0],[13,\"C\",\"A\",0],[13,\"D\",\"A\",0],[14,\"A\",\"A\",0],[14,\"B\",\"A\",0],[14,\"C\",\"A\",0],[14,\"D\",\"A\",0],[15,\"A\",\"A\",0],[15,\"B\",\"A\",0],[15,\"C\",\"A\",0],[15,\"D\",\"A\",0],[16,\"A\",\"A\",0],[16,\"B\",\"A\",0],[16,\"C\",\"A\",0],[16,\"D\",\"A\",0],[17,\"A\",\"A\",0],[17,\"B\",\"A\",0],[17,\"C\",\"A\",0],[17,\"D\",\"A\",0],[18,\"A\",\"A\",0],[18,\"B\",\"A\",0],[18,\"C\",\"A\",0],[18,\"D\",\"A\",0],[19,\"A\",\"A\",0],[19,\"B\",\"A\",0],[19,\"C\",\"A\",0],[19,\"D\",\"A\",0],[20,\"A\",\"A\",0],[20,\"B\",\"A\",0],[20,\"C\",\"A\",0],[20,\"D\",\"A\",0]]]] }";
			json = "{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"train_no\": \"1331\", \"dep_date\": \"20140611\", \"seat_null\": [[\"KERETA_EKS\",1,[[1,\"A\",\"A\",0],[1,\"B\",\"A\",0],[1,\"C\",\"A\",0],[1,\"D\",\"A\",0],[2,\"A\",\"A\",0],[2,\"B\",\"A\",0],[2,\"C\",\"A\",0],[2,\"D\",\"A\",0],[3,\"A\",\"A\",0],[3,\"B\",\"A\",0],[3,\"C\",\"A\",0],[3,\"D\",\"A\",0],[4,\"A\",\"A\",0],[4,\"B\",\"A\",0],[4,\"C\",\"A\",0],[4,\"D\",\"A\",0],[5,\"A\",\"A\",0],[5,\"B\",\"A\",0],[5,\"C\",\"A\",0],[5,\"D\",\"A\",0],[6,\"A\",\"A\",0],[6,\"B\",\"A\",0],[6,\"C\",\"A\",0],[6,\"D\",\"A\",0],[7,\"A\",\"A\",0],[7,\"B\",\"A\",0],[7,\"C\",\"A\",0],[7,\"D\",\"A\",0],[8,\"A\",\"A\",0],[8,\"B\",\"A\",0],[8,\"C\",\"A\",0],[8,\"D\",\"A\",0],[9,\"A\",\"A\",0],[9,\"B\",\"A\",0],[9,\"C\",\"A\",0],[9,\"D\",\"A\",0],[10,\"A\",\"A\",0],[10,\"B\",\"A\",0],[10,\"C\",\"A\",0],[10,\"D\",\"A\",0],[11,\"A\",\"A\",0],[11,\"B\",\"A\",0],[11,\"C\",\"A\",0],[11,\"D\",\"A\",0],[12,\"A\",\"A\",0],[12,\"B\",\"A\",0],[12,\"C\",\"A\",0],[12,\"D\",\"A\",0],[13,\"A\",\"A\",0],[13,\"B\",\"A\",0],[13,\"C\",\"A\",0],[13,\"D\",\"A\",0],[14,\"A\",\"A\",0],[14,\"B\",\"A\",0],[14,\"C\",\"A\",0],[14,\"D\",\"A\",0],[15,\"A\",\"A\",0],[15,\"B\",\"A\",0],[15,\"C\",\"A\",0],[15,\"D\",\"A\",0],[16,\"A\",\"A\",0],[16,\"B\",\"A\",0],[16,\"C\",\"A\",0],[16,\"D\",\"A\",0],[17,\"A\",\"A\",0],[17,\"B\",\"A\",0],[17,\"C\",\"A\",0],[17,\"D\",\"A\",0],[18,\"A\",\"A\",0],[18,\"B\",\"A\",0],[18,\"C\",\"A\",0],[18,\"D\",\"A\",0],[19,\"A\",\"A\",0],[19,\"B\",\"A\",0],[19,\"C\",\"A\",0],[19,\"D\",\"A\",0],[20,\"A\",\"A\",0],[20,\"B\",\"A\",0],[20,\"C\",\"A\",0],[20,\"D\",\"A\",0]]],[\"KERETA_EKS\",2,[[1,\"A\",\"A\",0],[1,\"B\",\"A\",0],[1,\"C\",\"A\",0],[1,\"D\",\"A\",0],[2,\"A\",\"A\",0],[2,\"B\",\"A\",0],[2,\"C\",\"A\",0],[2,\"D\",\"A\",0],[3,\"A\",\"A\",0],[3,\"B\",\"A\",0],[3,\"C\",\"A\",0],[3,\"D\",\"A\",0],[4,\"A\",\"A\",0],[4,\"B\",\"A\",0],[4,\"C\",\"A\",0],[4,\"D\",\"A\",0],[5,\"A\",\"A\",0],[5,\"B\",\"A\",0],[5,\"C\",\"A\",0],[5,\"D\",\"A\",0],[6,\"A\",\"A\",0],[6,\"B\",\"A\",0],[6,\"C\",\"A\",0],[6,\"D\",\"A\",0],[7,\"A\",\"A\",0],[7,\"B\",\"A\",0],[7,\"C\",\"A\",0],[7,\"D\",\"A\",0],[8,\"A\",\"A\",0],[8,\"B\",\"A\",0],[8,\"C\",\"A\",0],[8,\"D\",\"A\",0],[9,\"A\",\"A\",0],[9,\"B\",\"A\",0],[9,\"C\",\"A\",0],[9,\"D\",\"A\",0],[10,\"A\",\"A\",0],[10,\"B\",\"A\",0],[10,\"C\",\"A\",0],[10,\"D\",\"A\",0],[11,\"A\",\"A\",0],[11,\"B\",\"A\",0],[11,\"C\",\"A\",0],[11,\"D\",\"A\",0],[12,\"A\",\"A\",0],[12,\"B\",\"A\",0],[12,\"C\",\"A\",0],[12,\"D\",\"A\",0],[13,\"A\",\"A\",0],[13,\"B\",\"A\",0],[13,\"C\",\"A\",0],[13,\"D\",\"A\",0],[14,\"A\",\"A\",0],[14,\"B\",\"A\",0],[14,\"C\",\"A\",0],[14,\"D\",\"A\",0],[15,\"A\",\"A\",0],[15,\"B\",\"A\",0],[15,\"C\",\"A\",0],[15,\"D\",\"A\",0],[16,\"A\",\"A\",0],[16,\"B\",\"A\",0],[16,\"C\",\"A\",0],[16,\"D\",\"A\",0],[17,\"A\",\"A\",0],[17,\"B\",\"A\",0],[17,\"C\",\"A\",0],[17,\"D\",\"A\",0],[18,\"A\",\"A\",0],[18,\"B\",\"A\",0],[18,\"C\",\"A\",0],[18,\"D\",\"A\",0],[19,\"A\",\"A\",0],[19,\"B\",\"A\",0],[19,\"C\",\"A\",0],[19,\"D\",\"A\",0],[20,\"A\",\"A\",0],[20,\"B\",\"A\",0],[20,\"C\",\"A\",1]]]] }";
		} else {
			DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
			Transaction t = new Transaction();
			t.setTransactionType(com.dwidasa.engine.Constants.KAI_GET_SEAT_NULL_PER_SUBCLASS);
			t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
			t.setFromAccountType("00");
			t.setToAccountType("00");
			t.setFromAccountNumber(view.getAccountNumber());
			t.setCardNumber(view.getCardNumber());
			t.setProviderCode("PAC");
			StringBuilder bit48 = new StringBuilder();
			bit48.append(view.getOriginCode()).append("#").append(view.getDestinationCode()).append("#").append(view.getTrainNumber()).append("#");
			bit48.append(fmtYmd.format(view.getDepartureDate())).append("#").append(view.getSubclass());
			t.setFreeData1(bit48.toString());
			CommLink link = new MxCommLink(t);
	        link.sendMessage();
	        if (!"00".equals(t.getResponseCode()) && !"10".equals(t.getResponseCode())) {
	        	throw new BusinessException("IB-1009", t.getResponseCode());
	        }
	        System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
	        System.out.println("HOHO RESPON BIT 48:");
	        System.out.println(t.getFreeData1());
			json = t.getFreeData1();
		}
		TrainChooseSeat trainChooseSeat = PojoJsonMapper.trainFromJsonToTrainChooseSeat(json, view);
		return trainChooseSeat;
		
	}

	private void cancelBook(TrainPurchaseView view) {
		String json = "";
		if ("LOCAL".equals(TrainPurchaseInput.MODE)) {
			json = "{ \"err_code\": 0, \"book_code\": \"E31Y22\", \"status\": \"XX\", \"refund_amount\": 0 }";
		} else {
			Transaction t = new Transaction();
			t.setTransactionType(com.dwidasa.engine.Constants.KAI_CANCEL_BOOK);
			t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
			t.setFromAccountType("00");
			t.setToAccountType("00");
			t.setFromAccountNumber(view.getAccountNumber());
			t.setCardNumber(view.getCardNumber());
			t.setProviderCode("PAC");
			StringBuilder bit48 = new StringBuilder();
			bit48.append(view.getBookingCode()).append("#").append("Pembatalan");
			t.setFreeData1(bit48.toString());
			CommLink link = new MxCommLink(t);
	        link.sendMessage();
	        if (!"00".equals(t.getResponseCode()) && !"10".equals(t.getResponseCode())) {
	        	throw new BusinessException("IB-1009", t.getResponseCode());
	        }
	        System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
	        System.out.println("HOHO RESPON BIT 48:");
	        System.out.println(t.getFreeData1());
			json = t.getFreeData1();
		}
		PojoJsonMapper.trainFromJsonGenericValidate(json);
		
	}
	
	@InjectPage
	private TrainPurchaseDetail trainPurchaseDetail;
	
	@InjectPage
	private TrainPurchaseReceipt trainPurchaseReceipt; 

	@DiscardAfter
	Object onSuccessFromForm() {
		if (isBackButton) {
			trainPurchaseDetail.setView(view);
			return trainPurchaseDetail;
		}
		if (isChangeSeatButton) {
			trainPurchaseChangeSeat.setView(view);
			trainPurchaseChangeSeat.setTrainChooseSeat(trainChooseSeat);
			return trainPurchaseChangeSeat;
		}
		
		trainPurchaseReceipt.setView(view);
		return trainPurchaseReceipt;
	}
	
	@Persist
	@Property(write = false)
	private ApiGetSchedule apiGetSchedule;

	public void setApiGetSchedule(ApiGetSchedule apiGetSchedule) {
		this.apiGetSchedule = apiGetSchedule;
	}
	
	@Inject
    private ThreadLocale threadLocale;
	
	@Property
	private TrainSchedule trainSchedule;
	
	@Property
	private TrainSubclass trainSubclass;
	
	@Property
	private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
	
	@Property
	private DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	public String getStrPenumpang() {
		StringBuilder sb = new StringBuilder();
//		2 Dewasa, 1 Anak, 1 Infant
		if (view.getAdult() > 0) {
			sb.append(", ").append(view.getAdult()).append(" Dewasa");
		}
		if (view.getChild() > 0) {
			sb.append(", ").append(view.getChild()).append(" Anak");
		}
		if (view.getInfant() > 0) {
			sb.append(", ").append(view.getInfant()).append(" Infant");
		}
		if (sb.length() > 0) {
			sb.delete(0,2);
		}
		return sb.toString();
	}
	
	public String getStrDeparture() {
//		Selasa, 27 Mei 2014
		if (view == null) return "";
		Date depDate = view.getDepartureDate();
		if (depDate == null) return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(depDate);
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.strDay[cal.get(Calendar.DAY_OF_WEEK)-1]).append(", ");
		sb.append(cal.get(Calendar.DAY_OF_MONTH)).append(" ").append(Constants.strMonth[cal.get(Calendar.MONTH)]);
		sb.append(" ").append(cal.get(Calendar.YEAR));
		DateFormat fmtHHmm = new SimpleDateFormat("HH:mm");
		sb.append(" ").append(fmtHHmm.format(view.getDepartureDate()));
		return sb.toString();
	}
	
	public String getStrArrival() {
//		Selasa, 27 Mei 2014
		if (view == null) return "";
		Date arrivalDate = view.getArrivalDate();
		if (arrivalDate == null) return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(arrivalDate);
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.strDay[cal.get(Calendar.DAY_OF_WEEK)-1]).append(", ");
		sb.append(cal.get(Calendar.DAY_OF_MONTH)).append(" ").append(Constants.strMonth[cal.get(Calendar.MONTH)]);
		sb.append(" ").append(cal.get(Calendar.YEAR));
		DateFormat fmtHHmm = new SimpleDateFormat("HH:mm");
		sb.append(" ").append(fmtHHmm.format(view.getArrivalDate()));
		return sb.toString();
	}


	public String getStrTime(Date date) {
		if (date == null) return "";
		DateFormat fmt = new SimpleDateFormat("HH:mm");
		return fmt.format(date);
	}
	
	@Property
	private Integer index;
	
	@Property
	private String label;
	
	public boolean isAdult() {
		if (label == null) return false;
		return label.startsWith("Dewasa");
	}
	
    public boolean isHasSeat() {
		if (passenger == null) return false;
		return "A".equals(passenger.getType()) || "C".equals(passenger.getType());
	}

	
}
