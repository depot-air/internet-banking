package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.dao.mapper.BillerMapper;
import com.dwidasa.engine.dao.mapper.BillerProductMapper;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.ProviderMapper;
import com.dwidasa.engine.dao.mapper.ProviderProductMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderProduct;
import com.dwidasa.engine.model.TransactionType;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:25 PM
 */
@Repository("providerProductDao")
public class ProviderProductDaoImpl extends GenericDaoImpl<ProviderProduct, Long> implements ProviderProductDao {
    private final ChainedRowMapper<BillerProduct> billerProductMapper;
    private final ChainedRowMapper<Biller> billerMapper;
    private final ChainedRowMapper<TransactionType> transactionTypeMapper;
    private final ChainedRowMapper<Provider> providerMapper;

    @Autowired
    public ProviderProductDaoImpl(DataSource dataSource, ProviderProductMapper providerProductMapper,
                                  BillerProductMapper billerProductMapper, BillerMapper billerMapper,
                                  TransactionTypeMapper transactionTypeMapper, ProviderMapper providerMapper) {
        super("m_provider_product", dataSource);
        defaultMapper = providerProductMapper;
        this.billerProductMapper = billerProductMapper;
        this.billerMapper = billerMapper;
        this.transactionTypeMapper = transactionTypeMapper;
        this.providerMapper = providerMapper;

        insertSql = new StringBuilder()
            .append("insert into m_provider_product ( ")
            .append("   m_provider_id, m_biller_product_id, fee, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :providerId, :billerProductId, :fee, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_provider_product ")
            .append("set ")
            .append("   m_provider_id = :providerId, m_biller_product_id = :billerProductId, fee = :fee, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public List<ProviderProduct> getAllWithTransactionTypeAndProvider() {
        StringBuilder sql = new StringBuilder()
                .append("select mpp.*, mbp.*, mb.*, mtt.*, mp.* ")
                .append("from m_provider_product mpp ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpp.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ")
                .append("join m_provider mp ")
                .append("   on mp.id = mpp.m_provider_id where (mpp.is_active <> TRUE or mpp.is_active is null)");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ProviderProduct>() {
            public ProviderProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProviderProduct pp = defaultMapper.mapRow(rs, index);
                index += Constants.PROVIDER_PRODUCT_LENGTH;
                BillerProduct bp = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller b = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType tt = transactionTypeMapper.chainRow(rs, index);
                index += Constants.TRANSACTION_TYPE_LENGTH;
                Provider p = providerMapper.chainRow(rs, index);

                b.setTransactionType(tt);
                bp.setBiller(b);
                pp.setBillerProduct(bp);
                pp.setProvider(p);

                return pp;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public List<ProviderProduct> getAll(String productCode) {
        StringBuilder sql = new StringBuilder()
                .append("select mpp.*, mp.* ")
                .append("from m_provider_product mpp ")
                .append("join m_provider mp ")
                .append("   on mp.id = mpp.m_provider_id ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpp.m_biller_product_id ")
                .append("where mbp.product_code = ? and (mpp.is_active <> TRUE or mpp.is_active is null)");

        List<ProviderProduct> providerProducts = getJdbcTemplate().query(sql.toString(),
                new ResultSetExtractor<List<ProviderProduct>>() {
            public List<ProviderProduct> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<ProviderProduct> providerProducts = new ArrayList<ProviderProduct>();

                while (rs.next()) {
                    int index = 0;
                    ProviderProduct providerProduct = defaultMapper.mapRow(rs, index);
                    index += Constants.PROVIDER_PRODUCT_LENGTH;
                    Provider provider = providerMapper.chainRow(rs, index);
                    providerProduct.setProvider(provider);

                    providerProducts.add(providerProduct);
                }

                return providerProducts;
            }
        }, productCode);

        return providerProducts;
    }

    @Override
    public List<ProviderProduct> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                    List<String> orders, Object... params) {
        String sql =
                  "select mpp.*, mbp.*, mb.*, mtt.*, mp.* "
                + "from m_provider_product mpp "
                + "join m_biller_product mbp "
                + "   on mbp.id = mpp.m_biller_product_id "
                + "join m_biller mb "
                + "   on mb.id = mbp.m_biller_id "
                + "join m_transaction_type mtt "
                + "   on mtt.id = mb.m_transaction_type_id "
                + "join m_provider mp "
                + "   on mp.id = mpp.m_provider_id ";

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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<ProviderProduct>() {
            public ProviderProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProviderProduct mpp = defaultMapper.mapRow(rs, index);
                index += Constants.PROVIDER_PRODUCT_LENGTH;
                BillerProduct mbp = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller mb = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType mtt = transactionTypeMapper.chainRow(rs, index);
                index += Constants.TRANSACTION_TYPE_LENGTH;
                Provider mp = providerMapper.chainRow(rs, index);

                mb.setTransactionType(mtt);
                mbp.setBiller(mb);
                mpp.setBillerProduct(mbp);
                mpp.setProvider(mp);

                return mpp;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                "select count(*) "
              + "from m_provider_product mpp "
              + "join m_biller_product mbp "
              + "   on mbp.id = mpp.m_biller_product_id "
              + "join m_biller mb "
              + "   on mb.id = mbp.m_biller_id "
              + "join m_transaction_type mtt "
              + "   on mtt.id = mb.m_transaction_type_id "
              + "join m_provider mp "
              + "   on mp.id = mpp.m_provider_id ";

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
	public ProviderProduct getWithTransactionType(Long id) {
		String sqlSelect = "select pp.*, tt.transaction_type from m_provider_product pp"+
			" inner join m_biller_product bp on bp.id=pp.m_biller_product_id"+
			" inner join m_biller b on b.id=bp.m_biller_id"+
			" inner join m_transaction_type tt on tt.id=b.m_transaction_type_id" +
			" where pp.id=?";
		return getSimpleJdbcTemplate().queryForObject(sqlSelect, new RowMapper<ProviderProduct>() {
            public ProviderProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProviderProduct pp = defaultMapper.mapRow(rs, index);
                pp.setTransactionType(rs.getString("transaction_type"));	
                return pp;
            }
        }, new Object[] {id});
	}

    public ProviderProduct getByProviderCodeProductCode(String providerCode, String productCode) {
        String sqlSelect = "select pp.* from m_provider_product pp"+
			" inner join m_biller_product bp on bp.id=pp.m_biller_product_id"+
            " inner join m_provider v on v.id=pp.m_provider_id"+
			" where v.provider_code=? AND bp.product_code=? ";
        try {
            ProviderProduct providerProduct = getSimpleJdbcTemplate().queryForObject(sqlSelect, new RowMapper<ProviderProduct>() {
                public ProviderProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    ProviderProduct pp = defaultMapper.mapRow(rs, index);
                    return pp;
                }
            }, new Object[] {providerCode, productCode});
            return providerProduct;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    
    public void updateStatusProviderProduct(Long id, boolean status, Date createdBy, Long created, Date UpdateBy, Long updated){
    	//
		//created = :created, createdby = :createdby, ")
	    //.append("   updated = :updated, updatedby = :updatedby
	    	StringBuilder sql = new StringBuilder()
	        .append("update m_provider_product ")
	        .append("set is_active = ?, created = ?, createdby = ?,  updated = ?, updatedby = ? where id = ?");

	        getSimpleJdbcTemplate().update(sql.toString(), status, createdBy, created, UpdateBy, updated, id);
	    	
	    }

}
