package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CellularPrefixDao;
import com.dwidasa.engine.dao.mapper.BillerProductMapper;
import com.dwidasa.engine.dao.mapper.CellularPrefixMapper;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.model.Provider;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:38 PM
 */
@Repository("cellularPrefixDao")
public class CellularPrefixDaoImpl extends GenericDaoImpl<CellularPrefix, Long> implements CellularPrefixDao {
    private final ChainedRowMapper<BillerProduct> billerProductMapper;

    @Autowired
    public CellularPrefixDaoImpl(DataSource dataSource, CellularPrefixMapper cellularPrefixMapper,
                                 BillerProductMapper billerProductMapper) {
        super("m_cellular_prefix", dataSource);
        defaultMapper = cellularPrefixMapper;
        this.billerProductMapper = billerProductMapper;

        insertSql = new StringBuilder()
            .append("insert into m_cellular_prefix ( ")
            .append("   m_biller_product_id, prefix, status, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :billerProductId, :prefix, :status, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_cellular_prefix ")
            .append("set ")
            .append("   m_biller_product_id = :billerProductId, prefix = :prefix, status = :status, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    @Override
    public List<CellularPrefix> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                   List<String> orders, Object... params) {
        String sql =
                  "select mcp.*, mbp.* "
                + "from m_cellular_prefix mcp "
                + "join m_biller_product mbp "
                + "   on mbp.id = mcp.m_biller_product_id ";

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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<CellularPrefix>() {
            public CellularPrefix mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                CellularPrefix cp = defaultMapper.mapRow(rs, index);
                index += Constants.CELLULAR_PREFIX_LENGTH;
                BillerProduct bp = billerProductMapper.chainRow(rs, index);
                cp.setBillerProduct(bp);

                return cp;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                  "select count(*) "
                + "from m_cellular_prefix mcp "
                + "join m_biller_product mbp "
                + "   on mbp.id = mcp.m_biller_product_id ";

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
	public CellularPrefix getWithTransactionType(Long id) {
		String sqlSelect = "select cp.*, tt.transaction_type from m_cellular_prefix cp"+
			" inner join m_biller_product bp on bp.id=cp.m_biller_product_id"+
			" inner join m_biller b on b.id=bp.m_biller_id"+
			" inner join m_transaction_type tt on tt.id=b.m_transaction_type_id"+
			" where cp.id=?";
		return getSimpleJdbcTemplate().queryForObject(sqlSelect, new RowMapper<CellularPrefix>() {
            public CellularPrefix mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                CellularPrefix cp = defaultMapper.mapRow(rs, index);
                cp.setTransactionType(rs.getString("transaction_type"));	
                return cp;
            }
        }, new Object[] {id});
	}

    public List<CellularPrefix> getAllCellularPrefix() {
        String sqlSelect = "select cp.*,bp.* from m_cellular_prefix cp"+
                " inner join m_biller_product bp on bp.id=cp.m_biller_product_id"+
                " inner join m_biller b on b.id=bp.m_biller_id"+
                " inner join m_transaction_type tt on tt.id=b.m_transaction_type_id";
        return getJdbcTemplate().query(sqlSelect,
                new ResultSetExtractor<List<CellularPrefix>>()
                {
                    public List<CellularPrefix> extractData(ResultSet rs) throws SQLException, DataAccessException
                    {
                        List<CellularPrefix> result = new ArrayList<CellularPrefix>();
                        while (rs.next())
                        {
                            int index = 0;
                            CellularPrefix cellularPrefix = defaultMapper.mapRow(rs, index);
                            result.add(cellularPrefix) ;
                        }
                        return result;
                    }
                });
    }
    
    
    public List<CellularPrefix> getAllCellularPrefixBiller(Long billerId) {
        String sqlSelect = "select cp.*,bp.* from m_cellular_prefix cp"+
                " inner join m_biller_product bp on bp.id=cp.m_biller_product_id"+
                " inner join m_biller b on b.id=bp.m_biller_id"+
                " inner join m_transaction_type tt on tt.id=b.m_transaction_type_id where m_biller_id = ?";
        return getJdbcTemplate().query(sqlSelect,
                new ResultSetExtractor<List<CellularPrefix>>()
                {
                    public List<CellularPrefix> extractData(ResultSet rs) throws SQLException, DataAccessException
                    {
                        List<CellularPrefix> result = new ArrayList<CellularPrefix>();
                        while (rs.next())
                        {
                            int index = 0;
                            CellularPrefix cellularPrefix = defaultMapper.mapRow(rs, index);
                            result.add(cellularPrefix) ;
                        }
                        return result;
                    }
                }, new Object[] {billerId});
    }

	@Override
	public List<CellularPrefix> getByPrefix(String prefix) {
		String sqlSelect = "select cp.*,bp.* from m_cellular_prefix cp"+
                " inner join m_biller_product bp on bp.id=cp.m_biller_product_id"+
                " inner join m_biller b on b.id=bp.m_biller_id"+
                " inner join m_transaction_type tt on tt.id=b.m_transaction_type_id" +
                " WHERE cp.prefix=?";

        return getJdbcTemplate().query(sqlSelect,
                new ResultSetExtractor<List<CellularPrefix>>()
                {
                    public List<CellularPrefix> extractData(ResultSet rs) throws SQLException, DataAccessException
                    {
                        List<CellularPrefix> result = new ArrayList<CellularPrefix>();
                        while (rs.next())
                        {
                            int index = 0;
                            CellularPrefix cellularPrefix = defaultMapper.mapRow(rs, index);
                            index += Constants.CELLULAR_PREFIX_LENGTH;
                            BillerProduct bp = billerProductMapper.chainRow(rs, index);
                            cellularPrefix.setBillerProduct(bp);
                            
                            result.add(cellularPrefix) ;
                        }
                        return result;
                    }
                }, new Object[] {prefix});
	}
}

