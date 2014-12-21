package com.potaliadmin.dto.web.wrapper;

import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.framework.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakti Singh on 12/7/14.
 */
public class GenericResponseWrapper /*extends JSONObject*/ {

  private int status;
  private Object results;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Object getResults() {
    return results;
  }

  public void setResults(Object results) {
    this.results = results;
  }

  /*@Override
  protected List<String> getKeys() {
    List<String> keys = new ArrayList<String>();
    keys.add(DtoJsonConstants.RESULTS);
    keys.add(DtoJsonConstants.STATUS);
    return keys;
  }

  @Override
  protected List<Object> getValues() {
    List<Object> values = new ArrayList<Object>();
    values.add(this.results);
    values.add(this.status);
    return values;
  }*/
}
