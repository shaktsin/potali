package com.potaliadmin.impl.service.newsfeed;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.constants.attachment.EnumImageFormat;
import com.potaliadmin.constants.post.EnumPostType;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.domain.job.Job;
import com.potaliadmin.domain.post.Post;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.internal.image.CreateAttachmentResponseDto;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedCreateRequest;
import com.potaliadmin.dto.web.request.newsfeed.NewsFeedEditRequest;
import com.potaliadmin.dto.web.response.attachment.AttachmentDto;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
import com.potaliadmin.dto.web.response.newsfeed.NewsFeedSearchResponse;
import com.potaliadmin.dto.web.response.newsfeed.PrepareNewsFeedResponse;
import com.potaliadmin.dto.web.response.post.CommentListResponse;
import com.potaliadmin.dto.web.response.post.CommentResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import com.potaliadmin.dto.web.response.post.ReplyDto;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.framework.elasticsearch.ESSearchFilter;
import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.pact.dao.attachment.AttachmentDao;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.dao.post.PostDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.service.post.NewsFeedService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.comment.CommentVO;
import com.potaliadmin.vo.job.JobVO;
import com.potaliadmin.vo.post.PostVO;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by shaktsin on 3/28/15.
 */
@Service
public class NewsFeedServiceImpl implements NewsFeedService {

  @Autowired
  UserService userService;
  @Autowired
  CircleDao circleDao;
  @Autowired
  BaseESService baseESService;
  @Autowired
  PostDao postDao;
  @Autowired
  PostBlobDao postBlobDao;
  @Autowired
  PostService postService;
  @Autowired
  UploadService uploadService;
  @Autowired
  AttachmentDao attachmentDao;




  @Override
  @SuppressWarnings("unchecked")
  public PrepareNewsFeedResponse prepareNewsFeed() {
    UserResponse userResponse = getUserService().getLoggedInUser();

    // get circle list from DB
    List<UserCircleMapping> userCircleMappingList = (List<UserCircleMapping>)getCircleDao().findByNamedQueryAndNamedParam("findByUser",
        new String[]{"userId"}, new Object[]{userResponse.getId()});

    if (userCircleMappingList == null || userCircleMappingList.isEmpty()) {
      PrepareNewsFeedResponse prepareNewsFeedResponse = new PrepareNewsFeedResponse();
      prepareNewsFeedResponse.setException(true);
      prepareNewsFeedResponse.addMessage("Some exception occurred, please try again!");
      return prepareNewsFeedResponse;
    }

    List<Long> authCircleList = new ArrayList<Long>();
    List<Long> circleList = userResponse.getCircleList();
    if (circleList == null || circleList.isEmpty()) {
      PrepareNewsFeedResponse prepareNewsFeedResponse = new PrepareNewsFeedResponse();
      prepareNewsFeedResponse.setException(true);
      prepareNewsFeedResponse.addMessage("Some exception occurred, please try again!");
      return prepareNewsFeedResponse;
    }


    for (UserCircleMapping userCircleMapping : userCircleMappingList) {
      if (userCircleMapping.isAuthorised() && circleList.contains(userCircleMapping.getUserCircleMappingKey().getCircleId())) {
        authCircleList.add(userCircleMapping.getUserCircleMappingKey().getCircleId());
      }
    }

    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (Long circle : authCircleList) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circle, null, CircleVO.class);
      if (circleVO != null) {
        if (!circleVO.isActive()) {
          continue;
        }
        CircleDto circleDto = new CircleDto();
        circleDto.setId(circleVO.getId());
        circleDto.setName(circleVO.getName());
        circleDto.setSelected(false);
        circleDtoList.add(circleDto);
      }
    }

    PrepareNewsFeedResponse prepareNewsFeedResponse = new PrepareNewsFeedResponse();
    prepareNewsFeedResponse.setCircleDtoList(circleDtoList);

    return prepareNewsFeedResponse;
  }

  @Override
  public GenericPostResponse createNewsFeed(NewsFeedCreateRequest newsFeedCreateRequest,
                                            List<FormDataBodyPart> imgFiles,
                                            List<FormDataBodyPart> jFiles) {
    if (!newsFeedCreateRequest.validate()) {
      throw new InValidInputException("Please input valid parameters");
    }

    UserResponse userResponse = getUserService().getLoggedInUser();
    newsFeedCreateRequest.setUserId(userResponse.getId());
    newsFeedCreateRequest.setUserInstituteId(userResponse.getInstituteId());

    // check whether user is authorized to comment
    boolean isAuthorized = false;
    List<Long> circleVOs = newsFeedCreateRequest.getCircleList();
    for (Long circleId : circleVOs) {
      if (userResponse.getCircleList() == null) {
        break;
      }
      if (userResponse.getCircleList().contains(circleId)) {
        isAuthorized = true;
        break;
      }
    }

    if (!isAuthorized) {
      GenericPostResponse genericPostResponse = new GenericPostResponse();
      genericPostResponse.setException(true);
      genericPostResponse.addMessage("You are not the part of this club");
      return genericPostResponse;
    }

    Post post = (Post) getPostDao().createPost(newsFeedCreateRequest);
    // set blob
    PostBlob postBlob = getPostBlobDao().findByPostId(post.getId());
    if (postBlob == null) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage("Some Internal Exception Occurred!");
      return jobResponse;
    }

    PostVO postVO = new PostVO(post, postBlob);
    postVO.setPostType(EnumPostType.NEWSFEED.getId());


    // now upload images, trim images and upload them to amazon
    List<CreateAttachmentResponseDto> imageResponseDtoList = null;
    if (imgFiles != null && !imgFiles.isEmpty()) {
      imageResponseDtoList = getPostService().postImages(imgFiles, post.getId());
      if (imageResponseDtoList == null || imageResponseDtoList.isEmpty()) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setException(Boolean.TRUE);
        jobResponse.addMessage("Some Internal Exception Occurred!");
        return jobResponse;
      }
    }

    // set images link
    if (imageResponseDtoList != null) {
      //List<String> imageLinks = new ArrayList<String>();
      List<AttachmentDto> attachmentDtoList = new ArrayList<AttachmentDto>();
      //Map<Long, String> imageMap = new HashMap<Long, String>();
      for (CreateAttachmentResponseDto createAttachmentResponseDto : imageResponseDtoList) {
        String imageLink = getUploadService()
            .getCanonicalPathOfCloudResource(createAttachmentResponseDto.getPublicId(), createAttachmentResponseDto.getVersion()
                , createAttachmentResponseDto.getFormat(), EnumAttachmentType.IMAGE);
        //mageLinks.add(imageLink);
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(createAttachmentResponseDto.getId());
        attachmentDto.setUrl(imageLink);
        attachmentDto.setName(createAttachmentResponseDto.getName());
        attachmentDtoList.add(attachmentDto);
        //imageMap.put(createImageResponseDto.getId(), imageLink);
      }
      //postVO.setImageList(imageLinks);
      postVO.setAttachmentDtoList(attachmentDtoList);
      //postVO.setImageMap(imageMap);
    }


    // post doc lists
    List<CreateAttachmentResponseDto> docResponseDtoList = null;
    if (jFiles != null && !jFiles.isEmpty()) {
      docResponseDtoList = getPostService().postRawFiles(jFiles, post.getId());
      if (docResponseDtoList == null || docResponseDtoList.isEmpty()) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setException(Boolean.TRUE);
        jobResponse.addMessage("Some Internal Exception Occurred!");
        return jobResponse;
      }
    }

    // set images link
    if (docResponseDtoList != null) {
      //List<String> imageLinks = new ArrayList<String>();
      List<AttachmentDto> attachmentDtoList = new ArrayList<AttachmentDto>();
      //Map<Long, String> imageMap = new HashMap<Long, String>();
      for (CreateAttachmentResponseDto createAttachmentResponseDto : docResponseDtoList) {
        String uploadLink = getUploadService()
            .getCanonicalPathOfCloudResource(createAttachmentResponseDto.getPublicId(), createAttachmentResponseDto.getVersion()
                , createAttachmentResponseDto.getFormat(), EnumAttachmentType.DOC);
        //imageLinks.add(imageLink);
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(createAttachmentResponseDto.getId());
        attachmentDto.setUrl(uploadLink);
        attachmentDto.setAttachmentType(EnumAttachmentType.DOC.getName());
        attachmentDto.setName(createAttachmentResponseDto.getName());
        attachmentDtoList.add(attachmentDto);

        //imageMap.put(createImageResponseDto.getId(), imageLink);
      }
      //postVO.setImageList(imageLinks);
      List<AttachmentDto> docList = postVO.getAttachmentDtoList();
      if (docList != null && !docList.isEmpty()) {
        attachmentDtoList.addAll(docList);
      }
      postVO.setAttachmentDtoList(attachmentDtoList);
      //postVO.setImageMap(imageMap);
    }

    // set circle
    List<CircleVO> circleVOList = new ArrayList<CircleVO>();
    for (Long circleId : newsFeedCreateRequest.getCircleList()) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circleId, null , CircleVO.class);
      circleVOList.add(circleVO);
    }
    postVO.setCircleList(circleVOList);

    boolean published = getBaseESService().put(postVO);
    if (published) {
      return createPostResponse(postVO, userResponse);
    } else {
      GenericPostResponse postResponse = new GenericPostResponse();
      postResponse.setException(true);
      postResponse.addMessage("Something unexpected occurred ! Try Again");
      return postResponse;
    }

  }

  @Override
  public GenericPostResponse getNewsFeed(Long postId) {
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    if (postId == null) {
      throw new InValidInputException("Post Id cannot be null");
    }

    PostVO postVO = (PostVO)getBaseESService().get(postId, null,PostVO.class);
    if (postVO != null) {
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = createPostResponse(postVO, postUser);

      boolean isImp = getPostService().isPostImportantForUser(postVO.getPostId(), userResponse.getId());
      boolean isLiked = getPostService().isPostLikedForUser(postVO.getPostId(), userResponse.getId());

      genericPostResponse.setImportant(isImp);
      genericPostResponse.setLiked(isLiked);

      boolean hasComments = getPostService().postHasComments(postId);

      if (hasComments) {

        TermFilterBuilder termFilterBuilder = FilterBuilders.termFilter("parentId", postVO.getPostId());

        ESSearchFilter esSearchFilter =
            new ESSearchFilter().setFilterBuilder(termFilterBuilder).addSortedMap("id", SortOrder.DESC)
                .setPageNo(DefaultConstants.AND_APP_PAGE_NO).setPerPage(DefaultConstants.AND_APP_PER_PAGE);

        ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, CommentVO.class);
        if (esSearchResponse.getTotalResults() > 0) {
          CommentListResponse commentListResponse = new CommentListResponse();
          List<CommentResponse> commentResponses = new ArrayList<CommentResponse>();
          for (BaseElasticVO baseElasticVO : esSearchResponse.getBaseElasticVOs()) {
            CommentVO commentVO = (CommentVO) baseElasticVO;
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(commentVO.getId());
            commentResponse.setContent(commentVO.getComment());
            UserResponse commentUser = getUserService().findById(commentVO.getUserId());
            UserDto userDto = new UserDto(commentUser);
            commentResponse.setUserDto(userDto);
            commentResponse.setPostId(Long.parseLong(commentVO.getParentId()));
            commentResponse.setCommentedOn(DateUtils.getPostedOnDate(commentVO.getCommentedOn()));
            commentResponses.add(commentResponse);
          }
          commentListResponse.setReorderCommentResponse(commentResponses);
          commentListResponse.setPageNo(DefaultConstants.AND_APP_PAGE_NO);
          commentListResponse.setPerPage(DefaultConstants.AND_APP_PER_PAGE);
          commentListResponse.setTotalResults(esSearchResponse.getTotalResults());
          genericPostResponse.setCommentListResponse(commentListResponse);
        }

      }
      return genericPostResponse;

    } else {
      GenericPostResponse genericPostResponse = new GenericPostResponse();
      genericPostResponse.setException(true);
      genericPostResponse.addMessage("Something unexpected occurred ! Try Again");
      return genericPostResponse;
    }

  }

  @Override
  public GenericPostResponse editNewsFeed(NewsFeedEditRequest newsFeedEditRequest,
                                          List<FormDataBodyPart> imgFiles,
                                          List<FormDataBodyPart> jFiles) {

    if (!newsFeedEditRequest.validate()) {
      throw new InValidInputException("Please input valid request");
    }

    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access");
    }

    newsFeedEditRequest.setUserId(userResponse.getId());
    newsFeedEditRequest.setUserInstituteId(userResponse.getInstituteId());

    Post post = getPostDao().editPost(newsFeedEditRequest);

    // set blob
    PostBlob postBlob = getPostBlobDao().findByPostId(post.getId());
    if (postBlob == null) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage("Some Internal Exception Occurred!");
      return jobResponse;
    }


    if (newsFeedEditRequest.getDeletedAttachment() != null) {
      for (Long id : newsFeedEditRequest.getDeletedAttachment()) {
        Attachment attachment = getAttachmentDao().get(Attachment.class, id);
        if (attachment == null) {
          throw new InValidInputException("No attachment found");
        }
        getAttachmentDao().delete(attachment);

        boolean isDeleted = getUploadService().deleteImage(attachment.getPublicId());
        if (!isDeleted) {
          throw new PotaliRuntimeException("Could not delete attachment from cloud");
        }
      }
    }

    // now upload images, trim images and upload them to amazon
    List<CreateAttachmentResponseDto> imageResponseDtoList = null;
    if (imgFiles != null && !imgFiles.isEmpty()) {
      imageResponseDtoList = getPostService().postImages(imgFiles, post.getId());
      if (imageResponseDtoList == null || imageResponseDtoList.isEmpty()) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setException(Boolean.TRUE);
        jobResponse.addMessage("Some Internal Exception Occurred!");
        return jobResponse;
      }
    }

    PostVO postVO = new PostVO(post, postBlob);
    postVO.setPostType(EnumPostType.NEWSFEED.getId());

    List<Attachment> attachmentList = getAttachmentDao().findByPostId(post.getId());
    if (attachmentList != null) {
      //Map<Long, String> imageMap = new HashMap<Long, String>();
      List<AttachmentDto> attachmentDtoList = new ArrayList<AttachmentDto>();
      for (Attachment attachment : attachmentList) {
        String imageLink = getUploadService()
            .getCanonicalPathOfCloudResource(attachment.getPublicId(), attachment.getVersion()
                , EnumImageFormat.getImageFormatById(attachment.getFormat()), EnumAttachmentType.IMAGE);
        //imageMap.put(attachment.getId(), imageLink);
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(attachment.getId());
        attachmentDto.setUrl(imageLink);
        attachmentDto.setName(attachment.getName());
        attachmentDtoList.add(attachmentDto);
      }
      //postVO.setImageMap(imageMap);
      postVO.setAttachmentDtoList(attachmentDtoList);
    }

    // post doc lists
    List<CreateAttachmentResponseDto> docResponseDtoList = null;
    if (jFiles != null && !jFiles.isEmpty()) {
      docResponseDtoList = getPostService().postRawFiles(jFiles, post.getId());
      if (docResponseDtoList == null || docResponseDtoList.isEmpty()) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setException(Boolean.TRUE);
        jobResponse.addMessage("Some Internal Exception Occurred!");
        return jobResponse;
      }
    }

    // set images link
    if (docResponseDtoList != null) {
      //List<String> imageLinks = new ArrayList<String>();
      List<AttachmentDto> attachmentDtoList = new ArrayList<AttachmentDto>();
      //Map<Long, String> imageMap = new HashMap<Long, String>();
      for (CreateAttachmentResponseDto createAttachmentResponseDto : docResponseDtoList) {
        String uploadLink = getUploadService()
            .getCanonicalPathOfCloudResource(createAttachmentResponseDto.getPublicId(), createAttachmentResponseDto.getVersion()
                , createAttachmentResponseDto.getFormat(), EnumAttachmentType.DOC);
        //imageLinks.add(imageLink);
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(createAttachmentResponseDto.getId());
        attachmentDto.setUrl(uploadLink);
        attachmentDto.setAttachmentType(EnumAttachmentType.DOC.getName());
        attachmentDto.setName(createAttachmentResponseDto.getName());
        attachmentDtoList.add(attachmentDto);

        //imageMap.put(createImageResponseDto.getId(), imageLink);
      }
      //postVO.setImageList(imageLinks);
      postVO.setAttachmentDtoList(attachmentDtoList);
      //postVO.setImageMap(imageMap);
    }

    List<CircleVO> circleVOList = new ArrayList<CircleVO>();
    for (Long circleId : newsFeedEditRequest.getCircleList()) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circleId, null , CircleVO.class);
      circleVOList.add(circleVO);
    }
    postVO.setCircleList(circleVOList);

    boolean published = getBaseESService().put(postVO);
    if (published) {
      return createPostResponse(postVO, userResponse);
    } else {
      GenericPostResponse genericPostResponse = new GenericPostResponse();
      genericPostResponse.setException(true);
      genericPostResponse.addMessage("Something unexpected occurred ! Try Again");
      return genericPostResponse;
    }

  }

  @Override
  public NewsFeedSearchResponse searchNewsFeed(Long[] circleList, EnumSearchOperation searchOperation,
                                               Date postDate,Long postId, int perPage, int pageNo) {

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    NewsFeedSearchResponse newsFeedSearchResponse= new NewsFeedSearchResponse();
    AndFilterBuilder andFilterBuilder = new AndFilterBuilder();
    andFilterBuilder.add(FilterBuilders.termFilter("postType", EnumPostType.NEWSFEED.getId()));

    andFilterBuilder.add(FilterBuilders.termFilter("userInstituteId", userResponse.getInstituteId()));
    // load more
    if (searchOperation != null && postId != null) {
      if (EnumSearchOperation.NEWER.getId() == searchOperation.getId()) {
        andFilterBuilder.add(FilterBuilders.rangeFilter("postId").gt(postId).lte(postId+perPage));
      } else {
        andFilterBuilder.add(FilterBuilders.rangeFilter("postId").lt(postId).gte(postId-perPage));
      }
    }

    if (postDate != null) {
      andFilterBuilder.add(FilterBuilders.rangeFilter("createdDate").
          lt(DateUtils.formatDate(postDate, DateUtils.DEFAULT_ES_DATE_FORMAT)));
    }

    if (circleList != null && circleList.length > 0) {
      List<Long> arrayList = Arrays.asList(circleList);
      arrayList.add(DefaultConstants.DEFAULT_FILTER);
      List<Long> finalArray = new ArrayList<Long>();
      finalArray.addAll(arrayList);
      andFilterBuilder.add(FilterBuilders.inFilter("circleList.id", finalArray.toArray()));
    } else {
      if (userResponse.getCircleList() != null && userResponse.getCircleList().size() > 0) {
        //Long[] circleArrayList = (Long[])userResponse.getCircleList().toArray();
        //if (circleList != null && circleList.)
        andFilterBuilder.add(FilterBuilders.inFilter("circleList.id", userResponse.getCircleList().toArray()));
      }
    }

    ESSearchFilter esSearchFilter = new ESSearchFilter().setFilterBuilder(andFilterBuilder)
        .addSortedMap("postId", SortOrder.DESC).setPageNo(pageNo).setPerPage(perPage);

    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, PostVO.class);

    List<BaseElasticVO> postVOList = esSearchResponse.getBaseElasticVOs();

    List<GenericPostResponse> genericPostResponseList = new ArrayList<GenericPostResponse>();
    for (BaseElasticVO baseElasticVO : postVOList) {
      PostVO postVO = (PostVO) baseElasticVO;

      boolean isHiddenOrSpammed = getPostService().isPostMarkHiddenOrSpammed(postVO.getPostId(), userResponse.getId());
      if (isHiddenOrSpammed) {
        continue;
      }

      UserResponse postUser = getUserService().findById(postVO.getUserId());
      GenericPostResponse genericPostResponse = new GenericPostResponse(postVO, postUser);
      boolean isImp = getPostService().isPostImportantForUser(postVO.getPostId(), userResponse.getId());
      boolean isLiked = getPostService().isPostLikedForUser(postVO.getPostId(), userResponse.getId());
      genericPostResponse.setImportant(isImp);
      genericPostResponse.setLiked(isLiked);
      genericPostResponseList.add(genericPostResponse);
    }
    newsFeedSearchResponse.setNewsfeedList(genericPostResponseList);
    newsFeedSearchResponse.setTotalResults(esSearchResponse.getTotalResults());
    newsFeedSearchResponse.setPerPage(perPage);
    newsFeedSearchResponse.setPageNo(pageNo);

    return newsFeedSearchResponse;
  }


  private GenericPostResponse createPostResponse(PostVO postVO, UserResponse userResponse) {
    GenericPostResponse postResponse = new GenericPostResponse();
    postResponse.setPostId(postVO.getPostId());
    postResponse.setSubject(postVO.getSubject());
    postResponse.setContent(postVO.getContent());
    postResponse.setPostType(postVO.getPostType());
    postResponse.setPostedOn(DateUtils.getPostedOnDate(postVO.getCreatedDate()));


    ReplyDto replyDto = new ReplyDto(-1, -1, -1);
    if (StringUtils.isNotBlank(postVO.getReplyEmail())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_EMAIL.getId());
    }
    if (StringUtils.isNotBlank(postVO.getReplyPhone())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_PHONE.getId());
    }
    if (StringUtils.isNotBlank(postVO.getReplyWatsApp())) {
      replyDto.setReplyEmail(EnumReactions.REPLY_VIA_WATSAPP.getId());
    }
    postResponse.setReplyDto(replyDto);

    postResponse.setShareDto(postVO.getShareDto());

    UserDto userDto = new UserDto();
    userDto.setId(userResponse.getId());
    userDto.setName(userResponse.getName());
    userDto.setImage(userResponse.getImage());
    userDto.setGcmId(userResponse.getGcmId());
    postResponse.setUserDto(userDto);

    postResponse.setImages(postVO.getImageList());
    postResponse.setAttachmentDtoList(postVO.getAttachmentDtoList());
    //jobResponse.setImageMap(postVO.getImageMap());

    List<CircleVO> circleVOs = postVO.getCircleList();

    List<CircleDto> circleDtoList = new ArrayList<CircleDto>();
    for (CircleVO circleVO : circleVOs) {
      CircleDto circleDto = new CircleDto();
      circleDto.setId(circleVO.getId());
      circleDto.setName(circleVO.getName());
      circleDto.setSelected(false);
      circleDtoList.add(circleDto);
    }

    postResponse.setCircleDtoList(circleDtoList);

    postResponse.setNumComment(postVO.getNumComment());
    postResponse.setNumImportant(postVO.getNumImportant());
    postResponse.setNumHides(postVO.getNumHides());
    postResponse.setNumSpam(postVO.getNumSpam());
    postResponse.setNumShared(postVO.getNumShared());
    postResponse.setNumReplies(postVO.getNumReplies());
    postResponse.setNumLikes(postVO.getNumLikes());

    return postResponse;
  }


  public UserService getUserService() {
    return userService;
  }

  public CircleDao getCircleDao() {
    return circleDao;
  }

  public BaseESService getBaseESService() {
    return baseESService;
  }

  public PostDao getPostDao() {
    return postDao;
  }

  public PostBlobDao getPostBlobDao() {
    return postBlobDao;
  }

  public PostService getPostService() {
    return postService;
  }

  public UploadService getUploadService() {
    return uploadService;
  }

  public AttachmentDao getAttachmentDao() {
    return attachmentDao;
  }
}
