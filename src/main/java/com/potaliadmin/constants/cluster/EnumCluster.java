package com.potaliadmin.constants.cluster;

/**
 * Created by Shakti Singh on 1/10/15.
 */
public enum EnumCluster {

  MEMCAHCE("memcache"),
  ELASTIC_SEARCH("es");

  private String name;

  EnumCluster(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
