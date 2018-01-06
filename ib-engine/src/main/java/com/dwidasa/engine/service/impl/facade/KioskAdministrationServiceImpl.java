package com.dwidasa.engine.service.impl.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.SmsRegistrationView;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.KioskAdministrationService;
import com.dwidasa.engine.service.impl.view.KioskAdministrationMessageCustomizer;
import com.dwidasa.engine.service.impl.view.KioskCheckStatusMessageCustomizer;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.BaseViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 9/4/12
 */

@Service("kioskAdministrationService")
public class KioskAdministrationServiceImpl extends BaseTransactionServiceImpl implements KioskAdministrationService {
    private static Logger logger = Logger.getLogger(KioskAdministrationServiceImpl.class);

    public BaseView posting(BaseView view) {
//        BaseViewService viewService = getServiceObject(view);
//        viewService.preProcess(view);

        Transaction transaction = TransformerFactory.getTransformer(view)
                .transformTo(view, new Transaction());
        MessageCustomizer msgCustomizer = KioskAdministrationMessageCustomizer.instance().getAdministrationMessageCustomizer(view);
        transaction.setReferenceNumber(view.getReferenceNumber());
        logger.info("msgCustomizer=" + msgCustomizer);
        logger.info("view=" + view);
        logger.info("transaction=" + transaction);
        msgCustomizer.compose(view, transaction);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();
        transaction.setResponseCode(getMappedResponseCode(transaction.getResponseCode(), transaction.getTransactionType()));
        /*
        if (transaction.getResponseCode().equals("G2")) {   //ATMB
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }
        else if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE) || !(transaction.getResponseCode().equals(Constants.TIMEOUT_CODE)) ) {
        */
        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            msgCustomizer.decompose(view, transaction);
        }
        else {
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        transaction.setExecutionType(Constants.NOW_ET);
        EngineUtils.setTransactionStatus(transaction);
        transactionDao.save(transaction);
        TransactionData transactionData = EngineUtils.createTransactionData(view, transaction.getId());
        transactionDataDao.save(transactionData);
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

    private String getMappedResponseCode(String responseCode, String transactionType) {
        if (transactionType.equals(Constants.VOUCHER_GAME.TRANS_TYPE_INQUIRY) || transactionType.equals(Constants.VOUCHER_GAME.TRANS_TYPE_POSTING) || transactionType.equals(Constants.VOUCHER_GAME.TRANS_TYPE_CHECKSTATUS)) {
            if (responseCode.equals("06")) return "S1";
            else if (responseCode.equals("95")) return "S2";
        }
        return responseCode;
    }
}
