package com.dwidasa.interlink.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dwidasa.interlink.dao.DataQueueDao;
import com.dwidasa.interlink.model.MDataQueue;

/**
 * @author prayugo
 */
@Repository( "dataQueueDao" )
public class DataQueueDaoImpl implements DataQueueDao {

	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Autowired
	public void setDataSource( DataSource dataSource ) {
	    this.simpleJdbcTemplate = new SimpleJdbcTemplate( dataSource );
	}

	/**
	 * get data queue configuration
	 * @param code data queue code, either inquiry or transaction
	 * @return data queue configuration
	 */
	@Override
	public MDataQueue getDataQueue( String code ) {

		try {
			
			String sql = "select * from m_data_queue where code = ?";
	
			return ( MDataQueue ) simpleJdbcTemplate.queryForObject( sql, 
				new RowMapper< MDataQueue >() {
					public MDataQueue mapRow( ResultSet rs, int rowNum ) throws SQLException {
						MDataQueue o = new MDataQueue();
						o.setId( rs.getLong( "id" ) );
						o.setCode( rs.getString( "code" ) );
						o.setLibrary( rs.getString( "as400_library" ) );
						o.setSystem( rs.getString( "as400_system" ) );
						o.setUsername( rs.getString( "as400_username" ) );
						o.setPassword( rs.getString( "as400_password" ) );
						o.setQueueReceiver( rs.getString( "data_queue_receiver" ) );
						o.setQueueSender( rs.getString( "data_queue_sender" ) );
						o.setDescription( rs.getString( "description" ) );
						return o;
					}
				},
				new Object[] { code }
			);
		}
		catch( Exception e ) {
			
			System.out.println( "DataQueueDaoImpl.getDataQueue( " + code + " ) is null" );
		}
		
		return null;
	}

}
