package com.dwidasa.admin.pages.customer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.engine.service.CustomerService;

@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.DAY_ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class CustomerDetail {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private CustomerDevice row;

    @Persist
    @Property(write = false)
    private Long id;

    @Property
    private Customer customer;

    @Inject
    private CustomerService customerService;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Inject
    private CustomerDeviceService customerDeviceService;

    void onActivate(Long id) {
        this.id = id;
    }
    
    Object onActivate() {
    	if (id == null) {
    		return CustomerList.class;
    	}
    	customer = customerService.get(id);
    	if (customer == null) {
    		return CustomerList.class;
    	}
    	return null;
    }
    
    public Long onPassivate() {
    	return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;
        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        if (id != null && !id.equals("")) {
            restrictions.add("m_customer_id =?");
            values.add(id);
        }
        dataSource = new BaseDataSource(CustomerDevice.class, Constants.PAGE_SIZE, restrictions, values);
    }
    
    private boolean isBackButton;

    void  onSelectedFromBack() {
    	isBackButton = true;
    }
    
    @DiscardAfter
    Object onSuccess() {
    	if (isBackButton) {
    		return CustomerList.class;
    	}
    	return null;
    }
    
    @Inject
    private SessionManager sessionManager;
    
    void onActionFromDelete(Long id) {
    	Long userId = sessionManager.getLoggedUser().getId();
    	customerDeviceService.remove(id, userId);
    }

    public String getStrPlatform(String deviceId) {
    	if (deviceId == null) return "";
    	if (deviceId.length() == 17 &&  deviceId.endsWith(com.dwidasa.engine.Constants.BLACKBERRY)) return "BlackBerry";
    	if (deviceId.length() == 17 && deviceId.endsWith(com.dwidasa.engine.Constants.ANDROID)) return "Android";
    	if (deviceId.length() == 17 && deviceId.endsWith(com.dwidasa.engine.Constants.IPHONE)) return "iPhone";
    	if (deviceId.length() == 17 && deviceId.endsWith(com.dwidasa.engine.Constants.BB10)) return "BB10";
    	return "Soft Token";
    }
    
    public String getStrStatus(int status) {
    	if (status == 1) {
    		return messages.get("aktif");	
    	} else {
    		return messages.get("tidakAktif");
    	}
    }
    
    public String getStrDeviceStatus(int status) {
    	if (status == 1) {
    		return messages.get("aktif");	
    	} else {
    		return messages.get("belumAktif");
    	}
    }
    
    void onBlock() {
    	customerService.updateStatus(id, 0);
    }
    
    void onUnblock() {
    	customerService.updateStatus(id, 1);
    }
    
    public boolean isCustomerActive() {
    	if (customer == null || customer.getStatus() == null) return false;
    	return customer.getStatus().intValue() == 1;
    }
}
