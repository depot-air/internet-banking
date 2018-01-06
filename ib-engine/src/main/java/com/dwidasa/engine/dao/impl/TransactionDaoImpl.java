package com.dwidasa.engine.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.TransactionMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.util.DateUtils;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/23/11
 * Time: 3:10 PM
 */
@Repository("transactionDao")
public class TransactionDaoImpl extends GenericDaoImpl<Transaction, Long> implements TransactionDao {
	private final ChainedRowMapper<TransactionType> transactionTypeMapper;
	
    @Autowired
    public TransactionDaoImpl(DataSource dataSource, TransactionMapper transactionMapper, TransactionTypeMapper transactionTypeMapper) {
        super("t_transaction", dataSource);
        defaultMapper = transactionMapper;
        this.transactionTypeMapper = transactionTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into t_transaction ( ")
            .append("   mti, transaction_type, card_number, transaction_amount, fee_indicator, ")
            .append("   fee, transmission_date, transaction_date, value_date, conversion_rate, stan, status, ")
            .append("   merchant_type, terminal_id, reference_number, approval_number, response_code, currency_code, ")
            .append("   customer_reference, from_account_number, to_account_number, from_account_type, ")
            .append("   to_account_type, balance, description, to_bank_code, execution_type, ")
            .append("   translation_code, free_data1, free_data2, free_data3, free_data4, free_data5, ")
            .append("   created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :mti, :transactionType, :cardNumber, :transactionAmount, :feeIndicator, ")
            .append("   :fee, :transmissionDate, :transactionDate, :valueDate, :conversionRate, :stan, :status,")
            .append("   :merchantType, :terminalId, :referenceNumber, :approvalNumber, :responseCode, :currencyCode, ")
            .append("   :customerReference, :fromAccountNumber, :toAccountNumber, :fromAccountType, ")
            .append("   :toAccountType, :balance, :description, :toBankCode, :executionType, ")
            .append("   :translationCode, :freeData1, :freeData2, :freeData3, :freeData4, :freeData5, ")
            .append("   :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_transaction ")
            .append("set ")
            .append("   mti = :mti, transaction_type = :transactionType, card_number = :cardNumber, ")
            .append("   transaction_amount = :transactionAmount, fee_indicator = :feeIndicator, ")
            .append("   fee = :fee, transmission_date = :transmissionDate, transaction_date = :transactionDate, ")
            .append("   value_date = :valueDate, conversion_rate = :conversionRate, stan = :stan, ")
            .append("   status = :status, merchant_type = :merchantType, terminal_id = :terminalId, ")
            .append("   reference_number = :referenceNumber, approval_number = :approvalNumber, ")
            .append("   response_code = :responseCode, currency_code = :currencyCode, ")
            .append("   customer_reference = :customerReference, from_account_number = :fromAccountNumber, ")
            .append("   to_account_number = :toAccountNumber, from_account_type = :fromAccountType, ")
            .append("   to_account_type = :toAccountType, balance = :balance, description = :description, ")
            .append("   to_bank_code = :toBankCode, execution_type = :executionType, free_data1 = :freeData1, ")
            .append("   free_data2 = :freeData2, translation_code = :translationCode, free_data3 = :freeData3, ")
            .append("   free_data4 = :freeData4, free_data5 = :freeData5, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Long nextStanValue() {
        String sql = "select nextval('t_transaction_stan_seq') ";
        return getSimpleJdbcTemplate().queryForLong(sql);
    }

    /**
     * {@inheritDoc}
     */
    public Long nextRrn() {
        String sql = "select nextval('t_transaction_rrn_seq') ";
        return getSimpleJdbcTemplate().queryForLong(sql);
    }

    /**
     * {@inheritDoc}
     */
    public Transaction get(String transactionType, String customerReference) {
        Transaction result = null;

        StringBuilder sql = new StringBuilder()
                .append("select tt.* ")
                .append("from t_transaction tt ")
                .append("where tt.transaction_type = ? ")
                .append("and tt.customer_reference = ? ");

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, transactionType,
                    customerReference);
        } catch (DataAccessException e) {
        }

        return result;
    }
    
    
//    public Transaction get(String transactionType, String customerReference) {
//        Transaction result = null;
//
//        StringBuilder sql = new StringBuilder()
//                .append("select tt.* ")
//                .append("from t_transaction tt ")
//                .append("where tt.transaction_type = ? ")
//                .append("and tt.customer_reference = ? ");
//
//        try {
//            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, transactionType,
//                    customerReference);
//        } catch (DataAccessException e) {
//        }
//
//        return result;
//    }

    /**
     * {@inheritDoc}
     */
    public List<Transaction> getAll(String accountNumber, String transactionType, String status,
                                    Date startDate, Date endDate) {

        String conditionalPredicate = "";

        if (status.equals("both")) {
            conditionalPredicate = " and tt.status in ('" + Constants.PENDING_STATUS + "', '"
                    + Constants.CANCELED_STATUS + "', '"+Constants.SUCCEED_STATUS+"') ";
        }
        else {
            conditionalPredicate = " and tt.status = '" + status + "' ";
        }

        StringBuilder sql = new StringBuilder()
                .append("select tt.* ")
                .append("from t_transaction tt ")
                .append("where tt.from_account_number = ? ")
                .append(conditionalPredicate)
                .append("and tt.transaction_type = ? ")
                .append("and tt.value_date >= ? ")
                .append("and tt.value_date < ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, accountNumber, transactionType,
                DateUtils.generateStart(startDate), DateUtils.generateEnd(endDate));
    }

    /**
     * {@inheritDoc}
     */
    public List<AccountStatementView> getAll(String accountNumber, String transactionType, Date startDate,
                                             Date endDate) {

        String conditionalPredicate;
        if (transactionType != null && !transactionType.equals("")) {
            conditionalPredicate = "and tt.transaction_type = ? ";
        }
        else {
            transactionType = null;
            conditionalPredicate = "and tt.transaction_type = coalesce(?, tt.transaction_type) ";
        }

        StringBuilder sql = new StringBuilder()
                .append("select tt.reference_number, tt.customer_reference, tt.transaction_amount, tt.status, ")
                .append("       tt.transaction_type, tt.id, tt.transaction_date, tt.description, ")
                .append("       mtt.description transaction_name ")
                .append("from t_transaction tt ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.transaction_type = tt.transaction_type ")
                .append("where tt.from_account_number = ? ")
                .append(conditionalPredicate)
                .append("and tt.status in (?, ?) ")
                .append("and tt.value_date >= ? ")
                .append("and tt.value_date < ? ")
                .append("and tt.response_code is not null ")
                .append("order by tt.transaction_date desc ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<AccountStatementView>() {
            public AccountStatementView mapRow(ResultSet rs, int i) throws SQLException {
                AccountStatementView asv = new AccountStatementView();

                int index = 0;
                asv.setReferenceNumber(rs.getString(++index));
                asv.setCustomerReference(rs.getString(++index));
                asv.setAmount(rs.getBigDecimal(++index));

                String executionStatus = rs.getString(++index);
                if (executionStatus.equals(Constants.SUCCEED_STATUS)) {
                    asv.setStatus(1);
                }
                else if (executionStatus.equals(Constants.PENDING_STATUS)) {
                    asv.setStatus(2);
                }

                asv.setTransactionType(rs.getString(++index));
                asv.setTransactionId(rs.getLong(++index));
                asv.setTransactionDate(rs.getTimestamp(++index));
                asv.setDescription(rs.getString(++index));
                asv.setTransactionName(rs.getString(++index));
                return asv;
            }
        }, accountNumber, transactionType, Constants.SUCCEED_STATUS, Constants.PENDING_STATUS,
                DateUtils.generateStart(startDate), DateUtils.generateEnd(endDate));
    }

    /**
     * {@inheritDoc}
     */
    public List<Transaction> getAll(String status, Date valueDate) {
        StringBuilder sql = new StringBuilder()
                .append("select tt.* ")
                .append("from t_transaction tt ")
                .append("where tt.status = ? ")
                .append("and tt.value_date >= ? ")
                .append("and tt.value_date < ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, status,
                DateUtils.generateStart(valueDate), DateUtils.generateEnd(valueDate));
    }
    
    @Override
    public List<Transaction> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                   List<String> orders, Object... params) {
        String sql =
        		"select t.*, tt.* "
              + " from t_transaction t "
              + " join m_transaction_type tt "
              + " on tt.transaction_type=t.transaction_type ";

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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<Transaction>() {
            public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                Transaction t = defaultMapper.mapRow(rs, index);
                index += Constants.TRANSACTION_LENGTH;
                TransactionType transactionTypeModel = transactionTypeMapper.chainRow(rs, index);
                t.setTransactionTypeModel(transactionTypeModel);
                return t;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                  "select count(*) "
                + "from t_transaction t "
                + "join m_transaction_type tt "
                + "   on tt.transaction_type=t.transaction_type ";

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

    @Override
    public List<Transaction> getAllForAccount(String accountNumber, String transactionType, Date startDate, Date endDate){

        StringBuilder sql = new StringBuilder()
                .append("select tt.* ")
                .append("from t_transaction tt ")
                .append("where tt.from_account_number = ? ")
                .append("and tt.transaction_type = ? ")
                .append("and tt.value_date >= ? ")
                .append("and tt.value_date < ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, accountNumber, transactionType,
                DateUtils.generateStart(startDate), DateUtils.generateEnd(endDate));
    }

    public Transaction getByTransType_AccountNo_CustRef_Amount(String transactionType, String accountNumber, String custReference, BigDecimal amount) {
        /*
        String[] samounts = amount.toString().split(".");
        String samount = amount.toString();
        if (samounts.length > 0) {
            String dec = samounts[1];
            dec = (dec.length() == 0) ? "00000" : (dec.length() == 1) ? dec + "0000" : (dec.length() == 2) ? dec + "000" : (dec.length() == 3) ? dec + "00" : (dec.length() == 4) ? dec + "0" : dec;
            samount = samounts[0] + "." + dec;
        } else {
            samount = samount + ".00000";
        }
        */
        StringBuilder sql = new StringBuilder()
            .append("select * ")
	        .append(" from t_transaction t ")
	        .append(" where t.transaction_type = ? and t.from_account_number = ? and t.customer_reference = ? and cast(transaction_amount as numeric) = ? " +
                    " ORDER BY t.id DESC ");
		try {
            logger.info("sql getByTransType_AccountNo_CustRef_Amount= " + sql);
            List<Transaction> tds = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, transactionType, accountNumber, custReference, amount.doubleValue());
            if (tds != null && tds.size() > 0) {
                return  tds.get(0);
            }
		} catch (EmptyResultDataAccessException e) {
            logger.info("sql e.getMessage()= " + e.getMessage());
			return null;
		}
		return null;
    }

    //bsueb tambah func utk cek status transfer atmb
    public Transaction getTransactionForTransferAMTBStatus
        (String fromAccNumber,String toAccountNumber, String toBankCode, BigDecimal transAmount)
    {
        StringBuilder sql = new StringBuilder()
                .append(" select * from t_transaction where ")
                .append(" transaction_type = ? and transaction_amount = ? ")
                .append(" and from_account_number = ? and to_account_number = ? and to_bank_code = ? ")
                .append(" order by id desc limit 1 ");

        List<Transaction> tds = getSimpleJdbcTemplate().query
                (sql.toString(), defaultMapper, Constants.ATMB.TT_POSTING, transAmount.doubleValue() , fromAccNumber, toAccountNumber,toBankCode);
        if (tds != null && tds.size() > 0) {
            return  tds.get(0);
        }
        return null;
    }

    // bsueb method utk cek transaksi denom dan provider di hari yg sama
    public Boolean checkProviderDenomAtSameDay
            (String fromAccNumber,String cellularNo, BigDecimal transAmount, Date transactionDate)
    {
        StringBuilder sql = new StringBuilder()
                .append(" select * from t_transaction where ")
                .append(" transaction_type = ? and ((transaction_amount - fee) = ? or (transaction_amount + fee) = ? )")
                .append(" and from_account_number = ? and customer_reference = ? ")
                .append(" and transaction_date >= ? and transaction_date < ?  ")
                .append(" limit 1 ");

        Calendar cal = Calendar.getInstance();
        cal.setTime(transactionDate);
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),0,0);
        Date trxDate1 = cal.getTime();
        cal.add(Calendar.DATE,1);
        Date trxDate2 = cal.getTime();
        List<Transaction> tds = getSimpleJdbcTemplate().query
                (sql.toString(), defaultMapper, Constants.VOUCHER_PURCHASE_CODE, transAmount.doubleValue(), transAmount.doubleValue(),
                        fromAccNumber, cellularNo, trxDate1, trxDate2);
        if (tds != null && tds.size() > 0)
            return  true;
        return false;
    }

	@Override
	public List<Transaction> getTransferInOnDay(String fromAccount, String toAccount, Date transferDate) {
        StringBuilder sql = new StringBuilder()
	        .append("select tt.* ")
	        .append("from t_transaction tt ")
	        .append("where tt.from_account_number = ? ")
	        .append("and tt.to_account_number = ? ")
	        .append("and tt.transaction_date = ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, fromAccount, toAccount, DateUtils.generateStart(transferDate));		
	}
	
	
	//CekStatus
		@Override
		public List<Transaction> getCustomerReference(String customerReference) {
			
			StringBuilder sql = new StringBuilder()
	        .append(" select t.* from ( select customer_reference, max(transmission_date) as tgl from t_transaction" +
	        		" group by customer_reference )data join t_transaction t " +
	        		"on data.customer_reference = t.customer_reference and data.tgl = t.transmission_date " +
	        		"where data.customer_reference = ?");

	        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerReference);		
		}
	
  
    
}
