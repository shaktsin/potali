package com.potaliadmin.framework.cache.cluster;

import com.potaliadmin.dto.internal.cache.cluster.ClusterVO;
import com.potaliadmin.framework.cache.LocalCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shakti Singh on 1/10/15.
 */
public class ClusterCache implements LocalCache {

  private static ClusterCache _mInstance;
  private Map<String, List<ClusterVO>> activeClusterMapByType = new HashMap<String, List<ClusterVO>>();

  private ClusterCache() {}

  static {
    _mInstance = new ClusterCache();
  }

  public static ClusterCache getCache() {
    return _mInstance;
  }

  public void addClusterByType(String type, ClusterVO clusterVO) {
    List<ClusterVO> clusterVOList = activeClusterMapByType.get(type);
    if (clusterVOList == null) {
      clusterVOList = new ArrayList<ClusterVO>();
    }
    clusterVOList.add(clusterVO);
    activeClusterMapByType.put(type, clusterVOList);
  }

  public List<ClusterVO> getAllActiveClusterByType(String type) {
    return activeClusterMapByType.get(type);
  }

  @Override
  public void reset() {

  }

  @Override
  public void freeze() {
    _mInstance = this;
  }
}
