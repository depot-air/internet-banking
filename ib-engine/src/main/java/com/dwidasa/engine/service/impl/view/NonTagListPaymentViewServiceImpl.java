package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.NonTagListPaymentViewService;
import com.dwidasa.engine.util.MoneyUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 12/11/11
 * Time: 9:10 PM
 */
@Service("nonTagListPaymentViewService")
public class NonTagListPaymentViewServiceImpl extends PaymentViewServiceImpl implements NonTagListPaymentViewService {
    @Autowired
    private LoggingService loggingService;

    @Autowired
    private ExtendedProperty extendedProperty;
    
    public NonTagListPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            NonTagListPaymentView nv = (NonTagListPaymentView) view;

            //-- switcher id
            String customData = "0000000";
            //-- registration number an(32) left justified padding with space
            customData += String.format("%1$-32s", nv.getCustomerReference());
            //-- area code & transaction code
            if (extendedProperty.isMigration()) {
            	customData += transaction.getCustomerReference().substring(0, 2) + "000";
            } else {
            	customData += "00000";
            }

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("024010001010010005");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            NonTagListPaymentView nv = (NonTagListPaymentView) view;
            String bit48 = transaction.getFreeData1();
            String bit62 = transaction.getFreeData3();

            nv.setAmount(transaction.getTransactionAmount());
            nv.setFeeIndicator(transaction.getFeeIndicator());
            nv.setFee(transaction.getFee());
            nv.setTotal(transaction.getTransactionAmount().add(transaction.getFee()));


            nv.setAreaCode(bit48.substring(39, 41));
            nv.setTransactionCode(bit48.substring(41, 44));
            nv.setTransactionName(bit48.substring(44, 69).trim());
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                nv.setRegistrationDate(sdf.parse(bit48.substring(69, 77)));
            } catch (ParseException e) {
                nv.setRegistrationDate(new Date());
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                nv.setExpiredDate(sdf.parse(bit48.substring(77, 85)));
            } catch (ParseException e) {
                nv.setExpiredDate(new Date());
            }
            //7+32+2+3+25+8+8 = 85
//            nv.setCustomerReference(bit48.substring(85,97));    //customer reference = subscriberId, kadang2 kosong padahal mandatory
            nv.setSubscriberId(bit48.substring(85, 97));

            nv.setSubscriberName(bit48.substring(97, 122).trim());
            nv.setBillerReference(bit48.substring(122, 154).trim());
            nv.setProviderReference(bit48.substring(154, 186).trim());
           
            nv.setServiceUnit(bit48.substring(186, 191));
            nv.setUnitAddress(bit48.substring(191, 226).trim());
            nv.setUnitPhone(bit48.substring(226, 241).trim());

            nv.setBit48(bit48);
            nv.setBit62(bit62);

            nv.setTraceNumber(transaction.getStanSixDigit());

            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            NonTagListPaymentView nv = (NonTagListPaymentView) view;
            transaction.setFreeData1(nv.getBit48());
            transaction.setFreeData3(nv.getBit62());

            transaction.setTranslationCode("024020001010010005");
        }
//
        public Boolean decompose(Object view, Transaction transaction) {
            NonTagListPaymentView nv = (NonTagListPaymentView) view;
            nv.setResponseCode(transaction.getResponseCode());
            nv.setReferenceNumber(transaction.getReferenceNumber());

            nv.setTraceNumber(transaction.getStanSixDigit());
            String informasiStruk = "";
            String capitalSentence = transaction.getFreeData2().toUpperCase();
            informasiStruk += transaction.getFreeData2();
            if (!capitalSentence.toUpperCase().contains("TOKEN")) {
               informasiStruk += nv.getUnitPhone();
                //atau ditaruh ditengah
                String[] words = informasiStruk.toUpperCase().split("ATAU");
                if (words.length > 1)
                {
                    informasiStruk = words[0] + "\nAtau\n" + words[1].trim();
                }
            }
            
            nv.setProviderReference(transaction.getFreeData1().substring(154, 186).trim());
    
            nv.setInformasiStruk(informasiStruk);
            loggingService.logActivity(nv.getCustomerId(), Constants.NONTAGLIST_PAYMENT_CODE, "Pembayaran Non Tagihan Listrik PLN, IDPEL/No Meter = " + nv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(nv.getAmount()) , nv.getReferenceNumber(), nv.getMerchantType(), nv.getTerminalId());
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
            NonTagListPaymentView nv = (NonTagListPaymentView) view;

            //-- switcher id
            String customData = "0000000";
            //-- registration number an(32) left justified padding with space
            customData += String.format("%1$-32s", nv.getCustomerReference());
            //-- area code & transaction code
            customData += "00000";

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("024030001010010005");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            NonTagListPaymentView nv = (NonTagListPaymentView) view;
            String bit48 = transaction.getFreeData1();
            String bit62 = transaction.getFreeData3();

            nv.setAmount(transaction.getTransactionAmount());
            nv.setFeeIndicator(transaction.getFeeIndicator());
            nv.setFee(transaction.getFee());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            nv.setAreaCode(bit48.substring(39, 41));
            nv.setTransactionCode(bit48.substring(41, 44));
            nv.setTransactionName(bit48.substring(44, 69).trim());
            try {
                nv.setRegistrationDate(sdf.parse(bit48.substring(69, 77)));
            } catch (ParseException e) {
                nv.setRegistrationDate(new Date());
            }
            try {
                nv.setExpiredDate(sdf.parse(bit48.substring(77, 85)));
            } catch (ParseException e) {
                nv.setExpiredDate(new Date());
            }
            nv.setSubscriberId(bit48.substring(85, 97));
            nv.setSubscriberName(bit48.substring(97, 122).trim());
            nv.setBillerReference(bit48.substring(122, 154).trim());
            nv.setProviderReference(bit48.substring(154, 186).trim());
            
            
            nv.setServiceUnit(bit48.substring(186, 191));
            nv.setUnitAddress(bit48.substring(191, 226).trim());
            nv.setUnitPhone(bit48.substring(226, 241).trim());

            nv.setBit48(bit48);
            nv.setBit62(bit62);

            nv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(nv.getCustomerId(), Constants.NONTAGLIST_PAYMENT_REP_CODE, "Reprint Pembayaran Non Tagihan Listrik PLN, IDPEL/No Meter = " + nv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(nv.getAmount()) , nv.getReferenceNumber(), nv.getMerchantType(), nv.getTerminalId());
            return Boolean.TRUE;
        }
    }
}
