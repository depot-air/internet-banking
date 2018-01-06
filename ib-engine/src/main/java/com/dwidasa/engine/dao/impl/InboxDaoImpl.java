package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.InboxDao;
import com.dwidasa.engine.dao.mapper.InboxMapper;
import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.InboxCustomer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 3:28 PM
 */
@Repository("inboxDao")
public class InboxDaoImpl extends GenericDaoImpl<Inbox, Long> implements InboxDao {
    @Autowired
    public InboxDaoImpl(DataSource dataSource, InboxMapper inboxMapper) {
        super("t_inbox", dataSource);
        defaultMapper = inboxMapper;

        insertSql = new StringBuilder()
            .append("insert into t_inbox ( ")
            .append("   title, content, start_date, end_date, created, createdby, updated, updatedby, for_all ")
            .append(") ")
            .append("values ( ")
            .append("   :title, :content, :startDate, :endDate, :created, :createdby, :updated, :updatedby, :forAll ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_inbox ")
            .append("set ")
            .append("   title = :title, content = :content, start_date = :startDate, end_date = :endDate, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby, for_all = :forAll ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(List<Long> customerIds, Inbox inbox) {
        inbox = save(inbox);

        String ids = "";
        for (Long id : customerIds) {
            ids = ids + id + ",";
        }
        ids = ids.substring(0, ids.length()-1);

        //-- TODO this implementation must be replaced with batch insert version
        //-- because it doesn't utilize bind variable correctly
        StringBuilder sql = new StringBuilder()
                .append("insert into t_inbox_customer ( ")
                .append("   t_inbox_id, m_customer_id, created, createdby, updated, updatedby ")
                .append(") ")
                .append("select ?, id, ?, ?, ?, ? ")
                .append("from m_customer mc ")
                .append("where mc.id in ( ")
                .append(    ids)
                .append(") ");

        getSimpleJdbcTemplate().update(sql.toString(), inbox.getId(), inbox.getCreated(), inbox.getCreatedby(),
                inbox.getUpdated(), inbox.getUpdatedby());
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(Inbox inbox) {
        inbox = save(inbox);

        if (inbox.getInboxCustomers() == null) return;
        if (inbox.getInboxCustomers().size() == 0) return;
        
        String ids = "";
        for (InboxCustomer ic : inbox.getInboxCustomers()) {
            ids = ids + ic.getId() + ",";
        }
        ids = ids.substring(0, ids.length()-1);

        StringBuilder sql = new StringBuilder()
                .append("insert into t_inbox_customer ( ")
                .append("   t_inbox_id, m_customer_id, created, createdby, updated, updatedby ")
                .append(") ")
                .append("select ?, mc.id, ?, ?, ?, ? ")
                .append("from m_customer mc where mc.id in (")
                .append(ids)
                .append(")");

        getSimpleJdbcTemplate().update(sql.toString(), inbox.getId(), inbox.getCreated(), inbox.getCreatedby(),
                inbox.getUpdated(), inbox.getUpdatedby());
    }
}
