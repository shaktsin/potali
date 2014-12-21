package com.potaliadmin.impl.service.cache;

import com.potaliadmin.domain.institute.Institute;
import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.framework.cache.institute.InstituteCache;
import com.potaliadmin.impl.framework.properties.AppProperties;
import com.potaliadmin.pact.framework.BaseDao;
import com.potaliadmin.pact.service.cache.AppCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Shakti Singh on 12/14/14.
 */
@Service
public class AppCacheServiceImpl implements AppCacheService {

  private Logger logger = LoggerFactory.getLogger(AppCacheServiceImpl.class);

  @Autowired
  BaseDao baseDao;

  @Autowired
  AppProperties appProperties;

  @Override
  public void reloadAll() {
    logger.info("----------------- Reloading All Cache --------------------");
    reloadInstituteCache();

    logger.info("----------------- Local Cache Reloaded --------------------");
  }

  @SuppressWarnings("unchecked")
  private void reloadInstituteCache() {
    boolean isProd = getAppProperties().isProd();
    String query = "select inst from Institute inst";
    if (isProd) {
      query = "select inst from Institute inst where inst.type = 0";
    }

    InstituteCache instituteCache = InstituteCache.getCache();
    List<Institute> institutes = getBaseDao().findByQuery(query);
    for (Institute institute : institutes) {
      InstituteVO instituteVO = new InstituteVO(institute);
      instituteCache.addInstitute(instituteVO.getId(), instituteVO);
    }
    instituteCache.freeze();
  }

  private void reloadCountryCache() {

  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public AppProperties getAppProperties() {
    return appProperties;
  }
}
