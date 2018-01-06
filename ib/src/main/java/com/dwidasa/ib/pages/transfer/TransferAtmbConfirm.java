package com.dwidasa.ib.pages.transfer;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/12/11
 * Time: 00:03 am
 */
public class TransferAtmbConfirm {
    @Property//(write = false)
    @Persist
    private TransferView transferView;
    
    @Property
    @Persist
    private CustomerView customerView;
      
    @Inject
    private TransferService transferService;

    @Inject
    private CacheManager cacheManager;

    @Property
    private int tokenType;
    
    @Property
    private TokenView tokenView;


    @InjectPage
    private TransferAtmbReceipt transferAtmbReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Inject
    private Locale locale;
       
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    public void setTransferConfirmView(TransferView transferView) {
        this.transferView = transferView;
    }
    
    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }
   
    public TransferView getTransferConfirm() {
        return transferView;
    }

    void setupRender() {
    	sessionManager.setSessionLastPage(TransferAtmbConfirm.class.toString());
    	setTokenType();
    }

    public Object onActivate() {
        if (transferView == null) {
            return TransferAtmbInput.class;
        }

        return null;
    }
    
    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    @Inject
    private SessionManager sessionManager;
    
    void onValidateFromForm() {

		if (tokenView.getToken() == null) {
			form.recordError(messages.get("nullTokenError"));
		} else if (!tokenView.getToken().matches("[0-9]+")) {
			form.recordError(messages.get("formatTokenError"));
//		} else if (tokenView.getToken().length() != 8) {
//			form.recordError(messages.get("lengthTokenError"));
		}

		if (!form.getHasErrors() && otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
			try {
				if (transferView.getCustomerReference().length() >=50) {
					transferView.setCustomerReference(transferView.getCustomerReference().substring(0,50).trim());
				}
				setTransferConfirmView((TransferView) transferService.executeATMB(getTransferConfirm())); // .execute(getWaterPaymentView()));
				// transferView.setTransactionStatus(messages.get(transferView.getTransactionStatus().toLowerCase()).toUpperCase());
			} catch (BusinessException e) {
				form.recordError(e.getFullMessage());
				e.printStackTrace();
			}
//			if (getTransferConfirm().getSave() && transferView.getInputType().equalsIgnoreCase("M")) {
//				try {
//					transferService.register(getTransferConfirm().transform());
//				} catch (BusinessException e) {
//					form.recordError(e.getFullMessage());
//					e.printStackTrace();
//				}
//			}
		}
    }

    @DiscardAfter
    public Object onSuccess() {
        transferAtmbReceipt.setTransferReceiptView(transferView);
        return transferAtmbReceipt;
    }

    @DiscardAfter
    public Object onSelectedFromCancel() {
//    	if (!isAlto()) {
//    		return TransferAtmbOption.class;
//    	}
    	return TransferOtherInput.class;
    }

    public boolean isAlto() {    	
    	return (transferView.getTransactionType().equals(Constants.ALTO.TT_INQUIRY) || transferView.getTransactionType().equals(Constants.ALTO.TT_POSTING));
    }
    
    public String getMasked(String str) {
     	return EngineUtils.getCardNumberMasked(str);
     }
}


