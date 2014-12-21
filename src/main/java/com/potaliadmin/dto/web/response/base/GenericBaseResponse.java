package com.potaliadmin.dto.web.response.base;

import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.framework.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakti Singh on 10/6/14.
 */
public class GenericBaseResponse /*extends JSONObject*/ {

  boolean exception;
  List<String> messages = new ArrayList<String>();

  public void addMessage(String message) {
    messages.add(message);
  }

  public boolean isException() {
    return exception;
  }

  public void setException(boolean exception) {
    this.exception = exception;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  /*@Override
  protected List<String> getKeys() {
    List<String> keys = new ArrayList<String>();
    keys.add(DtoJsonConstants.EXCEPTION);
    keys.add(DtoJsonConstants.MESSAGE);
    return keys;
  }

  @Override
  protected List<Object> getValues() {
    List<Object> values = new ArrayList<Object>();
    values.add(this.exception);
    values.add(this.messages);
    return values;
  }*/
}
