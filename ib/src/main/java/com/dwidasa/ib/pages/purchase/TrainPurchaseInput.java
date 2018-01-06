package com.dwidasa.ib.pages.purchase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionGroupModelImpl;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.joda.time.DateTime;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.TrainStation;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.train.ApiGetSchedule;
import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.train.TrainSchedule;
import com.dwidasa.engine.model.train.TrainSubclass;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TrainStationService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.ib.services.SessionManager;

@Import(stylesheet = {"context:bprks/css/train/select2.css","context:bprks/css/style_train.css",
		"context:bprks/css/train/ui-lightness/jquery-ui-1.10.4.custom.min.css"}, 
		library={
			"context:bprks/js/aero/jquery.js", "context:bprks/js/aero/jquery-ui.js",
			"context:bprks/js/aero/select2.min.js", 			
			"context:bprks/js/purchase/trainfLSScript.js"})
public class TrainPurchaseInput {
	public final static String MODE="IGATE";
//	public final static String MODE="LOCAL";
	
	private static Logger logger = Logger.getLogger( TrainPurchaseInput.class );
	
	@Persist
	@Property(write = false)
	private TrainPurchaseView view;
	
	@Property
	@Persist
	private String strDate;
	
	@Property
	@Persist
	private String origin;
	
	@Property
	@Persist
	private String destination;
	
	private SelectModel originModel;
	
	@Inject
	private TrainStationService trainStationService;
	
	@Inject
	private CacheManager cacheManager;
	
	@Inject
	private SessionManager sessionManager;
	
	void setupRender() {
		if (view == null) {
			view = new TrainPurchaseView();
			view.setAdult(1);
			view.setCustomerId(sessionManager.getLoggedCustomerView().getId());
		    view.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
		    view.setMerchantType(sessionManager.getDefaultMerchantType());
		    view.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
		    DateFormat dmy = new SimpleDateFormat("dd-MM-yyyy");
		    strDate = dmy.format(new DateTime().withTime(0, 0, 0, 0).plusDays(1).toDate());
		} else {
			origin = view.getOriginCode() + "#" + view.getOriginName();
			destination = view.getDestinationCode() + "#" + view.getDestinationName();
			if (view.getDepartureDate() != null) {
				DateFormat dmy = new SimpleDateFormat("dd-MM-yyyy"); 
				strDate = dmy.format(view.getDepartureDate());
			}
		}
	}
	
	public void setView(TrainPurchaseView view) {
		this.view = view;
	}



	public SelectModel getOriginModel() {
		if (originModel == null) {
			List<OptionGroupModel> optionGroupList = new ArrayList<OptionGroupModel>();
			List<OptionModel> optionList = new ArrayList<OptionModel>();
//			List<TrainStation> trainStationList = trainStationService.getAll();
			List<TrainStation> trainStationList = cacheManager.getTrainStation();
			String cityName = "";
			OptionGroupModel optionGroup;
			for (TrainStation row : trainStationList) {
				if (!cityName.equals(row.getCityName())) {
					cityName = row.getCityName();
					optionList = new ArrayList<OptionModel>();
					optionGroup = new OptionGroupModelImpl(cityName, false, optionList);
					optionGroupList.add(optionGroup);
				}
				optionList.add(new OptionModelImpl(row.getStationName(), row.getStationCode() + "#" + row.getStationName()));
			}
			originModel = new SelectModelImpl(optionGroupList, null);
		}
		return originModel;
	}
	
	@InjectComponent
	private Form form;
	
	@Inject
	private PurchaseService purchaseService;
	
	@Inject
	private Messages messages;
	
	void onValidateFromForm() {
		view.setCardNumber(sessionManager.getCardNumber(view.getAccountNumber()));
		
		
		if (form.getHasErrors()) {
			return;
		}
		
		if (origin == null) {
			form.recordError(messages.get("departInput-required-message"));
		}
		if (destination == null) {
			form.recordError(messages.get("arriveInput-required-message"));
		}
		if (strDate == null) {
			form.recordError(messages.get("departDate-required-message"));
		}
		
		if (form.getHasErrors()) {
			return;
		}
		
		
		if (view.getInfant() > view.getAdult() + view.getChild()) {
			form.recordError("Jumlah Penumpang Infant Tidak Boleh Lebih dari Penumpang Dewasa");
			return;
		}
		if (view.getAdult() + view.getChild() > 4) {
			form.recordError("Jumlah Penumpang Dewasa Maksimal 4 Kursi");
			return;
		}
		
		String[] arrOrigin = origin.split("#");
		String[] arrDestination = destination.split("#");
		view.setOriginCode(arrOrigin[0]);
		view.setOriginName(arrOrigin[1]);
		view.setDestinationCode(arrDestination[0]);
		view.setDestinationName(arrDestination[1]);
		
		if (view.getOriginCode().equals(view.getDestinationCode())) {
			form.recordError("Stasiun asal dan stasiun tujuan tidak boleh sama");
			return;
		}
		
		DateFormat dmy = new SimpleDateFormat("dd-MM-yyyy");
		dmy.setLenient(false);
		try {
			view.setDepartureDate(dmy.parse(strDate));
		} catch (ParseException e) {
			form.recordError("Tanggal keberangkatan Tidak Valid: " + strDate);
			return;
		}
		DateTime jodaDeparture = new DateTime(view.getDepartureDate()).withTime(0, 0, 0, 0);
		DateTime jodaToday = new DateTime().withTime(0,0,0,0);
		DateTime jodaDateLimit = jodaToday.plusDays(90);
		if (jodaDeparture.isBefore(jodaToday)) {
			form.recordError("Tanggal keberangkatan tidak valid");
			return;
		}
		if (jodaDeparture.isAfter(jodaDateLimit)) {
			form.recordError("Maksimum tanggal keberangkatan 90 hari: " + dmy.format(jodaDateLimit.toDate()));
			return;
		}
		
		try {
			this.apiGetSchedule = inquiryTrain(view);
			if (apiGetSchedule.getTrainSchedule().size() == 0) {
//				form.recordError("Tiket tanggal " + dmy.format(jodaDeparture.toDate()) + " dengan waktu keberangkatan 8 jam kedepan tidak dapat dipesan");
				form.recordError("Route tidak tersedia");
				return;
			}
			DateTime now = new DateTime();
			boolean adaKereta = false;
			boolean adaKeretaBawah8Jam = false;
			for (TrainSchedule sch : apiGetSchedule.getTrainSchedule()) {
	        	DateTime jodaSchDeparture = new DateTime(sch.getDepartureDate());
	        	if (now.plusHours(8).isAfter(jodaSchDeparture)) {
	        		//kereta dengan jam keberangkatan < 8 jam tidak usah ditampilkan
	        		continue;
	        	}
				
				if (sch.getSubclassList() != null && sch.getSubclassList().size() > 0) {
					for (TrainSubclass sub : sch.getSubclassList()) {
						if (sub.getAvailable() > 0) {
							adaKereta = true;
							break;
						}
					}
					if (adaKereta) {
						break;
					}
				}
			}
			if (!adaKereta) {
				if (adaKeretaBawah8Jam) {
					form.recordError("Tiket tanggal " + dmy.format(jodaDeparture.toDate()) + " dengan waktu keberangkatan 8 jam kedepan tidak dapat dipesan");
					return;
				} else {
					form.recordError("Kursi tidak mencukupi atau kursi tidak tersedia");
					return;
				}
			}
		} catch (BusinessException e) {
			form.recordError(e.getFullMessage());
            e.printStackTrace();
            return;
		} catch (Exception e) {
			form.recordError(e.getMessage());
			e.printStackTrace();
			return;
		}
		
		//siapkan train passenger sesuai jumlah penumpang
		List<TrainPassenger> passengerList = new ArrayList<TrainPassenger>();
		if (view.getAdult() > 0) {
			for (int i=0; i < view.getAdult(); i++) {
				TrainPassenger passenger = new TrainPassenger();
				passenger.setType("A");//Adult
				passenger.setLabel("Dewasa " + (i+1));
				passenger.setMember("N");
				passengerList.add(passenger);
			}
		}
		if (view.getChild() > 0) {
			for (int i=0; i < view.getChild(); i++) {
				TrainPassenger passenger = new TrainPassenger();
				passenger.setType("C");//Child
				passenger.setLabel("Anak " + (i+1));
				passengerList.add(passenger);
			}
		}
		if (view.getInfant() > 0) {
			for (int i=0; i < view.getInfant(); i++) {
				TrainPassenger passenger = new TrainPassenger();
				passenger.setType("I");//Infant
				passenger.setLabel("Infant " + (i+1));
				passengerList.add(passenger);
			}
		}
		view.setPassengerList(passengerList);
	}
	
	private ApiGetSchedule inquiryTrain(TrainPurchaseView view) {
		ApiGetSchedule schedule; 
		String[] arrJson;
		if ("LOCAL".equals(MODE)) {
			String jsonResult = "{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"dep_date\": \"20150401\", \"schedule\": [[\"1331\",\"KERETA MURAH\",\"20150401\",\"20150401\",\"0700\",\"1000\",[[\"A\",\"E\",84000],[\"B\",\"B\",62000],[\"C\",\"K\",38500],[\"D\",\"E\",78500],[\"E\",\"B\",56500],[\"F\",\"K\",33000]]],[\"3310\",\"ARGO PARAHYANGAN\",\"20150401\",\"20150401\",\"0830\",\"1030\",[[\"A\",\"E\",84000]]],[\"3\",\"KTX\",\"20150401\",\"20150401\",\"1300\",\"1500\",[[\"A\",\"E\",200000],[\"B\",\"B\",200000],[\"C\",\"K\",200000],[\"D\",\"E\",200000],[\"E\",\"E\",200000],[\"F\",\"E\",200000]]]] }";
			schedule = PojoJsonMapper.trainFromJsonToApiGetSchedule(jsonResult);
			String[] arrJsonLocal = 
				{"{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"dep_date\": \"20150401\", \"train_no\":\"1331\",\"avb\": [[\"A\",80,\"E\"],[\"B\",80,\"B\"],[\"C\",79,\"K\"],[\"D\",0,\"E\"],[\"E\",0,\"B\"],[\"F\",0,\"K\"]] }"
					,"{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"dep_date\": \"20150401\", \"train_no\":\"3310\",\"avb\": [[\"A\",30,\"E\"]] }"
					,"{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"dep_date\": \"20150401\", \"train_no\":\"1030\",\"avb\": [] }"};
			arrJson = arrJsonLocal;
		} else {
			DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
			Transaction t = new Transaction();
			t.setTransactionType(Constants.KAI_GET_SCHEDULE_ONLY);
			t.setMerchantType(Constants.MERCHANT_TYPE.IB);
			t.setFromAccountType("00");
			t.setToAccountType("00");
			t.setFromAccountNumber(view.getAccountNumber());
			t.setCardNumber(view.getCardNumber());
			t.setProviderCode("PAC");
			StringBuilder bit48 = new StringBuilder();
			bit48.append(view.getOriginCode()).append("#").append(view.getDestinationCode()).append("#").append(fmtYmd.format(view.getDepartureDate()));
			t.setFreeData1(bit48.toString());
			CommLink link = new MxCommLink(t);
	        link.sendMessage();	
	        if (!"00".equals(t.getResponseCode()) && !"10".equals(t.getResponseCode())) {
	        	throw new BusinessException("IB-1009", t.getResponseCode());
	        }
	        System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
	        System.out.println("HOHO RESPON BIT 48:");
	        System.out.println(t.getFreeData1());
			schedule = PojoJsonMapper.trainFromJsonToApiGetSchedule(t.getFreeData1());
			List<String> jsonList = new ArrayList<String>();
			for (TrainSchedule sch : schedule.getTrainSchedule()) {
				t.setTransactionType(Constants.KAI_GET_AVB_ONLY);
				t.setReferenceNumber(null);
				bit48.delete(0, bit48.length());
				bit48.append(view.getOriginCode()).append("#").append(view.getDestinationCode()).append("#")
					 .append(fmtYmd.format(view.getDepartureDate())).append("#").append(sch.getTrainNo());
				t.setFreeData1(bit48.toString());
				link = new MxCommLink(t);
		        link.sendMessage();
		        if (!"00".equals(t.getResponseCode()) && !"10".equals(t.getResponseCode())) {
		        	throw new BusinessException("IB-1009", t.getResponseCode());
		        }
		        System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
		        System.out.println("HOHO RESPON BIT 48:");
		        System.out.println(t.getFreeData1());
		        jsonList.add(t.getFreeData1());
			}
			arrJson = new String[jsonList.size()];
			arrJson = jsonList.toArray(arrJson);
		}
		
		Map<String, Integer> availableSeat = PojoJsonMapper.trainFromJsonToAvailableSeatMap(arrJson);
		for (TrainSchedule sch: schedule.getTrainSchedule()) {
			if (sch.getSubclassList() == null) continue;
			for (TrainSubclass sc : sch.getSubclassList()) {
				String key = sch.getTrainNo() + "#" + sc.getSeatClass() + "#" + sc.getSubclass();
				Integer seat = availableSeat.get(key);
				if (seat != null) {
					sc.setAvailable(seat);
				}
			}
		}
		return schedule;
	
		
//		String jsonResult = "{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"dep_date\": \"20150401\", \"schedule\": [[\"1331\",\"KERETA MURAH\",\"20150401\",\"20150401\",\"0700\",\"1000\",[[\"A\",\"E\",84000],[\"B\",\"B\",62000],[\"C\",\"K\",38500],[\"D\",\"E\",78500],[\"E\",\"B\",56500],[\"F\",\"K\",33000]]],[\"3310\",\"ARGO PARAHYANGAN\",\"20150401\",\"20150401\",\"0830\",\"1030\",[[\"A\",\"E\",84000]]],[\"3\",\"KTX\",\"20150401\",\"20150401\",\"1300\",\"1500\",[[\"A\",\"E\",200000],[\"B\",\"B\",200000],[\"C\",\"K\",200000],[\"D\",\"E\",200000],[\"E\",\"E\",200000],[\"F\",\"E\",200000]]]] }";
//		ApiGetSchedule schedule = PojoJsonMapper.trainFromJsonToApiGetSchedule(jsonResult);
		
//		t.setTransactionType(Constants.KAI_GET_AVB_ONLY);
//		t.setReferenceNumber(null);
//		bit48.delete(0, bit48.length());
//		bit48.append(view.getOriginCode()).append("#").append(view.getDestinationCode()).append("#").append(fmtYmd.format(view.getDepartureDate())).append("#").append("1331");
//		t.setFreeData1(bit48.toString());
//		CommLink link = new MxCommLink(t);
//        link.sendMessage();
//        
//		String[] arrJson = 
//		{t.getFreeData1()
//		,"{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"dep_date\": \"20140529\", \"train_no\":\"3310\",\"avb\": [[\"A\",30,\"E\"]] }"
//		,"{ \"err_code\": 0, \"org\": \"GMR\", \"des\": \"BD\", \"dep_date\": \"20140529\", \"train_no\":\"1030\",\"avb\": [] }"};
//		System.out.println("HOHO RESPON BIT 48: ");
//        System.out.println(t.getFreeData1());
		
		
		
	}

	@InjectPage
	private TrainPurchaseSelectTrain trainPurchaseSelectTrain;
	
	private ApiGetSchedule apiGetSchedule;
	
	@DiscardAfter
	Object onSuccessFromForm() {
		trainPurchaseSelectTrain.setView(view);
		trainPurchaseSelectTrain.setApiGetSchedule(apiGetSchedule);
		return trainPurchaseSelectTrain;
	}
}

