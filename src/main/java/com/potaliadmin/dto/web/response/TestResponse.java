package com.potaliadmin.dto.web.response;

import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.List;

/**
 * Created by Shakti Singh on 12/6/14.
 */
public class TestResponse /*extends GenericBaseResponse*/ {

  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  /*@Override
  protected List<String> getKeys() {
    List<String> keys = super.getKeys();
    keys.add(DtoJsonConstants.MESSAGE);
    return keys;
  }

  @Override
  protected List<Object> getValues() {
    List<Object> values = super.getValues();
    values.add(message);
    return values;
  }*/
}
