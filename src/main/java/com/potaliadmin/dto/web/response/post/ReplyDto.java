package com.potaliadmin.dto.web.response.post;

/**
 * Created by Shakti Singh on 1/2/15.
 */
public class ReplyDto {

  private long replyEmail;
  private long replyPhone;
  private long replyWatsApp;

  public ReplyDto() {
  }

  public ReplyDto(long replyEmail, long replyPhone, long replyWatsApp) {
    this.replyEmail = replyEmail;
    this.replyPhone = replyPhone;
    this.replyWatsApp = replyWatsApp;
  }

  public long getReplyEmail() {
    return replyEmail;
  }

  public void setReplyEmail(long replyEmail) {
    this.replyEmail = replyEmail;
  }

  public long getReplyPhone() {
    return replyPhone;
  }

  public void setReplyPhone(long replyPhone) {
    this.replyPhone = replyPhone;
  }

  public long getReplyWatsApp() {
    return replyWatsApp;
  }

  public void setReplyWatsApp(long replyWatsApp) {
    this.replyWatsApp = replyWatsApp;
  }
}
