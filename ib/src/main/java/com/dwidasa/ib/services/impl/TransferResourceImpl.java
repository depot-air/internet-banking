package com.dwidasa.ib.services.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.text.View;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.impl.CustomerDaoImpl;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.KioskCheckStatusService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.iso.IsoHelper;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.SessionManager;
import com.dwidasa.ib.services.TransferResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 2:22 PM
 */
@PublicPage
public class TransferResourceImpl implements TransferResource {
	
	private static Logger logger = Logger.getLogger( TransferResourceImpl.class );
	
    @Inject
    private TransferService transferService;

    @Inject
    private KioskCheckStatusService kioskCheckStatusService;

    @Inject
    private TransactionService transactionService;

    @Inject
    private TransactionDataService transactionDataService;
    
    @Inject
    private CacheManager cacheManager;
    
    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private CustomerDao customerDao;

    public TransferResourceImpl() {
    }

    public String saveTransfer(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTransferExtract(json);
    }

    public String saveTransfer2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTransfer(customerId, deviceId, sessionId, token, json);
    }

    public String saveTransferPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTransferExtract(json);
    }

    public String saveTransferPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveTransferExtract(json);
    }

    private String saveTransferExtract(String json) {
        TransferView view = PojoJsonMapper.fromJson(json, TransferView.class);
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        if (view.getTransactionDate() == null) {
            view.setTransactionDate(new Date());
        }
        if (view.getProviderCode() != null && view.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE)) {
        	
        	view.setTransactionType(Constants.ATMB.TT_POSTING);         
            view = transferService.executeATMB(view);
        } else if (view.getProviderCode() != null && view.getProviderCode().equals(Constants.ALTO.PROVIDER_CODE)) {
        	
        	view.setTransactionType(Constants.ALTO.TT_POSTING);
            view = transferService.executeALTO(view);
        } else {
            transferService.confirm(view);
            transferService.execute(view);
        }
        if (view.getSave()) {
            transferService.register(view.transform());
        }

        return PojoJsonMapper.toJson(view);
    }

    public String saveTransferBatch(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTransferBatchExtract(json);
    }

    public String saveTransferBatch2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTransfer(customerId, deviceId, sessionId, token, json);
    }

    public String saveTransferBatchPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTransferBatchExtract(json);
    }

    public String saveTransferBatchPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveTransferBatchExtract(json);
    }

    private String saveTransferBatchExtract(String json) {
        TransferView view = PojoJsonMapper.fromJson(json, TransferView.class);
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        if (view.getTransactionDate() == null) {
            view.setTransactionDate(new Date());
        }
        transferService.confirm(view);
        transferService.execute(view);
        if (view.getSave()) {
            transferService.register(view.transform());
        }

        return PojoJsonMapper.toJson(view);
    }

    public String register(Long customerId, String deviceId, String sessionId, String token, String json) {
        return registerExtract(json);
    }

    public String register2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return register(customerId, deviceId, sessionId, token, json);
    }

    public String registerPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return registerExtract(json);
    }

    public String registerPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return registerExtract(json);
    }

    private String registerExtract(String json) {
        TransferView view = PojoJsonMapper.fromJson(json, TransferView.class);
        
        if(Constants.Tambah_Daftar.equals(view.getTransactionType())){
        	view.setTransactionType(Constants.ATMB.TT_POSTING);
        }
        
        transferService.register(view.transform());

        return com.dwidasa.ib.Constants.OK;
    }

    public String unregister(Long customerId, String deviceId, String sessionId, String token,
            Long customerRegisterId) {
        return unregisterExtract(customerRegisterId, customerId);
    }

    public String unregisterPost(Long customerId, String deviceId, String sessionId, String token, Long customerRegisterId) {
        return unregisterExtract(customerRegisterId, customerId);
    }

    public String unregisterPostPin(Long customerId, String deviceId, String sessionId, String token, Long customerRegisterId) {
        return unregisterExtract(customerRegisterId, customerId);
    }

    public String unregisterGet(Long customerId, String sessionId, Long customerRegisterId) {
        return unregisterExtract(customerRegisterId, customerId);
    }

    public String unregisterJson(Long customerId, String sessionId, Long customerRegisterId) {
        transferService.unregister(customerRegisterId, customerId);
        return "{ \"status\" : \"1\" }";
    }

    private String unregisterExtract(Long customerRegisterId, Long customerId) {
        transferService.unregister(customerRegisterId, customerId);
        return com.dwidasa.ib.Constants.OK;
    }

    public String getRegisteredTransfer(Long customerId, String sessionId, String transactionType, String billerCode) {
        return getRegisteredTransferExtract(customerId, transactionType, billerCode);
    }

    public String getRegisteredTransferPost(Long customerId, String sessionId, String transactionType, String billerCode) {
        return getRegisteredTransferExtract(customerId, transactionType, billerCode);
    }

    private String getRegisteredTransferExtract(Long customerId, String transactionType, String billerCode) {
        List<CustomerRegister> crs = transferService.getRegisters(customerId, transactionType, billerCode);
        return PojoJsonMapper.toJson(crs);
    }

    public String inquiryTransfer(Long customerId, String sessionId, String json) {
        return inquiryTransferExtract(json);
    }

    public String inquiryTransferPost(Long customerId, String sessionId, String json) {
        return inquiryTransferExtract(json);
    }

    private String inquiryTransferExtract(String json) {
        TransferView view = PojoJsonMapper.fromJson(json, TransferView.class);
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        if (view.getAmount() == null) {
            view.setAmount(BigDecimal.ONE);
        }
        view.setTransactionDate(new Date());
        
        logger.info("Provider Code : "+view.getProviderCode());
        logger.info("Transaction Code : "+view.getTransactionType());
        
        if (Constants.Tambah_Daftar.equals(view.getTransactionType())) {
//        	if (Constants.TAMBAH_DAFTAR_PROVIDER.equals("ATMB")) {//
//	            view.setProviderCode(Constants.ATMB.PROVIDER_CODE);
//	            
//	            logger.info("Provider Code Atmb : "+view.getProviderCode());
//	            logger.info("Transaction Code Atmb : "+view.getTransactionType());
//	            
//        	} else if (Constants.TAMBAH_DAFTAR_PROVIDER.equals("ALTO")) {
//	            view.setProviderCode(Constants.ALTO.PROVIDER_CODE);
//	            
//	            logger.info("Provider Code Alto : "+view.getProviderCode());
//	            logger.info("Transaction Code Alto : "+view.getTransactionType());
//	            
//        	}
        	
        	//cek apakah kode bank ada di alto
        	Biller biller = cacheManager.getBiller("4b", view.getBillerCode()); 
        	if (biller == null) {// kalo null, berarti kode bank ga ada di alto, maka harus lewat ATMB
	            view.setProviderCode(Constants.ATMB.PROVIDER_CODE);
	            
	            logger.info("Provider Code Atmb : "+view.getProviderCode());
	            logger.info("Transaction Code Atmb : "+view.getTransactionType());
	            
        	} else {
	            view.setProviderCode(Constants.ALTO.PROVIDER_CODE);
	            
	            logger.info("Provider Code Alto : "+view.getProviderCode());
	            logger.info("Transaction Code Alto : "+view.getTransactionType());
	            
        	}
        }

        if (view.getProviderCode() != null ) {
        	
        	if(view.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE)){
        		view.setTransactionType(Constants.ATMB.TT_INQUIRY);       
        		
        		logger.info("Provider Code 1 : "+view.getProviderCode());
                logger.info("Transaction Code 2  : "+view.getTransactionType());
                logger.info("Customer Id : "+view.getCustomerId());
                
                //Set Customer Phone
                Customer customer = customerDao.getWithDefaultAccount(view.getCustomerId());
                view.setTerminalAddress(customer.getCustomerPhone());
                
        		if (view.getCustRefAtmb() == null) view.setCustRefAtmb("");
                view = transferService.inquiryATMB(view);
                return PojoJsonMapper.toJson(view );
        		
        	}else if (view.getProviderCode().equals(Constants.ALTO.PROVIDER_CODE)) {
        		
        		view.setTransactionType(Constants.ALTO.TT_INQUIRY);    
        		
        		
        		logger.info("Provider Code 3 : "+view.getProviderCode());
                logger.info("Transaction Code 4 : "+view.getTransactionType());
                 
                if (view.getCustRefAtmb() == null) view.setCustRefAtmb("");
                view = transferService.inquiryATMB(view);
                return PojoJsonMapper.toJson(view );
                
            } else if (view.getProviderCode().equals(Constants.TREASURY_PROVIDER_CODE)){
                return PojoJsonMapper.toJson(view);
            }
        }

        view = (TransferView) transferService.inquiry(view);

        return PojoJsonMapper.toJson(view);
    }

    public String inquiryTransferBatch(Long customerId, String sessionId, String json) {
        return inquiryTransferBatchExtract(json);
    }

    public String inquiryTransferBatchPost(Long customerId, String sessionId, String json) {
        return inquiryTransferBatchExtract(json);
    }

    private String inquiryTransferBatchExtract(String json) {
        TransferView view = PojoJsonMapper.fromJson(json, TransferView.class);
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        if (view.getAmount() == null) {
            view.setAmount(BigDecimal.ONE);
        }
        view.setTransactionDate(new Date());

        view = (TransferView) transferService.inquiry(view);

        return PojoJsonMapper.toJson(view);
    }

    @Override
    public String checkStatusTransfer(Long customerId, String sessionId, String json) {
        return checkStatusTransferExtract(json);
    }

    @Override
    public String checkStatusTransferPost(Long customerId, String sessionId, String json) {
        return checkStatusTransferExtract(json);
    }

    private String checkStatusTransferExtract(String json) {
        TransferView view = PojoJsonMapper.fromJson(json, TransferView.class);
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        if (view.getTransactionDate() == null) {
            view.setTransactionDate(new Date());
        }

        if (view.getProviderCode() != null && view.getProviderCode().equals(Constants.ATMB.PROVIDER_CODE)) {
            view.setTransactionType(Constants.ATMB.TT_CEK_STATUS);
            Transaction trans = transactionService.getByTransType_AccountNo_CustRef_Amount(Constants.ATMB.TT_POSTING, view.getAccountNumber(), view.getCustomerReference(), view.getAmount());
            view.setReferenceNumber(trans.getReferenceNumber());
            TransactionData transData = transactionDataService.getByTransactionFk(trans.getId());
            String jsonFromDB = transData.getTransactionData();
            TransferView viewFromDB = PojoJsonMapper.fromJson(jsonFromDB, TransferView.class);
            view.setInquiryRefNumber(viewFromDB.getInquiryRefNumber());
            view.setBit42(viewFromDB.getBit42());
            view.setCustRefAtmb(viewFromDB.getCustRefAtmb());
            view.setFee(viewFromDB.getFee());
            view.setReceiverName(viewFromDB.getReceiverName());
            view.setSenderName(viewFromDB.getSenderName());
            view.setBillerName(viewFromDB.getBillerName());
            view = (TransferView) kioskCheckStatusService.checkStatus(view);
        }

        return PojoJsonMapper.toJson(view);
    }

}
