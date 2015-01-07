package com.potaliadmin.dto.web.request.jobs;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.dto.web.request.framework.GenericRequest;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Shakti Singh on 1/6/15.
 */
public class JobSearchRequest extends GenericRequest {

  String locationFilter;
  String industryFilter;
  String rolesFilter;
  String salaryFilter;
  String experienceFilter;
  Long postId;
  int operation;
  int perPage;
  int pageNo;


  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    if (isValid && EnumSearchOperation.contains(operation) && postId == null) {
      isValid = false;
    }
    return isValid;
  }

  public String getLocationFilter() {
    return locationFilter;
  }

  public void setLocationFilter(String locationFilter) {
    this.locationFilter = locationFilter;
  }

  public String getIndustryFilter() {
    return industryFilter;
  }

  public void setIndustryFilter(String industryFilter) {
    this.industryFilter = industryFilter;
  }

  public String getRolesFilter() {
    return rolesFilter;
  }

  public void setRolesFilter(String rolesFilter) {
    this.rolesFilter = rolesFilter;
  }

  public String getSalaryFilter() {
    return salaryFilter;
  }

  public void setSalaryFilter(String salaryFilter) {
    this.salaryFilter = salaryFilter;
  }

  public String getExperienceFilter() {
    return experienceFilter;
  }

  public void setExperienceFilter(String experienceFilter) {
    this.experienceFilter = experienceFilter;
  }

  public int getPerPage() {
    if (perPage == 0) {
      return DefaultConstants.AND_APP_PER_PAGE;
    }
    return perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public int getPageNo() {
    if (pageNo == 0) {
      return DefaultConstants.AND_APP_PAGE_NO;
    }
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public int getOperation() {
    return operation;
  }

  public void setOperation(int operation) {
    this.operation = operation;
  }
}
