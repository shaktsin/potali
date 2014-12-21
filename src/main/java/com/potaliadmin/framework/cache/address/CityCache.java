package com.potaliadmin.framework.cache.address;

import com.potaliadmin.dto.internal.cache.address.CityVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.*;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class CityCache implements LocalCache {

  private Map<Long, CityVO> cityVOMap = new HashMap<Long, CityVO>();
  private static CityCache _mInstance;

  private CityCache() {}

  static {
    _mInstance = new CityCache();
  }

  public static CityCache getCache() {
    return _mInstance;
  }

  public void addCity(Long id , CityVO cityVO) {
    this.cityVOMap.put(id, cityVO);
  }

  public CityVO getCity(Long id) {
    return this.cityVOMap.get(id);
  }

  public boolean contains(Long id) {
    boolean exists = false;
    Set<Long> keySet = this.cityVOMap.keySet();
    if (keySet.contains(id)) {
      exists = true;
    }
    return exists;
  }

  public List<CityVO> getCityVO() {
    List<CityVO> cityVOList = new ArrayList<CityVO>();
    for (Map.Entry<Long, CityVO> instituteVOEntry : cityVOMap.entrySet()) {
      cityVOList.add(getCity(instituteVOEntry.getKey()));
    }
    return cityVOList;
  }

  public boolean isValidList(List<Long> idList) {
    boolean isValid = true;
    for (Long id : idList) {
      isValid = contains(id);
      if (!isValid) {
        break;
      }
    }
    return isValid;
  }

  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }
}
