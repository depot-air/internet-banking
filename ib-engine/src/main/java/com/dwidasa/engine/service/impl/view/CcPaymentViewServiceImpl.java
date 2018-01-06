package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CcPaymentView;
import com.dwidasa.engine.service.view.CcPaymentViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/19/11
 * Time: 6:59 PM
 */
@Service("ccPaymentViewService")
public class CcPaymentViewServiceImpl extends PaymentViewServiceImpl implements CcPaymentViewService {
    public CcPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    /**
     * Class to compose and decompose message for inquiry phase
     */
    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            CcPaymentView pv = (CcPaymentView) view;

            String customData = "";
            //-- biller id = industry code(n2) + biller code(n3) - (n5)
            customData = "08" + StringUtils.leftPad(pv.getBillerCode(), 3);
            //-- payee id = pt. dsi(n4)
            customData = customData + "0015";
            //-- product id(n4)
            customData = customData + StringUtils.leftPad(pv.getProductCode(), 4);
            //-- customer number(an16) left justified space padding
            customData = customData + String.format("%1$-16s", pv.getCustomerReference());
            //--nominal(an12) right justified zero padding
            customData = customData + "000000000000";

            transaction.setFreeData1(customData);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CcPaymentView pv = (CcPaymentView) view;
            pv.setReferenceName(transaction.getFreeData1().substring(13, 29));

            return Boolean.TRUE;
        }
    }

    /**
     * Class to compose and decompose message for execute phase
     */
    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            CcPaymentView pv = (CcPaymentView) view;

            String customData = "";
            //-- biller id = industry code(n2) + biller code(n3) - (n5)
            customData = "08" + StringUtils.leftPad(pv.getBillerCode(), 3);
            //-- payee id = pt. dsi(n4)
            customData = customData + "0015";
            //-- product id(n4)
            customData = customData + StringUtils.leftPad(pv.getProductCode(), 4);
            //-- customer number(an16) left justified space padding
            customData = customData + String.format("%1$-16s", pv.getCustomerReference());
            //--nominal(an12) right justified zero padding
            NumberFormat nf = new DecimalFormat("000000000000");
            customData = customData + nf.format(pv.getAmount().longValue());

            transaction.setFreeData1(customData);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CcPaymentView pv = (CcPaymentView) view;
            pv.setResponseCode(transaction.getResponseCode());
            pv.setReferenceNumber(transaction.getReferenceNumber());

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
