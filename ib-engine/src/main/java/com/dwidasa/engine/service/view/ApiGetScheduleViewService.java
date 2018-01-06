package com.dwidasa.engine.service.view;

import com.dwidasa.engine.model.train.ApiGetSchedule;
import com.dwidasa.engine.model.view.TrainPurchaseView;

public interface ApiGetScheduleViewService {
	public ApiGetSchedule getTrainSchedule(TrainPurchaseView view);

}