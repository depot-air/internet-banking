package com.dwidasa.engine.service.impl.facade;

import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.service.impl.view.KioskReprintMessageCustomizer;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.BaseViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;

@Service("kioskReprintService")
public class KioskReprintServiceImpl implements KioskReprintService{

	/**
     * {@inheritDoc}
     */
	public BaseView reprint(BaseView view) {
		// TODO Auto-generated method stub
		BaseViewService viewService = getServiceObject(view);
        viewService.preProcess(view);

        Transaction transaction = TransformerFactory.getTransformer(view)
                .transformTo(view, new Transaction());
        MessageCustomizer msgCustomizer = KioskReprintMessageCustomizer.instance().getReprintMessageCustomizer(view);
        
        transaction.setReferenceNumber(view.getReferenceNumber());
        
        msgCustomizer.compose(view, transaction);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();

        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	msgCustomizer.decompose(view, transaction);
        }
        else {
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        EngineUtils.setTransactionStatus(transaction);
        
        return view;
	}

	/**
     * Getting service object from view
     * @param view view object provided
     * @return viewService object
     */
    protected BaseViewService getServiceObject(BaseView view) {
        String viewServiceName = view.getClass().getSimpleName() + "Service";
        viewServiceName = viewServiceName.substring(0,1).toLowerCase() + viewServiceName.substring(1);
        return (BaseViewService) ServiceLocator.getService(viewServiceName);
    }


}
