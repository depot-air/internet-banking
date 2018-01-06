package com.dwidasa.engine.service.impl.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.KioskCheckStatusService;
import com.dwidasa.engine.service.impl.view.KioskCheckStatusMessageCustomizer;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.BaseViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ibaihaqi
 * Date: 3/28/12
 * Time: 6:59 PM
 */
@Service("kioskCheckStatusService")
public class KioskCheckStatusServiceImpl implements KioskCheckStatusService {
    private static Logger logger = Logger.getLogger( KioskCheckStatusServiceImpl.class );
    /**
         * {@inheritDoc}
         */
        public BaseView checkStatus(BaseView view) {
            BaseViewService viewService = getServiceObject(view);
            viewService.preProcess(view);

            Transaction transaction = TransformerFactory.getTransformer(view)
                    .transformTo(view, new Transaction());
            MessageCustomizer msgCustomizer = KioskCheckStatusMessageCustomizer.instance().getCheckStatusMessageCustomizer(view);

            transaction.setReferenceNumber(view.getReferenceNumber());

            msgCustomizer.compose(view, transaction);

            CommLink link = new MxCommLink(transaction);
            link.sendMessage();
            logger.info("RC=" + transaction.getResponseCode());

            if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)  ) {      //|| !(transaction.getResponseCode().equals(Constants.TIMEOUT_CODE))
                msgCustomizer.decompose(view, transaction);
            }
            else if (transaction.getTransactionType().equals(Constants.ATMB.TT_CEK_STATUS) && transaction.getResponseCode().equals("12")) {
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
