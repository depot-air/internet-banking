package com.dwidasa.interlink.dao.impl;

import com.dwidasa.interlink.dao.FeatureDao;
import com.dwidasa.interlink.model.MFeature;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 10/24/12
 */
@Repository( "featureDao" )
public class FeatureDaoImpl implements FeatureDao{
    private static Logger logger = Logger.getLogger( FeatureDaoImpl.class );
	private SimpleJdbcTemplate simpleJdbcTemplate;
    protected RowMapper<MFeature> defaultMapper;

	@Autowired
	public void setDataSource( DataSource dataSource ) {
	    this.simpleJdbcTemplate = new SimpleJdbcTemplate( dataSource );
	}

    @Override
    public MFeature getFeature(String transactionType, String providerCode) {
        logger.info("transactionType=" + transactionType + " providerCode=" + providerCode);
		try {
            StringBuilder sql = new StringBuilder()
                .append("select f.feature_code, f.feature_name, f.description ")
                .append("from m_feature f ")
                .append("where f.transaction_type = ? ");
            List<MFeature> features = simpleJdbcTemplate.query(sql.toString(), new RowMapper< MFeature >() {
					public MFeature mapRow( ResultSet rs, int rowNum ) throws SQLException {
						MFeature o = new MFeature();
						o.setFeatureCode(rs.getString("feature_code"));
						o.setFeatureName(rs.getString("feature_name"));
						o.setFeatureDescription(rs.getString("description"));
						return o;
					}
				}, transactionType);
            logger.info("features=" + features);
            if (features.size() == 1) {
                return features.get(0);
            } else if (features.size() > 1){
                sql = new StringBuilder()
                .append("select f.feature_code, f.feature_name, f.description ")
                .append("from m_feature f ")
                .append("where f.transaction_type = ? AND f.provider_code = ? ");
                return ( MFeature ) simpleJdbcTemplate.queryForObject(sql.toString(),
                        new RowMapper<MFeature>() {
                            public MFeature mapRow(ResultSet rs, int rowNum) throws SQLException {
                                MFeature o = new MFeature();
                                o.setFeatureCode(rs.getString("feature_code"));
                                o.setFeatureName(rs.getString("feature_name"));
                                o.setFeatureDescription(rs.getString("description"));
                                return o;
                            }
                        }, transactionType, providerCode
                );
            } else {
                return null;
            }

		}
		catch( Exception e ) {

			e.printStackTrace();
			System.out.println( "FeatureDaoImpl.getFeature() is null" );

		}

		return null;
    }

    public List<MFeature> getAll(){


        try {

            StringBuilder sql = new StringBuilder()
                .append("select f.feature_code, f.feature_name, f.description, f.transaction_type, f.provider_code ")
                .append("from m_feature f ");

            List<MFeature> features = simpleJdbcTemplate.query(sql.toString(), new RowMapper< MFeature >() {
					public MFeature mapRow( ResultSet rs, int rowNum ) throws SQLException {
						MFeature o = new MFeature();
						o.setFeatureCode(rs.getString("feature_code"));
						o.setFeatureName(rs.getString("feature_name"));
						o.setFeatureDescription(rs.getString("description"));
                        o.setTransactionType(rs.getString("transaction_type"));
                        o.setProviderCode(rs.getString("provider_code"));
						return o;
					}
				});

            return features;

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return new ArrayList<MFeature>();
    }

}
