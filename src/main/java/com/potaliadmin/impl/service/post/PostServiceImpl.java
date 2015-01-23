package com.potaliadmin.impl.service.post;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.cache.ESIndexKeys;
import com.potaliadmin.constants.post.EnumPostType;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.comment.Comment;
import com.potaliadmin.domain.reactions.PostReactions;
import com.potaliadmin.dto.internal.cache.es.framework.GenericPostVO;
import com.potaliadmin.dto.internal.cache.es.post.PostReactionVO;
import com.potaliadmin.dto.web.request.posts.AllPostReactionRequest;
import com.potaliadmin.dto.web.request.posts.BookMarkPostRequest;
import com.potaliadmin.dto.web.request.posts.PostCommentRequest;
import com.potaliadmin.dto.web.request.posts.PostReactionRequest;
import com.potaliadmin.dto.web.response.post.*;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.framework.elasticsearch.ESSearchFilter;
import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.pact.dao.post.PostCommentDao;
import com.potaliadmin.pact.dao.post.PostReactionDao;
import com.potaliadmin.pact.service.cache.ESCacheService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.comment.CommentVO;
import com.potaliadmin.vo.post.PostVO;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.MultiSearchRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

  @Autowired
  PostReactionDao postReactionDao;
  @Autowired
  ESCacheService esCacheService;
  @Autowired
  BaseESService baseESService;
  @Autowired
  UserService userService;
  @Autowired
  PostCommentDao postCommentDao;



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

    // first check if there is any post of this ID
    PostVO postVO = (PostVO) getBaseESService().get(postReactionRequest.getPostId(), null , PostVO.class);
    if (postVO == null) {
      throw new PotaliRuntimeException("NO POST FOUND FOR POST ID "+postReactionRequest.getPostId());
    }

    // first put in db and then in ES
    PostReactions postReactions = getPostReactionDao().createPostReaction(postReactionRequest.getActionId(),
        postReactionRequest.getPostId(), userResponse.getId());


    if (postReactions != null) {
      PostReactionVO postReactionVO = new PostReactionVO(postReactions);
      //boolean published = getEsCacheService().put(ESIndexKeys.REACTION_INDEX, postReactionVO, postReactionVO.getPostId());
      boolean published = getBaseESService().put(postReactionVO);
      // for comments and other things do something else
      //genericPostReactionResponse.setSuccess(published);
      if (published) {
        return generatePostReactionResponse(postVO, postReactionVO);
      } else {
        GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
        genericPostReactionResponse.setSuccess(Boolean.FALSE);
        return genericPostReactionResponse;
      }

    } else {
      GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
      genericPostReactionResponse.setSuccess(Boolean.FALSE);
      return genericPostReactionResponse;
    }
  }

  @Override
  @Deprecated
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
  public CommentResponse postComment(PostCommentRequest postCommentRequest) {
    if (postCommentRequest == null || !postCommentRequest.validate()) {
      throw new InValidInputException("INVALID_REQUEST");
    }

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new PotaliRuntimeException("USER CANNOT BE NULL");
    }

    // first get the post reaction
    //PostReactionVO postReactionVO = (PostReactionVO)getBaseESService().get(postCommentRequest.getPostReactionId(),
    //    postCommentRequest.getPostId(), PostReactionVO.class);

    // first check if there is any post of this ID
    PostVO postVO = (PostVO) getBaseESService().get(postCommentRequest.getPostId(), null , PostVO.class);
    if (postVO == null) {
      throw new PotaliRuntimeException("NO POST FOUND FOR POST ID "+postCommentRequest.getPostId());
    }

    // first put in db and then in ES
    PostReactions postReactions = getPostReactionDao().createPostReaction(EnumReactions.COMMENT.getId(),
        postCommentRequest.getPostId(), userResponse.getId());

    PostReactionVO postReactionVO = new PostReactionVO(postReactions);

    boolean isPostReactionPublished = getBaseESService().put(postReactionVO);

    if (!isPostReactionPublished) {
      throw new PotaliRuntimeException("Some Problem in Posting Reaction "+postCommentRequest.getPostId());
    }

    Comment comment = getPostCommentDao().createComment(postCommentRequest, userResponse);
    if (comment == null) {
      logger.error("Error in saving comment for post id "+postCommentRequest.getPostId());
      throw new PotaliRuntimeException("Something went wrong, Please Try Again!");
    }

    CommentVO commentVO = new CommentVO(comment);
    boolean published = getBaseESService().put(commentVO);
    if (published) {
      CommentResponse commentResponse = new CommentResponse();
      commentResponse.setContent(commentVO.getComment());
      UserDto userDto = new UserDto(userResponse);
      commentResponse.setUserDto(userDto);
      commentResponse.setPostId(Long.parseLong(commentVO.getParentId()));
      commentResponse.setCommentedOn(DateUtils.getPostedOnDate(commentVO.getCommentedOn()));
      return commentResponse;
    } else {
      CommentResponse commentResponse = new CommentResponse();
      commentResponse.setException(true);
      commentResponse.addMessage("Something went wrong, Please Try Again!");
      return commentResponse;

    }

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

    /*SearchResponse searchResponse = ESCacheManager.getInstance().getClient()
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
    postResponse.setTotalResults(totalHits);*/

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(hasChildFilterBuilder).addSortedMap("postId", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostVO.class);
    List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      PostVO postVO = (PostVO) baseElasticVO;
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = new GenericPostResponse(postVO, postUser);
      genericPostResponseList.add(genericPostResponse);
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

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(termFilterBuilder).addSortedMap("postId", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostVO.class);
    List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      PostVO postVO = (PostVO) baseElasticVO;
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = new GenericPostResponse(postVO, postUser);
      genericPostResponseList.add(genericPostResponse);
    }
    PostResponse postResponse = new PostResponse();
    postResponse.setPosts(genericPostResponseList);
    postResponse.setPageNo(pageNo);
    postResponse.setPerPage(perPage);
    postResponse.setTotalResults(totalHits);
    return postResponse;
  }

  @Override
  public CommentListResponse getAllComments(AllPostReactionRequest allPostReactionRequest) {
    if (allPostReactionRequest == null || !allPostReactionRequest.validate()) {
      throw new InValidInputException("POST_ID_IS_NULL");
    }

    // first check if there is any post of this ID
    PostVO postVO = (PostVO) getBaseESService().get(allPostReactionRequest.getPostId(), null , PostVO.class);
    if (postVO == null) {
      throw new PotaliRuntimeException("NO POST FOUND FOR POST ID "+allPostReactionRequest.getPostId());
    }

    AndFilterBuilder andFilterBuilder = new AndFilterBuilder();
    TermFilterBuilder termFilterBuilder = FilterBuilders.termFilter("parentId", postVO.getPostId());
    andFilterBuilder.add(termFilterBuilder);
    andFilterBuilder.add(FilterBuilders.rangeFilter("id").
        lt(allPostReactionRequest.getCommentId()).
        gt(allPostReactionRequest.getCommentId() - allPostReactionRequest.getPerPage()));

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(andFilterBuilder).addSortedMap("id", SortOrder.ASC)
            .setPageNo(DefaultConstants.AND_APP_PAGE_NO).setPerPage(DefaultConstants.AND_APP_PER_PAGE);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, CommentVO.class);
    CommentListResponse commentListResponse = new CommentListResponse();
    if (esSearchResponse.getTotalResults() > 0) {

      List<CommentResponse> commentResponses = new ArrayList<CommentResponse>();
      for (BaseElasticVO baseElasticVO : esSearchResponse.getBaseElasticVOs()) {
        CommentVO commentVO = (CommentVO) baseElasticVO;
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setContent(commentVO.getComment());
        UserResponse commentUser = getUserService().findById(commentVO.getUserId());
        UserDto userDto = new UserDto(commentUser);
        commentResponse.setUserDto(userDto);
        commentResponse.setPostId(Long.parseLong(commentVO.getParentId()));
        commentResponse.setCommentedOn(DateUtils.getPostedOnDate(commentVO.getCommentedOn()));
        commentResponses.add(commentResponse);
      }
      commentListResponse.setCommentResponseList(commentResponses);
      commentListResponse.setPageNo(DefaultConstants.AND_APP_PAGE_NO);
      commentListResponse.setPerPage(DefaultConstants.AND_APP_PER_PAGE);
      commentListResponse.setTotalResults(esSearchResponse.getTotalResults());

    }
    return commentListResponse;
  }

  @Override
  public boolean isPostImportantForUser(Long postId, Long userId) {
    boolean isImp = false;

    if (userId == null) {
      throw new InValidInputException("USER_ID_CANNOT_BE_NULL");
    }
    if (postId == null) {
      throw new InValidInputException("POST_ID_CANNOT_BE_NULL");
    }

    AndFilterBuilder andFilterBuilder = FilterBuilders.andFilter(FilterBuilders.termFilter("userId", userId),
        FilterBuilders.termFilter("postId", postId), FilterBuilders.termFilter("reactionId", EnumReactions.MARK_AS_IMPORTANT.getId()));

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(andFilterBuilder);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostReactionVO.class);
    return esSearchResponse.getTotalResults() > 0;
  }

  @Override
  public boolean postHasComments(Long postId) {
    if (postId == null) {
      throw new InValidInputException("POST_ID_CANNOT_BE_NULL");
    }
    AndFilterBuilder andFilterBuilder = FilterBuilders.andFilter(FilterBuilders.termFilter("postId", postId),
        FilterBuilders.termFilter("reactionId", EnumReactions.COMMENT.getId()));

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(andFilterBuilder);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostReactionVO.class);

    return esSearchResponse.getTotalResults() > 0;
  }

  private GenericPostReactionResponse generatePostReactionResponse(PostVO postVO, PostReactionVO postReactionVO) {

    if (EnumReactions.REPLY_VIA_EMAIL.getId().equals(postReactionVO.getReactionId())) {
      ReplyEmailReactionResponse replyEmailReactionResponse = new ReplyEmailReactionResponse();
      replyEmailReactionResponse.setSuccess(true);
      replyEmailReactionResponse.setReplyEmail(postVO.getReplyEmail());
      return replyEmailReactionResponse;
    } else if (EnumReactions.REPLY_VIA_PHONE.getId().equals(postReactionVO.getReactionId())) {
      ReplyPhoneEmailReactionResponse replyPhoneEmailReactionResponse = new ReplyPhoneEmailReactionResponse();
      replyPhoneEmailReactionResponse.setSuccess(true);
      replyPhoneEmailReactionResponse.setReplyPhone(postVO.getReplyPhone());
      return replyPhoneEmailReactionResponse;
    } else if (EnumReactions.REPLY_VIA_WATSAPP.getId().equals(postReactionVO.getReactionId())) {
      ReplyWatsAppReactionResponse replyWatsAppReactionResponse = new ReplyWatsAppReactionResponse();
      replyWatsAppReactionResponse.setSuccess(true);
      replyWatsAppReactionResponse.setWatsApp(postVO.getReplyWatsApp());
      return replyWatsAppReactionResponse;
    } else if (EnumReactions.SHARE_VIA_EMAIL.getId().equals(postReactionVO.getReactionId()) ||
        EnumReactions.SHARE_VIA_PHONE.getId().equals(postReactionVO.getReactionId()) ||
        EnumReactions.SHARE_VIA_WATSAPP.getId().equals(postReactionVO.getReactionId())) {

      ShareReactionResponse shareReactionResponse = new ShareReactionResponse();
      shareReactionResponse.setSuccess(true);
      shareReactionResponse.setContent(postVO.getContent());
      return shareReactionResponse;

    } else if (EnumReactions.COMMENT.getId().equals(postReactionVO.getReactionId())) {
      CommentPostReactionResponse commentPostReactionResponse = new CommentPostReactionResponse();
      commentPostReactionResponse.setSuccess(true);
      commentPostReactionResponse.setPostReactionId(postReactionVO.getId());
      return commentPostReactionResponse;
    } else {
      GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
      genericPostReactionResponse.setSuccess(true);
      return genericPostReactionResponse;
    }

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

  public BaseESService getBaseESService() {
    return baseESService;
  }

  public PostCommentDao getPostCommentDao() {
    return postCommentDao;
  }
}
