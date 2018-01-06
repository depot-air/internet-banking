package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.PlanePaymentViewService;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 2:59 PM
 */
@Service("planePaymentViewService")
public class PlanePaymentViewServiceImpl extends PaymentViewServiceImpl implements PlanePaymentViewService {
    public PlanePaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
        }

        public Boolean decompose(Object view, Transaction transaction) {
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
        }

        public Boolean decompose(Object view, Transaction transaction) {
            return Boolean.TRUE;
        }
    }

    /**
     * Class to compose and decompose message for reprint phase
     */
    private class ReprintMessageCustomizer implements MessageCustomizer {
        private ReprintMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
        }

        public Boolean decompose(Object view, Transaction transaction) {
            return Boolean.TRUE;
        }
    }

}
