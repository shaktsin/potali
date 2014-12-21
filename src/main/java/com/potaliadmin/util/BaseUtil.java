package com.potaliadmin.util;

import com.potaliadmin.constants.DefaultConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Shakti Singh on 10/6/14.
 */
public class BaseUtil {

  public static boolean isValidEmail(String email) {
    try {
      String emailRegEx = "^([A-Za-z0-9_%+-]\\.?)+@([A-Za-z0-9-]\\.?)*[A-Za-z0-9-]+\\.[A-Za-z]{2,4}$";
      Pattern p = Pattern.compile(emailRegEx);
      Matcher m = p.matcher(email);

      if (!m.find()) {
        return false;
      }
    } catch (PatternSyntaxException e) {
      return false;
    }
    return true;
  }

  public static boolean isValidPhone(String phone) {
    boolean valid = false;
    if (StringUtils.isNumeric(phone)) {
      valid = true;
    }
    if (valid && phone.length() == 10) {
      valid = true;
    }
    return valid;
  }

  public static String passwordEncrypt(String password) {
    return new Md5Hash(password, DefaultConstants.passwordSalt, DefaultConstants.hashIterations).toBase64();
  }
}
