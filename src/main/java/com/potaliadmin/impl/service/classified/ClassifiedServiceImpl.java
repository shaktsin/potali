package com.potaliadmin.impl.service.classified;

import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.constants.post.EnumPostType;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.classified.ClassifiedPost;
import com.potaliadmin.domain.post.Post;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryVO;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryDto;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryVO;
import com.potaliadmin.dto.internal.image.CreateAttachmentResponseDto;
import com.potaliadmin.dto.web.request.classified.ClassifiedPostRequest;
import com.potaliadmin.dto.web.response.attachment.AttachmentDto;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.classified.ClassifiedPostResponse;
import com.potaliadmin.dto.web.response.classified.PrepareClassifiedResponse;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.newsfeed.PrepareNewsFeedResponse;
import com.potaliadmin.dto.web.response.post.GenericPostResponse;
import com.potaliadmin.dto.web.response.post.ReplyDto;
import com.potaliadmin.dto.web.response.user.UserDto;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.framework.cache.classified.PrimaryCategoryCache;
import com.potaliadmin.framework.cache.classified.SecondaryCategoryCache;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.dao.classified.ClassifiedDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.service.classified.ClassifiedService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.classified.ClassifiedVO;
import com.potaliadmin.vo.post.PostVO;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaktsin on 4/5/15.
 */
@Service
public class ClassifiedServiceImpl implements ClassifiedService {

  @Autowired
  UserService userService;
  @Autowired
  CircleDao circleDao;
  @Autowired
  BaseESService baseESService;
  @Autowired
  ClassifiedDao classifiedDao;
  @Autowired
  PostBlobDao postBlobDao;
  @Autowired
  UploadService uploadService;
  @Autowired
  PostService postService;

  @Override
  @SuppressWarnings("unchecked")
  public PrepareClassifiedResponse prepareClassifiedResponse() {
    UserResponse userResponse = getUserService().getLoggedInUser();

    // get circle list from DB
    List<UserCircleMapping> userCircleMappingList = (List<UserCircleMapping>)getCircleDao().findByNamedQueryAndNamedParam("findByUser",
        new String[]{"userId"}, new Object[]{userResponse.getId()});

    if (userCircleMappingList == null || userCircleMappingList.isEmpty()) {
      PrepareClassifiedResponse prepareClassifiedResponse = new PrepareClassifiedResponse();
      prepareClassifiedResponse.setException(true);
      prepareClassifiedResponse.addMessage("Some exception occurred, please try again!");
      return prepareClassifiedResponse;
    }

    List<Long> authCircleList = new ArrayList<Long>();
    List<Long> circleList = userResponse.getCircleList();
    if (circleList == null || circleList.isEmpty()) {
      PrepareClassifiedResponse prepareClassifiedResponse = new PrepareClassifiedResponse();
      prepareClassifiedResponse.setException(true);
      prepareClassifiedResponse.addMessage("Some exception occurred, please try again!");
      return prepareClassifiedResponse;
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

    List<PrimaryCategoryDto> primaryCategoryDtoList = new ArrayList<PrimaryCategoryDto>();
    List<PrimaryCategoryVO> primaryCategoryVOs = PrimaryCategoryCache.getCache().getAllPrimaryCategoryVO();
    for (PrimaryCategoryVO primaryCategoryVO : primaryCategoryVOs) {
      PrimaryCategoryDto primaryCategoryDto = new PrimaryCategoryDto();
      primaryCategoryDto.setId(primaryCategoryVO.getId());
      primaryCategoryDto.setName(primaryCategoryDto.getName());
      List<Long> secondaryIdList = PrimaryCategoryCache.getCache().getSecondaryCategoriesFromPC(primaryCategoryVO.getId());
      for (Long secondId : secondaryIdList) {
        SecondaryCategoryVO secondaryCategoryVO = SecondaryCategoryCache.getCache().getSecondaryCategoryVO(secondId);
        SecondaryCategoryDto secondaryCategoryDto = new SecondaryCategoryDto();
        secondaryCategoryDto.setId(secondaryCategoryVO.getId());
        secondaryCategoryDto.setName(secondaryCategoryVO.getName());
        secondaryCategoryDto.setSelected(false);
        primaryCategoryDto.addSecondaryCategoryDto(secondaryCategoryDto);
      }
      primaryCategoryDtoList.add(primaryCategoryDto);
    }



    PrepareClassifiedResponse prepareClassifiedResponse = new PrepareClassifiedResponse();
    prepareClassifiedResponse.setCircleDtoList(circleDtoList);
    prepareClassifiedResponse.setPrimaryCategoryDtoList(primaryCategoryDtoList);

    return prepareClassifiedResponse;
  }

  @Override
  public GenericPostResponse createClassifiedPost(ClassifiedPostRequest classifiedPostRequest,
                                                  List<FormDataBodyPart> imgFiles,
                                                  List<FormDataBodyPart> jFiles) {

    if (!classifiedPostRequest.validate()) {
      throw new InValidInputException("Please input valid parameters");
    }

    UserResponse userResponse = getUserService().getLoggedInUser();
    classifiedPostRequest.setUserId(userResponse.getId());
    classifiedPostRequest.setUserInstituteId(userResponse.getInstituteId());

    // check whether user is authorized to comment
    boolean isAuthorized = false;
    List<Long> circleVOs = classifiedPostRequest.getCircleList();
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

    ClassifiedPost post = (ClassifiedPost) getClassifiedDao().createClassified(classifiedPostRequest);
    // set blob
    PostBlob postBlob = getPostBlobDao().findByPostId(post.getId());
    if (postBlob == null) {
      JobResponse jobResponse = new JobResponse();
      jobResponse.setException(Boolean.TRUE);
      jobResponse.addMessage("Some Internal Exception Occurred!");
      return jobResponse;
    }

    PostVO postVO = new PostVO(post, postBlob);
    postVO.setPostType(EnumPostType.CLASSIFIED.getId());



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


    List<CircleVO> circleVOList = new ArrayList<CircleVO>();
    for (Long circleId : classifiedPostRequest.getCircleList()) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circleId, null , CircleVO.class);
      circleVOList.add(circleVO);
    }
    postVO.setCircleList(circleVOList);

    boolean published = getBaseESService().put(postVO);
    if (published) {
      ClassifiedVO classifiedVO = new ClassifiedVO(post);

      return createPostResponse(postVO, userResponse);
    } else {
      GenericPostResponse postResponse = new GenericPostResponse();
      postResponse.setException(true);
      postResponse.addMessage("Something unexpected occurred ! Try Again");
      return postResponse;
    }




    return null;
  }

  private ClassifiedPostResponse createPostResponse(PostVO postVO, UserResponse userResponse) {
    ClassifiedPostResponse postResponse = new ClassifiedPostResponse();
    postResponse.setPostId(postVO.getPostId());
    postResponse.setSubject(postVO.getSubject());
    postResponse.setContent(postVO.getContent());
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
    postResponse.setUserDto(userDto);

    postResponse.setImages(postVO.getImageList());
    postResponse.setAttachmentDtoList(postVO.getAttachmentDtoList());

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

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public CircleDao getCircleDao() {
    return circleDao;
  }

  public void setCircleDao(CircleDao circleDao) {
    this.circleDao = circleDao;
  }

  public BaseESService getBaseESService() {
    return baseESService;
  }

  public void setBaseESService(BaseESService baseESService) {
    this.baseESService = baseESService;
  }

  public ClassifiedDao getClassifiedDao() {
    return classifiedDao;
  }

  public void setClassifiedDao(ClassifiedDao classifiedDao) {
    this.classifiedDao = classifiedDao;
  }

  public PostBlobDao getPostBlobDao() {
    return postBlobDao;
  }

  public UploadService getUploadService() {
    return uploadService;
  }

  public PostService getPostService() {
    return postService;
  }
}
