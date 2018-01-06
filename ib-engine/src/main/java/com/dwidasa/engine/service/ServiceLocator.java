package com.dwidasa.engine.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * uses this class (by all @ManagedBean or Manager classes) to get reference to specified bean.
 * it does not used springframework @Autowired in each @ManagedBean
 * since @ManagedBean usually exist for a very short period of time
 * hence we will not make springframework busy and do abused initialization
 * <p/>
 * add configuration in springframework applicationContext.xml
 * <bean id="serviceLocator" class="com.dwidasa.ibanking.service.ServiceLocator"
 *
 * @author prayugo
 */
public class ServiceLocator implements ApplicationContextAware {

    /**
     * cached springframework ApplicationContext
     */
    private static ApplicationContext applicationContext;

    /**
     * loaded during springframework initialization
     */
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        applicationContext = ctx;
    }

    /**
     * get access to the springframework ApplicationContext from anywhere in application.
     *
     * @return springfraework ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * same functionalities as @Autowired, it will get the reference to specified bean
     *
     * @param argBeanName bean identifier as registered into springframework applicationContext
     * @return the reference to specified bean name or bean identifier
     */
    public static Object getService(String argBeanName) {
        return applicationContext.getBean(argBeanName);
    }
}
