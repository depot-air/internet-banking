package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.TransactionHistoryDao;
import com.dwidasa.engine.dao.mapper.TransactionHistoryMapper;
import com.dwidasa.engine.model.TransactionHistory;
import com.dwidasa.engine.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/20/11
 * Time: 4:39 PM
 */
@Repository("transactionHistoryDao")
public class TransactionHistoryDaoImpl extends GenericDaoImpl<TransactionHistory, Long>
        implements TransactionHistoryDao {
    @Autowired
    public TransactionHistoryDaoImpl(DataSource dataSource, TransactionHistoryMapper transactionHistoryMapper) {
        super("h_transaction", dataSource);
        defaultMapper = transactionHistoryMapper;

        insertSql = new StringBuilder()
            .append("insert into h_transaction ( ")
            .append("   account_number, currency, transaction_indicator, transaction_amount, reference_number, ")
            .append("   entry_date, posting_date, processing_date, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :accountNumber, :currency, :transactionIndicator, :transactionAmount, :referenceNumber, ")
            .append("   :entryDate, :postingDate, :processingDate, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update h_transaction ")
            .append("set ")
            .append("   account_number = :accountNumber, transaction_indicator = :transactionIndicator, ")
            .append("   transaction_amount = :transactionAmount, reference_number = :referenceNumber, ")
            .append("   entry_date = :entryDate, posting_date = :postingDate, processing_date = :processingDate")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public void remove(Date processingDate) {
        StringBuilder sql = new StringBuilder()
                .append("delete from h_transaction ")
                .append("where processing_date >= ? ")
                .append("and processing_date < ? ");

        getSimpleJdbcTemplate().update(sql.toString(), DateUtils.generateStart(processingDate),
                DateUtils.generateEnd(processingDate));
    }

    /**
     * {@inheritDoc}
     */
    public List<TransactionHistory> getAll(String accountNumber, Date startDate, Date endDate) {
        StringBuilder sql = new StringBuilder()
                .append("select ht.* ")
                .append("from h_transaction ht ")
                .append("where ht.account_number = ? ")
                .append("and ht.posting_date >= ? ")
                .append("and ht.posting_date < ? ")
                .append("order by ht.posting_date, ht.id asc ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, accountNumber,
                DateUtils.generateStart(startDate), DateUtils.generateEnd(endDate));
    }

    /**
     * {@inheritDoc}
     */
    public List<TransactionHistory> getLastN(String accountNumber, int n) {
        StringBuilder sql = new StringBuilder()
                .append("select ot.* ")
                .append("from (")
                .append("   select ht.* ")
                .append("   from h_transaction ht ")
                .append("   where ht.account_number = ? ")
                .append("   order by id desc ")
                .append("   limit ? ")
                .append(") ot ")
                .append("order by ot.id ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, accountNumber, n);
    }
}
