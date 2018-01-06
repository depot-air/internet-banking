package com.dwidasa.engine.service;

import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.InboxCustomer;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/1/11
 * Time: 9:46 AM
 */
public interface InboxService extends GenericService<Inbox, Long> {
    /**
     * Sending message to several customer id
     * @param customerIds list of customer id
     * @param inbox object that contain the message string
     */
    public void sendMessage(List<Long> customerIds, Inbox inbox);

    /**
     * Sending message to all customer
     * @param inbox object that contain the message string
     */
    public void sendMessage(Inbox inbox);

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
    public List<InboxCustomer> getCustomerInboxes(Long customerId,  Date lastRequestDate);

    
    public List<InboxCustomer> getCustomerInboxesAll(Long customerId, int awal, int akhir, Date lastRequestDate);
    /**
     * Get inbox customer with relation to inbox object intialized.
     * @param id inbox customer id
     * @return inbox customer object
     */
    public InboxCustomer getCustomerInbox(Long id);
    
    public void updateStatusInboxCustomer(Long inboxCustomerId, Long customerId, Integer status);
}
