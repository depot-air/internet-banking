package com.dwidasa.engine.service.view;

import com.dwidasa.engine.model.view.AeroTicketingView;

public interface AeroTicketingSearchBookingViewService {
	public AeroTicketingView getSearchBooking(AeroTicketingView view);
	public AeroTicketingView getVoltrasRetrieve(AeroTicketingView view);
	public AeroTicketingView getVoltrasTicketing(AeroTicketingView view);
	public AeroTicketingView getVoltrasCancel(AeroTicketingView view);
	public String sendEmail(AeroTicketingView view);
	public AeroTicketingView getAeroIssue(AeroTicketingView view);
	public boolean savingCustRegIReport(AeroTicketingView view);
}
