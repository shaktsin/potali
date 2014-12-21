package com.potaliadmin.framework.cache.address;

import com.potaliadmin.dto.internal.cache.address.StateVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class StateCache implements LocalCache {

  private Map<Long, StateVO> stateVOMap = new HashMap<Long, StateVO>();
  private static StateCache _mInstance;

  private StateCache() {}

  static {
    _mInstance = new StateCache();
  }

  public static StateCache getCache() {
    return _mInstance;
  }

  public void addStateCache(Long id, StateVO stateVO) {
    this.stateVOMap.put(id, stateVO);
  }

  public StateVO getStateVO(Long id) {
    return this.stateVOMap.get(id);
  }


  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }
}
