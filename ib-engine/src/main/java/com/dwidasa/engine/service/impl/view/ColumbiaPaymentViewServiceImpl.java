package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AutodebetRegistrationView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.ColumbiaPaymentView;
import com.dwidasa.engine.model.view.WaterPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.ColumbiaPaymentViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;

@Service("columbiaPaymentViewService")
public class ColumbiaPaymentViewServiceImpl extends PaymentViewServiceImpl
		implements ColumbiaPaymentViewService {

	@Autowired
	private ExtendedProperty extendedProperty;

	@Autowired
	private LoggingService loggingService;

	 private static Logger logger = Logger.getLogger( ColumbiaPaymentViewServiceImpl.class );
	 
	public ColumbiaPaymentViewServiceImpl() {
		super();
		setInquiryMessageCustomizer(new InquiryMessageCustomizer());
		setTransactionMessageCustomizer(new TransactionMessageCustomizer());
		setReprintMessageCustomizer(new ReprintMessageCustomizer());
	}

	/**
	 * 
	 * Class to compose - decompose for Inquiry phase
	 *
	 */
	private class InquiryMessageCustomizer implements MessageCustomizer {
		private InquiryMessageCustomizer() {
		}

		@Override
		public void compose(BaseView view, Transaction transaction) {
			logger.info("view=" + view);
			ColumbiaPaymentView  cpv = (ColumbiaPaymentView) view;
			String customData = cpv.getCustomerReference();
			transaction.setFreeData1(customData);
			
		}

		@Override
		public Boolean decompose(Object view, Transaction transaction) {
			ColumbiaPaymentView  cpv = (ColumbiaPaymentView) view;
			String bit48 = transaction.getFreeData1();
			cpv.setCustomerReference((bit48.substring(0, 13).trim()));
	    	cpv.setReferenceName(bit48.substring(13, 43).trim());
	    	cpv.setColumbiaCode(bit48.substring(43, 44).trim());
	    	cpv.setNilaiAngsuran(new BigDecimal(bit48.substring(44,56).trim()));
	    	cpv.setPaymentPeriod(bit48.substring(56,66).trim());
	    	cpv.setNumOfBill(Integer.parseInt(bit48.substring(66,68).trim()));
	    	cpv.setJatuhTempo(bit48.substring(68,78).trim());
	    	cpv.setMinimumPayment(new BigDecimal(bit48.substring(78,90).trim()));
	    	cpv.setAmount(new BigDecimal(bit48.substring(90,102).trim()));
	    	cpv.setPenaltyFee(new BigDecimal(bit48.substring(102,114).trim()));
	    	cpv.setTransType(bit48.substring(114,115).trim());
	    	cpv.setTotal(transaction.getFee());
	    	cpv.setFee(transaction.getFee());
	    	BigDecimal total = null;
	    	total = cpv.getAmount().add(transaction.getFee());
	    	cpv.setTotal(total);
	    	cpv.setBit48(bit48);
			return Boolean.TRUE;
		}
	}
	
	/**
	 * 
	 * Class to compose - decompose for Posting phase
	 *
	 */
	private class TransactionMessageCustomizer implements MessageCustomizer{
		private TransactionMessageCustomizer() {
		}

		@Override
		public void compose(BaseView view, Transaction transaction) {
			ColumbiaPaymentView  cpv = (ColumbiaPaymentView) view;
			logger.info("view=" + view);
			String customData = cpv.getBit48();
			transaction.setFreeData1(customData);
			
			
		}

		@Override
		public Boolean decompose(Object view, Transaction transaction) {
			ColumbiaPaymentView  cpv = (ColumbiaPaymentView) view;
			cpv.setResponseCode(transaction.getResponseCode());
			cpv.setReferenceNumber(transaction.getReferenceNumber());


			return Boolean.TRUE;
		}
	}
	
	/**
	 * 
	 * Class to compose - decompose for Reprint phase
	 *
	 */
	private class ReprintMessageCustomizer implements MessageCustomizer{
		private ReprintMessageCustomizer(){
			
		}

		@Override
		public void compose(BaseView view, Transaction transaction) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Boolean decompose(Object view, Transaction transaction) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
