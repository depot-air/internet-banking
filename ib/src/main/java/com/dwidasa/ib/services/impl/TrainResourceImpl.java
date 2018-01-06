package com.dwidasa.ib.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.joda.time.DateTime;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.mapper.TrainStationMapper;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.TrainStation;
import com.dwidasa.engine.model.train.ApiBookTicket;
import com.dwidasa.engine.model.train.ApiSelectSeat;
import com.dwidasa.engine.model.train.ApiGetSchedule;
import com.dwidasa.engine.model.train.TrainChooseSeat;
import com.dwidasa.engine.model.train.TrainSchedule;
import com.dwidasa.engine.model.train.TrainSubclass;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.TrainStationService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.view.ApiBookTicketViewService;
import com.dwidasa.engine.service.view.ApiChooseSeatViewService;
import com.dwidasa.engine.service.view.ApiSelectSeatViewService;
import com.dwidasa.engine.service.view.ApiGetScheduleViewService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.TrainResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@PublicPage
public class TrainResourceImpl implements TrainResource {
	private static Logger logger = Logger.getLogger(TrainResourceImpl.class);

	@Inject
	public TrainStationService trainstationService;

	@Inject
	public ApiGetScheduleViewService apiGetScheduleViewService;

	@Inject
	public ApiSelectSeatViewService apiSelectSeatViewService;

	@Inject
	public ApiBookTicketViewService apiBookTicketViewService;
	
	@Inject
	public ApiChooseSeatViewService apiChooseSeatViewService;
	
	@Inject
    private PurchaseService purchaseService;
	
	@InjectComponent
	private Form form;

	Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss")
			.serializeNulls().create();

	public TrainResourceImpl() {

	}

	@Override
	public String getStations() {
		List<TrainStation> stations = trainstationService.getAll();
		return PojoJsonMapper.toJson(stations);
	}

	@Override
	public String inquiryTrain(String json) {
		TrainPurchaseView view = gson.fromJson(json, TrainPurchaseView.class);

		SimpleDateFormat dmy = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
		dmy.setLenient(false);

		ApiGetSchedule schedule = apiGetScheduleViewService
				.getTrainSchedule(view);
		
		DateTime jodaDeparture = new DateTime(view.getDepartureDate()).withTime(0, 0, 0, 0);
		DateTime jodaToday = new DateTime().withTime(0,0,0,0);
		DateTime jodaDateLimit = jodaToday.plusDays(90);
		
		DateTime now = new DateTime();
		boolean adaKereta = false;
		boolean adaKeretaBawah8Jam = false;
		for (TrainSchedule sch : schedule.getTrainSchedule()) {
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
			} else {
				form.recordError("Kursi tidak mencukupi atau kursi tidak tersedia");
			}
		}
		
		return PojoJsonMapper.toJson(schedule);
	}

	public String inquiryFare(String json) {
		TrainPurchaseView view = gson.fromJson(json, TrainPurchaseView.class);
		view = apiSelectSeatViewService.getInquiryFare(view);
		return gson.toJson(view);
	}

	public String bookTicket(String json) {
		TrainPurchaseView view = gson.fromJson(json, TrainPurchaseView.class);
		view = apiBookTicketViewService.getBookTicket(view);
		return gson.toJson(view);
	}
	
	public String changeSeat(String json, String wagonCode, String wagonNumber, String strSeat){
		TrainPurchaseView view = gson.fromJson(json, TrainPurchaseView.class);
		view = apiSelectSeatViewService.changeSeat(view, wagonCode, wagonNumber, strSeat);
		return gson.toJson(view);
		
	}
	
	@Override
	public String inquirySeat(String json) {
		TrainPurchaseView view = gson.fromJson(json, TrainPurchaseView.class);

		SimpleDateFormat dmy = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
		dmy.setLenient(false);

		TrainChooseSeat schedule = apiChooseSeatViewService.inquirySeat(view);
		return PojoJsonMapper.toJson(schedule);
	}
	
	private String saveKAIExtract(String json, Long customerId) {
        TrainPurchaseView view = gson.fromJson(json, TrainPurchaseView.class);
        view.setTransactionDate(new Date());
        view.setTransactionType(Constants.KAI_PAYMENT);
        view.setCustomerId(customerId);
        purchaseService.execute(view);

        return gson.toJson(view);
    }
	
	public String saveKAI(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveKAIExtract(json, customerId);
    }
	
	public String saveKAI2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveKAIExtract(json, customerId);
    }
	
	public void cancelBook(String json){
		TrainPurchaseView view = gson.fromJson(json, TrainPurchaseView.class);
		apiChooseSeatViewService.cancelBook(view);
		
	}

}