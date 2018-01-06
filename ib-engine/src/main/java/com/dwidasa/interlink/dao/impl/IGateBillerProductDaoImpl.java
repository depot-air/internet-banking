package com.dwidasa.interlink.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dwidasa.interlink.dao.IGateBillerProductDao;
import com.dwidasa.interlink.model.MFeature;
import com.dwidasa.interlink.model.MIGateBillerProduct;

@Repository( "iGateBillerProductDao" )
public class IGateBillerProductDaoImpl implements IGateBillerProductDao {
	private static Logger logger = Logger.getLogger( IGateBillerProductDaoImpl.class );
	private SimpleJdbcTemplate simpleJdbcTemplate;
    protected RowMapper<MFeature> defaultMapper;

	@Autowired
	public void setDataSource( DataSource dataSource ) {
	    this.simpleJdbcTemplate = new SimpleJdbcTemplate( dataSource );
	}

	@Override
	public MIGateBillerProduct getIGateBillerProduct(String billerCode, String productCode) {
		logger.info("billerCode=" + billerCode + " productCode=" + productCode);
		try {
			StringBuilder sql = new StringBuilder()
            .append("select i.biller_code, i.product_code, i.industrial_code, i.biller_code_igate, i.product_code_igate ")
            .append("from m_igate_biller_product i ")
            .append("where i.biller_code = ? AND i.product_code = ? ");
            return ( MIGateBillerProduct ) simpleJdbcTemplate.queryForObject(sql.toString(),
                    new RowMapper<MIGateBillerProduct>() {
                        public MIGateBillerProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                        	MIGateBillerProduct o = new MIGateBillerProduct();
                            o.setBillerCode(rs.getString("biller_code"));
                            o.setProductCode(rs.getString("product_code"));
                            o.setIndustrialCode(rs.getString("industrial_code"));
                            o.setiGateBillerCode(rs.getString("biller_code_igate"));
                            o.setiGateProductCode(rs.getString("product_code_igate"));
                            return o;
                        }
                    }, billerCode, productCode
            );            
		}
		catch( Exception e ) {

			e.printStackTrace();
			System.out.println( "IGateBillerProductDaoImpl.getIGateBillerProduct() is null" );

		}

		return null;
	}

    @Override
    public List<MIGateBillerProduct> getAll() {

        try {
			StringBuilder sql = new StringBuilder()
            .append("select i.biller_code, i.product_code, i.industrial_code, i.biller_code_igate, i.product_code_igate ")
            .append("from m_igate_biller_product i ");

            return (List<MIGateBillerProduct>) simpleJdbcTemplate.query(sql.toString(),
                    new RowMapper<MIGateBillerProduct>() {
                        public MIGateBillerProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
                        	MIGateBillerProduct o = new MIGateBillerProduct();
                            o.setBillerCode(rs.getString("biller_code"));
                            o.setProductCode(rs.getString("product_code"));
                            o.setIndustrialCode(rs.getString("industrial_code"));
                            o.setiGateBillerCode(rs.getString("biller_code_igate"));
                            o.setiGateProductCode(rs.getString("product_code_igate"));
                            return o;
                        }
                    }
            );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}

		return new ArrayList<MIGateBillerProduct>();
	}

}
