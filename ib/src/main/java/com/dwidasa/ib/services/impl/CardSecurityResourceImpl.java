package com.dwidasa.ib.services.impl;

import javax.ws.rs.FormParam;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerSession;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.*;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.WebAuthenticationService;
import com.dwidasa.engine.service.impl.view.KioskReprintMessageCustomizer;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.BaseViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.components.Account;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.CardSecurityResource;

import java.math.BigDecimal;

@PublicPage

public class CardSecurityResourceImpl implements CardSecurityResource{
    private static Logger logger = Logger.getLogger( CardSecurityResourceImpl.class );
    @Inject
    private AccountService accountService;

	@Inject
	private CustomerDao customerDao;
	
	public String authenticateCustomerCard(String cardData, String cardPin) {
		return PojoJsonMapper.toJson(new Boolean(true));
	}
	
	public String authoriseCustomerFromCard(String trackNo2, String cardPin, String json) {
		logger.info("trackNo2=" + trackNo2 + ", json=" + json);
        AccountView av = PojoJsonMapper.fromJson(json, AccountView.class);
        //taruh dl trackNo2 dan pin temporary
        av.setCustomerName(trackNo2);
//        av.setAccountName(EngineUtils.encryptPin(cardPin));
        av.setAccountName(EngineUtils.getEncryptedPin(cardPin, av.getCardNumber()));
        
        av.setTransactionType("11");
        av.setAccountType("00");
        
        logger.info("CIF=" + av.getProductName() + " cardNo=" + av.getCardNumber());
        av = (AccountView) authorize(av);

        logger.info("CIF=" + av.getProductName() + " cardNo=" + av.getCardNumber());
        Customer customer = customerDao.getByCifCardNo(av.getProductName(), av.getCardNumber());  //temporary, CIF ditaruh di productName
        if (customer == null) {
            /*
            customer = new Customer();
            customer.setCustomerName(av.getCustomerName());
            customer.setCustomerPin(cardPin);
            customer.setCustomerPhone("");
            customer.setCifNumber(av.getProductName());
           // accountService.registerKioskSilent(customer, av);
            */
            String bit48 = av.getCustomerName();
            //CIF an8 Nomor CIF -Rata kiri -Padding with space
            av.setProductName(bit48.substring(0, 8));   //temporary
            //Jumlah Kartu n2 Jumlah kartu -Rata kiri -Padding with space
            int n = Integer.parseInt(bit48.substring(8, 10));
            //Perulangan berdasarkan field Jumlah Kartu     //16 + 10 + 30 + 30 = 86
            String cardData = bit48.substring(10);
            //int numOfCard = Integer.valueOf(bit48.substring(10, 12));
            for (int i = 0; i < 1; i++) //pasti 1 kartu
            {
                av.setCustomerName(cardData.substring(26 + i * 86, 56 + i * 86).trim());
            }
//simpan m_customer dahulu
            customer = new Customer();
            customer.setCustomerName(av.getCustomerName());
            customer.setEncryptedCustomerPin(EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, cardPin));
            customer.setCustomerPhone("");
            customer.setCifNumber(av.getProductName());

            accountService.registerCustomerSilent(customer, av);

            Customer insertedCust = customerDao.get(customer.getCustomerUsername());    //query untuk mendapatkan cutomerId
//simpan m_customer_account sebanyak yg ada di kartu
            String defaultAccount = av.getAccountNumber();
            for (int i = 0; i < n; i++) {
                //No Rekening an10 Nomor rekening -Rata kiri -Padding with space
                av.setAccountNumber(cardData.substring(16 + i * 86, 26 + i * 86).trim());
                //Account Name Holder An30 -Rata kiri -Padding with space
                av.setCustomerName(cardData.substring(26 + i * 86, 56 + i * 86).trim());
                //Product Type An30 -Rata kiri -Padding with space
                av.setProductCode(cardData.substring(56 + i * 86, 86 + i * 86).trim());
                if (av.getAccountType() == null) {
                    av.setAccountType("10");    //defaultnya adalah tabungan
                }
                logger.info("insertedCust=" + insertedCust + ", av" + av + ", defaultAccount" + defaultAccount);
                accountService.registerAccountSilent(insertedCust, av, defaultAccount);
            }
        }

        customer = customerDao.getByCifCardNo(customer.getCifNumber(), av.getCardNumber());

        WebAuthenticationService was = (WebAuthenticationService) ServiceLocator.getService("webAuthenticationService");
        CustomerView cv = was.authenticateCardPin(customer, av.getCardNumber());
        return PojoJsonMapper.toJson(cv);

	}

    public BaseView authorize(BaseView view) {
//        BaseViewService viewService = getServiceObject(view);
//        viewService.preProcess(view);
        Transaction transaction = TransformerFactory.getTransformer(view)
                .transformTo(view, new Transaction());
        //MessageCustomizer msgCustomizer = KioskReprintMessageCustomizer.instance().getReprintMessageCustomizer(view);
        MessageCustomizer msgCustomizer = new CardMessageCustomizer();

        transaction.setReferenceNumber(view.getReferenceNumber());

        msgCustomizer.compose(view, transaction);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();

        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	msgCustomizer.decompose(view, transaction);
        }
        else {
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        EngineUtils.setTransactionStatus(transaction);

        return view;
	}


    private static class CardMessageCustomizer implements MessageCustomizer{
		private CardMessageCustomizer() {}

		public void compose(BaseView view, Transaction transaction) {
	        AccountView av = (AccountView) view;

	        transaction.setCardData1(av.getCustomerName());   //temporary utk bit 35
            transaction.setCardData2(av.getAccountName());    //temporary utk bit 52
            transaction.setTranslationCode("02611");
	    }

	    public Boolean decompose(Object view, Transaction transaction) {
            AccountView av = (AccountView) view;
            String bit48 = transaction.getFreeData1();

            //CIF an8 Nomor CIF -Rata kiri -Padding with space
            av.setProductName(bit48.substring(0, 8));   //temporary
            //Jumlah Kartu n2 Jumlah kartu -Rata kiri -Padding with space
            int n = Integer.parseInt(bit48.substring(8, 10));
            //Perulangan berdasarkan field Jumlah Kartu     //16 + 10 + 30 + 30 = 86
            String cardData = bit48.substring(10);
            //int numOfCard = Integer.valueOf(bit48.substring(10, 12));
            for (int i = 0; i < 1; i++) //pasti 1 kartu
            {
                //No Kartu an16 Nomor kartu -Rata kiri -Padding with space
                av.setCardNumber(cardData.substring(0 + i * 86, 16 + i * 86).trim());
                if (av.getCardNumber().equals("")) {
                    av.setCardNumber(transaction.getCardNumber());  //balikan dr MX emptyString, ambil dr bit 2
                }
                //
                //No Rekening an10 Nomor rekening -Rata kiri -Padding with space
                av.setAccountNumber(cardData.substring(16 + i * 86, 26 + i * 86).trim());
                //Account Name Holder An30 -Rata kiri -Padding with space
                av.setCustomerName(cardData.substring(26 + i * 86, 56 + i * 86).trim());
                //Product Type An30 -Rata kiri -Padding with space
                av.setProductCode(cardData.substring(56 + i * 86, 86 + i * 86).trim());
            }
            av.setCustomerName(bit48);    //temporary bit48 ditaruh di customerName
//silent registration

	        return Boolean.TRUE;
	    }
	}
    /**
     * Getting service object from view
     * @param view view object provided
     * @return viewService object
     */
    protected BaseViewService getServiceObject(BaseView view) {
        String viewServiceName = view.getClass().getSimpleName() + "Service";
        viewServiceName = viewServiceName.substring(0,1).toLowerCase() + viewServiceName.substring(1);
        return (BaseViewService) ServiceLocator.getService(viewServiceName);
    }
}
