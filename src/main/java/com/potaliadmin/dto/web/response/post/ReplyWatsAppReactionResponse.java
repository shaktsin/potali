package com.potaliadmin.dto.web.response.post;

/**
 * Created by shakti on 18/1/15.
 */
public class ReplyWatsAppReactionResponse extends GenericPostReactionResponse {

  private String watsApp;

  public String getWatsApp() {
    return watsApp;
  }

  public void setWatsApp(String watsApp) {
    this.watsApp = watsApp;
  }
}
