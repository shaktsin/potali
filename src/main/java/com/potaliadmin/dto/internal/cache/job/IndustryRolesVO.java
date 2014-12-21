package com.potaliadmin.dto.internal.cache.job;

import com.potaliadmin.domain.industry.IndustryRoles;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class IndustryRolesVO {

  Long id;
  String name;
  Long industryId;

  public IndustryRolesVO(IndustryRoles industryRoles) {
    id = industryRoles.getId();
    name = industryRoles.getName();
    industryId = industryRoles.getIndustryId();
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

  public Long getIndustryId() {
    return industryId;
  }

  public void setIndustryId(Long industryId) {
    this.industryId = industryId;
  }

}
