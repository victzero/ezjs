package me.ezjs.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    protected final Log log = LogFactory.getLog(getClass());

    private static ApplicationContext applicationContext;

    private final static String[] DAOSUFFIX = {"DaoImpl", "Dao"};

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
        log.debug("成功初始化 SpringContextUtil 工具类.");
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 从spring中获取bean.
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static Object getDao(String className) {
        Object obj = null;
        for (String suffix : DAOSUFFIX) {
            try {
                obj = applicationContext.getBean(className + suffix);
            } catch (NoSuchBeanDefinitionException e) {
                // Do Nothing.
            }
            if (obj != null) {
                break;
            }
        }
        return obj;
    }
}
