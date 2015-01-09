package com.potaliadmin.impl.service.post;

import com.potaliadmin.domain.reactions.PostReactions;
import com.potaliadmin.dto.internal.cache.es.post.PostReactionVO;
import com.potaliadmin.dto.web.request.posts.PostReactionRequest;
import com.potaliadmin.dto.web.response.post.GenericPostReactionResponse;
import com.potaliadmin.dto.web.response.post.PostSyncResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.pact.dao.post.PostReactionDao;
import com.potaliadmin.pact.service.cache.ESCacheService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.LoginService;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Shakti Singh on 12/28/14.
 */
@Service
public class PostServiceImpl implements PostService {

  @Autowired
  LoginService loginService;

  @Autowired
  PostReactionDao postReactionDao;

  @Autowired
  ESCacheService esCacheService;

  public static final String REACTION_INDEX = "post_reactions";
  private static final String INDEX = "ofc";
  private static final String JOB_TYPE = "job";

  @Override
  public GenericPostReactionResponse postReaction(PostReactionRequest postReactionRequest) {
    if (postReactionRequest == null) {
      throw new InValidInputException("Post Reaction Request Cannot be null");
    }
    if (!postReactionRequest.validate()) {
      throw new InValidInputException("Not a valid input");
    }
    UserResponse userResponse = getLoginService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Action!");
    }

    // first put in db and then in ES
    PostReactions postReactions = getPostReactionDao().createPostReaction(postReactionRequest.getActionId(),
        postReactionRequest.getPostId(), userResponse.getId());

    GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
    if (postReactions != null) {
      PostReactionVO postReactionVO = new PostReactionVO(postReactions);
      boolean published = getEsCacheService().put(REACTION_INDEX, postReactionVO, postReactionVO.getPostId());
      // for comments and other things do something else
      genericPostReactionResponse.setSuccess(published);
    } else {
      genericPostReactionResponse.setSuccess(Boolean.FALSE);
    }
    return genericPostReactionResponse;
  }

  @Override
  public PostSyncResponse syncPost(Long postId) {
    PostSyncResponse postSyncResponse = new PostSyncResponse();


    CountResponse countResponse = ESCacheManager.getInstance().getClient()
        .prepareCount(INDEX).setTypes(JOB_TYPE)
        .setQuery(QueryBuilders.rangeQuery("postId").gt(postId).includeLower(false).includeUpper(false))
        .execute().actionGet();

    if (countResponse.status().getStatus() == HttpStatus.OK.value()) {
      postSyncResponse.setJobCount(countResponse.getCount());
    } else {
      postSyncResponse.setException(true);
      postSyncResponse.addMessage("Something went wrong, please try again");
    }
    return postSyncResponse;
  }

  public LoginService getLoginService() {
    return loginService;
  }

  public PostReactionDao getPostReactionDao() {
    return postReactionDao;
  }

  public ESCacheService getEsCacheService() {
    return esCacheService;
  }
}
