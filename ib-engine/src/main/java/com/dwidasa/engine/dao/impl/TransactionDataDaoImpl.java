package com.dwidasa.engine.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.TransactionDataDao;
import com.dwidasa.engine.dao.mapper.TransactionDataMapper;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.util.DateUtils;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 3:54 PM
 */
@Repository("transactionDataDao")
public class TransactionDataDaoImpl extends GenericDaoImpl<TransactionData, Long> implements TransactionDataDao {
    private static Logger logger = Logger.getLogger( TransactionDataDaoImpl.class );

    @Autowired
    public TransactionDataDaoImpl(DataSource dataSource, TransactionDataMapper transactionDataMapper) {
        super("t_transaction_data", dataSource);
        defaultMapper = transactionDataMapper;

        insertSql = new StringBuilder()
            .append("insert into t_transaction_data ( ")
            .append("   t_transaction_id, class_name, transaction_data, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :transactionId, :className, :transactionData, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_transaction_data ")
            .append("set ")
            .append("   t_transaction_id = :transactionId, transaction_data = :transactionData, ")
            .append("   class_name = :className, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public TransactionData getByTransactionFk(Long transactionId) {
        StringBuilder sql = new StringBuilder()
                .append("select ttd.* ")
                .append("from t_transaction_data ttd ")
                .append("where ttd.t_transaction_id = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, transactionId);
    }
    
	@Override
	public TransactionData getByTransactionFk(Long id, String accountNumber) {
		StringBuilder sql = new StringBuilder()
	        .append("select ttd.* ")
	        .append(" from t_transaction_data ttd ")
	        .append(" inner join t_transaction t on t.id=ttd.t_transaction_id")
	        .append(" where ttd.t_transaction_id = ? and t.from_account_number=?");

		TransactionData result = null;
		try {
			result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, new Object[] {id, accountNumber}); 
		} catch (EmptyResultDataAccessException e) {
			//do nothing
			e.printStackTrace();
		}
		return result;

	}
    /*
    transaction_data LIKE '%"denomination":"40000"%"customerReference":"11111130053"%'
    OR
    transaction_data LIKE '%"denomination":"40000"%"meterNumber":"11111130053"%'
     */
    @Override
    public TransactionData getForPlnReprint(String denomination, String accountNumber, String idPelNoMeter) {
//        String queryParam =  "(" +
//                "transaction_data LIKE '%\"denomination\":\"" + denomination + "\"%\"customerReference\":\"" + idPelNoMeter + "\"%' " +
//                "OR " +
//                "transaction_data LIKE '%\"denomination\":\"" + denomination + "\"%\"meterNumber\":\"" + idPelNoMeter + "\"%' " +
//                "OR " +
//                "transaction_data LIKE '%\"customerReference\":\"" + idPelNoMeter + "\"%\"denomination\":\"" + denomination + "\"%' " +
//                "OR " +
//                "transaction_data LIKE '%\"meterNumber\":\"" + idPelNoMeter + "\"%\"denomination\":\"" + denomination + "\"%' " +
//                ")";
//        String orderBy = " ORDER BY id DESC ";
//        StringBuilder sql = new StringBuilder()
//                .append("select ttd.* ")
//                .append("from t_transaction_data ttd ")
//                .append("WHERE class_name LIKE '%PlnPurchaseView%' ")
//                .append("AND " + queryParam + orderBy);

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	StringBuilder sql = new StringBuilder()
    		.append(" select ttd.* from t_transaction t ")
    		.append(" inner join t_transaction_data ttd on ttd.t_transaction_id=t.id ")
    		.append(" where t.transaction_type='9a' ")
    		.append(" and t.transaction_amount=" + denomination)
    		.append(" and ttd.transaction_data like '%" + idPelNoMeter + "%'") 
    		.append(" order by id desc limit 1 ");
    		
        logger.info("sql for reprint beli PLN= " + sql);        
        List<TransactionData> tds = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
        logger.info("tds= " + tds);
        if (tds != null && tds.size() > 0) {
            return  tds.get(0);
        }
        return null;
        //return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper);
    }

	@Override
	public void insertTransactionData(TransactionData transactionData) {
	    SqlParameterSource parameters = new BeanPropertySqlParameterSource(transactionData);
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    new NamedParameterJdbcTemplate(getDataSource()).update(insertSql.toString(), parameters, keyHolder);
	}

	@Override
	public void updateTransactionData(TransactionData transactionData) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(transactionData);
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    new NamedParameterJdbcTemplate(getDataSource()).update(updateSql.toString(), parameters, keyHolder);
	}

}
