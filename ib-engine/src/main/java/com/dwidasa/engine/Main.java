package com.dwidasa.engine;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.view.PortfolioView;
import com.dwidasa.engine.util.EngineUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Class for test purpose only
 * @author rk
 */
public class Main {
    public static void main(String[] args) {
        //F8814D6AC1BA5F2D
        //String key = "0101010101010101";
        //String pin = "123456";

        //String encryptedPin = EngineUtils.encryptTin(key,pin);
        //String paddingPin = EngineUtils.calculatePaddingTin(key);

        //System.out.println("paddingPin = " + paddingPin);
        //System.out.println("pin = " + pin + "; encryptedPin = " + encryptedPin);
        //System.out.println("decrypt = " + EngineUtils.decryptTin(key, "F8814D6AC1BA5F2D" + paddingPin));


        //System.out.println("decrypted field = " + EngineUtils.decrypt(Constants.SERVER_SECRET_KEY, "e9e4233039fa2e34"));
        //System.out.println("0115815200032971YULIANA CHANDRA LESTARI       1TABUNGAN HARIAN               IDR015000000000000000+000000000000000+000000000100000+000000000000000+000000023913201+".substring(5 + 94, 5 + 109));

        //System.out.println(EngineUtils.generateTerminalIdByAccountNumber("1572888882"));

        //System.out.println(EngineUtils.encryptPin("123456"));

        //System.out.println("encrypt activation code 123456 = " + EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, "123456"));
        //System.out.println("decrypt activation code 841ef0f98f0bb4d1 = " + EngineUtils.decrypt(Constants.SERVER_SECRET_KEY, "841ef0f98f0bb4d1"));
        //System.out.println("CRC32 = " + EngineUtils.generateCrc32("s"));

        //System.out.println("decrypt tin f9613aacd26b4236 = " + EngineUtils.decrypt(Constants.SERVER_SECRET_KEY, "24bdc9b8641c381e837db03cdb3763823344f7a0"));
        try {
            testReplication();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void testPortofolioCASA(){

        List<PortfolioView>  pvs = new ArrayList<PortfolioView>();
        String bit48 = "0002N120002538 1TEGUH REINALDO                1TABUNGAN SADAYA KLASIK        IDR000000000000000000+000000000000000+000000000000000+000000000917298+000000000917298+15270092981TEGUH REINALDO                1TABUNGAN SADAYA PRAKTIS       IDR000000000000000000+000000000000000+000000000000000+000000012947075+000000012947075+";
        String content = bit48.substring(5);
            	int n = Integer.parseInt(bit48.substring(0, 4));
            	if (n > 0) {
	            		/*
						 * reply structure
						 *  01. account number. n10
						 *  02. account status. an1
						 *  03. account name. an30
						 *  04. account type. an1
						 *  05. product type. an30
						 *  06. account currency. an3
						 *  07. branch. an3
						 *  08. plafond. n15 + sign 80+78
						 *  09. blocked balance. n15 + sign
						 *  10. minimum balance. n15 + sign
						 *  11. clearing settlement. n15 + sign
						 *  12. available balance. n15 + sign
						 *  */
	            		//10 + 1 + 30 + 1 + 30 + 3 + 3 + 15 * 5 + 1 * 5 = 158
	            		int LENGTH = 158;
	            		for (int i = 0; i < n; i++) {
	            			int vLength = i * LENGTH;
	            			PortfolioView pv = new PortfolioView();
	            			pv.setAccountNumber(content.substring(0 + vLength, 10 + vLength));
	            			pv.setAccountStatus(Integer.parseInt(content.substring(10 + vLength, 11 + vLength)));
	            			pv.setAccountName(content.substring(11 + vLength, 41 + vLength));
	            			pv.setAccountType(content.substring(41 + vLength, 42 + vLength));
	            			pv.setProductName(content.substring(42 + vLength, 72 + vLength));
	            			pv.setCurrencyCode(content.substring(72 + vLength, 75 + vLength));
	            			pv.setBranchCode(content.substring(75 + vLength, 78 + vLength));
	            			pv.setPlafond(EngineUtils.parseIsoAmount(content.substring(78 + vLength, 94 + vLength)));
	            			pv.setBlockedBalance(EngineUtils.parseIsoAmount(content.substring(94 + vLength, 110 + vLength)));
                            pv.setMinimumBalance(EngineUtils.parseIsoAmount(content.substring(110 + vLength, 126 + vLength)));
	            			pv.setAvailableBalance(EngineUtils.parseIsoAmount(content.substring(142 + vLength, 158 +  vLength)));

                            pvs.add(pv);
	            		}
                }
        System.out.println(PojoJsonMapper.toJson(pvs));

    }

    public static void testPortfolioDeposito(){
        List<PortfolioView>  pvs = new ArrayList<PortfolioView>();
        String bit48 = "0002N2900034078IDR00000030000000006M7002013061720131117290003407802900047118IDR00001720000000006M825201306162013121629000471180";
        String content = bit48.substring(5);
            	int n = Integer.parseInt(bit48.substring(0, 4));
            	if (n > 0) {
	                /*
		        		 * reply structure
		        		 *  01. nomor deposito. an10
		        		 *  02. mata uang. an3
		        		 *  03. nominal. n15
		        		 *  04. jangka waktu. n3
		        		 *  05. bunga per-tahun %. n3
		        		 *  06. tanggal efektif. n8 yyyymmdd
		        		 *  07. tanggal jatuh tempo. n8 yyyymmdd
		        		 *  08. rekening kredit. n10
		        		 *  09. instruksi saat jatuh tempo. a1
		        		 */
	            		//10 + 3 + 15 + 3 + 3 + 8 + 8 + 10 + 1 = 61
	            		int LENGTH = 61;
	            		for (int i = 0; i < n; i++) {
	            			int vLength = i * LENGTH;
	            			PortfolioView pv = new PortfolioView();
	            			pv.setDepositoNumber(content.substring(0 + vLength, 10 + vLength).trim());
	            			pv.setCurrencyCode(content.substring(10 + vLength, 13 + vLength));
	                		pv.setNominalDeposito(EngineUtils.getBigDecimalValue(content.substring(13 + vLength, 28 + vLength), 0));
	                		pv.setJangkaWaktu(Integer.toString(Integer.parseInt(content.substring( 28 + vLength, 30 + vLength ))));
	                		BigDecimal annualRate = new BigDecimal(content.substring( 31 + vLength, 34 + vLength ));
                            annualRate = annualRate.divide(BigDecimal.valueOf(100l));
                            pv.setAnnualRate(EngineUtils.fmtDecimalRate.format(annualRate.doubleValue()));
	                		pv.setEffectiveDate(content.substring(34 + vLength, 42 + vLength));
	                		pv.setDueDate(content.substring(42 + vLength, 50 + vLength));
	                		pv.setCreditAccount(content.substring(50 + vLength, 60 + vLength));
	                		pv.setInstruction(content.substring(60 + vLength, 61 + vLength));
	            			pvs.add(pv);
                        }
                }
        System.out.println(PojoJsonMapper.toJson(pvs));
    }

    public static void testReplication(){
        //String str = "1022000086,IDR,D,17,PDLD1314603988;16,20130701,20130701,PD Penalty Interest Debit,,PD Penalty Interest Debit,ID0010010,SUPARDI,0022";
        String str  = "";
        String[] items = str.split(",");
        for (int i = 0; i < items.length; i++){
            System.out.println(i +" = " + items[i]);
        }
    }

    public static void testListDirectory() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");


        Date processingDate = sdf.parse("20130701");

        String replicationDirectory = "/Users/ryoputranto/bprks-new";

        String statementCandidateFilename = "MUTASI-HARIAN-" + sdf.format(processingDate);
        String balanceCandidateFilename = "MUTASI-HARIAN-ACCT-" + sdf.format(processingDate);

        File statementFile = null;
        File balanceFile = null;

        File replicationDir = new File(replicationDirectory);

        String[] files = replicationDir.list();
        for (int i = 0; i < files.length; i++){



            String strFile = files[i];

            System.out.println("file = " + strFile);
            if (strFile.startsWith(statementCandidateFilename)){

                File file = new File(replicationDir + System.getProperty("file.separator") + strFile);
                if (file.isFile()) {
                    statementFile = file;
                }

            }
            else if (strFile.startsWith(balanceCandidateFilename)){

                File file = new File(replicationDir + System.getProperty("file.separator") + strFile);
                if (file.isFile()) {
                    balanceFile = file;
                }
            }

        }


        if (statementFile != null && balanceFile != null){
            System.out.println("Sukses doong");
        }

        sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = sdf.parse("201307011812");

        System.out.println(date.toString());

    }
}
