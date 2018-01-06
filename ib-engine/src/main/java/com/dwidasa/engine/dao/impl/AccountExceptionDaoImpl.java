package com.dwidasa.engine.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.AccountExceptionDao;
import com.dwidasa.engine.dao.mapper.AccountExceptionMapper;
import com.dwidasa.engine.model.AccountException;

@Repository("accountExceptionDao")
public class AccountExceptionDaoImpl extends GenericDaoImpl<AccountException, Long> implements AccountExceptionDao {

    @Autowired
    public AccountExceptionDaoImpl(DataSource dataSource, AccountExceptionMapper accountExceptionMapper) {
        super("m_account_exception", dataSource);
        defaultMapper = accountExceptionMapper;

        insertSql = new StringBuilder()
            .append("insert into m_account_exception ( ")
            .append("   account_number,")
            .append("   created, createdby, updated, updatedby")
            .append(") ")
            .append("values ( ")
            .append("   :accountNumber,")
            .append("   :created, :createdby, :updated, :updatedby")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_account_exception ")
            .append("set ")
            .append("   account_number = :accountNumber, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");

        defaultMapper = new AccountExceptionMapper();
    }
    public List<AccountException> getAccountByAccountNumber(String accountNumber){
    	StringBuilder sql = new StringBuilder()
        .append("select ae.* ")
        .append("from m_account_exception ae ")
        .append("where ae.account_number like '%' || ? || '%'");
		List<AccountException> result = null;
		try {
		    result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, accountNumber);
		} catch (DataAccessException e) {
			System.out.println("error :" +e.getMessage());
		}		
		return result;
    }
   
	public AccountException getById(Long id){
		StringBuilder sql = new StringBuilder()
        .append("select ae.* ")
        .append("from m_account_number ae ")
        .append("where ae.id = ? ");
		AccountException accountException = null;
		
		try {
		    accountException = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, id);
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing
		}
		
		return accountException;
	}
	public Boolean isAccountException(String accountNumber){
//		Boolean result
		StringBuilder sql = new StringBuilder()
        .append("select ae.* ")
        .append("from m_account_exception ae ")
        .append("where ae.account_number like '%' || ? || '%'");
		List<AccountException> result = null;
		try {
		    result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, accountNumber);
		} catch (DataAccessException e) {
			System.out.println("error :" +e.getMessage());
		}		
		System.out.println(""+sql.toString()+ accountNumber);
		System.out.println("resultSize : "+result.size()+" : "+result);
		if(result.size() > 0){
			return true;
		}
		return false;
	}
}
