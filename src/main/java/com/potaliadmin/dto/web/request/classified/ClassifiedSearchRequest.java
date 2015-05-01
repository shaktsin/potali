package com.potaliadmin.dto.web.request.classified;

import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shaktsin on 4/30/15.
 */
public class ClassifiedSearchRequest extends GenericRequest {

  String locationFilter;
  String primaryCatFilter;
  String secondaryCatFilter;
  String circleFilter;
  Long postId;
  int operation;
  int perPage = 8;
  int pageNo = 0;


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

  public String getPrimaryCatFilter() {
    return primaryCatFilter;
  }

  public void setPrimaryCatFilter(String primaryCatFilter) {
    this.primaryCatFilter = primaryCatFilter;
  }

  public String getSecondaryCatFilter() {
    return secondaryCatFilter;
  }

  public void setSecondaryCatFilter(String secondaryCatFilter) {
    this.secondaryCatFilter = secondaryCatFilter;
  }

  public String getCircleFilter() {
    return circleFilter;
  }

  public void setCircleFilter(String circleFilter) {
    this.circleFilter = circleFilter;
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

  public int getPerPage() {
    return perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }
}
