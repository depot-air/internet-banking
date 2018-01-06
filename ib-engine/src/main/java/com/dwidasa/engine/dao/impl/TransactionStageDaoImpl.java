package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransactionStageDao;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.TransactionMapper;
import com.dwidasa.engine.dao.mapper.TransactionStageMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/26/11
 * Time: 2:38 PM
 */
@Repository("transactionStageDao")
public class TransactionStageDaoImpl extends GenericDaoImpl<TransactionStage, Long> implements TransactionStageDao {
    private final ChainedRowMapper<Transaction> transactionMapper;

    @Autowired
    public TransactionStageDaoImpl(DataSource dataSource, TransactionStageMapper transactionStageMapper,
                                   TransactionMapper transactionMapper) {
        super("t_transaction_stage", dataSource);
        defaultMapper = transactionStageMapper;
        this.transactionMapper = transactionMapper;

        insertSql = new StringBuilder()
            .append("insert into t_transaction_stage ( ")
            .append("   t_transaction_id, status, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :transactionId, :status, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_transaction_stage ")
            .append("set ")
            .append("   t_transaction_id = :transactionId, status = :status, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public TransactionStage getByTransactionFk(Long transactionId) {
        StringBuilder sql = new StringBuilder()
                .append("select tts.* ")
                .append("from t_transaction_stage tts ")
                .append("where tts.t_transaction_id = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, transactionId);
    }

    @Override
    public List<TransactionStage> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                     List<String> orders, Object... params) {
        String sql =
                  "select ts.*, tt.* "
                + "from t_transaction_stage ts "
                + "join t_transaction tt "
                + "   on tt.id = ts.t_transaction_id ";

        if (restrictions != null && restrictions.size() != 0) {
            sql += "where ";
            for (String restriction : restrictions) {
                sql += restriction;
                sql += " and ";
            }

            sql = sql.substring(0, sql.length()-4);
        }

        if (orders != null && orders.size() != 0) {
            sql += "order by ";
            for (String order : orders) {
                sql += order;
                sql += ", ";
            }

            sql = sql.substring(0, sql.length()-2);
        }

        sql += " limit ? offset ?";
        int pageNo = startIndex / pageSize + (startIndex % pageSize == 0 ? 0 : 1) + 1;

        Object[] newParams;
        int index;

        if (params != null) {
            newParams = new Object[params.length + 2];
            index = params.length;

            System.arraycopy(params, 0, newParams, 0, params.length);
        }
        else {
            newParams = new Object[2];
            index = 0;
        }

        newParams[index++] = pageSize;
        newParams[index] = (pageNo - 1) * pageSize;

        return getSimpleJdbcTemplate().query(sql, new RowMapper<TransactionStage>() {
                    public TransactionStage mapRow(ResultSet rs, int rowNum) throws SQLException {
                        int index = 0;
                        TransactionStage ts = defaultMapper.mapRow(rs, index);
                        index += Constants.TRANSACTION_STAGE_LENGTH;
                        Transaction t = transactionMapper.chainRow(rs, index);
                        ts.setTransaction(t);

                        return ts;
                    }
                }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                  "select count(*) "
                + "from t_transaction_stage ts "
                + "join t_transaction tt "
                + "   on tt.id = ts.t_transaction_id ";

        if (restrictions != null && restrictions.size() != 0) {
            sql += "where ";
            for (String restriction : restrictions) {
                sql += restriction;
                sql += " and ";
            }

            sql = sql.substring(0, sql.length()-4);
        }

        return getSimpleJdbcTemplate().queryForInt(sql, params);
    }
}
