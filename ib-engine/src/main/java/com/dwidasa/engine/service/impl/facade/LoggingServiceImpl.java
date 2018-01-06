package com.dwidasa.engine.service.impl.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.ActivityCustomerDao;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.dao.TransactionDataDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.facade.NativeAdministrationService;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Implementation of logginService interface
 *
 * @author rk
 */
@Service("loggingService")
public class LoggingServiceImpl implements LoggingService {
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private ActivityCustomerDao activityCustomerDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private TransactionDataDao transactionDataDao;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private NativeAdministrationService administrationService;

    /**
     * {@inheritDoc}
     */
    public Boolean incFailedAuthAttempts(Customer customer) {
        customer.setFailedAuthAttempts(customer.getFailedAuthAttempts()+1);
        customerDao.save(customer);

        String maxFailedAuthAttempts = cacheManager.getParameter("FAILED_AUTHENTICATION_ATTEMPTS").
                getParameterValue();

        int slot = Integer.parseInt(maxFailedAuthAttempts) - customer.getFailedAuthAttempts().intValue();

        if (slot <= 0) {
            try {
                administrationService.deactivateService(customer.getId());
            } catch (BusinessException e) {
                //-- do nothing
                e.printStackTrace();
            }
            throw new BusinessException("IB-1015", maxFailedAuthAttempts);
        }
        else if (slot < 3) {
            throw new BusinessException("IB-1020", Integer.toString(slot));
        }

        return Boolean.TRUE;
    }

    public Boolean incFailedTokenAttempts(Customer customer) {
        customer.setFailedAuthAttempts(customer.getFailedAuthAttempts()+1);
        customerDao.save(customer);

        String maxFailedAuthAttempts = cacheManager.getParameter("FAILED_AUTHENTICATION_ATTEMPTS").
                getParameterValue();

        int slot = Integer.parseInt(maxFailedAuthAttempts) - customer.getFailedAuthAttempts().intValue();

        if (slot <= 0) {
            try {
                administrationService.deactivateService(customer.getId());
            } catch (BusinessException e) {
                //-- do nothing
                e.printStackTrace();
            }
            throw new BusinessException("IB-1015", maxFailedAuthAttempts);
        }
        else if (slot < 3) {
            throw new BusinessException("IB-1020", Integer.toString(slot));
        }

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    public void logFailedTransaction(Transaction transaction, BaseView view) {
        if (view instanceof VoucherPurchaseView) {
            transaction.setTransactionAmount(view.getAmount());
        }

        EngineUtils.setTransactionStatus(transaction);
        transactionDao.save(transaction);
        transactionDataDao.save(EngineUtils.createTransactionData(view, transaction.getId()));
    }
    
    @Override
	public void logActivity(Long customerId, String activityType, String activityData, String referenceNumber, String merchantType, String terminalId) {
		activityCustomerDao.logActivity(customerId, activityType, activityData, referenceNumber, merchantType, terminalId);
	}

}
