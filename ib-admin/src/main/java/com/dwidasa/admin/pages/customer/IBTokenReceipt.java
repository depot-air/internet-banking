package com.dwidasa.admin.pages.customer;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;

@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, Constants.RoleName.DAY_ADMIN, Constants.RoleName.SUPERUSER})
public class IBTokenReceipt {
	@Property(write = false)
    @Persist
    private Customer customer;
	
	@Property(write = false)
    @Persist
	private CustomerAccount customerAccount;

	@Property(write = false)
    @Persist
	private CustomerDevice customerDevice;
	
	@Persist
	private String serialNumber;
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setCustomerAccount(CustomerAccount customerAccount) {
		this.customerAccount = customerAccount;
	}

	public void setCustomerDevice(CustomerDevice customerDevice) {
		this.customerDevice = customerDevice;
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	
	public String getSerialNumber() {
		return serialNumber;
	}

	void onPrepare() {
		if (customerDevice == null) {
			customerDevice = new CustomerDevice();
			customerDevice.setTerminalId("");
		} else if (customerDevice.getTerminalId() == null) { 
			customerDevice.setTerminalId("");
		}
	}
}
