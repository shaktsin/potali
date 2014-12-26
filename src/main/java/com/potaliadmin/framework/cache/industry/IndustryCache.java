package com.potaliadmin.framework.cache.industry;

import com.potaliadmin.dto.internal.cache.job.IndustryVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.*;

/**
 * Created by Shakti Singh on 12/17/14.
 */
public class IndustryCache implements LocalCache {

  private Map<Long, IndustryVO> industryVOMap = new HashMap<Long, IndustryVO>();
  private static IndustryCache _mInstance;

  private IndustryCache() {}

  static {
    _mInstance = new IndustryCache();
  }

  public void addIndustry(Long id, IndustryVO industryVO) {
    this.industryVOMap.put(id, industryVO);
  }

  public IndustryVO getIndustryVO(Long id) {
    return this.industryVOMap.get(id);
  }

  public static IndustryCache getCache() {
    return _mInstance;
  }

  public boolean contains(Long id) {
    boolean exists = false;
    Set<Long> keySet = this.industryVOMap.keySet();
    if (keySet.contains(id)) {
      exists = true;
    }
    return exists;
  }

  public List<IndustryVO> getAllIndustryVO() {
    List<IndustryVO> industryVOList = new ArrayList<IndustryVO>();
    for (Map.Entry<Long, IndustryVO> instituteVOEntry : industryVOMap.entrySet()) {
      industryVOList.add(getIndustryVO(instituteVOEntry.getKey()));
    }
    return industryVOList;
  }

  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }
}
