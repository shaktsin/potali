package com.potaliadmin.pact.dao.post;

import com.potaliadmin.domain.reactions.PostReactions;
import com.potaliadmin.pact.framework.BaseDao;

/**
 * Created by Shakti Singh on 12/28/14.
 */
public interface PostReactionDao extends BaseDao {

  public PostReactions createPostReaction(Long reactionId, Long postId, Long userId);

  public PostReactions getPostReactionByReactionAndPostId(Long reactionId, Long postId, Long userId);
}
