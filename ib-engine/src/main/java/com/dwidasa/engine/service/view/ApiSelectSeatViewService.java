package com.dwidasa.engine.service.view;

import com.dwidasa.engine.model.train.ApiSelectSeat;
import com.dwidasa.engine.model.view.TrainPurchaseView;

public interface ApiSelectSeatViewService {

	public TrainPurchaseView getInquiryFare(TrainPurchaseView view);
	
	public TrainPurchaseView changeSeat(TrainPurchaseView view, String wagonCode, String wagonNumber, String strSeat);
	
}
