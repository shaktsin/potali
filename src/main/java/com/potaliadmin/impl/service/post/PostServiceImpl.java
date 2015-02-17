package com.potaliadmin.impl.service.post;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.constants.attachment.EnumImageFormat;
import com.potaliadmin.constants.cache.ESIndexKeys;
import com.potaliadmin.constants.image.EnumBucket;
import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.domain.comment.Comment;
import com.potaliadmin.domain.reactions.PostReactions;
import com.potaliadmin.dto.internal.cache.address.CityVO;
import com.potaliadmin.dto.internal.cache.es.job.*;
import com.potaliadmin.dto.internal.cache.es.post.PostReactionVO;
import com.potaliadmin.dto.internal.cache.job.IndustryRolesVO;
import com.potaliadmin.dto.internal.cache.job.IndustryVO;
import com.potaliadmin.dto.internal.filter.BaseFilterDto;
import com.potaliadmin.dto.internal.filter.GeneralFilterDto;
import com.potaliadmin.dto.internal.filter.JobFilterDto;
import com.potaliadmin.dto.internal.image.CreateImageResponseDto;
import com.potaliadmin.dto.internal.image.ImageDto;
import com.potaliadmin.dto.web.request.posts.AllPostReactionRequest;
import com.potaliadmin.dto.web.request.posts.BookMarkPostRequest;
import com.potaliadmin.dto.web.request.posts.PostCommentRequest;
import com.potaliadmin.dto.web.request.posts.PostReactionRequest;
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
import com.potaliadmin.pact.dao.attachment.AttachmentDao;
import com.potaliadmin.pact.dao.post.PostCommentDao;
import com.potaliadmin.pact.dao.post.PostReactionDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.framework.utils.FileUploadService;
import com.potaliadmin.pact.service.cache.ESCacheService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.util.image.ImageNameBuilder;
import com.potaliadmin.util.image.ImageProcessUtil;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.comment.CommentVO;
import com.potaliadmin.vo.post.PostVO;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        || postReactionRequest.getActionId().equals(EnumReactions.HIDE_THIS_POST.getId())) {

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

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public List<CreateImageResponseDto> postImages(List<FormDataBodyPart> imageList, Long postId) {
    boolean isUploadSuccessful = false;
    boolean isUploadedToDisk = getFileUploadService().uploadPostImages(imageList, postId);
    if (!isUploadedToDisk) {
      logger.error("Image could not be downloaded to server from client for post "+postId);
      throw new PotaliRuntimeException("Some internal exception occurred in posting");
    }

    // convert post images
    List<ImageDto> imageDtoList = new ArrayList<ImageDto>();
    Integer count = 0;
    for (FormDataBodyPart image : imageList) {
      String absolutePath = getFileUploadService().getAbsolutePath(postId);
      String uploadRootPath = getFileUploadService().getUploadPath();
      String relativePath = getFileUploadService().getRelativePath(postId);
      String reSizedFileName = ImageProcessUtil.reSize(absolutePath, image.getContentDisposition().getFileName(),
          EnumImageSize.FIT, count.toString());
      if (reSizedFileName != null) {
        ImageDto imageDto = new ImageNameBuilder().addBucket(EnumBucket.POST_BUCKET)
            .addRootFolder(uploadRootPath).addUploadFolderName(relativePath).addFileName(reSizedFileName)
            .addSize(EnumImageSize.FIT).build();

        imageDtoList.add(imageDto);
      }
      count++;
    }

    List<Attachment> attachmentList = new ArrayList<Attachment>();
    for (ImageDto imageDto : imageDtoList) {
      String path = imageDto.getRelativePath() + File.separator + imageDto.getFileName();

      Attachment attachment = getAttachmentDao()
          .createAttachment(EnumAttachmentType.IMAGE, path, EnumImageSize.getImageSizeById(imageDto.getSize()), postId);

      imageDto.setAttachmentId(attachment.getId());

      Map<String,Object> map = getUploadService().uploadImageToCloud(postId, imageDto);
      attachment.setWidth(((Long)map.get("width")).intValue());
      attachment.setHeight(((Long) map.get("height")).intValue());
      attachment.setVersion((Long) map.get("version"));
      attachment.setPublicId((String) map.get("public_id"));
      attachment.setFormat(EnumImageFormat.getImageFormatByString((String) map.get("format")));
      getAttachmentDao().save(attachment);

      attachmentList.add(attachment);
    }


    /*boolean uploadedToAWS = getUploadService().uploadPostImages(postId, imageDtoList);
    if (!uploadedToAWS) {
      logger.error("Image could not be uploaded to aws from server "+postId);
      throw new PotaliRuntimeException("Some internal exception occurred in posting");
    }*/

    List<CreateImageResponseDto> createImageResponseDtoList = new ArrayList<CreateImageResponseDto>();
    /*for (ImageDto imageDto : imageDtoList) {
      String path = imageDto.getRelativePath() + File.separator + imageDto.getFileName();

      Attachment attachment = getAttachmentDao()
          .createAttachment(EnumAttachmentType.IMAGE, path, EnumImageSize.getImageSizeById(imageDto.getSize()), postId);

      if (attachment != null) {
        CreateImageResponseDto createImageResponseDto = new CreateImageResponseDto();
        createImageResponseDto.setPath(attachment.getPath());
        createImageResponseDto.setEnumImageSize(EnumImageSize.getImageSizeById(attachment.getSize()));
        createImageResponseDto.setId(attachment.getId());
        createImageResponseDto.setFormat(EnumImageFormat.getImageFormatById(am));
        createImageResponseDtoList.add(createImageResponseDto);
      }

    }*/

    for (Attachment attachment : attachmentList) {

      if (attachment != null) {
        CreateImageResponseDto createImageResponseDto = new CreateImageResponseDto();
        createImageResponseDto.setPath(attachment.getPath());
        createImageResponseDto.setEnumImageSize(EnumImageSize.getImageSizeById(attachment.getSize()));
        createImageResponseDto.setId(attachment.getId());
        createImageResponseDto.setFormat(EnumImageFormat.getImageFormatById(attachment.getFormat()));
        createImageResponseDto.setPublicId(attachment.getPublicId());
        createImageResponseDto.setVersion(attachment.getVersion());
        createImageResponseDtoList.add(createImageResponseDto);
      }

    }
    return createImageResponseDtoList;
  }

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
      getPostReactionDao().save(postReactions);
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
    }

    if (!published) {
      throw new PotaliRuntimeException("Something went wrong! Please try again!");
    }

    GenericPostReactionResponse genericPostReactionResponse = new GenericPostReactionResponse();
    genericPostReactionResponse.setSuccess(published);



    return genericPostReactionResponse;
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

  public FileUploadService getFileUploadService() {
    return fileUploadService;
  }

  public UploadService getUploadService() {
    return uploadService;
  }

  public AttachmentDao getAttachmentDao() {
    return attachmentDao;
  }
}
