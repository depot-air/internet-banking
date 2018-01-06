package com.dwidasa.engine.listener;

import com.dwidasa.engine.service.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Initializing global cache for populating drop down list across the application.
 *
 * @author rk
 */
public class StartupListener implements ServletContextListener {
    /**
     * {@inheritDoc}
     */
    public void contextInitialized(ServletContextEvent sce) {
        setupContext(sce.getServletContext());
    }

    private void setupContext(ServletContext context) {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

        CacheManager cacheManager = (CacheManager) ctx.getBean("cacheManager");
        cacheManager.initialiazeCache();
    }

    /**
     * {@inheritDoc}
     */
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
