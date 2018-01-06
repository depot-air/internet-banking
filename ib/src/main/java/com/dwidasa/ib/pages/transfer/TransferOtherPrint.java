package com.dwidasa.ib.pages.transfer;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

public class TransferOtherPrint extends BasePrintPage {
	@Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", locale);

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);

    @InjectPage
    private TransferOtherReceipt transferOtherReceipt;

    @Property
    private TransferView transferView;
    
    @Property
    private String status;
    
    @Property
    private String providerName;
    
    @Inject
    private CacheManager cacheManager;
    
    @Inject
    private Messages messages;


    void setupRender() {
    	transferView = transferOtherReceipt.getTransferView();
    	
//    	String productCode = cacheManager.getBillerProducts(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE,
//                transferView.getBillerCode()).get(0).getProductCode();
//        providerName = cacheManager.getProviderProduct(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE,
//                transferView.getBillerCode(),
//                productCode, transferView.getProviderCode()).getProvider().getProviderName();
    	
    	if (transferView.getPending() != null && transferView.getPending()) {
            status = messages.get("pending");
        }
        else if (transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        }
        else {
            status = messages.get("failed");
        }

    	 if (transferView.getPending() != null && transferView.getPending()) {
             status = messages.get("pending");
         }
         else if (transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
             status = messages.get("success");
         }
         else {
             status = messages.get("failed");
         }
    }
    
    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    
    public String getDashIfEmpty(String str) {
		if (str !=  null && str.trim().length() > 0) {
			return str;
		}
		return "-";
    }

}
