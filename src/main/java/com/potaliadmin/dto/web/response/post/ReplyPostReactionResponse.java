package com.potaliadmin.dto.web.response.post;

/**
 * Created by Shakti Singh on 1/16/15.
 */
public class ReplyPostReactionResponse extends GenericPostReactionResponse {

  private ReplyDto replyDto;

  public ReplyDto getReplyDto() {
    return replyDto;
  }

  public void setReplyDto(ReplyDto replyDto) {
    this.replyDto = replyDto;
  }
}
