package com.dwidasa.engine.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ProviderDenominationDao;
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
 * Time: 6:15 PM
 */
@Repository("providerDenominationDao")
public class ProviderDenominationDaoImpl extends GenericDaoImpl<ProviderDenomination, Long>
        implements ProviderDenominationDao {
    private final ChainedRowMapper<BillerProduct> billerProductMapper;
    private final ChainedRowMapper<Biller> billerMapper;
    private final ChainedRowMapper<TransactionType> transactionTypeMapper;
    private final ChainedRowMapper<ProductDenomination> productDenominationMapper;
    private final ChainedRowMapper<Provider> providerMapper;

    @Autowired
    public ProviderDenominationDaoImpl(DataSource dataSource, ProviderDenominationMapper providerDenominationMapper,
                                       BillerProductMapper billerProductMapper, BillerMapper billerMapper,
                                       TransactionTypeMapper transactionTypeMapper,
                                       ProductDenominationMapper productDenominationMapper,
                                       ProviderMapper providerMapper) {
        super("m_provider_denomination", dataSource);
        defaultMapper = providerDenominationMapper;
        this.billerProductMapper = billerProductMapper;
        this.billerMapper = billerMapper;
        this.transactionTypeMapper = transactionTypeMapper;
        this.productDenominationMapper = productDenominationMapper;
        this.providerMapper = providerMapper;

        insertSql = new StringBuilder()
            .append("insert into m_provider_denomination ( ")
            .append("   m_provider_id, m_product_denomination_id, price, fee, created, createdby, ")
            .append("   updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :providerId, :productDenominationId, :price, :fee, :created, :createdby, ")
            .append("   :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_provider_denomination ")
            .append("set ")
            .append("   m_provider_id = :providerId, m_product_denomination_id = :productDenominationId, ")
            .append("   price = :price, fee = :fee, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public List<ProviderDenomination> getAllWithTransactionTypeAndProvider() {
        StringBuilder sql = new StringBuilder()
                .append("select mprd.*, mp.*, mpd.*, mbp.*, mb.*, mtt.* ")
                .append("from m_provider_denomination mprd ")
                .append("join m_provider mp ")
                .append("   on mp.id = mprd.m_provider_id ")
                .append("join m_product_denomination mpd ")
                .append("   on mpd.id = mprd.m_product_denomination_id ")
                .append("join m_biller_product mbp ")
                .append("   on mbp.id = mpd.m_biller_product_id ")
                .append("join m_biller mb ")
                .append("   on mb.id = mbp.m_biller_id ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mb.m_transaction_type_id ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ProviderDenomination>() {
            public ProviderDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProviderDenomination providerDenomination = defaultMapper.mapRow(rs, index);
                index += Constants.PROVIDER_DENOMINATION_LENGTH;
                Provider provider = providerMapper.chainRow(rs, index);
                index += Constants.PROVIDER_LENGTH;
                ProductDenomination productDenomination = productDenominationMapper.chainRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct billerProduct = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller biller = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);

                providerDenomination.setProvider(provider);
                providerDenomination.setProductDenomination(productDenomination);
                productDenomination.setBillerProduct(billerProduct);
                billerProduct.setBiller(biller);
                biller.setTransactionType(transactionType);

                return providerDenomination;
            }
        });
    }

    @Override
    public List<ProviderDenomination> getAllOrderedByProviderPrice() {
        /*
        List<ProviderDenomination> pds = getAll();
        Collections.sort(pds);
        return pds;
        */
        String sql =
                "select mprd.*, mp.*, mpd.*, mbp.*, mb.*, mtt.* "
              + "from m_provider_denomination mprd "
              + "join m_provider mp "
              + "   on mp.id = mprd.m_provider_id "
              + "join m_product_denomination mpd "
              + "   on mpd.id = mprd.m_product_denomination_id "
              + "join m_biller_product mbp "
              + "   on mbp.id = mpd.m_biller_product_id "
              + "join m_biller mb "
              + "   on mb.id = mbp.m_biller_id "
              + "join m_transaction_type mtt "
              + "   on mtt.id = mb.m_transaction_type_id ";

        sql += "order by mprd.m_provider_id, mprd.price ";

        return getSimpleJdbcTemplate().query(sql, new RowMapper<ProviderDenomination>() {
            public ProviderDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProviderDenomination mprd = defaultMapper.mapRow(rs, index);
                index += Constants.PROVIDER_DENOMINATION_LENGTH;
                Provider mp = providerMapper.chainRow(rs, index);
                index += Constants.PROVIDER_LENGTH;
                ProductDenomination mpd = productDenominationMapper.chainRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct mbp = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller mb = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType mtt = transactionTypeMapper.chainRow(rs, index);

                mprd.setProvider(mp);
                mprd.setProductDenomination(mpd);
                mpd.setBillerProduct(mbp);
                mbp.setBiller(mb);
                mb.setTransactionType(mtt);

                if (mprd.getProductDenomination().getDenomination() != null && mprd.getProductDenomination().getDenomination().trim().matches("[0-9.,]+")) {
                	String denom = mprd.getProductDenomination().getDenomination().replaceAll("[^0-9]", "").trim();
//                	System.out.println(mprd.getProductDenomination().getDenomination() + " - " + denom);
                	BigDecimal ddenom = new BigDecimal(denom);
                    if (ddenom.doubleValue() >= 25000) {
                    	mprd.setPrice(ddenom);
//                    	System.out.println("new price - " + ddenom);
                    }	
                }
                return mprd;
            }
        });

    }

    @Override
    public List<ProviderDenomination> getAllDefaultOrderedByProviderPrice() {
        /*
        List<ProviderDenomination> pds = getAll();
        Collections.sort(pds);
        return pds;
        */
        String sql =
                "select mprd.*, mp.*, mpd.*, mbp.*, mb.*, mtt.* "
              + "from m_provider_denomination mprd "
              + "join m_provider mp "
              + "   on mp.id = mprd.m_provider_id "
              + "join m_product_denomination mpd "
              + "   on mpd.id = mprd.m_product_denomination_id "
              + "join m_biller_product mbp "
              + "   on mbp.id = mpd.m_biller_product_id "
              + "join m_biller mb "
              + "   on mb.id = mbp.m_biller_id "
              + "join m_transaction_type mtt "
              + "   on mtt.id = mb.m_transaction_type_id "
              + "where mp.id = mpd.m_provider_id_default "
              + "  and (to_number(mpd.denomination, '999999') >= 25000 "
              + "       or mpd.denomination like '%pts' or mpd.denomination like '%CC')) ";

        sql += "order by mprd.m_provider_id, mprd.price ";

        return getSimpleJdbcTemplate().query(sql, new RowMapper<ProviderDenomination>() {
            public ProviderDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProviderDenomination mprd = defaultMapper.mapRow(rs, index);
                index += Constants.PROVIDER_DENOMINATION_LENGTH;
                Provider mp = providerMapper.chainRow(rs, index);
                index += Constants.PROVIDER_LENGTH;
                ProductDenomination mpd = productDenominationMapper.chainRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct mbp = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller mb = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType mtt = transactionTypeMapper.chainRow(rs, index);

                mprd.setProvider(mp);
                mprd.setProductDenomination(mpd);
                mpd.setBillerProduct(mbp);
                mbp.setBiller(mb);
                mb.setTransactionType(mtt);

                return mprd;
            }
        });

    }
    
    @Override
    public List<ProviderDenomination> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                         List<String> orders, Object... params) {
        String sql =
                  "select mprd.*, mp.*, mpd.*, mbp.*, mb.*, mtt.* "
                + "from m_provider_denomination mprd "
                + "join m_provider mp "
                + "   on mp.id = mprd.m_provider_id "
                + "join m_product_denomination mpd "
                + "   on mpd.id = mprd.m_product_denomination_id "
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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<ProviderDenomination>() {
            public ProviderDenomination mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ProviderDenomination mprd = defaultMapper.mapRow(rs, index);
                index += Constants.PROVIDER_DENOMINATION_LENGTH;
                Provider mp = providerMapper.chainRow(rs, index);
                index += Constants.PROVIDER_LENGTH;
                ProductDenomination mpd = productDenominationMapper.chainRow(rs, index);
                index += Constants.PRODUCT_DENOMINATION_LENGTH;
                BillerProduct mbp = billerProductMapper.chainRow(rs, index);
                index += Constants.BILLER_PRODUCT_LENGTH;
                Biller mb = billerMapper.chainRow(rs, index);
                index += Constants.BILLER_LENGTH;
                TransactionType mtt = transactionTypeMapper.chainRow(rs, index);

                mprd.setProvider(mp);
                mprd.setProductDenomination(mpd);
                mpd.setBillerProduct(mbp);
                mbp.setBiller(mb);
                mb.setTransactionType(mtt);

                return mprd;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                "select count(*) "
              + "from m_provider_denomination mprd "
              + "join m_provider mp "
              + "   on mp.id = mprd.m_provider_id "
              + "join m_product_denomination mpd "
              + "   on mpd.id = mprd.m_product_denomination_id "
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
}
