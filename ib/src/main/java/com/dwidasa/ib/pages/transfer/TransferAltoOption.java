package com.dwidasa.ib.pages.transfer;


import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;

public class TransferAltoOption {
	@Inject
    private TransferService transferService;
	
	@InjectPage
	private TransferAtmbInput transferAtmbInput;
	
    @Property
    private SelectModel billerModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private CacheManager cacheManager;

    @Property
    @Persist
    private TransferView transferView;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private SessionManager sessionManager;
    
    public void buildBillerModel() {
        billerModel = genericSelectModelFactory.create(cacheManager.getBillers( com.dwidasa.engine.Constants.ALTO.TT_POSTING));
        if (billerModel.getOptions().size() > 0) {
            transferView.setProviderCode(com.dwidasa.engine.Constants.ALTO.PROVIDER_CODE);
        }
    }

    public void setupRender() {
        if (transferView == null) {
            transferView = new TransferView();
        }
        sessionManager.setSessionLastPage(TransferAltoOption.class.toString());        
        buildBillerModel();
    }

    public Object onSuccess() {
        Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.ALTO.TT_POSTING, transferView.getBillerCode());
        transferView.setBillerCode(biller.getBillerCode());
        transferView.setBillerName(biller.getBillerName());
        transferView.setTransactionType(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY);
        transferView.setProviderCode(com.dwidasa.engine.Constants.ALTO.PROVIDER_CODE);

        transferAtmbInput.setTransferInputView(transferView);
        return transferAtmbInput;
    }

    public void pageReset(){
    	transferView = null;
        transferAtmbInput.setTransferInputView(transferView);
    }
}


