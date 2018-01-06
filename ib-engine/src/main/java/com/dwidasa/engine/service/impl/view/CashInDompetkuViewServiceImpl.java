package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashInDompetkuView;
import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.engine.service.view.CashOutDelimaViewService;
import com.dwidasa.engine.service.view.CashInDompetkuViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.ReferenceGenerator;

import org.apache.commons.lang.StringUtils;
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
@Service("cashInDompetkuViewService")
public class CashInDompetkuViewServiceImpl implements CashInDompetkuViewService {
    private final MessageCustomizer inquiryMessageCustomizer;
    private final MessageCustomizer transactionMessageCustomizer;
    private final MessageCustomizer reprintMessageCustomizer;

    public CashInDompetkuViewServiceImpl() {
        inquiryMessageCustomizer = new InquiryMessageCustomizer();
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        CashOutDelimaView cv = (CashOutDelimaView) view;
        cv.setToAccountType("00");
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
            CashOutDelimaView cv = (CashOutDelimaView) view;

            String customData = "";
            //-- receiver id an(25) left justified padding with space
            customData += String.format("%1$-25s", cv.getReceiverIdNumber());
            //-- transfer code n(16) left justfied padding with space
            customData += String.format("%1$-16s", cv.getCustomerReference());
            //-- amount n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- provider fee n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- reference number an(12) left justfied padding with space
            customData += String.format("%1$-12s", " ");
            //-- admin charges - bank n(12) left justified padding with space
            customData += String.format("%012d", 0);

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00201002014");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashOutDelimaView cv = (CashOutDelimaView) view;

            String bit48 = transaction.getFreeData1();
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            cv.setReceiverName(bit48.substring(262, 292).trim());
            cv.setReceiverAddress(bit48.substring(293, 323).trim());
            cv.setReceiverCity(bit48.substring(323, 343).trim());
            cv.setReceiverPhoneNumber(bit48.substring(432, 447));
            cv.setAmount(transaction.getTransactionAmount());
            cv.setFee(transaction.getFee());
            cv.setFeeIndicator(transaction.getFeeIndicator());
            cv.setBit48(bit48);

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
            CashInDompetkuView cv = (CashInDompetkuView) view;
            
            cv.setReferenceNumber(ReferenceGenerator.generate());
            String customData = "";
            
            if(cv.getTransactionType().equals(Constants.DOMPETKU_CASH_IN)){
            
            customData = StringUtils.rightPad(cv.getReferenceNumber(), 12, "");
            customData += StringUtils.rightPad(cv.getMsiSDN(), 20, "");
          
            }else{
            	
            	 customData = StringUtils.rightPad(cv.getReferenceNumber(), 12, "");
            	 customData += StringUtils.rightPad(cv.getMsiSDN(), 20, "");
                 customData += StringUtils.rightPad(cv.getToken(), 6, "");
            }
            
            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00202002014");
        }

        public Boolean decompose(Object view, Transaction transaction) {

        	CashInDompetkuView cv = (CashInDompetkuView) view;
        	cv.setResponseCode(transaction.getResponseCode());
        	cv.setReferenceNumber(transaction.getReferenceNumber());
        	cv.setApproval(transaction.getApprovalNumber());
        	
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
            CashOutDelimaView cv = (CashOutDelimaView) view;

            String customData = "";
            //-- transfer code n(16) left justfied padding with space
            customData += String.format("%1$-16s", cv.getCustomerReference());

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00203002014");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashOutDelimaView cv = (CashOutDelimaView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

            String bit48 = transaction.getFreeData1();
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            cv.setBillerReference(bit48.substring(65, 77).trim());
            //-- sender data
            cv.setSenderName(bit48.substring(77, 107).trim());
            cv.setSenderGender(bit48.substring(107, 108));
            cv.setSenderAddress(bit48.substring(108, 138).trim());
            cv.setSenderCity(bit48.substring(138, 158).trim());
            cv.setSenderPostalCode(bit48.substring(158, 168).trim());
            cv.setSenderCountry(bit48.substring(168, 184).trim());
            cv.setSenderCardType(bit48.substring(184, 194).trim());
            cv.setSenderIdNumber(bit48.substring(194, 219).trim());
            cv.setSenderPob(bit48.substring(219, 239).trim());
            try {
                cv.setSenderDob(sdf.parse(bit48.substring(239, 247)));
            } catch (ParseException e) {
                cv.setSenderDob(new Date());
            }
            cv.setSenderPhoneNumber(bit48.substring(247, 262));
            //-- receiver data
            cv.setReceiverName(bit48.substring(262, 292).trim());
            cv.setReceiverGender(bit48.substring(292, 293));
            cv.setReceiverAddress(bit48.substring(293, 323).trim());
            cv.setReceiverCity(bit48.substring(323, 343).trim());
            cv.setReceiverPostalCode(bit48.substring(343, 353).trim());
            cv.setReceiverCountry(bit48.substring(353, 369).trim());
            cv.setReceiverCardType(bit48.substring(369, 379).trim());
            cv.setReceiverIdNumber(bit48.substring(379, 404).trim());
            cv.setReceiverPob(bit48.substring(404, 424).trim().trim());
            try {
                cv.setReceiverDob(sdf.parse(bit48.substring(424, 432)));
            } catch (ParseException e) {
                cv.setReceiverDob(new Date());
            }
            cv.setReceiverPhoneNumber(bit48.substring(432, 447).trim());

            cv.setAmount(transaction.getTransactionAmount());
            cv.setFee(transaction.getFee());
            cv.setFeeIndicator(transaction.getFeeIndicator());

            return Boolean.TRUE;
        }
    }
}
