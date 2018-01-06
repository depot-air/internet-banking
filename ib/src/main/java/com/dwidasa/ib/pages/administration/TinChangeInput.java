package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.dao.CustomerDao;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;


/**
 * Created by IntelliJ IDEA.
 * User: wks-001
 * Date: 8/3/11
 * Time: 10:17 PM
 */

public class TinChangeInput {
	@Property
	private CustomerView customerView;

	@Property
	private ResultView resultView;

	@Inject
	private SessionManager sessionManager;

	@Inject
	private WebAdministrationService administrationService;

	@Property
	private String oldTinValue;

	@Property
	private String confirmTinValue;

	@InjectPage
	private TinChangeReceipt tinChangeReceipt;

	@InjectComponent
	private Form form;
	
	@Inject
	private CustomerService customerService;

    @Inject
    private CustomerDao customerDao;

	@Inject
	private Messages message;

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

	void onPrepare() {
		if (customerView == null) {
			customerView = new CustomerView();
		}
	}

	void setupRender() {
		if (customerView == null) {
			customerView = new CustomerView();
		}
	}

	void onSelectedFromNext() {
		if (oldTinValue == null) {
			form.recordError(message.get("errorTinChangeNull"));
		} else if (!oldTinValue.matches("[0-9]+")) {
			form.recordError(message.get("formatTinError"));
		} else if (oldTinValue.length() != 6) {
			form.recordError(message.get("lengthTinError"));
		}

		if (customerView.getPin() == null) {
			form.recordError(message.get("errorNewTinNull"));
		} else if (!customerView.getPin().matches("[0-9]+")) {
			form.recordError(message.get("formatNewTinError"));
		} else if (customerView.getPin().length() != 6) {
			form.recordError(message.get("lengthNewTinError"));
		} else if (customerView.getPin().equals(oldTinValue)) {
			form.recordError(message.get("mustDifferent"));
		} else if (!customerView.getPin().equalsIgnoreCase(confirmTinValue)) {
			form.recordError(message.get("errorTinChange"));
		}

		if (confirmTinValue == null) {
			form.recordError(message.get("errorConfTinNull"));
		} else if (!confirmTinValue.matches("[0-9]+")) {
			form.recordError(message.get("formatConfTinError"));
		} else if (confirmTinValue.length() != 6) {
			form.recordError(message.get("lengthConfTinError"));
		}
		
		Customer customer = customerService.get(sessionManager.getLoggedCustomerView().getId());
        if (customer.getEncryptedCustomerPin().length() == 16) {
	        if (!customerDao.authenticate(customer.getCustomerUsername(), oldTinValue)) {
	            form.recordError(message.get("errorInvalidPin"));
	        }
        } else  {
        	if (!customerDao.authenticateSHA(customer.getCustomerUsername(), oldTinValue)) {
	            form.recordError(message.get("errorInvalidPin"));
	        }
        }

		if (!form.getHasErrors()) {
			try {
				customerView.setId(sessionManager.getLoggedCustomerView().getId());
				resultView = administrationService.changeCustomerInfo(
						customerView, oldTinValue, 3);
			} catch (BusinessException e) {
				form.recordError(e.getFullMessage());
				e.printStackTrace();
			}
		}

	}

	@DiscardAfter
	Object onSuccessFromForm() {
		tinChangeReceipt.setResultView(resultView);
		return tinChangeReceipt;
	}
}