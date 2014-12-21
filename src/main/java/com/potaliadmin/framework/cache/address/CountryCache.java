package com.potaliadmin.framework.cache.address;

import com.potaliadmin.dto.internal.cache.address.CountryVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class CountryCache implements LocalCache {

  private Map<Long, CountryVO> countryVOMap = new HashMap<Long, CountryVO>();
  private static CountryCache _mInstance;

  private CountryCache() {}

  static {
    _mInstance = new CountryCache();
  }

  public static CountryCache getCache() {
    return _mInstance;
  }

  public void addCountry(Long id, CountryVO countryVO) {
    this.countryVOMap.put(id, countryVO);
  }

  public CountryVO getCountryVO(Long id) {
    return this.countryVOMap.get(id);
  }

  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }
}
