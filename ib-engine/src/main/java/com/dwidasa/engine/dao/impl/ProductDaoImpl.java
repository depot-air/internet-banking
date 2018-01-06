package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.ProductDao;
import com.dwidasa.engine.dao.mapper.ProductMapper;
import com.dwidasa.engine.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:32 PM
 */
@Repository("productDao")
public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao {
    @Autowired
    public ProductDaoImpl(DataSource dataSource, ProductMapper productMapper) {
        super("m_product", dataSource);
        defaultMapper = productMapper;

        insertSql = new StringBuilder()
            .append("insert into m_product ( ")
            .append("   product_code, product_name, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :productCode, :productName, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_product ")
            .append("set ")
            .append("   product_code = :productCode, product_name = :productName, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    public Product get(String productCode) {
        StringBuilder sql = new StringBuilder()
                .append("select mp.* ")
                .append("from m_product mp ")
                .append("where mp.product_code = ?");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, productCode);
    }
}
