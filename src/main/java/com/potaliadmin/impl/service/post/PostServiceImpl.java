package com.potaliadmin.impl.service.post;

import com.potaliadmin.constants.cache.ESIndexKeys;
import com.potaliadmin.constants.post.EnumPostType;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.reactions.PostReactions;
import com.potaliadmin.dto.internal.cache.es.framework.GenericPostVO;
import com.potaliadmin.dto.internal.cache.es.post.PostReactionVO;
import com.potaliadmin.dto.web.request.posts.BookMarkPostRequest;
import com.potaliadmin.dto.web.request.posts.PostCommentRequest;
import com.potaliadmin.dto.web.request.posts.PostReactionRequest;
import com.potaliadmin.dto.web.response.post.GenericPostReactionResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import com.potaliadmin.dto.web.response.post.PostResponse;
import com.potaliadmin.dto.web.response.post.PostSyncResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.pact.dao.post.PostReactionDao;
import com.potaliadmin.pact.service.cache.ESCacheService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.pact.service.users.UserService;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shakti Singh on 12/28/14.
 */
@Service
public class PostServiceImpl implements PostService {

  @Autowired
  PostReactionDao postReactionDao;

  @Autowired
  ESCacheService esCacheService;

  @Autowired
  UserService userService;



  @Override
  public GenericPostReactionResponse postReaction(PostReactionRequest postReactionRequest) {
    if (postReactionRequest == null) {
      throw new InValidInputException("Post Reaction Request Cannot be null");
    }
    if (!postReactionRequest.validate()) {
      throw new InValidInputException("Not a valid input");
    }
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Action!");
    }

    // first put in db and then in ES
    PostReactions postReactions = getPostReactionDao().createPostReaction(postReactionRequest.getActionId(),
        postReactionRequest.getPostId(), userResponse.getId());

    GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
    if (postReactions != null) {
      PostReactionVO postReactionVO = new PostReactionVO(postReactions);
      boolean published = getEsCacheService().put(ESIndexKeys.REACTION_INDEX, postReactionVO, postReactionVO.getPostId());
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
        .prepareCount(ESIndexKeys.INDEX).setTypes(ESIndexKeys.JOB_TYPE)
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

  @Override
  public GenericPostReactionResponse postComment(PostCommentRequest postCommentRequest) {
    if (postCommentRequest != null && !postCommentRequest.validate()) {
      throw new InValidInputException("INVALID_REQUEST");
    }

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    // first post reaction



    return null;
  }

  @Override
  public PostResponse fetchPostsByReactionId(BookMarkPostRequest bookMarkPostRequest) {
    long totalHits=0;
    int pageNo = bookMarkPostRequest.getPageNo();
    int perPage = bookMarkPostRequest.getPerPage();

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }
    BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
    boolFilterBuilder.must(FilterBuilders.termFilter("userId", userResponse.getId()));
    boolFilterBuilder.must(FilterBuilders.termFilter("reactionId", bookMarkPostRequest.getActionId()));
    HasChildFilterBuilder hasChildFilterBuilder = FilterBuilders.hasChildFilter(ESIndexKeys.REACTION_INDEX, boolFilterBuilder);

    SearchResponse searchResponse = ESCacheManager.getInstance().getClient()
                                  .prepareSearch(ESIndexKeys.INDEX).setTypes(ESIndexKeys.JOB_TYPE)
                                  .setPostFilter(hasChildFilterBuilder).addSort("postId", SortOrder.DESC)
                                  .setFrom(pageNo * perPage).setSize(perPage).execute().actionGet();


    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    if (searchResponse != null && RestStatus.OK.getStatus() == searchResponse.status().getStatus()) {
      SearchHits searchHits = searchResponse.getHits();
      totalHits = searchHits.getTotalHits();

      for (SearchHit searchHit : searchHits) {
        Class rClass = EnumPostType.getPostTypeByName(searchHit.getType()).getaClass();
        GenericPostVO genericPostVO = (GenericPostVO)getEsCacheService().parseResponse(searchHit, rClass);
        if (genericPostVO != null) {
          UserResponse postUser = getUserService().findById(genericPostVO.getUserId());
          GenericPostResponse genericPostResponse = new GenericPostResponse(genericPostVO, postUser);
          genericPostResponse.setPostType(EnumPostType.getPostTypeByName(searchHit.getType()).getId());
          genericPostResponseList.add(genericPostResponse);
        }
      }
    }

    PostResponse postResponse = new PostResponse();
    postResponse.setPosts(genericPostResponseList);
    postResponse.setPageNo(pageNo);
    postResponse.setPerPage(perPage);
    postResponse.setTotalResults(totalHits);
    return postResponse;
  }

  @Override
  public PostResponse fetchMyPosts(BookMarkPostRequest bookMarkPostRequest) {
    long totalHits=0;
    int pageNo = bookMarkPostRequest.getPageNo();
    int perPage = bookMarkPostRequest.getPerPage();

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }
    TermFilterBuilder termFilterBuilder = FilterBuilders.termFilter("userId", userResponse.getId());

    SearchResponse searchResponse = ESCacheManager.getInstance().getClient()
        .prepareSearch(ESIndexKeys.INDEX).setTypes(ESIndexKeys.JOB_TYPE)
        .setPostFilter(termFilterBuilder).addSort("postId", SortOrder.DESC)
        .setFrom(pageNo * perPage).setSize(perPage).execute().actionGet();


    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    if (searchResponse != null && RestStatus.OK.getStatus() == searchResponse.status().getStatus()) {
      SearchHits searchHits = searchResponse.getHits();
      totalHits = searchHits.getTotalHits();

      for (SearchHit searchHit : searchHits) {
        Class rClass = EnumPostType.getPostTypeByName(searchHit.getType()).getaClass();
        GenericPostVO genericPostVO = (GenericPostVO)getEsCacheService().parseResponse(searchHit, rClass);
        if (genericPostVO != null) {
          UserResponse postUser = getUserService().findById(genericPostVO.getUserId());
          GenericPostResponse genericPostResponse = new GenericPostResponse(genericPostVO, postUser);
          genericPostResponse.setPostType(EnumPostType.getPostTypeByName(searchHit.getType()).getId());
          genericPostResponseList.add(genericPostResponse);
        }
      }
    }

    PostResponse postResponse = new PostResponse();
    postResponse.setPosts(genericPostResponseList);
    postResponse.setPageNo(pageNo);
    postResponse.setPerPage(perPage);
    postResponse.setTotalResults(totalHits);
    return postResponse;
  }

  public UserService getUserService() {
    return userService;
  }

  public PostReactionDao getPostReactionDao() {
    return postReactionDao;
  }

  public ESCacheService getEsCacheService() {
    return esCacheService;
  }
}
