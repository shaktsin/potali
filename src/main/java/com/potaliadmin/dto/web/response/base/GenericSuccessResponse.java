package com.potaliadmin.dto.web.response.base;

/**
 * Created by shakti on 28/1/15.
 */
public class GenericSuccessResponse extends GenericBaseResponse {

  boolean success;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
