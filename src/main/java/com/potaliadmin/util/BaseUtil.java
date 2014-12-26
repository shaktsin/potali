package com.potaliadmin.util;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.exceptions.PotaliRuntimeException;
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

  public static Long[] convertToLong(String[] ids) {
    if (ids == null || ids.length == 0) {
      return null;
    }
    Long[] data = new Long[ids.length];
    for (int i=0; i< ids.length ; i++) {
      try {
        data[i] = Long.valueOf(ids[i]);
      } catch (Exception e) {
        throw new PotaliRuntimeException("Exception while parsing string to long");
      }
    }
    return data;
  }

  public static Double[] convertToDouble(String[] ids) {
    if (ids == null || ids.length == 0) {
      return null;
    }
    Double[] data = new Double[ids.length];
    for (int i=0; i< ids.length ; i++) {
      try {
        data[i] = Double.valueOf(ids[i]);
      } catch (Exception e) {
        throw new PotaliRuntimeException("Exception while parsing string to long");
      }
    }
    return data;
  }

  public static Integer[] convertToInteger(String[] ids) {
    if (ids == null || ids.length == 0) {
      return null;
    }
    Integer[] data = new Integer[ids.length];
    for (int i=0; i< ids.length ; i++) {
      try {
        data[i] = Integer.valueOf(ids[i]);
      } catch (Exception e) {
        throw new PotaliRuntimeException("Exception while parsing string to long");
      }
    }
    return data;
  }

  public static String passwordEncrypt(String password) {
    return new Md5Hash(password, DefaultConstants.passwordSalt, DefaultConstants.hashIterations).toBase64();
  }

  public static String trimContent(String content) {
    if (content.length() > 100) {
      return content.substring(0,100);
    } else {
      return content;
    }
  }
}
