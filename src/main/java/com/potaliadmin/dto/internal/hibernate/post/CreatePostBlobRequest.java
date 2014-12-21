package com.potaliadmin.dto.internal.hibernate.post;

/**
 * Created by Shakti Singh on 12/20/14.
 */
public class CreatePostBlobRequest {

  private Long postId;
  private String content;


  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
