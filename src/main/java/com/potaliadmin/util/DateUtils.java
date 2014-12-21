package com.potaliadmin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class DateUtils {

  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String POSTED_ON_PATTERN = "EEE, MMM d, yyyy";
  public static SimpleDateFormat sdf;

  static {
    sdf = new SimpleDateFormat();
  }

  public static String getPostedOnDate(Date date) {
    Date currentDate = new Date();
    long timeDiff = (currentDate.getTime() - date.getTime());
    long dayDiff = timeDiff/(24 * 60 * 60 * 1000);
    if (dayDiff == 0) {
      return "Today";
    } else if (dayDiff == 1) {
      return "Yesterday";
    } else {
      sdf.applyPattern(POSTED_ON_PATTERN);
      return sdf.format(date);
    }
  }
}
