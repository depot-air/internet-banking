package com.dwidasa.engine.dao.impl;

import java.util.Date;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.HousekeepingDao;
import com.dwidasa.engine.util.EngineUtils;

@Repository("housekeepingDao")
public class HousekeepingDaoImpl extends JdbcDaoSupport implements HousekeepingDao {
	@Autowired
	public HousekeepingDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

	@Override
	public void runHousekeeping(int days) {
		Date limitDate = new DateTime().minusDays(days).toDate();
		String yearMonth = EngineUtils.fmtDateYM.print(new DateTime(limitDate.getTime())); 
		String sqlDelete;

//		sqlDelete = "TRUNCATE temp_t_transaction";
//		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from h_balance where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from h_periodic_task where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from h_periodic_process where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
//		sqlDelete = "delete from h_stats where year_month < ?";
//		getJdbcTemplate().update(sqlDelete, new Object[] {yearMonth});
		
		sqlDelete = "delete from h_transaction where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from t_activity_customer where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
//		sqlDelete = "delete from t_activity_user where created < ?";
//		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from t_audit_log where activity_date < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
//		sqlDelete = "delete from t_email where created < ?";
//		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		     	
		sqlDelete = "delete from t_transaction_data where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from t_transaction_queue where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});

		sqlDelete = "delete from t_transaction_stage where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});

		sqlDelete = "delete from t_treasury_stage where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from t_transaction where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from t_transfer_batch_content where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});
		
		sqlDelete = "delete from t_transfer_batch where created < ?";
		getJdbcTemplate().update(sqlDelete, new Object[] {limitDate});

		
	}

}
