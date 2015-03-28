package com.potaliadmin.dto.web.request.newsfeed;

import java.util.List;

/**
 * Created by shaktsin on 3/28/15.
 */
public class NewsFeedEditRequest extends NewsFeedCreateRequest {

  private List<Long> deletedAttachment;
  private Long postId;

  protected NewsFeedEditRequest() {
    //super();
  }

  protected NewsFeedEditRequest(Long plateFormId, String appName) {
    super(plateFormId, appName);
  }

  @Override
  public boolean validate() {
    return super.validate();
  }

  public void setDeletedAttachment(List<Long> deletedAttachment) {
    this.deletedAttachment = deletedAttachment;
  }

  public List<Long> getDeletedAttachment() {
    return deletedAttachment;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }
}
