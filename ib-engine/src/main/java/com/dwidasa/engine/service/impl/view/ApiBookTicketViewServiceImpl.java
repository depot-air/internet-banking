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
import com.dwidasa.engine.model.train.ApiBookTicket;
import com.dwidasa.engine.model.train.ApiSelectSeat;
import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.view.ApiBookTicketViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;

@Service("apiBookTicketServiceImpl")
public class ApiBookTicketViewServiceImpl implements ApiBookTicketViewService {

	private DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
	private static Logger logger = Logger
			.getLogger(ApiBookTicketViewServiceImpl.class);

	private class ApiBookTicketMessageCustomizer implements MessageCustomizer {

		@Override
		public void compose(BaseView view, Transaction transaction) {
			TrainPurchaseView tpv = (TrainPurchaseView) view;

			StringBuilder bit48 = new StringBuilder();
			tpv.setContactPhone(tpv.getPassengerList().get(0).getPhone());
			bit48.append(tpv.getOriginCode()).append("#")
					.append(tpv.getDestinationCode()).append("#")
					.append(fmtYmd.format(tpv.getDepartureDate())).append("#")
					.append(tpv.getTrainNumber()).append("#");
			bit48.append(tpv.getAdult()).append("#").append(tpv.getChild())
					.append("#").append(tpv.getInfant()).append("#");
			bit48.append(tpv.getSubclass()).append("#")
					.append(tpv.getContactPhone()).append("#");
			for (TrainPassenger passenger : tpv.getPassengerList()) {
				if ("A".equals(passenger.getType())) {
					bit48.append(passenger.getStrName()).append("#");
					bit48.append(passenger.getPhone()).append("#");
					bit48.append(passenger.getIdNumber()).append("#");
					bit48.append(passenger.getMember()).append("#");
				} else {
					bit48.append(passenger.getStrName()).append("#");
				}
			}
			bit48.delete(bit48.length() - 1, bit48.length()); // hapus # di
																// paling
																// belakang
			transaction.setFreeData1(bit48.toString());
		}

		@Override
		public Boolean decompose(Object view, Transaction transaction) {
			TrainPurchaseView tpv = (TrainPurchaseView) view;

			return Boolean.TRUE;
		}

	}

	public TrainPurchaseView getBookTicket(TrainPurchaseView view) {
		String json = "";

		DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
		Transaction t = new Transaction();
		t.setTransactionType(com.dwidasa.engine.Constants.KAI_BOOKING_WITH_ARV_INFO);
		t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
		t.setFromAccountType("00");
		t.setToAccountType("00");
		t.setFromAccountNumber(view.getAccountNumber());
		t.setCardNumber(view.getCardNumber());
		t.setProviderCode("PAC");

		MessageCustomizer customizer = new ApiBookTicketMessageCustomizer();
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
		json = t.getFreeData1();
		BigDecimal fee = t.getFee();
		if (fee == null) {
			fee = BigDecimal.ZERO;
		}
		view.setFee(fee);

		PojoJsonMapper.trainFromJsonBookingToView(json, view);

		return view;
	}

}
