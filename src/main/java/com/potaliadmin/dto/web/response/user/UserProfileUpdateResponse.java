package com.potaliadmin.dto.web.response.user;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

/**
 * Created by Shakti Singh on 1/2/15.
 */
public class UserProfileUpdateResponse extends GenericBaseResponse {

  private String accountName;
  private int gradYear;
  private String profileImageLink;


  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public int getGradYear() {
    return gradYear;
  }

  public void setGradYear(int gradYear) {
    this.gradYear = gradYear;
  }

  public String getProfileImageLink() {
    return profileImageLink;
  }

  public void setProfileImageLink(String profileImageLink) {
    this.profileImageLink = profileImageLink;
  }
}
