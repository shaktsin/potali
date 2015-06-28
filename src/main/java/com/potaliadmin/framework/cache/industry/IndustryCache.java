package com.potaliadmin.framework.cache.industry;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryVO;
import com.potaliadmin.dto.internal.cache.job.IndustryVO;
import com.potaliadmin.framework.cache.LocalCache;
import com.potaliadmin.framework.cache.classified.SecondaryCategoryCache;

import java.util.*;

/**
 * Created by Shakti Singh on 12/17/14.
 */
public class IndustryCache implements LocalCache {

  private Map<Long, IndustryVO> industryVOMap = new HashMap<Long, IndustryVO>();
  private Map<Long, List<Long>> industryToIndustryRoles= new HashMap<Long, List<Long>>();
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



  public void addToIndustryToIndustryRolesMap(Long industryId, Long industryRolesId) {
    List<Long> rolesList = industryToIndustryRoles.get(industryId);
    if (rolesList == null || rolesList.size() == 0) {
      rolesList = new ArrayList<Long>();
    }
    rolesList.add(industryRolesId);
    industryToIndustryRoles.put(industryId, rolesList);
  }

  public long getOtherFromParent(Long industryId) {
    for (long ind : industryToIndustryRoles.get(industryId)) {
      SecondaryCategoryVO secondaryCategoryVO = SecondaryCategoryCache.getCache().getSecondaryCategoryVO(ind);
      if (secondaryCategoryVO.getName().equals(DefaultConstants.DEFAULT_OTHER_FILTER)) {
        return secondaryCategoryVO.getId();
      }
    }
    //return pcToSecondaryCategories.get(industryId);
    return DefaultConstants.DEFAULT_FILTER;
  }

  public void addToIndustryToIndustryRolesMap(Long industryId, List<Long> indRolesList) {
    List<Long> rolesList = industryToIndustryRoles.get(industryId);
    if (rolesList == null || rolesList.size() == 0) {
      rolesList = new ArrayList<Long>();
    }
    rolesList.addAll(indRolesList);
    industryToIndustryRoles.put(industryId, rolesList);
  }

  public List<Long> getIndustryRolesListFromIndustryId(Long industryId) {
    return industryToIndustryRoles.get(industryId);
  }

  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }
}
