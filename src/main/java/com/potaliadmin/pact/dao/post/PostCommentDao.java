package com.potaliadmin.pact.dao.post;

import com.potaliadmin.domain.comment.Comment;
import com.potaliadmin.domain.user.User;
import com.potaliadmin.dto.web.request.posts.PostCommentRequest;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.pact.framework.BaseDao;

import java.util.List;

/**
 * Created by Shakti Singh on 1/14/15.
 */
public interface PostCommentDao extends BaseDao {

  Comment createComment(PostCommentRequest postCommentRequest, UserResponse userResponse);

  long getCommentOnPost(long postId);

  List<Long> getCommentsUser(long postId);
}
