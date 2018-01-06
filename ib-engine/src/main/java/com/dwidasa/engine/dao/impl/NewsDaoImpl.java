package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.NewsDao;
import com.dwidasa.engine.dao.mapper.NewsMapper;
import com.dwidasa.engine.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 2/13/12
 * Time: 8:08 PM
 */
@Repository("newsDao")
public class NewsDaoImpl extends GenericDaoImpl<News, Long> implements NewsDao {
    @Autowired
    public NewsDaoImpl(DataSource dataSource, NewsMapper newsMapper) {
        super("m_news", dataSource);
        this.defaultMapper = newsMapper;

        insertSql = new StringBuilder()
            .append("insert into m_news ( ")
            .append("   dateTime, day, title, preview, content, ")
            .append("   image, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :id, :dateTime, :day, :title, :preview, :content, ")
            .append("   :image, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_news ")
            .append("set ")
            .append("   dateTime = :dateTime, day = :day, ")
            .append("   title = :title, title = :title, preview = :preview, ")
            .append("   content = :content, image = :image, created = :created, :created = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }
        /*
        @Override
        public List<News> getLastN(int n) {
            StringBuilder sql = new StringBuilder()
                    .append("select nw.* ")
                    .append("from (")
                    .append("   select n.* ")
                    .append("   from m_news n ")
                    .append("   order by id desc ")
                    .append("   limit ? ")
                    .append(") nw ")
                    .append("order by nw.id ");

            return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, n);
        }

    */
}
