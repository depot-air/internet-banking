package com.dwidasa.engine.util.iso;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOMsg;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.model.MFeature;
import com.dwidasa.interlink.model.MIGateBillerProduct;

/**
 * this utility class for pack and unpack iso8583 message
 *
 * @author prayugo
 */
public final class IsoHelper {

    private static Logger logger = Logger.getLogger( IsoHelper.class );
    /**
     * populate an internal transaction data with information contained
     * in the iso message instance after an iso message extracted
     *
     * @param tt     an internal transaction data
     * @param argIso an iso message instance contained all of an extracted message or data
     * @throws Exception throw any exception occurred
     */
    @SuppressWarnings("rawtypes")
    public static void populate(Transaction tt, ISOMsg argIso) throws Exception {
        SimpleDateFormat df07a = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat sdf = new SimpleDateFormat("yy");

        //--response
        Map bits = argIso.getChildren();
        bits.remove(-1);

        for (Iterator itr = bits.keySet().iterator(); itr.hasNext(); ) {
            Integer key = (Integer) itr.next();
            String o = ((ISOComponent) bits.get(key)).getValue().toString();

            try {
                switch (key) {
                    case 1:
                        tt.setCardNumber(o);
                        break;
                    case 2:
                        tt.setCardNumber(o);
                        break;
                    case 3:
                        tt.setTransactionType(o.substring(0,2));
                        break;
                    case 4:
                        tt.setTransactionAmount(new BigDecimal(o));
                        break;
                    case 7:
                        tt.setTransmissionDate(df07a.parse(sdf.format(new Date()) + o));
                        break;
                    case 9:
                        tt.setConversionRate(new BigDecimal(o));
                        break;
                    case 11:
                        tt.setStan(new BigDecimal(o));
                        break;
                    case 18:
                        tt.setMerchantType(o);
                        break;
                    case 22:
                        tt.setMerchantType(o);
                        break;
                    case 29:
                        if (o == null || o.equals("")) {
                            tt.setFeeIndicator("C");
                            tt.setFee(BigDecimal.ZERO);
                        }
                        else {
                            tt.setFeeIndicator(o.substring(0, 1));
                            tt.setFee(new BigDecimal(o.substring(1)));
                        }
                        break;
                    case 35:
                        tt.setCardData1(o);
                        break;
                    case 37:
                        if (o != null) {
                            tt.setReferenceNumber(o);
                        }
                        break;
                    case 38:
                        tt.setApprovalNumber(o);
                        break;
                    case 39:
                        tt.setResponseCode(o);
                        break;
                    case 42:
                        if (tt.getTransactionType().equals(Constants.ATMB.TT_INQUIRY) || tt.getTransactionType().equals(Constants.ATMB.TT_POSTING) || tt.getTransactionType().equals(Constants.ATMB.TT_CEK_STATUS) ||
                        		tt.getTransactionType().equals(Constants.ALTO.TT_INQUIRY) || tt.getTransactionType().equals(Constants.ALTO.TT_POSTING) || tt.getTransactionType().equals(Constants.VOUCHER_PURCHASE_CODE) ) {
                            tt.setBit42(o);
                            break;
                        }
                    case 43:
                        if (tt.getTransactionType().equals(Constants.ATMB.TT_INQUIRY) || tt.getTransactionType().equals(Constants.ATMB.TT_POSTING) ||
                        		tt.getTransactionType().equals(Constants.ALTO.TT_INQUIRY) || tt.getTransactionType().equals(Constants.ALTO.TT_POSTING) ) {
                            tt.setBit43(o);
                            break;
                        }
                    case 47:
                        if (tt.getTransactionType().equals(Constants.ATMB.TT_POSTING)) {
                            tt.setBit47(o);
                            break;
                        }
                    case 48:
                        tt.setFreeData1(o);
                        break;
                    case 49:
                        tt.setCurrencyCode(o);
                        break;
                    case 52:
                        tt.setCardData2(o);
                        break;
                    case 53:
                        tt.setCardData3(o);
                        break;
                    case 54:
                        tt.setBalance(o);
                        break;
                    case 61:
                        tt.setFreeData2(o);
                        break;
                    case 62:
                        tt.setFreeData3(o);
                        break;
                    case 63:
                        tt.setTranslationCode(o);
                        break;
                    case 102:
                        tt.setFromAccountNumber(o);
                        break;
                    case 103:
                        tt.setToAccountNumber(o);
                        break;
                    case 120:
                        tt.setFreeData4(o);
                        break;
                    case 121:
                        tt.setBit121(o);
                        break;
                    case 122:
                        tt.setFreeData5(o);
                        break;
                    case 123:
                        tt.setBit123(o);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tt.setMti(argIso.getMTI());        // -- message type identifier
    }

    /**
     * populate the iso8583 instance object with the information
     * contained in the transaction data
     *
     * @param argIso an iso8583 instance used to pack and unpack iso8583 message
     * @param tt     an internal transaction data
     * @param bitmap iso bitmap
     * @return return the populated iso8583 instance with the information
     *         contained in the internal transaction data
     * @throws Exception any exception will be thrown
     */
    public static ISOMsg populate(ISOMsg argIso, Transaction tt, IsoBitmap bitmap) throws Exception {
        int key = 0;
        String dat = null;
        logger.info("tt=" + tt + " bitmap=" + bitmap);
        String[] abit = bitmap.getBitmap().split(",");

        //untuk debug
        String logIso = "\n";
        for (int i = 0; i < abit.length; i++) {
            key = Integer.parseInt(abit[i]);
            dat = IsoHelper.get(tt, key, bitmap, null);
             /*
            //inquiry ATMB/ALTO, bit 48 ada bagian yg diisi nilainya STAN
            if (key == 37 && tt.getTransactionType().equals(Constants.TRANSFER_INQ_CODE) && tt.getFreeData1() != null && (tt.getFreeData1().length() == 193) ) {
                //3 + 15 + 19 + 30 + 16 + 3 + 15 + 19 + 30 + 30 + 12 + 1 = 193
                String bit48 = tt.getFreeData1();
                String atmbOrAlto = bit48.substring(192);   //bit terakhir
                String withoutStan = bit48.substring(0, 180);
                String customData = withoutStan + dat + atmbOrAlto;
                tt.setFreeData1(customData);
                tt.setReferenceNumber(dat);
            }
            */
            if (tt.getTransactionType().equals(Constants.ATMB.TT_INQUIRY) || tt.getTransactionType().equals(Constants.ATMB.TT_POSTING) || tt.getTransactionType().equals(Constants.ATMB.TT_CEK_STATUS) ) {
                if (key == 39) {
                    dat = null;
                    tt.setResponseCode(dat);
                } else if (key == 42) {
                    dat = StringUtils.rightPad(StringUtils.rightPad(tt.getTerminalId(), 8) + tt.getStanSixDigit() , 15, " ");
                    tt.setBit42(dat);
                }
            }

            if (dat != null) {
                argIso.set(key, dat);
                logIso += " key" + key + ":" + dat;
            }
        }

        logger.info(logIso);
        // -- set the mti information
        argIso.setMTI(bitmap.getMti());
        tt.setMti(argIso.getMTI());
        
        return argIso;
    }

    /**
     * populate the iso8583 instance object with the information
     * contained in the transaction data
     *
     * @param argIso an iso8583 instance used to pack and unpack iso8583 message
     * @param originalBitmap an iso8583 instance used to pack and unpack iso8583 message
     * @param tt     an internal transaction data
     * @param bitmap iso bitmap
     * @return return the populated iso8583 instance with the information
     *         contained in the internal transaction data
     * @throws Exception any exception will be thrown
     */
    public static ISOMsg populate(ISOMsg argIso, ISOMsg originalBitmap, Transaction tt, IsoBitmap bitmap)
            throws Exception {
        int key = 0;
        String dat = null;
        String[] abit = bitmap.getBitmap().split(",");

        for (int i = 0; i < abit.length; i++) {
            key = Integer.parseInt(abit[i]);

            try {
                dat = IsoHelper.get(tt, key, bitmap, originalBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dat != null) {
                argIso.set(key, dat);
            }
        }

        // -- set the mti information
        argIso.setMTI(bitmap.getMti());
        tt.setMti(argIso.getMTI());
        return argIso;
    }

    /**
     * get the value in internal transaction data represent by a bit position of iso8583
     *
     * @param tt     internal transaction data
     * @param argBit a number or position of a bit or field in the iso8583 message
     * @param bitmap iso bitmap
     * @return the value in internal transaction data represent by a bit position of iso8583
     */
    @SuppressWarnings("rawtypes")
	public static String get(Transaction tt, int argBit, IsoBitmap bitmap, ISOMsg originalBitmap) throws Exception {
        // -- move to local variable for thread safety
        // -- iso bit 7 date formatter
        SimpleDateFormat df07 = new SimpleDateFormat("MMddHHmmss");

        // -- iso bit 12 date formatter
        SimpleDateFormat df12 = new SimpleDateFormat("HHmmss");

        // -- iso bit 13 date formatter
        SimpleDateFormat df13 = new SimpleDateFormat("MMdd");

        switch (argBit) {
            case 2:
                return tt.getCardNumber();
            case 3:
                return tt.getTransactionType() +
                       tt.getFromAccountType() +
                       tt.getToAccountType();
            case 4:
                return (tt.getTransactionAmount() == null) ? "0" : String.valueOf(tt.getTransactionAmount().longValue());
            case 7:
                return df07.format(tt.getTransmissionDate());
            case 11:
                return String.valueOf(tt.getStan().intValue());
            case 12:
                return df12.format(tt.getTransmissionDate());
            case 13:
                return df13.format(tt.getTransmissionDate());
            case 15:
                return df13.format(tt.getTransmissionDate());
            case 18:
                return tt.getMerchantType();
            case 22:
                return tt.getBit22();
            case 29:
                if (tt.getFee() == null || tt.getFee().equals(BigDecimal.ZERO)) {
                    return "C00000000";
                }
                else {
                    if (tt.getFeeIndicator() == null) {
                        tt.setFeeIndicator("C");
                    }
                    return tt.getFeeIndicator() + String.format("%08d", tt.getFee().longValue());
                }
            case 33:
            	if (tt.getTransactionType().equals(Constants.MINI_STATMENT_CODE)) {
                    return "627879";
                }
                return "";
            case 35:
                return tt.getCardData1();
            case 37:
                return tt.getReferenceNumber();
            case 39:
                return tt.getResponseCode();
            case 41:
                if (originalBitmap != null) {
					Map bits = originalBitmap.getChildren();
                    ISOComponent ic = (ISOComponent) bits.get(41);
                    return ic == null ? "" : ic.getValue().toString();
                }
                return StringUtils.rightPad(tt.getTerminalId(), 8, " ");
            case 42:
                if (tt.getTransactionType().equals(Constants.ATMB.TT_INQUIRY) || tt.getTransactionType().equals(Constants.ATMB.TT_POSTING) || tt.getTransactionType().equals(Constants.ATMB.TT_CEK_STATUS) || tt.getTransactionType().equals(Constants.VOUCHER_PURCHASE_CODE)) {
                    return tt.getBit42();
                }
                else if (originalBitmap != null) {
                    Map bits = originalBitmap.getChildren();
                    ISOComponent ic = (ISOComponent) bits.get(42);
                    return ic == null ? "" : ic.getValue().toString();
                }
                return StringUtils.rightPad(tt.getTerminalId(), 15, " ");
            case 43:
                if (tt.getTransactionType().equals(Constants.ATMB.TT_INQUIRY) || tt.getTransactionType().equals(Constants.ATMB.TT_POSTING) ||
                		tt.getTransactionType().equals(Constants.ALTO.TT_INQUIRY) || tt.getTransactionType().equals(Constants.ALTO.TT_POSTING) ) {
                    return tt.getBit43();
                }
            case 47:
                if (tt.getTransactionType().equals(Constants.ATMB.TT_POSTING) || tt.getTransactionType().equals(Constants.ALTO.TT_POSTING) ) {
                    return tt.getBit47();
                }
            case 48:
                return tt.getFreeData1();
            case 49:
                return tt.getCurrencyCode();
            case 52:
                return tt.getCardData2();
            case 53:
                if (tt.getCardData3() != null) {
                    return tt.getCardData3();
                }
                else if (originalBitmap != null) {
                    Map bits = originalBitmap.getChildren();
                    ISOComponent ic = (ISOComponent) bits.get(53);
                    return ic == null ? "" : ic.getValue().toString();
                }
                return "";

            case 56:
                CacheManager cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
                //speedy, telkomvision, flexy sama dengan PSTN                
                boolean isSamePSTN = false;
                boolean isDelima = false;
                boolean isAero = false;
                String transType = "";
                if (tt.getTransactionType().equals(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH) || tt.getTransactionType().equals(AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH_MULTI) ||
                		tt.getTransactionType().equals(AirConstants.AEROTICKETING.TRANSACTION_TYPE.GET_AIRLINES_CONNECT) || tt.getTransactionType().equals(AirConstants.AEROTICKETING.TRANSACTION_TYPE.SEARCH_DETAIL) ||
                		tt.getTransactionType().equals(AirConstants.AEROTICKETING.TRANSACTION_TYPE.PRICE_DETAIL) || tt.getTransactionType().equals(AirConstants.AEROTICKETING.TRANSACTION_TYPE.SEARCH_BOOK_V2) ||
                		tt.getTransactionType().equals(AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE) || tt.getTransactionType().equals(AirConstants.VOLTRAS.TRANSACTION_TYPE.OFFICE_INFORMATION) ) {
                	isAero = true;
                }
                if (tt.getProviderCode() != null && tt.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE)) {	//Finnet
                	if (tt.getProductCode() != null && tt.getProductCode().equals(Constants.POSTPAID_PRODCODE.TELKOMSEL_KATU_HALO)) {	//Telkomsel via Finnet
                		//do nothing
                	} else if (tt.getTransactionType().equals(Constants.CASHIN_DELIMA_INQ_CODE) || tt.getTransactionType().equals(Constants.CASHIN_DELIMA_CODE) ||  
                			tt.getTransactionType().equals(Constants.CASHIN_DELIMA_REP_CODE) || tt.getTransactionType().equals(Constants.CASHIN_DELIMA_CHK_CODE) ||
                			tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_INQ_CODE) || tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_CODE) || tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_SMS_VERIFICATION) || 
                			tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_REP_CODE) || tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_CHK_CODE) ||
                			tt.getTransactionType().equals(Constants.CASHTOBANK_INQ_CODE) || tt.getTransactionType().equals(Constants.CASHTOBANK_POS_CODE) || 
                			tt.getTransactionType().equals(Constants.CASHTOBANK_REP_CODE) || tt.getTransactionType().equals(Constants.CASHTOBANK_CHK_CODE) ||
                			tt.getTransactionType().equals(Constants.CASHOUT_ELMO_INQ_CODE) || tt.getTransactionType().equals(Constants.CASHOUT_ELMO_POS_CODE) || 
                			tt.getTransactionType().equals(Constants.CASHOUT_ELMO_REP_CODE) || tt.getTransactionType().equals(Constants.CASHOUT_ELMO_CHK_CODE)
                			){	//Delima
                		isDelima = true;
                	} else {                		
                		if (tt.getTransactionType().equals(Constants.INTERNET_PAYMENT_INQ_CODE) || tt.getTransactionType().equals(Constants.TELCO_PAYMENT_INQ_CODE) || (tt.getProductCode() != null && tt.getProductCode().equals("A07") && tt.getTransactionType().equals(Constants.ENTERTAINMENT_PAYMENT_INQ_CODE)) ) { 
    	                	isSamePSTN = true;
    	                	transType = Constants.TELKOM_PAYMENT_INQ_CODE;
    	                } else if (tt.getTransactionType().equals(Constants.INTERNET_PAYMENT_CODE) || tt.getTransactionType().equals(Constants.TELCO_PAYMENT_CODE) || (tt.getProductCode() != null && tt.getProductCode().equals("A07") && tt.getTransactionType().equals(Constants.ENTERTAINMENT_PAYMENT_CODE)) ) {
    	                	isSamePSTN = true;
    	                	transType = Constants.TELKOM_PAYMENT_CODE;
    	                } else if (tt.getTransactionType().equals(Constants.INTERNET_PAYMENT_REP_CODE) || tt.getTransactionType().equals(Constants.TELCO_PAYMENT_REP_CODE) || (tt.getProductCode() != null && tt.getProductCode().equals("A07") && tt.getTransactionType().equals(Constants.ENTERTAINMENT_PAYMENT_REP_CODE)) ) {
    	                	isSamePSTN = true;
    	                	transType = Constants.TELKOM_PAYMENT_REP_CODE;
    	                }	
                	}
                }

                //undian dan mutasi rekening transaction typenya sama, tidak punya provider, untuk menmbedakan provider code diset = translation code
                if (tt.getTransactionType().equals(Constants.MINI_STATMENT_CODE) || tt.getTransactionType().equals(Constants.INQUIRY_LOTTERY) || tt.getTransactionType().equals(Constants.INQUIRY_NOMOR_UNDIAN)) {
                	tt.setProviderCode(tt.getTranslationCode());
                }
                logger.info("tt.getTransactionType()=" + tt.getTransactionType() + " tt.getProviderCode()=" + tt.getProviderCode());
                
                MFeature feature;
                if (!isSamePSTN) {
                	feature = cacheManager.getFeatureCode(tt.getTransactionType(), tt.getProviderCode());
                } else {
                	feature = cacheManager.getFeatureCode(transType, tt.getProviderCode());
                }
                logger.info("feature=" + feature + " isSamePSTN=" + isSamePSTN);
                if (feature != null) {
                    logger.info("feature.getFeatureName()=" + feature.getFeatureName());
                    logger.info("tt.getBillerCode()=" + tt.getBillerCode() + " tt.getProductCode()=" + tt.getProductCode());
                    String res = feature.getFeatureName();
                    if (tt.getTransactionType().equals(Constants.TRANSFER_TREASURY_INQ_CODE) || tt.getTransactionType().equals(Constants.TRANSFER_TREASURY_CODE)) {
                    	res += ",99999,0001";
                    } else if (tt.getTransactionType().equals(Constants.ALTO.TT_INQUIRY)) {
                    	res += ",13002,0001";
                    } else if (tt.getTransactionType().equals(Constants.ALTO.TT_POSTING)) {
                    	res += ",13002,0002";                    	
                    } else if (tt.getTransactionType().equals(Constants.ATMB.TT_INQUIRY)) {
                    	res += ",13001,0003";
                    } else if (tt.getTransactionType().equals(Constants.ATMB.TT_POSTING)) {
                    	res += ",13001,0004";
                    } else if (tt.getTransactionType().equals(Constants.ATMB.TT_CEK_STATUS)) {
                    	res += ",13001,0002";
                    } else if (tt.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Posting_Pembelian) 
                    		|| tt.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Reprint) || 
                 		   tt.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Cek_Status)){
                    	res += ",04001,0001";
                    } else if (tt.getTransactionType().equals(Constants.INTERNET_PAYMENT_REP_CODE)) {
                    	res += ",07002,0001";
                    } 
                    
                    //Undian
                    else if(tt.getTransactionType().equals(Constants.INQUIRY_LOTTERY)){
                    	res = "INF.LUCKY.DRAW.TYPE";
                    }
                    else if(tt.getTransactionType().equals(Constants.INQUIRY_NOMOR_UNDIAN)){
                    	res = "INF.LUCKY.DRAW";
                    }

                    //Tiket Kramat
//                    else if(tt.getTransactionType().equals(Constants.TIKET_KERETA_DJATI.POS_KERETA_DJATI)){
//                    	res = "ACS.KD.PAY,06002,0001";
//                    }
                    //Dompetku
                    else if(tt.getTransactionType().equals(Constants.DOMPETKU_CASH_IN)){
                    	res += ",11003,0001";
                    }
                    else if(tt.getTransactionType().equals(Constants.DOMPETKU_CASH_OUT)){
                    	res += ",11003,0002";
                    }
                    else if(tt.getToAccountType().equals(Constants.DOMPETKU_TRANSFER_TOKEN)){
                    	res += ",11003,0003";
                    }
                    
                    else if(tt.getTransactionType().equals(Constants.MULTI_REKENING)){
                    	res = "LIST.CARD.ACCT";
                    }
                    
                    //MultiFinance MTF
                    else if(tt.getTransactionType().equals(Constants.MULTIFINANCE_NEW.INQ_MULTI_FINANCE)){
                    	res = "PAC.MTF.INQ,03002,0001";
                    }
                    else if(tt.getTransactionType().equals(Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE)){
                    	res = "PAC.MTF.PAY,03002,0001";
                    }
                    
                   //MultiFinance MNC
                    else if(tt.getTransactionType().equals(Constants.MULTIFINANCE_NEW.INQ_MULTI_FINANCE_MNC)){
                    	res = "PAC.MNCF.INQ,03001,0001";
                    }
                    else if(tt.getTransactionType().equals(Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE_MNC)){
                    	res = "PAC.MNCF.PAY,03001,0001";
                    }
                    
                    //inq Kota Asal
                    else if(tt.getTransactionType().equals(Constants.TIKET_KERETA_DJATI.INQ_KOTA_ASAL_KRAMAT_DJATI)){
                    	res = "ACS.KD.ORG";
                    }else if(tt.getTransactionType().equals(Constants.TIKET_KERETA_DJATI.INQ_KOTA_TUJUAN_KRAMAT_DJATI)){
                    	res = "ACS.KD.DST";
                    }
                    
                    //Paymetn Kartu Kredit BNI
                    else if(tt.getTransactionType().equals(Constants.PAYMENT_KARTU_KREDIT_BNI.PAYMENT_BNI)){
                    	res = "BNI.CC.PAY,08001,0001";
                    }
                    
                    else if (isDelima) {
                    	if (tt.getTransactionType().equals(Constants.CASHIN_DELIMA_INQ_CODE) || tt.getTransactionType().equals(Constants.CASHIN_DELIMA_CODE) ||  
                    			tt.getTransactionType().equals(Constants.CASHIN_DELIMA_REP_CODE) || tt.getTransactionType().equals(Constants.CASHIN_DELIMA_CHK_CODE)){	//Delima cash in
                    		res += ",11002,0001";
                    	} else if (tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_INQ_CODE) || tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_CODE) || tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_SMS_VERIFICATION) || 
                    			tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_REP_CODE) || tt.getTransactionType().equals(Constants.CASHOUT_DELIMA_CHK_CODE)){	//Delima cash out
                    		res += ",11002,0002";
                    	} else if (tt.getTransactionType().equals(Constants.CASHTOBANK_INQ_CODE) || tt.getTransactionType().equals(Constants.CASHTOBANK_POS_CODE) || 
                    			tt.getTransactionType().equals(Constants.CASHTOBANK_REP_CODE) || tt.getTransactionType().equals(Constants.CASHTOBANK_CHK_CODE) ){	//Delima cash tobank
                    		res += ",11002,0004";
                    	} else {
                    		res += ",11002,0005";
                    	}
                    	
                    } else if (isAero || isDelima) {
                    	//do nothing
                    } else  if (tt.getBillerCode() != null && tt.getProductCode() != null) {
                    	MIGateBillerProduct miGateBillerProduct = cacheManager.getIGateBillerProduct(tt.getBillerCode(), tt.getProductCode());
                    	logger.info("miGateBillerProduct=" + miGateBillerProduct);
                    	res += "," + miGateBillerProduct.getIndustrialCode() + miGateBillerProduct.getiGateBillerCode();
                    	String productCode = miGateBillerProduct.getiGateProductCode();
                    	if (tt.getTransactionType().equals(Constants.VOUCHER_PURCHASE_CODE) || tt.getTransactionType().equals(Constants.VOUCHER_PURCHASE_CHK_CODE)) {                    		
                        	long perThousand = Long.parseLong(tt.getDenomination()) / 1000;
                        	if (perThousand >= 1000) {
                        		productCode = String.valueOf(perThousand);
                        	} else if (perThousand >= 100) {
                        		productCode = "0" + perThousand;
                        	} else if (perThousand >= 10) {
                        		productCode = "00" + perThousand;
                        	} else {
                        		productCode = "000" + perThousand;
                        	}
                        }
                        res += "," + productCode;
                    }
                    return res;
                }
                return null;
            case 61:
                return tt.getFreeData2();
            case 62:
                return tt.getFreeData3();
            case 63:
                return tt.getTranslationCode();
            case 102:
                return tt.getFromAccountNumber();
            case 103:
                if (tt.getToAccountNumber() == null) tt.setToAccountNumber("");
                return tt.getToAccountNumber();
            case 104:
                return tt.getDescription();
            case 120:
                return tt.getFreeData4();
            case 121:
                return tt.getBit121();
            case 122:
                return tt.getFreeData5();
            case 123:
                return tt.getBit123();

            default:
                return bitmap.getCustomItem(String.valueOf(argBit));
        }
    }
}
