package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.mapper.*;
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/20/11
 * Time: 3:44 PM
 */
@Repository("customerAccountDao")
public class CustomerAccountDaoImpl extends GenericDaoImpl<CustomerAccount, Long> implements CustomerAccountDao {
    private final ChainedRowMapper<AccountType> accountTypeMapper;
    private final ChainedRowMapper<Product> productMapper;
    private final ChainedRowMapper<Currency> currencyMapper;

    @Autowired
    public CustomerAccountDaoImpl(DataSource dataSource, CustomerAccountMapper customerAccountMapper,
                                  AccountTypeMapper accountTypeMapper, ProductMapper productMapper,
                                  CurrencyMapper currencyMapper) {
        super("m_customer_account", dataSource);
        defaultMapper = customerAccountMapper;
        this.accountTypeMapper = accountTypeMapper;
        this.productMapper = productMapper;
        this.currencyMapper = currencyMapper;

        insertSql = new StringBuilder()
            .append("insert into m_customer_account ( ")
            .append("   m_account_type_id, m_currency_id, m_customer_id, m_product_id, account_number, ")
            .append("   card_number, is_default, status, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :accountTypeId, :currencyId, :customerId, :productId, :accountNumber, ")
            .append("   :cardNumber, :isDefault, :status, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_customer_account ")
            .append("set ")
            .append("   m_account_type_id = :accountTypeId, m_currency_id = :currencyId, ")
            .append("   m_customer_id = :customerId, m_product_id = :productId, ")
            .append("   account_number = :accountNumber, card_number = :cardNumber, ")
            .append("   is_default = :isDefault, status = :status, created = :created, ")
            .append("   createdby = :createdby , updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public CustomerAccount get(Long customerId, String accountNumber) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.* ")
                .append("from m_customer_account mca ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.account_number = ? order by mca.id asc ");

        CustomerAccount result = null;

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId, accountNumber);
        } catch (DataAccessException e) {
        	
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerAccount> get(String cardNumber) {
        StringBuilder sql = new StringBuilder()
//                .append("select mca.* ")
//                .append("from m_customer_account mca ")
//                .append("where mca.card_number = ?  ");
			.append("select mca.* from m_customer_account mca ")
			.append("INNER JOIN m_customer c ON c.id = mca.m_customer_id ")
			.append("where mca.card_number = ? AND c.customer_username LIKE ?  order by mca.id asc");
        logger.info("sql=" + sql);
        List<CustomerAccount> result = null;

        try {
        	String like = "%" + cardNumber.substring(cardNumber.length() - 4);
            result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, cardNumber, like);
        } catch (DataAccessException e) {
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public CustomerAccount getDefaultWithType(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.*, mat.* ")
                .append("from m_customer_account mca ")
                .append("join m_account_type mat ")
                .append("   on mat.id = mca.m_account_type_id ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.is_default = 'Y' ")
                .append("and mca.status = 1 order by mca.id asc");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), new RowMapper<CustomerAccount>() {
                    public CustomerAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
                        int index = 0;
                        CustomerAccount ca = defaultMapper.mapRow(rs, index);
                        index += Constants.CUSTOMER_ACCOUNT_LENGTH;
                        AccountType at = accountTypeMapper.chainRow(rs, index);
                        ca.setAccountType(at);

                        return ca;
                    }
                }, customerId);
    }

    /**
     * {@inheritDoc}
     */
    public CustomerAccount getWithTypeAndProduct(Long customerId, String accountNumber) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.*, mat.*, mp.* ")
                .append("from m_customer_account mca ")
                .append("join m_account_type mat ")
                .append("   on mat.id = mca.m_account_type_id ")
                .append("join m_product mp ")
                .append("   on mp.id = mca.m_product_id ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.account_number = ? ")
                .append("and mca.status = 1 order by mca.id asc");

        CustomerAccount customerAccount = null;
        try {
            customerAccount = getSimpleJdbcTemplate().queryForObject(sql.toString(), new RowMapper<CustomerAccount>() {
                public CustomerAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    CustomerAccount customerAccount = defaultMapper.mapRow(rs, index);
                    index += Constants.CUSTOMER_ACCOUNT_LENGTH;
                    AccountType accountType = accountTypeMapper.chainRow(rs, index);
                    index += Constants.ACCOUNT_TYPE_LENGTH;
                    Product product = productMapper.chainRow(rs, index);

                    customerAccount.setAccountType(accountType);
                    customerAccount.setProduct(product);
                    return customerAccount;
                }
            }, customerId, accountNumber);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return customerAccount;
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerAccount> getAll(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.* ")
                .append("from m_customer_account mca ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.status = 1 order by mca.id asc");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId);
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerAccount> getAllRegistered(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.* ")
                .append("from m_customer_account mca ")
                .append("where mca.m_customer_id = ? order by mca.id asc ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId);
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerAccount> getAll(Long customerId, String cardNumber) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.* ")
                .append("from m_customer_account mca ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.card_number = ? ")
                .append("and mca.status = 1 order by mca.id asc");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId, cardNumber);
    }
    
    
    public List<CustomerAccount> getAllNoDefault(Long customerId, String accountNumber) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.* ")
                .append("from m_customer_account mca ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.account_number <> ? ")
                .append("and mca.status = 1 and mca.is_default <> 'Y'");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId, accountNumber);
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerAccount> getAllWithTypeAndCurrency(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.*, mat.*, mc.* ")
                .append("from m_customer_account mca ")
                .append("join m_account_type mat ")
                .append("   on mat.id = mca.m_account_type_id ")
                .append("join m_currency mc ")
                .append("   on mc.id = mca.m_currency_id ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.status = 1 order by mca.id asc");

        List<CustomerAccount> customerAccount = null;
        try {
            customerAccount = getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<CustomerAccount>() {
                public CustomerAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    CustomerAccount customerAccount = defaultMapper.mapRow(rs, index);
                    index += Constants.CUSTOMER_ACCOUNT_LENGTH;
                    AccountType accountType = accountTypeMapper.chainRow(rs, index);
                    index += Constants.ACCOUNT_TYPE_LENGTH;
                    Currency c = currencyMapper.chainRow(rs, index);

                    customerAccount.setAccountType(accountType);
                    customerAccount.setCurrency(c);

                    return customerAccount;
                }
            }, customerId);
        } catch (DataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return customerAccount;
    }
    
    
    public List<CustomerAccount> getAllRekMerchant(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.*, mat.*, mc.* ")
                .append("from m_customer_account mca ")
                .append("join m_account_type mat ")
                .append("   on mat.id = mca.m_account_type_id ")
                .append("join m_currency mc ")
                .append("   on mc.id = mca.m_currency_id ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.status = 1 and mca.is_default = 'Y' order by mca.id asc");

        List<CustomerAccount> customerAccount = null;
        try {
            customerAccount = getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<CustomerAccount>() {
                public CustomerAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    CustomerAccount customerAccount = defaultMapper.mapRow(rs, index);
                    index += Constants.CUSTOMER_ACCOUNT_LENGTH;
                    AccountType accountType = accountTypeMapper.chainRow(rs, index);
                    index += Constants.ACCOUNT_TYPE_LENGTH;
                    Currency c = currencyMapper.chainRow(rs, index);

                    customerAccount.setAccountType(accountType);
                    customerAccount.setCurrency(c);

                    return customerAccount;
                }
            }, customerId);
        } catch (DataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return customerAccount;
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerAccount> getAllWithTypeAndProduct(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mca.*, mat.*, mp.* ")
                .append("from m_customer_account mca ")
                .append("join m_account_type mat ")
                .append("   on mat.id = mca.m_account_type_id ")
                .append("join m_product mp ")
                .append("   on mp.id = mca.m_product_id ")
                .append("where mca.m_customer_id = ? ")
                .append("and mca.status = 1 order by mca.id asc");

        List<CustomerAccount> customerAccount = null;
        try {
            customerAccount = getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<CustomerAccount>() {
                public CustomerAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    CustomerAccount customerAccount = defaultMapper.mapRow(rs, index);
                    index += Constants.CUSTOMER_ACCOUNT_LENGTH;
                    AccountType accountType = accountTypeMapper.chainRow(rs, index);
                    index += Constants.ACCOUNT_TYPE_LENGTH;
                    Product product = productMapper.chainRow(rs, index);

                    customerAccount.setAccountType(accountType);
                    customerAccount.setProduct(product);
                    return customerAccount;
                }
            }, customerId);
        } catch (DataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return customerAccount;
    }

    @Override
    public int deleteCustomerAccounts(Long customerId) {

        StringBuilder sql = new StringBuilder()
                .append("delete from m_customer_account mca ")
                .append("where mca.m_customer_id = ? ");

        try {

            return getSimpleJdbcTemplate().update(sql.toString(), customerId);

        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    
    
    @Override
    public int deleteCustomerAccountsPerId(Long Id) {

        StringBuilder sql = new StringBuilder()
                .append("delete from m_customer_account mca ")
                .append("where mca.id = ? ");

        try {

            return getSimpleJdbcTemplate().update(sql.toString(), Id);

        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    
    
    @Override
    public int deleteCustomerAccountsCartNumber(Long customerId, String AccountNumber) {

        StringBuilder sql = new StringBuilder()
                .append("delete from m_customer_account mca ")
                .append("where mca.m_customer_id = ? and account_number <> ? and is_default <> 'Y'");

        try {

            return getSimpleJdbcTemplate().update(sql.toString(), customerId, AccountNumber);

        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    
    
    @Override
    public List<CustomerAccount> getAllCardNumberCustomer() {

        StringBuilder sql = new StringBuilder()
                .append("select * from m_customer_account order by id asc");

        try {

            return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<CustomerAccount>() {

				@Override
				public CustomerAccount mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					int index = 0;
					CustomerAccount customer = new CustomerAccount();
			        customer.setId(rs.getLong(1));
			        customer.setCardNumber(rs.getString(7));
			        return customer;
				}
			});

        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<CustomerAccount>();
        }

    }
    
    
    @Override
	public void updateEncriptCardNumber(Long id, String cardNew) {
		String sqlUpdate = "update m_customer_account set card_number=? where id=? ";
		getJdbcTemplate().update(sqlUpdate, new Object[] {cardNew, id});
	}
    
}
