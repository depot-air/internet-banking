package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.FormConfigDao;
import com.dwidasa.engine.dao.mapper.FormConfigMapper;
import com.dwidasa.engine.model.FormConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/7/11
 * Time: 5:56 PM
 */
@Repository("formConfigDao")
public class FormConfigDaoImpl extends GenericDaoImpl<FormConfig, Long> implements FormConfigDao {
    @Autowired
    public FormConfigDaoImpl(DataSource dataSource, FormConfigMapper formConfigMapper) {
        super("m_form_config", dataSource);
        defaultMapper = formConfigMapper;

        insertSql = new StringBuilder()
                .append("insert into m_form_config ( ")
                .append("   form_name, token_type, created, createdby, updated, updatedby ")
                .append(") ")
                .append("values ( ")
                .append("   :formName, :tokenType, :created, :createdby, :updated, :updatedby ")
                .append(") ");

        updateSql = new StringBuilder()
                .append("update m_form_config ")
                .append("set ")
                .append("   form_name = :formName, token_type = :tokenType, created = :created, ")
                .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
                .append("where id = :id ");
    }
}
