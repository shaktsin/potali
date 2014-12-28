package com.potaliadmin.dto.internal.cache.es.post;

import com.potaliadmin.domain.reactions.PostReactions;
import com.potaliadmin.dto.internal.cache.es.framework.GenericVO;

/**
 * Created by Shakti Singh on 12/28/14.
 */
public class PostReactionVO extends GenericVO {

  private Long reactionId;
  private Long postId;
  private Long userId;

  public PostReactionVO() {
  }

  public PostReactionVO(PostReactions postReactions) {
    super(postReactions.getId());
    this.reactionId = postReactions.getReactionId();
    this.postId = postReactions.getPostId();
    this.userId = postReactions.getUserId();
  }

  public Long getReactionId() {
    return reactionId;
  }

  public void setReactionId(Long reactionId) {
    this.reactionId = reactionId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
