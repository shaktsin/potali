package com.potaliadmin.dto.web.request.user;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shaktsin on 3/22/15.
 */
public class UserChangePasswordRequest extends GenericRequest {

  String password;
  String rePassword;

  @Override
  public boolean validate() {
    boolean valid = super.validate();
    if (valid && StringUtils.isBlank(password)) {
      valid = false;
    }
    if (valid && StringUtils.isBlank(rePassword)) {
      valid = false;
    }
    if (valid && rePassword.equals(password)) {
      valid = false;
    }
    return valid;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRePassword() {
    return rePassword;
  }

  public void setRePassword(String rePassword) {
    this.rePassword = rePassword;
  }
}
