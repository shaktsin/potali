package com.potaliadmin.util;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.List;
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

  public static String trimContent(String content, int len) {
    if (len == 0) {
      len = 300;
    }
    if (content.length() > len) {
      return content.substring(0,len)+"...";
    } else {
      return content+".";
    }
  }

  public static <T> List<T> getPaginatedList(List<T> list, int pageNo, int perPage) {
    if (list == null) {
      throw new PotaliRuntimeException("List cannot be null");
    }
    int startIndex = (pageNo)*perPage;
    int lastIndex = startIndex + perPage;
    if (startIndex > list.size()) {
      return null;
    }
    if (lastIndex > list.size()) {
      return list.subList(startIndex, list.size());
    } else {
      return list.subList(startIndex, lastIndex);
    }

  }

  public static int generateVerificationToken() {
    return (int)(Math.random()*9000)+1000;
  }

  public static String capitalize(String str) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return str;
    }
    return new StringBuilder(strLen)
        .append(Character.toTitleCase(str.charAt(0)))
        .append(str.substring(1))
        .toString();
  }
}
