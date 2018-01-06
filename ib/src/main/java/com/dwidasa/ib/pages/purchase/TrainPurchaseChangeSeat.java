package com.dwidasa.ib.pages.purchase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
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
import com.dwidasa.engine.model.train.TrainWagon;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.TrainStationService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

@Import(stylesheet = {"context:bprks/css/train/select2.css","context:bprks/css/style_train.css",
		"context:bprks/css/train/ui-lightness/jquery-ui-1.10.4.custom.min.css"}, 
		library={
			"context:bprks/js/aero/jquery.js", "context:bprks/js/aero/jquery-ui.js",
			"context:bprks/js/aero/select2.min.js", 			
			"context:bprks/js/purchase/trainfLQScript.js"})
public class TrainPurchaseChangeSeat {
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
	
	public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }
	
	public void setupRender() {
    	sessionManager.setSessionLastPage(TrainPurchaseChangeSeat.class.toString());
        setTokenType();
        
        List<OptionModel> optionList = new ArrayList<OptionModel>();
		for (TrainWagon wagon : trainChooseSeat.getTrainWagonList()) {
			OptionModel model = new OptionModelImpl(wagon.getWagonCode() + "-" + wagon.getWagonNumber());
			optionList.add(model);
		}
		wagonModel = new SelectModelImpl(null, optionList);
		selectedWagon = trainChooseSeat.getSelectedWagonCode() + "-" + trainChooseSeat.getSelectedWagonNumber();
		
//		seatLayoutClass = "layout" + trainChooseSeat.getColCount(); 
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }
    
	void onSelectedFromBack() {
		isBackButton = true;
	}
	
	void onValidateFromForm() {
		if (isBackButton) {
			form.clearErrors();
			return;
		}
		
		if (form.getHasErrors()) {
			return;
		}
		try {
			int posDash = selectedWagon.indexOf("-");
			String wagonCode = selectedWagon.substring(0, posDash);
			String wagonNumber = selectedWagon.substring(posDash + 1);
			changeSeat(view, wagonCode, wagonNumber, trainChooseSeat.getSelectedSeat());
		} catch (BusinessException e) {
			form.recordError(e.getFullMessage());
            e.printStackTrace();
            return;
		} catch (Exception e) {
			form.recordError(e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	private void changeSeat(TrainPurchaseView view, String wagonCode, String wagonNumber, String strSeat) {
		String json = "";
		if ("LOCAL".equals(TrainPurchaseInput.MODE)) {
			json = "{ \"err_code\": 0, \"book_code\": \"YYAVB8\", \"wagon_code\": [\"KERETA_EKS\"], \"wagon_no\": [1], \"seat\": [\"2A\"] }";
		} else {
			Transaction t = new Transaction();
			t.setTransactionType(com.dwidasa.engine.Constants.KAI_MANUAL_SEAT);
			t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
			t.setFromAccountType("00");
			t.setToAccountType("00");
			t.setFromAccountNumber(view.getAccountNumber());
			t.setCardNumber(view.getCardNumber());
			t.setProviderCode("PAC");
			StringBuilder bit48 = new StringBuilder();
			bit48.append(view.getBookingCode()).append("#").append(wagonCode).append("#").append(wagonNumber).append("#").append(strSeat);
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
		//kalau pindah kursi valid, maka update field seat di view
		view.setWagonCode(wagonCode);
		view.setWagonNumber(wagonNumber);
		String[] arrSeat = strSeat.split(",");
		int i = 0;		
		for (TrainPassenger passenger : view.getPassengerList()) {
			if ("I".equals(passenger.getType())) continue; //infant ga punya kursi
			passenger.setWagonCode(wagonCode);
			passenger.setWagonNumber(wagonNumber);
			passenger.setSeatRow(arrSeat[i].substring(0, arrSeat[i].length()-1));
			passenger.setSeatCol(arrSeat[i].substring(arrSeat[i].length()-1));
			i++;
		}
		
	}

	@InjectPage
	private TrainPurchaseConfirm trainPurchaseConfirm; 

	@DiscardAfter
	Object onSuccessFromForm() {
		if (isBackButton) {
			trainPurchaseConfirm.setView(view);
			return trainPurchaseConfirm;
		}
		trainPurchaseConfirm.setView(view);
		return trainPurchaseConfirm;
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
    
    @Property(write = false)
    @Persist
    private TrainChooseSeat trainChooseSeat;

	public void setTrainChooseSeat(TrainChooseSeat trainChooseSeat) {
		this.trainChooseSeat = trainChooseSeat;
	}
	
	@Property
	private String selectedWagon;
	
//	@Property
//	private String seatLayoutClass;

	@Property
	private SelectModel wagonModel;
	
	@Property
	private TrainWagon trainWagon;
    
}
