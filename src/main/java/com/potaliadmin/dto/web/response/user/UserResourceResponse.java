package com.potaliadmin.dto.web.response.user;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class UserResourceResponse extends GenericBaseResponse {

  private Long userId;
  private String name;
  private String email;
  private String authToken;
  private String image;
  private boolean verified;
  private String firstName;
  private String lastName;
  private int yearOfGrad;
  //private JobSearchResponse jobSearchResponse;


  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
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

  public int getYearOfGrad() {
    return yearOfGrad;
  }

  public void setYearOfGrad(int yearOfGrad) {
    this.yearOfGrad = yearOfGrad;
  }
}
