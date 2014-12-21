package com.potaliadmin.dto.internal.cache.address;

import com.potaliadmin.domain.address.City;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class CityVO {

  Long id;
  String name;
  Long stateId;

  public CityVO(City city) {
    this.id = city.getId();
    this.name = city.getName();
    this.stateId = city.getStateId();
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

  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }
}
