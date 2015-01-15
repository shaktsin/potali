package com.potaliadmin.impl.dao.post;

import com.potaliadmin.domain.comment.Comment;
import com.potaliadmin.dto.web.request.posts.PostCommentRequest;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.post.PostCommentDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Shakti Singh on 1/14/15.
 */
@Repository
public class PostCommentDaoImpl extends BaseDaoImpl implements PostCommentDao {

  @Override
  @Transactional
  public Comment createComment(PostCommentRequest postCommentRequest, UserResponse userResponse) {
    Comment comment = new Comment();
    comment.setComment(postCommentRequest.getComment());
    comment.setPostId(postCommentRequest.getPostId());
    comment.setUserId(userResponse.getId());
    comment.setUserInstituteId(userResponse.getInstituteId());
    return (Comment)save(comment);
  }
}
