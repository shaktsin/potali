package com.potaliadmin.dto.internal.cache.es.job;

/**
 * Created by Shakti Singh on 12/22/14.
 */
public class IndustryRolesDto {

  private Long id;
  private String name;
  private Long industryId;
  private String industryName;
  private boolean selected;

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

  public Long getIndustryId() {
    return industryId;
  }

  public void setIndustryId(Long industryId) {
    this.industryId = industryId;
  }

  public String getIndustryName() {
    return industryName;
  }

  public void setIndustryName(String industryName) {
    this.industryName = industryName;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
