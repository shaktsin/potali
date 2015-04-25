package com.potaliadmin.framework.cache.classified;

import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryVO;
import com.potaliadmin.dto.internal.cache.job.IndustryRolesVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.*;

/**
 * Created by shaktsin on 4/5/15.
 */
public class SecondaryCategoryCache implements LocalCache {


  private Map<Long, SecondaryCategoryVO> secondaryCategoryVOMap = new HashMap<Long, SecondaryCategoryVO>();
  private static SecondaryCategoryCache _mInstance;

  private SecondaryCategoryCache() {}

  static {
    _mInstance = new SecondaryCategoryCache();
  }

  public static SecondaryCategoryCache getCache() {
    return _mInstance;
  }

  public void addSecondaryCategory(Long id,SecondaryCategoryVO secondaryCategoryVO) {
    this.secondaryCategoryVOMap.put(id, secondaryCategoryVO);
  }


  public SecondaryCategoryVO getSecondaryCategoryVO(Long id) {
    return this.secondaryCategoryVOMap.get(id);
  }

  public List<SecondaryCategoryVO> getAllSecondaryCategoryVO() {
    List<SecondaryCategoryVO> secondaryCategoryVOList = new ArrayList<SecondaryCategoryVO>();
    for (Map.Entry<Long, SecondaryCategoryVO> instituteVOEntry : secondaryCategoryVOMap.entrySet()) {
      secondaryCategoryVOList.add(getSecondaryCategoryVO(instituteVOEntry.getKey()));
    }
    return secondaryCategoryVOList;
  }

  public boolean contains(Long id) {
    boolean exists = false;
    Set<Long> keySet = this.secondaryCategoryVOMap.keySet();
    if (keySet.contains(id)) {
      exists = true;
    }
    return exists;
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
