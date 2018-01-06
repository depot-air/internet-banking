package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

import org.apache.log4j.Logger;
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
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/08/11
 * Time: 11:14
 */
public class PlnPurchaseConfirm {
	private Logger logger = Logger.getLogger(PlnPurchaseConfirm.class);
    @Property(write = false)
    @Persist
    private PlnPurchaseView plnPurchaseView;

    @Property
    private TokenView tokenView;

    @Inject
    private OtpManager otpManager;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private PurchaseService purchaseService;

    @Property
    private int tokenType;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @InjectPage
    private PlnPurchaseReceipt plnPurchaseReceipt;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private BigInteger denomination;

    @Property
    private int chooseValue;
    
    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (plnPurchaseView == null) {
            return PlnPurchaseInput.class;
        }
        return null;
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(PlnPurchaseConfirm.class.toString());
        setTokenType();
        denomination = new BigInteger(plnPurchaseView.getDenomination());
        if (plnPurchaseView.getUnsold1() != null && plnPurchaseView.getUnsold1().doubleValue() > 0D) {
//        	plnPurchaseView.setUnsold1(plnPurchaseView.getUnsold1().add(plnPurchaseView.getFee()));
        }
        if (plnPurchaseView.getUnsold2() != null && plnPurchaseView.getUnsold2().doubleValue() > 0D) {
//        	plnPurchaseView.setUnsold2(plnPurchaseView.getUnsold2().add(plnPurchaseView.getFee()));
        }
        
    }

    void onValidateFromForm() {

        try {

            if (plnPurchaseView.getSave() && plnPurchaseView.getInputType().equalsIgnoreCase("M")) {
                purchaseService.register(plnPurchaseView.transform());
            }
            if (otpManager.validateToken(plnPurchaseView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            if(chooseValue==0){
                plnPurchaseView.setAmount(new BigDecimal(plnPurchaseView.getDenomination()));
                plnPurchaseView.setBuyingOption("0");
            } else if(chooseValue==1){
            	logger.info("plnPurchaseView.getUnsold1()=" + plnPurchaseView.getUnsold1());
                plnPurchaseView.setAmount(plnPurchaseView.getUnsold1());	//sudah ditambah fee
                plnPurchaseView.setBuyingOption("1");
            } else if(chooseValue==2){
            	logger.info("plnPurchaseView.getUnsold2()=" + plnPurchaseView.getUnsold2());
                plnPurchaseView.setAmount(plnPurchaseView.getUnsold2());	//sudah ditambah fee
                plnPurchaseView.setBuyingOption("1");
            }
            logger.info("sebelum setPlnPurchaseView");
            //plnPurchaseView = (PlnPurchaseView) purchaseService.execute(plnPurchaseView);
            purchaseService.execute(plnPurchaseView);

            logger.info("sesudah setPlnPurchaseView");
            }

        } catch (BusinessException e) {
            logger.info("e=" + e.getMessage());
            form.recordError(e.getFullMessage());
        } catch (Exception e) {
            logger.info("e=" + e.getMessage());
            form.recordError(Constants.EXCEPTION_OVERRIDE_MESSAGE);
        }

        
    }

    @DiscardAfter
    public Object onSuccess() {
        plnPurchaseReceipt.setPlnPurchaseView(plnPurchaseView);
        return plnPurchaseReceipt;
    }

    public void setPlnPurchaseView(PlnPurchaseView plnPurchaseView) {
        this.plnPurchaseView = plnPurchaseView;
    }
}
