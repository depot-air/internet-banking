package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Menu;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 10:42 AM
 */
@Component("menuMapper")
public class MenuMapper extends ChainedRowMapper<Menu> implements ParameterizedRowMapper<Menu> {
    public MenuMapper() {
    }

    public Menu chainRow(ResultSet rs, int index) throws SQLException {
        Menu menu = new Menu();

        menu.setId(rs.getLong(++index));
        menu.setParentId(rs.getLong(++index));
        menu.setMenuName(rs.getString(++index));
        menu.setLocation(rs.getString(++index));
        menu.setRank(rs.getInt(++index));
        menu.setCreated(rs.getTimestamp(++index));
        menu.setCreatedby(rs.getLong(++index));
        menu.setUpdated(rs.getTimestamp(++index));
        menu.setUpdatedby(rs.getLong(++index));

        return menu;
    }
}
