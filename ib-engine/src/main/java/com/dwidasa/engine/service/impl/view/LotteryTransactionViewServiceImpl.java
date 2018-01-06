package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.LotteryTransactionViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.MncLivePurchaseViewService;

/**
 * Service implementation specific to purchase voucher feature. Don't used
 * this service directly on client page, use PurchaseService instead.
 *
 * @author rk
 */
@Service("lotteryTransactionViewService")
public class LotteryTransactionViewServiceImpl implements LotteryTransactionViewService {
	@Autowired
    private ExtendedProperty extendedProperty;
    
    @Autowired
    private LoggingService loggingService;
    
    private static Logger logger = Logger.getLogger( LotteryTransactionViewServiceImpl.class );
    private MessageCustomizer transactionMessageCustomizer;
    private MessageCustomizer reprintMessageCustomizer;
    private MessageCustomizer InquiryMessageCustomizer;

    @Autowired
    private ProviderProductDao providerProductDao;

    @Autowired
    private CellularPrefixDao cellularPrefixDao;
    
    public LotteryTransactionViewServiceImpl() {
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
        InquiryMessageCustomizer = new InquiryNomorUndianMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        LotteryTransactionView vpv = (LotteryTransactionView) view;
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
    	InquiryMessageCustomizer.compose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeInquiry(BaseView view, Transaction transaction) {
        return InquiryMessageCustomizer.decompose(view, transaction);
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

        	LotteryTransactionView lv = (LotteryTransactionView) view;
        	//an 4 PosisiAwal
            String customData = StringUtils.rightPad(lv.getPosisiAwal().toString(), 4, "");
            //n 4 TotalData
            customData += StringUtils.rightPad(lv.getTotalData().toString(), 4, "");
            //an 1 next
            customData += StringUtils.rightPad(lv.getNextAvailableFlag(), 1, "");
            //an 990 nomorRekening
            customData += StringUtils.rightPad(lv.getAccountNumber(), 990, "");
            
            transaction.setFreeData1(customData); //Bit 48
            transaction.setTranslationCode(Constants.TRANSLATION_LOTTERY);

        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
        	
        	LotteryTransactionView lotteryView = (LotteryTransactionView) view;
    		List<LotteryTransactionView> views = new ArrayList<LotteryTransactionView>();
    		
    		String bit48 = transaction.getFreeData1();
    		
    			String bit120 = transaction.getFreeData4();
                String bit121 = transaction.getFreeData2();
                String bit122 = transaction.getFreeData5();
                String bit123 = transaction.getFreeData3();
            //bit120
            int LENGTH = 74;
            for(int i=0; i < 13; i++){
            	int vLength = i * LENGTH;
            	LotteryTransactionView lv = new LotteryTransactionView();
            	try {
            		lv.setAccountNumber(bit120.substring(0 + vLength, 20 + vLength));
                    lv.setKodeUndian(bit120.substring(20 + vLength, 24 + vLength));
                    lv.setJenisUndian(bit120.substring(24 + vLength, 50 + vLength));
				} catch (Exception e) {
					lv.setAccountNumber(null);
                    lv.setKodeUndian("");
                    lv.setJenisUndian("");
				}
            	System.out.println("Account Number "+lv.getAccountNumber());
            	System.out.println("Kode Undian Number "+lv.getKodeUndian());
            	System.out.println("Jenis Undian "+lv.getJenisUndian());
            	views.add(lv);
            	
            }
            
          //bit121
            for(int i=0; i < 13; i++){
            	int vLength = i * LENGTH;
            	LotteryTransactionView lv = new LotteryTransactionView();
            	try {
            		lv.setAccountNumber(bit121.substring(0 + vLength, 20 + vLength));
                    lv.setKodeUndian(bit121.substring(20 + vLength, 24 + vLength));
                    lv.setJenisUndian(bit121.substring(24 + vLength, 50 + vLength));
				} catch (Exception e) {
					lv.setAccountNumber(null);
                    lv.setKodeUndian("");
                    lv.setJenisUndian("");
				}
            	System.out.println("Account Number "+lv.getAccountNumber());
            	System.out.println("Kode Undian Number "+lv.getKodeUndian());
            	System.out.println("Jenis Undian "+lv.getJenisUndian());
            	views.add(lv);
            }
            
          //bit122
            for(int i=0; i < 13; i++){
            	int vLength = i * LENGTH;
            	LotteryTransactionView lv = new LotteryTransactionView();
            	try {
            		lv.setAccountNumber(bit122.substring(0 + vLength, 20 + vLength));
                    lv.setKodeUndian(bit122.substring(20 + vLength, 24 + vLength));
                    lv.setJenisUndian(bit122.substring(24 + vLength, 50 + vLength));
				} catch (Exception e) {
					lv.setAccountNumber(null);
                    lv.setKodeUndian("");
                    lv.setJenisUndian("");
				}
            	System.out.println("Account Number "+lv.getAccountNumber());
            	System.out.println("Kode Undian Number "+lv.getKodeUndian());
            	System.out.println("Jenis Undian "+lv.getJenisUndian());
            	views.add(lv);
            }
            
          //bit123
            for(int i=0; i < 13; i++){
            	int vLength = i * LENGTH;
            	LotteryTransactionView lv = new LotteryTransactionView();
            	try {
            		lv.setAccountNumber(bit123.substring(0 + vLength, 20 + vLength));
                    lv.setKodeUndian(bit123.substring(20 + vLength, 24 + vLength));
                    lv.setJenisUndian(bit123.substring(24 + vLength, 50 + vLength));
				} catch (Exception e) {
					lv.setAccountNumber(null);
                    lv.setKodeUndian("");
                    lv.setJenisUndian("");
				}
            	System.out.println("Account Number "+lv.getAccountNumber());
            	System.out.println("Kode Undian Number "+lv.getKodeUndian());
            	System.out.println("Jenis Undian "+lv.getJenisUndian());
            	views.add(lv);
            }
            
            if(views.size() > 0){
            	
            	for(int i = views.size() - 1; i >=0; i--){
            		LotteryView lotteryView2 = views.get(i);
            		if(lotteryView2.getAccountNumber() == null || lotteryView2.getAccountNumber().equals(null)){
            			views.remove(i);
            		}
            	}
            	
            	//lotteryView.setLotteryTransactionViews(views);
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
        	
        	
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	
        	
      	return Boolean.TRUE;
        }
    }
    
    
    private class InquiryNomorUndianMessageCustomizer implements MessageCustomizer {
        private InquiryNomorUndianMessageCustomizer() {
        }

        
        public void compose(BaseView view, Transaction transaction) {
        	
        	LotteryTransactionView lv = (LotteryTransactionView) view;
        	//an 4 PosisiAwal
            String customData = StringUtils.rightPad(lv.getAccountNumber(), 20, "");
            //n 4 TotalData
            customData += StringUtils.rightPad(lv.getKodeUndian(), 4, "");
            //an 1 next
            customData += StringUtils.rightPad(lv.getPosisiAwal().toString(), 4, "");
            
            transaction.setFreeData1(customData); //Bit 48
            transaction.setTranslationCode(Constants.TRANSLATION_LOTTERY);
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	
        	LotteryTransactionView lotteryView = (LotteryTransactionView) view;
        	
        	String bit48 = transaction.getFreeData1();
        	
			lotteryView.setAccountNumber(bit48.substring(0, 20));
			lotteryView.setNamaUndian(bit48.substring(20, 70));
			lotteryView.setPosisiAwal(new BigDecimal(bit48.substring(70, 74)));
			lotteryView.setTotalData(new BigDecimal(bit48.substring(74, 78)));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			
			try {
				lotteryView.setAwalPeriod(dateFormat.parse(bit48.substring(78, 86)));
			} catch (Exception e) {
				lotteryView.setAwalPeriod(new Date());// TODO: handle exception
			}
			
			try {
				lotteryView.setAkhirPeriod(dateFormat.parse(bit48.substring(86, 94)));
			} catch (Exception e) {
				lotteryView.setAkhirPeriod(new Date());// TODO: handle exception
			}
			
			lotteryView.setNextAvailableFlag(bit48.substring(94, 95));
        	return Boolean.TRUE;
        }
    }

}
