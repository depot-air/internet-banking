package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.CashOutDelimaViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 5:45 PM
 */
@Service("cashOutDelimaViewService")
public class CashOutDelimaViewServiceImpl implements CashOutDelimaViewService {
	private static Logger logger = Logger.getLogger(CashOutDelimaViewServiceImpl.class);
	
    private final MessageCustomizer inquiryMessageCustomizer;
    private final MessageCustomizer transactionMessageCustomizer;
    private final MessageCustomizer reprintMessageCustomizer;
    private final MessageCustomizer smsMessageCustomizer;

    public CashOutDelimaViewServiceImpl() {
        inquiryMessageCustomizer = new InquiryMessageCustomizer();
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
        smsMessageCustomizer = new SmsMessageCustomizer();
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
            customData += String.format("%1$-25s", " ");	//cv.getReceiverIdNumber()
            //-- transfer code n(16) left justfied padding with space
            customData += String.format("%1$-16s", cv.getCustomerReference());
            //-- reference number an(16) left justfied padding with space
            customData += String.format("%1$-16s", " ");
            
            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00201002014");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashOutDelimaView cv = (CashOutDelimaView) view;
   
            DateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            String bit48 = transaction.getFreeData1();
            //25:R: :customer-identity;
            cv.setSenderIdNumber(bit48.substring(0, 25).trim());
            //16:R: :transfer-code;
            cv.setBillerReference(bit48.substring(25, 41).trim());
            //12:L:0:amount;
            cv.setAmount(BigDecimal.valueOf(Double.parseDouble(bit48.substring(41, 53))));
            //12:L:0:provider-fee;
            cv.setFee(BigDecimal.valueOf(Double.parseDouble(bit48.substring(53, 65))));
            //16:R: :reference;
            //30:R: :sender-name;
            cv.setSenderName(bit48.substring(81, 111));
            //1:R: :sender-gender;
            cv.setSenderGender(bit48.substring(111, 112));
            //30:R: :sender-address;
            cv.setSenderAddress(bit48.substring(112, 142));
            //20:R: :sender-city;
            cv.setSenderCity(bit48.substring(142, 162));
            //10:R: :sender-postcode;
            cv.setSenderPostalCode(bit48.substring(162, 172));
            //16:R: :sender-country;
            cv.setSenderCountry(bit48.substring(172, 188));
            //10:R: :sender-idcard-type;
            cv.setSenderCardType(bit48.substring(188, 198));
            //25:R: :sender-idcard-number;	198-223
            
            //20:R: :sender-birth-place;
            cv.setSenderPob(bit48.substring(223, 243));
            //8:R: :sender-birth-date;	
            try {
				cv.setSenderDob(sdf.parse(bit48.substring(243, 251)));
			} catch (ParseException e) {
				cv.setSenderDob(new Date());
			}
            //15:R: :sender-phone;
            cv.setSenderPhoneNumber(bit48.substring(251, 266));
            //30:R: :recipient-name;
            cv.setReceiverName(bit48.substring(266, 296));
            //1:R: :recipient-gender;
            cv.setReceiverGender(bit48.substring(296, 297));
            //30:R: :recipient-address;
            cv.setReceiverAddress(bit48.substring(297, 327));
            //20:R: :recipient-city;
            cv.setReceiverCity(bit48.substring(327, 347));
            //10:R: :recipient-postcode;
            cv.setReceiverPostalCode(bit48.substring(347, 357));
            //16:R: :recipient-country;
            cv.setReceiverCountry(bit48.substring(357, 373));
            //10:R: :recipient-idcard-type;
            cv.setReceiverCardType(bit48.substring(373, 383));
            //25:R: :recipient-idcard-number;
            cv.setReceiverIdNumber(bit48.substring(383, 408));
            //20:R: :recipient-birth-place;
            cv.setReceiverPob(bit48.substring(408, 428));
            //8:R: :recipient-birth-date;
            try {
				cv.setReceiverDob(sdf.parse(bit48.substring(428, 436)));
			} catch (ParseException e) {
				cv.setSenderDob(new Date());
			}
            //15:R: :recipient-phone;
            cv.setReceiverPhoneNumber(bit48.substring(436, 451));
            //12:L:0:admin-fee;
            
//            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
//            cv.setReceiverName(bit48.substring(262, 292).trim());
//            cv.setReceiverAddress(bit48.substring(293, 323).trim());
//            cv.setReceiverCity(bit48.substring(323, 343).trim());
//            cv.setReceiverPhoneNumber(bit48.substring(432, 447));
            
//            cv.setAmount(transaction.getTransactionAmount());
//            cv.setFee(transaction.getFee());
//            cv.setFeeIndicator(transaction.getFeeIndicator());
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
            CashOutDelimaView cv = (CashOutDelimaView) view;
            DateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            //25:customer-identity;
            String customData = StringUtils.rightPad(cv.getCardNumber(), 25, " ");
            //16:customer-id;
            customData += StringUtils.rightPad(cv.getCardNumber(), 16, " ");
            //12:amount;
            customData += StringUtils.leftPad(cv.getAmount().toPlainString(), 12, "0");
            //12:provider-fee;
            customData += StringUtils.leftPad(cv.getFee().toPlainString(), 12, "0");
            //16:reference;
            customData += StringUtils.rightPad(cv.getBillerReference(), 16, " ");
            //30:recipient-name;
            customData += StringUtils.rightPad(cv.getReceiverName(), 30, " ");
            //1:recipient-gender;
            customData += StringUtils.rightPad(cv.getReceiverGender(), 1, " ");
            //30:recipient-address;
            customData += StringUtils.rightPad(cv.getReceiverAddress(), 30, " ");
            //20:recipient-city;
            customData += StringUtils.rightPad(cv.getReceiverCity(), 20, " ");
            //10:recipient-postcode;
            customData += StringUtils.rightPad(cv.getReceiverPostalCode(), 10, " ");
            //16:recipient-country;
            customData += StringUtils.rightPad(cv.getReceiverCountry(), 16, " ");
            //10:recipient-idcard-type;
            customData += StringUtils.rightPad(cv.getReceiverCardType(), 10, " ");
            //25:recipient-idcard-number;
            customData += StringUtils.rightPad(cv.getReceiverIdNumber(), 25, " ");
            //20:recipient-birth-place;
            customData += StringUtils.rightPad(cv.getReceiverPob(), 20, " ");
            //8:recipient-birth-date;
            Date dob = new Date();
            if (cv.getReceiverDob() != null) dob = cv.getReceiverDob();
            customData += StringUtils.rightPad(sdf.format(dob), 8, " ");
            //15:recipient-phone;
            customData += StringUtils.rightPad(cv.getReceiverPhoneNumber(), 15, " ");
            //12:admin-fee
            customData += StringUtils.leftPad(cv.getFee().toPlainString(), 12, " ");
            
            transaction.setFreeData1(cv.getBit48());
            transaction.setTranslationCode("00202002014");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashOutDelimaView cv = (CashOutDelimaView) view;
            String bit48 = transaction.getFreeData1();
            
            //1234567890               975319951547734700000012000000000000350002136069147     000000003500
            //25:R: :customer-identity;
            cv.setSenderIdNumber(bit48.substring(0, 25).trim());
            //16:R: :transfer code;
            cv.setBillerReference(bit48.substring(25, 41).trim());
            //12:L:0:amount;
            cv.setAmount(BigDecimal.valueOf(Double.valueOf(bit48.substring(41, 53))));
            //12:L:0:provider-fee;
            cv.setFee(BigDecimal.valueOf(Double.valueOf(bit48.substring(53, 65))));
            //16:R: :reference;
            //12:L:0:admin-fee
            
            cv.setBit48(bit48);
            
            cv.setResponseCode(transaction.getResponseCode());
            cv.setReferenceNumber(transaction.getReferenceNumber());
            cv.setTraceNumber(transaction.getStanSixDigit());
            
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

    private class SmsMessageCustomizer implements MessageCustomizer {
        private SmsMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            CashOutDelimaView cv = (CashOutDelimaView) view;
            logger.info("cv.getTransactionType()=" + cv.getTransactionType());
            cv.setTransactionType(Constants.CASHOUT_DELIMA_SMS_VERIFICATION);
            logger.info("after cv.getTransactionType()=" + cv.getTransactionType());
            logger.info("cv.getBit48()=" + cv.getBit48());
            String customData = cv.getBit48();
            //sms verification
            logger.info("cv.getDescription()=" + cv.getDescription());
            customData += StringUtils.rightPad(cv.getDescription(), 6, "");
            
            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00202002014");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashOutDelimaView cv = (CashOutDelimaView) view;
            String bit48 = transaction.getFreeData1();
            
            //1234567890               975319951547734700000012000000000000350002136069147     000000003500
            //25:R: :customer-identity;
            cv.setSenderIdNumber(bit48.substring(0, 25).trim());
            //16:R: :transfer code;
            cv.setBillerReference(bit48.substring(25, 41).trim());
            //12:L:0:amount;
            cv.setAmount(BigDecimal.valueOf(Double.valueOf(bit48.substring(41, 53))));
            //12:L:0:provider-fee;
            cv.setFee(BigDecimal.valueOf(Double.valueOf(bit48.substring(53, 65))));
            //16:R: :reference;
            //12:L:0:admin-fee
           
            cv.setResponseCode(transaction.getResponseCode());
            cv.setReferenceNumber(transaction.getReferenceNumber());
            cv.setTraceNumber(transaction.getStanSixDigit());
            
            return Boolean.TRUE;
        }
    }

	@Override
	public BaseView smsProcess(BaseView view) {

		Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
		t.setTranslationCode(null);
		t.setFreeData1(new String());

		CommLink link = new MxCommLink(t);
		MessageCustomizer customizer = new SmsMessageCustomizer();
		customizer.compose(view, t);

        logger.info("after compose");
		link.sendMessage();
        logger.info("after sendMessage()");

		if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
			customizer.decompose(view, t);
		} else {
			throw new BusinessException("IB-1009", t.getResponseCode());
		}

		return view;
	}
}
