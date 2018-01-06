package com.dwidasa.ib.components;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.javascript.StylesheetLink;
import org.apache.tapestry5.services.javascript.StylesheetOptions;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.CustomerSessionDao;
import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Menu;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.Index;
import com.dwidasa.ib.services.SessionManager;

/**
 * Layout component for pages of application ib.
 */
@Import(library = {"context:bprks/js/jquery-1.6.2.min.js", "context:bprks/js/jquery.js",
        "context:bprks/js/script.js", "context:bprks/js/layout.js", "context:bprks/js/validation.js", "context:bprks/js/timeDate.js",
        "context:bprks/js/zone-overlay.js"}, stylesheet={"context:bprks/css/zone-overlay.css", 
		"context:bprks/css/reset.css", 
		"context:bprks/css/text.css", 
		"context:bprks/css/960.css",
		"context:bprks/css/style.css"})
public class Layout
{
	@Inject @Path("context:bprks/css/zone-overlay-ie.css")
	private Asset ieCSS;

	@Environmental
	private JavaScriptSupport javaScriptSupport;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private InboxCustomerDao customerDao;
	
	@Inject
	private ParameterDao parameterDao;
	
    void afterRender() {
    	javaScriptSupport.importStylesheet(new StylesheetLink(ieCSS, new StylesheetOptions().withCondition("IE")));
    	request.getSession(false).setAttribute("currentPage", componentResources.getPageName());    	
    }
    
    @Inject
	private Response response;
    
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String group;

    @SessionAttribute(Constants.SELECTED_LANGUAGE)
    @Property
    private String selectedLanguage;

    @Inject
    private Messages messages;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private Request request;

    @Inject
    private LoggingService loggingService;

    @Inject
    private CustomerSessionDao customerSessionDao;

    @Inject
    private ExtendedProperty extendedProperty;
    
    private List<Menu> roots;

    public List<Menu> getRoots() {
        return roots;
    }

    public List<String> getAvailableLanguages() {
        return Arrays.asList(messages.get("indonesian"), messages.get("english"));
    }

    @Environmental
	private JavaScriptSupport renderSupport;   
    
    @Property
    private CustomerView customerView;
    
    @Property
    private int countUnread;
    
    @Property
    private String status;
    
    void setupRender() {
    	response.setHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0"); 
	    response.setHeader("Pragma","No-Cache"); 
	    customerView = sessionManager.getLoggedCustomerView();
	    //user merchant EDC, hyperwalet dan CVPATRIO (3000113122)
        if (customerView != null) {
            if(customerView.getAccountNumber().startsWith(Constants.MERCHANT_EDC1) || customerView.getAccountNumber().startsWith(Constants.MERCHANT_EDC2) ||
                    customerView.getAccountNumber().startsWith(Constants.HIPERWALLET) || customerView.getAccountNumber().equals("3000113122") ){
                roots = cacheManager.getMenuRootsForMerchantEdc();

            }else if (isAccountPAC(customerView.getAccountNumber(), getMerchantPAC())){
                roots = cacheManager.getMenuRootsForMerchantPac();

            } else if (customerView.getTerminalId().equals(extendedProperty.getDefaultTerminalId())) {
                roots = cacheManager.getMenuRoots();
            }
            else {
                roots = cacheManager.getMenuRootsForMerchant();
            }
        }

	    //UnreadMessageInbox // Untuk Menu Inbox
	    int jml = customerDao.getAllUnreadMessage(sessionManager.getLoggedCustomerView().getId()).size();
	    if(jml == 0){
	    	status = "";
	    	
	    }else{
	    	status = "ada";
	    	countUnread = customerDao.getAllUnreadMessage(sessionManager.getLoggedCustomerView().getId()).size();
	    }
	    
        renderSupport.addScript(InitializationPriority.LATE, "if (($('alerts') != null) && ($('alerts').innerHTML != '')) new Effect.Fade('alerts', { duration:7.0, from:1.0, to:0 });");
        JSONObject spec = new JSONObject();
        spec.put("time", 10 * 60 *  1000); // 10 menit
        renderSupport.addInitializerCall("screenTimeout", spec);
    }
    
    
    public String getMerchantPAC() {    	
    	com.dwidasa.engine.model.Parameter parameter = parameterDao.get("MERCHANT_PAC");
    	return parameter.getParameterValue();
    }
    
    public boolean isAccountPAC(String accountNumber, String accountNumberPac) {    	
    	//String firstFive = phoneInput.substring(0, 5);
    	String[] PAC = accountNumberPac.split(",");
    	boolean isPAC = false;
    	if (PAC.length > 0 ) {
    		for(int i = 0; i < PAC.length; i++) {
    			if (accountNumber.equals(PAC[i]))
    				isPAC = true;
    		}
    	}
    	return isPAC;
    }
   
    void onActionFromLogout() {
        loggingService.logActivity(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.LOGOUT_TYPE, "Logout", "", sessionManager.getDefaultMerchantType(), sessionManager.getLoggedCustomerView().getTerminalId());
//        customerSessionDao.deleteByCustomerId(sessionManager.getLoggedCustomerView().getId());	//ditaruh di HttpSessionEventPublisher, termasuk yg keluar karena session habis
        request.getSession(Boolean.FALSE).invalidate();
    }
    
    public CustomerView getUser() {
    	return sessionManager.getLoggedCustomerView();
    }
    
    public String getCurrentTime() {
    	return String.valueOf(System.currentTimeMillis());
    }
    
    Object onGotoHome() {
    	return Index.class;
    }
    
}
