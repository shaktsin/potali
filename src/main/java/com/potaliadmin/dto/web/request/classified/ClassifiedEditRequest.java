package com.potaliadmin.dto.web.request.classified;

import java.util.List;

/**
 * Created by shaktsin on 4/29/15.
 */
public class ClassifiedEditRequest extends ClassifiedPostRequest {

  private Long postId;
  private List<Long> deletedAttachment;

  public ClassifiedEditRequest() {}

  public ClassifiedEditRequest(Long postId) {
    this.postId = postId;
  }

  public ClassifiedEditRequest(Long plateFormId, String appName, Long postId) {
    super(plateFormId, appName);
    this.postId = postId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public List<Long> getDeletedAttachment() {
    return deletedAttachment;
  }

  public void setDeletedAttachment(List<Long> deletedAttachment) {
    this.deletedAttachment = deletedAttachment;
  }
}
