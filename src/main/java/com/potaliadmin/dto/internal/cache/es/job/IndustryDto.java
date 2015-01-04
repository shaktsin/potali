package com.potaliadmin.dto.internal.cache.es.job;

import java.util.List;

/**
 * Created by Shakti Singh on 12/26/14.
 */
public class IndustryDto {

  private Long id;
  private String name;
  private boolean selected;
  private List<IndustryRolesDto> industryRolesDtoList;

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

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public List<IndustryRolesDto> getIndustryRolesDtoList() {
    return industryRolesDtoList;
  }

  public void setIndustryRolesDtoList(List<IndustryRolesDto> industryRolesDtoList) {
    this.industryRolesDtoList = industryRolesDtoList;
  }
}
