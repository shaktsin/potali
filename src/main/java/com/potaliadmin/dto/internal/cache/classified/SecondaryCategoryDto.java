package com.potaliadmin.dto.internal.cache.classified;

/**
 * Created by shaktsin on 4/5/15.
 */
public class SecondaryCategoryDto {

  private Long id;
  private String name;
  private Boolean selected;

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
}
