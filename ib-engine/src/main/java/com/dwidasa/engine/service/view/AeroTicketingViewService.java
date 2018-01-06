package com.dwidasa.engine.service.view;

import java.util.List;

import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;

public interface AeroTicketingViewService {
	public List<AeroFlightView> getSearch(AeroTicketingView view);
	public AeroFlightView getPriceDetail(AeroTicketingView view);
	public AeroTicketingView getIssue(AeroTicketingView view);
}
