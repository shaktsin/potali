package com.potaliadmin.constants.cache;

/**
 * Created by Shakti Singh on 1/10/15.
 */
public enum MemCacheNS {

  USER_BY_ID("userById"),
  USER_BY_EMAIL("userByEmail");

  private String bucket;

  MemCacheNS(String bucket) {
    this.bucket = bucket;
  }

  public String getBucket() {
    return bucket;
  }
}
