package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.BillerProductDao;
import com.dwidasa.engine.dao.mapper.BillerMapper;
import com.dwidasa.engine.dao.mapper.BillerProductMapper;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:04 PM
 */
@Repository("billerProductDao")
public class BillerProductDaoImpl extends GenericDaoImpl<BillerProduct, Long> implements BillerProductDao {

	private final ChainedRowMapper<Biller> billerMapper;
    private final ChainedRowMapper<TransactionType> transactionTypeMapper;

    @Autowired
    public BillerProductDaoImpl(DataSource dataSource, BillerProductMapper billerProductMapper,
                                BillerMapper billerMapper, TransactionTypeMapper transactionTypeMapper) {
        super("m_biller_product", dataSource);
        defaultMapper = billerProductMapper;
        this.billerMapper = billerMapper;
        this.transactionTypeMapper = transactionTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into m_biller_product ( ")
            .append("   m_biller_id, product_code, product_name, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :billerId, :productCode, :productName, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_biller_product ")
            .append("set ")
            .append("   m_biller_id = :billerId, product_code = :productCode, product_name = :productName, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public BillerProduct get(String code) {
        StringBuilder sql = new StringBuilder()
                .append("select mbp.* ")
                .append("from m_biller_product mbp ")
                .append("where mbp.product_code = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<BillerProduct> getAllWithTransactionType() {
        StringBuilder sql = new StringBuilder()
                .append("select mbp.*, mb.*, mtt.* ")
                .append("from m_biller_product mbp ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<BillerProduct>() {
            public BillerProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                BillerProduct billerProduct = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller biller = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                System.out.println("Index INDEX "+index);
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                biller.setTransactionType(transactionType);
                billerProduct.setBiller(biller);
                return billerProduct;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public List<BillerProduct> getAll(String billerCode) {
        StringBuilder sql = new StringBuilder()
                .append("select mbp.* ")
                .append("from m_biller_product mbp ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("where mb.biller_code = ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, billerCode);
    }

    @Override
    public List<BillerProduct> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                  List<String> orders, Object... params) {
        String sql =
                  "select mbp.*, mb.*, mtt.* "
                + "from m_biller_product mbp "
                + "join m_biller mb "
                + "   on mb.id = mbp.m_biller_id "
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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<BillerProduct>() {
            public BillerProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                BillerProduct mbp = defaultMapper.mapRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller mb = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType mtt = transactionTypeMapper.chainRow(rs, index);

                mb.setTransactionType(mtt);
                mbp.setBiller(mb);

                return mbp;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                  "select count(*) "
                + "from m_biller_product mbp "
                + "join m_biller mb "
                + "   on mb.id = mbp.m_biller_id "
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
	public List<BillerProduct> getAllByTransactionType(String transactionType) {
		String sqlSelect = "select bp.* from m_biller b" + 
			" inner join m_transaction_type tt on tt.id=b.m_transaction_type_id" +
			" inner join m_biller_product bp on bp.m_biller_id=b.id" +
			" where tt.transaction_type = ?" +
			" order by bp.product_name";
		return getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, new Object[] {transactionType});
	}

    @Override
	public BillerProduct getByBillerCodeProductCode(String billerCode, String productCode) {
    	/*
        StringBuilder sql = new StringBuilder()
        .append("select mbp.* ")
        .append("from m_biller_product mbp ")
        .append("from m_biller b ON b.id = mbp.m_biller_id ")
        .append("where b.biller_code = ? and mbp.product_code = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, billerCode, productCode);
        */
        return null;
	}

}
