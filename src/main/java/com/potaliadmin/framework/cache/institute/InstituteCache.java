package com.potaliadmin.framework.cache.institute;

import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shakti Singh on 12/14/14.
 */
public class InstituteCache implements LocalCache {

  private Map<Long, InstituteVO> instituteVOMap = new HashMap<Long, InstituteVO>();
  private static InstituteCache _mInstance;

  private InstituteCache() {}

  static {
    _mInstance = new InstituteCache();
  }

  public static InstituteCache getCache() {
    return _mInstance;
  }

  public void addInstitute(Long id, InstituteVO instituteVO) {
    this.instituteVOMap.put(id, instituteVO);
  }

  public InstituteVO getInstitute(Long id) {
    return this.instituteVOMap.get(id);
  }

  public List<InstituteVO> getAllInstitute() {
    List<InstituteVO> instituteVOList = new ArrayList<InstituteVO>();
    for (Map.Entry<Long, InstituteVO> instituteVOEntry : instituteVOMap.entrySet()) {
      instituteVOList.add(getInstitute(instituteVOEntry.getKey()));
    }
    return instituteVOList;
  }

  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }
}
