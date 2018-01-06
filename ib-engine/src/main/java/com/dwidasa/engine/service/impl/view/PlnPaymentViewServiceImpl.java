package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.PlnPaymentViewService;
import com.dwidasa.engine.util.MoneyUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:03
 */
@Service("plnPaymentViewService")
public class PlnPaymentViewServiceImpl extends PaymentViewServiceImpl implements PlnPaymentViewService {
    private static Logger logger = Logger.getLogger(  PlnPaymentViewServiceImpl.class);
    @Autowired
    private LoggingService loggingService;
    
    public PlnPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            PlnPaymentView pv = (PlnPaymentView) view;
        	//-- customer id an(12) left justified padding with space
            String customData = String.format("%1$-12s", pv.getCustomerReference());

            transaction.setFreeData2(customData);
            transaction.setTranslationCode("00501");
            //set bit 4 = 0, karena kadang2 masih menyimpan nilai dr transaksi sebelumnya
            transaction.setTransactionAmount(BigDecimal.ZERO);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            PlnPaymentView pv = (PlnPaymentView) view;

            pv.setPenaltyFee(BigDecimal.ZERO);
            pv.setFee(BigDecimal.ZERO);
            pv.setAmount1(BigDecimal.ZERO);
            pv.setAmount2(BigDecimal.ZERO);
            pv.setAmount3(BigDecimal.ZERO);
            pv.setAmount4(BigDecimal.ZERO);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");

            String bit48 = transaction.getFreeData1();
            int numOfBill = Integer.valueOf(bit48.substring(0, 1));
            int numOfOutstanding = Integer.valueOf(bit48.substring(1, 3));
            pv.setOutstanding(numOfOutstanding - numOfBill);
            pv.setBillerReference(bit48.substring(3, 35).trim());
            pv.setReferenceName(bit48.substring(35, 60).trim());
            pv.setPowerCategory(bit48.substring(80, 84) + "/" + bit48.substring(84, 93));
            pv.setUnitCode(bit48.substring(60,65).trim());
            pv.setUnitPhone(bit48.substring(65,80).trim());

            int base = 102;
            int len = 111;
            pv.setBillPeriods(new ArrayList<Date>());

            for (int i = 0; i < numOfBill; i++) {
                if (i == 0) {
                    pv.setPreviousMeter(bit48.substring(base + i * len + 63, base + i * len + 63 + 8));
                }

                if (i == numOfBill-1) {
                    pv.setCurrentMeter(bit48.substring(base + i * len + 71, base + i * len + 71 + 8));
                    try {
                        pv.setMeterReadingDate(sdf1.parse(bit48.substring(base + i * len + 6, base + i * len + 6 + 8)));
                    } catch (ParseException e) {
                        pv.setMeterReadingDate(null);
                    }
                }

                try {
                    pv.getBillPeriods().add(sdf2.parse(bit48.substring(base + i * len, base + i * len + 6)));
                } catch (ParseException e) {
                    pv.getBillPeriods().add(null);
                }

                switch (i) {
                    case 0 :
                        pv.setAmount1(new BigDecimal(bit48.substring(base + i * len + 22, base + i * len + 22 + 11)));
                        break;
                    case 1 :
                        pv.setAmount2(new BigDecimal(bit48.substring(base + i * len + 22, base + i * len + 22 + 11)));
                        break;
                    case 2 :
                        pv.setAmount3(new BigDecimal(bit48.substring(base + i * len + 22, base + i * len + 22 + 11)));
                        break;
                    case 3 :
                        pv.setAmount4(new BigDecimal(bit48.substring(base + i * len + 22, base + i * len + 22 + 11)));
                        break;
                }

                pv.setPenaltyFee(pv.getPenaltyFee().add(
                        new BigDecimal(bit48.substring(base + i * len + 54, base + i * len + 54 + 9))));
            }

            pv.setAmount(transaction.getTransactionAmount());
            pv.setFeeIndicator(transaction.getFeeIndicator());
            logger.info("pv.getFee()=" + pv.getFee());
            logger.info("transaction.getFee()=" + transaction.getFee());
            pv.setFee(transaction.getFee());
            pv.setTotal(pv.getAmount().add(pv.getFee()));
            pv.setBit48(bit48);
            pv.setBit61(transaction.getFreeData2());

            pv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            PlnPaymentView pv = (PlnPaymentView) view;
            transaction.setFreeData1(pv.getBit48().substring(0, 1) +
                    pv.getBit48().substring(0, 1) + pv.getBit48().substring(1));
            transaction.setFreeData2(pv.getBit61());
            transaction.setTranslationCode("00502");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            PlnPaymentView pv = (PlnPaymentView) view;
            pv.setResponseCode(transaction.getResponseCode());
            pv.setReferenceNumber(transaction.getReferenceNumber());
            pv.setBit48(transaction.getFreeData1());    //bit48 trx beda dg bit48 inquiry
            pv.setUnitPhone(transaction.getFreeData1().substring(66, 81).trim());

            pv.setTraceNumber(transaction.getStanSixDigit());
            String informasiStruk = "";
            String capitalSentence = transaction.getTranslationCode().toUpperCase();
            informasiStruk += (transaction.getTranslationCode().length() > 4) ? transaction.getTranslationCode().substring(5) : "";
            if (!capitalSentence.toUpperCase().contains("TOKEN")) {
                //atau ditaruh ditengah
                String[] words = informasiStruk.toUpperCase().split("ATAU");
                if (words.length > 1)
                {
                	informasiStruk = words[0] ;
                	/*
                    informasiStruk = words[0] + "\nATAU\n" + words[1];
                    informasiStruk += " : ";
                    //1+1+2+32+25+5
                    informasiStruk += pv.getUnitPhone();
                    informasiStruk = informasiStruk.toUpperCase();
                    */
                }
            }
            informasiStruk += "\nInformasi Hubungi Call Center : 123";
            informasiStruk += "\nAtau Hub. PLN Terdekat : " + pv.getUnitPhone();
            pv.setInformasiStruk(informasiStruk);
            
            if (pv.getProviderCode().equals(Constants.GSP.PROVIDER_CODE)) {
            	pv.setGspRef(transaction.getFreeData5());
            }
            
            loggingService.logActivity(pv.getCustomerId(), Constants.PLN_PAYMENT_CODE, "Pembayaran Listrik PLN, IDPEL/No Meter = " + pv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(pv.getAmount()), pv.getReferenceNumber(), pv.getMerchantType(), pv.getTerminalId());
            
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
            PlnPaymentView pv = (PlnPaymentView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            transaction.setFreeData1("");

            String customData = "";
            customData += String.format("%1$-16s", pv.getCustomerReference());
            customData += sdf.format(pv.getBillPeriods().get(0));

            transaction.setFreeData2(customData);
            transaction.setTranslationCode("00503");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            PlnPaymentView pv = (PlnPaymentView) view;
            pv.setResponseCode(transaction.getResponseCode());
            pv.setReferenceNumber(transaction.getReferenceNumber());

            pv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(pv.getCustomerId(), Constants.PLN_PAYMENT_REP_CODE, "Reprint Pembayaran Listrik PLN, IDPEL/No Meter = " + pv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(pv.getAmount()), pv.getReferenceNumber(), pv.getMerchantType(), pv.getTerminalId());
            return Boolean.TRUE;
        }
    }
}
