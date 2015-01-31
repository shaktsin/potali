package com.potaliadmin.dto.internal.filter;

/**
 * Created by shakti on 31/1/15.
 */
public class BaseFilterDto {

  private String name;

  public BaseFilterDto(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
