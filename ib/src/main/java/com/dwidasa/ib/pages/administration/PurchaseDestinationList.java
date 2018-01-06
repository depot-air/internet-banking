package com.dwidasa.ib.pages.administration;

import java.util.ArrayList;
import java.util.List;

import com.dwidasa.engine.AirConstants;
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
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.ib.base.BaseCustomerRegisterList;
import com.dwidasa.ib.common.GeneralUtil;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;

public class PurchaseDestinationList extends BaseCustomerRegisterList {
	@Inject
	private SessionManager sessionManager;
	
	@InjectComponent
	private Form form;
	
	@Inject
    private ExtendedProperty extendedProperty;
	
	@Inject
	private ParameterDao parameterDao;
	
	
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
		sbWhere.append("'").append(Constants.PLN_PURCHASE_CODE).append("',");
		sbWhere.append("'").append(Constants.VOUCHER_PURCHASE_CODE).append("',");
		sbWhere.append("'").append(Constants.MNCLIFE.Mnc_Live_Posting_Pembelian).append("',");
        sbWhere.append("'").append(AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE).append("'");
		sbWhere.append(")");
		restrictions.add(sbWhere.toString());
    	return null;
    }

	void onActivate(Long locationId) {
		this.locationId = locationId;
	}
	
	
	@Inject
	private AlertManager alertManager;
	
	@Inject
	private Messages messages;
	
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
		
		if(isMerchant()){
			if (transactionType.equals(Constants.PLN_PURCHASE_CODE)) {
				location = "purchase/plnpurchaseinput";
			} else if (transactionType.equals(Constants.VOUCHER_PURCHASE_CODE)) {
				location = "purchase/voucherPurchaseInput";
			} else if(transactionType.equals(Constants.MNCLIFE.Mnc_Live_Posting_Pembelian)){
				location = "purchase/mncLifePurchaseInput";
			}
		}else{
			
			if (transactionType.equals(Constants.PLN_PURCHASE_CODE)) {
				location = "purchase/plnpurchaseinput";
			} else if (transactionType.equals(Constants.VOUCHER_PURCHASE_CODE)) {
				location = "purchase/voucherPurchaseInput";
			}
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
	
	
	public boolean isMerchant() {    	
		String firstFour = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
    	String[] tokens = ip.getParameterValue().split(",");
    	boolean isMerch = false;
    	if (tokens.length > 0 ) {
    		for(int i = 0; i < tokens.length; i++) {
    			if (firstFour.equals(tokens[i]))
    				isMerch = true;
    		}
    	}
    	return isMerch;
    }
	
}


