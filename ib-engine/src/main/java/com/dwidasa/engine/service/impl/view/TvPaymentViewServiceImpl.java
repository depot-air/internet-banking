package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TvPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.TvPaymentViewService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.MoneyUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:44
 */
@Service("tvPaymentViewService")
public class TvPaymentViewServiceImpl extends PaymentViewServiceImpl implements TvPaymentViewService {
	
    @Autowired
    private ExtendedProperty extendedProperty;
    
    @Autowired
    private LoggingService loggingService;
    
    private boolean isIndovisionOkeTopTV(String billerCode) {
    	if (billerCode.equals(Constants.TV_CODE.INDOVISION) || billerCode.equals(Constants.TV_CODE.OKEVISION) || billerCode.equals(Constants.TV_CODE.TOPTV)) {
    		return true;
    	}
    	return false;
    }
    private boolean isTelkomvision(String billerCode) {
    	if (billerCode.equals(Constants.TELKOM_VISION)) {
    		return true;
    	}
    	return false;
    }
    public TvPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    @SuppressWarnings("unused")
	private void decomposeTelkomvision(Transaction transaction, TvPaymentView tv, String bit48) {
        //Kode Area n-4 -Rata kiri -Padding with space
        //Nomer telepon n-9 -Rata kiri -Padding with space
        tv.setCustomerReference(tv.getBit48().substring(0,13));
        //Divre Code an-2 -Rata kiri -Padding with space
        //Datel Code an-4 -Rata kiri -Padding with space
        //Bill Number n-1
        int n = Integer.parseInt(bit48.substring(19, 20));
        BigDecimal totAmount = BigDecimal.ZERO;
        int index = 0;
        //These fields below can be repeated for value of Bill Number (max: 3 reps) 1 + 2 + 8 + 12 =23
        for(int i = 0; i < n; i++)
        {
            //Ref year an-1 -Rata kiri -Padding with space

            //Ref month an-2 -Rata kiri -Padding with space

            //Ref bill number an-8 -Rata kiri -Padding with space

            //Bill Amount n-12 -Right kanan -Padding with zero End
            totAmount = totAmount.add(BigDecimal.valueOf(Double.parseDouble(bit48.substring(31 + (i * 23), 43 + (i * 23)))));
            index = 43 + (i + 23);
        }
        tv.setAmount(totAmount);
        tv.setFee(transaction.getFee());
        //Customer Name an-30 -Rata kiri -Padding with space
        tv.setReferenceName(bit48.substring(index, index + 30));
        //NPWP an-15 -Rata kiri -Padding with space
    }
    @SuppressWarnings("unused")
	private void decomposeTV(Transaction transaction, TvPaymentView tv, String bit48) {
            //-- biller code n(5)
            tv.setBillerCode(bit48.substring(0, 5).trim());
            //-- payee id n(4)
            tv.setProviderCode(bit48.substring(5, 9).trim());
            //-- product id n(4)
            tv.setProductCode(bit48.substring(9, 13).trim());
            //-- customer reference an(13)
            tv.setCustomerReference(bit48.substring(13, 26).trim());
            //-- Nama Customer an-30 -Rata kiri -Padding with space -Blank on Inquiry & reprint request
            tv.setReferenceName(bit48.substring(26, 56).trim());
            //--Due Date n-8 Format:ddMMyyyy    -Fill with 00000000 if not available

            //--Bill Reference an-10 -Rata kiri -Padding with space -Blank on Inquiry & reprint request

            //--Start Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available

            //--End Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            //56 + 8 + 10 + 8 + 8 = 90
            //--Amount an-12 -Rata kanan -Padding with zero -Fill with 000000000000 on inquiry  request & reprint request
            tv.setAmount(EngineUtils.getBigDecimalValue(bit48.substring(90, 102), 2));
            //--Admin Fee an-12 -Rata kanan -Padding with zero -Fill with 000000000000 on inquiry request & reprint request
            tv.setFee(EngineUtils.getBigDecimalValue(bit48.substring(102, 114), 2));

            tv.setTotal(tv.getAmount().add(tv.getFee()));
            tv.setFeeIndicator(transaction.getFeeIndicator());
        }
    @SuppressWarnings("unused")
	private void composeTelkomvision(Transaction transaction, TvPaymentView tv) {
        //set bit 48
        String customData = "";
        //Kode Area n-4 -Rata kiri -Padding with space
        //Nomer telepon n-9 -Rata kiri -Padding with space
        customData += StringUtils.rightPad(tv.getCustomerReference(), 13, " ");
        //Divre Code an-2 -Rata kiri -Padding with space
        customData += StringUtils.rightPad("", 2, " ");
        //Datel Code an-4 -Rata kiri -Padding with space
        customData += StringUtils.rightPad("", 4, " ");
        //Bill Number n-1
        customData += "1";
        //These fields below can be repeated for value of Bill Number (max: 3 reps) 1 + 2 + 8 + 12 =23
        //Ref year an-1 -Rata kiri -Padding with space
        customData += StringUtils.rightPad("", 1, " ");
        //Ref month an-2 -Rata kiri -Padding with space
        customData += StringUtils.rightPad("", 2, " ");
        //Ref bill number an-8 -Rata kiri -Padding with space
        customData += StringUtils.rightPad("", 8, " ");
        //Bill Amount n-12 -Right kanan -Padding with zero End
        customData += StringUtils.leftPad(tv.getAmount().stripTrailingZeros().toPlainString(), 12, "0");

        //Customer Name an-30 -Rata kiri -Padding with space
        customData += StringUtils.rightPad(tv.getReferenceName(), 30, " ");
        //NPWP an-15 -Rata kiri -Padding with space
    }
    @SuppressWarnings("unused")
	private void composeTV(Transaction transaction, TvPaymentView tv) {
            //set bit 48
            String customData = "";
            //-- biller code n(5)
            customData += StringUtils.leftPad(tv.getBillerCode(), 5, "0");
            //-- payee id n(4)
            customData += StringUtils.leftPad(tv.getProviderCode(), 4, "0");
            //-- product id n(4)
            customData += StringUtils.leftPad(tv.getProductCode(), 4, "0");
            //-- customer reference an(13)
            customData += String.format("%1$-13s", tv.getCustomerReference());
            //-- Nama Customer an-30 -Rata kiri -Padding with space -Blank on Inquiry & reprint request
            customData += StringUtils.leftPad("", 30, " ");
            //--Due Date n-8 Format:ddMMyyyy    -Fill with 00000000 if not available
            customData += "00000000";
            //--Bill Reference an-10 -Rata kiri -Padding with space -Blank on Inquiry & reprint request
            customData += StringUtils.leftPad("", 10, " ");
            //--Start Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            customData += "00000000";
            //--End Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            customData += "00000000";
            //--Amount an-12 -Rata kanan -Padding with zero -Fill with 000000000000 on inquiry  request & reprint request
            customData += "000000000000";
            //--Admin Fee an-12 -Rata kanan -Padding with zero -Fill with 000000000000 on inquiry request & reprint request
            customData += "000000000000";
            transaction.setFreeData1(customData);
        }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            TvPaymentView tv = (TvPaymentView) view;

            if (tv.getBillerCode().equals(Constants.CENTRIN_TV))  {
                String customData = "";

                //-- biller code n(5)
                customData += "07" + tv.getBillerCode();
                //-- payee id n(4)
                customData += StringUtils.leftPad(tv.getProviderCode(), 4, "0");
                //-- product id n(4)
                customData += StringUtils.leftPad(tv.getProductCode(), 4, "0");
                //-- customer reference an(13)
                customData += String.format("%1$-13s", tv.getCustomerReference());

                transaction.setFreeData1(customData);

                String translationCode = "02101";
                translationCode += StringUtils.leftPad(tv.getProviderCode(), 4, "0");
                translationCode += "07";
                translationCode += StringUtils.leftPad(tv.getBillerCode(), 3, "0");
                translationCode += StringUtils.leftPad(tv.getProductCode(), 4, "0");

                transaction.setTranslationCode(translationCode);
                return;
            }

            //-- customer id an(13) padding with zero
            String customData = StringUtils.leftPad(tv.getCustomerReference(), 13, "0");
            transaction.setFreeData2(customData);

            if (isTelkomvision(tv.getBillerCode())) {
                transaction.setTranslationCode("0111101001001");
            }
            else {
                transaction.setTranslationCode(Constants.TRANSLATION_CODE.TV_INQUIRY);
                if (extendedProperty.isMigration() && isIndovisionOkeTopTV(tv.getBillerCode())) {
                	transaction.setTranslationCode(Constants.TRANSLATION_CODE.TV_INQUIRY_INDOVISION_OKE_TOPTV);
                }
                
            }
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TvPaymentView tv = (TvPaymentView) view;

            String bit48 = transaction.getFreeData1();
            if (tv.getBillerCode().equals(Constants.CENTRIN_TV)) {
                tv.setReferenceName(bit48.substring(26, 56).trim());
            }
            else if (tv.getBillerCode().equals(Constants.TELKOM_VISION)){
                int numOfBill = Integer.valueOf(bit48.substring(19, 20));
                if(numOfBill == 0){
                	throw new BusinessException("IB-1009","91");
                }
                
                int base = 20;
                int len = 23;
                tv.setReferenceName(bit48.substring(base + len * numOfBill, base + len * numOfBill + 30).trim());
            }
            else {
                tv.setReferenceName(bit48.substring(13, 43).trim());
            }
            tv.setAmount(transaction.getTransactionAmount());
            tv.setFeeIndicator(transaction.getFeeIndicator());
            tv.setFee(transaction.getFee());
            tv.setTotal(tv.getAmount().add(tv.getFee()));
            tv.setBit48(transaction.getFreeData1());

            tv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            TvPaymentView tv = (TvPaymentView) view;
            transaction.setFreeData1(tv.getBit48());

            if (tv.getBillerCode().equals(Constants.CENTRIN_TV)) {
                String translationCode = "02102";
                translationCode += StringUtils.leftPad(tv.getProviderCode(), 4, "0");
                translationCode += "07";
                translationCode += StringUtils.leftPad(tv.getBillerCode(), 3, "0");
                translationCode += StringUtils.leftPad(tv.getProductCode(), 4, "0");

                transaction.setTranslationCode(translationCode);
            }
            else {
                //-- customer id an(13) padding with zero
                String customData = StringUtils.leftPad(tv.getCustomerReference(), 13, "0");
                transaction.setFreeData2(customData);

                if (isTelkomvision(tv.getBillerCode())) {
                    transaction.setTranslationCode("0110201001001");
                }
                else {
                	transaction.setTranslationCode(Constants.TRANSLATION_CODE.TV_POSTING);
                    if (extendedProperty.isMigration() && isIndovisionOkeTopTV(tv.getBillerCode())) {
                    	transaction.setTranslationCode(Constants.TRANSLATION_CODE.TV_POSTING_INDOVISION_OKE_TOPTV);
                    }
                }
            }
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TvPaymentView tv = (TvPaymentView) view;
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());

            tv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(tv.getCustomerId(), Constants.ENTERTAINMENT_PAYMENT_CODE, "Pembayaran TV, ID Pelanggan = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()), tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
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
            TvPaymentView tv = (TvPaymentView) view;

            if (tv.getBillerCode().equals(Constants.CENTRIN_TV))  {
                String customData = "";

                //-- biller code n(5)
                customData += "07" + tv.getBillerCode();
                //-- payee id n(4)
                customData += StringUtils.leftPad(tv.getProviderCode(), 4, "0");
                //-- product id n(4)
                customData += StringUtils.leftPad(tv.getProductCode(), 4, "0");
                //-- customer reference an(13)
                customData += String.format("%1$-13s", tv.getCustomerReference());
                transaction.setFreeData1(customData);

                String translationCode = "02103";
                translationCode += StringUtils.leftPad(tv.getProviderCode(), 4, "0");
                translationCode += "07";
                translationCode += StringUtils.leftPad(tv.getBillerCode(), 3, "0");
                translationCode += StringUtils.leftPad(tv.getProductCode(), 4, "0");

                transaction.setTranslationCode(translationCode);
                return;
            }

            //-- customer id an(13) padding with zero
            String customData = StringUtils.leftPad(tv.getCustomerReference(), 13, "0");
            transaction.setFreeData2(customData);

            if (isTelkomvision(tv.getBillerCode())) {
                transaction.setTranslationCode(Constants.TRANSLATION_TELKOM_REPRINT);
            }
            else {
            	transaction.setTranslationCode(Constants.TRANSLATION_CODE.TV_REPRINT);
                if (extendedProperty.isMigration() && isIndovisionOkeTopTV(tv.getBillerCode())) {
                	transaction.setTranslationCode(Constants.TRANSLATION_CODE.TV_REPRINT_INDOVISION_OKE_TOPTV);
                }
            }

            transaction.setFreeData1(transaction.getFreeData2());
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TvPaymentView tv = (TvPaymentView) view;
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());

            tv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(tv.getCustomerId(), Constants.ENTERTAINMENT_PAYMENT_REP_CODE, "Pembayaran TV, ID Pelanggan = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()), tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
            return Boolean.TRUE;
        }
    }
}
