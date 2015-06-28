package com.potaliadmin.framework.cache.classified;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryVO;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryVO;
import com.potaliadmin.dto.internal.cache.job.IndustryVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.*;

/**
 * Created by shaktsin on 4/5/15.
 */
public class PrimaryCategoryCache implements LocalCache {

  private Map<Long, PrimaryCategoryVO> primaryCategoryVOMap = new HashMap<Long, PrimaryCategoryVO>();
  private Map<Long, List<Long>> pcToSecondaryCategories= new HashMap<Long, List<Long>>();
  private static PrimaryCategoryCache _mInstance;

  private PrimaryCategoryCache() {}

  static {
    _mInstance = new PrimaryCategoryCache();
  }

  public void addPrimaryCategory(Long id, PrimaryCategoryVO primaryCategoryVO) {
    this.primaryCategoryVOMap.put(id, primaryCategoryVO);
  }

  public PrimaryCategoryVO getPrimaryCategoryVO(Long id) {
    return this.primaryCategoryVOMap.get(id);
  }

  public boolean contains(Long id) {
    boolean exists = false;
    Set<Long> keySet = this.primaryCategoryVOMap.keySet();
    if (keySet.contains(id)) {
      exists = true;
    }
    return exists;
  }

  public List<PrimaryCategoryVO> getAllPrimaryCategoryVO() {
    List<PrimaryCategoryVO> industryVOList = new ArrayList<PrimaryCategoryVO>();
    for (Map.Entry<Long, PrimaryCategoryVO> instituteVOEntry : primaryCategoryVOMap.entrySet()) {
      industryVOList.add(getPrimaryCategoryVO(instituteVOEntry.getKey()));
    }
    return industryVOList;
  }

  public void mapSecondaryCatToPrimaryCategory(Long pcId, List<Long> scIds) {
    List<Long> scList = pcToSecondaryCategories.get(pcId);
    if (scList == null || scList.size() == 0) {
      scList = new ArrayList<Long>();
    }
    scList.addAll(scIds);
    pcToSecondaryCategories.put(pcId, scList);
  }

  public List<Long> getSecondaryCategoriesFromPC(Long industryId) {
    return pcToSecondaryCategories.get(industryId);
  }

  public long getOtherFromParent(Long industryId) {
    for (long ind : pcToSecondaryCategories.get(industryId)) {
      SecondaryCategoryVO secondaryCategoryVO = SecondaryCategoryCache.getCache().getSecondaryCategoryVO(ind);
      if (secondaryCategoryVO.getName().equals(DefaultConstants.DEFAULT_OTHER_FILTER)) {
        return secondaryCategoryVO.getId();
      }
    }
    //return pcToSecondaryCategories.get(industryId);
    return DefaultConstants.DEFAULT_FILTER;
  }

  public static PrimaryCategoryCache getCache() {
    return _mInstance;
  }


  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }


}
