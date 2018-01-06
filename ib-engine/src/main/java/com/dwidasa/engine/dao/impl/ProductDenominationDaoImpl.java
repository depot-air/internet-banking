package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ProductDenominationDao;
import com.dwidasa.engine.dao.mapper.BillerMapper;
import com.dwidasa.engine.dao.mapper.BillerProductMapper;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.ProductDenominationMapper;
import com.dwidasa.engine.dao.mapper.ProviderDenominationMapper;
import com.dwidasa.engine.dao.mapper.ProviderMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.TransactionType;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:07 PM
 */
@Repository("productDenominationDao")
public class ProductDenominationDaoImpl extends GenericDaoImpl<ProductDenomination, Long>
        implements ProductDenominationDao {

	private final ChainedRowMapper<BillerProduct>  billerProductMapper;
    private final ChainedRowMapper<Biller> billerMapper;
    private final ChainedRowMapper<TransactionType> transactionTypeMapper;
    private final ChainedRowMapper<ProviderDenomination> providerDenominationMapper;
    private final ChainedRowMapper<Provider> providerMapper;

    @Override
    public List<ProductDenomination> getAllOrderedByProductIdDenom() {
        /*
        List<ProductDenomination> pds = getAll();
        Collections.sort(pds);
        return pds;
        */
/*        
        StringBuilder sql = new StringBuilder()
                .append("select mpd.*, mbp.*, mb.*, mtt.* ")
                .append("from m_product_denomination mpd ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ");
        sql = sql.append("order by mbp.id, cast(mpd.denomination as numeric)  ");
        //sql = sql.append("order by mbp.id, mpd.denomination ");
*/
        StringBuilder sql = new StringBuilder()
                .append("SELECT mpd.*, mbp.*, mb.*, mtt.*, mv.* ")
                .append("from m_product_denomination mpd ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ")
                .append("join m_provider mv ")
                .append("   on mv.id = mpd.m_provider_id_default where mpd.is_active <> TRUE or mpd.is_active is null ")
                .append("order by mbp.id, cast( ")
                .append("( CASE ")
                .append("    WHEN isdigits(mpd.denomination) THEN '' || cast(mpd.denomination as numeric) ")
                .append("    ELSE '0' ")
                .append("  END ")
                .append(") as numeric) ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ProductDenomination>() {
            public ProductDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProductDenomination productDenomination = defaultMapper.mapRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct billerProduct = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller biller = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                index += Constants.TRANSACTION_TYPE_LENGTH;
                Provider provider = providerMapper.chainRow(rs, index);

                productDenomination.setBillerProduct(billerProduct);
                billerProduct.setBiller(biller);
                biller.setTransactionType(transactionType);
                productDenomination.setDefaultProvider(provider);

                return productDenomination;
            }
        });
    }
    
    
    @Override
    public List<ProductDenomination> getAllOrderedByProductIdDenomNoActive() {
        /*
        List<ProductDenomination> pds = getAll();
        Collections.sort(pds);
        return pds;
        */
/*        
        StringBuilder sql = new StringBuilder()
                .append("select mpd.*, mbp.*, mb.*, mtt.* ")
                .append("from m_product_denomination mpd ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ");
        sql = sql.append("order by mbp.id, cast(mpd.denomination as numeric)  ");
        //sql = sql.append("order by mbp.id, mpd.denomination ");
*/
        StringBuilder sql = new StringBuilder()
                .append("SELECT mpd.*, mbp.*, mb.*, mtt.*, mv.* ")
                .append("from m_product_denomination mpd ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ")
                .append("join m_provider mv ")
                .append("   on mv.id = mpd.m_provider_id_default where mpd.is_active = TRUE ")
                .append("order by mbp.id, cast( ")
                .append("( CASE ")
                .append("    WHEN isdigits(mpd.denomination) THEN '' || cast(mpd.denomination as numeric) ")
                .append("    ELSE '0' ")
                .append("  END ")
                .append(") as numeric) ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ProductDenomination>() {
            public ProductDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProductDenomination productDenomination = defaultMapper.mapRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct billerProduct = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller biller = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                index += Constants.TRANSACTION_TYPE_LENGTH;
                Provider provider = providerMapper.chainRow(rs, index);

                productDenomination.setBillerProduct(billerProduct);
                billerProduct.setBiller(biller);
                biller.setTransactionType(transactionType);
                productDenomination.setDefaultProvider(provider);

                return productDenomination;
            }
        });
    }

    public List<ProductDenomination> getNonMerchantOrderedByProductIdDenom(){
        StringBuilder sql = new StringBuilder()
                .append("SELECT mpd.*, mbp.*, mb.*, mtt.* ")
                .append("from m_product_denomination mpd ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ")
                .append(" where to_number(mpd.denomination, '999999') >= 25000 ")
                .append(" or mpd.denomination like '%pts' or mpd.denomination like '%CC' or (mpd.is_active <> TRUE or mpd.is_active is null) ")
                .append("order by mbp.id, cast( ")
                .append("( CASE ")
                .append("    WHEN isdigits(mpd.denomination) THEN '' || cast(mpd.denomination as numeric) ")
                .append("    ELSE '0' ")
                .append("  END ")
                .append(") as numeric) ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ProductDenomination>() {
            public ProductDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProductDenomination productDenomination = defaultMapper.mapRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct billerProduct = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller biller = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);

                productDenomination.setBillerProduct(billerProduct);
                billerProduct.setBiller(biller);
                biller.setTransactionType(transactionType);

                return productDenomination;
            }
        });
    }

    @Autowired
    public ProductDenominationDaoImpl(DataSource dataSource, ProductDenominationMapper productDenominationMapper,
                                      BillerProductMapper billerProductMapper, BillerMapper billerMapper,
                                      TransactionTypeMapper transactionTypeMapper,
                                      ProviderDenominationMapper providerDenominationMapper,
                                      ProviderMapper providerMapper) {
        super("m_product_denomination", dataSource);
        defaultMapper = productDenominationMapper;
        this.billerProductMapper = billerProductMapper;
        this.billerMapper = billerMapper;
        this.transactionTypeMapper = transactionTypeMapper;
        this.providerDenominationMapper = providerDenominationMapper;
        this.providerMapper = providerMapper;

        insertSql = new StringBuilder()
            .append("insert into m_product_denomination ( ")
            .append("   m_biller_product_id, denomination, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :billerProductId, :denomination, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_product_denomination ")
            .append("set ")
            .append("   m_biller_product_id = :billerProductId, denomination = :denomination, m_provider_id_default = :defaultProviderId,")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public List<ProductDenomination> getAllWithTransactionType() {
        StringBuilder sql = new StringBuilder()
                .append("select mpd.*, mbp.*, mb.*, mtt.*, mv.* ")
                .append("from m_product_denomination mpd ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ")
                .append("join m_provider mv ")
                .append("   on mv.id = mpd.m_provider_id_default ")
                .append(" where mpd.is_active <> TRUE or mpd.is_active is null order by mpd.m_biller_product_id, char_length(mpd.denomination),mpd.denomination ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ProductDenomination>() {
            public ProductDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProductDenomination productDenomination = defaultMapper.mapRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct billerProduct = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller biller = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                index += Constants.TRANSACTION_TYPE_LENGTH;
                Provider provider = providerMapper.chainRow(rs, index);

                biller.setTransactionType(transactionType);
                billerProduct.setBiller(biller);
                productDenomination.setBillerProduct(billerProduct);
                productDenomination.setBillerProductCode(billerProduct.getProductCode());
                productDenomination.setDefaultProvider(provider);

                return productDenomination;
            }
        });
    }

    
    public List<ProductDenomination> getAllWithTransactionType(String transactionType, String BillerCode, String productCode) {
        StringBuilder sql = new StringBuilder()
                .append("select mpd.*, mbp.*, mb.*, mtt.*, mv.* ")
                .append("from m_product_denomination mpd ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ")
                .append("join m_provider mv ")
                .append("   on mv.id = mpd.m_provider_id_default ")
                .append(" where mtt.transaction_type = ? and mb.biller_code = ? and mbp.product_code = ? and (mpd.is_active <> TRUE or mpd.is_active is null) order by mpd.m_biller_product_id, char_length(mpd.denomination),mpd.denomination ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ProductDenomination>() {
            public ProductDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProductDenomination productDenomination = defaultMapper.mapRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct billerProduct = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller biller = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                index += Constants.TRANSACTION_TYPE_LENGTH;
                Provider provider = providerMapper.chainRow(rs, index);

                biller.setTransactionType(transactionType);
                billerProduct.setBiller(biller);
                productDenomination.setBillerProduct(billerProduct);
                productDenomination.setBillerProductCode(billerProduct.getProductCode());
                productDenomination.setDefaultProvider(provider);

                return productDenomination;
            }
        }, transactionType, BillerCode, productCode);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ProductDenomination> getAllWithProvider(String productCode) {
        StringBuilder sql = new StringBuilder()
                .append("select mpd.*, mprd.*, mp.* ")
                .append("from m_product_denomination mpd ")
                .append("join m_provider_denomination mprd ")
                .append("   on mprd.m_product_denomination_id = mpd.id ")
                .append("join m_provider mp ")
                .append("   on mp.id = mprd.m_provider_id ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("where mbp.product_code = ? ")
                .append(" and (mpd.is_active <> TRUE or mpd.is_active is null) order by mpd.m_biller_product_id, char_length(mpd.denomination),mpd.denomination ");

        List<ProductDenomination> productDenominations = getJdbcTemplate().query(sql.toString(),
                new ResultSetExtractor<List<ProductDenomination>>() {
            public List<ProductDenomination> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Long, ProductDenomination> prober = new HashMap<Long, ProductDenomination>();
                List<ProductDenomination> result = new ArrayList<ProductDenomination>();

                while (rs.next()) {
                    int index = 0;
                    ProductDenomination productDenomination = defaultMapper.mapRow(rs, index);
                    index += Constants.PRODUCT_DENOMINATION_LENGTH;
                    ProviderDenomination providerDenomination = providerDenominationMapper.chainRow(rs, index);
                    index += Constants.PROVIDER_DENOMINATION_LENGTH;
                    providerDenomination.setProvider(providerMapper.chainRow(rs, index));

                    if (prober.containsKey(productDenomination.getId())) {
                        ProductDenomination pd = prober.get(productDenomination.getId());
                        pd.getProviderDenominations().add(providerDenomination);
                    }
                    else {
                        Set<ProviderDenomination> providerDenominations = new HashSet<ProviderDenomination>();
                        providerDenominations.add(providerDenomination);

                        productDenomination.setProviderDenominations(providerDenominations);
                        prober.put(productDenomination.getId(), productDenomination);
                        result.add(productDenomination);
                    }
                }

                return result;
            }
        }, productCode);

        return productDenominations;
    }

    @Override
    public List<ProductDenomination> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                        List<String> orders, Object... params) {
        String sql =
                  "select mpd.*, mbp.*, mb.*, mtt.* "
                + "from m_product_denomination mpd "
                + "join m_biller_product mbp "
                + "   on mbp.id = mpd.m_biller_product_id "
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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<ProductDenomination>() {
            public ProductDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProductDenomination mpd = defaultMapper.mapRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct mbp = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller mb = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType mtt = transactionTypeMapper.chainRow(rs, index);

                mb.setTransactionType(mtt);
                mbp.setBiller(mb);
                mpd.setBillerProduct(mbp);

                return mpd;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                "select count(*) "
              + "from m_product_denomination mpd "
              + "join m_biller_product mbp "
              + "   on mbp.id = mpd.m_biller_product_id "
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
    
	public ProductDenomination getByProductCodeDenom(String productCode, String denom) {
		try {
            StringBuilder sql = new StringBuilder()
	            .append("select mpd.*, mv.* ")
	            .append("from m_product_denomination mpd ")
	            .append("join m_biller_product mbp ")
	            .append("   on mbp.id = mpd.m_biller_product_id ")
	            .append("join m_provider mv ")
	            .append("   on mv.id = mpd.m_provider_id_default ")
	            .append("where mbp.product_code = ? and mpd.denomination = ? ");
            logger.info("sql" + sql.toString());
            List<ProductDenomination> pds = getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ProductDenomination>() {
                public ProductDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    ProductDenomination mpd = defaultMapper.mapRow(rs, index);
                    index += Constants.PRODUCT_DENOMINATION_LENGTH;
                    Provider pv = providerMapper.chainRow(rs, index);

                    mpd.setDefaultProvider(pv);

                    return mpd;
                }
            }, productCode, denom);
            logger.info("pds=" + pds);
            if (pds.size() > 0) {
                return pds.get(0);
            } else {
                return null;
            }

		}
		catch( Exception e ) {

			e.printStackTrace();
			System.out.println( "ProductDenominationDaoImpl.getByProductCodeDenom is null" );
		}
		return null;
	}
	
	
	public void updateStatusBiller(Long id, boolean status, Date createdBy, Long created, Date UpdateBy, Long updated){
    	
		//created = :created, createdby = :createdby, ")
	    //.append("   updated = :updated, updatedby = :updatedby
	    	StringBuilder sql = new StringBuilder()
	        .append("update m_product_denomination ")
	        .append("set is_active = ?, created = ?, createdby = ?,  updated = ?, updatedby = ? where id = ?");

	        getSimpleJdbcTemplate().update(sql.toString(), status, createdBy, created, UpdateBy, updated, id);
	    	
	    }
	
	
}
