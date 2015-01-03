package com.potaliadmin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class DateUtils {

  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String POSTED_ON_PATTERN = "d MMM yyyy, hh:mm aaa";
  public static SimpleDateFormat sdf;

  static {
    sdf = new SimpleDateFormat();
  }

  public static String getPostedOnDate(Date date) {
    sdf.applyPattern(POSTED_ON_PATTERN);
    return sdf.format(date);
  }

  public static void main(String[] args) {
    System.out.println(getPostedOnDate(new Date()));
  }
}
