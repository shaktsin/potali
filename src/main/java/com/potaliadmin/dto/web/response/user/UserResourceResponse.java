package com.potaliadmin.dto.web.response.user;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class UserResourceResponse extends GenericBaseResponse {

  private String name;
  private String email;
  private String authToken;
  private String image;
  private boolean verified;
  //private JobSearchResponse jobSearchResponse;


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
}
