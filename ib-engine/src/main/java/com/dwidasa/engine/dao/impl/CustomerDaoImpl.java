package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.mapper.AccountTypeMapper;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.CustomerAccountMapper;
import com.dwidasa.engine.dao.mapper.CustomerMapper;
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/20/11
 * Time: 2:46 PM
 */
@Repository("customerDao")
public class CustomerDaoImpl extends GenericDaoImpl<Customer, Long> implements CustomerDao {
    private final ChainedRowMapper<CustomerAccount> customerAccountMapper;
    private final ChainedRowMapper<AccountType> accountTypeMapper;

    @Autowired
    public CustomerDaoImpl(DataSource dataSource, CustomerMapper customerMapper,
                           CustomerAccountMapper customerAccountMapper, AccountTypeMapper accountTypeMapper) {
        super("m_customer", dataSource);
        defaultMapper = customerMapper;
        this.customerAccountMapper = customerAccountMapper;
        this.accountTypeMapper = accountTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into m_customer ( ")
            .append("   customer_name, customer_username, customer_pin, customer_phone, customer_email, ")
            .append("   cif_number, failed_auth_attempts, first_login, token_activated, ")
            .append("   last_login, last_token_id, mobile_web_token_id, status, created, createdby, ")
            .append("   updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :customerName, :customerUsername, :encryptedCustomerPin, :customerPhone, :customerEmail, ")
            .append("   :cifNumber, :failedAuthAttempts, :firstLogin, :tokenActivated, ")
            .append("   :lastLogin, :lastTokenId, :mobileWebTokenId, :status, :created, :createdby, ")
            .append("   :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_customer ")
            .append("set ")
            .append("   customer_name = :customerName, customer_username = :customerUsername, ")
            .append("   customer_pin = :encryptedCustomerPin, customer_phone = :customerPhone, ")
            .append("   customer_email = :customerEmail, cif_number = :cifNumber, ")
            .append("   failed_auth_attempts = :failedAuthAttempts, first_login = :firstLogin, ")
            .append("   token_activated = :tokenActivated, last_login = :lastLogin, last_token_id = :lastTokenId, ")
            .append("   mobile_web_token_id = :mobileWebTokenId, status = :status, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");

        defaultMapper = new CustomerMapper();
    }

    /**
     * {@inheritDoc}
     */
    public Customer getWithDefaultAccount(Long id) {
        StringBuilder sql = new StringBuilder()
                .append("select mc.*, mca.*, mat.* ")
                .append("from m_customer mc ")
                .append("join m_customer_account mca ")
                .append("   on mca.m_customer_id = mc.id ")
                .append("join m_account_type mat ")
                .append("    on mat.id = mca.m_account_type_id ")
                .append("where mc.id = ? ")
                .append("and mca.is_default = 'Y' ");

        Customer customer = null;
        try {
            customer = getSimpleJdbcTemplate().queryForObject(sql.toString(), new RowMapper<Customer>() {
                public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    Customer customer = defaultMapper.mapRow(rs, index);
                    index += Constants.CUSTOMER_LENGTH;
                    CustomerAccount customerAccount = customerAccountMapper.chainRow(rs, index);
                    index += Constants.CUSTOMER_ACCOUNT_LENGTH;
                    AccountType accountType = accountTypeMapper.chainRow(rs, index);

                    customerAccount.setAccountType(accountType);
                    Set<CustomerAccount> customerAccounts = new HashSet<CustomerAccount>();
                    customerAccounts.add(customerAccount);
                    customer.setCustomerAccounts(customerAccounts);
                    return customer;
                }
            }, id);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return customer;
    }

    /**
     * {@inheritDoc}
     */
    public Customer getWithDefaultAccount(String username) {
        StringBuilder sql = new StringBuilder()
                .append("select mc.*, mca.*, mat.* ")
                .append("from m_customer mc ")
                .append("join m_customer_account mca ")
                .append("   on mca.m_customer_id = mc.id ")
                .append("join m_account_type mat ")
                .append("    on mat.id = mca.m_account_type_id ")
                .append("where mc.customer_username = ? ")
                .append("and mca.is_default = 'Y' ");

        Customer customer = null;
        try {
            customer = getSimpleJdbcTemplate().queryForObject(sql.toString(), new RowMapper<Customer>() {
                public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    Customer customer = defaultMapper.mapRow(rs, index);
                    index += Constants.CUSTOMER_LENGTH;
                    CustomerAccount customerAccount = customerAccountMapper.chainRow(rs, index);
                    index += Constants.CUSTOMER_ACCOUNT_LENGTH;
                    AccountType accountType = accountTypeMapper.chainRow(rs, index);

                    customerAccount.setAccountType(accountType);
                    Set<CustomerAccount> customerAccounts = new HashSet<CustomerAccount>();
                    customerAccounts.add(customerAccount);
                    customer.setCustomerAccounts(customerAccounts);
                    return customer;
                }
            }, username);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public Customer getByUsernameCardNo(String username, String cardNo) {

        StringBuilder sql = new StringBuilder()
                .append("select mc.*, mca.*, mat.* ")
                .append("from m_customer mc ")
                .append("join m_customer_account mca ")
                .append("   on mca.m_customer_id = mc.id ")
                .append("join m_account_type mat ")
                .append("    on mat.id = mca.m_account_type_id ")
                .append("where mc.customer_username = ? ")
                .append("and mca.card_number = ? ")
                .append("and mca.is_default = 'Y' ");
        Customer customer = null;
        try {
            customer = getSimpleJdbcTemplate().queryForObject(sql.toString(), new RowMapper<Customer>() {
                public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    Customer customer = defaultMapper.mapRow(rs, index);
                    index += Constants.CUSTOMER_LENGTH;
                    CustomerAccount customerAccount = customerAccountMapper.chainRow(rs, index);
                    index += Constants.CUSTOMER_ACCOUNT_LENGTH;
                    AccountType accountType = accountTypeMapper.chainRow(rs, index);

                    customerAccount.setAccountType(accountType);
                    Set<CustomerAccount> customerAccounts = new HashSet<CustomerAccount>();
                    customerAccounts.add(customerAccount);
                    customer.setCustomerAccounts(customerAccounts);
                    return customer;
                }
            }, username, cardNo);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return customer;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isActive(Long id) {
        StringBuilder sql = new StringBuilder()
                .append("select status ")
                .append("from m_customer mc ")
                .append("where mc.id = ? ")
                .append("and mc.status = 1 ");

        Integer status = 0;

        try {
            status = getSimpleJdbcTemplate().queryForInt(sql.toString(), id);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return status == 1;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean authenticate(String username, String pin) {
        StringBuilder sql = new StringBuilder()
                .append("select 1 ")
                .append("from m_customer mc ")
                .append("where mc.customer_username = ? ")
                .append("and mc.customer_pin = ? ");

        Integer result = 0;

        try {
            result = getSimpleJdbcTemplate().queryForInt(sql.toString(), username, EngineUtils.encrypt(Constants.SERVER_SECRET_KEY,pin));
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return result == 1;
    }

	@Override
	public Boolean authenticateSHA(String username, String pin) {
        StringBuilder sql = new StringBuilder()
        .append("select 1 ")
        .append("from m_customer mc ")
        .append("where mc.customer_username = ? ")
        .append("and mc.customer_pin = ? ");

		Integer result = 0;
		
		try {
		    result = getSimpleJdbcTemplate().queryForInt(sql.toString(), username, EngineUtils.encryptSHA(username, pin));
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing
		    e.printStackTrace();
		}
		
		return result == 1;
	}

    /**
     * {@inheritDoc}
     */
    public Customer get(String username) {
        StringBuilder sql = new StringBuilder()
                .append("select mc.* ")
                .append("from m_customer mc ")
                .append("where mc.customer_username = ? ");
        logger.info("sql=" + sql.toString() + " username=" + username);
        Customer customer = null;

        try {
            customer = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, username);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
        }

        return customer;
    }
    
    public Customer getByTID(String TerminalId) {
        StringBuilder sql = new StringBuilder()
                .append("select mc.* ")
                .append("from m_customer mc inner join m_ib_merchant mim on mc.id = mim.m_customer_id ")
                .append("where mim.terminal_id = ? ");
        logger.info("sql=" + sql.toString() + " TerminalId=" + TerminalId);
        Customer customer = null;

        try {
            customer = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, TerminalId);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
        }

        return customer;
    }
    
    public Customer getByUsernameAndByTID(String Username, String TerminalId) {
        StringBuilder sql = new StringBuilder()
                .append("select mc.* ")
                .append("from m_customer mc inner join m_ib_merchant mim on mc.id = mim.m_customer_id ")
                .append("where mc.customer_username = ? and mim.terminal_id = ? ");
        logger.info("sql=" + sql.toString() + " TerminalId=" + TerminalId);
        Customer customer = null;

        try {
            customer = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, Username, TerminalId);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
        }

        return customer;
    }

    @Override
    public Customer getByCifCardNo(String cif, String cardNo) {
        StringBuilder sql = new StringBuilder()
                .append("select mc.* ")
                .append("from m_customer mc ")
                .append("where mc.cif_number = ? ")
                .append("and mc.id IN ")
                .append("(select mca.m_customer_id FROM m_customer_account mca WHERE mca.card_number = ? ) ");
        Customer customer = null;

        try {
            //customer = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, cif, cardNo);
        	List<Customer> custs = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, cif, cardNo);
        	if (custs.size() > 0) {
        		customer = custs.get(0);	
        	}
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
        }

        return customer;
    }

    /**
     *
     * {@inheritDoc}
     */
    public int checkCustomerStatus(Long id) {
         StringBuilder sql = new StringBuilder()
                .append("select status ")
                .append("from m_customer mc ")
                .append("where mc.id = ? ");

        Integer status;

        try {
            status = getSimpleJdbcTemplate().queryForInt(sql.toString(), id);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }

        return status;
    }

//    @Override
//    public Customer getByCustomerNumber(Long customerId) {
//        StringBuilder sql = new StringBuilder()
//                .append("select mc.* ")
//                .append("from m_customer mc ")
//                .append("where mc.id = ? ");
//        Customer customer = null;
//
//        try {
//            customer = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId);
//        } catch (EmptyResultDataAccessException e) {
//            //-- actually do nothing
//        }
//
//        return customer;
//    }
    
	@Override
	public void updateStatus(Long id, int status) {
		String sqlUpdate = "update m_customer set status=? where id=? ";
		if (status == 1) {
			sqlUpdate = "update m_customer set status=?, failed_auth_attempts=? where id=?";
			getJdbcTemplate().update(sqlUpdate, new Object[] {status, 0, id});
		} else {
			getJdbcTemplate().update(sqlUpdate, new Object[] {status, id});
		}		
	}
	
	@Override
	public void updateStatusUnblockOrblock(Long id, String tin, int status) {
		int updateBy = Integer.parseInt(tin);
		String sqlUpdate = "update m_customer set status=?, updatedby= ? where id=? ";
		if (status == 1) {
			sqlUpdate = "update m_customer set status=?, failed_auth_attempts=?, updatedby= ? where id=?";
			getJdbcTemplate().update(sqlUpdate, new Object[] {status, 0, updateBy, id});
		} else {
			getJdbcTemplate().update(sqlUpdate, new Object[] {status, updateBy, id});
		}		
	}
	
	@Override
	public void updateStatusUnblockOrblockCustomerDevice(Long id, String tin, int status) {
		int updateBy = Integer.parseInt(tin);
		String sqlUpdate = "update m_customer_device set status=?, updatedby= ? where m_customer_id=? ";
		if (status == 1) {
			sqlUpdate = "update m_customer_device set status=?, updatedby= ? where m_customer_id=?";
			getJdbcTemplate().update(sqlUpdate, new Object[] {status, updateBy, id});
		} else {
			getJdbcTemplate().update(sqlUpdate, new Object[] {status, updateBy, id});
		}		
	}
	
	@Override
	public void updateStatusUnblockOrblockIbMerchant(Long id, String tin, int status) {
		int updateBy = Integer.parseInt(tin);
		String sqlUpdate = "update m_ib_merchant set status=?, updatedby= ? where m_customer_id=? ";
		if (status == 1) {
			sqlUpdate = "update m_ib_merchant set status=?, updatedby= ? where m_customer_id=?";
			getJdbcTemplate().update(sqlUpdate, new Object[] {Constants.HARD_TOKEN.STATUS_ACTIVE, updateBy, id});
		} else {
			getJdbcTemplate().update(sqlUpdate, new Object[] {Constants.HARD_TOKEN.STATUS_LINKED, updateBy, id});
		}		
	}
	
	@Override
	public void updateStatusUnblockOrblockIbToken(Long id, String tin, int status) {
		int updateBy = Integer.parseInt(tin);
		String sqlUpdate = "update m_ib_token set status=?, updatedby= ? where m_customer_id=? ";
		if (status == 1) {
			sqlUpdate = "update m_ib_token set status=?, updatedby= ? where m_customer_id=?";
			getJdbcTemplate().update(sqlUpdate, new Object[] {Constants.HARD_TOKEN.STATUS_ACTIVE, updateBy, id});
		} else {
			getJdbcTemplate().update(sqlUpdate, new Object[] {Constants.HARD_TOKEN.STATUS_LINKED, updateBy, id});
		}		
	}

    @Override
    public void insertCustomerRegisterSilent(Customer customer) {
        StringBuilder insertSql = new StringBuilder()
            .append("insert into m_customer ( ")
            .append("   customer_name, customer_username, customer_pin, customer_phone, cif_number, failed_auth_attempts, ")
            .append("   first_login, token_activated, mobile_web_token_id, status, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :customerName, :customerUsername, :encryptedCustomerPin, :customerPhone, :cifNumber, :failedAuthAttempts, ")
            .append("   :firstLogin, :tokenActivated, :mobileWebTokenId, :status, :created, :createdby, :updated, :updatedby ")
            .append(") ");
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(customer);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        new NamedParameterJdbcTemplate(getDataSource()).update(insertSql.toString(), parameters, keyHolder);
    }

    public Customer save(Customer customer) {
    	SqlParameterSource parameters = new BeanPropertySqlParameterSource(customer);
        if (customer.getId() == null) {
            //-- insert
        	KeyHolder keyHolder = new GeneratedKeyHolder();     

            new NamedParameterJdbcTemplate(getDataSource()).update(insertSql.toString(), parameters, keyHolder);
//            object.setId(keyHolder.getKey().longValue());
            Long id = (Long) keyHolder.getKeyList().get(0).get("id");
            logger.info("id=" + id);
            customer.setId(id);
        }
        else {
            //-- update
            new NamedParameterJdbcTemplate(getDataSource()).update(updateSql.toString(), parameters);
        }

        return customer;
    }

    @Override
    public List<Customer> getByDefaultAccountNumber(String accountNumber) {

        StringBuilder sql = new StringBuilder()
                .append("select mc.* ")
                .append("from m_customer mc ")
                .append("where id in ")
                .append(" (select m_customer_id from m_customer_account mca ")
                .append("    where mca.account_number = ? and mca.is_default = 'Y') ");

        try {

            return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, accountNumber);

        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<Customer>();
        }

    }

    @Override
    public int remove(Long customerId) {

        try {

            StringBuilder sql = new StringBuilder()
                .append("delete from t_transfer_batch_content ")
                .append(" where t_transfer_batch_id in ")
                .append("  (select id from t_transfer_batch where m_customer_id = ?)");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from t_transfer_batch ")
                .append("where m_customer_id = ? ");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from m_batch_content ")
                .append(" where m_batch_id in ")
                .append("  (select id from m_batch where m_customer_id = ?)");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from m_batch ")
                .append("where m_customer_id = ? ");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from m_customer_account mca ")
                .append("where mca.m_customer_id = ? ");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from m_customer_data mcd ")
                .append("where mcd.m_customer_id = ? ");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from m_customer_register mcr ")
                .append("where mcr.m_customer_id = ? ");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from m_customer_session mcs ")
                .append("where mcs.m_customer_id = ? ");
            getSimpleJdbcTemplate().update(sql.toString(), customerId);

            sql = new StringBuilder()
                .append("delete from m_customer mc ")
                .append("where mc.id = ? ");
            return getSimpleJdbcTemplate().update(sql.toString(), customerId);

        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }
    
    public Boolean isMerchant(Long customerId){
    	
    	// count(*) from m_customer_device where m_customer_id = ? and terminal_id like 'MB%'
    	
    	StringBuilder sql = new StringBuilder()
        .append("select count(*) ")
        .append("from m_customer_device mcd ")
        .append("where mcd.m_customer_id = ? ")
        .append("and mcd.terminal_id is not null ");

		Integer status = 0;
		
		try {
		    status = getSimpleJdbcTemplate().queryForInt(sql.toString(), customerId);
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing
		    e.printStackTrace();
		}
		
		if(status > 0){
			return true;
		}else{
			return false;
		}
    }

	@Override
	public Long getCustomerIdByUsername(String username) {
		String sql = "select id from m_customer where customer_username = ?  ";
		Long id = 0L;
		try {
			id = getSimpleJdbcTemplate().queryForLong(sql, username);
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing
		}
		return id;
	}
	
    @Override
    public List<Customer> getByAccountNumber(String accountNumber) {

        StringBuilder sql = new StringBuilder()
                .append("select mc.* ")
                .append("from m_customer mc ")
                .append("where id in ")
                .append(" (select m_customer_id from m_customer_account mca ")
                .append("    where mca.account_number = ?) ");

        try {

            return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, accountNumber);

        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<Customer>();
        }

    }
    
    
    @Override
    public List<Customer> getAllActiveCustomer() {

        StringBuilder sql = new StringBuilder()
                .append("select * from m_customer where id in " +
                		"(" +
                		"select m_customer_id from m_customer_device " +
                		"where status = 1" +
                		")");

        try {

            return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Customer>() {

				@Override
				public Customer mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					int index = 0;
					Customer customer = new Customer();
			        customer.setId(rs.getLong(++index));
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
            return new ArrayList<Customer>();
        }

    }
    
    
    @Override
    public List<Customer> getByCustomerIsMerchant() {

    	StringBuilder sql;
    	
    		 sql = new StringBuilder()
            .append("select * from ( select *, 'Merchant' as status " +
            		"from m_customer where id in " +
            		"(select m_customer_id from m_customer_device mcd " +
            		"where mcd.terminal_id is not null and status = 1)" +
            		")data");
    	
        try {

            return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Customer>() {

				@Override
				public Customer mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					int index = 0;
					Customer customer = new Customer();
			        customer.setId(rs.getLong(++index));
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
            return new ArrayList<Customer>();
        }

    }
    
    
    @Override
    public List<Customer> getByCustomerIsMerchantByName(String customerName, String customerUserName) {

    	StringBuilder sql;
    	
    	
    		 sql = new StringBuilder()
             .append("select * from ( select *, 'Merchant' as status " +
             		"from m_customer where id in " +
             		"(select m_customer_id from m_customer_device mcd " +
             		"where mcd.terminal_id is not null and status = 1)" +
             		")data where customer_name ilike '%' || ? || '%' or customer_username ilike '%' || ? || '%'");
    	

        try {

            return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerName, customerUserName);

        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<Customer>();
        }

    }
    
    
    @Override
    public List<Customer> getByCustomerByCustomerName(String customerName, String customerUserName) {

    	StringBuilder sql;
    	
    	
    		 sql = new StringBuilder()
             .append("select * from ( select *, 'Merchant' as status " +
             		"from m_customer )data " +
             		"where customer_name ilike '%' || ? || '%' or customer_username ilike '%' || ? || '%'");
    	

        try {

            return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerName, customerUserName);

        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<Customer>();
        }

    }
    
    
    @Override
    public List<Customer> getByCustomerIsIndividual() {

    	StringBuilder sql;
    	
    		
    		sql = new StringBuilder()
            .append("select id from ( select *, 'Individual' as status " +
            		"from m_customer where id not in " +
            		"(select m_customer_id from m_customer_device mcd " +
            		"where mcd.terminal_id is not null and status = 1)" +
            		")data");
    	

        try {

            return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Customer>() {

				@Override
				public Customer mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					int index = 0;
					Customer customer = new Customer();
			        customer.setId(rs.getLong(++index));
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
            return new ArrayList<Customer>();
        }

    }
    
    
    @Override
    public List<Customer> getByCustomerIsIndividualByName(String customerName, String customerUserName) {

    	StringBuilder sql;
    	
    	
    		sql = new StringBuilder()
            .append("select * from ( select *, 'Individual' as status " +
            		"from m_customer where id not in " +
            		"(select m_customer_id from m_customer_device mcd " +
            		"where mcd.terminal_id is not null and status = 1)" +
            		")data where customer_name ilike '%' || ? || '%' or customer_username ilike '%' || ? || '%'");
    
        try {

            return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerName, customerUserName);

        }
        catch (DataAccessException dae){
            dae.printStackTrace();
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<Customer>();
        }

    }
	
}
