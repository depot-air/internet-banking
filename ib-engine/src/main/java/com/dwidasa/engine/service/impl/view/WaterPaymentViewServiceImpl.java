package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.WaterPaymentView;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.WaterPaymentViewService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:44
 */
@Service("waterPaymentViewService")
public class WaterPaymentViewServiceImpl extends PaymentViewServiceImpl implements WaterPaymentViewService{
    private static Logger logger = Logger.getLogger( WaterPaymentViewServiceImpl.class );
    public WaterPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
        	logger.info("view=" + view);
            WaterPaymentView pv = (WaterPaymentView) view;
            //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding;
            String customData = Constants.INDUSTRY_CODE.UTILITY_WATER + Constants.WATER.BILLER_ID;	//pv.getBillerCode();
            //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;
            customData += pv.getProviderCode();
            //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding;
            customData += pv.getProductCode();
            //Kode Pelanggan an-15 -Rata Kiri -Padding with Space
            customData += StringUtils.rightPad(pv.getCustomerReference(), 16, " ");
            transaction.setFreeData1(customData);
            transaction.setResponseCode(null);
/*
Project ID + Kode Routing + Payee Id + Industry Code + Biller Code + Product ID
*/
            String translationCode = Constants.WATER.PROJECT_ID + Constants.WATER.TRANSLATION_CODE.INQUIRY +
                    pv.getProviderCode() + Constants.INDUSTRY_CODE.UTILITY_WATER + Constants.WATER.BILLER_ID + pv.getProductCode();
            transaction.setTranslationCode(translationCode);
            
        }

        public Boolean decompose(Object view, Transaction transaction) {
            WaterPaymentView pv = (WaterPaymentView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	//bukan ddMMyyyy
            String bit48 = transaction.getFreeData1();

            //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding;

            //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;

            //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding;

            //Kode Pelanggan an-15 -Rata Kiri -Padding with Space

            //Kode Area an-10 -Rata Kiri -Padding with Space
            // 5 + 4 + 4 + 15 + 10
            pv.setAreaCode(bit48.substring(28, 38).trim());
            //Nama Customer an-30 -Rata kiri -Padding with space
            pv.setReferenceName(bit48.substring(38, 68).trim());
            //Due Date n-8 Format:ddMMyyyy Fill with 00000000 if not available
            pv.setDueDateDdMMyyyy(bit48.substring(68, 76).trim());
            if (!pv.getDueDateDdMMyyyy().equals("00000000")) {
                try {
                    pv.setDueDate(sdf.parse(pv.getDueDateDdMMyyyy()));
                } catch (ParseException e) {
                    logger.info(e.getMessage());
                }
            }
            //Bill Reference an-15 -Bill ID dari Palyja -Rata kiri -Padding with space
            pv.setBillReference(bit48.substring(76, 91));
            //Start Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available -> yyyyMMdd
            pv.setStartServiceDdMMyyyy(bit48.substring(91, 99).trim());
            if (!pv.getStartServiceDdMMyyyy().equals("00000000")) {
                try {
                    pv.setStartService(sdf.parse(pv.getStartServiceDdMMyyyy()));
                } catch (ParseException e) {
                    logger.info(e.getMessage());
                }
            }
            //End Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            pv.setEndServiceDdMMyyyy(bit48.substring(99, 107).trim());
            if (!pv.getEndServiceDdMMyyyy().equals("00000000")) {
                try {
                    pv.setEndService(sdf.parse(pv.getEndServiceDdMMyyyy()));
                } catch (ParseException e) {
                    logger.info(e.getMessage());
                }
            }
            //Bill Date n-8 Format: ddMMyyyy Fill with 00000000 if not available
            String billDate;
            if(Constants.WATER.PRODUCT_CODE.PAM_SURABAYA.equals(pv.getProductCode())){
            	//pv.setJmlTagihan(new BigDecimal(bit48.substring(106, 108)));
            	billDate = bit48.substring(109, 115).trim()+"00";
            }else{
            	billDate = bit48.substring(107, 115).trim();
            }
            pv.setBillDateYyyyMMdd(billDate);
            if (!pv.getBillDateYyyyMMdd().equals("00000000")) {
                try {
                	if (pv.getBillDateYyyyMMdd().substring(6).equals("00")) {
                		pv.setBillDateYyyyMMdd( pv.getBillDateYyyyMMdd().substring(0, 6) + "01");
                	}
                    pv.setBillDate(sdf.parse(pv.getBillDateYyyyMMdd()));
                } catch (ParseException e) {
                    logger.info(e.getMessage());
                }
            }
            //Amount an-12 -Rata kanan -Padding with zero -No decimal
            BigDecimal amount;
            try {
            	amount = new BigDecimal(bit48.substring(115, 127));
			} catch (Exception e) {
				amount = BigDecimal.ZERO;// TODO: handle exception
			}
            
            
            BigDecimal penalty;
            try {
//            	if(Constants.WATER.PRODUCT_CODE.PAM_SEMARANG.equals(pv.getProductCode()) || 
//            			Constants.WATER.PRODUCT_CODE.PAM_SURABAYA.equals(pv.getProductCode())){
//            		penalty = new BigDecimal(bit48.substring(135, 139));
//            	}else 
            		penalty = new BigDecimal(bit48.substring(139, 151));
			} catch (Exception e) {
				penalty = BigDecimal.ZERO;// TODO: handle exception
			}
            
            //Provider Fee an-12 -Rata kanan -Padding with zero -No Decimals
//            pv.setFee(new BigDecimal(bit48.substring(127, 139)));
            pv.setFee(transaction.getFee());
            //Penalty an-12 -Rata kanan -Padding with zero -No decimal
            pv.setPenalty(penalty);
            pv.setAmount(amount);
            //Reserved1 an-30 -Berisi Tariff Code dari Palyja -Rata Kiri -Padding with Space
            pv.setReserved1(bit48.substring(151, 181).trim());
            //Reserved2 an-30 -Reserved -Fill with blank -Rata Kiri -Padding with Space
            pv.setReserved2(bit48.substring(181).trim());

            BigDecimal total = null;
            if(Constants.WATER.PRODUCT_CODE.PAM_SURABAYA.equals(pv.getProductCode())){
            	BigDecimal retribusi = new BigDecimal(pv.getReserved2());
            	total = pv.getAmount().add(pv.getFee()).add(pv.getPenalty()).add(retribusi);
            }else{
            	total = pv.getAmount().add(pv.getFee()).add(pv.getPenalty());
            }
            
            pv.setTotal(total);

            pv.setTraceNumber(transaction.getStanSixDigit());

            if (pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PALYJA)) {
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PALYJA);
            } else if (pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.AETRA)) {
            	pv.setBillerName(Constants.WATER.BILLER_NAME.AETRA);
            }else 
            	if(pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAM_BILLER)){ 
            
            if(pv.getProductCode().equals(Constants.WATER.PRODUCT_CODE.PAM_SURABAYA)){
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PAMSurabaya);
            }else if(pv.getProductCode().equals(Constants.WATER.PRODUCT_CODE.PAM_SEMARANG)){
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PAMSemarang);
            }
            
            }
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            WaterPaymentView pv = (WaterPaymentView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	//bukan ddMMyyyy

            //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding;
            String customData = Constants.INDUSTRY_CODE.UTILITY_WATER + Constants.WATER.BILLER_ID;	//pv.getBillerCode();
            //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;
            customData += pv.getProviderCode();
            //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding;
            customData += pv.getProductCode();
            //Kode Pelanggan an-15 -Rata Kiri -Padding with Space
            customData += StringUtils.rightPad(pv.getCustomerReference(), 15, " ");
            //Kode Area an-10 -Rata Kiri -Padding with Space
            customData += StringUtils.rightPad(pv.getAreaCode(), 10, " ");
            //Nama Customer an-30 -Rata kiri -Padding with space
            customData += StringUtils.rightPad(pv.getReferenceName(), 30, " ");
            //Due Date n-8 Format:ddMMyyyy Fill with 00000000 if not available
            customData += pv.getDueDateDdMMyyyy();
            //Bill Reference an-15 -Bill ID dari Palyja -Rata kiri -Padding with space
            customData += StringUtils.rightPad(pv.getBillReference(), 15, " ");
            //Start Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            customData += pv.getStartServiceDdMMyyyy();
            //End Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            customData += pv.getEndServiceDdMMyyyy();
            //Bill Date n-8 Format: ddMMyyyy Fill with 00000000 if not available
            customData += sdf.format(pv.getBillDate()); //pv.getBillDateYyyyMMdd();
            //Amount an-12 -Rata kanan -Padding with zero -No decimal
            customData += StringUtils.leftPad((pv.getAmount() != null) ? (pv.getAmount()).toPlainString() : "000000000000", 12, "0");
            //Provider Fee an-12 -Rata kanan -Padding with zero -No Decimals
            customData += StringUtils.leftPad((pv.getFee() != null) ? pv.getFee().toPlainString() : "000000000000", 12, "0");
            //Penalty an-12 -Rata kanan -Padding with zero -No decimal
            customData += StringUtils.leftPad((pv.getPenalty() != null) ? pv.getPenalty().toPlainString() : "000000000000", 12, "0");
            //Reserved1 an-30 -Berisi Tariff Code dari Palyja -Rata Kiri -Padding with Space
            customData += StringUtils.rightPad((pv.getReserved1() != null) ? pv.getReserved1() : " ", 30, " ");
            //Reserved2 an-30 -Reserved -Fill with blank -Rata Kiri -Padding with Space
            customData += StringUtils.rightPad((pv.getReserved2() != null) ? pv.getReserved2() : " ", 30, " ");

            transaction.setFreeData1(customData);
            transaction.setResponseCode(null);
            transaction.setTransactionAmount(pv.getAmount().add(pv.getPenalty()));		//yg dikirim ke core bit4 = amount + penalty            
            String translationCode = Constants.WATER.PROJECT_ID + Constants.WATER.TRANSLATION_CODE.POSTING +
                    pv.getProviderCode() + Constants.INDUSTRY_CODE.UTILITY_WATER + Constants.WATER.BILLER_ID + pv.getProductCode();
            transaction.setTranslationCode(translationCode);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            WaterPaymentView pv = (WaterPaymentView) view;
            pv.setResponseCode(transaction.getResponseCode());
            pv.setReferenceNumber(transaction.getReferenceNumber());

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            String bit48 = transaction.getFreeData1();

            //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding;

            //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;

            //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding;

            //Kode Pelanggan an-15 -Rata Kiri -Padding with Space

            //Kode Area an-10 -Rata Kiri -Padding with Space
            // 5 + 4 + 4 + 15 + 10
            pv.setAreaCode(bit48.substring(28, 38).trim());
            //Nama Customer an-30 -Rata kiri -Padding with space
            pv.setReferenceName(bit48.substring(38, 68).trim());
            //Due Date n-8 Format:ddMMyyyy Fill with 00000000 if not available
            pv.setDueDateDdMMyyyy(bit48.substring(68, 76).trim());
            if (!pv.getDueDateDdMMyyyy().equals("00000000")) {
                try {
                    pv.setDueDate(sdf.parse(pv.getDueDateDdMMyyyy()));
                } catch (ParseException e) {
                    logger.info(e.getMessage());
                }
            }
            //Bill Reference an-15 -Bill ID dari Palyja -Rata kiri -Padding with space
            pv.setBillReference(bit48.substring(76, 91));
//            //Start Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
//            pv.setStartServiceDdMMyyyy(bit48.substring(91, 99).trim());
//            if (!pv.getStartServiceDdMMyyyy().equals("00000000")) {
//                try {
//                    pv.setStartService(sdf.parse(pv.getStartServiceDdMMyyyy()));
//                } catch (ParseException e) {
//                    logger.info(e.getMessage());
//                }
//            }
//            //End Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
//            pv.setEndServiceDdMMyyyy(bit48.substring(99, 107).trim());
//            if (!pv.getEndServiceDdMMyyyy().equals("00000000")) {
//                try {
//                    pv.setEndService(sdf.parse(pv.getEndServiceDdMMyyyy()));
//                } catch (ParseException e) {
//                    logger.info(e.getMessage());
//                }
//            }
////Bill Date n-8 Format: ddMMyyyy Fill with 00000000 if not available
//            pv.setBillDateDdMMyyyy(bit48.substring(107, 115).trim());
//            if (!pv.getBillDateDdMMyyyy().equals("00000000")) {
//                try {                	
//                    pv.setBillDate(sdf.parse(pv.getBillDateDdMMyyyy()));
//                } catch (ParseException e) {
//                    logger.info(e.getMessage());
//                }
//            }
            
            //Amount an-12 -Rata kanan -Padding with zero -No decimal
            BigDecimal amount = new BigDecimal(bit48.substring(115, 127));
            BigDecimal penalty = new BigDecimal(bit48.substring(139, 151));
            //Provider Fee an-12 -Rata kanan -Padding with zero -No Decimals
//            pv.setFee(new BigDecimal(bit48.substring(127, 139)));S
            //Penalty an-12 -Rata kanan -Padding with zero -No decimal
            pv.setPenalty(penalty);
            pv.setAmount(amount);
            
            if(Constants.WATER.PRODUCT_CODE.PAM_SURABAYA.equals(pv.getProductCode())){
            	
            	pv.setReserved1(bit48.substring(151, 168));
            	
            }else{
            //Reserved1 an-30 -Berisi Tariff Code dari Palyja -Rata Kiri -Padding with Space
            pv.setReserved1(bit48.substring(151, 181).trim());
            //Reserved2 an-30 -Reserved -Fill with blank -Rata Kiri -Padding with Space
            pv.setReserved1(bit48.substring(181).trim());
            }
            
            //BigDecimal total = pv.getAmount().add(pv.getFee()).add(pv.getPenalty());
            
            
            BigDecimal total = null;
            if(Constants.WATER.PRODUCT_CODE.PAM_SURABAYA.equals(pv.getProductCode())){
            	BigDecimal retribusi = new BigDecimal(pv.getReserved2());
            	total = pv.getAmount().add(pv.getFee()).add(pv.getPenalty()).add(retribusi);
            }else{
            	total = pv.getAmount().add(pv.getFee()).add(pv.getPenalty());
            }
            pv.setTotal(total);
            
            pv.setTraceNumber(transaction.getStanSixDigit());

            if (pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PALYJA)) {
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PALYJA);
            } else if (pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.AETRA)) {
            	pv.setBillerName(Constants.WATER.BILLER_NAME.AETRA);
            }else 
            	if(pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAM_BILLER)){ 
            
            if(pv.getProductCode().equals(Constants.WATER.PRODUCT_CODE.PAM_SURABAYA)){
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PAMSurabaya);
            }else if(pv.getProductCode().equals(Constants.WATER.PRODUCT_CODE.PAM_SEMARANG)){
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PAMSemarang);
            }
            
            }
            
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
            WaterPaymentView  pv = (WaterPaymentView) view;

            //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding;
            String customData = Constants.INDUSTRY_CODE.UTILITY_WATER + Constants.WATER.BILLER_ID;	//pv.getBillerCode();
            //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;
            customData += pv.getProviderCode();
            //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding;
            customData += pv.getProductCode();
            //Kode Pelanggan an-15 -Rata Kiri -Padding with Space
            customData += StringUtils.rightPad(pv.getCustomerReference(), 16, " ");
            transaction.setFreeData1(customData);

/*
Project ID + Kode Routing + Payee Id + Industry Code + Biller Code + Product ID
*/
            String translationCode = Constants.WATER.PROJECT_ID + Constants.WATER.TRANSLATION_CODE.REPRINT +
                    pv.getProviderCode() + Constants.INDUSTRY_CODE.UTILITY_WATER + Constants.WATER.BILLER_ID + pv.getProductCode();
            transaction.setTranslationCode(translationCode);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            WaterPaymentView pv = (WaterPaymentView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String bit48 = transaction.getFreeData1();

            //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding;

            //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;

            //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding;

            //Kode Pelanggan an-15 -Rata Kiri -Padding with Space

            //Kode Area an-10 -Rata Kiri -Padding with Space
            // 5 + 4 + 4 + 15 + 10
            pv.setAreaCode(bit48.substring(28, 38).trim());
            //Nama Customer an-30 -Rata kiri -Padding with space
            pv.setReferenceName(bit48.substring(38, 68).trim());
            //Due Date n-8 Format:ddMMyyyy Fill with 00000000 if not available
            pv.setDueDateDdMMyyyy(bit48.substring(68, 76).trim());
            if (!pv.getDueDateDdMMyyyy().equals("00000000")) {
                try {
                    pv.setDueDate(sdf.parse(pv.getDueDateDdMMyyyy()));
                } catch (ParseException e) {
                    //logger.info(e.getMessage());
                }
            }
            //Bill Reference an-15 -Bill ID dari Palyja -Rata kiri -Padding with space
            pv.setBillReference(bit48.substring(76, 91));
            //Start Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            pv.setStartServiceDdMMyyyy(bit48.substring(91, 99).trim());
            if (!pv.getStartServiceDdMMyyyy().equals("00000000")) {
                try {
                    pv.setStartService(sdf.parse(pv.getStartServiceDdMMyyyy()));
                } catch (ParseException e) {
                    //logger.info(e.getMessage());
                }
            }
            //End Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            pv.setEndServiceDdMMyyyy(bit48.substring(99, 107).trim());
            if (!pv.getEndServiceDdMMyyyy().equals("00000000")) {
                try {
                    pv.setEndService(sdf.parse(pv.getEndServiceDdMMyyyy()));
                } catch (ParseException e) {
                    //logger.info(e.getMessage());
                }
            }
//Bill Date n-8 Format: ddMMyyyy Fill with 00000000 if not available
            
            pv.setBillDateYyyyMMdd(bit48.substring(107, 115).trim());
            if (!pv.getBillDateYyyyMMdd().equals("00000000")) {
                try {
                    pv.setBillDate(sdf.parse(pv.getBillDateYyyyMMdd()));
                } catch (ParseException e) {
                    //logger.info(e.getMessage());
                }
            }
            //Amount an-12 -Rata kanan -Padding with zero -No decimal
            pv.setAmount(new BigDecimal(bit48.substring(115, 127)));
            //Provider Fee an-12 -Rata kanan -Padding with zero -No Decimals
            pv.setFee(new BigDecimal(bit48.substring(127, 139)));
            //Penalty an-12 -Rata kanan -Padding with zero -No decimal
            pv.setPenalty(new BigDecimal(bit48.substring(139, 151)));
            //Reserved1 an-30 -Berisi Tariff Code dari Palyja -Rata Kiri -Padding with Space
            pv.setReserved1(bit48.substring(151, 181).trim());
            //Reserved2 an-30 -Reserved -Fill with blank -Rata Kiri -Padding with Space
            pv.setReserved1(bit48.substring(181).trim());

            //BigDecimal total = pv.getAmount().add(pv.getFee().add(pv.getPenalty()));
            
            BigDecimal total = null;
            if(Constants.WATER.PRODUCT_CODE.PAM_SURABAYA.equals(pv.getProductCode())){
            	BigDecimal retribusi = new BigDecimal(pv.getReserved2());
            	total = pv.getAmount().add(pv.getFee()).add(pv.getPenalty()).add(retribusi);
            }else{
            	total = pv.getAmount().add(pv.getFee()).add(pv.getPenalty());
            }
            pv.setTotal(total);
            
            pv.setTotal(total);
            
            

            pv.setTraceNumber(transaction.getStanSixDigit());

            if (pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PALYJA)) {
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PALYJA);
            } else if (pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.AETRA)) {
            	pv.setBillerName(Constants.WATER.BILLER_NAME.AETRA);
            }else 
            	if(pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAM_BILLER)){ 
            
            if(pv.getProductCode().equals(Constants.WATER.PRODUCT_CODE.PAM_SURABAYA)){
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PAMSurabaya);
            }else if(pv.getProductCode().equals(Constants.WATER.PRODUCT_CODE.PAM_SEMARANG)){
            	pv.setBillerName(Constants.WATER.BILLER_NAME.PAMSemarang);
            }
            
            }
            
            return Boolean.TRUE;
        }
    }
    
    public static void main(String[] args) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	String inp = "20130300";
		try {
			System.out.println(sdf.parse(inp));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

