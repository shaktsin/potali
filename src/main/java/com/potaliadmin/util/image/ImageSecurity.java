package com.potaliadmin.util.image;

import org.apache.shiro.codec.Base64;

/**
 * Created by Shakti Singh on 1/3/15.
 */
public class ImageSecurity {



  public static String encodeImage(String canonicalFileName) {
    return Base64.encodeToString(canonicalFileName.getBytes());
  }

  public static String decodeImage(String imageToken) {
    return Base64.decodeToString(imageToken);
  }
}
