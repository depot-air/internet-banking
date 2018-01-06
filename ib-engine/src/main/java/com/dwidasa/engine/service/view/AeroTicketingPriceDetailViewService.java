package com.dwidasa.engine.service.view;

import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;

public interface AeroTicketingPriceDetailViewService {
	public AeroFlightView getPriceDetail(AeroFlightView view);
	public AeroTicketingView getVoltrasFare(AeroTicketingView view);
	public String getDepositInformation();
}
