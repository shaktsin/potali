package com.potaliadmin.impl.dao.post;

import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.domain.job.Job;
import com.potaliadmin.domain.post.Post;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.dto.internal.hibernate.post.CreatePostBlobRequest;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedCreateRequest;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedEditRequest;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.impl.framework.BaseDaoImpl;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.dao.post.PostDao;
import com.potaliadmin.pact.service.users.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shaktsin on 3/28/15.
 */
@Repository
public class PostDaoImpl extends BaseDaoImpl implements PostDao {

  private static final int CONTENT_SIZE = 499;

  @Autowired
  PostBlobDao postBlobDao;
  @Autowired
  UserService userService;
  @Autowired
  CircleDao circleDao;


  @Override
  @Transactional
  public Post createPost(NewsFeedCreateRequest newsFeedCreateRequest) {
    if (newsFeedCreateRequest.getUserId() == null) {
      throw new InValidInputException("User id cannot be null");
    }
    if (newsFeedCreateRequest.getUserInstituteId() == null) {
      throw new InValidInputException("User Institute Id cannot be null");
    }
    Post post = new Post();
    post.setUserId(newsFeedCreateRequest.getUserId());
    post.setUserInstituteId(newsFeedCreateRequest.getUserInstituteId());
    post.setSubject(newsFeedCreateRequest.getSubject());
    post.setContent(StringUtils.substring(newsFeedCreateRequest.getContent(), 0, CONTENT_SIZE));
    post.setReplyEmail(newsFeedCreateRequest.getReplyEmail());
    post.setReplyPhone(newsFeedCreateRequest.getReplyPhone());
    post.setReplyWatsApp(newsFeedCreateRequest.getReplyWatsApp());
    post.setShareEmail(EnumReactions.isValidShareReaction(newsFeedCreateRequest.getShareDto().getShareEmail()));
    post.setSharePhone(EnumReactions.isValidShareReaction(newsFeedCreateRequest.getShareDto().getSharePhone()));
    post.setShareWatsApp(EnumReactions.isValidShareReaction(newsFeedCreateRequest.getShareDto().getShareWatsApp()));

    Set<Circle> circles = new HashSet<Circle>();
    for (long circleId : newsFeedCreateRequest.getCircleList()) {
      Circle circle = getCircleDao().get(Circle.class, circleId);
      circles.add(circle);
    }
    post.setCircleSet(circles);

    //save job
    post = (Post) save(post);

    // save job post blob
    CreatePostBlobRequest createPostBlobRequest = new CreatePostBlobRequest();
    createPostBlobRequest.setPostId(post.getId());
    createPostBlobRequest.setContent(newsFeedCreateRequest.getContent());
    getPostBlobDao().createPostBlob(createPostBlobRequest);

    return post;
  }

  @Override
  @Transactional
  public Post editPost(NewsFeedEditRequest newsFeedEditRequest) {
    Post post = get(Post.class, newsFeedEditRequest.getPostId());
    if (post == null) {
      throw new InValidInputException("Attachment with no post associated");
    }

    UserResponse postUser = getUserService().findById(post.getUserId());
    if (postUser == null) {
      throw new RuntimeException("Something unexpected occurred, please try again");
    }


    post.setSubject(newsFeedEditRequest.getSubject());
    post.setContent(StringUtils.substring(newsFeedEditRequest.getContent(), 0, CONTENT_SIZE));
    post.setReplyEmail(newsFeedEditRequest.getReplyEmail());
    post.setReplyPhone(newsFeedEditRequest.getReplyPhone());
    post.setReplyWatsApp(newsFeedEditRequest.getReplyWatsApp());
    post.setShareEmail(EnumReactions.isValidShareReaction(newsFeedEditRequest.getShareDto().getShareEmail()));
    post.setSharePhone(EnumReactions.isValidShareReaction(newsFeedEditRequest.getShareDto().getSharePhone()));
    post.setShareWatsApp(EnumReactions.isValidShareReaction(newsFeedEditRequest.getShareDto().getShareWatsApp()));

    Set<Circle> circles = new HashSet<Circle>();
    for (long circleId : newsFeedEditRequest.getCircleList()) {
      Circle circle = getCircleDao().get(Circle.class, circleId);
      circles.add(circle);
    }
    post.setCircleSet(circles);


    PostBlob postBlob = getPostBlobDao().findByPostId(post.getId());
    if (postBlob == null) {
      throw new PotaliRuntimeException("Some exception occurred, please try again");
    }
    postBlob.setContent(newsFeedEditRequest.getContent());
    getPostBlobDao().save(postBlob);


    return (Post)save(post);
  }

  public PostBlobDao getPostBlobDao() {
    return postBlobDao;
  }

  public UserService getUserService() {
    return userService;
  }

  public CircleDao getCircleDao() {
    return circleDao;
  }
}
