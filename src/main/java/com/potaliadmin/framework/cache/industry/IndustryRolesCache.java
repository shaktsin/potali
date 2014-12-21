package com.potaliadmin.framework.cache.industry;

import com.potaliadmin.domain.industry.IndustryRoles;
import com.potaliadmin.dto.internal.cache.job.IndustryRolesVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.*;

/**
 * Created by Shakti Singh on 12/17/14.
 */
public class IndustryRolesCache implements LocalCache {

  private Map<Long, IndustryRolesVO> industryRolesVOMap = new HashMap<Long, IndustryRolesVO>();
  private static IndustryRolesCache _mInstance;

  private IndustryRolesCache() {}

  static {
    _mInstance = new IndustryRolesCache();
  }

  public static IndustryRolesCache getCache() {
    return _mInstance;
  }

  public void addIndustry(Long id, IndustryRolesVO industryRolesVO) {
    this.industryRolesVOMap.put(id, industryRolesVO);
  }

  public void addIndustryRoles(Long id,IndustryRolesVO industryRolesVO) {
    this.industryRolesVOMap.put(id, industryRolesVO);
  }

  public IndustryRolesVO getIndustryRolesVO(Long id) {
    return this.industryRolesVOMap.get(id);
  }

  public List<IndustryRolesVO> getAllIndustryVO() {
    List<IndustryRolesVO> industryRolesVOList = new ArrayList<IndustryRolesVO>();
    for (Map.Entry<Long, IndustryRolesVO> instituteVOEntry : industryRolesVOMap.entrySet()) {
      industryRolesVOList.add(getIndustryRolesVO(instituteVOEntry.getKey()));
    }
    return industryRolesVOList;
  }

  public boolean contains(Long id) {
    boolean exists = false;
    Set<Long> keySet = this.industryRolesVOMap.keySet();
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

  public boolean isValidRoles(long industryRolesId, long industryId) {
    boolean valid = false;
    IndustryRolesVO industryRolesVO = getIndustryRolesVO(industryRolesId);
    if (industryRolesVO == null) {
      return false;
    }
    if (industryRolesVO.getIndustryId().equals(industryId)) {
      valid = true;
    }

    return valid;
  }



  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }
}
