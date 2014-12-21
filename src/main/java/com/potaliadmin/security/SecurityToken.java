package com.potaliadmin.security;

import com.potaliadmin.constants.DefaultConstants;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class SecurityToken {

  private static final String appName = DefaultConstants.APP_NAME;
  private static final String separator = ":";


  public static String getSecurityToken(String email, String password, Long instituteId) {
    String digest = email + separator + appName+ separator+instituteId + separator +password;
    return Base64.encodeToString(digest.getBytes());
  }

  public static String[] getCredentials(String authToken) {
    String digest = Base64.decodeToString(authToken);
    return digest.split(separator);
  }


}
