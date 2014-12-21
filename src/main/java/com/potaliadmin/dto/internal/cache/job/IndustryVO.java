package com.potaliadmin.dto.internal.cache.job;

import com.potaliadmin.domain.industry.Industry;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class IndustryVO {

  Long id;
  String name;

  public IndustryVO(Industry industry) {
    id = industry.getId();
    name = industry.getName();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
