package com.dwidasa.engine.service.impl.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerRegisterDao;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.dao.TransactionDataDao;
import com.dwidasa.engine.dao.TreasuryStageDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.TreasuryStage;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.HpPaymentView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.model.view.TiketKeretaDjatiPurchaseView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.model.view.WaterPaymentView;
import com.dwidasa.engine.service.MessageMailer;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.BaseTransactionService;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.BaseViewService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.MoneyUtils;
import com.dwidasa.engine.util.ReferenceGenerator;

/**
 * An implementation class of <code>BaseTransactionService</code> interface that will
 * perform common task upon various transactions.
 *
 * @author rk
 */
public class BaseTransactionServiceImpl implements BaseTransactionService {
	private Logger logger = Logger.getLogger(BaseTransactionServiceImpl.class);
    @Autowired
    protected TransactionDao transactionDao;

    @Autowired
    protected TransactionDataDao transactionDataDao;

    @Autowired
    private CustomerRegisterDao customerRegisterDao;

    @Autowired
    private TreasuryStageDao treasuryStageDao;

    @Autowired
    protected LoggingService loggingService;

    @Autowired
    protected TransactionService transactionService;

    @Autowired
    protected MessageMailer mailer;

    public BaseTransactionServiceImpl() {
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

    /**
     * {@inheritDoc}
     */
    public BaseView inquiry(BaseView view) {
        BaseViewService viewService = getServiceObject(view);
        viewService.preProcess(view);

        Transaction transaction = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        
        viewService.composeInquiry(view, transaction);
        CommLink link = new MxCommLink(transaction);
        link.sendMessage();
        //transaction.setResponseCode(Constants.SUCCESS_CODE);
        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            viewService.decomposeInquiry(view, transaction);
        }
        else {
        	if (view instanceof VoucherPurchaseView || view instanceof HpPaymentView) {
            	transaction.setResponseCode(getMappedResponseCode(transaction.getResponseCode(), view));
            } else if (view instanceof PlnPaymentView && transaction.getResponseCode().equals("63")) {	//RC63 = "NOMOR METER/IDPEL YANG ANDA MASUKKAN SALAH, MOHON TELITI KEMBALI"	
            	BusinessException be = new BusinessException("IB-1009", transaction.getResponseCode());
            	String msg = be.getFullMessage().substring(12);
            	be.setFullMessage(msg.toUpperCase());
            	throw be;
            } else if( view instanceof WaterPaymentView && transaction.getResponseCode().equals("10")){
            	BusinessException be = new BusinessException("IB-1009", "CN");
            	String msg = be.getFullMessage();
            	be.setFullMessage(msg);
            	throw be;
            } else if( view instanceof TiketKeretaDjatiPurchaseView && transaction.getResponseCode().equals("08")){
            	BusinessException be = new BusinessException("IB-1009", "DU");
            	String msg = be.getFullMessage();
            	be.setFullMessage(msg);
            	throw be;
            } else if( view instanceof TiketKeretaDjatiPurchaseView && transaction.getResponseCode().equals("01")){
            	BusinessException be = new BusinessException("IB-1009", "DV");
            	String msg = be.getFullMessage();
            	be.setFullMessage(msg);
            	throw be;
            } else if( view instanceof TiketKeretaDjatiPurchaseView && transaction.getResponseCode().equals("06")){
            	BusinessException be = new BusinessException("IB-1009", "DW");
            	String msg = be.getFullMessage();
            	be.setFullMessage(msg);
            	throw be;
            } 
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        EngineUtils.setTransactionStatus(transaction);
        return view;
    }

	private void checkTransferAmount(Transaction transaction) {
		BigDecimal totalInOneDay = transactionService.getTotalTransferInOneDay(transaction.getFromAccountNumber(), transaction.getToAccountNumber(), transaction.getTransactionDate());
		BigDecimal totalAll = totalInOneDay.add(transaction.getTransactionAmount());
		if (totalAll.compareTo(Constants.TRANSFER_AMOUNT.MAX_IN_ONE_DAY) == 1) {
			throw new BusinessException("IB-1009", "AR", MoneyUtils.getMoney(Constants.TRANSFER_AMOUNT.MAX_IN_ONE_DAY), transaction.getToAccountNumber(), MoneyUtils.getMoney(totalInOneDay));
		}
	}

    /**
     * {@inheritDoc}
     */
    public void confirm(BaseView view) {
        BaseViewService viewService = getServiceObject(view);
        viewService.preProcess(view);
    }

    /**
     * {@inheritDoc}
     */
    public BaseView execute(BaseView view) {
        BaseViewService viewService = getServiceObject(view);
        viewService.validate(view);

        Transaction transaction = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
       
        if (transaction.getTransactionType().equals(Constants.TRANSFER_CODE)) {
        	checkTransferAmount(transaction);	
        }
        
        viewService.composeTransaction(view, transaction);
        transaction.setExecutionType(Constants.NOW_ET);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();
        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            viewService.decomposeTransaction(view, transaction);
        }
        else {
            loggingService.logFailedTransaction(transaction, view);
            if (view instanceof VoucherPurchaseView || view instanceof HpPaymentView) {
            	transaction.setResponseCode(getMappedResponseCode(transaction.getResponseCode(), view));
            }            
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        EngineUtils.setTransactionStatus(transaction);
//        transaction.setCardNumber(EngineUtils.encrypt(com.dwidasa.engine.Constants.SERVER_SECRET_KEY, 
//				transaction.getCardNumber()));
        transactionDao.save(transaction);
        if(!transaction.getTransactionType().equals(Constants.INQUIRY_LOTTERY)){
        	if (!transaction.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Posting_Pembelian)) {
            	transactionDataDao.save(EngineUtils.createTransactionData(view, transaction.getId()));
            } else {
            	logger.info("transaction.getId()=" + transaction.getId());
            	transactionDataDao.insertTransactionData(EngineUtils.createTransactionData(view, transaction.getId()));
            }
        }
        
        
        
        mailer.sendTransactionMessage(view.getClass().getSimpleName()+".vm", view);
        //treasury insert ke t_treasury_stage
        if (view.getTransactionType().equals(Constants.TRANSFER_TREASURY_CODE) && transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	TransferView tv = (TransferView) view;
        	TreasuryStage treasuryStage = new TreasuryStage();
        	treasuryStage.setTransactionId(transaction.getId());
        	CustomerRegister custRegister = customerRegisterDao.get(transaction.getCreatedby(), Constants.TRANSFER_TREASURY_CODE, tv.getCustRefAtmb());
        	treasuryStage.setCustomerRegisterId(custRegister.getId());
        	treasuryStage.setSenderId(transaction.getCreatedby());
        	treasuryStage.setStatus(Constants.QUEUED_STATUS);
        	treasuryStage.setCreated(new Date());
        	treasuryStage.setCreatedby(transaction.getCreatedby());
        	treasuryStage.setUpdated(new Date());
        	treasuryStage.setUpdatedby(transaction.getCreatedby());
        	logger.info("" + treasuryStage.getTransactionId() + " , " + treasuryStage.getCustomerRegisterId() + " , " + treasuryStage.getSenderId()  + " , " + treasuryStage.getStatus() );
        	treasuryStageDao.save(treasuryStage);
        }
        return view;
    }

    
    /**
     * {@inheritDoc}
     */
    public BaseView executeInqNomorUndian(BaseView view) {
        BaseViewService viewService = getServiceObject(view);
        viewService.validate(view);

        Transaction transaction = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        
        if (transaction.getTransactionType().equals(Constants.TRANSFER_CODE)) {
        	checkTransferAmount(transaction);	
        }
        
        viewService.composeInquiry(view, transaction);
        transaction.setExecutionType(Constants.NOW_ET);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();
        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            viewService.decomposeInquiry(view, transaction);
        }
        else {
            loggingService.logFailedTransaction(transaction, view);
            if (view instanceof VoucherPurchaseView || view instanceof HpPaymentView) {
            	transaction.setResponseCode(getMappedResponseCode(transaction.getResponseCode(), view));
            }            
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        EngineUtils.setTransactionStatus(transaction);
        transactionDao.save(transaction);
        if(!transaction.getTransactionType().equals(Constants.INQUIRY_LOTTERY)){
        	if (!transaction.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Posting_Pembelian)) {
            	transactionDataDao.save(EngineUtils.createTransactionData(view, transaction.getId()));
            } else {
            	logger.info("transaction.getId()=" + transaction.getId());
            	transactionDataDao.insertTransactionData(EngineUtils.createTransactionData(view, transaction.getId()));
            }
        }
        
        
        
        mailer.sendTransactionMessage(view.getClass().getSimpleName()+".vm", view);
        //treasury insert ke t_treasury_stage
        if (view.getTransactionType().equals(Constants.TRANSFER_TREASURY_CODE) && transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	TransferView tv = (TransferView) view;
        	TreasuryStage treasuryStage = new TreasuryStage();
        	treasuryStage.setTransactionId(transaction.getId());
        	CustomerRegister custRegister = customerRegisterDao.get(transaction.getCreatedby(), Constants.TRANSFER_TREASURY_CODE, tv.getCustRefAtmb());
        	treasuryStage.setCustomerRegisterId(custRegister.getId());
        	treasuryStage.setSenderId(transaction.getCreatedby());
        	treasuryStage.setStatus(Constants.QUEUED_STATUS);
        	treasuryStage.setCreated(new Date());
        	treasuryStage.setCreatedby(transaction.getCreatedby());
        	treasuryStage.setUpdated(new Date());
        	treasuryStage.setUpdatedby(transaction.getCreatedby());
        	logger.info("" + treasuryStage.getTransactionId() + " , " + treasuryStage.getCustomerRegisterId() + " , " + treasuryStage.getSenderId()  + " , " + treasuryStage.getStatus() );
        	treasuryStageDao.save(treasuryStage);
        }
        return view;
    }
    /**
     * {@inheritDoc}
     */
    public BaseView reprint(BaseView view, Long transactionId) {
        Transaction ot = transactionDao.get(transactionId);
        BaseViewService viewService = getServiceObject(view);
        viewService.preProcess(view);

        Transaction transaction = TransformerFactory.getTransformer(view)
                .transformTo(view, new Transaction());
        viewService.composeReprint(view, transaction);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();
        logger.info("base reprint transaction=" + transaction.getCustomerReference());
        logger.info("base reprint transaction.getResponseCode()=" + transaction.getResponseCode());
        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {        	
            viewService.decomposeReprint(view, transaction);

            ot.setResponseCode(transaction.getResponseCode());
            ot.setUpdated(new Date());
            ot.setUpdatedby(view.getCustomerId());

            if (!(view instanceof VoucherPurchaseView)) {
                ot.setStatus(null);
                EngineUtils.setTransactionStatus(ot);
            }
            logger.info("bbefore save, ot=" + ot);
            if (!transaction.getTransactionType().equals(Constants.MNCLIFE.Mnc_Live_Reprint)) {
	            transactionDao.save(ot);
	
	            TransactionData data = transactionDataDao.getByTransactionFk(ot.getId());
	            data.setTransactionData(PojoJsonMapper.toJson(view));
	            data.setUpdated(new Date());
	            logger.info("before save transactionData, data=" + data);
	            
            	transactionDataDao.save(data);
//            } else {
//            	logger.info("transaction.getId()=" + transaction.getId());
//            	transactionDataDao.insertTransactionData(data);
            }
        }
        else {
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        return view;
    }

    /**
     * {@inheritDoc}
     */
    public ResultView register(CustomerRegister register) {
        CustomerRegister cr = customerRegisterDao.get(register.getCustomerId(), register.getTransactionType(),
                register.getCustomerReference());

        if (cr == null) {
            cr = register;
        }
        else {
            cr.setData1(register.getData1());
            cr.setData2(register.getData2());
            cr.setData3(register.getData3());
            cr.setData4(register.getData4());
            cr.setData5(register.getData5());
            cr.setUpdated(new Date());
        }

        customerRegisterDao.save(cr);

        ResultView view = new ResultView();
        view.setReferenceNumber(ReferenceGenerator.generate());
        view.setResponseCode(Constants.SUCCESS_CODE);
        return view;
    }

    /**
     * {@inheritDoc}
     */
    public ResultView unregister(Long customerRegisterId, Long customerId) {
        customerRegisterDao.remove(customerRegisterId, customerId);

        ResultView view = new ResultView();
        view.setReferenceNumber(ReferenceGenerator.generate());
        view.setResponseCode(Constants.SUCCESS_CODE);
        return view;
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerRegister> getRegisters(Long customerId, String transactionType, String billerCode) {
        if (billerCode == null || billerCode.trim().equals("")){
            return customerRegisterDao.getByTransactionType(customerId, transactionType);
        }
        return customerRegisterDao.getAll(customerId, transactionType, billerCode);
    }
    public List<CustomerRegister> getRegistersOrderBy(Long customerId, String transactionType, String billerCode, String orderBy) {
        return customerRegisterDao.getAllOrderBy(customerId, transactionType, billerCode, orderBy);
    }
    
    
    private String getMappedResponseCode(String responseCode, BaseView view) {
        //SERA
        String providerCode = "";        
        if (view instanceof VoucherPurchaseView ){
            VoucherPurchaseView vpv = (VoucherPurchaseView) view;
            providerCode = vpv.getProviderCode();
        } else if (view instanceof HpPaymentView) {
            HpPaymentView hpv = (HpPaymentView) view;
            providerCode = hpv.getProviderCode();
        } else {
            return responseCode;
        }        
        if (providerCode.equals(Constants.PAYEE_ID.SERA)) {
            if (responseCode.equals("46")) return "SA";
            else if (responseCode.equals("72")) return "SB";
        }
        return responseCode;
    }
    
        
}
