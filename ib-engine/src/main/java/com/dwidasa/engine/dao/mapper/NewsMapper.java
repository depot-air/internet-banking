package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.News;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 2/13/12
 * Time: 8:11 PM
 */
@Component("newsMapper")
public class NewsMapper extends ChainedRowMapper<News> implements ParameterizedRowMapper<News> {
    public NewsMapper() {
    }

    @Override
    public News chainRow(ResultSet rs, int index) throws SQLException {
        News news = new News();

        news.setId(rs.getLong(++index));
        news.setDateTime(rs.getTimestamp(++index));
        news.setDay(rs.getString(++index));
        news.setTitle(rs.getString(++index));
        news.setPreview(rs.getString(++index));
        news.setContent(rs.getString(++index));
        news.setImage(rs.getString(++index));
        news.setCreated(rs.getTimestamp(++index));
        news.setCreatedby(rs.getLong(++index));
        news.setUpdated(rs.getTimestamp(++index));
        news.setUpdatedby(rs.getLong(++index));

        return news;
    }
}
