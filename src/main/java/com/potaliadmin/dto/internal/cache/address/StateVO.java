package com.potaliadmin.dto.internal.cache.address;

import com.potaliadmin.domain.address.State;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class StateVO {

  Long id;
  String name;
  Long countryId;

  public StateVO(State state) {
    this.id = state.getId();
    this.name = state.getName();
    this.countryId = state.getCountryId();
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

  public Long getCountryId() {
    return countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }
}
