package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.MenuDao;
import com.dwidasa.engine.dao.mapper.MenuMapper;
import com.dwidasa.engine.model.Menu;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 10:57 AM
 */
@Service("menuDao")
public class MenuDaoImpl extends GenericDaoImpl<Menu, Long> implements MenuDao {
    @Autowired
    public MenuDaoImpl(DataSource dataSource, MenuMapper menuMapper) {
        super("m_menu", dataSource);
        defaultMapper = menuMapper;

        insertSql = new StringBuilder()
            .append("insert into m_menu ( ")
            .append("   parent_id, menu_name, location, rank, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :parentId, :menuName, :location, :rank, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_menu ")
            .append("set ")
            .append("   parent_id = :parentId, menu_name = :menuName, location = :location, rank = :rank, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    public List<Menu> getMerchantEdcAndHiperwalletMenu(){
    	StringBuilder sql = new StringBuilder()
        .append("with recursive r as ( ")
        .append("    select s, 1 as level, array[id] as path ")
        .append("    from m_menu s ")
        .append("    where s.parent_id is null and id =1")
        .append("    union all ")
        .append("    select h, level + 1 as level, path || id ")
        .append("    from r ")
        .append("    join m_menu h ")
        .append("    on h.parent_id = (r.s).id and id in (100,101)")
        .append(") ")
        .append("select (r.s).*, path ")
        .append("from r ")
        .append("order by path ");

		return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Menu>() {
		    public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
		        Menu menu = defaultMapper.mapRow(rs, 0);
		        String path = rs.getString(Constants.MENU_LENGTH + 1);
		
		        path = "," + path.substring(1, path.length() - 1) + ",";
		        menu.setPath(path);
		
		        return menu;
		    }
		});
  }
    String baseQuery = "select s, 1 as level, array[id] as path " +
    		"from m_menu s " +
    		"where s.parent_id is null AND s.id NOT IN (6) " +
    		"union all " +
    		"select h, level + 1 as level, path || id " +
    		"from r " +
    		"join m_menu h " +
    		"on h.parent_id = (r.s).id ";
    
    String baseQueryMerchant = "select s, 1 as level, array[id] as path " +
    		"from m_menu s " +
    		"where s.parent_id is null " +
    		"union all " +
    		"select h, level + 1 as level, path || id " +
    		"from r " +
    		"join m_menu h " +
    		"on h.parent_id = (r.s).id ";
    
    String baseQueryMerchantPac = "select s, 1 as level, array[id] as path " +
    		"from m_menu s " +
    		"where s.parent_id is null and id not in(3, 4, 5, 6, 9, 11) " +
    		"union all " +
    		"select h, level + 1 as level, path || id " +
    		"from r " +
    		"join m_menu h " +
    		"on h.parent_id = (r.s).id ";
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getAllByHierarchy() {
        StringBuilder sql = new StringBuilder()
                .append("with recursive r as ( ")
                .append(baseQuery + " and id not in (402, 6, 601, 602, 603, 604, 1104) ")	//tanpa mnclife, cash out, cash to bank, dan cash out elmo
                .append(") ")
                .append("select (r.s).*, path ")
                .append("from r ")
                .append("order by path ");
        logger.info("sql.toString()=" + sql.toString());
        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Menu>() {
            public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
                Menu menu = defaultMapper.mapRow(rs, 0);
                String path = rs.getString(Constants.MENU_LENGTH + 1);

                path = "," + path.substring(1, path.length() - 1) + ",";
                menu.setPath(path);

                return menu;
            }
        });
    }

	@Override
	public List<Menu> getMerchantMenu() {
		StringBuilder sql = new StringBuilder()
        .append("with recursive r as ( ")
        .append(baseQueryMerchant + " and id not in (1007, 1008, 1102, 1103) ")		//tanpa sms registration & mobile registration & Link Soft Token & Undian
        .append(") ")
        .append("select (r.s).*, path ")
        .append("from r ")
        .append("order by path ");

		return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Menu>() {
		    public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
		        Menu menu = defaultMapper.mapRow(rs, 0);
		        String path = rs.getString(Constants.MENU_LENGTH + 1);
		
		        path = "," + path.substring(1, path.length() - 1) + ",";
		        menu.setPath(path);
		
		        return menu;
		    }
		});
	}

	@Override
	public List<Menu> getMerchantMenuPac() {
		StringBuilder sql = new StringBuilder()
        .append("with recursive r as ( ")
        .append(baseQueryMerchantPac + " and id not in (102, 103, 1001, 1002, 1007, 1008, 1102, 1103, 601, 602, 603, 604) ")		//tanpa delima dkk, sms registration & mobile registration & Link Soft Token & Undian
        .append(") ")
        .append("select (r.s).*, path ")
        .append("from r ")
        .append("order by path ");

		return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<Menu>() {
		    public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
		        Menu menu = defaultMapper.mapRow(rs, 0);
		        String path = rs.getString(Constants.MENU_LENGTH + 1);
		
		        path = "," + path.substring(1, path.length() - 1) + ",";
		        menu.setPath(path);
		
		        return menu;
		    }
		});
	}
}
