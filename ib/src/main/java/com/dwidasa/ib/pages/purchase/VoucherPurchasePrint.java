package com.dwidasa.ib.pages.purchase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/5/11
 * Time: 1:49 PM
 */
public class VoucherPurchasePrint extends BasePrintPage {

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_DATE, threadLocale.getLocale());

    @InjectPage
    private VoucherPurchaseReceipt voucherPurchaseReceipt;

    @Property
    private VoucherPurchaseView voucherPurchaseView;
	
    void setupRender() {    	
        voucherPurchaseView = voucherPurchaseReceipt.getVoucherPurchaseView();
    }

    public Long getDenomination() {
        return Long.valueOf(voucherPurchaseView.getDenomination());
    }

    public boolean isTelkomsel() {    	
    	return (voucherPurchaseView.getProviderCode().equals(com.dwidasa.engine.Constants.PROVIDER_FINNET_CODE) );
    }
    
    public String getISB(){
    	if(voucherPurchaseView.getProviderCode().equals("ISB")){
    		return "ISB";
    	}else{
    		return "";
    	}
    }
    
}
