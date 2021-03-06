package com.dwidasa.ib.pages.util;

import com.dwidasa.engine.model.view.AccountStatementView;

import java.math.BigDecimal;
import java.util.*;

public class DemoListUtils {
    public static Map<Integer, String> getBit48s() {
        Map<Integer, String> bit48s = new HashMap();
        bit48s.put(0, "000000000010840001051");
        bit48s.put(1, "000000000010840001151");
        bit48s.put(2, "000000000010840001100");
        bit48s.put(3, "000000000010840002201");
        bit48s.put(4, "000000000010840002100");
        bit48s.put(5, "000000000010840003300");
        bit48s.put(6, "000000000010840004101");
        bit48s.put(7, "000000000010840004151");
        bit48s.put(8, "000000000010840004050");
        bit48s.put(9, "000000000010840005300");
        bit48s.put(10, "000000000010840006051");
        bit48s.put(11, "000000000010840006151");
        bit48s.put(12, "000000000010840006100");
        bit48s.put(13, "000000000010840007101");
        bit48s.put(14, "000000000010840007051");
        bit48s.put(15, "000000000010840007151");
        return bit48s;
    }

    public static Map<Integer, String> getBit120s() {
        Map<Integer, String> bit120s = new HashMap();
        bit120s.put(0, "050120130722 203D000000000542709000000005882291+0220130722 206D000000000724293000000005157998+0320130722 200D000000002028444000000003129554+0420130722 203D000000000031396000000003098158+0520130722 207D000000000088094000000003010064+");
        bit120s.put(1, "150620130723 103C000000114000000000000117010064+0720130724 206D000000001000000000000116010064+0820130725 200D000000001000000000000115010064+0920130726 203D000000001000000000000114010064+1020130727 207D000000001000000000000113010064+1120130728 201D000000001000000000000112010064+1220130729 202D000000001000000000000111010064+1320130730 203D000000001000000000000110010064+1420130801 204D000000001000000000000109010064+1520130802 205D000000001000000000000108010064+1620130803 206D000000001000000000000107010064+1720130804 207D000000001000000000000106010064+1820130805 208D000000001000000000000105010064+1920130806 209D000000001000000000000104010064+2020130807 201D000000001000000000000103010064+");
        bit120s.put(2, "102120130808 203D000000000500000000000102510064+2220130809 206D000000000500000000000102010064+2320130810 200D000000000500000000000101510064+2420130811 203D000000000500000000000101010064+2520130812 207D000000000500000000000100510064+2620130813 201D000000000500000000000100010064+2720130814 202D000000000500000000000099510064+2820130815 203D000000000500000000000099010064+2920130816 204D000000000500000000000098510064+3020130817 205D000000000500000000000098010064+");
        bit120s.put(3, "200120130818 203D000000000500000000000098010064+0220130819 206D000000000500000000000097510064+0320130820 200D000000000500000000000097010064+0420130821 203D000000000500000000000096510064+0520130822 207D000000000500000000000096010064+0620130823 203D000000000500000000000095510064+0720130824 206D000000000500000000000095010064+0820130825 200D000000000500000000000094510064+0920130826 203D000000000500000000000094010064+1020130827 207D000000000500000000000093510064+1120130828 203D000000000500000000000093010064+1220130829 206D000000000500000000000092510064+1320130830 200D000000000500000000000092010064+1420130901 203D000000000500000000000091510064+1520130902 207D000000000500000000000091010064+1620130903 203D000000000500000000000090510064+1720130904 206D000000000500000000000090010064+1820130905 200D000000000500000000000089510064+1920130906 203D000000000500000000000089010064+2020130907 207D000000000500000000000088510064+");
        bit120s.put(4, "102120130908 203D000000000500000000000088010064+2220130909 206D000000000500000000000087510064+2320130910 200D000000000500000000000087010064+2420130911 203D000000000500000000000086510064+2520130912 207D000000000500000000000086010064+2620130913 203D000000000500000000000085510064+2720130914 206D000000000500000000000085010064+2820130915 200D000000000500000000000084510064+2920130916 203D000000000500000000000084010064+3020130917 207D000000000500000000000083510064+");
        bit120s.put(5, "050120130918 203D000000000500000000000083010064+0220130919 206D000000000500000000000082510064+0320130920 200D000000000500000000000082010064+0420130921 203D000000000500000000000081510064+0520130922 207D000000000500000000000081010064+0620130923 203D000000000500000000000080510064+0720130924 206D000000000500000000000080010064+0820130925 200D000000000500000000000079510064+0920130926 203D000000000500000000000079010064+1020130927 207D000000000500000000000078510064+1120130928 203D000000000500000000000078010064+1220130929 206D000000000500000000000077510064+1320130930 200D000000000500000000000077010064+1420131001 203D000000000500000000000076510064+1520131002 207D000000000500000000000076010064+1620131003 203D000000000500000000000075510064+1720131004 206D000000000500000000000075010064+1820131005 200D000000000500000000000074510064+1920131006 203D000000000500000000000074010064+2020131007 207D000000000500000000000073510064+2120131008 203D000000001000000000000072510064+2220131009 206D000000001000000000000071510064+2320131010 200D000000001000000000000070510064+2420131011 203D000000001000000000000069510064+2520131012 207D000000001000000000000068510064+2620131013 203D000000001000000000000067510064+2720131014 206D000000001000000000000066510064+2820131015 200D000000001000000000000065510064+2920131016 203D000000001000000000000064510064+3020131017 207D000000001000000000000063510064+");
        bit120s.put(6, "100120131018 203D000000001000000000000063510064+0220131019 206D000000001000000000000062510064+0320131020 200D000000001000000000000061510064+0420131021 203D000000001000000000000060510064+0520131022 207D000000001000000000000059510064+0620131023 203D000000001000000000000058510064+0720131024 206D000000001000000000000057510064+0820131025 200D000000001000000000000056510064+0920131026 203D000000001000000000000055510064+1020131027 207D000000001000000000000054510064+");
        bit120s.put(7, "151120131018 203D000000000500000000000054010064+1220131019 206D000000000500000000000053510064+1320131020 200D000000000500000000000053010064+1420131021 203D000000000500000000000052510064+1520131022 207D000000000500000000000052010064+1620131023 203D000000000500000000000051510064+1720131024 206D000000000500000000000051010064+1820131025 200D000000000500000000000050510064+1920131026 203D000000000500000000000050010064+2020131027 207D000000000500000000000049510064+2120131028 203D000000000500000000000049010064+2220131029 206D000000000500000000000048510064+2320131030 200D000000000500000000000047510064+2420131101 203D000000000500000000000047010064+2520131102 207D000000000500000000000046510064+");
        bit120s.put(8, "052620131018 203D000000000100000000000046410064+2720131019 206D000000000100000000000046310064+2820131020 200D000000000100000000000046210064+2920131021 203D000000000100000000000046110064+3020131022 207D000000000100000000000046010064+");
        bit120s.put(9, "300120131023 203D000000000100000000000046910064+0220131024 206D000000000100000000000045810064+0320131025 200D000000000100000000000045710064+0420131026 203D000000000100000000000045610064+0520131027 207D000000000100000000000045510064+0620131028 203D000000000500000000000045010064+0720131029 206D000000000500000000000044510064+0820131030 200D000000000500000000000044010064+0920131101 203D000000000500000000000043510064+1020131102 207D000000000500000000000043010064+1120131103 207C000000030000000000000073010064+1220131024 206D000000000500000000000072510064+1320131025 200D000000000500000000000072010064+1420131026 203D000000000500000000000071510064+1520131027 207D000000000500000000000071010064+1620131028 203D000000000500000000000070510064+1720131029 206D000000000500000000000070010064+1820131030 200D000000000500000000000069510064+1920131101 203D000000000500000000000069010064+2020131102 207D000000000500000000000068510064+2120131103 207D000000001000000000000068010064+2220131024 206D000000001000000000000067510064+2320131025 200D000000001000000000000067010064+2420131026 203D000000001000000000000066510064+2520131027 207D000000001000000000000066010064+2620131028 203D000000000500000000000065510064+2720131029 206D000000000500000000000065010064+2820131030 200D000000000500000000000064510064+2920131101 203D000000000500000000000064010064+3020131102 207D000000000500000000000063510064+");
        bit120s.put(10, "050120131103 203D000000000100000000000063410064+0220131104 206D000000000100000000000063310064+0320131105 200D000000000100000000000043210064+0420131106 203D000000000100000000000043110064+0520131107 207D000000000100000000000043010064+");
        bit120s.put(11, "150620131108 203D000000000500000000000043010064+0720131109 206D000000000500000000000042510064+0820131110 200D000000000500000000000042010064+0920131111 203D000000000500000000000041510064+1020131112 207D000000000500000000000041010064+1120131113 207D000000000500000000000039510064+1220131114 206D000000000500000000000039010064+1320131115 200D000000000500000000000038510064+1420131116 203D000000000500000000000038010064+1520131117 207D000000000500000000000037510064+1620131118 203D000000000500000000000037010064+1720131119 206D000000000500000000000036510064+1820131120 200D000000000500000000000036010064+1920131121 203D000000000500000000000035510064+2020131122 207D000000000500000000000035010064+");
        bit120s.put(12, "102120131123 207D000000001000000000000034010064+2220131124 206D000000001000000000000033010064+2320131125 200D000000001000000000000032010064+2420131126 203D000000001000000000000031510064+2520131127 207D000000001000000000000030010064+2620131128 203D000000000500000000000029010064+2720131129 206D000000000500000000000028010064+2820131130 200D000000000500000000000027010064+2920131201 203D000000000500000000000026010064+3020131202 207D000000000500000000000025010064+");
        bit120s.put(13, "100120131203 207D000000000500000000000034510064+0220131204 206D000000000500000000000034010064+0320131205 200D000000000500000000000033510064+0420131206 203D000000000500000000000033010064+0520131207 207D000000000500000000000032510064+0620131208 203D000000000500000000000032010064+0720131209 206D000000000500000000000031510064+0820131210 200D000000000500000000000031010064+0920131211 203D000000000500000000000030510064+1020131212 207D000000000500000000000030010064+");
        bit120s.put(14, "051120131213 207C000000050000000000000084010064+1220131214 206D000000001000000000000083010064+1320131215 200D000000001000000000000082010064+1420131216 203D000000001000000000000081510064+1520131217 207D000000001000000000000080010064+");
        bit120s.put(15, "151620131218 203D000000000500000000000079510064+1720131219 206D000000000500000000000079010064+1820131220 200D000000001000000000000078010064+1920131221 203D000000000500000000000077510064+2020131222 207D000000000500000000000077010064+2120131223 207D000000000500000000000076510064+2220131224 206D000000000500000000000076010064+2320131225 200D000000000500000000000075510064+2420131226 203D000000000500000000000075010064+2520131227 207D000000000500000000000074510064+2620131228 203D000000000500000000000074010064+2720131229 206D000000000500000000000073510064+2820131230 200D000000000500000000000073010064+2920140101 203D000000000500000000000072510064+3020140102 207D000000000500000000000072010064+");
        return bit120s;
    }

    public static Map<Integer, String> getSsppUpdates() {
        Map<Integer, String> ssppUpdates = new HashMap();
        ssppUpdates.put(0, "1");
        ssppUpdates.put(1, "1");
        ssppUpdates.put(2, "0");
        ssppUpdates.put(3, "1");
        ssppUpdates.put(4, "0");
        ssppUpdates.put(5, "0");
        ssppUpdates.put(6, "1");
        ssppUpdates.put(7, "1");
        ssppUpdates.put(8, "0");
        ssppUpdates.put(9, "0");
        ssppUpdates.put(10, "1");
        ssppUpdates.put(11, "1");
        ssppUpdates.put(12, "0");
        ssppUpdates.put(13, "1");
        ssppUpdates.put(14, "1");
        ssppUpdates.put(15, "1");
        return ssppUpdates;
    }

    public static List<AccountStatementView> getAccountStatementViews() {
        List<AccountStatementView> accountStatementViews = new ArrayList<AccountStatementView>();
        AccountStatementView asv = new AccountStatementView();
        asv.setCustomerId(1L);
        asv.setCustomerReference("1234567890");
        asv.setAccountNumber("0627115555");
        asv.setAmount(BigDecimal.valueOf(500000L));
        asv.setCurrencyCode("360");
        asv.setDescription("Pembelian Pulsa");
        asv.setFormattedAmount("Rp. 500.000,00");
        asv.setReferenceNumber("FTI000111222");
        asv.setRunningBalance(BigDecimal.ZERO);
        asv.setResponseCode("00");
        asv.setToAccountNumber("0627112222");
        asv.setStatus(1);
        asv.setTransactionType("82");
        asv.setTransactionName("Pembelian Pulsa");
        asv.setTransactionDate(new Date());
        asv.setValueDate(new Date());
        asv.setTransactionId(100L);
        asv.setTransactionIndicator("D");
        accountStatementViews.add(asv);

        AccountStatementView asv2 = new AccountStatementView();
        asv2.setCustomerId(1L);
        asv2.setCustomerReference("1234567890");
        asv2.setAccountNumber("0627115555");
        asv2.setAmount(BigDecimal.valueOf(200000L));
        asv2.setCurrencyCode("360");
        asv2.setDescription("Pembayaran PLN");
        asv2.setFormattedAmount("Rp. 200.000,00");
        asv2.setReferenceNumber("FTI000111333");
        asv2.setRunningBalance(BigDecimal.ZERO);
        asv2.setResponseCode("00");
        asv2.setToAccountNumber("0627113333");
        asv2.setStatus(1);
        asv2.setTransactionType("62");
        asv2.setTransactionName("Pembayaran PLN");
        asv2.setTransactionDate(new Date());
        asv2.setValueDate(new Date());
        asv2.setTransactionId(100L);
        asv2.setTransactionIndicator("D");
        accountStatementViews.add(asv2);

        AccountStatementView asv3 = new AccountStatementView();
        asv3.setCustomerId(1L);
        asv3.setCustomerReference("1234567890");
        asv3.setAccountNumber("0627115555");
        asv3.setAmount(BigDecimal.valueOf(100000L));
        asv3.setCurrencyCode("360");
        asv3.setDescription("Pembayaran Telepon PSTN");
        asv3.setFormattedAmount("Rp. 100.000,00");
        asv3.setReferenceNumber("FTI000111444");
        asv3.setRunningBalance(BigDecimal.ZERO);
        asv3.setResponseCode("00");
        asv3.setToAccountNumber("0627114444");
        asv3.setStatus(1);
        asv3.setTransactionType("72");
        asv3.setTransactionName("Pembayaran Telepon PSTN");
        asv3.setTransactionDate(new Date());
        asv3.setValueDate(new Date());
        asv3.setTransactionId(100L);
        asv3.setTransactionIndicator("D");
        accountStatementViews.add(asv3);

        AccountStatementView asv4 = new AccountStatementView();
        asv4.setCustomerId(1L);
        asv4.setCustomerReference("1234567890");
        asv4.setAccountNumber("0627115555");
        asv4.setAmount(BigDecimal.valueOf(500000L));
        asv4.setCurrencyCode("360");
        asv4.setDescription("Pembelian Pulsa");
        asv4.setFormattedAmount("Rp. 500.000,00");
        asv4.setReferenceNumber("FTI000111222");
        asv4.setRunningBalance(BigDecimal.ZERO);
        asv4.setResponseCode("00");
        asv4.setToAccountNumber("0627112222");
        asv4.setStatus(1);
        asv4.setTransactionType("82");
        asv4.setTransactionName("Pembelian Pulsa");
        asv4.setTransactionDate(new Date());
        asv4.setValueDate(new Date());
        asv4.setTransactionId(100L);
        asv4.setTransactionIndicator("D");
        accountStatementViews.add(asv4);

        AccountStatementView asv5 = new AccountStatementView();
        asv5.setCustomerId(1L);
        asv5.setCustomerReference("1234567890");
        asv5.setAccountNumber("0627115555");
        asv5.setAmount(BigDecimal.valueOf(200000L));
        asv5.setCurrencyCode("360");
        asv5.setDescription("Pembayaran PLN");
        asv5.setFormattedAmount("Rp. 200.000,00");
        asv5.setReferenceNumber("FTI000111333");
        asv5.setRunningBalance(BigDecimal.ZERO);
        asv5.setResponseCode("00");
        asv5.setToAccountNumber("0627113333");
        asv5.setStatus(1);
        asv5.setTransactionType("62");
        asv5.setTransactionName("Pembayaran PLN");
        asv5.setTransactionDate(new Date());
        asv5.setValueDate(new Date());
        asv5.setTransactionId(100L);
        asv5.setTransactionIndicator("D");
        accountStatementViews.add(asv5);

        AccountStatementView asv6 = new AccountStatementView();
        asv6.setCustomerId(1L);
        asv6.setCustomerReference("1234567890");
        asv6.setAccountNumber("0627115555");
        asv6.setAmount(BigDecimal.valueOf(100000L));
        asv6.setCurrencyCode("360");
        asv6.setDescription("Pembayaran Telepon PSTN");
        asv6.setFormattedAmount("Rp. 100.000,00");
        asv6.setReferenceNumber("FTI000111444");
        asv6.setRunningBalance(BigDecimal.ZERO);
        asv6.setResponseCode("00");
        asv6.setToAccountNumber("0627114444");
        asv6.setStatus(1);
        asv6.setTransactionType("72");
        asv6.setTransactionName("Pembayaran Telepon PSTN");
        asv6.setTransactionDate(new Date());
        asv6.setValueDate(new Date());
        asv6.setTransactionId(100L);
        asv6.setTransactionIndicator("D");
        accountStatementViews.add(asv6);

        AccountStatementView asv7 = new AccountStatementView();
        asv7.setCustomerId(1L);
        asv7.setCustomerReference("1234567890");
        asv7.setAccountNumber("0627115555");
        asv7.setAmount(BigDecimal.valueOf(500000L));
        asv7.setCurrencyCode("360");
        asv7.setDescription("Pembelian Pulsa");
        asv7.setFormattedAmount("Rp. 500.000,00");
        asv7.setReferenceNumber("FTI000111222");
        asv7.setRunningBalance(BigDecimal.ZERO);
        asv7.setResponseCode("00");
        asv7.setToAccountNumber("0627112222");
        asv7.setStatus(1);
        asv7.setTransactionType("82");
        asv7.setTransactionName("Pembelian Pulsa");
        asv7.setTransactionDate(new Date());
        asv7.setValueDate(new Date());
        asv7.setTransactionId(100L);
        asv7.setTransactionIndicator("D");
        accountStatementViews.add(asv7);

        AccountStatementView asv8 = new AccountStatementView();
        asv8.setCustomerId(1L);
        asv8.setCustomerReference("1234567890");
        asv8.setAccountNumber("0627115555");
        asv8.setAmount(BigDecimal.valueOf(200000L));
        asv8.setCurrencyCode("360");
        asv8.setDescription("Pembayaran PLN");
        asv8.setFormattedAmount("Rp. 200.000,00");
        asv8.setReferenceNumber("FTI000111333");
        asv8.setRunningBalance(BigDecimal.ZERO);
        asv8.setResponseCode("00");
        asv8.setToAccountNumber("0627113333");
        asv8.setStatus(1);
        asv8.setTransactionType("62");
        asv8.setTransactionName("Pembayaran PLN");
        asv8.setTransactionDate(new Date());
        asv8.setValueDate(new Date());
        asv8.setTransactionId(100L);
        asv8.setTransactionIndicator("D");
        accountStatementViews.add(asv8);

        AccountStatementView asv9 = new AccountStatementView();
        asv9.setCustomerId(1L);
        asv9.setCustomerReference("1234567890");
        asv9.setAccountNumber("0627115555");
        asv9.setAmount(BigDecimal.valueOf(100000L));
        asv9.setCurrencyCode("360");
        asv9.setDescription("Pembayaran Telepon PSTN");
        asv9.setFormattedAmount("Rp. 100.000,00");
        asv9.setReferenceNumber("FTI000111444");
        asv9.setRunningBalance(BigDecimal.ZERO);
        asv9.setResponseCode("00");
        asv9.setToAccountNumber("0627114444");
        asv9.setStatus(1);
        asv9.setTransactionType("72");
        asv9.setTransactionName("Pembayaran Telepon PSTN");
        asv9.setTransactionDate(new Date());
        asv9.setValueDate(new Date());
        asv9.setTransactionId(100L);
        asv9.setTransactionIndicator("D");
        accountStatementViews.add(asv9);

        AccountStatementView asv10 = new AccountStatementView();
        asv10.setCustomerId(1L);
        asv10.setCustomerReference("1234567890");
        asv10.setAccountNumber("0627115555");
        asv10.setAmount(BigDecimal.valueOf(100000L));
        asv10.setCurrencyCode("360");
        asv10.setDescription("Pembayaran Telepon PSTN");
        asv10.setFormattedAmount("Rp. 100.000,00");
        asv10.setReferenceNumber("FTI000111444");
        asv10.setRunningBalance(BigDecimal.ZERO);
        asv10.setResponseCode("00");
        asv10.setToAccountNumber("0627114444");
        asv10.setStatus(1);
        asv10.setTransactionType("72");
        asv10.setTransactionName("Pembayaran Telepon PSTN");
        asv10.setTransactionDate(new Date());
        asv10.setValueDate(new Date());
        asv10.setTransactionId(100L);
        asv10.setTransactionIndicator("D");
        accountStatementViews.add(asv10);

        AccountStatementView asv11 = new AccountStatementView();
        asv11.setCustomerId(1L);
        asv11.setCustomerReference("1234567890");
        asv11.setAccountNumber("0627115555");
        asv11.setAmount(BigDecimal.valueOf(500000L));
        asv11.setCurrencyCode("360");
        asv11.setDescription("Pembelian Pulsa");
        asv11.setFormattedAmount("Rp. 500.000,00");
        asv11.setReferenceNumber("FTI000111222");
        asv11.setRunningBalance(BigDecimal.ZERO);
        asv11.setResponseCode("00");
        asv11.setToAccountNumber("0627112222");
        asv11.setStatus(1);
        asv11.setTransactionType("82");
        asv11.setTransactionName("Pembelian Pulsa");
        asv11.setTransactionDate(new Date());
        asv11.setValueDate(new Date());
        asv11.setTransactionId(100L);
        asv11.setTransactionIndicator("D");
        accountStatementViews.add(asv11);

        AccountStatementView asv12 = new AccountStatementView();
        asv12.setCustomerId(1L);
        asv12.setCustomerReference("1234567890");
        asv12.setAccountNumber("0627115555");
        asv12.setAmount(BigDecimal.valueOf(200000L));
        asv12.setCurrencyCode("360");
        asv12.setDescription("Pembayaran PLN");
        asv12.setFormattedAmount("Rp. 200.000,00");
        asv12.setReferenceNumber("FTI000111333");
        asv12.setRunningBalance(BigDecimal.ZERO);
        asv12.setResponseCode("00");
        asv12.setToAccountNumber("0627113333");
        asv12.setStatus(1);
        asv12.setTransactionType("62");
        asv12.setTransactionName("Pembayaran PLN");
        asv12.setTransactionDate(new Date());
        asv12.setValueDate(new Date());
        asv12.setTransactionId(100L);
        asv12.setTransactionIndicator("D");
        accountStatementViews.add(asv12);

        AccountStatementView asv13 = new AccountStatementView();
        asv13.setCustomerId(1L);
        asv13.setCustomerReference("1234567890");
        asv13.setAccountNumber("0627115555");
        asv13.setAmount(BigDecimal.valueOf(100000L));
        asv13.setCurrencyCode("360");
        asv13.setDescription("Pembayaran Telepon PSTN");
        asv13.setFormattedAmount("Rp. 100.000,00");
        asv13.setReferenceNumber("FTI000111444");
        asv13.setRunningBalance(BigDecimal.ZERO);
        asv13.setResponseCode("00");
        asv13.setToAccountNumber("0627114444");
        asv13.setStatus(1);
        asv13.setTransactionType("72");
        asv13.setTransactionName("Pembayaran Telepon PSTN");
        asv13.setTransactionDate(new Date());
        asv13.setValueDate(new Date());
        asv13.setTransactionId(100L);
        asv13.setTransactionIndicator("D");
        accountStatementViews.add(asv13);

        return accountStatementViews;
    }
}
