package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Transaction;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/23/11
 * Time: 3:10 PM
 */
@Component("transactionMapper")
public class TransactionMapper extends ChainedRowMapper<Transaction> implements ParameterizedRowMapper<Transaction> {
    public TransactionMapper() {
    }

    public Transaction chainRow(ResultSet rs, int index) throws SQLException {
        Transaction transaction = new Transaction();

        transaction.setId(rs.getLong(++index));
        transaction.setMti(rs.getString(++index));
        transaction.setTransactionType(rs.getString(++index));
        transaction.setCardNumber(rs.getString(++index));
        transaction.setTransactionAmount(rs.getBigDecimal(++index));
        transaction.setFeeIndicator(rs.getString(++index));
        transaction.setFee(rs.getBigDecimal(++index));
        transaction.setTransmissionDate(rs.getTimestamp(++index));
        transaction.setTransactionDate(rs.getTimestamp(++index));
        transaction.setValueDate(rs.getTimestamp(++index));
        transaction.setConversionRate(rs.getBigDecimal(++index));
        transaction.setStan(rs.getBigDecimal(++index));
        transaction.setMerchantType(rs.getString(++index));
        transaction.setTerminalId(rs.getString(++index));
        transaction.setReferenceNumber(rs.getString(++index));
        transaction.setApprovalNumber(rs.getString(++index));
        transaction.setResponseCode(rs.getString(++index));
        transaction.setCurrencyCode(rs.getString(++index));
        transaction.setCustomerReference(rs.getString(++index));
        transaction.setFromAccountNumber(rs.getString(++index));
        transaction.setToAccountNumber(rs.getString(++index));
        transaction.setFromAccountType(rs.getString(++index));
        transaction.setToAccountType(rs.getString(++index));
        transaction.setBalance(rs.getString(++index));
        transaction.setDescription(rs.getString(++index));
        transaction.setToBankCode(rs.getString(++index));
        transaction.setExecutionType(rs.getString(++index));
        transaction.setStatus(rs.getString(++index));
        transaction.setTranslationCode(rs.getString(++index));
        transaction.setFreeData1(rs.getString(++index));
        transaction.setFreeData2(rs.getString(++index));
        transaction.setFreeData3(rs.getString(++index));
        transaction.setFreeData4(rs.getString(++index));
        transaction.setFreeData5(rs.getString(++index));
        transaction.setCreated(rs.getTimestamp(++index));
        transaction.setCreatedby(rs.getLong(++index));
        transaction.setUpdated(rs.getTimestamp(++index));
        transaction.setUpdatedby(rs.getLong(++index));

        return transaction;
    }
}
