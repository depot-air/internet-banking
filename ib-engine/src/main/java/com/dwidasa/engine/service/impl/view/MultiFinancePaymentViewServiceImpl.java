package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.DetilMultiFinance;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.MultiFinancePaymentView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.MultiFinancePaymentViewService;
import com.dwidasa.engine.service.view.TrainPaymentViewService;
import com.dwidasa.engine.util.MoneyUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 2:43 PM
 */
@Service("multiFinancePaymentViewService")
public class MultiFinancePaymentViewServiceImpl extends PaymentViewServiceImpl implements MultiFinancePaymentViewService {
    @Autowired
    private LoggingService loggingService;
    
    public MultiFinancePaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            MultiFinancePaymentView tv = (MultiFinancePaymentView) view;

            //-- customer id an(20) left justified padding with space
            String customData =  StringUtils.rightPad(tv.getNumKontrak(), 14, "");//String.format("%1$-14s", tv.getNumKontrak());
            transaction.setFreeData1(customData);
            //transaction.setTranslationCode("01113000801030002");
            
        }

        public Boolean decompose(Object view, Transaction transaction) {
            MultiFinancePaymentView tv = (MultiFinancePaymentView) view;

            String bit48 = transaction.getFreeData1();
            tv.setNumKontrak(bit48.substring(0, 14).trim());
            tv.setNamaPelanggan(bit48.substring(14, 44));
            tv.setDeskripsi(bit48.substring(44, 94));
            tv.setJumlahTagihan(Integer.parseInt(bit48.substring(94, 95)));

            List<DetilMultiFinance> financePaymentViews = new ArrayList<DetilMultiFinance>();
            int length = 48;
            for(int i=0; i<tv.getJumlahTagihan(); i++){
            	
            	DetilMultiFinance financePaymentView = new DetilMultiFinance();
            	int jml = i * length;
            	financePaymentView.setAngsuranKe(Integer.parseInt(bit48.substring(95 + jml, 99 + jml)));
            	SimpleDateFormat DtFormat = new SimpleDateFormat("dd/MM/yyyy");
				try {
					financePaymentView.setTglJatuhTempo(DtFormat.parse(bit48.substring(99 + jml, 107 + jml)));
				} catch (ParseException e) {
					financePaymentView.setTglJatuhTempo(new Date());
				}
            	BigDecimal nominalAngsuran = new BigDecimal(bit48.substring(107 + jml, 119 + jml));
            	financePaymentView.setNominalAngsuran(nominalAngsuran);
            	BigDecimal nominalDenda = new BigDecimal(bit48.substring(119 + jml, 131 + jml));
            	financePaymentView.setNominalDenda(nominalDenda);
            	BigDecimal nominalMinimalPembayaran = new BigDecimal(bit48.substring(131 + jml, 143 + jml));
            	financePaymentView.setMinimalPembayaran(nominalMinimalPembayaran);
            	financePaymentViews.add(financePaymentView);	
            }
            tv.setFinancePaymentViews(financePaymentViews);
            tv.setBit48(bit48);
            tv.setFee(transaction.getFee());
            
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            MultiFinancePaymentView tv = (MultiFinancePaymentView) view;
            transaction.setFreeData1(tv.getBit48());
        }

        public Boolean decompose(Object view, Transaction transaction) {
            
        	MultiFinancePaymentView tv = (MultiFinancePaymentView) view;

            String bit48 = transaction.getFreeData1();
            tv.setNumKontrak(bit48.substring(0, 14).trim());
            tv.setNamaPelanggan(bit48.substring(14, 44));
            tv.setDeskripsi(bit48.substring(44, 94));
            tv.setJumlahTagihan(Integer.parseInt(bit48.substring(94, 95)));
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());
            
            List<DetilMultiFinance> financePaymentViews = new ArrayList<DetilMultiFinance>();
            int length = 48;
            for(int i=0; i<tv.getJumlahTagihan(); i++){
            	
            	DetilMultiFinance financePaymentView = new DetilMultiFinance();
            	
            	int jml = i * length;
            	financePaymentView.setAngsuranKe(Integer.parseInt(bit48.substring(95 + jml, 99 + jml)));
            	SimpleDateFormat DtFormat = new SimpleDateFormat("dd/MM/yyyy");
				try {
					financePaymentView.setTglJatuhTempo(DtFormat.parse(bit48.substring(99 + jml, 107 + jml)));
				} catch (ParseException e) {
					financePaymentView.setTglJatuhTempo(new Date());
				}
            	BigDecimal nominalAngsuran = new BigDecimal(bit48.substring(107 + jml, 119 + jml));
            	financePaymentView.setNominalAngsuran(nominalAngsuran);
            	BigDecimal nominalDenda = new BigDecimal(bit48.substring(119 + jml, 131 + jml));
            	financePaymentView.setNominalDenda(nominalDenda);
            	BigDecimal nominalMinimalPembayaran = new BigDecimal(bit48.substring(131 + jml, 143 + jml));
            	financePaymentView.setMinimalPembayaran(nominalMinimalPembayaran);
            	
            	tv.setFinancePaymentViews(financePaymentViews);
            	
            }
            
            tv.setFinancePaymentViews(financePaymentViews);
            tv.setFee(transaction.getFee());
        	
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
