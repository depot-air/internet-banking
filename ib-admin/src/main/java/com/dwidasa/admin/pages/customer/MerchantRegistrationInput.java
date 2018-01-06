package com.dwidasa.admin.pages.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.AbstractSelectModel;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.pages.customer.MerchantRegistrationConfirm;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.view.IbTokenEncoder;
import com.dwidasa.engine.model.IbToken;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.IbTokenService;

@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, Constants.RoleName.DAY_ADMIN, Constants.RoleName.SUPERUSER})
public class MerchantRegistrationInput {
	
    @Property
    @Persist
    private String strSearch;

    @Inject
    private IbTokenService ibTokenService;

	@Property
    @Persist
    private List<IbToken> ibTokens;
	
	@Property
	@Persist
	private List<IbToken> selectedIbTokens;
	
	private IbTokenEncoder ibTokenEncoder;
    
    @Inject
    private Messages messages;

    @Property
    private SelectModel ibTokenModel;

    @Inject
    private SessionManager sessionManager;
    
    @InjectPage
    private MerchantRegistrationConfirm merchantRegistrationConfirm;

    private boolean isNotNext;

    void onSelectedFromSearch() {
    	isNotNext = true;
    }
    
    void onSelectedFromReset() {
    	isNotNext = true;
    	strSearch = null;
    }
    
    void onSelectedFromNext() {
    	isNotNext = false;
    }
    
	public IbTokenEncoder getIbTokenEncoder() {
		return (IbTokenEncoder) (ibTokenEncoder == null ? new IbTokenEncoder(ibTokenService) : ibTokenService);
	}
	
    void onPrepare() {

    }
    
    void setupRender() {
    	if(ibTokens == null){
 	   		ibTokens = new ArrayList<IbToken>();
		}    
    /*
		if (selectedIbTokens == null) {
			selectedIbTokens = new ArrayList<IbToken>();
		}
	*/
		selectedIbTokens = new ArrayList<IbToken>();
		if (strSearch == null) {
			ibTokenModel = getModel("");
		} else {
			ibTokenModel = getModel(strSearch);
		}
    }

    void onValidateFromForm() {
    	if (isNotNext) {
            return;
    	}
    	User user = sessionManager.getLoggedUser();
    	for (int i = 0; i < selectedIbTokens.size(); i++) {
			IbToken ibToken = selectedIbTokens.get(i);
			ibToken.setStatus(com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_SELECTED);
			ibToken.setCustomerId(null);
			ibToken.setCreatedby(user.getId());
			ibTokenService.save(ibToken);
		}
    }

    public Object onSuccess() {
    	if (isNotNext) {
            return null;
    	}
        return merchantRegistrationConfirm;
    }

    public SelectModel getModel(String serialNumber){
    	if (serialNumber.equals("")) {
    		ibTokens = ibTokenService.getAvailableTokens();
    	} else {
    		ibTokens = ibTokenService.getAvailableTokens(serialNumber);
    	}
    	
       return new AbstractSelectModel(){

          public List<OptionGroupModel> getOptionGroups() {
             return null;
          }

          public List<OptionModel> getOptions() {
             List<OptionModel> options = new ArrayList<OptionModel>();
             if(ibTokens == null){
 	   		    ibTokens = new ArrayList<IbToken>();
		     }
             for(IbToken ibToken: ibTokens){
                options.add(new OptionModelImpl(ibToken.getSerialNumber(), ibToken));
             }
             return options;
          }

       };
    } 

}
