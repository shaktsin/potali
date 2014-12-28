package com.potaliadmin.dto.internal.cache.es.framework;

/**
 * Created by Shakti Singh on 12/28/14.
 */
public class GenericVO {

  private Long id;

  public GenericVO() {
  }

  public GenericVO(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
