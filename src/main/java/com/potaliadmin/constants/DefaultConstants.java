package com.potaliadmin.constants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shakti Singh on 10/6/14.
 */
public class DefaultConstants
{
  //app
  public static String APP_NAME = "ofCampus";
  // security
  public static String realmName = "DefaultHibernateRealmName";
  public static String passwordSalt = "";
  public static int hashIterations = 1;
  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  public static final String DEFAULT_ES_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

  public static final String PLATE_FROM = "0";
  public static final int AND_APP_PER_PAGE = 12;
  public static final int AND_APP_PAGE_NO = 0;
  public static final String AND_APP_PER_PAGE_STR = "12";
  public static final String AND_APP_PAGE_NO_STR = "0";
  public static final Long DEFAULT_FILTER = 1l;
  public static final String DEFAULT_OTHER_FILTER = "Other";

  public static final String REQUEST_SEPARATOR="~";
  public static final Double MAX_SALARY = 9999999999D;
  public static final Double MIN_SALARY = 0D;

  public static final int MIN_EXP_YEAR = 0;
  public static final int MAX_EXP_YEAR = 100;

  public static final String DEFAULT_PASSWORD = "DEFAULT_PASSWORD";

  public static final long MAX_IMAGE_UPLOAD_SIZE = 500;//in kb
  public static final String NAME_SEPARATOR = "_";
  public static final String PATH_SEPARATOR = "/";
  public static final String FILE_EXT_SEPARATOR = ".";

  public static final List<String> ALLOWED_IMAGE_CONTENT_TYPE = Arrays.asList("image/png", "image/jpg", "image/jpeg");
  public static final String PROFILE = "profile";
  public static final String POST = "post";
  public static final String IMAGE = "image";
  public static final String DOC = "doc";
  public static final String JD ="jd";

  public static final String SHARE_SUFFIX = "Shared By OfCampus";

  // APP Update
  public static final boolean FORCE_UPDATE = false;
  public static final String UPDATE_TITLE = "Update Required";
  public static final String UPDATE_MESSAGE = "Update Required";
}
