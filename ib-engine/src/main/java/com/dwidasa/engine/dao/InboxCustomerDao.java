package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.InboxCustomer;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 7:14 PM
 */
public interface InboxCustomerDao extends GenericDao<InboxCustomer, Long> {
    /**
     * Mark customer message as
     *      0 = unread
     *      1 = read / unstarred
     *      2 = starred
     * @param inboxCustomerId customer inbox id
     * @param status message status to be set
     * @return customer inbox object after operation has been applied
     */
    public InboxCustomer markMessage(Long inboxCustomerId, Integer status);

    /**
     * Get message inbox by customer id, if last request date is null, then all message
     * owned by a customer will be returned. Otherwise, this service will return data that
     * is created after lastRequestDate only.
     * @param customerId customer id
     * @param lastRequestDate last request date
     * @return list of customer's inbox object own by specified customer user
     */
    public List<InboxCustomer> getAllWithInbox(Long customerId, Date lastRequestDate);

    public List<InboxCustomer> getAllWithInboxList(Long customerId, int awal, int akhir, Date lastRequestDate);
    
    /**
     * Get inbox customer with relation to inbox object intialized.
     * @param id inbox customer id
     * @return inbox customer object
     */
    public InboxCustomer getWithInbox(Long id);
    
    
    public List<InboxCustomer> getAllWithInboxId(Long inboxID, Long customerID, Date lastRequestDate);
    
    
    
    public void removeByIdCus(Long inboxId, Long customerId);
    
    public void removeAllById(Long inboxId);
    
    public void updateStatusById(Long inboxId, Long customerId, int status);
    
    public List<InboxCustomer> getAllUnreadMessage(Long customerId);
    
}
