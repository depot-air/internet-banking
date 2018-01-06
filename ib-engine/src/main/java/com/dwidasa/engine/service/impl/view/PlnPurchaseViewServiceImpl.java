package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.PlnPurchaseViewService;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/9/11
 * Time: 10:21 AM
 */
@Service("plnPurchaseViewService")
public class PlnPurchaseViewServiceImpl implements PlnPurchaseViewService {
	private Logger logger = Logger.getLogger(PlnPurchaseViewServiceImpl.class);
//    @Autowired
//    private LoggingService loggingService;
    
    private MessageCustomizer transactionMessageCustomizer;
    private MessageCustomizer inquiryMessageCustomizer;
    private MessageCustomizer reprintMessageCustomizer;

    public PlnPurchaseViewServiceImpl() {
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        inquiryMessageCustomizer = new InquiryMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        PlnPurchaseView pv = (PlnPurchaseView) view;
        pv.setToAccountType("00");
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validate(BaseView view) {
        Boolean result = view.validate();
        if (result) {
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
            PlnPurchaseView pv = (PlnPurchaseView) view;

            String customData = "";
            //-- GSP product code n(6)
            customData += "301422";
            //-- language n(1)
            customData += "0";
            //-- unique number n(15)
            customData += String.format("%1$-15s", pv.getCustomerReference());
            //-- buying option n(1)
            customData += "0";

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("022010001010010001");
            //set bit 4 = 0, karena kadang2 masih menyimpan nilai dr transaksi sebelumnya
            transaction.setTransactionAmount(BigDecimal.ZERO);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            PlnPurchaseView pv = (PlnPurchaseView) view;
            String bit48 = transaction.getFreeData1();

            pv.setReferenceName(bit48.substring(37, 62).trim());

            String bit62 = transaction.getFreeData3();

            pv.setMeterNumber(bit62.substring(0, 15).trim());
            pv.setCustomerReference(bit62.substring(15, 30).trim());
            pv.setPowerCategory(bit62.substring(55, 72).trim());
            pv.setUnsold1(EngineUtils.getBigDecimalValue(bit62.substring(72, 84), 2));
            pv.setUnsold2(EngineUtils.getBigDecimalValue(bit62.substring(84, 96), 2));
            pv.setFeeIndicator(transaction.getFeeIndicator());
            pv.setFee(transaction.getFee());

            pv.setBit48(bit48);
            pv.setBit62(bit62);

            pv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    /**
     * Class to compose and decompose message for execute phase
     */
    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        /**
         * {@inheritDoc}
         */
        public void compose(BaseView view, Transaction transaction) {
            PlnPurchaseView pv = (PlnPurchaseView) view;

//            Unsold token sudah ditambahkan Fee
//            if (pv.getBuyingOption().equals("1")) {
//                pv.setAmount(pv.getAmount().add(pv.getFee()));
//                transaction.setTransactionAmount(pv.getAmount());
//            }

            String bit48 = pv.getBit48().substring(0, 22) + pv.getBuyingOption() + pv.getBit48().substring(23, 62) +
                    StringUtils.leftPad(String.valueOf(pv.getAmount().longValue() + "00"), 12, "0") +
                    pv.getBit48().substring(74);

            transaction.setFreeData1(bit48);
            transaction.setFreeData3(pv.getBit62());
            transaction.setTranslationCode("022020001010010001");
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
            PlnPurchaseView pv = (PlnPurchaseView) view;
            pv.setResponseCode(transaction.getResponseCode());
            pv.setReferenceNumber(transaction.getReferenceNumber());

            String bit62 = transaction.getFreeData3();

            pv.setStamp(EngineUtils.getBigDecimalValue(bit62.substring(52, 64), 2));
            pv.setPpn(EngineUtils.getBigDecimalValue(bit62.substring(81, 93), 2));
            pv.setBillerReference(bit62.substring(93, 125).trim());
            pv.setPpj(EngineUtils.getBigDecimalValue(bit62.substring(125, 137), 2));
            pv.setInstallment(EngineUtils.getBigDecimalValue(bit62.substring(137, 149), 2));
            pv.setPaymentAmount(EngineUtils.getBigDecimalValue(bit62.substring(149, 161), 2));
            pv.setTokenAmount(EngineUtils.getBigDecimalValue(bit62.substring(161, 173), 2));
            pv.setKwh(EngineUtils.getBigDecimalValue(bit62.substring(173, 187), 2).toString().replace(".", ","));
            pv.setTokenNumber(bit62.substring(187, 207).trim());
            pv.setFeeIndicator(transaction.getFeeIndicator());
            pv.setFee(transaction.getFee());
            pv.setUpjPhoneNumber(bit62.substring(219, 234).trim());
            
            

            pv.setTraceNumber(transaction.getStanSixDigit());
            String informasiStruk = "";
            String capitalSentence = transaction.getTranslationCode().toUpperCase();
            informasiStruk += (transaction.getTranslationCode().length() >= 18) ? transaction.getTranslationCode().substring(18) : "";
            if (informasiStruk.length() > 1 && informasiStruk.substring(0,1).equals("\"")) {
                informasiStruk = informasiStruk.substring(1, informasiStruk.length() - 1);  //menghilangkan "
            }

            if (!capitalSentence.toUpperCase().contains("TOKEN")) {
                //atau ditaruh ditengah
                String[] words = informasiStruk.toUpperCase().split("ATAU");
            	if (words.length > 1) {
                    informasiStruk = words[0] + "\nAtau\n" + words[1].trim();
                }
                String lastChar = informasiStruk.substring(informasiStruk.length() -1);
                if (lastChar.equals(":")) {
                	informasiStruk += bit62.trim().substring(bit62.trim().length() - 3);
                } else {
                	informasiStruk += " : " + bit62.trim().substring(bit62.trim().length() - 3);                	
                }
            }
            pv.setInformasiStruk(informasiStruk);
            pv.setBit48(transaction.getFreeData1());
            pv.setBit62(bit62);
            
            if(Constants.GSP.PROVIDER_CODE.equals(pv.getProviderCode())){
            	pv.setGspRef(transaction.getFreeData1().substring(86, 86+32));
            }else{
            	pv.setGspRef(bit62.substring(86, 86+32));
            }
//            loggingService.logActivity(pv.getCustomerId(), Constants.PLN_PURCHASE_CODE, "Pembelian Listrik PLN, IDPEL/No Meter = " + pv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(pv.getAmount()), pv.getReferenceNumber(), pv.getMerchantType(), pv.getTerminalId());
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
            PlnPurchaseView pv = (PlnPurchaseView) view;

            if (pv.getBuyingOption().equals("1")) {
                pv.setAmount(pv.getAmount().add(pv.getFee()));
                transaction.setTransactionAmount(pv.getAmount());
            }

            String bit48 = pv.getBit48().substring(0, 22) + pv.getBuyingOption() + pv.getBit48().substring(23, 62) +
                    StringUtils.leftPad(String.valueOf(pv.getAmount().longValue() + "00"), 12, "0") +
                    pv.getBit48().substring(74);

            transaction.setFreeData1(bit48);
            transaction.setFreeData3(pv.getBit62());
            transaction.setTranslationCode("022030001010010001");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            PlnPurchaseView pv = (PlnPurchaseView) view;
            pv.setResponseCode(transaction.getResponseCode());
            pv.setReferenceNumber(transaction.getReferenceNumber());

            String bit62 = transaction.getFreeData3();
            logger.info("bit62=" + bit62);
            pv.setStamp(EngineUtils.getBigDecimalValue(bit62.substring(52, 64), 2));
            pv.setPpn(EngineUtils.getBigDecimalValue(bit62.substring(81, 93), 2));
            pv.setBillerReference(bit62.substring(93, 125).trim());
            pv.setPpj(EngineUtils.getBigDecimalValue(bit62.substring(125, 137), 2));
            pv.setInstallment(EngineUtils.getBigDecimalValue(bit62.substring(137, 149), 2));
            pv.setPaymentAmount(EngineUtils.getBigDecimalValue(bit62.substring(149, 161), 2));
            pv.setTokenAmount(EngineUtils.getBigDecimalValue(bit62.substring(161, 173), 2));
            pv.setKwh(EngineUtils.getBigDecimalValue(bit62.substring(173, 187), 2).toString().replace(".", ","));
            pv.setTokenNumber(bit62.substring(187, 207).trim());
            pv.setFeeIndicator(transaction.getFeeIndicator());
            pv.setFee(transaction.getFee());
            pv.setUpjPhoneNumber(bit62.substring(219, 234).trim());

            //String bit48 = transaction.getFreeData1();
            //pv.setGspRef(bit48.substring(86, 86+32));
            
            if(Constants.GSP.PROVIDER_CODE.equals(pv.getProviderCode())){
            	pv.setGspRef(transaction.getFreeData1().substring(86, 86+32));
            }else{
            	pv.setGspRef(bit62.substring(86, 86+32));
            }
            
            
            pv.setTraceNumber(transaction.getStanSixDigit());

            String informasiStruk = "";
            String capitalSentence = transaction.getTranslationCode().toUpperCase();
            informasiStruk += (transaction.getTranslationCode().length() >= 18) ? transaction.getTranslationCode().substring(18) : "";
            if (informasiStruk.length() > 1 && informasiStruk.substring(0,1).equals("\"")) {
                informasiStruk = informasiStruk.substring(1, informasiStruk.length() - 1);  //menghilangkan "
            }
            if (!capitalSentence.toUpperCase().contains("TOKEN")) {
                //atau ditaruh ditengah
                String[] words = informasiStruk.toUpperCase().split("ATAU");
                if (words.length > 1) {
                    informasiStruk = words[0] + "\nAtau\n" + words[1].trim();
                }
                String lastChar = informasiStruk.substring(informasiStruk.length() -1);
                if (lastChar.equals(":")) {
                	informasiStruk += bit62.trim().substring(bit62.trim().length() - 3);
                } else {
                	informasiStruk += " : " + bit62.trim().substring(bit62.trim().length() - 3);                	
                }
            }
            pv.setInformasiStruk(informasiStruk);
            logger.info("informasi struk=" + informasiStruk);
//            logger.info("loggingService=" + loggingService);
//            loggingService.logActivity(pv.getCustomerId(), Constants.PLN_PAYMENT_REP_CODE, "Reprint Pembelian Listrik PLN, IDPEL/No Meter = " + pv.getCustomerReference() + " sebesar "+ MoneyUtils.getMoney(pv.getAmount()), pv.getReferenceNumber(), pv.getMerchantType(), pv.getTerminalId());
            return Boolean.TRUE;
        }
    }
}
