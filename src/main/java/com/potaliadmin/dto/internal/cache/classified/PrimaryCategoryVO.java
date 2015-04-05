package com.potaliadmin.dto.internal.cache.classified;

import com.potaliadmin.domain.classified.PrimaryCategory;

/**
 * Created by shaktsin on 4/5/15.
 */
public class PrimaryCategoryVO {

  Long id;
  String name;

  public PrimaryCategoryVO(PrimaryCategory primaryCategory) {
    id = primaryCategory.getId();
    name = primaryCategory.getName();
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
