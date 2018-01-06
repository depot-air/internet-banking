package com.dwidasa.engine.service.impl.facade;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.IGatePurposeService;
import com.dwidasa.engine.service.impl.view.IGatePurposeMessageCustomizer;
import com.dwidasa.engine.service.impl.view.KioskCheckStatusMessageCustomizer;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.BaseViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Ibaihaqi
 * Date: 3/28/12
 * Time: 6:59 PM
 */
@Service("iGatePurposeServiceImpl")
public class IGatePurposeServiceImpl implements IGatePurposeService {
    private static Logger logger = Logger.getLogger( IGatePurposeServiceImpl.class );
    /**
     * {@inheritDoc}
     */
    public BaseView execute(BaseView view) {
    	logger.info("IGatePurposeServiceImpl execute" );
        Transaction transaction = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        MessageCustomizer msgCustomizer = IGatePurposeMessageCustomizer.instance().getCheckStatusMessageCustomizer(view);

        transaction.setReferenceNumber(view.getReferenceNumber());

        msgCustomizer.compose(view, transaction);

        CommLink link = new MxCommLink(transaction);
        //kirim dan tidak perlu tunggu response
        link.sendMessage(false);

//        logger.info("RC=" + transaction.getResponseCode());
//        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)  ) {
//            msgCustomizer.decompose(view, transaction);
//        }
//        else {
//            throw new BusinessException("IB-1009", transaction.getResponseCode());
//        }

        EngineUtils.setTransactionStatus(transaction);

        return view;
    }

}
