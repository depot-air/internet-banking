package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Inbox;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 3:28 PM
 */
public interface InboxDao extends GenericDao<Inbox, Long> {
    /**
     * Sending message to several customer id.
     * @param customerIds list of customer id
     * @param inbox object that contain the message string
     */
    public void sendMessage(List<Long> customerIds, Inbox inbox);

    /**
     * Sending message to all customer.
     * @param inbox object that contain the message string
     */
    public void sendMessage(Inbox inbox);
}
