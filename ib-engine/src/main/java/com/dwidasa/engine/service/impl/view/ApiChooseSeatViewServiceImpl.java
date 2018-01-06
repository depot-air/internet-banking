package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.train.ApiBookTicket;
import com.dwidasa.engine.model.train.ApiChooseSeat;
import com.dwidasa.engine.model.train.TrainChooseSeat;
import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.view.ApiChooseSeatViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;

@Service("apiChooseSeatViewServiceImpl")
public class ApiChooseSeatViewServiceImpl implements ApiChooseSeatViewService {

	private DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
	private static Logger logger = Logger
			.getLogger(ApiChooseSeatViewServiceImpl.class);

	private class ApiChooseSeatMessageCustomizer implements MessageCustomizer {

		@Override
		public void compose(BaseView view, Transaction transaction) {
			TrainPurchaseView tpv = (TrainPurchaseView) view;

			StringBuilder bit48 = new StringBuilder();
			bit48.append(tpv.getOriginCode()).append("#")
					.append(tpv.getDestinationCode()).append("#")
					.append(tpv.getTrainNumber()).append("#");
			bit48.append(fmtYmd.format(tpv.getDepartureDate())).append("#")
					.append(tpv.getSubclass());
			transaction.setFreeData1(bit48.toString());
		}

		@Override
		public Boolean decompose(Object view, Transaction transaction) {
			TrainPurchaseView tpv = (TrainPurchaseView) view;

			return Boolean.TRUE;
		}

	}

	public TrainChooseSeat inquirySeat(TrainPurchaseView view) {
		String json = "";

		DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
		Transaction t = new Transaction();
		t.setTransactionType(com.dwidasa.engine.Constants.KAI_GET_SEAT_NULL_PER_SUBCLASS);
		t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
		t.setFromAccountType("00");
		t.setToAccountType("00");
		t.setFromAccountNumber(view.getAccountNumber());
		t.setCardNumber(view.getCardNumber());
		t.setProviderCode("PAC");

		MessageCustomizer customizer = new ApiChooseSeatMessageCustomizer();
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
		json = t.getFreeData1();

		TrainChooseSeat trainChooseSeat = PojoJsonMapper
				.trainFromJsonToTrainChooseSeat(json, view);
		return trainChooseSeat;

	}

	@Override
	public void cancelBook(TrainPurchaseView view) {
		String json = "";
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
		if (!"00".equals(t.getResponseCode())
				&& !"10".equals(t.getResponseCode())) {
			throw new BusinessException("IB-1009", t.getResponseCode());
		}
		System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
		System.out.println("HOHO RESPON BIT 48:");
		System.out.println(t.getFreeData1());
		json = t.getFreeData1();

		PojoJsonMapper.trainFromJsonGenericValidate(json);

	}

}
