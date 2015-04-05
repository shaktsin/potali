package com.potaliadmin.dto.internal.cache.classified;

import com.potaliadmin.domain.classified.SecondaryCategory;

/**
 * Created by shaktsin on 4/5/15.
 */
public class SecondaryCategoryVO {

  Long id;
  String name;
  Long primaryCategoryId;

  public SecondaryCategoryVO(SecondaryCategory secondaryCategory) {
    this.id = secondaryCategory.getId();
    this.name = secondaryCategory.getName();
    this.primaryCategoryId = secondaryCategory.getPrimaryCategoryId();
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

  public Long getPrimaryCategoryId() {
    return primaryCategoryId;
  }

  public void setPrimaryCategoryId(Long primaryCategoryId) {
    this.primaryCategoryId = primaryCategoryId;
  }
}
