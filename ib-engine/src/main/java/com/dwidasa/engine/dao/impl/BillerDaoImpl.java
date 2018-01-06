package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.dao.mapper.BillerMapper;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.TransactionType;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:00 PM
 */
@Repository("billerDao")
public class BillerDaoImpl extends GenericDaoImpl<Biller, Long> implements BillerDao {
    private final ChainedRowMapper<TransactionType> transactionTypeMapper;
    
    @Autowired
    private ParameterDao parameterDao;

    @Autowired
    public BillerDaoImpl(DataSource dataSource, BillerMapper billerMapper,
                         TransactionTypeMapper transactionTypeMapper) {
        super("m_biller", dataSource);
        defaultMapper = billerMapper;
        this.transactionTypeMapper = transactionTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into m_biller ( ")
            .append("   m_transaction_type_id, biller_code, biller_name, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :transactionTypeId, :billerCode, :billerName, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_biller ")
            .append("set ")
            .append("   m_transaction_type_id = :transactionTypeId, biller_code = :billerCode, ")
            .append("   biller_name = :billerName, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Biller get(String code) {
        StringBuilder sql = new StringBuilder()
                .append("select mb.* ")
                .append("from m_biller mb ")
                .append("where mb.biller_code = ? and (mb.is_active <> TRUE or mb.is_active is null)");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public Biller getBillerName(String code) {
        StringBuilder sql = new StringBuilder()
                .append("select mb.* ")
                .append("from m_biller mb ")
                .append("where mb.biller_name = ? and (mb.is_active <> TRUE or mb.is_active is null)");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }
    
    /**
     * {@inheritDoc}
     */
    public Biller getTransactionType(long code) {
        StringBuilder sql = new StringBuilder()
                .append("select mb.* ")
                .append("from m_biller mb ")
                .append("where mb.id = ? and (mb.is_active <> TRUE or mb.is_active is null)");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }

   
    /**
     * {@inheritDoc}
     */
    public List<Biller> getAllWithTransactionType() {
        StringBuilder sql = new StringBuilder()
                .append("select mb.*, mtt.* ")
                .append("from m_biller mb ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id where mb.is_active <> TRUE or mb.is_active is null");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Biller>() {
            public Biller mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                Biller biller = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                biller.setTransactionType(transactionType);
                return biller;
            }
        });
    }
    
    
    /**
     * {@inheritDoc}
     */
    public List<Biller> getAllWithTransactionType(String transactionType) {
        StringBuilder sql = new StringBuilder()
                .append("select mb.*, mtt.* ")
                .append("from m_biller mb ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id where mtt.transaction_type = ? and (mb.is_active <> TRUE or mb.is_active is null)");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Biller>() {
            public Biller mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                Biller biller = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                biller.setTransactionType(transactionType);
                return biller;
            }
        }, transactionType);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Biller> getAllWithTransactionTypeTransfer(String transactionAtmb) {
        StringBuilder sql = new StringBuilder()
                .append("select mb.*, mtt.* ")
                .append("from m_biller mb ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id where transaction_type in (?) and (mb.is_active <> TRUE or mb.is_active is null)");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Biller>() {
            public Biller mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                Biller biller = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                biller.setTransactionType(transactionType);
                return biller;
            }
        }, transactionAtmb);
    }

    
    public List<Biller> getAllTransactionTypeTransfer(long idBiller, long transactionTypeId, String transactionType) {
        StringBuilder sql = new StringBuilder()
                .append("select mb.*, mtt.* ")
                .append("from m_biller mb ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id where mb.id = ? and mb.m_transaction_type_id = ? and mtt.transaction_type = ? and (mb.is_active <> TRUE or mb.is_active is null)");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Biller>() {
            public Biller mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                Biller biller = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                biller.setTransactionType(transactionType);
                return biller;
            }
        }, idBiller, transactionTypeId, transactionType);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public List<Biller> getAllWithTransactionTypeIsMerchant() {
        StringBuilder sql = new StringBuilder()
                .append("select mb.*, mtt.* ")
                .append("from m_biller mb ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id where biller_code <> 'ISB' and (mb.is_active <> TRUE or mb.is_active is null)");
                //.append("   on mtt.id = mb.m_transaction_type_id");
                
        List<Biller> result = getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Biller>() {
            public Biller mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                Biller biller = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                biller.setTransactionType(transactionType);
                return biller;
            }
        });
        
        Parameter param = parameterDao.get("DISABLE_FOR_MERCHANT");
        if (param != null && param.getParameterValue() != null && param.getParameterValue().trim().length() > 0) {
        	String disabledBillerCode = param.getParameterValue().trim();
        	Iterator<Biller> it = result.iterator();
        	while (it.hasNext()) {
        		Biller biller = it.next();
        		if (disabledBillerCode.indexOf(biller.getBillerCode()) > -1) {
        			it.remove();
        		}
        	}
	        
        }
        return result;
    }
    
    
    public List<Biller> getAllWithTransactionTypeIsMerchant(String transactionType) {
        StringBuilder sql = new StringBuilder()
                .append("select mb.*, mtt.* ")
                .append("from m_biller mb ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id where mtt.transaction_type = ? and (mb.is_active <> TRUE or mb.is_active is null)");
                //.append("   on mtt.id = mb.m_transaction_type_id");
                
        List<Biller> result = getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Biller>() {
            public Biller mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                Biller biller = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                biller.setTransactionType(transactionType);
                return biller;
            }
        }, transactionType);
        
        Parameter param = parameterDao.get("DISABLE_FOR_MERCHANT");
        if (param != null && param.getParameterValue() != null && param.getParameterValue().trim().length() > 0) {
        	String disabledBillerCode = param.getParameterValue().trim();
        	Iterator<Biller> it = result.iterator();
        	while (it.hasNext()) {
        		Biller biller = it.next();
        		if (disabledBillerCode.indexOf(biller.getBillerCode()) > -1) {
        			it.remove();
        		}
        	}
	        
        }
        return result;
    }
    
    
public void updateStatusBiller(Long id, boolean status, Date createdBy, Long created, Date UpdateBy, Long updated){
    	
	//created = :created, createdby = :createdby, ")
    //.append("   updated = :updated, updatedby = :updatedby
    	StringBuilder sql = new StringBuilder()
        .append("update m_biller ")
        .append("set is_active = ?, created = ?, createdby = ?,  updated = ?, updatedby = ? where id = ?");

        getSimpleJdbcTemplate().update(sql.toString(), status, createdBy, created, UpdateBy, updated, id);
    	
    }


/**
 * {@inheritDoc}
 */
public List<Biller> getAll() {
    StringBuilder sql = new StringBuilder()
            .append("select mb.* ")
            .append("from m_biller mb ")
            .append("where mb.is_active <> TRUE or mb.is_active is null");

    return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
}


/**
 * {@inheritDoc}
 */
public List<Biller> getAllNoActive() {
    StringBuilder sql = new StringBuilder()
            .append("select mb.* ")
            .append("from m_biller mb ")
            .append("where mb.is_active = TRUE");

    return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
}
    
    /**
     * {@inheritDoc}
     */
    public List<Biller> getAll(Long transactionTypeId) {
        StringBuilder sql = new StringBuilder()
                .append("select mb.* ")
                .append("from m_biller mb ")
                .append("where mb.m_transaction_type_id = ? and (mb.is_active <> TRUE or mb.is_active is null)");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, transactionTypeId);
    }

    @Override
    public List<Biller> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions, List<String> orders, Object... params) {
        String sql =
                  "select mb.*, mtt.* "
                + "from m_biller mb "
                + "join m_transaction_type mtt "
                + "   on mtt.id = mb.m_transaction_type_id ";

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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<Biller>() {
            public Biller mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                Biller mb = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType mtt = transactionTypeMapper.chainRow(rs, index);
                mb.setTransactionType(mtt);

                return mb;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                  "select count(*) "
                + "from m_biller mb "
                + "join m_transaction_type mtt "
                + "   on mtt.id = mb.m_transaction_type_id ";

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
	public List<Biller> getAllData1WithTransactionTypeTransfer(
			String transactionAlto, String transactionAtmb, String billerCode) {
		 StringBuilder sql = new StringBuilder()
         .append("select mb.*, mtt.* ")
         .append("from m_biller mb ")
         .append("join m_transaction_type mtt ")
         .append("   on mtt.id = mb.m_transaction_type_id where transaction_type in (?, ?) and biller_code = ? and (mb.is_active <> TRUE or mb.is_active is null)");

 return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Biller>() {
     public Biller mapRow(ResultSet rs, int rowNum) throws SQLException {
         int index = 0;
         Biller biller = defaultMapper.mapRow(rs, index);
         index += Constants.BILLER_LENGTH;
         TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
         biller.setTransactionType(transactionType);
         return biller;
     }
 }, transactionAlto, transactionAtmb, billerCode);
	}

}
