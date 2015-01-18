package com.potaliadmin.dto.web.response.post;

/**
 * Created by shakti on 18/1/15.
 */
public class CommentPostReactionResponse extends GenericPostReactionResponse {

  private Long postReactionId;

  public Long getPostReactionId() {
    return postReactionId;
  }

  public void setPostReactionId(Long postReactionId) {
    this.postReactionId = postReactionId;
  }
}
