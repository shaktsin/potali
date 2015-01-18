package com.potaliadmin.dto.web.response.post;

/**
 * Created by shakti on 18/1/15.
 */
public class ReplyEmailReactionResponse extends GenericPostReactionResponse {

  private String replyEmail;

  public String getReplyEmail() {
    return replyEmail;
  }

  public void setReplyEmail(String replyEmail) {
    this.replyEmail = replyEmail;
  }
}
