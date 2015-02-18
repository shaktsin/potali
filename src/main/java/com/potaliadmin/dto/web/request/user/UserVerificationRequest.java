package com.potaliadmin.dto.web.request.user;

import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shakti on 19/2/15.
 */
public class UserVerificationRequest extends GenericRequest {

  public int token;

  public int getToken() {
    return token;
  }

  public void setToken(int token) {
    this.token = token;
  }
}
