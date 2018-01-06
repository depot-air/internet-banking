package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.IsoBitmapDao;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.IsoBitmapMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.IsoBitmap;
import com.dwidasa.engine.model.TransactionType;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/30/11
 * Time: 10:27 AM
 */
@Repository("isoBitmapDao")
public class IsoBitmapDaoImpl extends GenericDaoImpl<IsoBitmap, Long> implements IsoBitmapDao {
    private final ChainedRowMapper<TransactionType> transactionTypeMapper;

    @Autowired
    public IsoBitmapDaoImpl(DataSource dataSource, IsoBitmapMapper isoBitmapMapper,
                            TransactionTypeMapper transactionTypeMapper) {
        super("m_iso_bitmap", dataSource);
        defaultMapper = isoBitmapMapper;
        this.transactionTypeMapper = transactionTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into m_iso_bitmap ( ")
            .append("   m_transaction_type_id, mti, bitmap, custom, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :transactionTypeId, :mti, :bitmap, :custom, :created, :createdby, :updated, :updatedby")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_iso_bitmap ")
            .append("set ")
            .append("   m_transaction_type_id = :transactionTypeId, mti = :mti, bitmap = :bitmap, ")
            .append("   custom = :custom, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public List<IsoBitmap> getAllWithTransactionType() {
        StringBuilder sql = new StringBuilder()
                .append("select mib.*, mtt.* ")
                .append("from m_iso_bitmap mib ")
                .append("join m_transaction_type mtt ")
                .append("   on mtt.id = mib.m_transaction_type_id ");

        List<IsoBitmap> isoBitmaps =  getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<IsoBitmap>() {
            public IsoBitmap mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                IsoBitmap isoBitmap = defaultMapper.mapRow(rs, index);
                index += Constants.ISO_BITMAP_LENGTH;
                TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                isoBitmap.setTransactionType(transactionType);

                return isoBitmap;
            }
        });

        return isoBitmaps;
    }

	@Override
	public IsoBitmap getByTransactionType(String transactionType) {
		try {
            StringBuilder sql = new StringBuilder()
	            .append("select mib.*, mtt.* ")
	            .append("from m_iso_bitmap mib ")
	            .append("join m_transaction_type mtt ")
	            .append("   on mtt.id = mib.m_transaction_type_id ")
                .append("where mtt.transaction_type = ? ");
            List<IsoBitmap> bitmaps = getSimpleJdbcTemplate().query(sql.toString(), new RowMapper< IsoBitmap >() {
            	public IsoBitmap mapRow(ResultSet rs, int rowNum) throws SQLException {
                    int index = 0;
                    IsoBitmap isoBitmap = defaultMapper.mapRow(rs, index);
                    index += Constants.ISO_BITMAP_LENGTH;
                    TransactionType transactionType = transactionTypeMapper.chainRow(rs, index);
                    isoBitmap.setTransactionType(transactionType);

                    return isoBitmap;
				}
			}, transactionType);
            logger.info("bitmaps=" + bitmaps);
            if (bitmaps.size() >= 1) {
                return bitmaps.get(0);
            }            
		}
		catch( Exception e ) {
			e.printStackTrace();
			System.out.println( "IsoBitmapDaoImpl.getByTransactionType() is null" );
		}
		return null;
	}
}
