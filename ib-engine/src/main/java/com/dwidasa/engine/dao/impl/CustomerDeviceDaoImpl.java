package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.dao.mapper.CustomerDeviceMapper;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.util.EngineUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/4/11
 * Time: 12:06 PM
 */
@Repository("customerDeviceDao")
public class CustomerDeviceDaoImpl extends GenericDaoImpl<CustomerDevice, Long> implements CustomerDeviceDao {
    @Autowired
    public CustomerDeviceDaoImpl(DataSource dataSource, CustomerDeviceMapper customerDeviceMapper) {
        super("m_customer_device", dataSource);
        defaultMapper = customerDeviceMapper;

        insertSql = new StringBuilder()
            .append("insert into m_customer_device ( ")
            .append("   m_customer_id, device_id, terminal_id, expired_date, ime, activate_pin, status, ")
            .append("   created, createdby, updated, updatedby, soft_token ")
            .append(") ")
            .append("values ( ")
            .append("   :customerId, :deviceId, :terminalId, :expiredDate, :ime, :encryptedActivatePin, :status, ")
            .append("   :created, :createdby, :updated, :updatedby, :softToken ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_customer_device ")
            .append("set ")
            .append("   m_customer_id = :customerId, device_id = :deviceId, terminal_id = :terminalId, ")
            .append("   expired_date = :expiredDate, ime = :ime, activate_pin = :encryptedActivatePin, status = :status, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby, soft_token = :softToken ")
            .append("where id = :id ");
    }

    
    /**
     * {@inheritDoc}
     */
    public CustomerDevice getSoftToken(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.status = 1 and soft_token = 'TRUE'");

        CustomerDevice result = null;
        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public CustomerDevice get(Long customerId, String deviceId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.device_id = ? ")
                .append("and mcd.status = 1");

        CustomerDevice result = null;
        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId, deviceId);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    public CustomerDevice getDeviceSoftToken(Long customerId) { //
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.status = 1 and mcd.soft_token = 'TRUE'");

        CustomerDevice result = null;
        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId);
        } catch (EmptyResultDataAccessException e) {
            //e.printStackTrace();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public CustomerDevice get(String deviceId){
         StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.device_id = ?");

        CustomerDevice result = null;
        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, deviceId);
        } catch (EmptyResultDataAccessException e) {
            // simply do nothing
        }

        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    public CustomerDevice getDevice(String deviceId){
         StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.device_id = ? and soft_token = 'TRUE'");

        CustomerDevice result = null;
        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, deviceId);
        } catch (EmptyResultDataAccessException e) {
            // simply do nothing
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerDevice> getAll(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.status = 1 ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId);
    }
    
    
    public CustomerDevice getByDeviceSoftToken(Long customerId, String deviceId) {
    	String sqlSelect = "select mcd.* from m_customer_device mcd where mcd.m_customer_id = ? and mcd.device_id = ? and mcd.status = 1 ";
        return getJdbcTemplate().queryForObject(sqlSelect, new RowMapper<CustomerDevice>() {
        public CustomerDevice mapRow(ResultSet rs, int rowNum) throws SQLException {
          CustomerDevice customerDevice = new CustomerDevice();
          //index += Constant.BANK_CREDIT_CARD_LENGTH;
          customerDevice.setId(rs.getLong(1));
          customerDevice.setCustomerId(rs.getLong(2));
          customerDevice.setDeviceId(rs.getString(3));
          customerDevice.setTerminalId(rs.getString(4));
          customerDevice.setIme(rs.getString(5));
          customerDevice.setEncryptedActivatePin(rs.getString(6));
          customerDevice.setExpiredDate(rs.getDate(7));
          customerDevice.setStatus(rs.getInt(8));
          customerDevice.setCreated(rs.getTimestamp(9));
          customerDevice.setCreatedby(rs.getLong(10));
          customerDevice.setUpdated(rs.getTimestamp(11));
          customerDevice.setUpdatedby(rs.getLong(12));
          customerDevice.setSoftToken(rs.getBoolean(13));
          return customerDevice;
      }}, new Object[] {customerId, deviceId});

    }
    
    public List<CustomerDevice> getAllNotActiveYet(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.status = 0 ");
        System.out.println("qSelect not active : " + sql.toString());
        System.out.println("customer id = " + customerId);
        List<CustomerDevice> custDevices = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId);
        return custDevices;
    }
    
	@Override
    public List<CustomerDevice> getAllActive() {
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.status = 1 ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
    }
	
	
	@Override
    public List<CustomerDevice> getAllActiveCustomerId() {
        StringBuilder sql = new StringBuilder()
                .append("select * from m_customer ")
                .append("where id in ")
                .append("(select m_customer_id from m_customer_device where status = 1)");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
    }
	
	
    
//    public List<CustomerDevice> getAllById(Long id){
//    	 StringBuilder sql = new StringBuilder()
//         .append("select mcd.* ")
//         .append("from m_customer_device mcd ")
//         .append("where mcd.id = ? ");
//
//    	 return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, id);
//    }
    /**
     * {@inheritDoc}
     */
    public void removeAll(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("delete from m_customer_device ")
                .append("where m_customer_id = ? ");

        getSimpleJdbcTemplate().update(sql.toString(), customerId);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isActive(Long customerId, String tokenId) {
        StringBuilder sql = new StringBuilder()
                .append("select 1 ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.device_id = ? ")
                .append("and mcd.status = 1 ");

        Boolean result = true;
        try {
            getSimpleJdbcTemplate().queryForInt(sql.toString(), customerId, tokenId);
        } catch (EmptyResultDataAccessException e) {
            result = false;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void deactivateAll(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("update m_customer_device ")
                .append("set status = ? ")
                .append("where m_customer_id = ? ");

        getSimpleJdbcTemplate().update(sql.toString(), Constants.INACTIVE_STATUS, customerId);
    }
    
    /**
     * {@inheritDoc}
     */
    public void deactivateSoftTokenAll(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("update m_customer_device ")
                .append("set soft_token = ? ")
                .append("where m_customer_id = ? ");

        getSimpleJdbcTemplate().update(sql.toString(), Boolean.FALSE, customerId);
    }
    
    /**
     * {@inheritDoc}
     */
    public void activateSoftToken(Long customerId, String deviceId) {
        StringBuilder sql = new StringBuilder()
                .append("update m_customer_device ")
                .append("set soft_token = ? ")
                .append("where m_customer_id = ? and device_id = ?");

        getSimpleJdbcTemplate().update(sql.toString(), Boolean.TRUE, customerId, deviceId);
    }

    /**
     * {@inheritDoc}
     */
    public CustomerDevice validateActivationCode(Long customerId, String activationCode, Integer status) {
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.activate_pin = ? ")
                .append("and mcd.status = ? ");

        CustomerDevice customerDevice = null;
        try {
            customerDevice = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper,
                    customerId, EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, activationCode), status);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            //e.printStackTrace();
        }

        return customerDevice;
    }

    /**
     * {@inheritDoc}
     */
    public void removeInactiveDevices(Long customerId){
        StringBuilder sql = new StringBuilder()
                .append("delete from m_customer_device ")
                .append("where m_customer_id = ? and status = 0");

        getSimpleJdbcTemplate().update(sql.toString(), customerId);
    }

    /**
     * @deprecated
     * {@inheritDoc}
     */
    public boolean isTerminalIdAlreadyTaken(String terminalId){
        StringBuilder sql = new StringBuilder()
                        .append("select count(*) ")
                        .append("from m_customer_device mcd ")
                        .append("where mcd.terminal_id = ? ");
        try {
            int result = getSimpleJdbcTemplate().queryForInt(sql.toString(),terminalId);
            if (result == 0) return false;

        } catch (EmptyResultDataAccessException e) {
            //e.printStackTrace();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValidActivationCode(Long customerId, String activationCode, Integer status){
        StringBuilder sql = new StringBuilder()
                .append("select count(*) ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.activate_pin = ? ")
                .append("and mcd.status = ? ");

        int count = 0;
        try {
            count = getSimpleJdbcTemplate().queryForInt(sql.toString(),
                    customerId, EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, activationCode), status);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            //e.printStackTrace();
        }
        //System.out.println("customerId = " + customerId + " activationCode = " + EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, activationCode) + " status = " + status);
        return (count > 0);
    }

	@Override
	public Boolean isIbFirstLogin(Long customerId) {
		/*
        StringBuilder sql = new StringBuilder()
        .append("select count(*) ")
        .append("from m_customer_device mcd inner join m_customer c on c.id = mcd.m_customer_id ")
        .append("where mcd.m_customer_id = ? ")
        //.append("and mcd.terminal_id = ? ")	//buat registrasi MB saja        
        .append("and mcd.status = ? ")
        .append("and c.first_login = ? ");
		*/
        StringBuilder sql = new StringBuilder()
                .append("select count(*) ")
                .append("from m_customer  ")
                .append("where id = ? and first_login = 'Y' ");

		
		int count = 0;
		try {
		    //count = getSimpleJdbcTemplate().queryForInt(sql.toString(), customerId, Constants.IBS_SERVER, Constants.STATUS.ZERO, Constants.STATUS.YES);
			//count = getSimpleJdbcTemplate().queryForInt(sql.toString(), customerId, Constants.STATUS.ZERO, Constants.STATUS.YES);
            count = getSimpleJdbcTemplate().queryForInt(sql.toString(), customerId);
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing
		    e.printStackTrace();
		}
//		count = 1;
//		System.out.println("Sql : " +sql + " jumlah row : " +count);
		return (count > 0);
//		if(count == 0){
//			return true;
//		}else{
//			return false;
//		}
	}
	
	@Override
	public Boolean isEulaAgreed (Long customerId) {
		StringBuilder sql = new StringBuilder()
        .append("select count(*) ")
        .append("from m_customer_device mcd inner join m_customer c on c.id = mcd.m_customer_id ")
        .append("where mcd.m_customer_id = ? ")   
        .append("and mcd.status = ? ")
        .append("and c.first_login = ? ");
		
		
		int count = 0;
		try {
		    //count = getSimpleJdbcTemplate().queryForInt(sql.toString(), customerId, Constants.IBS_SERVER, Constants.STATUS.ZERO, Constants.STATUS.YES);
			count = getSimpleJdbcTemplate().queryForInt(sql.toString(), customerId, Constants.STATUS.ZERO, Constants.STATUS.YES);
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing
		    //e.printStackTrace();
		}
//		count = 1;
//		System.out.println("Sql : " +sql + " jumlah row : " +count);
		return (count > 0);
//		if(count == 0){
//			return true;
//		}else{
//			return false;
//		}
	}

    public CustomerDevice getHardTokenDevice(Long customerId) {

        CustomerDevice cd = null;

        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd  ")
                .append("where mcd.device_id in ")
                .append("   (select serial_number from m_ib_token mit ")
                .append("   where mit.m_customer_id = ? )");

		try {
		    cd = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId);
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing

		}

        return cd;
    }

    public int setPushId(Long customerId, Long customerDeviceId, String pushId) {
        StringBuilder sql = new StringBuilder()
                .append("update m_customer_device  ")
                .append("set push_id = ? ")
                .append("where m_customer_id = ? and id = ? ");

        return getSimpleJdbcTemplate().update(sql.toString(), pushId, customerId, customerDeviceId);
    }

    public List<CustomerDevice> getAllActiveByInboxId(Long inboxId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.status = 1 AND mcd.m_customer_id IN ")
                .append("(select ic.m_customer_id from t_inbox_customer ic where ic.t_inbox_id =  ?)");
        List<CustomerDevice> custDevices = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, inboxId);
        return custDevices;
    }


    @Override
    public List<CustomerDevice> getAllActive(Long customerId) {//
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from m_customer_device mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.status = 1 and soft_token = TRUE");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId);
    }
    
}
