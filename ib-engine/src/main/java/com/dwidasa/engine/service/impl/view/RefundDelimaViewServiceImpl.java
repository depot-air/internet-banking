package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.RefundDelimaView;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.RefundDelimaViewService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 5:45 PM
 */
@Service("refundDelimaViewService")
public class RefundDelimaViewServiceImpl implements RefundDelimaViewService {
    private final MessageCustomizer inquiryMessageCustomizer;
    private final MessageCustomizer transactionMessageCustomizer;
    private final MessageCustomizer reprintMessageCustomizer;

    public RefundDelimaViewServiceImpl() {
        inquiryMessageCustomizer = new InquiryMessageCustomizer();
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        RefundDelimaView rv = (RefundDelimaView) view;
        rv.setToAccountType("00");
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validate(BaseView view) {
        Boolean result = view.validate();
        if (result) {
            //-- database validation if required
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void composeInquiry(BaseView view, Transaction transaction) {
        inquiryMessageCustomizer.compose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeInquiry(BaseView view, Transaction transaction) {
        return inquiryMessageCustomizer.decompose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public void composeTransaction(BaseView view, Transaction transaction) {
        transactionMessageCustomizer.compose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeTransaction(BaseView view, Transaction transaction) {
        return transactionMessageCustomizer.decompose(view, transaction);
    }

    public void composeReprint(BaseView view, Transaction transaction) {
        reprintMessageCustomizer.compose(view, transaction);
    }

    public Boolean decomposeReprint(BaseView view, Transaction transaction) {
        return reprintMessageCustomizer.decompose(view, transaction);
    }

    /**
     * Class to compose and decompose message for inquiry phase
     */
    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            RefundDelimaView rv = (RefundDelimaView) view;

            String customData = "";
            //-- sender id an(25) left justified padding with space
            customData += String.format("%1$-25s", " ");
            //-- transfer code n(16) left justified padding with space
            customData += String.format("%1$-16s", rv.getCustomerReference());
            //-- amount n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- provider fee n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- reference an(12) left justified padding with space
            customData += String.format("%1$-12s", " ");
            //-- bank admin charge n(12) right justified zero padding
            customData += String.format("%012d", 0);

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00209002018");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            RefundDelimaView rv = (RefundDelimaView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

            String bit48 = transaction.getFreeData1();
            rv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            rv.setBillerReference(bit48.substring(65, 77).trim());
            //-- sender data
            rv.setSenderName(bit48.substring(77, 107).trim());
            rv.setSenderGender(bit48.substring(107, 108));
            rv.setSenderAddress(bit48.substring(108, 138).trim());
            rv.setSenderCity(bit48.substring(138, 158).trim());
            rv.setSenderPostalCode(bit48.substring(158, 168).trim());
            rv.setSenderCountry(bit48.substring(168, 184).trim());
            rv.setSenderCardType(bit48.substring(184, 194).trim());
            rv.setSenderIdNumber(bit48.substring(194, 219).trim());
            rv.setSenderPob(bit48.substring(219, 239).trim());
            try {
                rv.setSenderDob(sdf.parse(bit48.substring(239, 247)));
            } catch (ParseException e) {
                rv.setSenderDob(new Date());
            }
            rv.setSenderPhoneNumber(bit48.substring(247, 262));
            //-- receiver data
            rv.setReceiverName(bit48.substring(262, 292).trim());
            rv.setReceiverGender(bit48.substring(292, 293));
            rv.setReceiverAddress(bit48.substring(293, 323).trim());
            rv.setReceiverCity(bit48.substring(323, 343).trim());
            rv.setReceiverPostalCode(bit48.substring(343, 353).trim());
            rv.setReceiverCountry(bit48.substring(353, 369).trim());
            rv.setReceiverCardType(bit48.substring(369, 379).trim());
            rv.setReceiverIdNumber(bit48.substring(379, 404).trim());
            rv.setReceiverPob(bit48.substring(404, 424).trim().trim());
            try {
                rv.setReceiverDob(sdf.parse(bit48.substring(424, 432)));
            } catch (ParseException e) {
                rv.setReceiverDob(new Date());
            }
            rv.setReceiverPhoneNumber(bit48.substring(432, 447).trim());

            rv.setAmount(transaction.getTransactionAmount());
            rv.setFee(transaction.getFee());
            rv.setFeeIndicator(transaction.getFeeIndicator());

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
            RefundDelimaView rv = (RefundDelimaView) view;

            String customData = "";
            //-- sender id an(25) left justified padding with space
            customData += String.format("%1$-25s", rv.getSenderIdNumber());
            //-- transfer code n(16) left justified padding with space
            customData += String.format("%1$-16s", rv.getCustomerReference());
            //-- amount n(12) right justified zero padding
            customData += String.format("%012d", rv.getAmount().longValue());
            //-- provider fee n(12) right justified zero padding
            customData += String.format("%012d", rv.getProviderFee().longValue());
            //-- reference an(12) left justified padding with space
            customData += String.format("%1$-12s", rv.getBillerReference());
            //-- bank admin charge n(12) right justified zero padding
            customData += String.format("%012d", rv.getFee().longValue());

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00210002018");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            RefundDelimaView rv = (RefundDelimaView) view;
            rv.setBillerReference(transaction.getFreeData1().substring(65, 77).trim());
            rv.setResponseCode(transaction.getResponseCode());
            rv.setReferenceNumber(transaction.getReferenceNumber());

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
            RefundDelimaView cv = (RefundDelimaView) view;

            String customData = "";
            //-- transfer code n(16) left justfied padding with space
            customData += String.format("%1$-16s", cv.getCustomerReference());

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00211002018");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            RefundDelimaView cv = (RefundDelimaView) view;

            String bit48 = transaction.getFreeData1();
            cv.setReceiverIdNumber(bit48.substring(0, 25));
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            cv.setBillerReference(bit48.substring(65, 77).trim());

            cv.setAmount(transaction.getTransactionAmount());
            cv.setFee(transaction.getFee());
            cv.setFeeIndicator(transaction.getFeeIndicator());

            return Boolean.TRUE;
        }
    }
}
