package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TiketKeretaDjatiPurchaseView;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.PlnPurchaseViewService;
import com.dwidasa.engine.service.view.TiketKeretaDjatiPurchaseViewService;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/9/11
 * Time: 10:21 AM
 */
@Service("tiketKeretaDjatiPurchaseViewService")
public class TiketKeretaDjatiPurchaseViewServiceImpl implements TiketKeretaDjatiPurchaseViewService {
	private Logger logger = Logger.getLogger(TiketKeretaDjatiPurchaseViewServiceImpl.class);
//    @Autowired
//    private LoggingService loggingService;
    
    private MessageCustomizer transactionMessageCustomizer;
    private MessageCustomizer inquiryMessageCustomizer;
    private MessageCustomizer reprintMessageCustomizer;

    public TiketKeretaDjatiPurchaseViewServiceImpl() {
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        inquiryMessageCustomizer = new InquiryMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        TiketKeretaDjatiPurchaseView pv = (TiketKeretaDjatiPurchaseView) view;
        pv.setToAccountType("00");
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validate(BaseView view) {
        Boolean result = view.validate();
        if (result) {
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
        	TiketKeretaDjatiPurchaseView pv = (TiketKeretaDjatiPurchaseView) view;
        	
        	if(pv.getTransactionType().equals(Constants.TIKET_KERETA_DJATI.INQ_KOTA_ASAL_KRAMAT_DJATI)){
        		
        		String customData = "06"+pv.getBillerCode();
                customData += StringUtils.rightPad("0018", 4, "");
                customData += StringUtils.rightPad(pv.getProductCode(), 4, "");
                
                transaction.setFreeData1(customData);
                transaction.setTranslationCode("022010001010010001");
                //set bit 4 = 0, karena kadang2 masih menyimpan nilai dr transaksi sebelumnya
                transaction.setTransactionAmount(BigDecimal.ZERO);
        		
        	}else
        	if(pv.getTransactionType().equals(Constants.TIKET_KERETA_DJATI.INQ_KOTA_TUJUAN_KRAMAT_DJATI)){
        		
        		String customData = "06"+pv.getBillerCode();
                customData += StringUtils.rightPad("0018", 4, "");
                customData += StringUtils.rightPad(pv.getProductCode(), 4, "");
                customData += StringUtils.rightPad(pv.getDataKotaAsal(), 20, "");
                transaction.setFreeData1(customData);
                transaction.setTranslationCode("022010001010010001");
                //set bit 4 = 0, karena kadang2 masih menyimpan nilai dr transaksi sebelumnya
                transaction.setTransactionAmount(BigDecimal.ZERO);
        		
        	}
        	else{
        		
        	if(pv.getJenisLayanan().equals(Constants.TIKETUX.JENIS_LAYANAN)){
        		
        		String customData = "06"+pv.getBillerCode();
                customData += StringUtils.rightPad("0018", 4, "");
                customData += StringUtils.rightPad(pv.getProductCode(), 4, "");
                customData += StringUtils.rightPad(pv.getScheduleCodeId(), 30, "");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                customData += StringUtils.rightPad(dateFormat.format(pv.getTglKeberangkatan()).toString(), 10, "");
                customData += StringUtils.rightPad(pv.getVechileTypeCode(), 5, "");
                transaction.setFreeData1(customData);
                transaction.setTranslationCode("022010001010010001");
                //set bit 4 = 0, karena kadang2 masih menyimpan nilai dr transaksi sebelumnya
                transaction.setTransactionAmount(BigDecimal.ZERO);
                
            	
            }else{
        		
            String customData = "06"+pv.getBillerCode();
            customData += StringUtils.rightPad("0018", 4, "");
            customData += StringUtils.rightPad(pv.getProductCode(), 4, "");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            customData += StringUtils.rightPad(dateFormat.format(pv.getTglKeberangkatan()).toString(), 10, "");
            customData += StringUtils.rightPad(pv.getDari(), 20, "");
            customData += StringUtils.rightPad(pv.getTujuan(), 20, "");
            
            transaction.setFreeData1(customData);
            transaction.setTranslationCode("022010001010010001");
            //set bit 4 = 0, karena kadang2 masih menyimpan nilai dr transaksi sebelumnya
            transaction.setTransactionAmount(BigDecimal.ZERO);
            
            }
            
        	}
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	TiketKeretaDjatiPurchaseView pv = (TiketKeretaDjatiPurchaseView) view;
        	//List<TiketKeretaDjatiPurchaseView> views = new ArrayList<TiketKeretaDjatiPurchaseView>();
            if(pv.getTransactionType().equals(Constants.TIKET_KERETA_DJATI.INQ_KOTA_ASAL_KRAMAT_DJATI)){
            	
            	 String bit48 = transaction.getFreeData1();
            	 
            	 pv.setBillerCode(bit48.substring(0, 5));
                 pv.setPayeeId(bit48.substring(5, 9));
                 pv.setProductCode(bit48.substring(9, 13));
                 pv.setOriginData(Integer.parseInt(bit48.substring(13, 16)));
                 
                 for(int i=0; i<pv.getOriginData(); i++){
                	 int kotaAsal = i * 20;
                	 pv.setDataKotaAsal(bit48.substring(16 + kotaAsal, 36 + kotaAsal));
                	 System.out.println("Data Kota "+pv.getDataKotaAsal());
                	 pv.getListKotaAsal().add(pv.getDataKotaAsal());
                }
                 
            	return Boolean.TRUE;
            	
            }else
            if(pv.getTransactionType().equals(Constants.TIKET_KERETA_DJATI.INQ_KOTA_TUJUAN_KRAMAT_DJATI)){
            	
            	String bit48 = transaction.getFreeData1();
           	 
            	pv.setBillerCode(bit48.substring(0, 5));
                pv.setPayeeId(bit48.substring(5, 9));
                pv.setProductCode(bit48.substring(9, 13));
            	pv.setDataKotaAsal(bit48.substring(13, 33));
            	pv.setDestinationData(Integer.parseInt(bit48.substring(33, 36)));
            	
            	for(int i=0; i<pv.getDestinationData(); i++){
            		
            		 int kotaTujuan = i * 20;
                	 pv.setDestinationKotaTujuan(bit48.substring(36 + kotaTujuan, 56 + kotaTujuan));
                	 System.out.println("Data Kota Tujuan "+pv.getDestinationKotaTujuan());
                	 pv.getListKotaTujuan().add(pv.getDestinationKotaTujuan());
            		
            	}
            	
            	
            	return Boolean.TRUE;
            	
            }
            else {

				if (pv.getJenisLayanan().equals(Constants.TIKETUX.JENIS_LAYANAN)) {

					String bit48 = transaction.getFreeData1();//"06003001800040XTR                          18/06/201411   050102030506";//transaction.getFreeData1();
					pv.setBillerCode(bit48.substring(0, 5));
	                pv.setPayeeId(bit48.substring(5, 9));
	                pv.setProductCode(bit48.substring(9, 13));
	                pv.setScheduleCodeId(bit48.substring(13, 43));
	                SimpleDateFormat DtFormat = new SimpleDateFormat(
							"dd/MM/yyyy");
					try {
						pv.setTglKeberangkatan(DtFormat.parse(bit48.substring(
								43, 53)));
					} catch (ParseException e) {
						pv.setTglKeberangkatan(new Date());
					}
					
					pv.setVechileTypeCode(bit48.substring(53, 58));
					pv.setTotalEmptySheat(Integer.parseInt(bit48.substring(58, 60)));
					
					for(int i=0; i<pv.getTotalEmptySheat(); i++){
						
						int kursi = (i * 2);
						pv.setNoKursi(bit48.substring(
								60 + kursi, 62 + kursi));
						System.out.println("Kursi "+ pv.getNoKursi());
						pv.getListKursi().add(pv.getNoKursi());
						
					}
					

				} else {

					String bit48 = transaction.getFreeData1();//"0600400180XTR03/07/2014Bandung             Jakarta             12BSM-BLR                       72        07BSM-BLR08                     BSM (Trans Studio)                                Blora                                             07:4511   1000000009000000BSM-BLR10                     BSM (Trans Studio)                                Blora                                             09:4511   1000000009000000BSM-BLR12                     BSM (Trans Studio)                                Blora                                             11:4511   1000000009000000BSM-BLR14                     BSM (Trans Studio)                                Blora                                             13:4511   1000000009000000BSM-BLR16                     BSM (Trans Studio)                                Blora                                             15:4511   1000000009000000BSM-BLR19                     BSM (Trans Studio)                                Blora                                             18:4511   1000000009000000BSM-BLR21                     BSM (Trans Studio)                                Blora                                             20:4511   1000000009000000BALE-BSD                      41        00BALE-KC                       80        00BALE-KRW                      12        16BALE-KRW06                    Bale Xtrans                                       Karawaci                                          06:0011   1000000010500000BALE-KRW07                    Bale Xtrans                                       Karawaci                                          07:0011   1000000010500000BALE-KRW08                    Bale Xtrans                                       Karawaci                                          08:0011   1000000010500000BALE-KRW09                    Bale Xtrans                                       Karawaci                                          09:0011   1000000010500000BALE-KRW10                    Bale Xtrans                                       Karawaci                                          10:0011   1000000010500000BALE-KRW11                    Bale Xtrans                                       Karawaci                                          11:0011   1000000010500000BALE-KRW12                    Bale Xtrans                                       Karawaci                                          12:0011   1000000010500000BALE-KRW13                    Bale Xtrans                                       Karawaci                                          13:0011   1000000010500000BALE-KRW14                    Bale Xtrans                                       Karawaci                                          14:0011   1000000010500000BALE-KRW15                    Bale Xtrans                                       Karawaci                                          15:0011   1000000010500000BALE-KRW16                    Bale Xtrans                                       Karawaci                                          16:0011   1000000010500000BALE-KRW17                    Bale Xtrans                                       Karawaci                                          17:0011   1000000010500000BALE-KRW18                    Bale Xtrans                                       Karawaci                                          18:3011   1000000010500000BALE-KRW19                    Bale Xtrans                                       Karawaci                                          19:3011   1000000010500000BALE-KRW20                    Bale Xtrans                                       Karawaci                                          20:3011   1000000010500000BALE-KRW21                    Bale Xtrans                                       Karawaci                                          21:3011   1000000010500000BMI-TM                        6         00BMI-BM                        11        00BMI-PCR                       8         16BMI-PCR0545                   Bumi Xtrans                                       Pancoran                                          05:4511   1000000009000000BMI-PCR0645                   Bumi Xtrans                                       Pancoran                                          06:4511   1000000009000000BMI-PCR0745                   Bumi Xtrans                                       Pancoran                                          07:4511   1000000009000000BMI-PCR0845                   Bumi Xtrans                                       Pancoran                                          08:4511   1000000009000000BMI-PCR0945                   Bumi Xtrans                                       Pancoran                                          09:4511   1000000009000000BMI-PCR1045                   Bumi Xtrans                                       Pancoran                                          10:4511   1000000009000000BMI-PCR1145                   Bumi Xtrans                                       Pancoran                                          11:4511   1000000009000000BMI-PCR1245                   Bumi Xtrans                                       Pancoran                                          12:4511   1000000009000000BMI-PCR1345                   Bumi Xtrans                                       Pancoran                                          13:4511   1000000009000000BMI-PCR1445                   Bumi Xtrans                                       Pancoran                                          14:4511   1000000009000000BMI-PCR1545                   Bumi Xtrans                                       Pancoran                                          15:4511   1000000009000000BMI-PCR1645                   Bumi Xtrans                                       Pancoran                                          16:4511   1000000009000000BMI-PCR1745                   Bumi Xtrans                                       Pancoran                                          17:4511   1000000009000000BMI-PCR1845                   Bumi Xtrans                                       Pancoran                                          18:4511   1000000009000000BMI-PCR1945                   Bumi Xtrans                                       Pancoran                                          19:4511   1000000009000000BMI-PCR2045                   Bumi Xtrans                                       Pancoran                                          20:4511   1000000009000000ONCOM-CBBR                    60        00INTI-BLR                      70        08INTI-BLR0515                  PT.INTI                                           Blora                                             05:0011   1000000009000000INTI-BLR0815                  PT.INTI                                           Blora                                             08:0011   1000000009000000INTI-BLR1015                  PT.INTI                                           Blora                                             10:0011   1000000009000000INTI-BLR1215                  PT.INTI                                           Blora                                             12:0011   1000000009000000INTI-BLR1415                  PT.INTI                                           Blora                                             14:0011   1000000009000000INTI-BLR1615                  PT.INTI                                           Blora                                             16:0011   1000000009000000INTI-BLR1915                  PT.INTI                                           Blora                                             19:0011   1000000009000000INTI-BLR2115                  PT.INTI                                           Blora                                             21:0011   1000000009000000PRGN-BTC                      13        12DBTR-BTC0520                  RM. Pringgodani                                   Bintaro                                           05:2011   1000000009000000DBTR-BTC0620                  RM. Pringgodani                                   Bintaro                                           06:2011   1000000009000000DBTR-BTC0820                  RM. Pringgodani                                   Bintaro                                           08:2011   1000000009000000DBTR-BTC0920                  RM. Pringgodani                                   Bintaro                                           09:2011   1000000009000000DBTR-BTC1020                  RM. Pringgodani                                   Bintaro                                           10:2011   1000000009000000DBTR-BTC1220                  RM. Pringgodani                                   Bintaro                                           12:2011   1000000009000000DBTR-BTC1320                  RM. Pringgodani                                   Bintaro                                           13:2011   1000000009000000DBTR-BTC1420                  RM. Pringgodani                                   Bintaro                                           14:2011   1000000009000000DBTR-BTC1620                  RM. Pringgodani                                   Bintaro                                           16:2011   1000000009000000DBTR-BTC1720                  RM. Pringgodani                                   Bintaro                                           17:2011   1000000009000000DBTR-BTC1820                  RM. Pringgodani                                   Bintaro                                           18:2011   1000000009000000DBTR-BTC2020                  RM. Pringgodani                                   Bintaro                                           20:2011   1000000009000000PRGN-PI                       14        17DBTR-PI/FTM06*                RM. Pringgodani                                   Pondok Indah                                      05:1011   1000000009000000DBTR-PI/FTM06                 RM. Pringgodani                                   Pondok Indah                                      06:1011   1000000009000000DBTR-PI/FTM07                 RM. Pringgodani                                   Pondok Indah                                      07:1011   1000000009000000DBTR-PI/FTM08                 RM. Pringgodani                                   Pondok Indah                                      08:1011   1000000009000000DBTR-PI/FTM09                 RM. Pringgodani                                   Pondok Indah                                      09:1011   1000000009000000DBTR-PI/FTM10                 RM. Pringgodani                                   Pondok Indah                                      10:1011   1000000009000000DBTR-PI/FTM11                 RM. Pringgodani                                   Pondok Indah                                      11:1011   1000000009000000DBTR-PI/FTM12                 RM. Pringgodani                                   Pondok Indah                                      12:1011   1000000009000000DBTR-PI/FTM13                 RM. Pringgodani                                   Pondok Indah                                      13:1011   1000000009000000DBTR-PI/FTM14                 RM. Pringgodani                                   Pondok Indah                                      14:1011   1000000009000000DBTR-PI/FTM15                 RM. Pringgodani                                   Pondok Indah                                      15:1011   1000000009000000DBTR-PI/FTM16                 RM. Pringgodani                                   Pondok Indah                                      16:1011   1000000009000000DBTR-PI/FTM17                 RM. Pringgodani                                   Pondok Indah                                      17:1011   1000000009000000DBTR-PI/FTM18                 RM. Pringgodani                                   Pondok Indah                                      18:1011   1000000009000000DBTR-PI/FTM19                 RM. Pringgodani                                   Pondok Indah                                      19:1011   1000000009000000DBTR-PI/FTM20                 RM. Pringgodani                                   Pondok Indah                                      20:1011   1000000009000000DBTR-PI/FTM21                 RM. Pringgodani                                   Pondok Indah                                      21:1011   1000000009000000PRGN-FTM                      57        08DBTR-FTM07                    RM. Pringgodani                                   Fatmawati                                         07:0011   1000000009000000DBTR-FTM09                    RM. Pringgodani                                   Fatmawati                                         09:0011   1000000009000000DBTR-FTM11                    RM. Pringgodani                                   Fatmawati                                         11:0011   1000000009000000DBTR-FTM13                    RM. Pringgodani                                   Fatmawati                                         13:0011   1000000009000000DBTR-FTM15                    RM. Pringgodani                                   Fatmawati                                         15:0011   1000000009000000DBTR-FTM17                    RM. Pringgodani                                   Fatmawati                                         17:0011   1000000009000000DBTR-FTM19                    RM. Pringgodani                                   Fatmawati                                         19:0011   1000000009000000DBTR-FTM21                    RM. Pringgodani                                   Fatmawati                                         21:0011   1000000009000000         ";//transaction.getFreeData1();
					//String bit48 = transaction.getFreeData1();//"060030018000103/07/2014Bandung             Bojonegoro          01BDOBJGR                       236624    064154                          Jl Kebonjati 96                                                                                     13:300    361700000022000016171819202122232425262728293031324152                          Kios Bunga los 5 Psr Antri                                                                          13:300    361700000022000016171819202122232425262728293031324155                          Cicaheum Loket 7                                                                                    14:000    361700000022000016171819202122232425262728293031324153                          Jl Ambon No 3                                                                                       15:000    361700000022000016171819202122232425262728293031324159                          Terminal Cirebon                                                                                    21:000    361700000022000016171819202122232425262728293031324271                          Jl Sukarno hatta 151 Tamsur                                                                         14:000    361700000022000016171819202122232425262728293031326344177623";//"0600400180XTR03/07/2014Bandung             Jakarta             12BSM-BLR                       72        07BSM-BLR08                     BSM (Trans Studio)                                Blora                                             07:4511   1000000009000000BSM-BLR10                     BSM (Trans Studio)                                Blora                                             09:4511   1000000009000000BSM-BLR12                     BSM (Trans Studio)                                Blora                                             11:4511   1000000009000000BSM-BLR14                     BSM (Trans Studio)                                Blora                                             13:4511   1000000009000000BSM-BLR16                     BSM (Trans Studio)                                Blora                                             15:4511   1000000009000000BSM-BLR19                     BSM (Trans Studio)                                Blora                                             18:4511   1000000009000000BSM-BLR21                     BSM (Trans Studio)                                Blora                                             20:4511   1000000009000000BALE-BSD                      41        00BALE-KC                       80        00BALE-KRW                      12        16BALE-KRW06                    Bale Xtrans                                       Karawaci                                          06:0011   1000000010500000BALE-KRW07                    Bale Xtrans                                       Karawaci                                          07:0011   1000000010500000BALE-KRW08                    Bale Xtrans                                       Karawaci                                          08:0011   1000000010500000BALE-KRW09                    Bale Xtrans                                       Karawaci                                          09:0011   1000000010500000BALE-KRW10                    Bale Xtrans                                       Karawaci                                          10:0011   1000000010500000BALE-KRW11                    Bale Xtrans                                       Karawaci                                          11:0011   1000000010500000BALE-KRW12                    Bale Xtrans                                       Karawaci                                          12:0011   1000000010500000BALE-KRW13                    Bale Xtrans                                       Karawaci                                          13:0011   1000000010500000BALE-KRW14                    Bale Xtrans                                       Karawaci                                          14:0011   1000000010500000BALE-KRW15                    Bale Xtrans                                       Karawaci                                          15:0011   1000000010500000BALE-KRW16                    Bale Xtrans                                       Karawaci                                          16:0011   1000000010500000BALE-KRW17                    Bale Xtrans                                       Karawaci                                          17:0011   1000000010500000BALE-KRW18                    Bale Xtrans                                       Karawaci                                          18:3011   1000000010500000BALE-KRW19                    Bale Xtrans                                       Karawaci                                          19:3011   1000000010500000BALE-KRW20                    Bale Xtrans                                       Karawaci                                          20:3011   1000000010500000BALE-KRW21                    Bale Xtrans                                       Karawaci                                          21:3011   1000000010500000BMI-TM                        6         00BMI-BM                        11        00BMI-PCR                       8         16BMI-PCR0545                   Bumi Xtrans                                       Pancoran                                          05:4511   1000000009000000BMI-PCR0645                   Bumi Xtrans                                       Pancoran                                          06:4511   1000000009000000BMI-PCR0745                   Bumi Xtrans                                       Pancoran                                          07:4511   1000000009000000BMI-PCR0845                   Bumi Xtrans                                       Pancoran                                          08:4511   1000000009000000BMI-PCR0945                   Bumi Xtrans                                       Pancoran                                          09:4511   1000000009000000BMI-PCR1045                   Bumi Xtrans                                       Pancoran                                          10:4511   1000000009000000BMI-PCR1145                   Bumi Xtrans                                       Pancoran                                          11:4511   1000000009000000BMI-PCR1245                   Bumi Xtrans                                       Pancoran                                          12:4511   1000000009000000BMI-PCR1345                   Bumi Xtrans                                       Pancoran                                          13:4511   1000000009000000BMI-PCR1445                   Bumi Xtrans                                       Pancoran                                          14:4511   1000000009000000BMI-PCR1545                   Bumi Xtrans                                       Pancoran                                          15:4511   1000000009000000BMI-PCR1645                   Bumi Xtrans                                       Pancoran                                          16:4511   1000000009000000BMI-PCR1745                   Bumi Xtrans                                       Pancoran                                          17:4511   1000000009000000BMI-PCR1845                   Bumi Xtrans                                       Pancoran                                          18:4511   1000000009000000BMI-PCR1945                   Bumi Xtrans                                       Pancoran                                          19:4511   1000000009000000BMI-PCR2045                   Bumi Xtrans                                       Pancoran                                          20:4511   1000000009000000ONCOM-CBBR                    60        00INTI-BLR                      70        08INTI-BLR0515                  PT.INTI                                           Blora                                             05:0011   1000000009000000INTI-BLR0815                  PT.INTI                                           Blora                                             08:0011   1000000009000000INTI-BLR1015                  PT.INTI                                           Blora                                             10:0011   1000000009000000INTI-BLR1215                  PT.INTI                                           Blora                                             12:0011   1000000009000000INTI-BLR1415                  PT.INTI                                           Blora                                             14:0011   1000000009000000INTI-BLR1615                  PT.INTI                                           Blora                                             16:0011   1000000009000000INTI-BLR1915                  PT.INTI                                           Blora                                             19:0011   1000000009000000INTI-BLR2115                  PT.INTI                                           Blora                                             21:0011   1000000009000000PRGN-BTC                      13        12DBTR-BTC0520                  RM. Pringgodani                                   Bintaro                                           05:2011   1000000009000000DBTR-BTC0620                  RM. Pringgodani                                   Bintaro                                           06:2011   1000000009000000DBTR-BTC0820                  RM. Pringgodani                                   Bintaro                                           08:2011   1000000009000000DBTR-BTC0920                  RM. Pringgodani                                   Bintaro                                           09:2011   1000000009000000DBTR-BTC1020                  RM. Pringgodani                                   Bintaro                                           10:2011   1000000009000000DBTR-BTC1220                  RM. Pringgodani                                   Bintaro                                           12:2011   1000000009000000DBTR-BTC1320                  RM. Pringgodani                                   Bintaro                                           13:2011   1000000009000000DBTR-BTC1420                  RM. Pringgodani                                   Bintaro                                           14:2011   1000000009000000DBTR-BTC1620                  RM. Pringgodani                                   Bintaro                                           16:2011   1000000009000000DBTR-BTC1720                  RM. Pringgodani                                   Bintaro                                           17:2011   1000000009000000DBTR-BTC1820                  RM. Pringgodani                                   Bintaro                                           18:2011   1000000009000000DBTR-BTC2020                  RM. Pringgodani                                   Bintaro                                           20:2011   1000000009000000PRGN-PI                       14        17DBTR-PI/FTM06*                RM. Pringgodani                                   Pondok Indah                                      05:1011   1000000009000000DBTR-PI/FTM06                 RM. Pringgodani                                   Pondok Indah                                      06:1011   1000000009000000DBTR-PI/FTM07                 RM. Pringgodani                                   Pondok Indah                                      07:1011   1000000009000000DBTR-PI/FTM08                 RM. Pringgodani                                   Pondok Indah                                      08:1011   1000000009000000DBTR-PI/FTM09                 RM. Pringgodani                                   Pondok Indah                                      09:1011   1000000009000000DBTR-PI/FTM10                 RM. Pringgodani                                   Pondok Indah                                      10:1011   1000000009000000DBTR-PI/FTM11                 RM. Pringgodani                                   Pondok Indah                                      11:1011   1000000009000000DBTR-PI/FTM12                 RM. Pringgodani                                   Pondok Indah                                      12:1011   1000000009000000DBTR-PI/FTM13                 RM. Pringgodani                                   Pondok Indah                                      13:1011   1000000009000000DBTR-PI/FTM14                 RM. Pringgodani                                   Pondok Indah                                      14:1011   1000000009000000DBTR-PI/FTM15                 RM. Pringgodani                                   Pondok Indah                                      15:1011   1000000009000000DBTR-PI/FTM16                 RM. Pringgodani                                   Pondok Indah                                      16:1011   1000000009000000DBTR-PI/FTM17                 RM. Pringgodani                                   Pondok Indah                                      17:1011   1000000009000000DBTR-PI/FTM18                 RM. Pringgodani                                   Pondok Indah                                      18:1011   1000000009000000DBTR-PI/FTM19                 RM. Pringgodani                                   Pondok Indah                                      19:1011   1000000009000000DBTR-PI/FTM20                 RM. Pringgodani                                   Pondok Indah                                      20:1011   1000000009000000DBTR-PI/FTM21                 RM. Pringgodani                                   Pondok Indah                                      21:1011   1000000009000000PRGN-FTM                      57        08DBTR-FTM07                    RM. Pringgodani                                   Fatmawati                                         07:0011   1000000009000000DBTR-FTM09                    RM. Pringgodani                                   Fatmawati                                         09:0011   1000000009000000DBTR-FTM11                    RM. Pringgodani                                   Fatmawati                                         11:0011   1000000009000000DBTR-FTM13                    RM. Pringgodani                                   Fatmawati                                         13:0011   1000000009000000DBTR-FTM15                    RM. Pringgodani                                   Fatmawati                                         15:0011   1000000009000000DBTR-FTM17                    RM. Pringgodani                                   Fatmawati                                         17:0011   1000000009000000DBTR-FTM19                    RM. Pringgodani                                   Fatmawati                                         19:0011   1000000009000000DBTR-FTM21                    RM. Pringgodani                                   Fatmawati                                         21:0011   1000000009000000         ";//transaction.getFreeData1();

					
					pv.setBillerCode(bit48.substring(0, 5));
					pv.setPayeeId(bit48.substring(5, 9));
					pv.setProductCode(bit48.substring(9, 13));
					SimpleDateFormat DtFormat = new SimpleDateFormat("dd/MM/yyyy");
					try {
						pv.setTglKeberangkatan(DtFormat.parse(bit48.substring(13, 23)));
					} catch (ParseException e) {
						pv.setTglKeberangkatan(new Date());
					}

					pv.setDari(bit48.substring(23, 43));
					pv.setTujuan(bit48.substring(43, 63));
					pv.setTotalDirecetion(Integer.parseInt(bit48.substring(63, 65)));

					// if(pv.getJenisLayanan().equals(Constants.TIKETUX.JENIS_LAYANAN)){

					System.out.println("Baca Ini Tiketux ");

					int angka = 0;
					int angkaTiketux = 0;
					int totalSchedule = 0;
					for (int i = 0; i < pv.getTotalDirecetion(); i++) {

						int vLenght = i * 42 + (totalSchedule);

						TiketKeretaDjatiPurchaseView djatiPurchaseView = new TiketKeretaDjatiPurchaseView();
						djatiPurchaseView.setKodeJurusan(bit48.substring(65 + vLenght, 95 + vLenght));
						djatiPurchaseView.setDepartID(bit48.substring(95 + vLenght, 105 + vLenght));
						djatiPurchaseView.setTotalSchedule(Integer.parseInt(bit48.substring(105 + vLenght, 107 + vLenght)));

						System.out.println("Kode Jurusan "+ djatiPurchaseView.getKodeJurusan());
						System.out.println("Depart ID "+ djatiPurchaseView.getDepartID());
						System.out.println("Total Schedule "+ djatiPurchaseView.getTotalSchedule());

						totalSchedule += (djatiPurchaseView.getTotalSchedule() * 156)+ (pv.getListKursi().size() * 2);

						int totSeat = 0;
						for (int j = 0; j < djatiPurchaseView.getTotalSchedule(); j++) {

							totSeat = (j * 156)+ (pv.getListKursi().size() * 2) + vLenght;
							System.out.println("Jml " + totSeat);
							djatiPurchaseView.setIndex(i);
							djatiPurchaseView.setScheduleCodeId(bit48.substring(107 + totSeat, 137 + totSeat));
							djatiPurchaseView.setDepartBranch(bit48.substring(137 + totSeat, 187 + totSeat));
							djatiPurchaseView.setDestinationBranch(bit48.substring(187 + totSeat, 237 + totSeat));
							djatiPurchaseView.setDepartHour(bit48.substring(237 + totSeat, 242 + totSeat));
							djatiPurchaseView.setVechileTypeCode(bit48.substring(242 + totSeat, 247 + totSeat));
							djatiPurchaseView.setTotalSheat(Integer.parseInt(bit48.substring(247 + totSeat, 249 + totSeat)));
							djatiPurchaseView.setTotalEmptySheat(Integer.parseInt(bit48.substring(249 + totSeat, 251 + totSeat)));
							BigDecimal ticketPricePerSheat = new BigDecimal(bit48.substring(251 + totSeat, 263 + totSeat));
							djatiPurchaseView.setTicketPricePerSheat(ticketPricePerSheat);

							if (!djatiPurchaseView.getDestinationBranch().equals(null) || !djatiPurchaseView.getDestinationBranch().equals("")) {
								
								pv.getListLocation().add(djatiPurchaseView.getScheduleCodeId()+ " : "+ djatiPurchaseView.getDepartBranch()+ " pkl :  ("+ djatiPurchaseView.getDepartHour()+ ") , "+ djatiPurchaseView.getDestinationBranch()+";"+i);

							} else {
								pv.getListLocation().add(djatiPurchaseView.getScheduleCodeId()+ " : "+ djatiPurchaseView.getDepartBranch()+ " pkl :  ("+ djatiPurchaseView.getDepartHour() + ")");

							}
							
							angkaTiketux = totSeat;

							for (int k = 0; k < djatiPurchaseView.getTotalEmptySheat(); k++) {

								int kursi = (k * 2) + totSeat;
								djatiPurchaseView.setNoKursi(bit48.substring(263 + kursi, 265 + kursi));
								System.out.println("Kursi "+ djatiPurchaseView.getNoKursi());
								pv.getListKursi().add(djatiPurchaseView.getNoKursi());
								angka = kursi;
							}

							System.out.println("Schedule Code "+ djatiPurchaseView.getScheduleCodeId());
							System.out.println("Depart Branch "+ djatiPurchaseView.getDepartBranch());
							System.out.println("Destination Branch "+ djatiPurchaseView.getDestinationBranch());
							System.out.println("Depart Hour "+ djatiPurchaseView.getDepartHour());
							System.out.println("Total Sheat "+ djatiPurchaseView.getTotalSheat());
							System.out.println("Total Sheat Empty "+ djatiPurchaseView.getTotalEmptySheat());
							System.out.println("Ticket Per Price "+ djatiPurchaseView.getTicketPricePerSheat());

						}
						pv.getDjatiPurchaseViews().add(djatiPurchaseView);

					}
					System.out.println("Angka " + angkaTiketux);
					if (angka > 0) {
						pv.setTerminalId(bit48.substring((265 + angka), (265 + angka) + 10));
					} else {
						pv.setTerminalId(bit48.substring((263 + angkaTiketux), (263 + angkaTiketux) + 10));
					}

					System.out.println("Terminal ID " + pv.getTerminalId());

				}

			}
            return Boolean.TRUE;
        }
    }

    /**
     * Class to compose and decompose message for execute phase
     */
    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        /**
         * {@inheritDoc}
         */
        public void compose(BaseView view, Transaction transaction) {
        	TiketKeretaDjatiPurchaseView pv = (TiketKeretaDjatiPurchaseView) view;

        	String customData = "06"+pv.getBillerCode();
            customData += StringUtils.rightPad("0018", 4, "");
            customData += StringUtils.rightPad(pv.getProductCode(), 4, "");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            customData += StringUtils.rightPad(dateFormat.format(pv.getTglKeberangkatan()).toString(), 10, "");
            customData += StringUtils.rightPad(pv.getDari(), 20, "");
            customData += StringUtils.rightPad(pv.getTujuan(), 20, "");
            customData += StringUtils.rightPad(pv.getKodeJurusan(), 30, "");
            customData += StringUtils.rightPad(pv.getDepartID(), 10, "");
            customData += StringUtils.rightPad(pv.getScheduleCodeId(), 30, "");
            customData += StringUtils.rightPad(pv.getDepartBranch(), 50, "");
            customData += StringUtils.rightPad(pv.getDestinationBranch(), 50, "");
            customData += StringUtils.rightPad(pv.getDepartHour(), 5, "");
            customData += StringUtils.leftPad((pv.getVechileTypeCode() != null) ? (pv.getVechileTypeCode()) : "00000", 5, ""); //StringUtils.rightPad(pv.getVechileTypeCode(), 5, "");
            //TOtal
            String[] Seat = pv.getSeat().split(",");
            String nomorSisa = "";
            if(Seat.length < 10){
            	nomorSisa = "0"+Seat.length;
            }else{
            	nomorSisa = ""+Seat.length;
            }
            customData += StringUtils.rightPad(nomorSisa, 2, "");
            
            customData += StringUtils.leftPad((pv.getTicketPricePerSheat() != null) ? (pv.getTicketPricePerSheat()).toPlainString() : "000000000000", 12, "0"); //StringUtils.rightPad(pv.getTicketPricePerSheat().toString(), 12, "");
            
            String[] name = pv.getName().split(",");
            for(int i=0; i<Seat.length; i++){
            	customData += StringUtils.rightPad(Seat[i], 2, "");
            	customData += StringUtils.rightPad(name[i], 20, "");
            }
            
            customData += StringUtils.rightPad(pv.getTransactionID(), 10, "");
            customData += StringUtils.rightPad(pv.getNoIdentitas(), 20, "");
            customData += StringUtils.rightPad(pv.getPemesan(), 30, "");
            customData += StringUtils.rightPad(pv.getNoHp(), 16, "");
            customData += StringUtils.rightPad(pv.getAlamatPemesan(), 30, "");
            
            System.out.println("Bit 48 nya "+customData);
            
            transaction.setFreeData1(customData);
            BigDecimal jmlSeat = new BigDecimal(Seat.length);
            BigDecimal totAmount = pv.getTicketPricePerSheat().multiply(jmlSeat);
            transaction.setTranslationCode("022010001010010001");
            transaction.setTransactionAmount(totAmount);
 
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	TiketKeretaDjatiPurchaseView pv = (TiketKeretaDjatiPurchaseView) view;
            
        	String bit48 = transaction.getFreeData1();
        	pv.setTransactionType(transaction.getTransactionType());
        	pv.setResponseCode(transaction.getResponseCode());
        	pv.setBillerCode(bit48.substring(0, 5));
            pv.setPayeeId(bit48.substring(5, 9));
            pv.setProductCode(bit48.substring(9, 13));
            SimpleDateFormat DtFormat = new SimpleDateFormat("dd/MM/yyyy");
        	try {
        		pv.setTglKeberangkatan(DtFormat.parse(bit48.substring(13, 23)));
        	} catch (ParseException e) {
        		pv.setTglKeberangkatan(new Date());
        	}
            
        	pv.setDari(bit48.substring(23, 43));
        	pv.setTujuan(bit48.substring(43, 63));
        	
        	pv.setKodeJurusan(bit48.substring(63, 93));
        	pv.setDepartID(bit48.substring(93, 103));
        	pv.setScheduleCodeId(bit48.substring(103, 133));
        	pv.setDepartBranch(bit48.substring(133, 183));
        	pv.setDestinationBranch(bit48.substring(183, 233));
        	pv.setDepartHour(bit48.substring(233, 238));
        	pv.setVechileTypeCode(bit48.substring(238, 243));
        	pv.setTotalEmptySheat(Integer.parseInt(bit48.substring(243, 245)));
        	BigDecimal ticketPrice = new BigDecimal(bit48.substring(245, 257));
        	pv.setTicketPricePerSheat(ticketPrice);
        	
        	int angka = 0;
        	for(int i=0; i<pv.getTotalEmptySheat(); i++){
        		int tot = i * 38;
        		if(tot == 0){
        			pv.setNoKursiPenumpang1(bit48.substring(257 + tot, 259 + tot));
        		}else{
        			pv.setNoKursiPenumpang2(bit48.substring(257 + tot, 259 + tot));
        		}
        		
        		if(tot == 0){
        			pv.setNamaPenumpang1(bit48.substring(259 + tot, 279 + tot));
        		}else{
        			pv.setNamaPenumpang2(bit48.substring(259 + tot, 279 + tot));
        		}
        		pv.setNoTicket(bit48.substring(279 + tot, 295 + tot));
        		angka = tot;
        	}
        	
        	pv.setTransactionID(bit48.substring((295 + angka), (305 + angka)));
        	pv.setNoIdentitas(bit48.substring((305 + angka), (325 + angka)));
        	pv.setName(bit48.substring((325 + angka), (355 + angka)));
        	pv.setNoHp(bit48.substring((355 + angka), (371 + angka)));
        	pv.setAlamatPemesan(bit48.substring((371 + angka), (401 + angka)));
        	pv.setBookingCode(bit48.substring((401 + angka), (417 + angka)));
        	pv.setPaymentCode(bit48.substring((417 + angka), (433 + angka)));
            
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
