package com.potaliadmin.dto.web.request.posts;

import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.dto.web.request.framework.GenericRequest;

/**
 * Created by Shakti Singh on 12/28/14.
 */
public class PostReactionRequest extends GenericRequest {

  private Long actionId;
  private Long postId;

  public PostReactionRequest() {
  }

  public PostReactionRequest(Long actionId) {
    this.actionId = actionId;
  }

  public PostReactionRequest(Long plateFormId, String appName, Long actionId) {
    super(plateFormId, appName);
    this.actionId = actionId;
  }

  public boolean validate() {
    boolean isValid = super.validate();
    if (actionId == null) {
      isValid = false;
    }
    if (isValid && !EnumReactions.contains(actionId)) {
      isValid = false;
    }

    if (isValid && postId == null) {
      isValid = false;
    }

    return isValid;
  }

  public Long getActionId() {
    return actionId;
  }

  public void setActionId(Long actionId) {
    this.actionId = actionId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }
}
