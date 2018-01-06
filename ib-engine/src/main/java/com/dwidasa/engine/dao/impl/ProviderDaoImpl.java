package com.dwidasa.engine.dao.impl;

import java.util.Date;
import java.util.List;

import com.dwidasa.engine.dao.ProviderDao;
import com.dwidasa.engine.dao.mapper.ProviderMapper;
import com.dwidasa.engine.model.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:21 PM
 */
@Repository("providerDao")
public class ProviderDaoImpl extends GenericDaoImpl<Provider, Long> implements ProviderDao {
    @Autowired
    public ProviderDaoImpl(DataSource dataSource, ProviderMapper providerMapper) {
        super("m_provider", dataSource);
        defaultMapper = providerMapper;

        insertSql = new StringBuilder()
            .append("insert into m_provider ( ")
            .append("   provider_code, provider_name, inquiry, created, createdby, updated, updatedby, description ")
            .append(") ")
            .append("values ( ")
            .append("   :providerCode, :providerName, :inquiry, :created, :createdby, :updated, :updatedby, :description ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_provider ")
            .append("set ")
            .append("   provider_code = :providerCode, provider_name = :providerName, inquiry = :inquiry, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby, description = :description ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Provider get(String code) {
        StringBuilder sql = new StringBuilder()
                .append("select mp.* ")
                .append("from m_provider mp ")
                .append("where mp.provider_code = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }

	@Override
	public List<Provider> getAllWithOrder() {
		String sqlSelect = "select * from m_provider order by provider_name";
		return getSimpleJdbcTemplate().query(sqlSelect, defaultMapper);
	}

	@Override
	public void updateStatusBiller(Long id, boolean status, Date createdBy,
			Long created, Date UpdateBy, Long updated) {
		
		StringBuilder sql = new StringBuilder()
        .append("update m_provider ")
        .append("set is_active = ?, created = ?, createdby = ?,  updated = ?, updatedby = ? where id = ?");

        getSimpleJdbcTemplate().update(sql.toString(), status, createdBy, created, UpdateBy, updated, id);
		
	}
}
