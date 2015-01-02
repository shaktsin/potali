package com.potaliadmin.dto.web.response.post;

/**
 * Created by Shakti Singh on 1/2/15.
 */
public class ShareDto {

  private long shareEmail;
  private long sharePhone;
  private long shareWatsApp;

  public ShareDto() {
  }

  public ShareDto(long shareEmail, long sharePhone, long shareWatsApp) {
    this.shareEmail = shareEmail;
    this.sharePhone = sharePhone;
    this.shareWatsApp = shareWatsApp;
  }

  public long getShareEmail() {
    return shareEmail;
  }

  public void setShareEmail(long shareEmail) {
    this.shareEmail = shareEmail;
  }

  public long getSharePhone() {
    return sharePhone;
  }

  public void setSharePhone(long sharePhone) {
    this.sharePhone = sharePhone;
  }

  public long getShareWatsApp() {
    return shareWatsApp;
  }

  public void setShareWatsApp(long shareWatsApp) {
    this.shareWatsApp = shareWatsApp;
  }
}
