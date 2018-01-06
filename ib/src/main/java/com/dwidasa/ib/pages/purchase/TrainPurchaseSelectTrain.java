package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.dwidasa.engine.model.train.TrainSchedule;
import com.dwidasa.engine.model.train.TrainSubclass;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.TrainStationService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.ib.Constants;

@Import(stylesheet = {"context:bprks/css/train/select2.css","context:bprks/css/style_train.css",
		"context:bprks/css/train/ui-lightness/jquery-ui-1.10.4.custom.min.css"}, 
		library={
			"context:bprks/js/aero/jquery.js", "context:bprks/js/aero/jquery-ui.js",
			"context:bprks/js/aero/select2.min.js", 			
			"context:bprks/js/purchase/trainfLSScript.js"})
public class TrainPurchaseSelectTrain {
	
	@Persist
	@Property(write = false)
	private TrainPurchaseView view;
	
	public void setView(TrainPurchaseView view) {
		this.view = view;
	}
	
	@Property
	private String strSelectTrainClass;

	@Inject
	private TrainStationService trainStationService;
	
	@InjectComponent
	private Form form;
	
	void onValidateFromForm() {
		if (isBackButton) {
			form.clearErrors();
			return;
		}
		if (form.getHasErrors()) {
			return;
		}
		
		String[] arrTrain = strSelectTrainClass.split("#"); 
		String trainNo = arrTrain[0];
		String trainSubclass = arrTrain[1];
		view.setTrainNumber(trainNo);
		view.setSubclass(trainSubclass);
		//get trainName and seatClass
		for (TrainSchedule sch : apiGetSchedule.getTrainSchedule()) {
			if (trainNo.equals(sch.getTrainNo())) {
				view.setTrainName(sch.getTrainName());
				view.setDepartureDate(sch.getDepartureDate());
				view.setArrivalDate(sch.getArrivalDate());
				for (TrainSubclass sc : sch.getSubclassList()) {
					if (trainSubclass.equals(sc.getSubclass())) {
						view.setSeatClass(sc.getSeatClass());
						if (sc.getAvailable() < (view.getAdult() + view.getChild())) {
							form.recordError("Jumlah kursi tersedia tidak mencukupi");
							return;
						}
					}
				}
			}
		}
		
		try {
			inquiryFare(view);
		} catch (Exception e) {
			form.recordError(e.getMessage());
			e.printStackTrace();
			return;
		}
		
	}
	
	private void inquiryFare(TrainPurchaseView view2) {
		//get fare
		String jsonFare;
		if ("LOCAL".equals(TrainPurchaseInput.MODE)) {
			jsonFare = "{ \"err_code\": 0, \"org\":\"GMR\",\"des\":\"BD\",\"train_no\":\"1331\",\"dep_date\":\"20140529\",\"fare\":[[\"A\",\"E\",84000,79000,73000],[\"D\",\"E\",79000,73000,68000],[\"B\",\"B\",62000,28000,51000],[\"E\",\"B\",57000,41000,36000],[\"C\",\"K\",38500,33000,27500],[\"F\",\"K\",33000,27500,22000]] }";
		} else {
			DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
			Transaction t = new Transaction();
			t.setTransactionType(com.dwidasa.engine.Constants.KAI_GET_FARE);
			t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
			t.setFromAccountType("00");
			t.setToAccountType("00");
			t.setFromAccountNumber(view.getAccountNumber());
			t.setCardNumber(view.getCardNumber());
			t.setProviderCode("PAC");
			StringBuilder bit48 = new StringBuilder();
			bit48.append(view.getOriginCode()).append("#").append(view.getDestinationCode()).append("#").append(fmtYmd.format(view.getDepartureDate())).append("#").append(view.getTrainNumber());
			t.setFreeData1(bit48.toString());
			CommLink link = new MxCommLink(t);
	        link.sendMessage();
	        if (!"00".equals(t.getResponseCode()) && !"10".equals(t.getResponseCode())) {
	        	throw new BusinessException("IB-1009", t.getResponseCode());
	        }
	        System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
	        System.out.println("HOHO RESPON BIT 48:");
	        System.out.println(t.getFreeData1());
			jsonFare = t.getFreeData1();
		}
		
		Map<String, List<BigDecimal>> fareMap = PojoJsonMapper.trainFromJsonToFareMap(jsonFare);
		String key = view.getSeatClass() + "#" + view.getSubclass();
		List<BigDecimal> fareList = fareMap.get(key);
		view.setAdultFare(fareList.get(0));
		view.setChildFare(fareList.get(1));
		view.setInfantFare(fareList.get(2));
		view.setAmount(view.getAdultFare().multiply(new BigDecimal(view.getAdult())));
		view.setAmount(view.getAmount().add(view.getChildFare().multiply(new BigDecimal(view.getChild()))));
		view.setAmount(view.getAmount().add(view.getInfantFare().multiply(new BigDecimal(view.getInfant()))));
		
	}

	private boolean isBackButton;
	
	void onSelectedFromBack() {
		isBackButton = true;
	}
	
	@InjectPage
	private TrainPurchaseDetail trainPurchaseDetail;
	
	@InjectPage
	private TrainPurchaseInput trainPurchaseInput;
	
	@DiscardAfter
	Object onSuccessFromForm() {
		if (isBackButton) {
			trainPurchaseInput.setView(view);
			return trainPurchaseInput;
		}
		trainPurchaseDetail.setView(view);
		return trainPurchaseDetail;
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
		return sb.toString();
	}
	
	
	
}
