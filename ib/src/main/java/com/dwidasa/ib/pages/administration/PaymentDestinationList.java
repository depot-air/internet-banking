package com.dwidasa.ib.pages.administration;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.ib.base.BaseCustomerRegisterList;
import com.dwidasa.ib.common.GeneralUtil;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;

public class PaymentDestinationList extends BaseCustomerRegisterList {
	@Inject
	private SessionManager sessionManager;
	
	@InjectComponent
	private Form form;
	
	private boolean isDeleteButton;	
	
	void onSelectedFromDelete() {
		isDeleteButton = true;
	}
	
	private Long locationId;
	
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	Long onPassivate() {
		return locationId;
	}
	
	void onActivate(Long locationId) {
		this.locationId = locationId;
	}
	
	@Inject
	private AlertManager alertManager;
	
	@Inject
	private Messages messages;
	
	public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	
		List<String> orders = new ArrayList<String>();
		orders.add("data5");orders.add("biller_name");orders.add("data4");
		List<String> restrictions = new ArrayList<String>();
		setOrders(orders);
		setRestrictions(restrictions);
		
		StringBuilder sbWhere = new StringBuilder();
		sbWhere.append(" r.m_customer_id=").append(sessionManager.getLoggedCustomerView().getId()); 
		sbWhere.append(" and r.transaction_type in (");
		sbWhere.append("'").append(Constants.WATER.TRANSACTION_TYPE.POSTING).append("',");
		sbWhere.append("'").append(Constants.PLN_PAYMENT_CODE).append("',");
		sbWhere.append("'").append(Constants.TELCO_PAYMENT_CODE).append("',");
		sbWhere.append("'").append(Constants.MULTIFINANCE_PAYMENT_CODE).append("',");
		sbWhere.append("'").append(Constants.INSURANCE_PAYMENT_CODE).append("',");
		sbWhere.append("'").append(Constants.EDUCATION_PAYMENT_CODE).append("',");
		sbWhere.append("'").append(Constants.TRANSPORTATION_PAYMENT_CODE).append("',");
		sbWhere.append("'").append(Constants.ENTERTAINMENT_PAYMENT_CODE).append("',");
		sbWhere.append("'").append(Constants.INTERNET_PAYMENT_CODE).append("',");
		sbWhere.append("'").append(Constants.TELKOM_PAYMENT_CODE).append("'");
		sbWhere.append(")");
		restrictions.add(sbWhere.toString());
		return null;
	}

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
	
	@Inject
	private PageRenderLinkSource pageRenderLinkSource;
	
	Link onRedirect(Long recordId, String transactionType) {
		String location = null;
		if (transactionType.equals(Constants.PLN_PAYMENT_CODE)) {
			location = "payment/plnPaymentInput";
		} else if (transactionType.equals(Constants.TELKOM_PAYMENT_CODE)) {
			location = "payment/telkomPaymentInput";
		} else if (transactionType.equals(Constants.INTERNET_PAYMENT_CODE)) {
			location = "payment/internetPaymentInput";
		} else if (transactionType.equals(Constants.TELCO_PAYMENT_CODE)) {
			location = "payment/hpPaymentInput";
		} else if (transactionType.equals(Constants.ENTERTAINMENT_PAYMENT_CODE)) {
			location = "payment/tvPaymentInput";
		} else if (transactionType.equals(Constants.TRANSPORTATION_PAYMENT_CODE)) {
			location = "payment/trainPaymentInput";
		} 
		if (location != null) {
	        Link link = pageRenderLinkSource.createPageRenderLinkWithContext(location, recordId);
	        return link;
		} else {
			return null;
		}
    }
	
	@Inject
	private CustomerRegisterService customerRegisterService;
	
}

