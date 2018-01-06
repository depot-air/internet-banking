package com.dwidasa.ib.pages.transfer;

import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: HMS
 * Date: 31/07/2012
 * Time: 00:03 am
 */
public class RegisterTransferOtherConfirmAtmB {
    @Property(write = false)
    @Persist
    private TransferView transferView;
    
    @Persist
    private String billerCode;
    
    @Persist
    private boolean Alto;

    @Property
    private TokenView tokenView;

    @Inject
    private TransferService transferService;

    @Inject
    private CacheManager cacheManager;

    @Property
    private int tokenType;

    @InjectPage
    private RegisterTransferOtherReceipt registerTransferOtherReceiptAtmB;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private BillerDao billerDao;
    
    @Persist
    private long idBiller;
    
    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    void setupRender() {
    	tokenView = new TokenView();
        setTokenType();
    }

    public Object onActivate() {
        if (transferView == null) {
            return RegisterTransferOtherInputAtmB.class;
        }

        return null;
    }

    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    void onValidateFromForm() {
    	try {
            if (otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
               
            	System.out.println("HOHOHOHOHO 3 "+getIdBiller());
            	Biller bil = billerDao.getTransactionType(getIdBiller()); //cacheManager.getBiller("4b", transferView.getBillerCode());
        		List<Biller>billers = billerDao.getAllTransactionTypeTransfer(getIdBiller(), bil.getTransactionTypeId(), "4b");
        		
            	if(billers.size() > 0){
            		transferView.setBillerCode(getBillerCode());
            	}
            	transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_POSTING);
            	transferService.register(transferView.transform());
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        transferView.setResponseCode(Constants.SUCCESS_CODE);
        transferView.setReferenceNumber(ReferenceGenerator.generate());
        registerTransferOtherReceiptAtmB.setTransferView(transferView);
        return registerTransferOtherReceiptAtmB;
    }

    public boolean isAlto() {
    	return transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ALTO.TT_POSTING);
    }
    
    public void setBillerCode(String billerCode) {
		this.billerCode = billerCode;
	}
    
    public String getBillerCode() {
		return billerCode;
	}
    
    public void setIdBiller(long idBiller) {
		this.idBiller = idBiller;
	}
    
    public long getIdBiller() {
		return idBiller;
	}
}


