package com.potaliadmin.dto.internal.cache.cluster;

import com.potaliadmin.domain.cluster.CacheCluster;

import java.io.Serializable;

/**
 * Created by Shakti Singh on 1/10/15.
 */
public class ClusterVO implements Serializable {

  private Long id;
  private String host;
  private String port;
  private String type;
  private boolean active;
  private boolean primary;

  public ClusterVO(CacheCluster cacheCluster) {
    this.id = cacheCluster.getId();
    this.host = cacheCluster.getHost();
    this.port = cacheCluster.getPort();
    this.type = cacheCluster.getType();
    this.active = cacheCluster.isActive();
    this.primary = cacheCluster.isPrimary();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(boolean primary) {
    this.primary = primary;
  }
}
