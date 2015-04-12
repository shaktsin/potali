package com.potaliadmin.dto.web.request.search;

import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by shaktsin on 4/12/15.
 */
public class SearchRequest extends GenericRequest {

  private String token;

  @Override
  public boolean validate() {
    boolean valid = super.validate();
    if (valid && token.length() < 2) {
      valid = false;
    }
    return valid;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
