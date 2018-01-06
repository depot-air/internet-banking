package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.NewsDao;
import com.dwidasa.engine.model.News;
import com.dwidasa.engine.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 2/13/12
 * Time: 9:46 PM
 */
@Service("newsService")
public class NewsServiceImpl extends GenericServiceImpl<News, Long> implements NewsService {
    @Autowired
    public NewsServiceImpl(NewsDao newsDao) {
        super(newsDao);
    }
}
