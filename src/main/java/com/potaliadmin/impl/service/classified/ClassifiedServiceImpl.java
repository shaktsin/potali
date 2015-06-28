package com.potaliadmin.impl.service.classified;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumAttachmentType;
import com.potaliadmin.constants.attachment.EnumImageFormat;
import com.potaliadmin.constants.post.EnumPostType;
import com.potaliadmin.constants.query.EnumSearchOperation;
import com.potaliadmin.constants.reactions.EnumReactions;
import com.potaliadmin.domain.attachment.Attachment;
import com.potaliadmin.domain.classified.ClassifiedPost;
import com.potaliadmin.domain.post.Post;
import com.potaliadmin.domain.post.PostBlob;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.internal.cache.address.CityVO;
import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryDto;
import com.potaliadmin.dto.internal.cache.classified.PrimaryCategoryVO;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryDto;
import com.potaliadmin.dto.internal.cache.classified.SecondaryCategoryVO;
import com.potaliadmin.dto.internal.cache.es.job.CityDto;
import com.potaliadmin.dto.internal.image.CreateAttachmentResponseDto;
import com.potaliadmin.dto.web.request.classified.ClassifiedEditRequest;
import com.potaliadmin.dto.web.request.classified.ClassifiedPostRequest;
import com.potaliadmin.dto.web.response.attachment.AttachmentDto;
import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.classified.ClassifiedPostResponse;
import com.potaliadmin.dto.web.response.classified.ClassifiedSearchResponse;
import com.potaliadmin.dto.web.response.classified.PrepareClassifiedResponse;
import com.potaliadmin.dto.web.response.job.JobResponse;
import com.potaliadmin.dto.web.response.job.JobSearchResponse;
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
import com.potaliadmin.framework.cache.address.CityCache;
import com.potaliadmin.framework.cache.classified.PrimaryCategoryCache;
import com.potaliadmin.framework.cache.classified.SecondaryCategoryCache;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.framework.elasticsearch.ESSearchFilter;
import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.pact.dao.attachment.AttachmentDao;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.dao.classified.ClassifiedDao;
import com.potaliadmin.pact.dao.post.PostBlobDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.service.classified.ClassifiedService;
import com.potaliadmin.pact.service.post.PostService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.util.DateUtils;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.classified.ClassifiedVO;
import com.potaliadmin.vo.comment.CommentVO;
import com.potaliadmin.vo.job.JobVO;
import com.potaliadmin.vo.post.PostVO;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortOrder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by shaktsin on 4/5/15.
 */
@Service
public class ClassifiedServiceImpl implements ClassifiedService {

  private Logger logger = LoggerFactory.getLogger(ClassifiedServiceImpl.class);


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
  @Autowired
  AttachmentDao attachmentDao;

  private static final String CLASSIFIED = "classified";

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
      primaryCategoryDto.setName(primaryCategoryVO.getName());
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

    List<CityDto> cityDtoList = new ArrayList<CityDto>();
    List<CityVO> cityVOList = CityCache.getCache().getCityVO();
    for (CityVO cityVO : cityVOList) {
      CityDto cityDto = new CityDto();
      cityDto.setId(cityVO.getId());
      cityDto.setName(cityVO.getName());
      cityDtoList.add(cityDto);
    }



    PrepareClassifiedResponse prepareClassifiedResponse = new PrepareClassifiedResponse();
    prepareClassifiedResponse.setCircleDtoList(circleDtoList);
    prepareClassifiedResponse.setPrimaryCategoryDtoList(primaryCategoryDtoList);
    prepareClassifiedResponse.setCityDtoList(cityDtoList);

    return prepareClassifiedResponse;
  }

  @Override
  public ClassifiedPostResponse createClassifiedPost(ClassifiedPostRequest classifiedPostRequest,
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
      ClassifiedPostResponse genericPostResponse = new ClassifiedPostResponse();
      genericPostResponse.setException(true);
      genericPostResponse.addMessage("You are not the part of this club");
      return genericPostResponse;
    }

    ClassifiedPost post = (ClassifiedPost) getClassifiedDao().createClassified(classifiedPostRequest);
    // set blob
    PostBlob postBlob = getPostBlobDao().findByPostId(post.getId());
    if (postBlob == null) {
      ClassifiedPostResponse genericPostResponse = new ClassifiedPostResponse();
      genericPostResponse.setException(Boolean.TRUE);
      genericPostResponse.addMessage("Some Internal Exception Occurred!");
      return genericPostResponse;
    }

    PostVO postVO = new PostVO(post, postBlob);
    postVO.setPostType(EnumPostType.CLASSIFIED.getId());



    // now upload images, trim images and upload them to amazon
    List<CreateAttachmentResponseDto> imageResponseDtoList = null;
    if (imgFiles != null && !imgFiles.isEmpty()) {
      imageResponseDtoList = getPostService().postImages(imgFiles, post.getId());
      if (imageResponseDtoList == null || imageResponseDtoList.isEmpty()) {
        ClassifiedPostResponse genericPostResponse = new ClassifiedPostResponse();
        genericPostResponse.setException(Boolean.TRUE);
        genericPostResponse.addMessage("Some Internal Exception Occurred!");
        return genericPostResponse;
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
        ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
        classifiedPostResponse.setException(Boolean.TRUE);
        classifiedPostResponse.addMessage("Some Internal Exception Occurred!");
        return classifiedPostResponse;
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


    List<CircleVO> circleVOList = new ArrayList<CircleVO>();
    for (Long circleId : classifiedPostRequest.getCircleList()) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circleId, null , CircleVO.class);
      circleVOList.add(circleVO);
    }
    postVO.setCircleList(circleVOList);

    boolean published = getBaseESService().put(postVO);
    if (published) {
      ClassifiedVO classifiedVO = new ClassifiedVO(post);
      boolean classifiedPublished = getBaseESService().put(classifiedVO);
      if (!classifiedPublished) {
        logger.error("Could not publish post classified" + " " + postVO.getId());
        getBaseESService().delete(postVO.getId(), PostVO.class);
        ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
        classifiedPostResponse.setException(true);
        classifiedPostResponse.addMessage("Something unexpected occurred ! Try Again");
        return classifiedPostResponse;
      } else {
        return createPostResponse(postVO, classifiedVO,userResponse);
      }

    } else {
      ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
      classifiedPostResponse.setException(true);
      classifiedPostResponse.addMessage("Something unexpected occurred ! Try Again");
      return classifiedPostResponse;
    }

  }

  @Override
  public ClassifiedPostResponse editClassifiedPost(ClassifiedEditRequest classifiedPostRequest,
                                                   List<FormDataBodyPart> imgFiles,
                                                   List<FormDataBodyPart> jFiles) {

    if (!classifiedPostRequest.validate()) {
      throw new InValidInputException("Please input valid request");
    }

    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Access");
    }

    classifiedPostRequest.setUserId(userResponse.getId());
    classifiedPostRequest.setUserInstituteId(userResponse.getInstituteId());

    ClassifiedPost classifiedPost = getClassifiedDao().editClassified(classifiedPostRequest);

    // set blob
    PostBlob postBlob = getPostBlobDao().findByPostId(classifiedPost.getId());
    if (postBlob == null) {
      ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
      classifiedPostResponse.setException(Boolean.TRUE);
      classifiedPostResponse.addMessage("Some Internal Exception Occurred!");
      return classifiedPostResponse;
    }


    if (classifiedPostRequest.getDeletedAttachment() != null) {
      for (Long id : classifiedPostRequest.getDeletedAttachment()) {
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
      imageResponseDtoList = getPostService().postImages(imgFiles, classifiedPost.getId());
      if (imageResponseDtoList == null || imageResponseDtoList.isEmpty()) {
        ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
        classifiedPostResponse.setException(Boolean.TRUE);
        classifiedPostResponse.addMessage("Some Internal Exception Occurred!");
        return classifiedPostResponse;
      }
    }

    PostVO postVO = new PostVO(classifiedPost, postBlob);
    postVO.setPostType(EnumPostType.CLASSIFIED.getId());

    List<Attachment> attachmentList = getAttachmentDao().findByPostId(classifiedPost.getId());
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
      docResponseDtoList = getPostService().postRawFiles(jFiles, classifiedPost.getId());
      if (docResponseDtoList == null || docResponseDtoList.isEmpty()) {
        ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
        classifiedPostResponse.setException(Boolean.TRUE);
        classifiedPostResponse.addMessage("Some Internal Exception Occurred!");
        return classifiedPostResponse;
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
    for (Long circleId : classifiedPostRequest.getCircleList()) {
      CircleVO circleVO = (CircleVO) getBaseESService().get(circleId, null , CircleVO.class);
      circleVOList.add(circleVO);
    }
    postVO.setCircleList(circleVOList);


    boolean published = getBaseESService().put(postVO);
    if (published) {
      ClassifiedVO classifiedVO = new ClassifiedVO(classifiedPost);
      boolean isPostPublished = getBaseESService().put(classifiedVO);
      if (!isPostPublished) {
        getBaseESService().delete(postVO.getId(), PostVO.class);
        ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
        classifiedPostResponse.setException(true);
        classifiedPostResponse.addMessage("Something unexpected occurred ! Try Again");
        return classifiedPostResponse;
      }
      return createPostResponse(postVO, classifiedVO, userResponse);
    } else {
      ClassifiedPostResponse classifiedPostResponse = new ClassifiedPostResponse();
      classifiedPostResponse.setException(true);
      classifiedPostResponse.addMessage("Something unexpected occurred ! Try Again");
      return classifiedPostResponse;
    }
  }

  @Override
  public ClassifiedSearchResponse searchClassified(Long[] circleList,Long[] locationList, Long[] primaryCatList,
                                            Long[] secondaryCatList,EnumSearchOperation searchOperation,
                                            Date postDate,Long postId, int perPage, int pageNo) {

    UserResponse userResponse = getUserService().getLoggedInUser();

    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    ClassifiedSearchResponse classifiedSearchResponse= new ClassifiedSearchResponse();
    AndFilterBuilder andFilterBuilder = new AndFilterBuilder();

    andFilterBuilder.add(FilterBuilders.termFilter("postType", EnumPostType.CLASSIFIED.getId()));

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

    if (locationList != null && locationList.length > 0) {
      //locationList = Arrays.asList(locationList);
      List<Long> arrayList = Arrays.asList(locationList);
      arrayList.add(DefaultConstants.DEFAULT_FILTER);
      List<Long> finalArray = new ArrayList<Long>();
      finalArray.addAll(arrayList);
      TermsFilterBuilder locationFilter = FilterBuilders.inFilter("locationList.id", finalArray.toArray());
      HasChildFilterBuilder hasLocationChild = FilterBuilders.hasChildFilter(CLASSIFIED, locationFilter);
      andFilterBuilder.add(hasLocationChild);
      //Arrays.asList(locationList);
    }
    if (primaryCatList != null && primaryCatList.length > 0) {
      List<Long> arrayList = Arrays.asList(primaryCatList);
      arrayList.add(DefaultConstants.DEFAULT_FILTER);
      List<Long> finalArray = new ArrayList<Long>();
      finalArray.addAll(arrayList);
      TermsFilterBuilder primaryCatFilter = FilterBuilders.inFilter("primaryCategoryDtoList.id", finalArray.toArray());
      HasChildFilterBuilder hasRolesChild = FilterBuilders.hasChildFilter(CLASSIFIED, primaryCatFilter);
      andFilterBuilder.add(hasRolesChild);
      //andFilterBuilder.add(FilterBuilders.inFilter("industryRolesList.id", rolesList));
    }
    if (secondaryCatList != null && secondaryCatList.length > 0) {
      List<Long> arrayList = Arrays.asList(secondaryCatList);
      if (primaryCatList != null) {
        for (long primaryCatId :  primaryCatList) {
          arrayList.add(PrimaryCategoryCache.getCache().getOtherFromParent(primaryCatId));
        }
      }
      List<Long> finalArray = new ArrayList<Long>();
      finalArray.addAll(arrayList);
      TermsFilterBuilder secCatFilter = FilterBuilders.inFilter("secondaryCategoryDtoList.id", finalArray.toArray());
      HasChildFilterBuilder hasIndustryChild = FilterBuilders.hasChildFilter(CLASSIFIED, secCatFilter);
      andFilterBuilder.add(hasIndustryChild);
      //andFilterBuilder.add(FilterBuilders.inFilter("industryRolesList.industryId", industryList));
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
      if (postVO == null) {
        continue;
      }
      ClassifiedVO classifiedVO = (ClassifiedVO) getBaseESService().get(postVO.getPostId(), postVO.getPostId(), ClassifiedVO.class);

      if (classifiedVO == null) {
        continue;
      }

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
      genericPostResponse.setSecondaryCategoryDtoList(classifiedVO.getSecondaryCategoryDtoList());
      genericPostResponse.setLocations(classifiedVO.getLocationList());
      genericPostResponseList.add(genericPostResponse);
    }

    classifiedSearchResponse.setPosts(genericPostResponseList);
    classifiedSearchResponse.setTotalResults(esSearchResponse.getTotalResults());
    classifiedSearchResponse.setPerPage(perPage);
    classifiedSearchResponse.setPageNo(pageNo);

    // set all cities
    List<CityDto> cityDtoList = new ArrayList<CityDto>();
    List<CityVO> cityVOList = CityCache.getCache().getCityVO();

    for (CityVO cityVO : cityVOList) {
      CityDto cityDto = new CityDto();
      cityDto.setId(cityVO.getId());
      cityDto.setName(cityVO.getName());
      if (locationList != null && locationList.length > 0) {
        boolean isSelected = Arrays.asList(locationList).contains(cityVO.getId());
        cityDto.setSelected(isSelected);
      }
      cityDtoList.add(cityDto);
    }
    classifiedSearchResponse.setCityDtoList(cityDtoList);

    List<PrimaryCategoryDto> primaryCategoryDtoList = new ArrayList<PrimaryCategoryDto>();
    List<PrimaryCategoryVO> primaryCategoryVOs = PrimaryCategoryCache.getCache().getAllPrimaryCategoryVO();
    for (PrimaryCategoryVO primaryCategoryVO : primaryCategoryVOs) {
      PrimaryCategoryDto primaryCategoryDto = new PrimaryCategoryDto();
      primaryCategoryDto.setId(primaryCategoryVO.getId());
      primaryCategoryDto.setName(primaryCategoryDto.getName());
      if (primaryCatList != null && primaryCatList.length > 0) {
        boolean isSelected = Arrays.asList(primaryCatList).contains(primaryCategoryVO.getId());
        primaryCategoryDto.setSelected(isSelected);
      }
      primaryCategoryDtoList.add(primaryCategoryDto);
    }
    classifiedSearchResponse.setPrimaryCategoryDtoList(primaryCategoryDtoList);

    List<SecondaryCategoryDto> secondaryCategoryDtoList = new ArrayList<SecondaryCategoryDto>();
    /*List<SecondaryCategoryVO> secondaryCategoryVOs = SecondaryCategoryCache.getCache().getAllSecondaryCategoryVO();
    for (SecondaryCategoryVO secondaryCategoryVO : secondaryCategoryVOs) {
      SecondaryCategoryDto secondaryCategoryDto = new SecondaryCategoryDto();
      secondaryCategoryDto.setId(secondaryCategoryVO.getId());
      secondaryCategoryDto.setName(secondaryCategoryVO.getName());
      secondaryCategoryDto.setPrimaryCatId(secondaryCategoryVO.getPrimaryCategoryId());

      // get primary name
      PrimaryCategoryVO primaryCategoryVO = PrimaryCategoryCache.getCache().getPrimaryCategoryVO(secondaryCategoryVO.getId());
      secondaryCategoryDto.setPrimaryCatName(primaryCategoryVO.getName());

      if (secondaryCatList != null && secondaryCatList.length > 0) {
        boolean isSelected = Arrays.asList(secondaryCatList).contains(secondaryCategoryVO.getId());
        secondaryCategoryDto.setSelected(isSelected);
      }
      secondaryCategoryDtoList.add(secondaryCategoryDto);
    }*/
    classifiedSearchResponse.setSecondaryCategoryDtoList(secondaryCategoryDtoList);

    return classifiedSearchResponse;
  }

  @Override
  public ClassifiedPostResponse getClassified(Long postId) {
    UserResponse userResponse = getUserService().getLoggedInUser();
    if (userResponse == null) {
      throw new InValidInputException("USER CANNOT BE NULL");
    }

    if (postId == null) {
      throw new InValidInputException("Post Id cannot be null");
    }

    PostVO postVO = (PostVO)getBaseESService().get(postId, null,PostVO.class);
    ClassifiedVO classifiedVO = (ClassifiedVO) getBaseESService().get(postId, postId, ClassifiedVO.class);
    if (postVO != null && classifiedVO != null) {
      UserResponse postUser = getUserService().findById(postVO.getUserId());
      ClassifiedPostResponse classifiedPostResponse = createPostResponse(postVO, classifiedVO, postUser);

      boolean isImp = getPostService().isPostImportantForUser(postVO.getPostId(), userResponse.getId());
      boolean isLiked = getPostService().isPostLikedForUser(postVO.getPostId(), userResponse.getId());

      classifiedPostResponse.setImportant(isImp);
      classifiedPostResponse.setLiked(isLiked);

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
          classifiedPostResponse.setCommentListResponse(commentListResponse);
        }
      }
      return classifiedPostResponse;
    } else {
      ClassifiedPostResponse classifiedPostResponse  = new ClassifiedPostResponse();
      classifiedPostResponse.setException(true);
      classifiedPostResponse.addMessage("Something unexpected occurred ! Try Again");
      return classifiedPostResponse;
    }
  }

  private ClassifiedPostResponse createPostResponse(PostVO postVO, ClassifiedVO classifiedVO,UserResponse userResponse) {
    ClassifiedPostResponse postResponse = new ClassifiedPostResponse();
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
    postResponse.setCityDtoList(classifiedVO.getLocationList());
    postResponse.setSecondaryCategoryDtoList(classifiedVO.getSecondaryCategoryDtoList());

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

  public AttachmentDao getAttachmentDao() {
    return attachmentDao;
  }
}
