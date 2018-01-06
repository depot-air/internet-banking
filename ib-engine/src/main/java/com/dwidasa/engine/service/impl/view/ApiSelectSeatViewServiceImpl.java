package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.train.ApiSelectSeat;
import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.train.TrainSchedule;
import com.dwidasa.engine.model.train.TrainSubclass;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.view.ApiSelectSeatViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;

@Service("apiSelectSeatServiceImpl")
public class ApiSelectSeatViewServiceImpl implements ApiSelectSeatViewService {

	private DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
	private static Logger logger = Logger
			.getLogger(ApiSelectSeatViewServiceImpl.class);

	private class ApiSelectSeatMessageCustomizer implements MessageCustomizer {

		@Override
		public void compose(BaseView view, Transaction transaction) {
			TrainPurchaseView tpv = (TrainPurchaseView) view;

			StringBuilder bit48 = new StringBuilder();
			bit48.append(tpv.getOriginCode()).append("#")
					.append(tpv.getDestinationCode()).append("#")
					.append(fmtYmd.format(tpv.getDepartureDate())).append("#")
					.append(tpv.getTrainNumber());
			transaction.setFreeData1(bit48.toString());

		}

		@Override
		public Boolean decompose(Object view, Transaction transaction) {
			TrainPurchaseView tpv = (TrainPurchaseView) view;

			return Boolean.TRUE;
		}

	}

	public TrainPurchaseView getInquiryFare(TrainPurchaseView view) {

		//get trainName and seatClass
//		for (TrainSchedule sch : apiGetSchedule.getTrainSchedule()) {
//			if (trainNo.equals(sch.getTrainNo())) {
//				view.setTrainName(sch.getTrainName());
//				view.setDepartureDate(sch.getDepartureDate());
//				view.setArrivalDate(sch.getArrivalDate());
//				for (TrainSubclass sc : sch.getSubclassList()) {
//					if (trainSubclass.equals(sc.getSubclass())) {
//						view.setSeatClass(sc.getSeatClass());
//						if (sc.getAvailable() < (view.getAdult() + view.getChild())) {
//							form.recordError("Jumlah kursi tersedia tidak mencukupi");
//							return;
//						}
//					}
//				}
//			}
//		}
		
		// get fare
		String jsonFare;

		DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
		Transaction t = new Transaction();
		view.setTransactionType(com.dwidasa.engine.Constants.KAI_GET_FARE);;
		t.setTransactionType(view.getTransactionType());
		t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
		t.setFromAccountType("00");
		t.setToAccountType("00");
		t.setFromAccountNumber(view.getAccountNumber());
		t.setCardNumber(view.getCardNumber());
		t.setProviderCode("PAC");

		MessageCustomizer customizer = new ApiSelectSeatMessageCustomizer();
		logger.info("view.getTransactionType()=" + view.getTransactionType());

		customizer.compose(view, t);
		CommLink link = new MxCommLink(t);
		link.sendMessage();
		if (!"00".equals(t.getResponseCode())
				&& !"10".equals(t.getResponseCode())) {
			throw new BusinessException("IB-1009", t.getResponseCode());
		} else {
			customizer.decompose(view, t);
		}
		System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
		System.out.println("HOHO RESPON BIT 48:");
		System.out.println(t.getFreeData1());
		jsonFare = t.getFreeData1();

		Map<String, List<BigDecimal>> fareMap = PojoJsonMapper.trainFromJsonToFareMap(jsonFare);
		System.out.println("FARE SIZE ==================" + fareMap.size());
		System.out.println("SUBCLASS ============= "+view.getSeatClass()+" "+view.getSubclass());
		String key = view.getSeatClass() + "#" + view.getSubclass();
		List<BigDecimal> fareList = fareMap.get(key);
		view.setAdultFare(fareList.get(0));
		view.setChildFare(fareList.get(1));
		view.setInfantFare(fareList.get(2));
		view.setAmount(view.getAdultFare().multiply(
				new BigDecimal(view.getAdult())));
		view.setAmount(view.getAmount().add(
				view.getChildFare().multiply(new BigDecimal(view.getChild()))));
		view.setAmount(view.getAmount()
				.add(view.getInfantFare().multiply(
						new BigDecimal(view.getInfant()))));

		return view;
	}

	public TrainPurchaseView changeSeat(TrainPurchaseView view, String wagonCode,
			String wagonNumber, String strSeat) {
		String json = "";

		Transaction t = new Transaction();
		t.setTransactionType(com.dwidasa.engine.Constants.KAI_MANUAL_SEAT);
		t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
		t.setFromAccountType("00");
		t.setToAccountType("00");
		t.setFromAccountNumber(view.getAccountNumber());
		t.setCardNumber(view.getCardNumber());
		t.setProviderCode("PAC");
		StringBuilder bit48 = new StringBuilder();
		bit48.append(view.getBookingCode()).append("#").append(wagonCode)
				.append("#").append(wagonNumber).append("#").append(strSeat);
		t.setFreeData1(bit48.toString());
		CommLink link = new MxCommLink(t);
		link.sendMessage();
		if (!"00".equals(t.getResponseCode())
				&& !"10".equals(t.getResponseCode())) {
			throw new BusinessException("IB-1009", t.getResponseCode());
		}
		System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
		System.out.println("HOHO RESPON BIT 48:");
		System.out.println(t.getFreeData1());
		json = t.getFreeData1();

		PojoJsonMapper.trainFromJsonGenericValidate(json);
		// kalau pindah kursi valid, maka update field seat di view
		view.setWagonCode(wagonCode);
		view.setWagonNumber(wagonNumber);
		String[] arrSeat = strSeat.split(",");
		int i = 0;
		for (TrainPassenger passenger : view.getPassengerList()) {
			if ("I".equals(passenger.getType()))
				continue; // infant ga punya kursi
			passenger.setWagonCode(wagonCode);
			passenger.setWagonNumber(wagonNumber);
			passenger.setSeatRow(arrSeat[i].substring(0,
					arrSeat[i].length() - 1));
			passenger.setSeatCol(arrSeat[i].substring(arrSeat[i].length() - 1));
			i++;
		}

		return view;
	}

	
}
