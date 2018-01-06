package com.dwidasa.ib.pages.administration;

import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
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
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;
/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/8/11
 * Time: 00:21 am
 */
public class LinkSoftTokenInput {
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
    
    @Property
    private Boolean smsToken;
    
    @InjectPage
    private LinkSoftTokenConfirm linkSoftTokenConfirm;
   
    void onValidateFromForm() {
        if(smsToken == false){
        	form.recordError("Sms Token Tidak Boleh Kosong");
        }
    }
    
   
    
    public void setupRender(){
    	//buildCustomerReferenceModel();//
    	smsToken = true;
    }
    


    private void buildCustomerReferenceModel() {
    	deviceList = deviceDao.getAll(sessionManager.getLoggedCustomerView().getId());
    	customerDeviceSelect = genericSelectModelFactory.create(deviceList);
        if (customerDeviceSelect.getOptions().size() > 0) {
            customerReference2 = customerDeviceSelect.getOptions().get(0).getValue().toString();
        }
    }
    
   
    Object onSuccess() {

//    	if(smsToken){
//    	deviceDao.deactivateSoftTokenAll(sessionManager.getLoggedCustomerView().getId());
//    	}else{
        	//String deviceId = customerReference2.substring(0, customerReference2.lastIndexOf("   "));
        	//String namaPrngkat = customerReference2.substring(customerReference2.lastIndexOf("   "), customerReference2.lastIndexOf("-"));
//        	deviceDao.deactivateSoftTokenAll(sessionManager.getLoggedCustomerView().getId());
//        	deviceDao.activateSoftToken(sessionManager.getLoggedCustomerView().getId(), deviceId);
//    	}
//    	return this;
    	//String 
        //linkSoftTokenConfirm.setDeviceId(deviceId);
    	//linkSoftTokenConfirm.setNamaPerangkat(namaPrngkat);
    	linkSoftTokenConfirm.setSmsToken(smsToken);
    	return linkSoftTokenConfirm;
    	
    }
    
    
    public String getsoftToken(){
    	customerDevice = deviceDao.getSoftToken(sessionManager.getLoggedCustomerView().getId());
    	if(customerDevice != null){
    		return customerDevice.getLabel(Locale.getDefault());
    	}else
    		return "Sms Token";
    }
    
    @Property
    private final ValueEncoder<CustomerDevice> encoder = new ValueEncoder<CustomerDevice> () {

		public String toClient(CustomerDevice value) {
			return String.valueOf(value.getId());
		}

		public CustomerDevice toValue(String clientValue) {
			return deviceDao.get(Long.parseLong(clientValue));
		}
    	
    };
    

}
