package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.BalanceDao;
import com.dwidasa.engine.dao.mapper.BalanceMapper;
import com.dwidasa.engine.model.Balance;
import com.dwidasa.engine.util.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/3/11
 * Time: 3:22 PM
 */
@Repository("balanceDao")
public class BalanceDaoImpl extends GenericDaoImpl<Balance, Long> implements BalanceDao {

    private Logger logger = Logger.getLogger(BalanceDao.class);

    @Autowired
    public BalanceDaoImpl(DataSource dataSource, BalanceMapper balanceMapper) {
        super("h_balance", dataSource);
        defaultMapper = balanceMapper;

        insertSql = new StringBuilder()
            .append("insert into h_balance ( ")
            .append("   account_number, date, currency_code, amount, created, createdby, ")
            .append("   updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :accountNumber, :date, :currencyCode, :amount, :created, :createdby, ")
            .append("   :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update h_balance ")
            .append("set ")
            .append("   account_number = :accountNumber, date = :date, currency_code = :currencyCode, ")
            .append("   amount = :amount, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Balance get(String accountNumber, Date date) {
        logger.info("accountNumber = " + accountNumber + ", date = " + date);

        StringBuilder sql = new StringBuilder()
                .append("select hb.* ")
                .append("from h_balance hb ")
                .append("where hb.account_number = ? ")
                .append("and hb.date >= ? ")
                .append("and hb.date < ? ");

        Balance result = null;

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, accountNumber,
                    DateUtils.generateStart(date), DateUtils.generateEnd(date));
        } catch (DataAccessException e) {
        }

        return result;
    }

     public Balance getEndingBalance(String accountNumber, Date startDate, Date endDate){
        logger.info("accountNumber = " + accountNumber + ", startDate = " + startDate + ", endDate = " + endDate);

        StringBuilder sql = new StringBuilder()
                .append("select hb.* ")
                .append("from h_balance hb ")
                .append("where hb.account_number = ? ")
                .append("and hb.date >= ? ")
                .append("and hb.date < ? order by hb.date desc limit 1 ");

        Balance result = null;

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, accountNumber,
                    DateUtils.generateStart(startDate), DateUtils.generateEnd(endDate));
        } catch (DataAccessException e) {
        }

        return result;
     }

    /**
     * {@inheritDoc}
     */
    public void remove(Date processingDate) {
        StringBuilder sql = new StringBuilder()
                .append("delete from h_balance ")
                .append("where date >= ? ")
                .append("and date < ? ");

        getSimpleJdbcTemplate().update(sql.toString(), DateUtils.generateStart(processingDate),
                DateUtils.generateEnd(processingDate));
    }

	@Override
	public Balance getLastBalance(String accountNumber) {
        StringBuilder sql = new StringBuilder()
                .append("select hb.* ")
                .append("from h_balance hb ")
                .append("where hb.account_number = ? ")
                .append("order by hb.date desc limit 1 ");

        Balance result = null;

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, accountNumber);
        } catch (DataAccessException e) {
        }

        return result;
	}
}
