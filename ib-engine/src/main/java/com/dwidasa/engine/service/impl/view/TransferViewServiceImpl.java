package com.dwidasa.engine.service.impl.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.TransferViewService;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/8/11
 * Time: 3:29 PM
 */
@Service("transferViewService")
public class TransferViewServiceImpl implements TransferViewService {
    private static Logger logger = Logger.getLogger( TransferViewServiceImpl.class );
    private final MessageCustomizer inquiryMessageCustomizer;
    private final MessageCustomizer transactionMessageCustomizer;
    private final MessageCustomizer reprintMessageCustomizer;

    public TransferViewServiceImpl() {
        inquiryMessageCustomizer = new InquiryMessageCustomizer();
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
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
            TransferView tv = (TransferView) view; 
            String customData = "";
            if (tv.getProviderCode() == null) tv.setProviderCode("");
            if (tv.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE) || tv.getProviderCode().equals(Constants.ALTO.PROVIDER_CODE) ) {
                if (tv.getCardData1() != null ) {
                    String bit35 = tv.getCardData1();
                    String[] words = bit35.split("=");
                    if (words.length > 1) {
                        transaction.setCardData1(words[0] + "=01019999");
                    }
                    transaction.setTranslationCode("1");
                } else {
                    transaction.setCardData2(Constants.ATMB.NON_CARD_52);
                    transaction.setTranslationCode("0");
                }
               
                transaction.setResponseCode(null);
                transaction.setFromAccountNumber("");
                String bit41 = transaction.getTerminalId();
                if (tv.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE)) {
                	bit41 = Constants.getTerminalIdAtmb(transaction.getTerminalId(), tv.getAccountNumber());	
                }
                transaction.setTerminalId(bit41);
                tv.setTerminalIdView(transaction.getTerminalId());
                transaction.setBit42(StringUtils.rightPad(bit41 + tv.getStanSixDigit(), 15));
                //System.out.println("HOHOHOHOHOHOHOHO "+transaction.getTerminalAddress());
                String bit43 = StringUtils.rightPad(transaction.getTerminalAddress(), 40);
                //String bit43 = StringUtils.rightPad(tv.getc, 40);
                if(tv.getProviderCode().equals(Constants.ALTO.PROVIDER_CODE)){
                	bit43 = StringUtils.rightPad(StringUtils.rightPad("BPRKS", 32, " ") + " JBR ID ", 40);
                }
                
                if (transaction.getTerminalId().substring(0, 3).equals("999")) {
                    bit43 = StringUtils.rightPad(StringUtils.rightPad("BPRKS", 32, " ") + " JBR ID ", 40);
                }
                //System.out.println("HOHOHOHOHOHOHOHO "+bit43);
                transaction.setBit43(bit43);
                
                //Destination Bank Code an3 -Rata kiri -Space padding
                customData += StringUtils.rightPad(tv.getBillerCode(), 3, " ");
                //Destination Account Number an19 -Rata kiri -Space padding
                customData += StringUtils.rightPad(tv.getCustomerReference(), 19, " ");
                //Amount n12 -Rata kanan -Zero leading padding -No decimal
                customData += StringUtils.leftPad(tv.getAmount().toPlainString(), 12, "0");
                //Customer Reff Number an16 -Rata kiri -Space padding
                customData += StringUtils.rightPad(tv.getCustRefAtmb() == null ? "" : tv.getCustRefAtmb(), 16, " ");
                transaction.setProviderCode(tv.getProviderCode());
                transaction.setFromAccountType("10");
            } else {    //treasury
                //-- beneficiary name(an30)
                customData += String.format("%1$-30s", " ");
                //-- to currency(an3)
                customData += String.format("%1$-3s", " ");
                transaction.setTranslationCode("01609");
            }
            transaction.setFreeData1(customData);
            transaction.setTransactionType(tv.getTransactionType());
        }
        public Boolean decompose(Object view, Transaction transaction) {
            TransferView tv = (TransferView) view;
            if (tv.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE) || tv.getProviderCode().equals(Constants.ALTO.PROVIDER_CODE)) {
                String bit48 = transaction.getFreeData1();
                //Destination Bank Code an3 -Rata kiri -Space padding
                tv.setBillerCode(bit48.substring(0, 3).trim());
                //Destination Bank Name an15 -Rata kiri -Space padding
                tv.setBillerName(bit48.substring(3, 18).trim());
                //Destination Account Number an19 -Rata kiri -Space padding
                tv.setCustomerReference(bit48.substring(18, 37).trim());
                //Destination Account Name an30 -Rata kiri -Space padding
                tv.setReceiverName(bit48.substring(37, 67).trim());
                //Separator an12 -Fill with Space -Rata kiri -Space padding --67+12=79
                //Customer Reff Number an16 -Rata kiri -Space padding
                tv.setCustRefAtmb(bit48.substring(79, 95).trim());
                //Issuer Bank Code an3 -Rata kiri -Space padding    --95+3 = 98
                //Issuer Bank Name an15 -Rata kiri -Space padding   --98+15 = 113
                //Issuer Account Number an19 -Rata kiri -Space padding
//                tv.setAccountNumber(bit48.substring(113, 132).trim());
                //Issuer Account Name an30 -Rata kiri -Space padding
//                tv.setSenderName(bit48.substring(132).trim());

                tv.setInquiryRefNumber(transaction.getReferenceNumber());
            } else {
            	//treasury receiverName sudah diset
            	if (!tv.getTransactionType().equals(Constants.TRANSFER_TREASURY_INQ_CODE) ) {
            		tv.setReceiverName(transaction.getFreeData1().substring(0, 30).trim());
            	}                 
                tv.setTraceNumber(transaction.getStanSixDigit());
                tv.setProviderCode(null);
            }
            tv.setReferenceNumber(transaction.getReferenceNumber());
            tv.setFee(transaction.getFee());

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
            TransferView tv = (TransferView) view;
            String customData = "";
            if (tv.getProviderCode() == null) tv.setProviderCode("");
            if (tv.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE) || tv.getProviderCode().equals(Constants.ALTO.PROVIDER_CODE) ) {
                if (tv.getCardData1() != null ) {
                    String bit35 = tv.getCardData1();
                    String[] words = bit35.split("=");
                    if (words.length > 1) {
                        transaction.setCardData1(words[0] + "=01019999");
                    }
                    transaction.setTranslationCode("1");
                } else {
                    transaction.setCardData2(Constants.ATMB.NON_CARD_52);
                    transaction.setTranslationCode("0");
                }
                
                transaction.setResponseCode(null);
                
                tv.setAccountNumber(tv.getAccountNumber().substring(tv.getAccountNumber().length() - 10));
                transaction.setFromAccountNumber(tv.getAccountNumber());
                String bit41 = transaction.getTerminalId();
                if (tv.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE)) {
                	bit41 = Constants.getTerminalIdAtmb(transaction.getTerminalId(), tv.getAccountNumber());	
                }
                transaction.setTerminalId(bit41);
                tv.setTerminalIdView(transaction.getTerminalId());
                transaction.setBit42(StringUtils.rightPad(bit41 + tv.getStanSixDigit(), 15));
                
                String bit43 = StringUtils.rightPad(transaction.getTerminalAddress(), 40);
                if(tv.getProviderCode().equals(Constants.ALTO.PROVIDER_CODE)){
                	bit43 = StringUtils.rightPad(StringUtils.rightPad("BPRKS", 32, " ") + " JBR ID ", 40);
                }
                if (transaction.getTerminalId().substring(0, 3).equals("999")) {
                	 bit43 = StringUtils.rightPad(StringUtils.rightPad("BPRKS", 32, " ") + " JBR ID ", 40);
                }
                //System.out.println("HOHOHOHOHOHOHOHO "+bit43);
                transaction.setBit43(bit43);

                //Destination Bank Code an3 -Rata kiri -Space padding
                customData += StringUtils.rightPad(tv.getBillerCode(), 3, " ");
                //Destination Bank Name an15 -Rata kiri -Space padding
                customData += StringUtils.rightPad(tv.getBillerName(), 15, " ");
                //Destination Account Number an19 -Rata kiri -Space padding
                customData += StringUtils.rightPad(tv.getCustomerReference(), 19, " ");
                //Destination Account Name an30 -Rata kiri -Space padding
                customData += StringUtils.rightPad((tv.getReceiverName() == null) ? " " : tv.getReceiverName(), 30, " ");
                //Separator an12 -Fill with Space -Rata kiri -Space padding
                customData += "            ";
                //Customer Reff Number an16 -Rata kiri -Space padding
                customData += StringUtils.rightPad(tv.getCustRefAtmb() == null ? "" : tv.getCustRefAtmb(), 16, " ");
                //Issuer Bank Code an3 -Rata kiri -Space padding
                customData += StringUtils.rightPad("688", 3, " ");
                //Issuer Bank Name an15 -Rata kiri -Space padding
                customData += StringUtils.rightPad(Constants.BPRKS.NAME, 15, " ");
                //Issuer Account Number an19 -Rata kiri -Space padding
                customData += StringUtils.rightPad(tv.getAccountNumber(), 19, " ");
                //Issuer Account Name an30 -Rata kiri -Space padding
                String issuerName = tv.getSenderName() == null ? " " : tv.getSenderName();
                customData += StringUtils.rightPad(issuerName, 30, " ");
                //Note an30 -Rata kiri-Space padding
                String note = (tv.getDescription() != null) ? tv.getDescription() : (tv.getNews() != null) ? tv.getNews() : "                              ";
                customData += StringUtils.rightPad(note, 30, " ");
                transaction.setProviderCode(tv.getProviderCode());
                transaction.setFromAccountType("10");
            } else {    //treasury
                //-- beneficiary name(an30)
                NumberFormat nf = new DecimalFormat("000000000000000");
//                if (tv.getTransactionType().equals(Constants.TRANSFER_CODE)){
                    //-- from currency amount(n15) right justified zero padding
                    customData += nf.format(tv.getAmount().longValue());
                    //-- to currency amount(n15) right justified zero padding
                    customData += nf.format(tv.getAmount().longValue());
//                }
//                else {
//                    // Constants.TRANSFER_OTHER_CODE
//                    if (tv.getTransactionType().equals(Constants.TRANSFER_TREASURY_CODE)){
//                        long total = tv.getAmount().longValue() + tv.getFee().longValue();
//                        //-- from currency amount(n15) right justified zero padding
//                        customData += nf.format(total);
//                        //-- to currency amount(n15) right justified zero padding
//                        customData += nf.format(total);
//                        tv.setDescription("TRANSFER KE " + tv.getBillerName() + " VIA TREASURY");
//                        //-- set transaction amount and fee for treasury purpose transaction message, must be switched back to as it is on view in decompose
//                        transaction.setTransactionAmount(new BigDecimal(total));
//                        transaction.setFee(BigDecimal.ZERO);
//                    }
//                    else {
//
//                        //-- from currency amount(n15) right justified zero padding
//                        customData += nf.format(tv.getAmount().longValue());
//                        //-- to currency amount(n15) right justified zero padding
//                        customData += nf.format(tv.getAmount().longValue());
//                    }
//                }
                transaction.setTranslationCode("01610");
                //-- transfer description(an30)
                customData += String.format("%1$-30s", tv.getDescription());
            }
            transaction.setFreeData1(customData);
            transaction.setValueDate(new Date());
            transaction.setTransactionType(tv.getTransactionType());
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TransferView tv = (TransferView) view;
            if (tv.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE) || tv.getProviderCode().equals(Constants.ALTO.PROVIDER_CODE) ) {
                String bit48 = transaction.getFreeData1();
                logger.info("bit48=" + bit48);
                //Destination Bank Code an3 -Rata kiri -Space padding
                tv.setBillerCode(bit48.substring(0, 3).trim());
                //Destination Bank Name an15 -Rata kiri -Space padding
                tv.setBillerName(bit48.substring(3, 18).trim());
                //Destination Account Number an19 -Rata kiri -Space padding
                tv.setCustomerReference(bit48.substring(18, 37).trim());
                //Destination Account Name an30 -Rata kiri -Space padding
                tv.setReceiverName(bit48.substring(37, 67).trim());
                //Separator an12 -Fill with Space -Rata kiri -Space padding --67+12=79
                //Customer Reff Number an16 -Rata kiri -Space padding
                tv.setCustRefAtmb(bit48.substring(79, 95).trim());
                //Issuer Bank Code an3 -Rata kiri -Space padding    --95+3 = 98
                //Issuer Bank Name an15 -Rata kiri -Space padding   --98+15 = 113
                //Issuer Account Number an19 -Rata kiri -Space padding
//                tv.setAccountNumber(bit48.substring(113, 132).trim()); sudah ambil dari bit 102
                //Issuer Account Name an30 -Rata kiri -Space padding
                tv.setSenderName(bit48.substring(132, 162).trim());
                //Note an30 -Rata kiri-Space padding
                //tv.setDescription(bit48.substring(162).trim());//
                tv.setNews(bit48.substring(162).trim());

                logger.info("tv.getCustRefAtmb()=" + tv.getCustRefAtmb());
                tv.setBit42(transaction.getBit42());
            }
//            if (tv.getTransactionType().equals(Constants.TRANSFER_TREASURY_CODE)){
//                //-- switch back transaction amount and fee as it is on view
//                transaction.setTransactionAmount(tv.getAmount());
//                transaction.setFee(tv.getFee());
//            }
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());

            tv.setTraceNumber(transaction.getStanSixDigit());
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
