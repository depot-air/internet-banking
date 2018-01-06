package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.RoleMenuDao;
import com.dwidasa.engine.dao.mapper.RoleMenuMapper;
import com.dwidasa.engine.model.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 10:57 AM
 */
@Service("roleMenuDao")
public class RoleMenuDaoImpl extends GenericDaoImpl<RoleMenu, Long> implements RoleMenuDao {
    @Autowired
    public RoleMenuDaoImpl(DataSource dataSource, RoleMenuMapper roleMenuMapper) {
        super("m_role_menu", dataSource);
        defaultMapper = roleMenuMapper;

        insertSql = new StringBuilder()
            .append("insert into m_role_menu (")
            .append("   m_role_id, m_menu_id, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :roleId, :menuId, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_role_menu ")
            .append("set ")
            .append("   m_role_id = :roleId, m_menu_id = :menuId, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }
}
