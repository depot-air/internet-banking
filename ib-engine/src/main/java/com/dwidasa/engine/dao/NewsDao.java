package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.News;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 2/13/12
 * Time: 7:58 PM
 */
public interface NewsDao extends GenericDao<News, Long> {

	/**
     * Get all news last N
     * @return list of news
     */
    //public List<News> getLastN(int n);
}
