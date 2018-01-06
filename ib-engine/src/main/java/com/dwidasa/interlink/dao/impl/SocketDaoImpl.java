package com.dwidasa.interlink.dao.impl;

import com.dwidasa.interlink.dao.SocketDao;
import com.dwidasa.interlink.model.MSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author prayugo
 */
@Repository( "socketDao" )
public class SocketDaoImpl implements SocketDao {

	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Autowired
	public void setDataSource( DataSource dataSource ) {
	    this.simpleJdbcTemplate = new SimpleJdbcTemplate( dataSource );
	}

	/**
	 * get interlink socket configuration
	 * @param id m_interlink_id
	 * @return interlink socket configuration
	 */
	public MSocket getSocket( long id ) {
	
		try {
			
			String sql = "select * from m_interlink_socket where m_interlink_id = ? " ;
	
			return ( MSocket ) simpleJdbcTemplate.queryForObject( sql, 
				new RowMapper< MSocket >() {
					public MSocket mapRow( ResultSet rs, int rowNum ) throws SQLException {
						MSocket o = new MSocket();
						o.setId( rs.getLong( "id" ) );
						o.setInterlinkId( rs.getLong( "m_interlink_id" ) );
						o.setConnectionType( rs.getInt( "connection_type" ) );
						o.setServerAddress( rs.getString( "server_address" ) );
						o.setServerPort( rs.getInt( "server_port" ) );
						o.setMonitorPeriod( rs.getLong( "monitor_period" ) );
						o.setSocketDriver( rs.getString( "socket_driver" ) );
						o.setDescription( rs.getString( "description" ) );
						o.setStatus( rs.getString( "status" ) );
						return o;
					}
				},
				new Object[] { id }
			);
		}
		catch( Exception e ) {
			
			System.out.println( "SocketDaoImpl.getSocket( " + id + " ) is null" );
		}
		
		return null;
	}

	/**
	 * update the connection status
	 * @param model socket model
	 * @param status true for connected and vice versa
	 */
	public void setConnection( MSocket model, boolean status ) {
	
		String yn = ( status == true ) ? "Y" : "N";
		String sql = "update m_interlink_socket set status = ? where id = ?";
		
		simpleJdbcTemplate.update( sql, new Object[] { yn, model.getId() } );
	}

}
