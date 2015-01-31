package com.potaliadmin.dto.internal.cache.es.job;

/**
 * Created by shakti on 31/1/15.
 */
public class BaseRangeDto {

  private String name;

  public BaseRangeDto(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
