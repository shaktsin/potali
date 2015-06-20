package com.potaliadmin.dto.internal.cache.classified;

/**
 * Created by shaktsin on 4/5/15.
 */
public class SecondaryCategoryDto {

  private Long id;
  private String name;
  private Boolean selected;
  private Long primaryCatId;
  private String primaryCatName;

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

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

  public Long getPrimaryCatId() {
    return primaryCatId;
  }

  public void setPrimaryCatId(Long primaryCatId) {
    this.primaryCatId = primaryCatId;
  }

  public String getPrimaryCatName() {
    return primaryCatName;
  }

  public void setPrimaryCatName(String primaryCatName) {
    this.primaryCatName = primaryCatName;
  }
}
