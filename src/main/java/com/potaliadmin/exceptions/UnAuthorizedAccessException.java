package com.potaliadmin.exceptions;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Shakti Singh on 12/21/14.
 */
public class UnAuthorizedAccessException extends PotaliRuntimeException {

  public UnAuthorizedAccessException(String messageKey, Object... params) {
    super(messageKey, params);
  }

  @Override
  public String getMessage() {
    String message = super.getMessage();

    if (StringUtils.isBlank(message)) {
      message = "UnAuthorized Access! Please Login First!";
    }
    return message;
  }
}
