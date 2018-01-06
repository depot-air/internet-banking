package com.dwidasa.engine.service.impl.view;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.SmsRegistrationView;
import com.dwidasa.engine.service.view.MessageCustomizer;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 9/4/12
 */
public class KioskAdministrationMessageCustomizer {
    private static Logger logger = Logger.getLogger( KioskAdministrationMessageCustomizer.class );
    private static KioskAdministrationMessageCustomizer _instance = new KioskAdministrationMessageCustomizer();
	private Map<String, MessageCustomizer> customizerDict = new HashMap<String, MessageCustomizer>();

	private KioskAdministrationMessageCustomizer() {
        customizerDict.put(SmsRegistrationView.class.getSimpleName(), new SmsRegistrationMessageCustomizer()); //ATMB
	}

	public static KioskAdministrationMessageCustomizer instance() {
		return _instance;
	}

	public MessageCustomizer getAdministrationMessageCustomizer(BaseView view) {
		return customizerDict.get(view.getClass().getSimpleName());
	}

    private static class SmsRegistrationMessageCustomizer implements MessageCustomizer{
		private SmsRegistrationMessageCustomizer() {}

		public void compose(BaseView view, Transaction transaction) {
	        SmsRegistrationView srv = (SmsRegistrationView) view;
            logger.info("srv=" + srv);

            srv.setAccountNumber(srv.getAccountNumber().substring(srv.getAccountNumber().length() - 10));
            transaction.setFromAccountNumber(srv.getAccountNumber());
            String bit41 = Constants.getTerminalIdAtmb(transaction.getTerminalId(), srv.getAccountNumber());
            transaction.setTerminalId(bit41);

	        String customData = "";
            //Biller ID   n5  Biller ID :10001
            customData += Constants.SMS_REGISTRATION.BILLER_ID;
            //Payee ID  n4  Payee ID : 0000
            customData += Constants.SMS_REGISTRATION.PAYEE_ID;
            //Product ID   n4  Product ID: 0012
            customData += Constants.SMS_REGISTRATION.PRODUCT_ID;
            //CIF  an8  Nomor CIF -Rata kiri -Padding with space -Saat request diisi blank
            customData += StringUtils.rightPad(srv.getCifNumber(), 8, " ");
            //TIN  an16  TIN USSD & SMS Banking -Rata kiri -Padding with space
            customData += StringUtils.rightPad(srv.getEncryptedTinUssd(), 16, " ");
            //No Kartu   an16  Nomor kartu -Rata kiri -Padding with space
            customData += StringUtils.rightPad(srv.getCardNumber(), 16, " ");
            //No Rekening   an10  Nomor rekening -Rata kiri -Padding with space -Saat request diisi blank(EDC Server)
            customData += StringUtils.rightPad(srv.getAccountNumber(), 10, " ");
            //No Telepon   n15  Nomor telephone yang akan didaftarkan -Rata kiri (left justified) -Space padding
            customData += StringUtils.rightPad(srv.getCustomerReference(), 15, " ");
            //Kode Aktifasi  an20  Kode Aktifasi -Rata kiri -Padding with space -Saat request diisi blank
            customData += StringUtils.rightPad(" ", 20, " ");
            //Counter Jumlah Rekening n2  -Rata kanan -Zero leading padding
            logger.info("srv.getAccountViews().size()=" + srv.getAccountViews().size());
            customData += StringUtils.leftPad("" + srv.getAccountViews().size(), 2, "0");
            //Loop sebanyak jumlah r ekening  (maks : 99)
            for(int i = 0; i < srv.getAccountViews().size(); i++) {
                AccountView accountView = srv.getAccountViews().get(i);
                //No Rekening   an10  Nomor rekening -Rata kiri -Padding with space
                customData += StringUtils.rightPad(accountView.getAccountNumber(), 10, " ");
                //Nama Pemilik Rekiening an30  -Rata kiri -Space  leading padding
                customData += StringUtils.rightPad(srv.getReferenceName(), 30, " ");    //ambil dari srv.getReferenceName()
                //Tipe Rekening  an1  -Rata kiri -Spcae  leading padding
                customData += StringUtils.rightPad(accountView.getAccountType(), 1, " ");
                //Produk Tabungan   an30  -Rata kiri -Space  leading padding
                customData += StringUtils.rightPad(accountView.getProductCode(), 30, " ");
            }
            //End Loop
            transaction.setFreeData1(customData);
	    }

	    public Boolean decompose(Object view, Transaction transaction) {
            SmsRegistrationView srv = (SmsRegistrationView) view;
            srv.setResponseCode(transaction.getResponseCode());
            srv.setReferenceNumber(transaction.getReferenceNumber());
            String bit48 = transaction.getFreeData1();
            logger.info(("bit48=" + bit48));
            //Biller ID   n5  Biller ID :10001
            //0 + 5
            //Payee ID  n4  Payee ID : 0000
            //5 + 4
            //Product ID   n4  Product ID: 0012
            //9 + 4
            //CIF  an8  Nomor CIF -Rata kiri -Padding with spa ce -Saat request diisi blank
            //13 + 8
            //TIN  an16  TIN USSD & SMS Banking -Rata kiri -Padding with space
            //21 + 16
            //No Kartu   an16  Nomor kartu -Rata kiri -Padding with space
            //37 + 16
            //No Rekening   an10  Nomor rekening -Rata kiri -Padding with space -Saat request diisi blank(EDC Server)
            //53 + 10
            //No Telepon   n15  Nomor telephone yang akan didaftarkan -Rata kiri (left justified) -Space padding
            //srv.setActivationCode(bit48.substring(63, 78).trim());
            //Kode Aktifasi  an20  Kode Aktifasi -Rata kiri -Padding with space -Saat request diisi blank
            srv.setActivationCode(bit48.substring(78, 98).trim());
            //Counter Jumlah Rekening n2  -Rata kanan -Zero leading padding
            int n = Integer.parseInt(bit48.substring(98, 100).trim());
            //Loop s ebanyak jumlah r ekening  (maks : 99)
            int dataLong = 71;
            for(int i = 0; i < n; i++) {
                AccountView accountView = srv.getAccountViews().get(i);
                //No Rekening   an10  Nomor rekening -Rata kiri -Padding with space
                accountView.setAccountNumber(bit48.substring(100 + (i * dataLong), 110 + (i * dataLong)).trim());
                //Nama Pemilik Rekiening an30  -Rata kiri -Space  leading padding
                accountView.setCustomerName(bit48.substring(110 + (i * dataLong), 140 + (i * dataLong)).trim());
                //Tipe Rekening  an1  -Rata kiri -Spcae  leading padding
                accountView.setAccountType(bit48.substring(140 + (i * dataLong), 141 + (i * dataLong)).trim());
                //Produk Tabungan   an30  -Rata kiri -Space  leading padding
                accountView.setProductCode(bit48.substring(141 + (i * dataLong), 144 + (i * dataLong)).trim());
            }
            //End Loop

            srv.setTraceNumber(transaction.getStanSixDigit());
            srv.setInformation("Harap lakukan aktifasi dengan SMS ke 3888 \"AKTIF<spasi>"+ srv.getActivationCode() +"\"");
	        return Boolean.TRUE;
	    }
	}

}
