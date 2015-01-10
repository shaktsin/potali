package com.potaliadmin.domain.cluster;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Shakti Singh on 1/10/15.
 */
@Entity
@Table(name = "cache_cluster")
@NamedQueries({
    @NamedQuery(name = "findAllActiveCluster", query = "select c from CacheCluster c where c.active = true"),
    @NamedQuery(name = "findAllActiveClusterByType", query = "select c from CacheCluster c where c.active = true and c.type = :type")
})
public class CacheCluster implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "host",nullable = false)
  private String host;

  @Column(name = "port",nullable = false)
  private String port;

  @Column(name = "type",nullable = false)
  private String type;

  @Column(name = "primary",nullable = false)
  private boolean primary;

  @Column(name = "active",nullable = false)
  private boolean active;


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

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(boolean primary) {
    this.primary = primary;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
