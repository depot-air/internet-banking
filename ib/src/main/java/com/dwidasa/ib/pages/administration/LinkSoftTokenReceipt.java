package com.dwidasa.ib.pages.administration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/8/11
 * Time: 00:21 am
 */
public class LinkSoftTokenReceipt {
	
    @Property
    private String tokenCode;
    @Inject
    private SessionManager sessionManager;
    @InjectComponent
    private Form form;
    @Inject
    private Messages messages;
    private ResultView resultView;
    
    @Property
    private String namaToken;
    
    @Property
    private String accountNumber;
    
    @Property
    private CustomerDevice customerDevice;
    
    @Property
    private String cardNumber;

    @Inject
    private CustomerDeviceService customerDeviceService;
    
    @Inject
    private CustomerDeviceDao deviceDao;

    @Inject
    private CustomerService customerService;
    
    @Property
    private List<CustomerDevice> deviceList;
    
    @Property
    private String customerReference2;
    
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;
    
    @Property
    private SelectModel customerDeviceSelect;
    
    @Persist
    private Boolean smsToken;
   
    @Persist
    private String namaPerangkat;
    
    @Persist
    private String deviceId;
    
    
   
    public void setNamaPerangkat(String namaPerangkat) {
		this.namaPerangkat = namaPerangkat;
	}
    
    
    public String getNamaPerangkat() {
		return namaPerangkat;
	}
    
    
    public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
    
    public String getDeviceId() {
		return deviceId;
	}
    
    
    public void setSmsToken(Boolean smsToken) {
		this.smsToken = smsToken;
	}
    
    public Boolean getSmsToken() {
		return smsToken;
	}
    
//    @DiscardAfter
//    Object onSuccess(){
//    	return LinkSoftTokenInput.class;
//    }

}
