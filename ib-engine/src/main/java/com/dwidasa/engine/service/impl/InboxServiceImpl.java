package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.dao.InboxDao;
import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.InboxCustomer;
import com.dwidasa.engine.service.InboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/1/11
 * Time: 9:47 AM
 */
@Service("inboxService")
public class InboxServiceImpl extends GenericServiceImpl<Inbox, Long> implements InboxService {
    @Autowired
    private InboxDao inboxDao;

    @Autowired
    private InboxCustomerDao inboxCustomerDao;

    @Autowired
    public InboxServiceImpl(InboxDao inboxDao) {
        super(inboxDao);
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(List<Long> customerIds, Inbox inbox) {
        inboxDao.sendMessage(customerIds, inbox);
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(Inbox inbox) {
        inboxDao.sendMessage(inbox);
    }

    /**
     * {@inheritDoc}
     */
    public InboxCustomer markMessage(Long inboxCustomerId, Integer status) {
        return inboxCustomerDao.markMessage(inboxCustomerId, status);
    }
    
    public void updateStatusInboxCustomer(Long inboxCustomerId, Long customerId, Integer status){
    	inboxCustomerDao.updateStatusById(inboxCustomerId, customerId, status);
    }

    /**
     * {@inheritDoc}
     */
    public List<InboxCustomer> getCustomerInboxes(Long customerId,  Date lastRequestDate) {
        return inboxCustomerDao.getAllWithInbox(customerId, lastRequestDate);
    }
    
    
    public List<InboxCustomer> getCustomerInboxesAll(Long customerId, int awal, int akhir, Date lastRequestDate) {
        return inboxCustomerDao.getAllWithInboxList(customerId, awal, akhir, lastRequestDate);
    }

    /**
     * {@inheritDoc}
     */
    public InboxCustomer getCustomerInbox(Long id) {
        return inboxCustomerDao.getWithInbox(id);
    }
}
