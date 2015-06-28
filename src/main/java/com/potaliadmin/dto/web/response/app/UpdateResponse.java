package com.potaliadmin.dto.web.response.app;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

/**
 * Created by shaktsin on 6/28/15.
 */
public class UpdateResponse extends GenericBaseResponse {

  private boolean appUpdate;
  private String updateTitle;
  private String updateMessage;


  public boolean isAppUpdate() {
    return appUpdate;
  }

  public void setAppUpdate(boolean appUpdate) {
    this.appUpdate = appUpdate;
  }

  public String getUpdateTitle() {
    return updateTitle;
  }

  public void setUpdateTitle(String updateTitle) {
    this.updateTitle = updateTitle;
  }

  public String getUpdateMessage() {
    return updateMessage;
  }

  public void setUpdateMessage(String updateMessage) {
    this.updateMessage = updateMessage;
  }
}
