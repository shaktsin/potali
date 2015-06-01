package com.potaliadmin.impl.dao.post;

import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.domain.comment.Comment;
import com.potaliadmin.domain.user.User;
import com.potaliadmin.dto.web.request.posts.PostCommentRequest;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.post.PostCommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

  @Override
  public long getCommentOnPost(long postId) {
    String query = "select count(*) from Comment c where c.postId = :postId";
    return countByNamedParams(query, new String[]{"postId"}, new Object[]{postId});
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Long> getCommentsUser(long postId) {

    List<Long>  userList = new ArrayList<Long>();

    List<Comment> comments = (List<Comment>)
        findByNamedQueryAndNamedParam("findCommentsByPostId", new String[]{"postId"}, new Object[]{postId});

    for (Comment comment : comments) {
      userList.add(comment.getUserId());
    }

    return userList;
  }
}
