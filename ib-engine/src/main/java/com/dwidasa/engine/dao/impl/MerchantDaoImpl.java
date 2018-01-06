package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.MerchantDao;
import com.dwidasa.engine.dao.mapper.MerchantMapper;
import com.dwidasa.engine.model.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:24 PM
 */
@Repository("merchantDao")
public class MerchantDaoImpl extends GenericDaoImpl<Merchant, Long> implements MerchantDao {
    @Autowired
    public MerchantDaoImpl(DataSource dataSource, MerchantMapper merchantMapper) {
        super("m_merchant", dataSource);
        defaultMapper = merchantMapper;

        insertSql = new StringBuilder()
            .append("insert into m_merchant ( ")
            .append("   merchant_type, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :merchantType, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_merchant ")
            .append("set ")
            .append("   merchant_type = :merchantType, description = :description, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }
}
