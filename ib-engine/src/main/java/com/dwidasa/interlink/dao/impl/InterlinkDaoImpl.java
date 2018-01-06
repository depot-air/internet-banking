package com.dwidasa.interlink.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.interlink.dao.InterlinkDao;
import com.dwidasa.interlink.model.MInterlink;

/**
 * @author prayugo
 */
@Repository( "interlinkDao" )
public class InterlinkDaoImpl implements InterlinkDao {
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Autowired
	public void setDataSource( DataSource dataSource ) {
	    this.simpleJdbcTemplate = new SimpleJdbcTemplate( dataSource );
	}

    @Autowired
    private ExtendedProperty extendedProperty;

	/**
	 * get interlink configuration object
	 * @return interlink configuration object
	 */
	public MInterlink getInterlink() {
		
		try {
			String sql = "select * from m_interlink where id=" + extendedProperty.getServerType();
			return ( MInterlink ) simpleJdbcTemplate.queryForObject( sql, 
				new RowMapper< MInterlink >() {
					public MInterlink mapRow( ResultSet rs, int rowNum ) throws SQLException {
						MInterlink o = new MInterlink();
						o.setId( rs.getLong( "id" ) );
						o.setLinkType( rs.getInt( "link_type" ) );
						o.setEchoPeriod( rs.getLong( "echo_period" ) );
						o.setMonitorPeriod( rs.getLong( "monitor_period" ) );
						o.setTimeoutPeriod( rs.getLong( "timeout_period" ) );
						o.setDigesterName( rs.getString( "digester_name" ) );
						o.setTransporterName( rs.getString( "transporter_name" ) );
						o.setMessageType( rs.getString( "message_type" ) );
						o.setIsoPackager( rs.getString( "iso_packager" ) );
						o.setMessageKeyPosition( rs.getInt( "message_key_position" ) );
						o.setMessageKeyLength( rs.getInt( "message_key_length" ) );
						o.setMessageKeyElement( rs.getString( "message_key_element" ) );
						o.setDescription( rs.getString( "description" ) );
						return o;
					}
				}
			);
		}
		catch( Exception e ) {

			e.printStackTrace();
			System.out.println( "InterlinkDaoImpl.getInterlink() is null" );

		}
		
		return null;
	}

}
