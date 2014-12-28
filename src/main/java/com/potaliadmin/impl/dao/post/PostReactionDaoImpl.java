package com.potaliadmin.impl.dao.post;

import com.potaliadmin.domain.reactions.PostReactions;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.dao.post.PostReactionDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Shakti Singh on 12/28/14.
 */
@Repository
public class PostReactionDaoImpl extends BaseDaoImpl implements PostReactionDao {

  @Override
  @Transactional
  public PostReactions createPostReaction(Long reactionId, Long postId, Long userId) {
    PostReactions postReactions = new PostReactions();
    postReactions.setReactionId(reactionId);
    postReactions.setPostId(postId);
    postReactions.setUserId(userId);
    postReactions = (PostReactions)save(postReactions);
    return postReactions;
  }
}
