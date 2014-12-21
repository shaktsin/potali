package com.potaliadmin.impl.framework;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by Shakti Singh on 12/14/14.
 */
public class ServiceLocatorFactory implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public synchronized void setApplicationContext(ApplicationContext appContext) throws BeansException {
    applicationContext = appContext;
  }

  public static synchronized ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(String beanName, Class<T> interfaceClass){
    return (T)applicationContext.getBean(beanName, interfaceClass);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(Class<T> interfaceClass){
    return (T)applicationContext.getBean(interfaceClass);
  }


}
