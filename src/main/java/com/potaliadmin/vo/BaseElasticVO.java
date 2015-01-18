package com.potaliadmin.vo;

import java.util.List;

/**
 * Created by Shakti Singh on 1/16/15.
 */
public class BaseElasticVO {

  private Long id;
  private String parentId;


  public BaseElasticVO() {

  }

  public BaseElasticVO(Long id) {
    this.id = id;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }
}
