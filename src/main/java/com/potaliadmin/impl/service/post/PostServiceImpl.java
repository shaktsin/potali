package com.potaliadmin.impl.service.post;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.constants.attachment.EnumImageFormat;
import com.potaliadmin.constants.cache.ESIndexKeys;
import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.constants.post.EnumPostType;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.domain.comment.Comment;
import com.potaliadmin.domain.reactions.PostReactions;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.internal.attachment.AttachmentDto;
import com.potaliadmin.dto.internal.attachment.AttachmentMap;
import com.potaliadmin.dto.internal.cache.address.CityVO;
import com.potaliadmin.dto.internal.cache.es.job.BaseRangeDto;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.internal.cache.es.job.ExperienceRangeDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryDto;
import com.potaliadmin.dto.internal.cache.es.job.IndustryRolesDto;
import com.potaliadmin.dto.internal.cache.es.job.SalaryRangeDto;
import com.potaliadmin.dto.internal.cache.es.post.PostReactionVO;
import com.potaliadmin.dto.internal.cache.job.IndustryRolesVO;
import com.potaliadmin.dto.internal.cache.job.IndustryVO;
import com.potaliadmin.dto.internal.filter.BaseFilterDto;
import com.potaliadmin.dto.internal.filter.GeneralFilterDto;
import com.potaliadmin.dto.internal.filter.JobFilterDto;
import com.potaliadmin.dto.internal.image.CreateAttachmentResponseDto;
import com.potaliadmin.dto.web.request.posts.AllPostReactionRequest;
import com.potaliadmin.dto.web.request.posts.BookMarkPostRequest;
import com.potaliadmin.dto.web.request.posts.CirclePostRequest;
import com.potaliadmin.dto.web.request.posts.PostCommentRequest;
import com.potaliadmin.dto.web.request.posts.PostReactionRequest;
import com.potaliadmin.dto.web.request.posts.UserProfileRequest;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.post.*;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.cache.ESCacheManager;
import com.potaliadmin.framework.cache.address.CityCache;
import com.potaliadmin.framework.cache.industry.IndustryCache;
import com.potaliadmin.framework.cache.industry.IndustryRolesCache;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.framework.elasticsearch.ESSearchFilter;
import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.impl.framework.properties.AppProperties;
import com.potaliadmin.pact.dao.attachment.AttachmentDao;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.dao.post.PostCommentDao;
import com.potaliadmin.pact.dao.post.PostReactionDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.framework.utils.FileUploadService;
import com.potaliadmin.pact.service.cache.ESCacheService;
import com.potaliadmin.pact.service.notification.NotificationService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.util.image.AttachmentCloudTask;
import com.potaliadmin.util.image.AttachmentUploaderTask;
import com.potaliadmin.util.image.RawAttachmentCloudTask;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.comment.CommentVO;
import com.potaliadmin.vo.post.PostVO;
import com.potaliadmin.vo.user.UserVO;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.HasChildFilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
  @Autowired
  FileUploadService fileUploadService;
  @Autowired
  UploadService uploadService;
  @Autowired
  AttachmentDao attachmentDao;
  @Autowired
  AppProperties appProperties;
  @Autowired
  CircleDao circleDao;
  @Autowired
  NotificationService notificationService;


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
    // first reverse it in DB and then In ES
    PostReactions postReactions = null;
    if (postReactionRequest.getActionId().equals(EnumReactions.MARK_AS_IMPORTANT.getId())
        || postReactionRequest.getActionId().equals(EnumReactions.HIDE_THIS_POST.getId())
        || postReactionRequest.getActionId().equals(EnumReactions.LIKE_IT.getId())) {

      postReactions = getPostReactionDao().getPostReactionByReactionAndPostId(postReactionRequest.getActionId(),
          postReactionRequest.getPostId(), userResponse.getId());

      if (postReactions != null) {
        throw new PotaliRuntimeException("Already Marked  hidden or important");
      }
    }

    postReactions = getPostReactionDao().createPostReaction(postReactionRequest.getActionId(),
        postReactionRequest.getPostId(), userResponse.getId());


    if (postReactions != null) {
      PostReactionVO postReactionVO = new PostReactionVO(postReactions);
      //boolean published = getEsCacheService().put(ESIndexKeys.REACTION_INDEX, postReactionVO, postReactionVO.getPostId());
      boolean published = getBaseESService().put(postReactionVO);
      // for comments and other things do something else
      //genericPostReactionResponse.setSuccess(published);
      if (published) {
        GenericPostReactionResponse genericPostReactionResponse = generatePostReactionResponse(postVO, postReactionVO);
        published = getBaseESService().put(postVO);
        if (published) {

          // if users like it then send notification
          getNotificationService().sendLikeNotification(postVO.getPostId(), userResponse.getId());

          return genericPostReactionResponse;
        } else {
          getBaseESService().delete(postReactions.getId(), PostReactionVO.class);
          throw new PotaliRuntimeException("Some exception occurred, please try again");
        }

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
  public PostSyncResponse syncPost(Date currentDate) {
    PostSyncResponse postSyncResponse = new PostSyncResponse();
    long newsFeeds =0, classifieds = 0, jobs = 0;

    SearchRequestBuilder searchRequestBuilder =  ESCacheManager.getInstance().getClient().
        prepareSearch(ESIndexKeys.INDEX).setTypes(ESIndexKeys.POST)
        .setQuery(QueryBuilders.rangeQuery("createdDate")
            .gt(DateUtils.formatDate(currentDate, DateUtils.DEFAULT_ES_DATE_FORMAT)));


    //List<BaseElasticVO> genericPostResponseList = new ArrayList<BaseElasticVO>();
    try {
      SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

      if (searchResponse.status().getStatus() == HttpStatus.OK.value()) {
        SearchHits searchHits = searchResponse.getHits();
        //totalHits = searchHits.getTotalHits();
        for (SearchHit searchHit : searchHits) {
          try {
            PostVO baseElasticVO = (PostVO)
                getBaseESService().parserResponse(searchHit.getSourceAsString(), PostVO.class);
            if (EnumPostType.NEWSFEED.getId() == baseElasticVO.getPostType()) {
              newsFeeds += 1;
            } else if (EnumPostType.CLASSIFIED.getId() == baseElasticVO.getPostType()) {
              classifieds += 1;
            } else if (EnumPostType.JOBS.getId() == baseElasticVO.getPostType()) {
              jobs += 1;
            }
          } catch (Exception e) {
            logger.info("Some exception occurred while parsing data ", e);
          }
        }

      }

      postSyncResponse.setJobCount(jobs);
      postSyncResponse.setClassCount(classifieds);
      postSyncResponse.setNewsFeedCount(newsFeeds);
    } catch (Exception e) {
      logger.error("Error while running search in ES ",e);
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

    // check whether user is authorized to comment
    boolean isAuthorized = false;
    List<CircleVO> circleVOs = postVO.getCircleList();
    for (CircleVO circleVO : circleVOs) {
      if (userResponse.getCircleList() == null) {
        break;
      }
      if (userResponse.getCircleList().contains(circleVO.getId())) {
        isAuthorized = true;
        break;
      }
    }

    if (!isAuthorized) {
      CommentResponse commentResponse = new CommentResponse();
      commentResponse.setException(true);
      commentResponse.addMessage("You are not the part of this club");
      return commentResponse;
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

      // final publish how many comments
      postVO.setNumComment(postVO.getNumComment() + 1);
      postVO.setUpdatedDate(new Date());

      getBaseESService().put(postVO);

      // if users like it then send notification
      getNotificationService().sendCommentNotification(commentVO.getId());


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

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(hasChildFilterBuilder)
            .addSortedMap("postId", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

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
  public UserPostResponse fetchUsersPosts(UserProfileRequest userProfileRequest) {
    long totalHits=0;
    int pageNo = userProfileRequest.getPageNo();
    int perPage = userProfileRequest.getPerPage();

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    UserResponse requestUser = getUserService().findById(userProfileRequest.getUserId());
    if (requestUser == null) {
      throw new PotaliRuntimeException("You cannot see posts of a ghost user");
    }

    if (!requestUser.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("You cannot see post of users of other institutions");
    }

    BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
    boolFilterBuilder.must(FilterBuilders.termFilter("userId", userResponse.getId()));

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(boolFilterBuilder)
            .addSortedMap("postId", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostVO.class);
    List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      PostVO postVO = (PostVO) baseElasticVO;
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = new GenericPostResponse(postVO, postUser);
      genericPostResponseList.add(genericPostResponse);
    }
    UserPostResponse postResponse = new UserPostResponse();
    postResponse.setPosts(genericPostResponseList);
    postResponse.setPageNo(pageNo);
    postResponse.setPerPage(perPage);
    postResponse.setTotalResults(totalHits);
    Integer integer = userResponse.getCircleList().size();
    postResponse.setTotalCircles(Long.parseLong(integer.toString()));


    return postResponse;
  }

  @Override
  public UserPostResponse fetchCirclePosts(CirclePostRequest circlePostRequest) {
    long totalHits=0;
    int pageNo = circlePostRequest.getPageNo();
    int perPage = circlePostRequest.getPerPage();

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    CircleVO circleVO = (CircleVO)
        getBaseESService().get(circlePostRequest.getCircleId(), null, CircleVO.class);

    if (circleVO == null) {
      throw new PotaliRuntimeException("You cannot see posts of a ghost circle");
    }

    if (!circleVO.isActive()) {
      throw new PotaliRuntimeException("Circle is deactivated, you cannot see its posts");
    }



    if (!circleVO.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("You cannot see post of users of other institutions");
    }

    TermFilterBuilder termFilterBuilder = FilterBuilders.termFilter("circleList.id", circleVO.getId());

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(termFilterBuilder)
            .addSortedMap("postId", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostVO.class);
    List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      PostVO postVO = (PostVO) baseElasticVO;
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = new GenericPostResponse(postVO, postUser);
      genericPostResponseList.add(genericPostResponse);
    }
    UserPostResponse postResponse = new UserPostResponse();
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
        commentResponse.setCommentId(commentVO.getId());
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
  public boolean isPostLikedForUser(Long postId, Long userId) {
    boolean isImp = false;

    if (userId == null) {
      throw new InValidInputException("USER_ID_CANNOT_BE_NULL");
    }
    if (postId == null) {
      throw new InValidInputException("POST_ID_CANNOT_BE_NULL");
    }

    AndFilterBuilder andFilterBuilder = FilterBuilders.andFilter(FilterBuilders.termFilter("userId", userId),
        FilterBuilders.termFilter("postId", postId), FilterBuilders.termFilter("reactionId", EnumReactions.LIKE_IT.getId()));

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(andFilterBuilder);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostReactionVO.class);
    return esSearchResponse.getTotalResults() > 0;
  }

  @Override
  public boolean isPostMarkHiddenOrSpammed(Long postId, Long userId) {
    if (userId == null) {
      throw new InValidInputException("USER_ID_CANNOT_BE_NULL");
    }
    if (postId == null) {
      throw new InValidInputException("POST_ID_CANNOT_BE_NULL");
    }

    Long[] REMOVE_LIST = {EnumReactions.HIDE_THIS_POST.getId(), EnumReactions.MARK_AS_SPAM.getId()};
    AndFilterBuilder andFilterBuilder = FilterBuilders.andFilter(FilterBuilders.termFilter("userId", userId),
        FilterBuilders.termFilter("postId", postId), FilterBuilders.inFilter("reactionId", REMOVE_LIST));

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

  /*@Override
  @Transactional(propagation = Propagation.REQUIRED)
  public List<CreateAttachmentResponseDto> postImages(List<FormDataBodyPart> imageList, final Long postId) {
    boolean isUploadSuccessful = false;
    List<CreateAttachmentResponseDto> createAttachmentResponseDtoList = new ArrayList<CreateAttachmentResponseDto>();
    List<ImageDto> imageDtoList = new ArrayList<ImageDto>();

    for (final FormDataBodyPart image : imageList) {

      Future<ImageDto> future = FileUploadService.EXECUTOR_SERVICE.submit(new Callable<ImageDto>() {
        @Override
        public ImageDto call() throws Exception {
          List<FormDataBodyPart> formDataBodyParts = new ArrayList<FormDataBodyPart>();
          formDataBodyParts.add(image);
          boolean isUploadedToDisk = getFileUploadService().uploadFiles(formDataBodyParts, postId, EnumAttachmentType.IMAGE);
          if (!isUploadedToDisk) {
            logger.error("Image could not be downloaded to server from client for post "+postId);
            throw new PotaliRuntimeException("Some internal exception occurred in posting");
          }

          String absolutePath = getFileUploadService().getAbsolutePath(postId, EnumAttachmentType.IMAGE);
          String uploadRootPath = getFileUploadService().getUploadPath();
          String relativePath = getFileUploadService().getRelativePath(postId, EnumAttachmentType.IMAGE);

          ImageDto imageDto = new ImageNameBuilder().addBucket(EnumBucket.POST_BUCKET)
              .addRootFolder(uploadRootPath).addUploadFolderName(relativePath)
              .addFileName(image.getContentDisposition().getFileName())
              .addSize(EnumImageSize.FIT).build();

          return imageDto;

        }
      });


      try {
        ImageDto imageDto = future.get();
        if (imageDto != null) {
          logger.info("Image is uploaded to server" + imageDto.getFileName());
          imageDtoList.add(imageDto);

          String path = imageDto.getRelativePath() + File.separator + imageDto.getFileName();

          Attachment attachment = getAttachmentDao()
              .createAttachment(EnumAttachmentType.IMAGE, path, EnumImageSize.getImageSizeById(imageDto.getSize()), postId);

          //attachment = (Attachment) getAttachmentDao().save(attachment);

          imageDto.setAttachmentId(attachment.getId());
          imageDtoList.add(imageDto);

        }
      } catch (Exception e) {
        logger.error("Async upload for docs failed for post "+postId,e);
      }

      for (final ImageDto imageDto : imageDtoList) {

        Future<CreateAttachmentResponseDto> futureCloud = FileUploadService.EXECUTOR_SERVICE.submit(new Callable<CreateAttachmentResponseDto>() {
          @Override
          public CreateAttachmentResponseDto call() throws Exception {
            Map<String,Object> map = getUploadService().uploadImageToCloud(postId, imageDto);

            Attachment attachment = getAttachmentDao().get(Attachment.class, imageDto.getAttachmentId());
            attachment.setWidth(((Long) map.get("width")).intValue());
            attachment.setHeight(((Long) map.get("height")).intValue());
            attachment.setVersion((Long) map.get("version"));
            attachment.setPublicId((String) map.get("public_id"));
            attachment.setFormat(EnumImageFormat.getImageFormatByString((String) map.get("format")));
            attachment = (Attachment) getAttachmentDao().save(attachment);

            //attachmentList.add(attachment);

            CreateAttachmentResponseDto createAttachmentResponseDto = null;
            if (attachment != null) {
              createAttachmentResponseDto = new CreateAttachmentResponseDto();
              createAttachmentResponseDto.setPath(attachment.getPath());
              createAttachmentResponseDto.setEnumImageSize(EnumImageSize.getImageSizeById(attachment.getSize()));
              createAttachmentResponseDto.setId(attachment.getId());
              createAttachmentResponseDto.setFormat(EnumImageFormat.getImageFormatById(attachment.getFormat()));
              createAttachmentResponseDto.setPublicId(attachment.getPublicId());
              createAttachmentResponseDto.setVersion(attachment.getVersion());
            }

            return createAttachmentResponseDto;

          }
        });

        try {
          CreateAttachmentResponseDto createAttachmentResponseDto = futureCloud.get();
          if (createAttachmentResponseDto != null) {
            createAttachmentResponseDtoList.add(createAttachmentResponseDto);
          }
        } catch (Exception e) {
          logger.error("Async upload for docs failed for post "+postId,e);
        }

      }



    }


    return createAttachmentResponseDtoList;
  }
*/
  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public List<CreateAttachmentResponseDto> postImages(List<FormDataBodyPart> imageList, final Long postId) {
    int size = imageList.size();
    List<CreateAttachmentResponseDto> createAttachmentResponseDtoList = new ArrayList<CreateAttachmentResponseDto>();
    ExecutorService executorService = Executors.newFixedThreadPool(size+size);
    //List<Attachment> attachmentList = new ArrayList<Attachment>();
    List<AttachmentMap> attachmentMapList = new ArrayList<AttachmentMap>();
    List<Callable<AttachmentDto>> uploaderTasks = new ArrayList<Callable<AttachmentDto>>();
    for (FormDataBodyPart formDataBodyPart : imageList) {
      String uploadPath = appProperties.getUploadPicPath();
      uploaderTasks.add(new AttachmentUploaderTask(formDataBodyPart, uploadPath, postId, EnumAttachmentType.IMAGE));
    }
    //Important - Not using CompletionService executor as it main thread in non blocking queue as soon as
    // one threads completes
    // hence using Normal executor method
    //CompletionService<AttachmentDto> uploadCompletionTasks = new ExecutorCompletionService<AttachmentDto>(executorService);
    Set<Future<AttachmentDto>> uploadCompletionTasks = new HashSet<Future<AttachmentDto>>();

    // first upload to server
    try {
      for (Callable<AttachmentDto> task : uploaderTasks) {
        uploadCompletionTasks.add(executorService.submit(task));
      }

      for (Future<AttachmentDto> attachmentDtoFuture : uploadCompletionTasks) {
        //Future<AttachmentDto> attachmentDtoFuture = uploadCompletionTasks.take();
        AttachmentDto attachmentDto = attachmentDtoFuture.get();
        if (attachmentDto.getUploaded()) {
          String path = attachmentDto.getRelativePath() + File.separator + attachmentDto.getFileName();

          Attachment attachment = getAttachmentDao()
              .createAttachment(EnumAttachmentType.IMAGE, path,
                  EnumImageSize.FIT, postId);

          //attachmentList.add(attachment);
          AttachmentMap attachmentMap = new AttachmentMap();
          attachmentMap.setAttachment(attachment);
          attachmentMap.setAttachmentDto(attachmentDto);
          attachmentMapList.add(attachmentMap);
        }

      }


    } catch (Exception e) {
      logger.error("Error in uploading images ",e);
    }

    // now upload attachments to cloudinary
    List<Callable<Attachment>> cloudTasks = new ArrayList<Callable<Attachment>>();
    for (AttachmentMap attachmentMap : attachmentMapList) {
      String cloudName = appProperties.getCloudName();
      String apiKey = appProperties.getCloudApiKey();
      String apiSecret = appProperties.getCloudSecKey();
      String relativePath = attachmentMap.getAttachmentDto().getRelativePath();
      String path = attachmentMap.getAttachmentDto().getAbsolutePath() + File.separator +
          attachmentMap.getAttachmentDto().getFileName();

      Attachment attachment = attachmentMap.getAttachment();
      cloudTasks.add(new AttachmentCloudTask(cloudName,apiKey,apiSecret,relativePath,path, attachment));
    }

    //CompletionService<Attachment> cloudCompletionTasks = new ExecutorCompletionService<Attachment>(executorService);
    Set<Future<Attachment>> cloudCompletionTasks = new HashSet<Future<Attachment>>();
    try {
      for (Callable<Attachment> task : cloudTasks) {
        cloudCompletionTasks.add(executorService.submit(task));
      }

      for (Future<Attachment> attachmentFuture : cloudCompletionTasks) {
        //Future<Attachment> attachmentFuture = cloudCompletionTasks.take();

        Attachment attachment = (Attachment) getAttachmentDao().save(attachmentFuture.get());
        CreateAttachmentResponseDto createAttachmentResponseDto = null;
        if (attachment != null) {
          createAttachmentResponseDto = new CreateAttachmentResponseDto();
          createAttachmentResponseDto.setPath(attachment.getPath());
          createAttachmentResponseDto.setEnumImageSize(EnumImageSize.getImageSizeById(attachment.getSize()));
          createAttachmentResponseDto.setId(attachment.getId());
          createAttachmentResponseDto.setFormat(EnumImageFormat.getImageFormatById(attachment.getFormat()));
          createAttachmentResponseDto.setPublicId(attachment.getPublicId());
          createAttachmentResponseDto.setVersion(attachment.getVersion());
        }
        createAttachmentResponseDtoList.add(createAttachmentResponseDto);
      }
    } catch (Exception e) {
      logger.error("Error in uploading images ",e);
    }


    executorService.shutdown();
    return createAttachmentResponseDtoList;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public List<CreateAttachmentResponseDto> postRawFiles(List<FormDataBodyPart> docList, final Long postId) {
    int size = docList.size();
    List<CreateAttachmentResponseDto> createAttachmentResponseDtoList = new ArrayList<CreateAttachmentResponseDto>();
    ExecutorService executorService = Executors.newFixedThreadPool(size+size);
    //List<Attachment> attachmentList = new ArrayList<Attachment>();
    List<AttachmentMap> attachmentMapList = new ArrayList<AttachmentMap>();
    List<Callable<AttachmentDto>> uploaderTasks = new ArrayList<Callable<AttachmentDto>>();
    for (FormDataBodyPart formDataBodyPart : docList) {
      String uploadPath = appProperties.getUploadPicPath();
      uploaderTasks.add(new AttachmentUploaderTask(formDataBodyPart, uploadPath, postId, EnumAttachmentType.DOC));
    }
    //Important - Not using CompletionService executor as it main thread in non blocking queue as soon as
    // one threads completes
    // hence using Normal executor method
    //CompletionService<AttachmentDto> uploadCompletionTasks = new ExecutorCompletionService<AttachmentDto>(executorService);
    Set<Future<AttachmentDto>> uploadCompletionTasks = new HashSet<Future<AttachmentDto>>();

    // first upload to server
    try {
      for (Callable<AttachmentDto> task : uploaderTasks) {
        uploadCompletionTasks.add(executorService.submit(task));
      }

      for (Future<AttachmentDto> attachmentDtoFuture : uploadCompletionTasks) {
        //Future<AttachmentDto> attachmentDtoFuture = uploadCompletionTasks.take();
        AttachmentDto attachmentDto = attachmentDtoFuture.get();
        if (attachmentDto.getUploaded()) {
          String path = attachmentDto.getRelativePath() + File.separator + attachmentDto.getFileName();

          Attachment attachment = getAttachmentDao()
              .createAttachment(EnumAttachmentType.DOC, path,
                  EnumImageSize.FIT, postId);

          //attachmentList.add(attachment);
          AttachmentMap attachmentMap = new AttachmentMap();
          attachmentMap.setAttachment(attachment);
          attachmentMap.setAttachmentDto(attachmentDto);
          attachmentMapList.add(attachmentMap);
        }

      }


    } catch (Exception e) {
      logger.error("Error in uploading images ",e);
    }

    // now upload attachments to cloudinary
    List<Callable<Attachment>> cloudTasks = new ArrayList<Callable<Attachment>>();
    for (AttachmentMap attachmentMap : attachmentMapList) {
      String cloudName = appProperties.getCloudName();
      String apiKey = appProperties.getCloudApiKey();
      String apiSecret = appProperties.getCloudSecKey();
      String relativePath = attachmentMap.getAttachmentDto().getRelativePath();
      String path = attachmentMap.getAttachmentDto().getAbsolutePath() + File.separator +
          attachmentMap.getAttachmentDto().getFileName();

      Attachment attachment = attachmentMap.getAttachment();
      cloudTasks.add(new RawAttachmentCloudTask(cloudName,apiKey,apiSecret,relativePath,path, attachment));
    }

    //CompletionService<Attachment> cloudCompletionTasks = new ExecutorCompletionService<Attachment>(executorService);
    Set<Future<Attachment>> cloudCompletionTasks = new HashSet<Future<Attachment>>();
    try {
      for (Callable<Attachment> task : cloudTasks) {
        cloudCompletionTasks.add(executorService.submit(task));
      }

      for (Future<Attachment> attachmentFuture : cloudCompletionTasks) {
        //Future<Attachment> attachmentFuture = cloudCompletionTasks.take();

        Attachment attachment = (Attachment) getAttachmentDao().save(attachmentFuture.get());
        CreateAttachmentResponseDto createAttachmentResponseDto = null;
        if (attachment != null) {
          createAttachmentResponseDto = new CreateAttachmentResponseDto();
          createAttachmentResponseDto.setPath(attachment.getPath());
          createAttachmentResponseDto.setEnumImageSize(EnumImageSize.getImageSizeById(attachment.getSize()));
          createAttachmentResponseDto.setId(attachment.getId());
          createAttachmentResponseDto.setFormat(EnumImageFormat.getImageFormatById(attachment.getFormat()));
          createAttachmentResponseDto.setPublicId(attachment.getPublicId());
          createAttachmentResponseDto.setVersion(attachment.getVersion());
        }
        createAttachmentResponseDtoList.add(createAttachmentResponseDto);
      }
    } catch (Exception e) {
      logger.error("Error in uploading images ",e);
    }


    executorService.shutdown();
    return createAttachmentResponseDtoList;
  }

 /* @Override
  public List<CreateAttachmentResponseDto> postRawFiles(List<FormDataBodyPart> docList, final Long postId) {
    int numFiles = docList.size();
    int uploadCount = 0;
    List<CreateAttachmentResponseDto> createAttachmentResponseDtoList = new ArrayList<CreateAttachmentResponseDto>();

    for (final FormDataBodyPart formDataBodyPart : docList) {
      Future<CreateAttachmentResponseDto> future = FileUploadService.EXECUTOR_SERVICE.submit(new Callable<CreateAttachmentResponseDto>() {
        @Override
        public CreateAttachmentResponseDto call() throws Exception {
          List<FormDataBodyPart> formDataBodyParts = new ArrayList<FormDataBodyPart>();
          formDataBodyParts.add(formDataBodyPart);
          boolean isUploadedToDisk = getFileUploadService().uploadFiles(formDataBodyParts, postId, EnumAttachmentType.DOC);

          if (!isUploadedToDisk) {
            logger.error("Image could not be downloaded to server from client for post "+postId);
            return null;
          }

          // upload to cloud now
          String absolutePath = getFileUploadService().getAbsolutePath(postId, EnumAttachmentType.IMAGE);
          String relativePath = getFileUploadService().getRelativePath(postId, EnumAttachmentType.IMAGE);
          String path = relativePath + File.separator + formDataBodyPart.getContentDisposition().getFileName();
          String filePath = absolutePath + File.separator + formDataBodyPart.getContentDisposition().getFileName();

          Attachment attachment = getAttachmentDao()
              .createAttachment(EnumAttachmentType.DOC, path, EnumImageSize.MEDIUM, postId);

          Map<String,Object> map = getUploadService().uploadRawFilesToCloud(new File(filePath),
              EnumAttachmentType.DOC, attachment.getId(), relativePath);

          attachment.setWidth(EnumImageSize.MEDIUM.getWidth());
          attachment.setHeight(EnumImageSize.MEDIUM.getHeight());
          attachment.setVersion((Long) map.get("version"));
          attachment.setPublicId((String) map.get("public_id"));
          attachment.setFormat(EnumImageFormat.getImageFormatByString((String) map.get("format")));
          attachment = (Attachment)getAttachmentDao().save(attachment);


          CreateAttachmentResponseDto createAttachmentResponseDto = null;
          if (attachment != null) {
            createAttachmentResponseDto = new CreateAttachmentResponseDto();
            createAttachmentResponseDto.setPath(attachment.getPath());
            createAttachmentResponseDto.setEnumImageSize(EnumImageSize.getImageSizeById(attachment.getSize()));
            createAttachmentResponseDto.setId(attachment.getId());
            createAttachmentResponseDto.setFormat(EnumImageFormat.getImageFormatById(attachment.getFormat()));
            createAttachmentResponseDto.setPublicId(attachment.getPublicId());
            createAttachmentResponseDto.setVersion(attachment.getVersion());
          }

          return createAttachmentResponseDto;
        }
      });

      //TODO should we use timeout here, evaluate later

      try {
        CreateAttachmentResponseDto attachmentResponseDto = future.get();
        if (attachmentResponseDto != null) {
          uploadCount += 1;
          createAttachmentResponseDtoList.add(attachmentResponseDto);
        }
      } catch (Exception e) {
        logger.error("Async upload for docs failed for post "+postId,e);
      }

    }
    return createAttachmentResponseDtoList;
  }*/

  @Override
  public PostFiltersResponse getPostFilters() {
    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    PostFiltersResponse postFiltersResponse = new PostFiltersResponse();


    List<Long> circleList = userResponse.getCircleList();
    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (Long circle : circleList) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circle, null, CircleVO.class);
      if (circleVO != null) {
        CircleDto circleDto = new CircleDto();
        circleDto.setId(circleVO.getId());
        circleDto.setName(circleVO.getName());
        circleDto.setSelected(false);
        circleDtoList.add(circleDto);
      }
    }

    GeneralFilterDto generalFilterDto = new GeneralFilterDto(DtoJsonConstants.GENERAL);
    generalFilterDto.setCircleDtoList(circleDtoList);
    postFiltersResponse.setGenFilterDto(generalFilterDto);


    JobFilterDto jobFilterDto = getJobFilters();

    Map<String, BaseFilterDto> specificFilters = new HashMap<String, BaseFilterDto>();
    specificFilters.put(DtoJsonConstants.JOBS, jobFilterDto);
    postFiltersResponse.setSpecificFilterMap(specificFilters);

    return postFiltersResponse;
  }

  @Override
  @Transactional
  public GenericPostReactionResponse reverseReaction(PostReactionRequest postReactionRequest) {
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

    Long reactionId = EnumReactions.MARK_AS_IMPORTANT.getId();
    if (postReactionRequest.getActionId().equals(EnumReactions.MARK_AS_UN_IMPORTANT.getId())) {
      reactionId = EnumReactions.MARK_AS_IMPORTANT.getId();
    } else {
      reactionId = EnumReactions.HIDE_THIS_POST.getId();
    }


    // first reverse it in DB and then In ES
    PostReactions postReactions = getPostReactionDao().getPostReactionByReactionAndPostId(reactionId,
        postReactionRequest.getPostId(), userResponse.getId());

    if (postReactions != null) {
      postReactions.setReactionId(postReactionRequest.getActionId());
      postReactions = (PostReactions) getPostReactionDao().save(postReactions);
    }


    AndFilterBuilder andFilterBuilder = FilterBuilders.andFilter(FilterBuilders.termFilter("userId", userResponse.getId()),
        FilterBuilders.termFilter("postId", postReactionRequest.getPostId()), FilterBuilders.termFilter("reactionId",reactionId));



    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(andFilterBuilder);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostReactionVO.class);
    boolean published = false;
    List<BaseElasticVO> postReactionVOs = esSearchResponse.getBaseElasticVOs();
    if (postReactionVOs != null && !postReactionVOs.isEmpty()) {
      PostReactionVO postReactionVO = (PostReactionVO)postReactionVOs.get(0);
      postReactionVO.setReactionId(postReactionRequest.getActionId());

      published = getBaseESService().put(postReactionVO);
    } else {
      PostReactionVO postReactionVO = new PostReactionVO(postReactions);
      //boolean published = getEsCacheService().put(ESIndexKeys.REACTION_INDEX, postReactionVO, postReactionVO.getPostId());
      published = getBaseESService().put(postReactionVO);
    }

    if (!published) {
      throw new PotaliRuntimeException("Something went wrong! Please try again!");
    }

    if (EnumReactions.HIDE_THIS_POST.getId().equals(reactionId)) {
      postVO.setNumHides(postVO.getNumHides() - 1);
    }

    if (EnumReactions.MARK_AS_IMPORTANT.getId().equals(reactionId)) {
      postVO.setNumImportant(postVO.getNumImportant() - 1);
    }

    try {
      getBaseESService().put(postVO);
    } catch (Exception ex){
      logger.error("Error while saving reaction count to ES");
    }


    GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
    genericPostReactionResponse.setSuccess(true);



    return genericPostReactionResponse;
  }

  @Override
  public UserProfileResponse fetchUserProfile(UserProfileRequest userProfileRequest) {
    long totalHits=0;
    int pageNo = userProfileRequest.getPageNo();
    int perPage = userProfileRequest.getPerPage();

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    UserResponse requestUser = getUserService().findById(userProfileRequest.getUserId());
    if (requestUser == null) {
      throw new PotaliRuntimeException("You cannot see posts of a ghost user");
    }

    if (!requestUser.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("You cannot see post of users of other institutions");
    }

    BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
    boolFilterBuilder.must(FilterBuilders.termFilter("userId", userProfileRequest.getUserId()));

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(boolFilterBuilder)
            .addSortedMap("postId", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostVO.class);
    totalHits = esSearchResponse.getTotalResults();
    List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      PostVO postVO = (PostVO) baseElasticVO;
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = new GenericPostResponse(postVO, postUser);
      genericPostResponseList.add(genericPostResponse);
    }

    List<Long> circleList = BaseUtil.getPaginatedList(requestUser.getCircleList(), pageNo, perPage);
    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    if (circleList != null) {
      for (Long circleId : circleList) {

        CircleVO circleVO = (CircleVO)
            getBaseESService().get(circleId, null, CircleVO.class);

        if (circleVO == null || !circleVO.isActive()) {
          continue;
        }

        QueryBuilder queryBuilder = QueryBuilders.termQuery("circleList", circleVO.getId());
        long members = getBaseESService().count(queryBuilder, UserVO.class);

        // calculate number of posts
        queryBuilder = QueryBuilders.termQuery("circleList.id", circleVO.getId());
        long posts = getBaseESService().count(queryBuilder, PostVO.class);

        CircleDto circleDto = new CircleDto();
        circleDto.setId(circleVO.getId());
        circleDto.setName(circleVO.getName());
        if (userResponse.getCircleList().contains(circleVO.getId())) {
          circleDto.setJoined(true);
        }

        circleDto.setPosts(posts);
        circleDto.setMembers(members);
        circleDto.setModerate(circleVO.isModerate());
        circleDtoList.add(circleDto);
      }
    }


    UserProfileResponse userProfileResponse = new UserProfileResponse();
    userProfileResponse.setPageNo(pageNo);
    userProfileResponse.setPerPage(perPage);
    userProfileResponse.setFirstName(requestUser.getFirstName());
    userProfileResponse.setLastName(requestUser.getLastName());
    userProfileResponse.setName(requestUser.getName());
    userProfileResponse.setImage(requestUser.getImage());
    userProfileResponse.setYearOfGrad(requestUser.getYearOfGrad());
    userProfileResponse.setPosts(genericPostResponseList);
    userProfileResponse.setTotalPosts(totalHits);
    userProfileResponse.setCircleDtoList(circleDtoList);
    userProfileResponse.setTotalCircle(requestUser.getCircleList().size());

    return userProfileResponse;
  }

  @Override
  public CircleProfileResponse fetchCircleProfile(CirclePostRequest circlePostRequest) {
    long totalHits=0;
    int pageNo = circlePostRequest.getPageNo();
    int perPage = circlePostRequest.getPerPage();

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    CircleVO circleVO = (CircleVO)
        getBaseESService().get(circlePostRequest.getCircleId(), null, CircleVO.class);

    if (circleVO == null) {
      throw new PotaliRuntimeException("You cannot see posts of a ghost circle");
    }

    if (!circleVO.isActive()) {
      throw new PotaliRuntimeException("Circle is deactivated, you cannot see its posts");
    }



    if (!circleVO.getInstituteId().equals(userResponse.getInstituteId())) {
      throw new PotaliRuntimeException("You cannot see post of users of other institutions");
    }

    TermFilterBuilder termFilterBuilder = FilterBuilders.termFilter("circleList.id", circleVO.getId());

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(termFilterBuilder)
            .addSortedMap("postId", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostVO.class);
    totalHits = esSearchResponse.getTotalResults();
    List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      PostVO postVO = (PostVO) baseElasticVO;
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = new GenericPostResponse(postVO, postUser);
      genericPostResponseList.add(genericPostResponse);
    }

    termFilterBuilder = FilterBuilders.termFilter("circleList", circleVO.getId());

    esSearchFilter =
        new ESSearchFilter().setFilterBuilder(termFilterBuilder)
            .addSortedMap("id", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    esSearchResponse = getBaseESService().search(esSearchFilter, UserVO.class);
    long totalUsers = esSearchResponse.getTotalResults();
    baseElasticVOs = esSearchResponse.getBaseElasticVOs();
    List<UserDto> userDtoList = new ArrayList<UserDto>();
    for (BaseElasticVO baseElasticVO : baseElasticVOs) {
      UserVO userVO = (UserVO) baseElasticVO;

      UserCircleMapping userCircleMapping =
          (UserCircleMapping) getCircleDao().findUniqueByNamedQueryAndNamedParam("findByUserAndCircle",
              new String[]{"userId", "circleId"}, new Object[]{userVO.getId(), circleVO.getId()});

      if (userCircleMapping == null) {
        continue;
      }
      UserDto userDto = new UserDto();
      userDto.setId(userVO.getId());
      userDto.setYearOfGrad(userVO.getYearOfGrad());
      userDto.setName(userVO.getAccountName());
      userDto.setEmailId(userVO.getEmail());
      userDto.setImage(userVO.getImage());
      userDto.setCircles(userVO.getCircleList().size());
      Date memberSince = userCircleMapping.getCreatedDate();
      if (memberSince == null) {
        memberSince = new Date();
      }
      userDto.setMemberSince(DateUtils.getMemberSince(memberSince));

      userDtoList.add(userDto);
    }
    CircleProfileResponse circleProfileResponse = new CircleProfileResponse();
    circleProfileResponse.setId(circleVO.getId());
    circleProfileResponse.setName(circleVO.getName());
    circleProfileResponse.setDesc(circleVO.getDesc());
    circleProfileResponse.setModerate(circleVO.isModerate());
    if (!userResponse.getCircleList().contains(circleVO.getId())) {
      circleProfileResponse.setJoined(false);
    } else {
      circleProfileResponse.setJoined(true);
    }

    circleProfileResponse.setPosts(genericPostResponseList);
    circleProfileResponse.setPageNo(pageNo);
    circleProfileResponse.setPerPage(perPage);
    circleProfileResponse.setTotalPosts(totalHits);

    circleProfileResponse.setUserDtoList(userDtoList);
    circleProfileResponse.setMembers(totalUsers);

    return circleProfileResponse;
  }

  //tODO: remove later
  private JobFilterDto getJobFilters() {
    JobFilterDto jobFilterDto = new JobFilterDto(DtoJsonConstants.JOBS);

    List<CityDto> cityDtoList = new ArrayList<CityDto>();
    List<CityVO> cityVOList = CityCache.getCache().getCityVO();
    for (CityVO cityVO : cityVOList) {
      CityDto cityDto = new CityDto();
      cityDto.setId(cityVO.getId());
      cityDto.setName(cityVO.getName());
      cityDtoList.add(cityDto);
    }

    //List<IndustryRolesVO> industryRolesVOList = IndustryRolesCache.getCache().getAllIndustryRolesVO();

    /*for (IndustryRolesVO industryRolesVO : industryRolesVOList) {
      IndustryRolesDto industryRolesDto = new IndustryRolesDto();
      industryRolesDto.setId(industryRolesVO.getId());
      industryRolesDto.setName(industryRolesVO.getName());
      industryRolesDto.setIndustryId(industryRolesVO.getId());
      industryRolesDtoList.add(industryRolesDto);
    }*/

    List<IndustryDto> industryDtoList = new ArrayList<IndustryDto>();
    List<IndustryVO> industryVOList = IndustryCache.getCache().getAllIndustryVO();
    List<IndustryRolesDto> industryRolesDtoList = new ArrayList<IndustryRolesDto>();
    for (IndustryVO industryVO : industryVOList) {
      IndustryDto industryDto = new IndustryDto();
      industryDto.setId(industryVO.getId());
      industryDto.setName(industryVO.getName());
      industryDtoList.add(industryDto);

      // get all roles
      List<Long> rolesList = IndustryCache.getCache().getIndustryRolesListFromIndustryId(industryVO.getId());

      for (Long rolesId : rolesList) {
        IndustryRolesVO industryRolesVO = IndustryRolesCache.getCache().getIndustryRolesVO(rolesId);
        IndustryRolesDto industryRolesDto = new IndustryRolesDto();
        industryRolesDto.setId(industryRolesVO.getId());
        industryRolesDto.setName(industryRolesVO.getName());
        industryRolesDto.setIndustryId(industryRolesVO.getId());
        industryRolesDtoList.add(industryRolesDto);
      }
      industryDto.setIndustryRolesDtoList(industryRolesDtoList);
    }



    // max salary and min salary, max exp and min exp
    MinBuilder minFrom = AggregationBuilders.min("from_aggs").field("from");
    MaxBuilder maxTo = AggregationBuilders.max("to_aggs").field("to");

    MinBuilder salaryMin = AggregationBuilders.min("salary_from").field("salaryFrom");
    MaxBuilder salaryMax = AggregationBuilders.max("salary_to").field("salaryTo");

    SearchResponse searchResponse = ESCacheManager.getInstance().getClient().prepareSearch("ofc").setTypes("job")
        .setQuery(QueryBuilders.matchAllQuery()).addAggregation(minFrom).addAggregation(maxTo)
        .addAggregation(salaryMin).addAggregation(salaryMax).execute().actionGet();

    jobFilterDto.setCityList(cityDtoList);
    jobFilterDto.setIndList(industryDtoList);
    jobFilterDto.setRoDtoList(industryRolesDtoList);

    if (searchResponse.status().getStatus() == HttpStatus.OK.value()) {
      Min minExp = searchResponse.getAggregations().get("from_aggs");
      Max maxExp = searchResponse.getAggregations().get("to_aggs");
      Min minSal = searchResponse.getAggregations().get("salary_from");
      Max maxSal = searchResponse.getAggregations().get("salary_to");

      SalaryRangeDto salaryRangeDto = new SalaryRangeDto(DtoJsonConstants.SALARY);
      salaryRangeDto.setMin(minSal.getValue());
      salaryRangeDto.setMax(maxSal.getValue());


      ExperienceRangeDto experienceRangeDto = new ExperienceRangeDto(DtoJsonConstants.EXPERIENCE);
      experienceRangeDto.setMin(new Double(minExp.getValue()).intValue());
      experienceRangeDto.setMax(new Double(maxExp.getValue()).intValue());

      Map<String, BaseRangeDto> rangeDtoMap = new HashMap<String, BaseRangeDto>();
      rangeDtoMap.put(salaryRangeDto.getName(), salaryRangeDto);
      rangeDtoMap.put(experienceRangeDto.getName(), experienceRangeDto);

      jobFilterDto.setRangeDtoMap(rangeDtoMap);
    }

    return jobFilterDto;
  }

  private GenericPostReactionResponse generatePostReactionResponse(PostVO postVO, PostReactionVO postReactionVO) {

    if (EnumReactions.REPLY_VIA_EMAIL.getId().equals(postReactionVO.getReactionId())) {
      ReplyEmailReactionResponse replyEmailReactionResponse = new ReplyEmailReactionResponse();
      replyEmailReactionResponse.setSuccess(true);
      replyEmailReactionResponse.setReplyEmail(postVO.getReplyEmail());
      postVO.setNumReplies(postVO.getNumReplies() +  1);
      return replyEmailReactionResponse;
    } else if (EnumReactions.REPLY_VIA_PHONE.getId().equals(postReactionVO.getReactionId())) {
      ReplyPhoneEmailReactionResponse replyPhoneEmailReactionResponse = new ReplyPhoneEmailReactionResponse();
      replyPhoneEmailReactionResponse.setSuccess(true);
      replyPhoneEmailReactionResponse.setReplyPhone(postVO.getReplyPhone());
      postVO.setNumReplies(postVO.getNumReplies() +  1);
      return replyPhoneEmailReactionResponse;
    } else if (EnumReactions.REPLY_VIA_WATSAPP.getId().equals(postReactionVO.getReactionId())) {
      ReplyWatsAppReactionResponse replyWatsAppReactionResponse = new ReplyWatsAppReactionResponse();
      replyWatsAppReactionResponse.setSuccess(true);
      replyWatsAppReactionResponse.setWatsApp(postVO.getReplyWatsApp());
      postVO.setNumReplies(postVO.getNumReplies() +  1);
      return replyWatsAppReactionResponse;
    } else if (EnumReactions.SHARE_VIA_EMAIL.getId().equals(postReactionVO.getReactionId()) ||
        EnumReactions.SHARE_VIA_PHONE.getId().equals(postReactionVO.getReactionId()) ||
        EnumReactions.SHARE_VIA_WATSAPP.getId().equals(postReactionVO.getReactionId())) {

      ShareReactionResponse shareReactionResponse = new ShareReactionResponse();
      shareReactionResponse.setSuccess(true);
      shareReactionResponse.setContent(postVO.getContent());
      postVO.setNumShared(postVO.getNumShared() + 1);
      return shareReactionResponse;

    } else if (EnumReactions.COMMENT.getId().equals(postReactionVO.getReactionId())) {
      CommentPostReactionResponse commentPostReactionResponse = new CommentPostReactionResponse();
      commentPostReactionResponse.setSuccess(true);
      commentPostReactionResponse.setPostReactionId(postReactionVO.getId());
      postVO.setNumComment(postVO.getNumComment() + 1);
      return commentPostReactionResponse;
    } else {

      if (EnumReactions.HIDE_THIS_POST.getId().equals(postReactionVO.getId())) {
        postVO.setNumHides(postVO.getNumHides() + 1);
      }

      if (EnumReactions.MARK_AS_IMPORTANT.getId().equals(postReactionVO.getId())) {
        postVO.setNumImportant(postVO.getNumImportant() + 1);
      }


      if (EnumReactions.MARK_AS_SPAM.getId().equals(postReactionVO.getId())) {
        postVO.setNumSpam(postVO.getNumSpam() + 1);
      }

      if (EnumReactions.LIKE_IT.getId().equals(postReactionVO.getId())) {
        postVO.setNumLikes(postVO.getNumLikes() + 1);
      }

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

  public FileUploadService getFileUploadService() {
    return fileUploadService;
  }

  public UploadService getUploadService() {
    return uploadService;
  }

  public AttachmentDao getAttachmentDao() {
    return attachmentDao;
  }

  public CircleDao getCircleDao() {
    return circleDao;
  }

  public NotificationService getNotificationService() {
    return notificationService;
  }
}
