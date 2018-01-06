package com.dwidasa.engine.service.impl.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.CellularPrefixDao;
import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.MncLivePurchaseViewService;

/**
 * Service implementation specific to purchase voucher feature. Don't used
 * this service directly on client page, use PurchaseService instead.
 *
 * @author rk
 */
@Service("mncLifePurchaseViewService")
public class MncLivePurchaseViewServiceImpl implements MncLivePurchaseViewService {
	@Autowired
    private ExtendedProperty extendedProperty;
    
    @Autowired
    private LoggingService loggingService;
    
    private static Logger logger = Logger.getLogger( MncLivePurchaseViewServiceImpl.class );
    private MessageCustomizer transactionMessageCustomizer;
    private MessageCustomizer reprintMessageCustomizer;

    @Autowired
    private ProviderProductDao providerProductDao;

    @Autowired
    private CellularPrefixDao cellularPrefixDao;
    
    public MncLivePurchaseViewServiceImpl() {
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        MncLifePurchaseView vpv = (MncLifePurchaseView) view;
        vpv.setToAccountType("00");
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validate(BaseView view) {
        Boolean result = view.validate();
        if (result) {
            //-- validate number prefix for specified biller
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void composeInquiry(BaseView view, Transaction transaction) {
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeInquiry(BaseView view, Transaction transaction) {
        return Boolean.TRUE;
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
     * Class to compose and decompose message for execute phase
     */
    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        	        /**
         * {@inheritDoc}
         */
        public void compose(BaseView view, Transaction transaction) {

        	
        	logger.info("view=" + view);
        	MncLifePurchaseView pv = (MncLifePurchaseView) view;

//          Issuer Code		an 4	Rata kiri, padding space
            String customData = StringUtils.rightPad(pv.getIssuercode(), 4, "");
//            Kode Produk		an 3	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getKodeProduk(), 3, "");
            
            ////Sementara Di Lokal
//            customData += StringUtils.rightPad(pv.getNomoPolis1(), 20, "");
//            SimpleDateFormat dtawal = new SimpleDateFormat("yyyyMMdd");
//            customData += StringUtils.rightPad(dtawal.format(pv.getTglAwalPolis1()).toString(), 8, "");           
//            SimpleDateFormat dtakhir = new SimpleDateFormat("yyyyMMdd");
//            customData += StringUtils.rightPad(dtakhir.format(pv.getTglAkhirPolis1()).toString(), 8, "");
            //Sementara
            
//            Nama Tertanggung		An 30	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getNamaTertanggung(), 30, "");
//            Nomor KTP		An 20	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getCustomerReference(), 20, "");
//            Jenis Kelamin		A 1	L: Laki-laki, P: Perempuan
            customData += StringUtils.rightPad(pv.getJenisKelamin(), 1, "");
//          Tipe Dokumen		P 1	L: Polis, K: Ktp
//            customData += StringUtils.rightPad(pv.getTipeDokumen(), 1, "");  
//            Tempat Lahir		An 30	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getTempatLahir(), 30, "");
//            Tanggal Lahir		N 8	YYYYMMDD
            SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
            customData += StringUtils.rightPad(dt.format(pv.getTanggalLahir()).toString(), 8, "");
//            Alamat		An 50	Rata kiri, padding space		
            customData += StringUtils.rightPad(pv.getAlamat(), 50, "");
//            Kota		An 30	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getKota(), 30, "");
//            Nama Ahli Waris 1		An 30	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getNamaAhliWaris1(), 30, "");
//            Nama Ahli Waris 2		An 30	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getNamaAhliWaris2(), 30, "");
//            Nama Ahli Waris 3		An 30	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getNamaAhliWaris3(), 30, "");
//            Nomor HP		An 15	Rata kiri, padding space
            customData += StringUtils.rightPad(pv.getNoHp(), 15, ""); 
//            Email Address		An 50	Rata kiri, padding space		
            customData += StringUtils.rightPad(pv.getEmailAddress(), 50, "");

            transaction.setFreeData1(customData);
            transaction.setResponseCode(null);

        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	
        	
        	MncLifePurchaseView pv = (MncLifePurchaseView) view;
        	pv.setResponseCode(transaction.getResponseCode());
        	pv.setReferenceNumber(transaction.getReferenceNumber());
        	String bit48 = transaction.getFreeData1();
//          Issuer Code		an 4	Rata kiri, padding space
        	pv.setIssuercode(bit48.substring(0, 4).trim());
//          Kode Produk		an 3	Rata kiri, padding space
        	pv.setKodeProduk(bit48.substring(4, 7).trim());
//          Nomor Polis		An 20	Rata kiri, padding space
        	pv.setNomoPolis1(bit48.substring(7, 27).trim());
//          Tanggal Awal Polis		N 8	YYYYMMDD
        	SimpleDateFormat DtFormat = new SimpleDateFormat("yyyyMMdd");
        	try {
        		pv.setTglAwalPolis1(DtFormat.parse(bit48.substring(27, 35)));
        	} catch (ParseException e) {
        		pv.setTglAwalPolis1(new Date());
        	}
//          Tanggal Berakhir Polis		N 8	YYYYMMDD
        	try {
              pv.setTglAkhirPolis1(DtFormat.parse(bit48.substring(35, 43)));
        	} catch (ParseException e) {
        		pv.setTglAkhirPolis1(new Date());
        	}
//          Nama Tertanggung		An 30	Rata kiri, padding space
        	pv.setNamaTertanggung(bit48.substring(43, 73).trim());
//          Nomor KTP		An 20	Rata kiri, padding space
        	pv.setCustomerReference(bit48.substring(73, 93).trim());
//          Jenis Kelamin		A 1	L: Laki-laki, P: Perempuan
        	pv.setJenisKelamin(bit48.substring(93, 94).trim());
//        Jenis Kelamin		K 1	K: KTP, P: Polis
//        	pv.setTipeDokumen(bit48.substring(94, 95).trim());
//          Tempat Lahir		An 30	Rata kiri, padding space
        	pv.setTempatLahir(bit48.substring(94, 124).trim());
//          Tanggal Lahir		N 8	YYYYMMDD
          try {
              pv.setTanggalLahir(DtFormat.parse(bit48.substring(124, 132)));
          } catch (ParseException e) {
          	pv.setTanggalLahir(new Date());
          }
//          Alamat		An 50	Rata kiri, padding space
          pv.setAlamat(bit48.substring(132, 182).trim());
//          Kota		An 30	Rata kiri, padding space
          pv.setKota(bit48.substring(182, 212).trim());
//          Nama Ahli Waris 1		An 30	Rata kiri, padding space
          pv.setNamaAhliWaris1(bit48.substring(212, 242).trim());
//          Nama Ahli Waris 2		An 30	Rata kiri, padding space
          pv.setNamaAhliWaris2(bit48.substring(242, 272).trim());
//          Nama Ahli Waris 3		An 30	Rata kiri, padding space
          pv.setNamaAhliWaris3(bit48.substring(272, 302).trim());
//          Nomor HP		An 15	Rata kiri, padding space
          pv.setNoHp(bit48.substring(302, 317).trim());
//          Email Address		An 50	Rata kiri, padding space		
          pv.setEmailAddress(bit48.substring(317, 367).trim());
//          

        
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
        	
        	
        	
        	MncLifePurchaseView pv = (MncLifePurchaseView) view;

        	if(pv.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Reprint)){
        		
        	String customData = StringUtils.rightPad(pv.getIssuercode(), 4, "");

            customData += StringUtils.rightPad("K", 1, "");  
            
            //if(pv.getTipeDokumen().equals("P")){
           // 	customData += StringUtils.rightPad(pv.getNomoPolis1(), 20, "");  
            //}else{
            customData += StringUtils.rightPad(pv.getCustomerReference(), 20, "");  
            //}

            transaction.setFreeData1(customData);
            transaction.setResponseCode(null);
            
        	}
        	else if(pv.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Cek_Status)){
        		
        		String customData = StringUtils.rightPad(pv.getIssuercode(), 4, "");

                customData += StringUtils.rightPad(pv.getCustomerReference(), 20, "");  
                
                customData += StringUtils.rightPad(pv.getNamaTertanggung(), 30, "");  
               
                transaction.setFreeData1(customData);
                transaction.setResponseCode(null);
        		
        	}
            
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	
        	
        	MncLifePurchaseView pv = (MncLifePurchaseView) view;
        	
        	if(pv.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Reprint)){
        		
        	pv.setResponseCode(transaction.getResponseCode());
        	pv.setReferenceNumber(transaction.getReferenceNumber());
        	String bit48 = transaction.getFreeData1();
//          Issuer Code		an 4	Rata kiri, padding space
        	pv.setIssuercode(bit48.substring(0, 4).trim());
//          Kode Produk		an 3	Rata kiri, padding space
        	pv.setKodeProduk(bit48.substring(4, 7).trim());
//          Nomor Polis		An 20	Rata kiri, padding space
        	pv.setNomoPolis1(bit48.substring(7, 27).trim());
//          Tanggal Awal Polis		N 8	YYYYMMDD
        	SimpleDateFormat DtFormat = new SimpleDateFormat("yyyyMMdd");
        	try {
        		pv.setTglAwalPolis1(DtFormat.parse(bit48.substring(27, 35)));
        	} catch (ParseException e) {
        		pv.setTglAwalPolis1(new Date());
        	}
//          Tanggal Berakhir Polis		N 8	YYYYMMDD
        	try {
              pv.setTglAkhirPolis1(DtFormat.parse(bit48.substring(35, 43)));
        	} catch (ParseException e) {
        		pv.setTglAkhirPolis1(new Date());
        	}
//          Nama Tertanggung		An 30	Rata kiri, padding space
        	pv.setNamaTertanggung(bit48.substring(43, 73).trim());
//          Nomor KTP		An 20	Rata kiri, padding space
        	pv.setCustomerReference(bit48.substring(73, 93).trim());
//          Jenis Kelamin		A 1	L: Laki-laki, P: Perempuan
        	pv.setJenisKelamin(bit48.substring(93, 94).trim());
//        Jenis Kelamin		K 1	K: KTP, P: Polis
//        	pv.setTipeDokumen(bit48.substring(94, 95).trim());
//          Tempat Lahir		An 30	Rata kiri, padding space
        	pv.setTempatLahir(bit48.substring(94, 124).trim());
//          Tanggal Lahir		N 8	YYYYMMDD
          try {
              pv.setTanggalLahir(DtFormat.parse(bit48.substring(124, 132)));
          } catch (ParseException e) {
          	pv.setTanggalLahir(new Date());
          }
//          Alamat		An 50	Rata kiri, padding space
          pv.setAlamat(bit48.substring(132, 182).trim());
//          Kota		An 30	Rata kiri, padding space
          pv.setKota(bit48.substring(182, 212).trim());
//          Nama Ahli Waris 1		An 30	Rata kiri, padding space
          pv.setNamaAhliWaris1(bit48.substring(212, 242).trim());
//          Nama Ahli Waris 2		An 30	Rata kiri, padding space
          pv.setNamaAhliWaris2(bit48.substring(242, 272).trim());
//          Nama Ahli Waris 3		An 30	Rata kiri, padding space
          pv.setNamaAhliWaris3(bit48.substring(272, 302).trim());
//          Nomor HP		An 15	Rata kiri, padding space
          pv.setNoHp(bit48.substring(302, 317).trim());
//          Email Address		An 50	Rata kiri, padding space		
          pv.setEmailAddress(bit48.substring(317, 367).trim());
          
        	}
        	else if(pv.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Cek_Status)){
        		
        		pv.setResponseCode(transaction.getResponseCode());
            	pv.setReferenceNumber(transaction.getReferenceNumber());
            	String bit48 = transaction.getFreeData1();
//              Issuer Code		an 4	Rata kiri, padding space
            	pv.setIssuercode(bit48.substring(0, 4).trim());
            	//Nomor Polis1
            	try {
            		pv.setNomoPolis1(bit48.substring(4, 24).trim());
				} catch (Exception e) {
					pv.setNomoPolis1("");// TODO: handle exception
				}
            	
//              Tanggal Awal Polis		N 8	YYYYMMDD
            	SimpleDateFormat DtFormat = new SimpleDateFormat("yyyyMMdd");
            	try {
            		pv.setTglAwalPolis1(DtFormat.parse(bit48.substring(24, 32)));
            	} catch (ParseException e) {
            		pv.setTglAwalPolis1(new Date());
            	}
//              Tanggal Berakhir Polis		N 8	YYYYMMDD
            	try {
                  pv.setTglAkhirPolis1(DtFormat.parse(bit48.substring(32, 40)));
            	} catch (ParseException e) {
            		pv.setTglAkhirPolis1(new Date());
            	}
            	
            	try {
            		//Nomor Polis 2
                	try {
                		pv.setNomoPolis2(bit48.substring(40, 60).trim());
    				} catch (Exception e) {
    					pv.setNomoPolis2("");// TODO: handle exception
    				}
                	
//                  Tanggal Awal Polis 2		N 8	YYYYMMDD
                	try {
                		pv.setTglAwalPolis2(DtFormat.parse(bit48.substring(60, 68)));
                	} catch (ParseException e) {
                		pv.setTglAwalPolis2(new Date());
                	}
//                  Tanggal Berakhir Polis 2		N 8	YYYYMMDD
                	try {
                      pv.setTglAkhirPolis2(DtFormat.parse(bit48.substring(68, 76)));
                	} catch (ParseException e) {
                		pv.setTglAkhirPolis2(new Date());
                	}
				} catch (Exception e) {
					pv.setNomoPolis2("");// TODO: handle exception
				}
            	
            	try {
            		//Nomor Polis 3
                	try {
                		pv.setNomoPolis3(bit48.substring(76, 96).trim());
    				} catch (Exception e) {
    					pv.setNomoPolis3("");// TODO: handle exception
    				}
                	
//                  Tanggal Awal Polis 3		N 8	YYYYMMDD
                	try {
                		pv.setTglAwalPolis3(DtFormat.parse(bit48.substring(96, 104)));
                	} catch (ParseException e) {
                		pv.setTglAwalPolis3(new Date());
                	}
//                  Tanggal Berakhir Polis 3		N 8	YYYYMMDD
                	try {
                      pv.setTglAkhirPolis3(DtFormat.parse(bit48.substring(104, 112)));
                	} catch (ParseException e) {
                		pv.setTglAkhirPolis3(new Date());
                	}
				} catch (Exception e) {
					pv.setNomoPolis3("");// TODO: handle exception
				}
            	
            	try {
            		//Nomor Polis 4
                	try {
                		pv.setNomoPolis4(bit48.substring(112, 132).trim());
    				} catch (Exception e) {
    					pv.setNomoPolis4("");// TODO: handle exception
    				}
                	
//                  Tanggal Awal Polis 4		N 8	YYYYMMDD
                	try {
                		pv.setTglAwalPolis4(DtFormat.parse(bit48.substring(132, 140)));
                	} catch (ParseException e) {
                		pv.setTglAwalPolis4(new Date());
                	}
//                  Tanggal Berakhir Polis 4		N 8	YYYYMMDD
                	try {
                      pv.setTglAkhirPolis4(DtFormat.parse(bit48.substring(140, 148)));
                	} catch (ParseException e) {
                		pv.setTglAkhirPolis4(new Date());
                	}
				} catch (Exception e) {
					pv.setNomoPolis4("");// TODO: handle exception
				}
            	
            	
            	try {
            		//Nomor Polis 5
                	try {
                		pv.setNomoPolis5(bit48.substring(148, 168).trim());
    				} catch (Exception e) {
    					pv.setNomoPolis5("");// TODO: handle exception
    				}
                	
//                  Tanggal Awal Polis 5		N 8	YYYYMMDD
                	try {
                		pv.setTglAwalPolis5(DtFormat.parse(bit48.substring(168, 176)));
                	} catch (ParseException e) {
                		pv.setTglAwalPolis5(new Date());
                	}
//                  Tanggal Berakhir Polis 5		N 8	YYYYMMDD
                	try {
                      pv.setTglAkhirPolis5(DtFormat.parse(bit48.substring(176, 184)));
                	} catch (ParseException e) {
                		pv.setTglAkhirPolis5(new Date());
                	}
				} catch (Exception e) {
					pv.setNomoPolis5("");// TODO: handle exception
				}
            	
            	try {
            		//Nomor Polis 6
                	try {
                		pv.setNomoPolis6(bit48.substring(184, 204).trim());
    				} catch (Exception e) {
    					pv.setNomoPolis6("");// TODO: handle exception
    				}
                	
//                  Tanggal Awal Polis 6		N 8	YYYYMMDD
                	try {
                		pv.setTglAwalPolis6(DtFormat.parse(bit48.substring(204, 212)));
                	} catch (ParseException e) {
                		pv.setTglAwalPolis6(new Date());
                	}
//                  Tanggal Berakhir Polis 6		N 8	YYYYMMDD
                	try {
                      pv.setTglAkhirPolis6(DtFormat.parse(bit48.substring(212, 220)));
                	} catch (ParseException e) {
                		pv.setTglAkhirPolis6(new Date());
                	}
				} catch (Exception e) {
					pv.setNomoPolis6("");// TODO: handle exception
				}
            	
            	
            	try {
            		//Nomor Polis 7
                	try {
                		pv.setNomoPolis7(bit48.substring(220, 240).trim());
    				} catch (Exception e) {
    					pv.setNomoPolis7("");// TODO: handle exception
    				}
                	
//                  Tanggal Awal Polis 7		N 8	YYYYMMDD
                	try {
                		pv.setTglAwalPolis7(DtFormat.parse(bit48.substring(240, 248)));
                	} catch (ParseException e) {
                		pv.setTglAwalPolis7(new Date());
                	}
//                  Tanggal Berakhir Polis 5		N 8	YYYYMMDD
                	try {
                      pv.setTglAkhirPolis7(DtFormat.parse(bit48.substring(248, 256)));
                	} catch (ParseException e) {
                		pv.setTglAkhirPolis7(new Date());
                	}
				} catch (Exception e) {
					pv.setNomoPolis7("");// TODO: handle exception
				}
            	
            	
            	try {
            		//Nomor Polis 8
                	try {
                		pv.setNomoPolis8(bit48.substring(256, 276).trim());
    				} catch (Exception e) {
    					pv.setNomoPolis8("");// TODO: handle exception
    				}
                	
//                  Tanggal Awal Polis 8		N 8	YYYYMMDD
                	try {
                		pv.setTglAwalPolis8(DtFormat.parse(bit48.substring(276, 284)));
                	} catch (ParseException e) {
                		pv.setTglAwalPolis8(new Date());
                	}
//                  Tanggal Berakhir Polis 8		N 8	YYYYMMDD
                	try {
                      pv.setTglAkhirPolis8(DtFormat.parse(bit48.substring(284, 292)));
                	} catch (ParseException e) {
                		pv.setTglAkhirPolis8(new Date());
                	}
				} catch (Exception e) {
					pv.setNomoPolis8("");// TODO: handle exception
				}
            	
            	
            	try {
            		//Nomor Polis 9
                    
                	try {
                		pv.setNomoPolis9(bit48.substring(292, 312).trim());
    				} catch (Exception e) {
    					pv.setNomoPolis9("");// TODO: handle exception
    				}
                	
//                  Tanggal Awal Polis 9		N 8	YYYYMMDD
                	try {
                		pv.setTglAwalPolis9(DtFormat.parse(bit48.substring(312, 320)));
                	} catch (ParseException e) {
                		pv.setTglAwalPolis9(new Date());
                	}
//                  Tanggal Berakhir Polis 9		N 8	YYYYMMDD
                	try {
                      pv.setTglAkhirPolis9(DtFormat.parse(bit48.substring(320, 328)));
                	} catch (ParseException e) {
                		pv.setTglAkhirPolis9(new Date());
                	}
				} catch (Exception e) {
					pv.setNomoPolis9("");// TODO: handle exception
				}
            	
        		
        	}
          
      	return Boolean.TRUE;
        }
    }

}
