package com.potaliadmin.vo;

/**
 * Created by Shakti Singh on 1/16/15.
 */
public abstract class BaseElasticVO {

  private Long id;

  protected BaseElasticVO(Long id) {
    this.id = id;
  }

  public abstract String getType();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
