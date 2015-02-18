package com.potaliadmin.dto.web.request.user;

import com.potaliadmin.dto.internal.image.ImageDto;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

import java.util.List;

/**
 * Created by Shakti Singh on 1/2/15.
 */
public class UserProfileUpdateRequest extends GenericRequest {

  private String firstName;
  private String lastName;
  private String accountName;
  private Integer yearOfGrad;
  //private List<ImageDto> imageDtoList;

  public UserProfileUpdateRequest() {
  }

  public UserProfileUpdateRequest(Long plateFormId, String appName) {
    super(plateFormId, appName);
  }

  @Override
  public boolean validate() {
    return super.validate();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public Integer getYearOfGrad() {
    return yearOfGrad;
  }

  public void setYearOfGrad(Integer yearOfGrad) {
    this.yearOfGrad = yearOfGrad;
  }

}
