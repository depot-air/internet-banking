/**
 * 
 */
package com.dwidasa.ib.pages.payment;

import com.dwidasa.ib.base.BasePrintPage;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.ColumbiaPaymentView;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * @author dsi-bandung
 *
 */
public class ColumbiaPaymentPrint extends BasePrintPage{
	
	private Logger logger = Logger.getLogger(ColumbiaPaymentPrint.class);
	@Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", locale);

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);
    
    

    @InjectPage
    private ColumbiaPaymentReceipt columbiaPaymentReceipt;

    @Property
    private ColumbiaPaymentView columbiaPaymentView;
    
    @Property
    private String typeTransfer;
    
    @Property
    private String status;
    
    @Inject
    private Messages messages;
    
    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat time = new SimpleDateFormat("hh:mm:ss", threadLocale.getLocale());

    void setupRender() {
    	columbiaPaymentView = columbiaPaymentReceipt.getColumbiaPaymentView();
    	logger.info("columbiaPaymentView=" + columbiaPaymentView);
    	if (columbiaPaymentView.getResponseCode() != null && columbiaPaymentView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	status = messages.get("success");
        } else {
            status = messages.get("failed");
        }

       
        
    }
}