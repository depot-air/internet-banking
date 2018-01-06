package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.RoleDao;
import com.dwidasa.engine.dao.mapper.RoleMapper;
import com.dwidasa.engine.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 10:57 AM
 */
@Service("roleDao")
public class RoleDaoImpl extends GenericDaoImpl<Role, Long> implements RoleDao {
    @Autowired
    public RoleDaoImpl(DataSource dataSource, RoleMapper roleMapper) {
        super("m_role", dataSource);
        defaultMapper = roleMapper;

        insertSql = new StringBuilder()
            .append("insert into m_role ( ")
            .append("   role_name, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :roleName, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_role ")
            .append("set ")
            .append("   role_name = :roleName, description = :description, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id  = :id");
    }
}
