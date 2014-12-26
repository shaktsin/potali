package com.potaliadmin.impl.service.cache;

import com.potaliadmin.domain.address.City;
import com.potaliadmin.domain.industry.Industry;
import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.domain.institute.Institute;
import com.potaliadmin.dto.internal.cache.address.CityVO;
import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.dto.internal.cache.job.IndustryRolesVO;
import com.potaliadmin.dto.internal.cache.job.IndustryVO;
import com.potaliadmin.framework.cache.address.CityCache;
import com.potaliadmin.framework.cache.industry.IndustryCache;
import com.potaliadmin.framework.cache.industry.IndustryRolesCache;
import com.potaliadmin.framework.cache.institute.InstituteCache;
import com.potaliadmin.impl.framework.properties.AppProperties;
import com.potaliadmin.pact.framework.BaseDao;
import com.potaliadmin.pact.service.cache.AppCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    reloadIndustryCache();
    reloadCityCache();
    reloadIndustryRoles();

    logger.info("----------------- Local Cache Reloaded --------------------");
  }

  @SuppressWarnings("unchecked")
  private void reloadIndustryRoles() {
    String query = "select indR from IndustryRoles indR";
    IndustryRolesCache industryRolesCache = IndustryRolesCache.getCache();
    List<IndustryRoles> industryRolesList = getBaseDao().findByQuery(query);
    for (IndustryRoles industryRoles : industryRolesList) {
      IndustryRolesVO industryRolesVO = new IndustryRolesVO(industryRoles);
      industryRolesCache.addIndustryRoles(industryRolesVO.getId(), industryRolesVO);
    }
    industryRolesCache.freeze();
  }

  @SuppressWarnings("unchecked")
  private void reloadCityCache() {
    String query = "select city from City city";
    CityCache cityCache = CityCache.getCache();
    List<City> cityList = getBaseDao().findByQuery(query);
    for (City city : cityList) {
      CityVO cityVO = new CityVO(city);
      cityCache.addCity(cityVO.getId(), cityVO);
    }
    cityCache.freeze();
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

  @SuppressWarnings("unchecked")
  private void reloadIndustryCache() {
    String query = "select ind from Industry ind";
    IndustryCache industryCache = IndustryCache.getCache();
    List<Industry> industryList = getBaseDao().findByQuery(query);
    for (Industry industry : industryList) {
      IndustryVO industryVO = new IndustryVO(industry);
      industryCache.addIndustry(industryVO.getId(), industryVO);
    }
    industryCache.freeze();
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public AppProperties getAppProperties() {
    return appProperties;
  }
}
