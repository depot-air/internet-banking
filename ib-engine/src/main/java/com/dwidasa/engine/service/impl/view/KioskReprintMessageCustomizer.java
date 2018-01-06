package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.HpPaymentView;
import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.model.view.TvPaymentView;
import com.dwidasa.engine.model.view.WaterPaymentView;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;

/**
 * 
 * @author gandos
 * 
 * factory class for iso 8583  bit 48 message composer/decomposer
 * for kiosk reprint transaction
 */
public class KioskReprintMessageCustomizer {
    private static Logger logger = Logger.getLogger(KioskReprintMessageCustomizer.class);    
	private static KioskReprintMessageCustomizer _instance = new KioskReprintMessageCustomizer();
	private Map<String, MessageCustomizer> customizerDict = new HashMap<String, MessageCustomizer>();
	
	private KioskReprintMessageCustomizer() {
        customizerDict.put(PlnPurchaseView.class.getSimpleName(),
                            new PlnPurchaseReprintMessageCustomizer());
		customizerDict.put(CashInDelimaView.class.getSimpleName(), 
							new CashInDelimaReprintMessageCustomizer());
		customizerDict.put(TvPaymentView.class.getSimpleName(), 
							new TvPaymentReprintMessageCustomizer());
		customizerDict.put(HpPaymentView.class.getSimpleName(), 
							new HpPaymentReprintMessageCustomizer());
		customizerDict.put(InternetPaymentView.class.getSimpleName(), 
							new InternetPaymentReprintMessageCustomizer());
		customizerDict.put(PlnPaymentView.class.getSimpleName(), 
							new PlnPaymentReprintMessageCustomizer());
		customizerDict.put(TelkomPaymentView.class.getSimpleName(), 
							new TelkomPaymentReprintMessageCustomizer());
		customizerDict.put(TrainPaymentView.class.getSimpleName(), 
							new TrainPaymentReprintMessageCustomizer());
        customizerDict.put(NonTagListPaymentView.class.getSimpleName(),
							new NonTagListPaymentReprintMessageCustomizer());
        customizerDict.put(WaterPaymentView.class.getSimpleName(),
							new WaterPaymentReprintMessageCustomizer());
        customizerDict.put(AccountView.class.getSimpleName(),
							new SsppMessageCustomizer());
	}
	
	public static KioskReprintMessageCustomizer instance() {
		return _instance;
	}
	
	public MessageCustomizer getReprintMessageCustomizer(BaseView view) {
		return customizerDict.get(view.getClass().getSimpleName());
	}

    private static class PlnPurchaseReprintMessageCustomizer implements MessageCustomizer {
//        @Autowired
//        private LoggingService loggingService;
		private PlnPurchaseReprintMessageCustomizer() {}

		public void compose(BaseView view, Transaction transaction) {
            PlnPurchaseView pv = (PlnPurchaseView) view;
           
            transaction.setTranslationCode("022030001010010001");
        }

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
            pv.setUpjPhoneNumber(bit62.substring(219, 234).trim());

            pv.setBit48(transaction.getFreeData1());
            pv.setBit62(transaction.getFreeData3());
            pv.setFeeIndicator(transaction.getFeeIndicator());
            pv.setFee(transaction.getFee());

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

//            loggingService.logActivity(pv.getCustomerId(), Constants.PLN_PAYMENT_REP_CODE, "Reprint Pembayaran Listrik PLN, IDPEL/No Meter = " + pv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(pv.getAmount()), pv.getReferenceNumber(), pv.getMerchantType(), pv.getTerminalId());
            return Boolean.TRUE;
        }

	}

	private static class CashInDelimaReprintMessageCustomizer implements MessageCustomizer{
//        @Autowired
//        private LoggingService loggingService;
		private CashInDelimaReprintMessageCustomizer() {}
		
		public void compose(BaseView view, Transaction transaction) {
	        CashInDelimaView cv = (CashInDelimaView) view;

	        String customData = "";
	        // -- receiver id number an(25) left justfied padding with space
	        customData += String.format("%-25s", cv.getReceiverIdNumber());
	        //-- transfer code n(16) left justfied padding with space
	        customData += String.format("%1$-16s", cv.getCustomerReference());
	        // -- amount
	        customData += String.format("%012d", 0);
	        // -- provider fee
	        customData += String.format("%012d", 0);
	        // -- reference
	        customData += String.format("%-12s", " ");
	        // -- bank admin fee
	        customData += String.format("%012d", 0);

	        transaction.setFreeData1(customData);
	        transaction.setTranslationCode("00207002013");
	    }

	    public Boolean decompose(Object view, Transaction transaction) {
	        CashInDelimaView cv = (CashInDelimaView) view;
	        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

	        String bit48 = transaction.getFreeData1();
            //ID Penerima an-25 -Rata kiri -Padding with space -Reprint request diisi blank
            cv.setReceiverIdNumber(bit48.substring(0,25).trim());
            //Kode Transfer n-16 -Rata kiri -Padding with space
            cv.setCustomerReference(bit48.substring(25, 41).trim());
            //Amount n-12 -Rata kanan -Padding with zero
            cv.setAmount(new BigDecimal(bit48.substring(41, 53)));
            //Provider Fee n-12 -Rata kanan -Padding with zero
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            //Referensi an-12 -Rata kiri -Padding with space

            //Informasi Pengirim
            //Nama an-30 -Rata kiri -Padding with space
            cv.setSenderName(bit48.substring(77, 107).trim());
            //Jenis Kelamin a-1 Valid Value: F=Female, or M=Male
            cv.setSenderGender(bit48.substring(107, 108).trim());
            // Alamat an-30 -Rata kiri -Padding with space
            cv.setSenderAddress(bit48.substring(108, 138).trim());
            //Kota an-20 -Rata kiri -Padding with space
            cv.setSenderCity(bit48.substring(138, 158).trim());
            //Kode Pos an-10 -Rata kiri -Padding with space
            cv.setSenderPostalCode(bit48.substring(158, 168).trim());
            //Negara an-16 -Rata kiri -Padding with space
	        cv.setSenderCountry(bit48.substring(168, 184).trim());
            //Tipe ID Card an-10 -Rata kiri -Padding with space
	        cv.setSenderCardType(bit48.substring(184, 194).trim());
            //Nomor ID Card an-25 -Rata kiri -Padding with space
	        cv.setSenderIdNumber(bit48.substring(194, 219).trim());
            //Tempat Lahir an-20 -Rata kiri -Padding with space
	        cv.setSenderPob(bit48.substring(219, 239).trim());
            //Tanggal Lahir n-8 Format:DDMMYYYY
            try {
	            cv.setSenderDob(sdf.parse(bit48.substring(239, 247)));
	        } catch (ParseException e) {
	            cv.setSenderDob(new Date());
	        }
            //Nomer Telepon an-15 -Rata kiri -Padding with space
            cv.setSenderPhoneNumber(bit48.substring(247, 262));
            //Informasi Penerima
            // Nama an-30 -Rata kiri -Padding with space
            cv.setReceiverName(bit48.substring(262, 292).trim());
            //Jenis Kelamin a-1 Valid Value: F=Female, or M=Male
	        cv.setReceiverGender(bit48.substring(292, 293));
	        //Alamat an-30 -Rata kiri -Padding with space
            cv.setReceiverAddress(bit48.substring(293, 323).trim());
	        //Kota an-20 -Rata kiri -Padding with space
            cv.setReceiverCity(bit48.substring(323, 343).trim());
	        //Kode Pos an-10 -Rata kiri -Padding with space
            cv.setReceiverPostalCode(bit48.substring(343, 353).trim());
	        //Negara an-16 -Rata kiri -Padding with space
            cv.setReceiverCountry(bit48.substring(353, 369).trim());
	        //Tipe ID Card an-10 -Rata kiri -Padding with space
            cv.setReceiverCardType(bit48.substring(369, 379).trim());
	        //Nomor ID Card an-25 -Rata kiri -Padding with space
            cv.setReceiverIdNumber(bit48.substring(379, 404).trim());
	        //Tempat Lahir an-20 -Rata kiri -Padding with space
            cv.setReceiverPob(bit48.substring(404, 424).trim().trim());
	        //Tanggal Lahir n-8 Format:DDMMYYYY
            try {
	            cv.setReceiverDob(sdf.parse(bit48.substring(424, 432)));
	        } catch (ParseException e) {
	            cv.setReceiverDob(new Date());
	        }
            // Nomer Telepon an-15 -Rata kiri -Padding with space
	        cv.setReceiverPhoneNumber(bit48.substring(432, 447).trim());

            //Biaya Admin Bank n-12 -Rata kanan -Padding with zero
            cv.setFee(new BigDecimal(bit48.substring(447, 459)));
            // Copy Number n-1 -Need to be printed

            cv.setFeeIndicator(transaction.getFeeIndicator());
            cv.setResponseCode(transaction.getResponseCode());

            cv.setReferenceNumber(transaction.getReferenceNumber());
            cv.setTraceNumber(transaction.getStanSixDigit());

//            loggingService.logActivity(cv.getCustomerId(), Constants.CASHIN_DELIMA_REP_CODE, "Reprint Delima Cash In, Rekening Tujuan = " + cv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(cv.getAmount()), cv.getReferenceNumber(), cv.getMerchantType(), cv.getTerminalId());
	        return Boolean.TRUE;
	    }
	}
	
	private static class TvPaymentReprintMessageCustomizer implements MessageCustomizer{
//        @Autowired
//        private LoggingService loggingService;
		private TvPaymentReprintMessageCustomizer() {}
		
		public void compose(BaseView view, Transaction transaction) {
	        TvPaymentView tv = (TvPaymentView) view;
            //-- customer id an(13) padding with zero
	        String custRef = StringUtils.leftPad(tv.getCustomerReference(), 13, "0");
	        transaction.setFreeData2(custRef);

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
	        }
            else if (!tv.getBillerCode().equals(Constants.TELKOM_VISION))  {
                //indovision bit 48 diisi = bit 61
                transaction.setFreeData1(custRef);
            }

            /*
            //set bit 48
            customData = "";
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
            */
	        if (tv.getBillerCode().equals(Constants.TELKOM_VISION)) {
	            transaction.setTranslationCode(Constants.TRANSLATION_TELKOM_REPRINT);
	        }
            else if (tv.getBillerCode().equals(Constants.CENTRIN_TV))  {
                String translationCode = "02103";
	            translationCode += StringUtils.leftPad(tv.getProviderCode(), 4, "0");
	            translationCode += "07";
	            translationCode += StringUtils.leftPad(tv.getBillerCode(), 3, "0");
	            translationCode += StringUtils.leftPad(tv.getProductCode(), 4, "0");
	            transaction.setTranslationCode(translationCode);
            }
	        else {
	            transaction.setTranslationCode("01109000801001008");
	        }
	    }

	    public Boolean decompose(Object view, Transaction transaction) {
	        TvPaymentView tv = (TvPaymentView) view;

	        tv.setBit48(transaction.getFreeData1());
            if (tv.getBillerCode().equals(Constants.TELKOM_VISION)) {

                //tv.setCustomerReference(tv.getBit48().substring(0,13));
                tv.setReferenceName(tv.getBit48().substring(13, 13 + 6));
                tv.setTotal(BigDecimal.valueOf(Double.parseDouble(tv.getBit48().substring(85, 97))));    //13  + 6 + 30 + 12 + 12 + 12 +
                tv.setAccountNumber(tv.getBit48().substring(97, 116));      //13  + 6 + 30 + 12 + 12 + 12 + 12
            }
            else
            {
                if (tv.getBillerCode().equals(Constants.CENTRIN_TV)) {
                    tv.setReferenceName(tv.getBit48().substring(26, 56).trim());

                    tv.setAmount(transaction.getTransactionAmount());
                    tv.setFeeIndicator(transaction.getFeeIndicator());
                    tv.setFee(transaction.getFee());
                    tv.setTotal(tv.getAmount().add(tv.getFee()));
                    tv.setBit48(transaction.getFreeData1());
                }
                else {
                    tv.setReferenceName(tv.getBit48().substring(13, 43).trim());

                }

            }

            tv.setReferenceNumber(transaction.getReferenceNumber());
            tv.setTraceNumber(transaction.getStanSixDigit());
//            loggingService.logActivity(tv.getCustomerId(), Constants.ENTERTAINMENT_PAYMENT_REP_CODE, "Reprint Pembayaran TV, ID Pelanggan = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()), tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
	        return Boolean.TRUE;
	    }
	}
	
	private static class InternetPaymentReprintMessageCustomizer implements MessageCustomizer {
//        @Autowired
//        private LoggingService loggingService;
		private InternetPaymentReprintMessageCustomizer() {}
		
		public void compose(BaseView view, Transaction transaction) {
            InternetPaymentView iv = (InternetPaymentView) view;

//            //-- speedy flag
//            customData = "0";
            //-- customer id an(20) left justified padding with space
            transaction.setFreeData2(iv.getCustomerReference().trim());
            transaction.setTranslationCode(Constants.TRANSLATION_TELKOM_REPRINT);
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	InternetPaymentView iv = (InternetPaymentView) view;
            String bit48 = transaction.getFreeData1();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            
            //iv.setCustomerReference(bit48.substring(0, 13).trim());
            iv.setReferenceName(bit48.substring(19,49).trim());
            iv.setAmount1(new BigDecimal(bit48.substring(49, 61)));
            iv.setAmount2(new BigDecimal(bit48.substring(61, 73)));
            iv.setAmount3(new BigDecimal(bit48.substring(73, 85)));
            iv.setTotal(new BigDecimal(bit48.substring(85, 97)));
            
            //iv.setAccountNumber(new BigDecimal(bit48.substring(97, 116)).toString());
            iv.setBit48(bit48);
            iv.setFee(transaction.getFee());
            try {
            	Date yyyyMM = sdf.parse(bit48.substring(13, 19));
            	logger.info("yyyMM=" + yyyyMM);
            	
            	Calendar cal = Calendar.getInstance();
            	Calendar calPlusOne = Calendar.getInstance();
            	Calendar calPlusTwo = Calendar.getInstance();
            	
            	cal.setTime(yyyyMM);
            	calPlusOne.setTime(yyyyMM);
            	calPlusTwo.setTime(yyyyMM);
            	
            	calPlusOne.add(Calendar.MONTH, 1);
            	calPlusTwo.add(Calendar.MONTH, 2);
            	
            	List<Date> billPeriods = new ArrayList<Date>();            	
            	billPeriods.add(cal.getTime());
            	logger.info("cal.getTime()=" + cal.getTime());
            	if(iv.getAmount2().compareTo(BigDecimal.ZERO) == 1) {
            		billPeriods.add(calPlusOne.getTime());
            		logger.info("calPlusOne.getTime()=" + calPlusOne.getTime());
                }
                
                if(iv.getAmount3().compareTo(BigDecimal.ZERO) == 1) {
                	billPeriods.add(calPlusTwo.getTime());
                }
                
                iv.setBillPeriods(billPeriods);
                
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}            

            iv.setReferenceNumber(transaction.getReferenceNumber());
            iv.setTraceNumber(transaction.getStanSixDigit());
//            loggingService.logActivity(iv.getCustomerId(), Constants.INTERNET_PAYMENT_REP_CODE, "Reprint Pembayaran Internet, ID Pelanggan = " + iv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(iv.getAmount()), iv.getReferenceNumber(), iv.getMerchantType(), iv.getTerminalId());
            return Boolean.TRUE;
        }
    
	}
	
	private static class HpPaymentReprintMessageCustomizer implements MessageCustomizer {
//        @Autowired
//        private LoggingService loggingService;
		private HpPaymentReprintMessageCustomizer() {}
		private String setTranslationCode(String providerCode) {
            String translationCode = "";

            if (providerCode.equals(Constants.AKSES_NUSANTARA_CODE)) {
                translationCode = "01714";
            }
            else if (providerCode.equals(Constants.AKSES_CIPTA_CODE)) {
                translationCode = "01804";
            }
            return translationCode;
        }

        public void compose(BaseView view, Transaction transaction) {
            HpPaymentView hv = (HpPaymentView) view;
            String phone[] = EngineUtils.splitPhoneNumber(hv.getCustomerReference());

            String customData = "";
            if (hv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
                //-- industrial code(n2) + biller id(n3)
                customData += Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + HpPaymentViewServiceImpl.getSeraPaymentProductId(hv.getProductCode());
                //-- payee id (n4) right justified zero padding
                customData += Constants.PAYEE_ID.SERA_FOR_ALL;
            } else {
                //-- industrial code(n2) + biller id(n3)
                customData += "02" + hv.getProductCode();
                //-- payee id (n4) right justified zero padding
                customData += StringUtils.leftPad(hv.getProviderCode(), 4, '0');
            }

            //-- product id n(4) right justified zero padding
            customData += "0001";
            //-- country code an(2)
            customData += "62";
            //-- area / operator code an(4) right justified zero padding
            customData += phone[0];
            //-- phone number an(10) left justified space padding
            customData += String.format("%1$-10s", phone[1]);

            transaction.setFreeData1(customData);
            if (hv.getProductCode().equals(Constants.FLEXI_POSTPAID)){   //biller code = product code, flexi
                transaction.setTranslationCode(Constants.TRANSLATION_TELKOM_REPRINT);
            }
            else {
                transaction.setTranslationCode(setTranslationCode(hv.getProviderCode()) + hv.getProviderCode() +
                "02" + hv.getProductCode() + "0001");
            }

            if (hv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
                //transaction.setFreeData2("02" + HpPaymentViewServiceImpl.getSeraPaymentProductId(hv.getProductCode()) + Constants.PAYEE_ID.SERA_BIT61 + "0001");

                //023 + process code + product code (payee ID + industry code + biller code + product ID)
                transaction.setTranslationCode("023" + Constants.TRANSLATION_PROCESS_CODE.SERA.POSTPAID_REPRINT +
                    Constants.PAYEE_ID.SERA_FOR_ALL + Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + HpPaymentViewServiceImpl.getSeraPaymentProductId(hv.getProductCode()) + "0001"
                );

                if (hv.getCardData1() != null && hv.getCardData2() != null) {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.MAGNETIC_STRIPE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.PIN_ENTRY_CAPABILITY);
                } else {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.UNKNOWN_OR_NOT_APPLICABLE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.UNKNOWN);
                }
            }
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	HpPaymentView hv = (HpPaymentView) view;

            String bit48 = transaction.getFreeData1();
            hv.setBit48(transaction.getFreeData1());
            if (hv.getProductCode().equals(Constants.FLEXI_POSTPAID)) {
                //hv.setCustomerReference(hv.getBit48().substring(0,13));
                hv.setReferenceName(hv.getBit48().substring(13, 13 + 6));
                hv.setTotal(BigDecimal.valueOf(Double.parseDouble(hv.getBit48().substring(85, 97))));    //13  + 6 + 30 + 12 + 12 + 12 +
                hv.setAccountNumber(hv.getBit48().substring(97, 116));      //13  + 6 + 30 + 12 + 12 + 12 + 12
            }
            else {
                //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding. ; See Section 5
                //2 digit pertama = 02 (telko) lihat section 5 URS
                hv.setBillerCode(HpPaymentViewServiceImpl.getProductIdSeraReverse(bit48.substring(2, 5)));

                //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding. ; See Section 5
                hv.setProviderCode(bit48.substring(5, 9));
                //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding; See Section 5
                hv.setProductCode(bit48.substring(9, 13));
                //Country Code an2 Kode Negara; rata kanan (right justified); zero leading padding Untuk Indonesia: '62'
                //Area/Operator  Code  an4 Kode area atau kode operator; rata kanan (right justified); zero leading padding
                //Phone Number an10 Nomor telephone yang akan dibayar; rata kiri (left justified); space padding
/**/
                String phoneComplete = "0";
                // Area/Operator Code an4 Kode area atau kode operator; rata kanan (right justified); zero leading padding
                String soperatorCode = transaction.getFreeData1().substring(15, 19);   //5 + 4 + 4  + 2 + 4
                int ioperatorCode = Integer.parseInt(soperatorCode);
                phoneComplete += "" + ioperatorCode;
                // Phone Number an10 Nomor telephone yang akan dibayar; rata kiri (left justified); space padding
                String phoneNumber = transaction.getFreeData1().substring(19, 29).trim();    //19 + 10
                phoneComplete += "" + phoneNumber;
                hv.setCustomerReference(phoneComplete);
//                hv.setCustomerReference(transaction.getFreeData2());


                // Divre/Region Code an2 Kode Divre/Daerah dari pemilik nomor telepon
                //29 + 2 = 31
                // Datel Code an4 Kode Datel dari pemilik nomor
                //31 + 4 = 35
                //Customer Name an30 Nama Customer; rata kiri (left justified);
                hv.setReferenceName(bit48.substring(35, 65).trim());
                // space padding Other Customer ID / NPWP an16 Nomor ID customer lainnya (misal: NPWP); rata kiri (left justified); space padding
                //65 + 16 = 81
                // Total Bill n1 Tanggal jatuh tempo dari bill; format tanggal: ddmmyyyy
                hv.setNumOfBill(Integer.parseInt(bit48.substring(81, 82)));
                // Bill Status n1 Jumlah Bill yang harus dibayar customer; berisi: [1-3]
                //82 + 1 = 83
                // Due Date n8 Nomor referensi bill 1 (bulan berjalan); field ini berisi value bila field Total Bill > 0
                //83 + 8 = 91

                // Bill Ref #1 an11 Nomor referensi bill 1 (bulan berjalan); field ini berisi value bila field Total Bill > 0
                hv.setRef1(bit48.substring(91, 102));
                // Bill Amount #1 n12 Nilai tagihan bill 1 (bulan berjalan); field ini berisi value bila field Total Bill > 0
                Long lamount1 = Long.parseLong(bit48.substring(102, 114));
                hv.setAmount1(BigDecimal.valueOf(lamount1));
                BigDecimal totalAmount = BigDecimal.valueOf(lamount1);
                if (hv.getNumOfBill() > 1) {
                    // Bill Ref #2 an11 Nomor referensi bill 2 (tunggakan satu bulan sebelumnya); field ini berisi value bila field Total Bill > 1
                    hv.setRef2(bit48.substring(114, 125));
                    // Bill Amount #2 n12 Nilai tagihan bill 2 (tunggakan satu bulan sebelumnya); field ini berisi value bila field Total Bill > 1
                    Long lamount2 = Long.parseLong(bit48.substring(125, 137));
                    hv.setAmount2(BigDecimal.valueOf(lamount2));
                    totalAmount = totalAmount.add(hv.getAmount2());
                }
                if (hv.getNumOfBill() > 2) {
                    //  Bill Ref #3 an11 Nomor referensi bill 3 (tunggakan dua bulan sebelumnya); field ini berisi value bila field Total Bill > 2
                    hv.setRef3(bit48.substring(137, 148));
                    // Bill Amount #3 n12 Nilai tagihan bill 3 (tunggakan dua bulan sebelumnya); field ini berisi value bila field Total Bill > 2
                    Long lamount3 = Long.parseLong(bit48.substring(148, 160));
                    hv.setAmount3(BigDecimal.valueOf(lamount3));
                    totalAmount = totalAmount.add(hv.getAmount3());
                }
                // Fee amount n12 Nilai fee yang dibayar; rata kanan (right justified); zero leading padding
                Long fee = 0L;
                if (hv.getNumOfBill() == 1) {
                    fee = Long.parseLong(bit48.substring(114, 126));
                }
                else if (hv.getNumOfBill() == 1) {
                    fee = Long.parseLong(bit48.substring(137, 149));
                }
                else {
                    fee = Long.parseLong(bit48.substring(160, 172));
                }
                hv.setAmount(totalAmount);
                hv.setFee(BigDecimal.valueOf(fee));
                hv.setTotal(hv.getAmount().add(hv.getFee()));
            }

            hv.setReferenceNumber(transaction.getReferenceNumber());
            hv.setTraceNumber(transaction.getStanSixDigit());
//            loggingService.logActivity(hv.getCustomerId(), Constants.TELCO_PAYMENT_REP_CODE, "Reprint Pembayaran HP Pasca Bayar, No HP = " + hv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(hv.getAmount()), hv.getReferenceNumber(), hv.getMerchantType(), hv.getTerminalId());
            return Boolean.TRUE;
        }
    
	}
	
	private static class PlnPaymentReprintMessageCustomizer implements MessageCustomizer {
//        @Autowired
//        private LoggingService loggingService;
		private PlnPaymentReprintMessageCustomizer() {}
		
		public void compose(BaseView view, Transaction transaction) {
            PlnPaymentView pv = (PlnPaymentView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

//            transaction.setFreeData1("");

            String customData = "";
            customData += String.format("%1$-16s", pv.getCustomerReference());
            customData += sdf.format(pv.getBillPeriods().get(0));

            transaction.setFreeData2(customData);
            transaction.setTranslationCode("00503");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            /*
            PlnPaymentView pv = (PlnPaymentView) view;
            pv.setResponseCode(transaction.getResponseCode());
            pv.setReferenceNumber(transaction.getReferenceNumber());

            pv.setTraceNumber(transaction.getStanSixDigit());
            */

        	PlnPaymentView pv = (PlnPaymentView) view;
            String bit48 = transaction.getFreeData1();
            //Bill status n-1 1 = SP sends 1 bill 2 = SP sends 2 bills 3 = SP sends 3 bills 4 = SP sends 4 bills
//            int n = Integer.parseInt(bit48.substring(0, 1));
            //Payment status (an-1), n-1 -Rata kanan -Padding with zero -Fill with Bill Status on posting  request

            //Total outstanding bills n-2 -Rata kanan -Padding with zero

            //PLN reference number n-32 Nomer referensi PLN -Rata kiri -Padding with space
            pv.setBillerReference(bit48.substring(4, 36).trim());
            // Customer name an-25 Nama Pelanggan: -Rata kiri -Padding with space
            pv.setReferenceName(bit48.substring(36,61).trim());
            //UP (Unit Pelayanan) code an-5 Kode Pelayanan PLN -Rata kiri -Padding with space
            pv.setUnitCode(bit48.substring(61,66));
            //UP phone number an-15 Nomer telepon Pelayanan PLN -Rata kiri -Padding with space
            pv.setUnitPhone(bit48.substring(66, 81).trim());
            //Customer segmentation an-4 Segmentasi customer -Rata kiri -Padding with space
            String powerCat = bit48.substring(81, 85).trim() + "/";
            //Power consuming category an-9 -Rata kanan -Padding with zero
            powerCat += bit48.substring(85, 94);
            pv.setPowerCategory(powerCat);
            
            pv.setAmount(transaction.getTransactionAmount());
            pv.setFeeIndicator(transaction.getFeeIndicator());
            pv.setFee(transaction.getFee());
            pv.setTotal(pv.getAmount().add(pv.getFee()));
            pv.setBit48(transaction.getFreeData1());
            pv.setBit61(transaction.getFreeData2());
            /*
            //bit 61
            //Product ID an-4 Product ID PLN
            pv.setProductCode(transaction.getFreeData2().substring(0,4));
            //Customer's ID an-16 1. Left justified 2. padded with space 3. map from bit 121

            //GSP Trace Number an-16 1. Left justified 2. padded with space

            //MLPO Reference No an-22 1. Left justified 2. padded with space
            */
            pv.setReferenceNumber(transaction.getReferenceNumber());
            pv.setTraceNumber(transaction.getStanSixDigit());
            String informasiStruk = "";
            String capitalSentence = transaction.getTranslationCode().toUpperCase();
            informasiStruk += (transaction.getTranslationCode().length() >4) ? transaction.getTranslationCode().substring(5) : "";
            if (!capitalSentence.toUpperCase().contains("TOKEN")) {
                //atau ditaruh ditengah
                String[] words = informasiStruk.toUpperCase().split("ATAU");
                if (words.length > 1) {
                    informasiStruk = words[0] + "\nAtau\n" + words[1];
                    informasiStruk += " : ";
                    //1+1+2+32+25+5
                    informasiStruk += pv.getUnitPhone();
                }
            }
            informasiStruk += "\nInformasi Hubungi Call Center : 123";
            informasiStruk += "\nAtau Hub. PLN Terdekat : " + pv.getUnitPhone();
            //
            pv.setInformasiStruk(informasiStruk);
//          ce.logActivity(pv.getCustomerId(), Constants.PLN_PAYMENT_REP_CODE, "Reprint Pembayaran Listrik PLN, ID PEL / No Meter = " + pv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(pv.getAmount()), pv.getReferenceNumber(), pv.getMerchantType(), pv.getTerminalId());
            return Boolean.TRUE;
        }
    
	}

    private static class NonTagListPaymentReprintMessageCustomizer implements MessageCustomizer {
//        @Autowired
//        private LoggingService loggingService;
		private NonTagListPaymentReprintMessageCustomizer() {
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
            nv.setSubscriberId(bit48.substring(85, 97));
            nv.setSubscriberName(bit48.substring(97, 122).trim());
            nv.setBillerReference(bit48.substring(122, 154).trim());
            nv.setProviderReference(bit48.substring(154, 186).trim());
            nv.setServiceUnit(bit48.substring(186, 191));
            nv.setUnitAddress(bit48.substring(191, 226).trim());
            nv.setUnitPhone(bit48.substring(226, 241).trim());

            nv.setBit48(bit48);
            nv.setBit62(bit62);

            nv.setReferenceNumber(transaction.getReferenceNumber());
            String informasiStruk = "";
            String capitalSentence = transaction.getFreeData2().toUpperCase();
            informasiStruk += transaction.getFreeData2();
            if (!capitalSentence.toUpperCase().contains("TOKEN")) {
               informasiStruk += nv.getUnitPhone();
                //atau ditaruh ditengah
                String[] words = informasiStruk.toUpperCase().split("ATAU");
                if (words.length > 1){
                    informasiStruk = words[0] + "\nAtau\n" + words[1].trim();
                }
            }
            nv.setInformasiStruk(informasiStruk);
//            loggingService.logActivity(nv.getCustomerId(), Constants.NONTAGLIST_PAYMENT_REP_CODE, "Reprint Pembayaran Non Tagihan Listrik PLN, ID PEL / No Meter = " + nv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(nv.getAmount()), nv.getReferenceNumber(), nv.getMerchantType(), nv.getTerminalId());
            return Boolean.TRUE;
        }

	}

	private static class TelkomPaymentReprintMessageCustomizer implements MessageCustomizer {
//        @Autowired
//        private LoggingService loggingService;
		private TelkomPaymentReprintMessageCustomizer() {}
		
		public void compose(BaseView view, Transaction transaction) {
           TelkomPaymentView tv = (TelkomPaymentView) view;

            //-- customer id n(9) left justified padding with space
            String customData = String.format("%1$-13s", tv.getCustomerReference());
            transaction.setFreeData2(customData);
            transaction.setTranslationCode(Constants.TRANSLATION_TELKOM_REPRINT);
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	TelkomPaymentView tv = (TelkomPaymentView) view;
            String bit48 = transaction.getFreeData1();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            
            //tv.setCustomerReference(bit48.substring(0, 13).trim());
            tv.setReferenceName(bit48.substring(19,49).trim());
            tv.setAmount1(new BigDecimal(bit48.substring(49, 61)));
            tv.setAmount2(new BigDecimal(bit48.substring(61, 73)));
            tv.setAmount3(new BigDecimal(bit48.substring(73, 85)));
            tv.setTotal(new BigDecimal(bit48.substring(85, 97)));
            //tv.setAccountNumber(new BigDecimal(bit48.substring(97, 116)).toString());
            tv.setBit48(bit48);

            try {
            	Date yyyyMM = sdf.parse(bit48.substring(13, 19));
            	
            	Calendar cal = Calendar.getInstance();
            	Calendar calPlusOne = Calendar.getInstance();
            	Calendar calPlusTwo = Calendar.getInstance();
            	
            	cal.setTime(yyyyMM);
            	calPlusOne.setTime(yyyyMM);
            	calPlusTwo.setTime(yyyyMM);
            	
            	calPlusOne.add(Calendar.MONTH, 1);
            	calPlusTwo.add(Calendar.MONTH, 2);
            	
				tv.setBillPeriod1(cal.getTime());				
				
				if(tv.getAmount2().compareTo(BigDecimal.ZERO) == 1) {
					tv.setBillPeriod2(calPlusOne.getTime());
                }
                
                if(tv.getAmount3().compareTo(BigDecimal.ZERO) == 1) {
                	tv.setBillPeriod3(calPlusTwo.getTime());
                }
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				tv.setBillPeriod1(null);
				tv.setBillPeriod2(null);
				tv.setBillPeriod3(null);
			}

            tv.setFee(transaction.getFee());
            tv.setReferenceNumber(transaction.getReferenceNumber());
            tv.setTraceNumber(transaction.getStanSixDigit());
//            loggingService.logActivity(tv.getCustomerId(), Constants.TELKOM_PAYMENT_REP_CODE, "Reprint Pembayaran Telkom, No Telepon = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()) , tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
            return Boolean.TRUE;
        }
	}
	
	private static class TrainPaymentReprintMessageCustomizer implements MessageCustomizer {
//        @Autowired
//        private LoggingService loggingService;
		private TrainPaymentReprintMessageCustomizer() {}
		
		public void compose(BaseView view, Transaction transaction) {
            TrainPaymentView tv = (TrainPaymentView) view;
            //transaction.setFreeData1(tv.getBit48());  //nilainya null
            String customData = "";
            //Kode Booking an-13 -Rata kiri -Padding with space
            customData += StringUtils.rightPad(tv.getCustomerReference(), 13, " ");
            //kaiCode1 an-2 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 2, " ");
            //kaiCode2 an-4 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 4, " ");
            //totalTrip n-1
            customData += "1";
            //numberOfPassenger n-2 -Rata kanan -Padding with zero
            customData += StringUtils.leftPad((tv.getNumOfPassenger() != null) ? "" + tv.getNumOfPassenger() : "0", 2, "0");
            //carrier an-2 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 2, " ");
            //trainClass an-1 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 1, " ");
            //trainFrom an-3 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 3, " ");
            //trainTo an-3 -Right kanan -Padding with zero
            customData += StringUtils.leftPad("", 3, "0");
            //tripNumber an-4 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 4, " ");
            //departureDayTime n-8 Format:ddMMHHmm
            customData += "00000000";
            //passengerName an-30 Jumlah Bill yang harus dibayar customer; berisi: [1-3]
            customData += StringUtils.rightPad("", 30, " ");
            //trainNumber an-5 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 5, " ");
            //trainName an-32 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 32, " ");
            //seats an-32 -Rata kiri -Padding with space
            customData += StringUtils.rightPad("", 32, " ");
            //Admin charge n-12 -Rata kanan -Padding with zero
            customData += StringUtils.leftPad("", 12, "0");
            transaction.setFreeData1(customData);

            //-- customer id an(20) left justified padding with space
            customData = String.format("%1$-20s", tv.getCustomerReference());
            transaction.setFreeData2(customData);
            transaction.setTranslationCode("01107000801030002");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TrainPaymentView tv = (TrainPaymentView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("yyddMMHHmm");

            String bit48 = transaction.getFreeData1();
            tv.setReferenceName(bit48.substring(43, 73).trim());
            tv.setNumOfPassenger(Integer.valueOf(bit48.substring(20, 22)));
            tv.setTrainName(bit48.substring(78, 110).trim());
            tv.setTripInfo(bit48.substring(25, 28) + " - " + bit48.substring(28, 31));

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String yy = String.valueOf(cal.get(Calendar.YEAR)).substring(2);

            try {
                tv.setDepartureDate(sdf.parse(yy + bit48.substring(35, 43)));
            } catch (ParseException e) {
                tv.setDepartureDate(null);
            }

            tv.setAmount(transaction.getTransactionAmount());
            tv.setFeeIndicator(transaction.getFeeIndicator());
            tv.setFee(transaction.getFee());
            tv.setTotal(tv.getAmount().add(tv.getFee()));
            tv.setBit48(transaction.getFreeData1());

            tv.setReferenceNumber(transaction.getReferenceNumber());
            tv.setTraceNumber(transaction.getStanSixDigit());
//            loggingService.logActivity(tv.getCustomerId(), Constants.TRANSPORTATION_PAYMENT_REP_CODE, "Reprint Pembayaran Kereta, Kode Booking = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()), tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
            return Boolean.TRUE;
        }
	}

    private static class WaterPaymentReprintMessageCustomizer implements MessageCustomizer {
//        @Autowired
//        private LoggingService loggingService;
		private WaterPaymentReprintMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            WaterPaymentView  pv = (WaterPaymentView) view;

            //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding;
            String customData = Constants.INDUSTRY_CODE.UTILITY_WATER + pv.getBillerCode();
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
                    pv.getProviderCode() + Constants.INDUSTRY_CODE.UTILITY_WATER + pv.getBillerCode() + pv.getProductCode();
            transaction.setTranslationCode(translationCode);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            WaterPaymentView wv = (WaterPaymentView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            String bit48 = transaction.getFreeData1();

            //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding;

            //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;

            //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding;

            //Kode Pelanggan an-15 -Rata Kiri -Padding with Space

            //Kode Area an-10 -Rata Kiri -Padding with Space
            // 5 + 4 + 4 + 15 + 10
            wv.setAreaCode(bit48.substring(28, 38).trim());
            //Nama Customer an-30 -Rata kiri -Padding with space
            wv.setReferenceName(bit48.substring(38, 68).trim());
            //Due Date n-8 Format:ddMMyyyy Fill with 00000000 if not available
            wv.setDueDateDdMMyyyy(bit48.substring(68, 76).trim());
            if (!wv.getDueDateDdMMyyyy().equals("00000000")) {
                try {
                    wv.setDueDate(sdf.parse(wv.getDueDateDdMMyyyy()));
                } catch (ParseException e) {
                    //logger.info(e.getMessage());
                }
            }
            //Bill Reference an-15 -Bill ID dari Palyja -Rata kiri -Padding with space
            wv.setBillReference(bit48.substring(76, 91));
            //Start Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            wv.setStartServiceDdMMyyyy(bit48.substring(91, 99).trim());
            if (!wv.getStartServiceDdMMyyyy().equals("00000000")) {
                try {
                    wv.setStartService(sdf.parse(wv.getStartServiceDdMMyyyy()));
                } catch (ParseException e) {
                    //logger.info(e.getMessage());
                }
            }
            //End Service Periode n-8 Format:ddMMyyyy Fill with 00000000 if not available
            wv.setEndServiceDdMMyyyy(bit48.substring(99, 107).trim());
            if (!wv.getEndServiceDdMMyyyy().equals("00000000")) {
                try {
                    wv.setEndService(sdf.parse(wv.getEndServiceDdMMyyyy()));
                } catch (ParseException e) {
                    //logger.info(e.getMessage());
                }
            }
//Bill Date n-8 Format: ddMMyyyy Fill with 00000000 if not available
            wv.setBillDateYyyyMMdd(bit48.substring(107, 115).trim());
            if (!wv.getBillDateYyyyMMdd().equals("00000000")) {
                try {
                	if (wv.getBillDateYyyyMMdd().substring(6).equals("00")) {
                		wv.setBillDateYyyyMMdd( wv.getBillDateYyyyMMdd().substring(0, 6) + "01");
                	}
                    wv.setBillDate(sdf.parse(wv.getBillDateYyyyMMdd()));
                    wv.setBillDate(sdf.parse(wv.getBillDateYyyyMMdd()));
                } catch (ParseException e) {
                    //logger.info(e.getMessage());
                }
            }
            
            //Amount an-12 -Rata kanan -Padding with zero -No decimal
            wv.setAmount(new BigDecimal(bit48.substring(115, 127)));
            //Provider Fee an-12 -Rata kanan -Padding with zero -No Decimals
            wv.setFee(new BigDecimal(bit48.substring(127, 139)));
            //Penalty an-12 -Rata kanan -Padding with zero -No decimal
            wv.setPenalty(new BigDecimal(bit48.substring(139, 151)));
            //Reserved1 an-30 -Berisi Tariff Code dari Palyja -Rata Kiri -Padding with Space
            wv.setReserved1(bit48.substring(151, 181).trim());
            //Reserved2 an-30 -Reserved -Fill with blank -Rata Kiri -Padding with Space
            wv.setReserved1(bit48.substring(181).trim());

            BigDecimal total = wv.getAmount().add(wv.getFee().add(wv.getPenalty()));
            wv.setTotal(total);

            wv.setReferenceNumber(transaction.getReferenceNumber());
            wv.setTraceNumber(transaction.getStanSixDigit());
//            loggingService.logActivity(wv.getCustomerId(), Constants.WATER.TRANSACTION_TYPE.REPRINT, "Reprint Pembayaran Air, ID Pelanggan = " + wv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(wv.getAmount()) , wv.getReferenceNumber(), wv.getMerchantType(), wv.getTerminalId());
            return Boolean.TRUE;
        }

	}

    private static class SsppMessageCustomizer implements MessageCustomizer {
		private SsppMessageCustomizer() { }

      public void compose(BaseView view, Transaction transaction) {
          AccountView  av = (AccountView) view;

      }

      public Boolean decompose(Object view, Transaction transaction) {
    	  AccountView  av = (AccountView) view;
    	  String bit48 = transaction.getFreeData1();
    	  //0000001032217220020
    	  //000000002293949005071
    	  //account balance
    	  av.setAvailableBalance(new BigDecimal(bit48.substring(0, 15)));
    	  av.setProductId(Long.parseLong(bit48.substring(18, 20)));
    	  av.setAccountStatus(Integer.parseInt(bit48.substring(bit48.length() - 1)));    	  
    	  av.setCustomerName(bit48);
    	  
    	  av.setAccountName(transaction.getFreeData4());
    	  av.setProductName(transaction.getFreeData5());
          return Boolean.TRUE;
      }

	}
}
