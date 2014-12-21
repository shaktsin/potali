package com.potaliadmin.web.listerners;

import com.potaliadmin.domain.address.Country;
import com.potaliadmin.framework.cache.NativeCacheManager;
import com.potaliadmin.impl.framework.ServiceLocatorFactory;
import com.potaliadmin.pact.framework.BaseDao;
import com.potaliadmin.pact.service.cache.AppCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * Created by Shakti Singh on 11/15/14.
 */
public class PotAdminStartupListener implements ServletContextListener {

  private Logger logger = LoggerFactory.getLogger(PotAdminStartupListener.class);


  AppCacheService appCacheService;



  public PotAdminStartupListener() {
  }

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    logger.info("=================== STARTING POTALI ADMIN ===================");
    getAppCacheService().reloadAll();
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    logger.info("=================== SHUTTING DOWN POTALI ADMIN ===================");
  }

  public AppCacheService getAppCacheService() {
    if (appCacheService == null) {
      appCacheService = ServiceLocatorFactory.getBean(AppCacheService.class);
    }
    return appCacheService;
  }
}
