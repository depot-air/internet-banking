package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.*;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 3/28/12
 * Time: 7:01 PM
 */
public class KioskCheckStatusMessageCustomizer {
    private static Logger logger = Logger.getLogger( KioskCheckStatusMessageCustomizer.class );
    private static KioskCheckStatusMessageCustomizer _instance = new KioskCheckStatusMessageCustomizer();
	private Map<String, MessageCustomizer> customizerDict = new HashMap<String, MessageCustomizer>();

	private KioskCheckStatusMessageCustomizer() {
		customizerDict.put(VoucherPurchaseView.class.getSimpleName(), new VoucherPurchaseCheckStatusMessageCustomizer());
		customizerDict.put(CashInDelimaView.class.getSimpleName(), new CashInCheckStatusMessageCustomizer());
        customizerDict.put(TransferView.class.getSimpleName(), new TransferAtmbCheckStatusMessageCustomizer()); //ATMB
        customizerDict.put(VoucherGamePurchaseView.class.getSimpleName(), new VoucherGamePurchaseCheckStatusMessageCustomizer());
        customizerDict.put(MncLifePurchaseView.class.getSimpleName(), new MncLifePurchaseCheckStatusMessageCustomizer());
	}

	public static KioskCheckStatusMessageCustomizer instance() {
		return _instance;
	}

	public MessageCustomizer getCheckStatusMessageCustomizer(BaseView view) {
		return customizerDict.get(view.getClass().getSimpleName());
	}

    private static class VoucherPurchaseCheckStatusMessageCustomizer implements MessageCustomizer{
		private VoucherPurchaseCheckStatusMessageCustomizer() {}

        private String setTranslationCode(String providerCode) {
            String translationCode = "";

            if (providerCode.equals(Constants.INDOSMART_CODE)) {
                translationCode = "00102";
            }
            else if (providerCode.equals(Constants.MKN_CODE)) {
                translationCode = "00402";
            }
            else if (providerCode.equals(Constants.AKSES_NUSANTARA_CODE)) {
                translationCode = "01703";
            }
            else if (providerCode.equals(Constants.AKSES_CIPTA_CODE)) {
                translationCode = "01801";
            }
            return translationCode;
        }

		public void compose(BaseView view, Transaction transaction) {
	        VoucherPurchaseView vpv = (VoucherPurchaseView) view;
	        //-- customer id an(13) padding with zero
	        String customData = StringUtils.leftPad(vpv.getCustomerReference(), 13, "0");

            String[] phone = EngineUtils.splitPhoneNumber(vpv.getCustomerReference());

            BigDecimal denom = new BigDecimal(vpv.getDenomination());
            if (denom.compareTo(vpv.getAmount()) >= 0) {
                vpv.setFeeIndicator("C");
                vpv.setFee(denom.subtract(vpv.getAmount()));
            }
            else {
                vpv.setFeeIndicator("D");
                vpv.setFee(vpv.getAmount().subtract(denom));
            }
            transaction.setTransactionAmount(denom);

            //set bit 48
            customData = "";
            //-- Country Code an2 Kode Negara; rata kanan (right justified); zero leading padding Untuk Indonesia: '62'
            customData += "62";
            //-- Area/Operator Code an4 Kode area atau kode operator; rata kanan (right justified); zero leading padding
            customData += phone[0];
            //-- Phone Number an10 Nomor telephone yang akan dibayar; rata kiri (left justified); space padding
            customData += String.format("%1$-10s", phone[1]);
            //-- Voucher nominal n12 Nilai voucher yang dibeli; rata kanan (right justified); zero leading padding
            customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");
            //-- Fee Indicator a1 Indikator fee atau biaya admin 'D' : debet 'K' : kredit
            customData += vpv.getFeeIndicator();
            //-- Fee amount n12 Nilai fee yang dibayar; rata kanan (right justified); zero leading padding
            customData += String.format("%012d", vpv.getFee().longValue());

            transaction.setFreeData1(customData);
            transaction.setTransactionAmount(new BigDecimal(vpv.getDenomination()));

            String prodId = String.valueOf(Long.valueOf(vpv.getDenomination()) / 1000);
            transaction.setTranslationCode(setTranslationCode(vpv.getProviderCode()) + vpv.getProviderCode() +
                    "02" + vpv.getProductCode() + StringUtils.leftPad(prodId, 4, "0"));

	        if (vpv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
//                transaction.setFreeData2("02" + VoucherPurchaseViewServiceImpl.getSeraPurchaseBillerId(vpv.getProductCode()) + Constants.PAYEE_ID.SERA_BIT61 + VoucherPurchaseViewServiceImpl.getSeraPurchaseProductId(vpv.getProductCode(), vpv.getDenomination()));
                //payee ID + industry code + biller code + product ID
                //biller code di URS = product code di DB
                //023 + process code + product code (payee ID + industry code + biller code + product ID)
                transaction.setTranslationCode("023" + Constants.TRANSLATION_PROCESS_CODE.SERA.PREPAID_CEK_STATUS +
                    Constants.PAYEE_ID.SERA_FOR_ALL + Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + VoucherPurchaseViewServiceImpl.getSeraPurchaseBillerId(vpv.getProductCode()) + VoucherPurchaseViewServiceImpl.getSeraPurchaseProductId(vpv.getProductCode(), vpv.getDenomination())
                );

                if (vpv.getCardData1() != null && vpv.getCardData2() != null) {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.MAGNETIC_STRIPE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.PIN_ENTRY_CAPABILITY);
                } else {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.UNKNOWN_OR_NOT_APPLICABLE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.UNKNOWN);
                }
            } else {
                transaction.setFreeData2(customData);
            }

	    }

	    public Boolean decompose(Object view, Transaction transaction) {
            VoucherPurchaseView vpv = (VoucherPurchaseView) view;
            vpv.setResponseCode(transaction.getResponseCode());

            String phoneComplete = "";
            // Country Code an2 Kode Negara; rata kanan (right justified); zero leading padding Untuk Indonesia: '62'
            phoneComplete += "0";
            // Area/Operator Code an4 Kode area atau kode operator; rata kanan (right justified); zero leading padding
            String soperatorCode = transaction.getFreeData1().substring(2, 6);   //2 + 4
            int ioperatorCode = Integer.parseInt(soperatorCode);
            phoneComplete += "" + ioperatorCode;
            // Phone Number an10 Nomor telephone yang akan dibayar; rata kiri (left justified); space padding
            String phoneNumber = transaction.getFreeData1().substring(6, 16).trim();    //6 + 10
            phoneComplete += "" + phoneNumber;
            vpv.setCustomerReference(phoneComplete);
//            vpv.setCustomerReference(transaction.getFreeData2());
            // Voucher nominal n12 Nilai voucher yang dibeli; rata kanan (right justified); zero leading padding
            String sdenomination = transaction.getFreeData1().substring(16, 28);   //16 + 12
            Long ldenomination = Long.parseLong(sdenomination);
            vpv.setDenomination("" + ldenomination);
            // Window Period n8 Masa berlaku voucher
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            try {
                vpv.setWindowPeriod(sdf.parse(transaction.getFreeData1().substring(28, 36)));     //28 + 8
            } catch (ParseException e) {
                vpv.setWindowPeriod(new Date());
            }
            // Serial Number an16 Nomor serial voucher
            vpv.setSerialNumber(transaction.getFreeData1().substring(36, 52));     //36 + 16
            // Transaction Status n1 Jika 0 = Transaksi gagal Jika 1 = Transaksi sukses Jika 2 = Transaksi pending
            String statusCode = transaction.getFreeData1().substring(52, 53);
            vpv.setStatusCode(statusCode);

            vpv.setReferenceNumber(transaction.getReferenceNumber());
            vpv.setTraceNumber(transaction.getStanSixDigit());
	        return Boolean.TRUE;
	    }
	}

    private static class VoucherGamePurchaseCheckStatusMessageCustomizer implements MessageCustomizer{
            private VoucherGamePurchaseCheckStatusMessageCustomizer() {}


        public void compose(BaseView view, Transaction transaction) {
            VoucherGamePurchaseView vpv = (VoucherGamePurchaseView) view;

            //Translation Code =  Routing Code  + Process Code  + Product Code (if available)
            //032  01 = Inquiry, 02 = Posting, 03 = Cek Status
            //Value=Payee ID + Industry Code + Biller Code + ProductID
            transaction.setTranslationCode(
                    Constants.VOUCHER_GAME.ROUTING_CODE + "03" +
                    Constants.VOUCHER_GAME.PAYEE_ID + Constants.INDUSTRY_CODE.ENTERTAINMENT + Constants.VOUCHER_GAME.BILLER_CODE + Constants.VOUCHER_GAME.PRODUCT_ID);
//            transaction.setFeeIndicator(vpv.getFeeIndicator());
//            transaction.setFee(vpv.getFee());

            String customData = "";
            //Biller ID   n5  Identitas dari Biller(*); rata kanan(right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.BILLER_CODE;
            //Payee ID  n4  Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.PAYEE_ID;
            //Product ID   n4  Identitas dari Produk(*); rata kanan (right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.PRODUCT_ID;
            //Nama Voucher   ans20  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherName(), 20, " ");
            //Kode Product Voucher ans20  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getProductCode(), 20, " ");  //voucher product code = product code, bukan Constants.VOUCHER_GAME.PRODUCT_ID
            //Konversi Mata Uang Game ans40  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getCurrencyConversion(), 40, " ");
            //Kode Voucher   ans30  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherCode(), 30, " ");
            //No Serial Voucher   ans30  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherSerial(), 30, " ");
            //Bill Reference   an-10  -Rata kiri -Padding with space -Blank on posting & reprint request
            customData += StringUtils.rightPad("          ", 10, " ");
            //Denom   n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");
            //Harga Jual  n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getAmount().toPlainString(), 12, "0");    //amount = harga jual
            //Fee   n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getFee().toPlainString(), 12, "0");    //amount = harga jual

            transaction.setFreeData1(customData);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            VoucherGamePurchaseView vpv = (VoucherGamePurchaseView) view;
            vpv.setResponseCode(transaction.getResponseCode());
            vpv.setReferenceNumber(transaction.getReferenceNumber());

            transaction.setTransactionAmount(vpv.getAmount());

            String bit48 = transaction.getFreeData1();

            //Biller ID   n5  Identitas dari Biller(*); rata kanan(right justified); zero leading padding;See Section 5
            //Payee ID  n4  Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;See Section 5
            //Product ID   n4  Identitas dari Produk(*); rata kanan (right justified); zero leading padding;See Section 5
            //Nama Voucher   ans20  -Rata kiri -Padding with space
            vpv.setVoucherName(bit48.substring(13, 33).trim());
            //Kode Product Voucher ans20  -Rata kiri -Padding with space
            vpv.setProductCode(bit48.substring(33, 53).trim());     ////voucher product code = product code, bukan Constants.VOUCHER_GAME.PRODUCT_ID
            //Konversi Mata Uang Game ans40  -Rata kiri -Padding with space
            vpv.setCurrencyConversion(bit48.substring(53, 93).trim());
            //Kode Voucher   ans30  -Rata kiri -Padding with space
            vpv.setVoucherCode(bit48.substring(93, 123).trim());
            //No Serial Voucher   ans30  -Rata kiri -Padding with space
            vpv.setVoucherSerial(bit48.substring(123, 153).trim());
            //Bill Reference   an-10  -Rata kiri -Padding with space -Blank on posting & reprint request
            vpv.setBillReference(bit48.substring(153, 163).trim());
            //Denom   n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setDenomination(new BigDecimal(bit48.substring(163, 175)).toString());
            //Harga Jual  n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setAmount(new BigDecimal(bit48.substring(175, 187)));
            //Fee   n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setFee(new BigDecimal(bit48.substring(187, 199)));

            vpv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
        }

    private static class CashInCheckStatusMessageCustomizer implements MessageCustomizer{
		private CashInCheckStatusMessageCustomizer() {}

		public void compose(BaseView view, Transaction transaction) {
	        CashInDelimaView cdv = (CashInDelimaView) view;
	        //-- customer id an(13) padding with zero
	        String customData = StringUtils.leftPad(cdv.getCustomerReference(), 13, "0");
	        transaction.setFreeData2(customData);

            //set bit 48
            customData = "";
            //-- ID Penerima(KTP,SIM,etc) an-25 -Rata kiri, -Padding with space -Reprint request diisi blank
            customData += StringUtils.rightPad(cdv.getReceiverIdNumber(), 25, " ");
            //-- Kode Transfer n-16 -Rata kiri  -Padding with space
            customData += StringUtils.rightPad(cdv.getCustomerReference(), 16, " ");
            //-- Amount n-12 -Rata kanan -Padding with zero -Cek status request diisi '000000000000' -Reprint request diisi '000000000000'
            customData += "000000000000";
            //--  Provider Fee n-12 -Rata kanan -Padding with zero -Cek status request diisi '000000000000' -Reprint request diisi '000000000000'
            customData += "000000000000";
            //-- Referensi an-12 -Rata kiri -Padding with space -Cek status request diisi blank -Reprint request diisi blank
            customData += StringUtils.rightPad("", 12, " ");
            //--  Biaya Admin Bank n-12 -Rata kanan -Padding with zero -Cek status request diisi '000000000000' -Reprint request diisi '000000000000'
            customData += "000000000000";

            transaction.setFreeData1(customData);

            //002 05=Inquiry 06=Posting 07=Reprint 08=Cek Status 002013
            transaction.setTranslationCode("00208002013");
	    }

	    public Boolean decompose(Object view, Transaction transaction) {
            CashInDelimaView cdv = (CashInDelimaView) view;
            cdv.setResponseCode(transaction.getResponseCode());

            //mandatory diset empty string atau 0
            cdv.setSenderName("a");
            cdv.setSenderIdNumber("12345");
            cdv.setSenderAddress("a");
            cdv.setSenderCity("a");
            cdv.setSenderPostalCode("12345");
            cdv.setSenderPob("a");
            cdv.setSenderPhoneNumber("12345");

            cdv.setReceiverName("b");
            cdv.setReceiverIdNumber("12345");
            cdv.setReceiverAddress("b");
            cdv.setReceiverCity("b");
            cdv.setReceiverPostalCode("12345");
            cdv.setReceiverPob("b");
            cdv.setReceiverPhoneNumber("12345");

            String bit48 = transaction.getFreeData1();
            //-- ID Penerima(KTP,SIM,etc) an-25 -Rata kiri, -Padding with space -Reprint request diisi blank
            cdv.setReceiverIdNumber(bit48.substring(0, 25).trim());
            //-- Kode Transfer n-16 -Rata kiri  -Padding with space
            cdv.setCustomerReference(bit48.substring(25, 41).trim());   //25 + 16
            //-- Amount n-12 -Rata kanan -Padding with zero -Cek status request diisi '000000000000' -Reprint request diisi '000000000000'
            cdv.setAmount(new BigDecimal(bit48.substring(41, 53)));
            //--  Provider Fee n-12 -Rata kanan -Padding with zero -Cek status request diisi '000000000000' -Reprint request diisi '000000000000'
            cdv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            //-- Referensi an-12 -Rata kiri -Padding with space -Cek status request diisi blank -Reprint request diisi blank
//            cdv.setReferenceNumber();
            //--  Biaya Admin Bank n-12 -Rata kanan -Padding with zero -Cek status request diisi '000000000000' -Reprint request diisi '000000000000'
            cdv.setFee(new BigDecimal(bit48.substring(77, 89)));

            cdv.setReferenceNumber(transaction.getReferenceNumber());
            cdv.setTraceNumber(transaction.getStanSixDigit());
	        return Boolean.TRUE;
	    }
	}

    private static class TransferAtmbCheckStatusMessageCustomizer implements MessageCustomizer{
		private TransferAtmbCheckStatusMessageCustomizer() {}

		public void compose(BaseView view, Transaction transaction) {
	        TransferView tv = (TransferView) view;
            logger.info("tv=" + tv);
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
            String bit41 = Constants.getTerminalIdAtmb(transaction.getTerminalId(), tv.getAccountNumber());
            transaction.setTerminalId(bit41);
            transaction.setBit42(StringUtils.rightPad(bit41 + tv.getStanSixDigit(), 15));
//            String bi43 = StringUtils.rightPad(StringUtils.rightPad("DACEN", 32, " ") + " JBR ID ", 40);
//            transaction.setBit43(bi43);

	        String customData = "";
            //Dest Bank Code an 3 Left Justified, padded with spaces
            customData += StringUtils.rightPad(tv.getBillerCode(), 3, " ");
            //Dest Bank Name an 15 Left Justified, padded with spaces
            customData += StringUtils.rightPad(tv.getBillerName(), 15, " ");
            //Dest Acc Number an 19 Left Justified, padded with spaces
            customData += StringUtils.rightPad(tv.getCustomerReference(), 19, " ");
            //Dest Acc Name an 30 Left Justified, padded with spaces, saat request berisi blank
            customData += StringUtils.rightPad((tv.getReceiverName() != null) ? tv.getReceiverName() : "                              ", 30, " ");

            //tambah amount an 12 Right Justified, padded with zeros, saat request berisi 0
            customData += StringUtils.leftPad((tv.getAmount() != null) ? tv.getAmount().toPlainString() : "000000000000", 12, "0");
            //Cust Ref Number an 16 Left Justified, padded with spaces, saat request berisi blank
            customData += StringUtils.rightPad((tv.getCustRefAtmb() != null) ? tv.getCustRefAtmb() : "                ", 16, " ");
            //Issuer Bank Code an 3 Left Justified, padded with spaces
            customData += StringUtils.rightPad("688", 3, " ");
            //Issuer Bank Name an 15 Left Justified, padded with spaces, saat request berisi blank
            customData += StringUtils.rightPad(Constants.BPRKS.NAME, 15, " ");
            //Issuer Acc Number an 19 Left Justified, padded with spaces, saat request berisi blank
            customData += StringUtils.rightPad(tv.getAccountNumber(), 19, " ");
            //Issuer Acc Name an 30 Left Justified, padded with spaces, saat request berisi blank
            customData += StringUtils.rightPad((tv.getSenderName() != null) ? tv.getSenderName() : "                              ", 30, " ");
            //Admin Fee an 10 Right Justified, padded with zeros, saat request berisi 0
            customData += StringUtils.leftPad((tv.getFee() != null) ? tv.getFee().toString() : "0000000000", 10, "0");
            //Retrieval Ref Number an 12 Right Justified, padded with zeros
            customData += StringUtils.leftPad(tv.getReferenceNumber() == null ? "0" : tv.getReferenceNumber(), 12, "0");
            //Terminal ID an 8 Terminal ID
            customData += StringUtils.rightPad(bit41, 8);
            //Merchant ID ans 15 Merchant ID, dari posting
            customData += StringUtils.rightPad(tv.getBit42() == null ? " " : tv.getBit42(), 15, " ");    //sama dg bit 42
            //RRN inquiry an 12 Nomer RRN ketika posting
            customData += StringUtils.leftPad(tv.getInquiryRefNumber() == null ? "0" : tv.getInquiryRefNumber(), 12, "0");

            transaction.setFreeData1(customData);

	    }

	    public Boolean decompose(Object view, Transaction transaction) {
            TransferView tv = (TransferView) view;
            tv.setResponseCode(transaction.getResponseCode());

            String bit48 = transaction.getFreeData1();
            //Dest Bank Code an 3 Left Justified, padded with spaces
//            tv.setBillerCode(bit48.substring(0, 3).trim());
            //Dest Bank Name an 15 Left Justified, padded with spaces
//            tv.setBillerName(bit48.substring(3, 18).trim());
            //Dest Acc Number an 19 Left Justified, padded with spaces
//            37
            //Dest Acc Name an 30 Left Justified, padded with spaces, saat request berisi blank
            tv.setReceiverName(bit48.substring(37,67).trim());

            //tambah amount an 12 Right Justified, padded with zeros, saat request berisi 0
//            tv.setAmount(bit48.substring(67, 79));
            //Cust Ref Number an 16 Left Justified, padded with spaces, saat request berisi blank
            tv.setCustRefAtmb(bit48.substring(79,95).trim());
            //Issuer Bank Code an 3 Left Justified, padded with spaces
            //98
            //Issuer Bank Name an 15 Left Justified, padded with spaces, saat request berisi blank
            //113
            //Issuer Acc Number an 19 Left Justified, padded with spaces, saat request berisi blank
            //132
            //Issuer Acc Name an 30 Left Justified, padded with spaces, saat request berisi blank
            tv.setSenderName(bit48.substring(132,162).trim());
            //Admin Fee an 10 Right Justified, padded with zeros, saat request berisi 0
            //172
            //Retrieval Ref Number an 12 Right Justified, padded with zeros
            tv.setInquiryRefNumber(bit48.substring(172,184).trim());
            //Terminal ID an 8 Terminal ID
            //192
            //Merchant ID ans 15 Merchant ID, dari posting
            //117
            //RRN inquiry an 12 Nomer RRN ketika posting
            //129
            tv.setBit42(transaction.getBit42());
	        return Boolean.TRUE;
	    }
	}

	private static class MncLifePurchaseCheckStatusMessageCustomizer implements MessageCustomizer {
		private MncLifePurchaseCheckStatusMessageCustomizer() {
		}

		public void compose(BaseView view, Transaction transaction) {
			MncLifePurchaseView mv = (MncLifePurchaseView) view;

			String customData = "";
			customData += "MNC ";
			customData += StringUtils.rightPad(mv.getCustomerReference(), 20, " ");
			customData += StringUtils.rightPad(mv.getNamaTertanggung(), 30, " ");
			transaction.setFreeData1(customData);
		}

		public Boolean decompose(Object view, Transaction transaction) {
			MncLifePurchaseView mv = (MncLifePurchaseView) view;

			mv.setResponseCode(transaction.getResponseCode());
			mv.setReferenceNumber(transaction.getReferenceNumber());
			String bit48 = transaction.getFreeData1();
			// Issuer Code an 4 Rata kiri, padding space
			mv.setIssuercode(bit48.substring(0, 4).trim());
			// Nomor Polis1
			try {
				mv.setNomoPolis1(bit48.substring(4, 24).trim());
			} catch (Exception e) {
				mv.setNomoPolis1("");// TODO: handle exception
			}

			// Tanggal Awal Polis N 8 YYYYMMDD
			SimpleDateFormat DtFormat = new SimpleDateFormat("yyyyMMdd");
			try {
				mv.setTglAwalPolis1(DtFormat.parse(bit48.substring(24, 32)));
			} catch (ParseException e) {
				mv.setTglAwalPolis1(new Date());
			}
			// Tanggal Berakhir Polis N 8 YYYYMMDD
			try {
				mv.setTglAkhirPolis1(DtFormat.parse(bit48.substring(32, 40)));
			} catch (ParseException e) {
				mv.setTglAkhirPolis1(new Date());
			}

			try {
				// Nomor Polis 2
				try {
					mv.setNomoPolis2(bit48.substring(40, 60).trim());
				} catch (Exception e) {
					mv.setNomoPolis2("");// TODO: handle exception
				}

				// Tanggal Awal Polis 2 N 8 YYYYMMDD
				try {
					mv.setTglAwalPolis2(DtFormat.parse(bit48.substring(60, 68)));
				} catch (ParseException e) {
					mv.setTglAwalPolis2(new Date());
				}
				// Tanggal Berakhir Polis 2 N 8 YYYYMMDD
				try {
					mv.setTglAkhirPolis2(DtFormat.parse(bit48.substring(68, 76)));
				} catch (ParseException e) {
					mv.setTglAkhirPolis2(new Date());
				}
			} catch (Exception e) {
				mv.setNomoPolis2("");// TODO: handle exception
			}

			try {
				// Nomor Polis 3
				try {
					mv.setNomoPolis3(bit48.substring(76, 96).trim());
				} catch (Exception e) {
					mv.setNomoPolis3("");// TODO: handle exception
				}

				// Tanggal Awal Polis 3 N 8 YYYYMMDD
				try {
					mv.setTglAwalPolis3(DtFormat.parse(bit48.substring(96, 104)));
				} catch (ParseException e) {
					mv.setTglAwalPolis3(new Date());
				}
				// Tanggal Berakhir Polis 3 N 8 YYYYMMDD
				try {
					mv.setTglAkhirPolis3(DtFormat.parse(bit48.substring(104,
							112)));
				} catch (ParseException e) {
					mv.setTglAkhirPolis3(new Date());
				}
			} catch (Exception e) {
				mv.setNomoPolis3("");// TODO: handle exception
			}

			try {
				// Nomor Polis 4
				try {
					mv.setNomoPolis4(bit48.substring(112, 132).trim());
				} catch (Exception e) {
					mv.setNomoPolis4("");// TODO: handle exception
				}

				// Tanggal Awal Polis 4 N 8 YYYYMMDD
				try {
					mv.setTglAwalPolis4(DtFormat.parse(bit48
							.substring(132, 140)));
				} catch (ParseException e) {
					mv.setTglAwalPolis4(new Date());
				}
				// Tanggal Berakhir Polis 4 N 8 YYYYMMDD
				try {
					mv.setTglAkhirPolis4(DtFormat.parse(bit48.substring(140,
							148)));
				} catch (ParseException e) {
					mv.setTglAkhirPolis4(new Date());
				}
			} catch (Exception e) {
				mv.setNomoPolis4("");// TODO: handle exception
			}

			try {
				// Nomor Polis 5
				try {
					mv.setNomoPolis5(bit48.substring(148, 168).trim());
				} catch (Exception e) {
					mv.setNomoPolis5("");// TODO: handle exception
				}

				// Tanggal Awal Polis 5 N 8 YYYYMMDD
				try {
					mv.setTglAwalPolis5(DtFormat.parse(bit48
							.substring(168, 176)));
				} catch (ParseException e) {
					mv.setTglAwalPolis5(new Date());
				}
				// Tanggal Berakhir Polis 5 N 8 YYYYMMDD
				try {
					mv.setTglAkhirPolis5(DtFormat.parse(bit48.substring(176,
							184)));
				} catch (ParseException e) {
					mv.setTglAkhirPolis5(new Date());
				}
			} catch (Exception e) {
				mv.setNomoPolis5("");// TODO: handle exception
			}

			try {
				// Nomor Polis 6
				try {
					mv.setNomoPolis6(bit48.substring(184, 204).trim());
				} catch (Exception e) {
					mv.setNomoPolis6("");// TODO: handle exception
				}

				// Tanggal Awal Polis 6 N 8 YYYYMMDD
				try {
					mv.setTglAwalPolis6(DtFormat.parse(bit48
							.substring(204, 212)));
				} catch (ParseException e) {
					mv.setTglAwalPolis6(new Date());
				}
				// Tanggal Berakhir Polis 6 N 8 YYYYMMDD
				try {
					mv.setTglAkhirPolis6(DtFormat.parse(bit48.substring(212,
							220)));
				} catch (ParseException e) {
					mv.setTglAkhirPolis6(new Date());
				}
			} catch (Exception e) {
				mv.setNomoPolis6("");// TODO: handle exception
			}

			try {
				// Nomor Polis 7
				try {
					mv.setNomoPolis7(bit48.substring(220, 240).trim());
				} catch (Exception e) {
					mv.setNomoPolis7("");// TODO: handle exception
				}

				// Tanggal Awal Polis 7 N 8 YYYYMMDD
				try {
					mv.setTglAwalPolis7(DtFormat.parse(bit48
							.substring(240, 248)));
				} catch (ParseException e) {
					mv.setTglAwalPolis7(new Date());
				}
				// Tanggal Berakhir Polis 5 N 8 YYYYMMDD
				try {
					mv.setTglAkhirPolis7(DtFormat.parse(bit48.substring(248,
							256)));
				} catch (ParseException e) {
					mv.setTglAkhirPolis7(new Date());
				}
			} catch (Exception e) {
				mv.setNomoPolis7("");// TODO: handle exception
			}

			try {
				// Nomor Polis 8
				try {
					mv.setNomoPolis8(bit48.substring(256, 276).trim());
				} catch (Exception e) {
					mv.setNomoPolis8("");// TODO: handle exception
				}

				// Tanggal Awal Polis 8 N 8 YYYYMMDD
				try {
					mv.setTglAwalPolis8(DtFormat.parse(bit48
							.substring(276, 284)));
				} catch (ParseException e) {
					mv.setTglAwalPolis8(new Date());
				}
				// Tanggal Berakhir Polis 8 N 8 YYYYMMDD
				try {
					mv.setTglAkhirPolis8(DtFormat.parse(bit48.substring(284,
							292)));
				} catch (ParseException e) {
					mv.setTglAkhirPolis8(new Date());
				}
			} catch (Exception e) {
				mv.setNomoPolis8("");// TODO: handle exception
			}

			try {
				// Nomor Polis 9

				try {
					mv.setNomoPolis9(bit48.substring(292, 312).trim());
				} catch (Exception e) {
					mv.setNomoPolis9("");// TODO: handle exception
				}

				// Tanggal Awal Polis 9 N 8 YYYYMMDD
				try {
					mv.setTglAwalPolis9(DtFormat.parse(bit48
							.substring(312, 320)));
				} catch (ParseException e) {
					mv.setTglAwalPolis9(new Date());
				}
				// Tanggal Berakhir Polis 9 N 8 YYYYMMDD
				try {
					mv.setTglAkhirPolis9(DtFormat.parse(bit48.substring(320,
							328)));
				} catch (ParseException e) {
					mv.setTglAkhirPolis9(new Date());
				}
			} catch (Exception e) {
				mv.setNomoPolis9("");// TODO: handle exception
			}

			return Boolean.TRUE;
		}
	}

}
