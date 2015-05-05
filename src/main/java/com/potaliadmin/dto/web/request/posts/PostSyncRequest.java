package com.potaliadmin.dto.web.request.posts;

import com.potaliadmin.dto.web.request.framework.GenericRequest;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Shakti Singh on 1/7/15.
 */
public class PostSyncRequest extends GenericRequest {

  Long postId;
  String postDate;

  public PostSyncRequest() {
  }

  public PostSyncRequest(Long plateFormId, String appName, Long postId) {
    super(plateFormId, appName);
    this.postId = postId;
  }

  @Override
  public boolean validate() {
    boolean isValid = super.validate();
    if (isValid && null == postDate) {
      isValid = false;
    }
    return isValid;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getPostDate() {
    return postDate;
  }

  public void setPostDate(String postDate) {
    this.postDate = postDate;
  }
}
