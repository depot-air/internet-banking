package com.dwidasa.ib.pages.administration;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.ib.base.BaseCustomerRegisterList;
import com.dwidasa.ib.common.GeneralUtil;
import com.dwidasa.ib.services.SessionManager;

public class TransferDestinationList extends BaseCustomerRegisterList {
	@Property(write = false)
	private String transferType;
	
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	
	public void onActivate(String transferType) {
		this.transferType = transferType;
	}
	
	public String onPassivate() {
		return transferType;
	}
	
	@Inject
	private SessionManager sessionManager;
	
	@InjectComponent
	private Form form;
	
	@Inject
	private CustomerRegisterService customerRegisterService;
	
	private boolean isDeleteButton;
	
	void onSelectedFromDelete() {
		isDeleteButton = true;
	}
	
	Object onActivate() {
		List<String> orders = new ArrayList<String>();
		orders.add("data3");orders.add("data5");orders.add("biller_name");
		List<String> restrictions = new ArrayList<String>();
		setOrders(orders);
		setRestrictions(restrictions);
		if (com.dwidasa.ib.Constants.TRANSFER_TYPE.OVERBOOKING.equals(transferType)) { 
			restrictions.add(" r.m_customer_id=" + sessionManager.getLoggedCustomerView().getId() +" and (r.transaction_type='" + Constants.TRANSFER_CODE + "')");
			return null;
		} else if (com.dwidasa.ib.Constants.TRANSFER_TYPE.TREASURY.equals(transferType)) {
			restrictions.add(" r.m_customer_id=" + sessionManager.getLoggedCustomerView().getId() +" and (r.transaction_type='" + Constants.TRANSFER_TREASURY_CODE + "')");
			return null;
		} else if (com.dwidasa.ib.Constants.TRANSFER_TYPE.ALTO.equals(transferType)) {
			restrictions.add(" r.m_customer_id=" + sessionManager.getLoggedCustomerView().getId() +" and (r.transaction_type='" + Constants.ALTO.TT_POSTING + "')");
			return null;
		} else if (com.dwidasa.ib.Constants.TRANSFER_TYPE.ATMB.equals(transferType)) {
			restrictions.add(" r.m_customer_id=" + sessionManager.getLoggedCustomerView().getId() +" and (r.transaction_type='" + Constants.ATMB.TT_POSTING + "')");
			return null;
		}
	
		return TransferListOption.class;
	}
	
	@Inject
	private AlertManager alertManager;
	
	void onValidateFromForm() {
		if (isDeleteButton) {
			try {
				for (Long id : getSelectedSet()) {
					customerRegisterService.remove(id, sessionManager.getLoggedCustomerView().getId());
				}
		    	alertManager.alert(Duration.SINGLE, Severity.INFO, String.format(messages.get("deleteSuccess"), getSelectedSet().size()));
			} catch (Exception e) {
				e.printStackTrace();
				form.recordError(GeneralUtil.getRootCause(e));
			}
		}
	}
	
	Object onSuccessFromForm() {
		return null;
	}
	
	@Inject
	private Messages messages;
	
	public String getStrTitle() {
		if (com.dwidasa.ib.Constants.TRANSFER_TYPE.OVERBOOKING.equals(transferType)) { 
			return messages.get("titleOverbooking");
		} else if (com.dwidasa.ib.Constants.TRANSFER_TYPE.TREASURY.equals(transferType)) {
			return messages.get("titleTreasury");
		} else if (com.dwidasa.ib.Constants.TRANSFER_TYPE.ALTO.equals(transferType)) {
			return messages.get("titleAlto");
		} else if (com.dwidasa.ib.Constants.TRANSFER_TYPE.ATMB.equals(transferType)) {
			return messages.get("titleAtmb");
		}
		return "";
	}
	
	@Inject
	private Request request;
	
	public String getToken() {
		String token = (String) request.getSession(true).getAttribute("formtoken");
		return token;
	}
	
	public String getStrListEmpty() {
		if (com.dwidasa.ib.Constants.TRANSFER_TYPE.OVERBOOKING.equals(transferType)) { 
			return messages.get("listEmptyOverbooking");
		} else {
			return messages.get("listEmptyOtherBank");
		}
	}
	
}
