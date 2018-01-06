package com.dwidasa.engine.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.CRC32;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.acegisecurity.providers.encoding.ShaPasswordEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;

/**
 * This class will contain general utility for Internet Banking application.
 * @author rk
 */
public final class EngineUtils {
    private static Logger logger = Logger.getLogger( EngineUtils.class );
	public static final DateTimeFormatter fmtDate = DateTimeFormat.forPattern("dd-MMM-yyyy");
	public static final DateTimeFormatter fmtDateTime = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm");
	public static final DateTimeFormatter fmtDateYMD = DateTimeFormat.forPattern("yyyyMMdd");
	public static final DateTimeFormatter fmtDateTimeYMDHMS = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	public static final DateTimeFormatter fmtDateYM = DateTimeFormat.forPattern("yyyyMM");
	public static final DateTimeFormatter fmtDateD = DateTimeFormat.forPattern("dd");
	public static final DateTimeFormatter fmtDay = DateTimeFormat.forPattern("EEE");
	public static final DateTimeFormatter fmtMonth = DateTimeFormat.forPattern("MMM");
	public static final DateTimeFormatter fmtYear= DateTimeFormat.forPattern("yyyy");
	public static final DateTimeFormatter fmtMonthYear = DateTimeFormat.forPattern("MM/yy");
	public static final DateTimeFormatter fmtMonthYearLong = DateTimeFormat.forPattern("MMM yyyy");
	public static final DateTimeFormatter fmtDatePrima = DateTimeFormat.forPattern("yyMMdd");

    public static final DecimalFormat fmtDecimalRate = new DecimalFormat("#.00");
    /**
     * Split phone number into area or operator code and phone number itself.
     * @param phoneNumber phone number
     * @return splitted phone number, first index occupied by area or operator code,
     * second index by remaining phone number
     */
    public static String[] splitPhoneNumber(String phoneNumber) {
        String areaCode = phoneNumber.substring(0, 3);
        String phone;

        if (areaCode.equals("021") || areaCode.equals("022") || areaCode.equals("024") ||
                areaCode.equals("031") || areaCode.equals("061")) {
            areaCode = "0" + areaCode;
            phone = phoneNumber.substring(3);
        }
        else {
            areaCode = phoneNumber.substring(0, 4);
            phone = phoneNumber.substring(4);
        }

        return new String[] {areaCode, phone};
    }

    /**
     * Deserialize stored json string into its real object representation.
     * @param data transaction data record.
     * @return object
     */
    public static Object deserialize(TransactionData data) {
        BaseView object;
        try {
            object = (BaseView) PojoJsonMapper.fromJson(data.getTransactionData(), Class.forName(data.getClassName()));
        } catch (ClassNotFoundException e) {
            object = null;
        }

        return object;
    }

    /**
     * Set transaction status based from response code returned.
     * @param transaction transaction object to be set
     */
    public static void setTransactionStatus(Transaction transaction) {
        if (transaction.getStatus() == null) {
            if (transaction.getResponseCode() == null) {
                transaction.setStatus(Constants.FAILED_STATUS);
            }
            else if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
                transaction.setStatus(Constants.SUCCEED_STATUS);
            }
            else if (transaction.getResponseCode().equals(Constants.TIMEOUT_CODE)) {
                transaction.setStatus(Constants.PENDING_STATUS);
            }
            else {
                transaction.setStatus(Constants.FAILED_STATUS);
            }
        }
    }

    /**
     * Create transaction data object from provided input.
     * @param view view object
     * @param transactionId transaction id referenced
     * @return transaction data object
     */
    public static TransactionData createTransactionData(BaseView view, Long transactionId) {
        TransactionData data = new TransactionData();
        data.setTransactionId(transactionId);
        data.setClassName(view.getClass().getName());
        data.setTransactionData(PojoJsonMapper.toJson(view));
        data.setCreated(new Date());
        data.setCreatedby(view.getCustomerId());
        data.setUpdated(new Date());
        data.setUpdatedby(view.getCustomerId());

        return data;
    }

    /**
     * Convert a given value to return bigdecimal type where precision represent how many
     * character in value parameter belongs to fractional part.
     * @param value value to convert
     * @param precision precision length
     * @return bigdecimal representation of value with given precision
     */
    public static BigDecimal getBigDecimalValue(String value, int precision) {
        if (value.replaceAll("0", "").equals("")) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(value.substring(0, value.length() - precision) + "." +
                value.substring(value.length() - precision));
    }

    /**
     * Check whether an account is merchant account or not.
     * @param accountNumber account number
     * @return true if a merchant
     */
    public static boolean isMerchant(String accountNumber) {
        int infix = Integer.valueOf(accountNumber.substring(2, 4));
        boolean result = false;

        if (infix >= 71 && infix <= 75) {
            result = true;
        }

        return result;
    }

    /**
     * Parse a line split up by their length as specified in lengths.
     * If length in lengths array have negative sign that mean skip characters ahead as many as specified by the number
     * <br/>example: line = 012345678ABC9, lengths 2,4,3,-3,1
     * This method will return an array containing '01','2345','678', '9'
     * @param line string to be parsed
     * @param lengths an array of length that define the size of each element in returned array
     * @return array of parsed string data
     */
    public static String[] parseByLength(String line, Integer... lengths) {
        List<String> result = new ArrayList<String>();
        String item = "";

        for (int i = 0, idx = 0, ctr = 0; idx < lengths.length; i++) {
            item += line.charAt(i);
            ctr++;
            if (lengths[idx] > 0 && ctr == lengths[idx]) {
                result.add(item);
                item = "";
                ctr = 0;
                idx++;
            }
            else if (ctr == Math.abs(lengths[idx])) {
                item = "";
                ctr = 0;
                idx++;
            }
        }

        return result.toArray(new String[0]);
    }
    
    /**
     * get root cause string of an exception
     * @param t exception's throwable
     * @return error string
     */
    static public String getRootCause(Throwable t) {
		Throwable cause = t;
		Throwable subCause = cause.getCause();
		while (subCause != null && !subCause.equals(cause)) {
			cause = subCause;
			subCause = cause.getCause();
		}
		return cause.getMessage();
	}

    /**
     * Decrypt pin using AES
     * @param key string secret key
     * @param data string to be decrypted
     * @return
     */
    public static String decrypt(String key, String data){
        String result;

        try {
            Cipher cipher = Cipher.getInstance("DES");
            byte[] dataByte = Hex.decodeHex(data.toCharArray());
            SecretKeySpec k = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "DES");
            cipher.init(Cipher.DECRYPT_MODE, k);
            result = new String(cipher.doFinal(dataByte));
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("IB-0500", "DCRY1");
        } catch (NoSuchPaddingException e) {
            throw new BusinessException("IB-0500", "DCRY2");
        } catch (InvalidKeyException e) {
            throw new BusinessException("IB-0500", "DCRY3");
        } catch (IllegalBlockSizeException e) {
            throw new BusinessException("IB-0500", "DCRY4");
        } catch (BadPaddingException e) {
            throw new BusinessException("IB-0500", "DCRY5");
        } catch (DecoderException e) {
            throw new BusinessException("IB-0500", "DCRY6");
        }

        return result;
    }

    /**
     * Encrypt pin using AES
     * @param key string secret key
     * @param data string to be encrypted
     * @return
     */
    public static String encrypt(String key, String data){
        String result;

        try {
            Cipher cipher = Cipher.getInstance("DES");
            byte[] dataByte = data.getBytes();
            SecretKeySpec k = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            result = new String(Hex.encodeHex(cipher.doFinal(dataByte)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP1");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP2");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP3");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP4");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP5");
        } catch (DecoderException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "DCRY6");
        }
        return result;
    }
    
    public static String encryptSHA(String username, String password){
        String result;

        try {
    	    ShaPasswordEncoder encoder = new ShaPasswordEncoder();    	    
    	    result = encoder.encodePassword(password, username);    	    
        } catch (Exception e) {
        	e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP1");
        }
        return result;
    }

    public static String calculatePaddingTin(String key) {
        String result = encryptTin(key,"123456");
        return result.substring(16,32).toUpperCase();
    }

    public static String encryptTin(String key, String tin){
        String result;
        String data = tin + "FFFFFFFFFF";
        try {
            Cipher cipher = Cipher.getInstance("DES");
            byte[] dataByte = Hex.decodeHex(data.toCharArray());
            SecretKeySpec k = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "DES");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            result = new String(Hex.encodeHex(cipher.doFinal(dataByte)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP1");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP2");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP3");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP4");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "CRYP5");
        } catch (DecoderException e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "DCRY6");
        }
        return result.toUpperCase();
    }

    public static String decryptTin(String key, String tin){
        String result;
        try {
            Cipher cipher = Cipher.getInstance("DES");
            byte[] data = Hex.decodeHex(tin.toCharArray());
            SecretKeySpec k = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "DES");
            cipher.init(Cipher.DECRYPT_MODE, k);
            result = new String(Hex.encodeHex(cipher.doFinal(data)));
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (NoSuchPaddingException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (InvalidKeyException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (IllegalBlockSizeException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (BadPaddingException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (DecoderException e) {
            throw new BusinessException("IB-0500", "DECRY");
        }

        return result;
    }

    /**
     * Encrypt pin using specified secret key on table m_parameter
     * @param pin
     * @return
     */
    /* Diganti waktu pake ITM pak Ucok, pake getEncryptPin
    public static String encryptPin(String pin) {
        String result;
        CacheManager cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
        String key = cacheManager.getParameter("PRIVATE_KEY").getParameterValue();
        //String key = "0404040404040404";
        String data = pin + "FFFFFFFFFF";

        try {
            Cipher cipher = Cipher.getInstance("DES");
            byte[] dataBytes = Hex.decodeHex(data.toCharArray());
            byte[] keyBytes = Hex.decodeHex(key.toCharArray());
            SecretKeySpec k = new SecretKeySpec(keyBytes, "DES");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            String res = new String(Hex.encodeHex(cipher.doFinal(dataBytes)));
            result = res.substring(0, 16).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("IB-0500", "CRYP1");
        } catch (NoSuchPaddingException e) {
            throw new BusinessException("IB-0500", "CRYP2");
        } catch (InvalidKeyException e) {
            throw new BusinessException("IB-0500", "CRYP3");
        } catch (IllegalBlockSizeException e) {
            throw new BusinessException("IB-0500", "CRYP4");
        } catch (BadPaddingException e) {
            throw new BusinessException("IB-0500", "CRYP5");
        } catch (DecoderException e) {
            throw new BusinessException("IB-0500", "CRYP6");
        }

        return result;
    }
	*/
    public static String decryptPin(String key, String pin){
        String result;
        try {
            Cipher cipher = Cipher.getInstance("DES");
            byte[] data = Hex.decodeHex(pin.toCharArray());            
            SecretKeySpec k = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "DES");
            cipher.init(Cipher.DECRYPT_MODE, k);
            result = new String(Hex.encodeHex(cipher.doFinal(data)));
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (NoSuchPaddingException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (InvalidKeyException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (IllegalBlockSizeException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (BadPaddingException e) {
            throw new BusinessException("IB-0500", "DECRY");
        } catch (DecoderException e) {
            throw new BusinessException("IB-0500", "DECRY");
        }

        return result;
    }

    /**
     * Generate terminal id (TID) based on account number
     * It will convert 10 digit decimal account number to 6 digit base-36 characters representation
     * @param accountNumber
     * @return  terminal ID for specified account number
     */
    public static String generateTerminalIdByAccountNumber(String accountNumber){

        long base10AccountNumber = Long.parseLong(accountNumber);
        String base36AccountNumber = Long.toString(base10AccountNumber,36).toUpperCase();

        return "MB" + StringUtils.leftPad(base36AccountNumber,6,'0').substring(0,6);
    }

    /**
     * Generate string of CRC32 checksum
     * @param str
     * @return
     */
    public static String generateCrc32(String str) {
		byte[] input = str.getBytes();
		CRC32 crc32 = new CRC32();
		crc32.update(input, 0, input.length);
		return String.valueOf(crc32.getValue());
	}

    public static String generateMd5(String str) {
    	try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] input = str.getBytes();
	        md.update(input, 0, input.length);

	        byte[] mdbytes = md.digest();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
    	} catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
        	return "-";
        }
	}

    public static String generateChallengeCode(String crc32, String pin){

        String crc = StringUtils.rightPad(crc32, 10, '0');
        int i1 = Integer.parseInt(crc.substring(0,8));
        int i2 = Integer.parseInt(crc.substring(8,10) + pin);

        int i = i1+i2;

        return Integer.toString(i).substring(0,8);
    }

	public static List<Integer> getBitsFromChar(char ch) {
		List<Integer> result = new ArrayList<Integer>(4);
		switch (ch) {
			case '0' : 	result.add(0);	result.add(0);	result.add(0);	result.add(0); break;
			case '1' :	result.add(0);	result.add(0);	result.add(0);	result.add(1); break;
			case '2' :  result.add(0);	result.add(0);	result.add(1);	result.add(0); break;
			case '3' :  result.add(0);	result.add(0);	result.add(1);	result.add(1); break;
			case '4' :  result.add(0);	result.add(1);	result.add(0);	result.add(0); break;
			case '5' :  result.add(0);	result.add(1);	result.add(0);	result.add(1); break;
			case '6' :  result.add(0);	result.add(1);	result.add(1);	result.add(0); break;
			case '7' :  result.add(0);	result.add(1);	result.add(1);	result.add(1); break;
			case '8' :  result.add(1);	result.add(0);	result.add(0);	result.add(0); break;
			case '9' :  result.add(1);	result.add(0);	result.add(0);	result.add(1); break;
			case 'A' :  result.add(1);	result.add(0);	result.add(1);	result.add(0); break;
			case 'B' :  result.add(1);	result.add(0);	result.add(1);	result.add(1); break;
			case 'C' :  result.add(1);	result.add(1);	result.add(0);	result.add(0); break;
			case 'D' :  result.add(1);	result.add(1);	result.add(0);	result.add(1); break;
			case 'E' :  result.add(1);	result.add(1);	result.add(1);	result.add(0); break;
			case 'F' :  result.add(1);	result.add(1);	result.add(1);	result.add(1); break;
		}
		return result;
	}
	public static char getCharFromBits(List<Integer> ints) {		
		String sint = "";
		for (Integer integer : ints) {
			sint += integer;
		}
		if (sint.equals("0000")) return '0';
		else if (sint.equals("0001")) return '1';
		else if (sint.equals("0010")) return '2';
		else if (sint.equals("0011")) return '3';
		else if (sint.equals("0100")) return '4';
		else if (sint.equals("0101")) return '5';
		else if (sint.equals("0110")) return '6';
		else if (sint.equals("0111")) return '7';
		else if (sint.equals("1000")) return '8';
		else if (sint.equals("1001")) return '9';
		else if (sint.equals("1010")) return 'A';
		else if (sint.equals("1011")) return 'B';
		else if (sint.equals("1100")) return 'C';
		else if (sint.equals("1101")) return 'D';
		else if (sint.equals("1110")) return 'E';
		else return 'F';
	}
	public static String getXorEdc(String block1, String block2) {
		List<Integer> bits1 = new ArrayList<Integer>();
		for (int i = 0; i < block1.length(); i++) {
			List<Integer> bitsChar = getBitsFromChar(block1.charAt(i));
			bits1.addAll(bitsChar);			
		}
		List<Integer> bits2 = new ArrayList<Integer>();
		for (int i = 0; i < block2.length(); i++) {
			List<Integer> bitsChar = getBitsFromChar(block2.charAt(i));
			bits2.addAll(bitsChar);			
		}
		List<Integer> bitsResult = new ArrayList<Integer>();
		for (int i = 0; i < bits1.size(); i++) {
			int intXor = bits1.get(i) ^ bits2.get(i);
			bitsResult.add(intXor);
		}
		String res = "";
		for (int i = 0; i < block1.length(); i++) {	
			List<Integer> inner = bitsResult.subList(i * 4, (i + 1) * 4);
			res += getCharFromBits(inner);
		}
		return res;
	}
	public static String edcEncrypt(String key, String data) {
		String result = "";
        try {
            Cipher cipher = Cipher.getInstance("DES");
            byte[] dataBytes = Hex.decodeHex(data.toCharArray());
            byte[] keyBytes = Hex.decodeHex(key.toCharArray());
            SecretKeySpec k = new SecretKeySpec(keyBytes, "DES");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            String res = new String(Hex.encodeHex(cipher.doFinal(dataBytes)));
            result = res.toUpperCase();            
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("IB-0500", "CRYP1");
        } catch (NoSuchPaddingException e) {
            throw new BusinessException("IB-0500", "CRYP2");
        } catch (InvalidKeyException e) {
            throw new BusinessException("IB-0500", "CRYP3");
        } catch (IllegalBlockSizeException e) {
            throw new BusinessException("IB-0500", "CRYP4");
        } catch (BadPaddingException e) {
            throw new BusinessException("IB-0500", "CRYP5");
        } catch (DecoderException e) {
            throw new BusinessException("IB-0500", "CRYP6");
        }
        return result;
	}
	public static String getEncryptedPin(String pin, String cardNumber) {
		String block1 = "06" + pin + "FFFFFFFF";
		String block2 = "0000" + cardNumber.substring(3, cardNumber.length() - 1);
		String xorRes = getXorEdc(block1, block2);
		String encrypted = edcEncrypt(Constants.EDC_SERVER_SECRET_KEY, xorRes).substring(0,16);
		return encrypted;
	}
	public static String getEdcPin(String encryptedPin, String cardNumber) {
		/*
		String cardNumber = "6278790012500001";
    	String block1 = "06123456FFFFFFFF";		
		String block2 = "0000" + cardNumber.substring(3, cardNumber.length() - 1);
		String xorRes = getXorEdc(block1, block2);
		System.out.println("xorRes=" + xorRes);
		String fromEdc = edcEncrypt(Constants.EDC_SERVER_SECRET_KEY, xorRes).substring(0,16);
		System.out.println("fromEdc=" + fromEdc);
		
		//E36CEF0D49BBE0A2B21C053F63AEF2B8"
		//E36CEF0D49BBE0A2FFFFFFFFFFFFFFFF"
		*/
		String block1 = "FFFFFFFFFFFFFFFF";
		String block2 = "0000" + cardNumber.substring(3, cardNumber.length() - 1);		
//		logger.info("block2=" + block2);
		String block2Decrypt = "FFFFFFFF" + block2.substring(8);
		String xorRes = getXorEdc(block1, block2Decrypt);
//		logger.info("xorRes=" + xorRes);
		String complement = edcEncrypt(Constants.EDC_SERVER_SECRET_KEY, xorRes).substring(16);
//		logger.info("complement=" + complement);
		String decripted = decryptPin(Constants.EDC_SERVER_SECRET_KEY, encryptedPin + complement).toUpperCase();
//		logger.info("decripted=" + decripted);
		xorRes = getXorEdc(decripted, block2);		
		return xorRes.substring(2, 8);
	}
	public static String getIpPublic() {
		String ip = "";
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			ip = thisIp.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();		
		}
		return ip;
	}

    public static BigDecimal parseIsoAmount(String isoAmount){

        BigDecimal retval = new BigDecimal(isoAmount.substring(0,15));
        if ("-".equals(isoAmount.substring(15,16))){
            retval = retval.multiply(BigDecimal.valueOf(-1));
        }

        return retval;
    }

    public static void main(String[] args) {
    	/*
    	System.err.println(encryptSHA("ERICKFRE2348", "111111"));
    	
    	String pin = getEdcPin("347ADFE4B40CB7A4", "6278791527029098");
    	System.out.println("pin=" + pin);
    	
    	String encryptedPin = EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, pin);
    	System.out.println("encryptedPin=" + encryptedPin);
    	*/
    	/*
    	//cardNumber=6278790012500001, sub=879001250000
    	String cardNumber = "6278790096000001";
    	System.out.println("cardNumber=" + cardNumber);
    	String block1 = "06123456FFFFFFFF";
    	System.out.println("block1=" + block1);
		String block2 = "0000" + cardNumber.substring(3, cardNumber.length() - 1);
		System.out.println("block2=" + block2);
		String xorRes = getXorEdc(block1, block2);
		System.out.println("block1 XOR block2=" + xorRes);
		String fromEdc = edcEncrypt(Constants.EDC_SERVER_SECRET_KEY, xorRes).substring(0,16);
		System.out.println("encrypted=" + fromEdc);
		
			
		//E36CEF0D49BBE0A2B21C053F63AEF2B8
		block1 = "FFFFFFFFFFFFFFFF";		
		String block2Decrypt = "FFFFFFFF" + block2.substring(8);
		xorRes = getXorEdc(block1, block2Decrypt);
		System.out.println("xorRes=" + xorRes);
		String complement = edcEncrypt(Constants.EDC_SERVER_SECRET_KEY, xorRes).substring(16);
		System.out.println("complement=" + complement);
		
		String decripted = decryptPin(Constants.EDC_SERVER_SECRET_KEY, fromEdc + complement).toUpperCase();
		System.out.println("decripted=" + decripted);
		
		xorRes = getXorEdc(decripted, block2);		
		System.out.println("pin=" + xorRes.substring(2, 8) );
		

    	String pinX = getEdcPin("347ADFE4B40CB7A4", "6278791527029098");
    	System.out.println("\n\nXpin=" + pinX);
    	
    	System.err.println("portofolio="+ getBigDecimalValue("000000005742243", 0));
    	*/

    	//cardNumber=6278790012500001, sub=879001250000
//    	String cardNumber = "6278790035000001";
//    	System.out.println("cardNumber=" + cardNumber);
//    	String block1 = "06123456FFFFFFFF";
//    	System.out.println("block1=" + block1);
//		String block2 = "0000" + cardNumber.substring(3, cardNumber.length() - 1);
//		System.out.println("block2=" + block2);
//		String xorRes = getXorEdc(block1, block2);
//		System.out.println("block1 XOR block2=" + xorRes);
//		String fromEdc = edcEncrypt(Constants.EDC_SERVER_SECRET_KEY, xorRes).substring(0,16);
//		System.out.println("encrypted=" + fromEdc);
		
//		bit-2 = 6278790127040710
//		bit-52 = 48F5E487B870D9FD
//		280582
		
//		String cardNumber = "6278790127040710";
//    	System.out.println("cardNumber=" + cardNumber);
//    	String block1 = "06280582FFFFFFFF";
//    	System.out.println("block1=" + block1);
//		String block2 = "0000" + cardNumber.substring(3, cardNumber.length() - 1);
//		System.out.println("block2=" + block2);
//		String xorRes = getXorEdc(block1, block2);
//		System.out.println("block1 XOR block2=" + xorRes);
//		String fromEdc = edcEncrypt(Constants.EDC_SERVER_SECRET_KEY, xorRes);
//		System.out.println("encrypted=" + fromEdc + " encrypted.substring(0,16)=" + fromEdc.substring(0,16));
//				
////		System.out.println("decrypt=" + decrypt(Constants.SERVER_SECRET_KEY, "a92b705d2a333d59"));
////		116623

    	System.out.println(EngineUtils.decrypt(Constants.SERVER_SECRET_KEY, "62f3d8b076d829d6"));
    }
    
    
	public static String getCardNumberMasked(String str) {
		if (str == null || str.length() <= 6)
			return str;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (i < 6) {
				sb.append(str.substring(i, i + 1));
			}
			if (i > 6 && i < 12) {

				String ch = str.substring(i, i + 1);
				if (" ".equals(ch)) {
					sb.append(" ");
				} else {
					sb.append("*");
				}
			} else if (i > 11) {
				String ch = str.substring(i, i + 1);
				sb.append("" + ch);
			}
		}
		return sb.toString();
	}
    
    
}
