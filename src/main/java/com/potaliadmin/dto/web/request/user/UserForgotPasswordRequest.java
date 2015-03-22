package com.potaliadmin.dto.web.request.user;

import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shaktsin on 3/22/15.
 */
public class UserForgotPasswordRequest extends GenericRequest {

  String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
