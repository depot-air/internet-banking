package com.dwidasa.engine.service.view;

import com.dwidasa.engine.model.train.TrainChooseSeat;
import com.dwidasa.engine.model.view.TrainPurchaseView;

public interface ApiChooseSeatViewService {

	public TrainChooseSeat inquirySeat(TrainPurchaseView view);

	void cancelBook(TrainPurchaseView view);

}
