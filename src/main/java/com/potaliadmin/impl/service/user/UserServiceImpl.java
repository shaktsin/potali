package com.potaliadmin.impl.service.user;

import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.attachment.EnumImageFormat;
import com.potaliadmin.constants.circle.CircleType;
import com.potaliadmin.domain.circle.Circle;
import com.potaliadmin.domain.image.Avatar;
import com.potaliadmin.domain.user.User;
import com.potaliadmin.domain.user.UserCircleMapping;
import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.dto.internal.hibernate.user.UserSignUpQueryRequest;
import com.potaliadmin.dto.web.request.user.UserProfileUpdateRequest;
import com.potaliadmin.dto.web.request.user.UserSignUpRequest;
import com.potaliadmin.dto.web.request.user.UserVerificationRequest;
import com.potaliadmin.dto.web.response.base.GenericSuccessResponse;
import com.potaliadmin.dto.web.response.user.UserProfileUpdateResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.cache.institute.InstituteCache;
import com.potaliadmin.framework.elasticsearch.BaseESService;
import com.potaliadmin.framework.elasticsearch.ESSearchFilter;
import com.potaliadmin.framework.elasticsearch.response.ESSearchResponse;
import com.potaliadmin.impl.framework.ServiceLocatorFactory;
import com.potaliadmin.impl.framework.properties.AppProperties;
import com.potaliadmin.pact.dao.circle.CircleDao;
import com.potaliadmin.pact.dao.image.AvatarDao;
import com.potaliadmin.pact.dao.user.UserDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.service.cache.MemCacheService;
import com.potaliadmin.pact.service.circle.CircleService;
import com.potaliadmin.pact.service.institute.InstituteReadService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.security.Principal;
import com.potaliadmin.util.BaseUtil;
import com.potaliadmin.util.rest.HippoHttpUtils;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;
import com.potaliadmin.vo.user.UserVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shakti Singh on 10/6/14.
 */
@Service
public class UserServiceImpl implements UserService {

  private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  UserDao userDao;

  @Autowired
  UploadService uploadService;

  @Autowired
  AvatarDao avatarDao;

  @Autowired
  MemCacheService memCacheService;

  @Autowired
  BaseESService baseESService;

  @Autowired
  InstituteReadService instituteReadService;
  @Autowired
  AppProperties appProperties;
  @Autowired
  CircleDao circleDao;

  //CircleService circleService;

  @Override
  public UserResponse findById(Long id) {
    UserResponse userResponse = null;
    UserVO userVO = (UserVO)getBaseESService().get(id, null, UserVO.class);
    if (userVO != null) {
      userResponse = new UserResponse(userVO);
    }

    //UserResponse userResponse =(UserResponse) getMemCacheService().get(MemCacheNS.USER_BY_ID, id.toString());
    // first try to find out from cache
    if (userResponse != null) {
      return userResponse;
    }
    // otherwise fetch from database
    User user = getUserDao().findById(id);
    if (null != user) {
      userResponse = new UserResponse();
      userResponse.setId(user.getId());
      userResponse.setEmail(user.getEmail());
      userResponse.setName(user.getAccountName());
      userResponse.setPasswordChecksum(user.getPasswordChecksum());
      userResponse.setInstituteId(user.getInstituteId());
      userResponse.setImage(user.getProfileImage());

      // put in es
      //getMemCacheService().put(MemCacheNS.USER_BY_ID, user.getId().toString(), userResponse);
      List<Circle> circleList = getCircleDao().getUserCircle(user.getId());
      List<Long> circleIdList = new ArrayList<Long>();
      for (Circle circle : circleList) {
        circleIdList.add(circle.getId());
      }
      userVO = new UserVO(user, circleIdList);
      getBaseESService().put(userVO);
    }
    return userResponse;
  }

  public UserResponse findByEmail(String email) {
    UserResponse userResponse = null;
    TermFilterBuilder termFilterBuilder = FilterBuilders.termFilter("email", email);

    ESSearchFilter esSearchFilter =  new ESSearchFilter().setFilterBuilder(termFilterBuilder);
    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, UserVO.class);
    if (esSearchResponse.getTotalResults() > 0) {
      UserVO userVO =(UserVO) esSearchResponse.getBaseElasticVOs().get(0);
      userResponse = new UserResponse(userVO);
    }

    //UserResponse userResponse = (UserResponse) getMemCacheService().get(MemCacheNS.USER_BY_EMAIL, email);

    // first try to find out from cache
    if (userResponse != null) {
      return userResponse;
    }
    // otherwise fetch from database

    User user = getUserDao().findByEmail(email);
    if (null != user) {
      userResponse = new UserResponse();
      userResponse.setId(user.getId());
      userResponse.setEmail(user.getEmail());
      userResponse.setName(user.getAccountName());
      userResponse.setPasswordChecksum(user.getPasswordChecksum());
      userResponse.setInstituteId(user.getInstituteId());
      userResponse.setImage(user.getProfileImage());
      userResponse.setFirstName(user.getFirstName());
      userResponse.setLastName(user.getLastName());
      userResponse.setYearOfGrad(user.getYearOfGraduation());

      // put in es
      //getMemCacheService().put(MemCacheNS.USER_BY_ID, user.getId().toString(), userResponse);
      List<Circle> circleList = getCircleDao().getUserCircle(user.getId());
      List<Long> circleIdList = new ArrayList<Long>();
      for (Circle circle : circleList) {
        circleIdList.add(circle.getId());
      }
      UserVO userVO = new UserVO(user, circleIdList);
      getBaseESService().put(userVO);
    }
    return userResponse;
  }

  @Override
  @Transactional
  public UserResponse signUp(UserSignUpRequest userSignUpRequest) {

    if (userSignUpRequest == null) {
      UserResponse userResponse = new UserResponse();
      userResponse.setException(Boolean.TRUE);
      userResponse.addMessage("User request Parameters cannot be null");
      return userResponse;
      //throw new InValidInputException("User request Parameters cannot be null");
    }
    if (userSignUpRequest.validate()) {
      UserResponse userResponse = new UserResponse();
      userResponse.setException(Boolean.TRUE);
      userResponse.addMessage("Input Parameters are invalid!");
      return userResponse;
      //throw new InValidInputException("Input Parameters are invalid!");
    }

    List<InstituteVO> instituteVOs = getInstituteReadService().getAllInstitute();
    String emailSuffix = userSignUpRequest.getEmail().split("@")[1];
    if (emailSuffix == null) {
      UserResponse userResponse = new UserResponse();
      userResponse.setException(Boolean.TRUE);
      userResponse.addMessage("Input Parameters are invalid!");
      return userResponse;
    }
    for (InstituteVO instituteVO : instituteVOs) {
      if (instituteVO.getEmSuffix().equals(emailSuffix)) {
        userSignUpRequest.setInstituteId(instituteVO.getId());
        break;
      }
    }

    if (userSignUpRequest.getInstituteId() == null) {
      UserResponse userResponse = new UserResponse();
      userResponse.setException(Boolean.TRUE);
      userResponse.addMessage("Sorry, Your college is not registered with us.We will soon reach to you");
      return userResponse;
    }

    UserResponse userResponse = findByEmail(userSignUpRequest.getEmail());
    if (null != userResponse) {
      if (userSignUpRequest.getThirdPartAuth()) {
        return userResponse;
      } else {
        userResponse = new UserResponse();
        userResponse.setException(Boolean.TRUE);
        userResponse.addMessage("You have already registered with us!");
        return userResponse;
      }
      //throw new PotaliRuntimeException("You have already registered with us!");
    }

    InstituteVO instituteVO = InstituteCache.getCache().getInstitute(userSignUpRequest.getInstituteId());
    if (!userSignUpRequest.getEmail().toLowerCase().contains(instituteVO.getEmSuffix().toLowerCase())) {
      userResponse = new UserResponse();
      userResponse.setException(Boolean.TRUE);
      userResponse.addMessage("You are not the student of college !"+instituteVO.getNm());
      return userResponse;
      //throw new PotaliRuntimeException("You are not the student of college !"+instituteVO.getNm());
    }

    UserSignUpQueryRequest userSignUpQueryRequest = new UserSignUpQueryRequest(userSignUpRequest);
    User user = getUserDao().createUser(userSignUpQueryRequest);

    //create response
    userResponse = new UserResponse();
    userResponse.setId(user.getId());
    userResponse.setEmail(user.getEmail());
    userResponse.setName(user.getAccountName());
    userResponse.setInstituteId(user.getInstituteId());
    userResponse.setImage(user.getProfileImage());
    userResponse.setFirstName(user.getFirstName());
    userResponse.setLastName(user.getFirstName());
    userResponse.setYearOfGrad(user.getYearOfGraduation());
    userResponse.setVerified(false);

    // put in mem cache
    //getMemCacheService().put(MemCacheNS.USER_BY_ID, user.getId().toString(), userResponse);
    //getMemCacheService().put(MemCacheNS.USER_BY_EMAIL, user.getEmail(), userResponse);
    AndFilterBuilder andFilterBuilder = FilterBuilders.andFilter();
    TermFilterBuilder termFilterBuilder = FilterBuilders.termFilter("type", CircleType.ALL.getId());
    TermFilterBuilder termFilterBuilder2 = FilterBuilders.termFilter("instituteId", userResponse.getInstituteId());
    andFilterBuilder.add(termFilterBuilder);
    andFilterBuilder.add(termFilterBuilder2);

    ESSearchFilter esSearchFilter =
        new ESSearchFilter().setFilterBuilder(andFilterBuilder);
    ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, CircleVO.class);
    List<BaseElasticVO> postReactionVOs = esSearchResponse.getBaseElasticVOs();
    if (postReactionVOs != null && !postReactionVOs.isEmpty()) {
      CircleVO circleVO = (CircleVO) postReactionVOs.get(0);


      // join circle first in DB
      UserCircleMapping userCircleMapping = getCircleDao().joinCircle(userResponse, circleVO.getId(), true,false);
      if (userCircleMapping == null) {
        throw new PotaliRuntimeException("Some Exception occurred in sign up! Please Try Again");
      }

      UserVO userVO = new UserVO(user, null);
      List<Long> circleList = userVO.getCircleList();
      try {
        circleList.add(circleVO.getId());
      } catch (Exception e) {
        //e.printStackTrace();
        logger.error("Error ",e);
      }

      userVO.setCircleList(circleList);

      boolean published = getBaseESService().put(userVO);
      if (!published) {
        throw new PotaliRuntimeException("Some Exception occurred in sign up! Please Try Again");
      }

    } else {
      //create a circle for first user
      //throw new PotaliRuntimeException("Some Exception occurred in sign up! Please Try Again");
      Circle circle = getCircleDao().createCircle(CircleType.ALL.getName(), CircleType.ALL,
          userResponse, false);

      if (circle == null) {
        logger.error("Error in creating all circle in database");
        throw new PotaliRuntimeException("Some Exception occurred in sign up! Please Try Again");
      }

      CircleVO circleVO = new CircleVO(circle);
      circleVO.setAdmin(userResponse.getId());
      circleVO.setInstituteId(userResponse.getInstituteId());
      circleVO.setActive(true);


      boolean published = getBaseESService().put(circleVO);
      if (!published) {
        logger.error("Error in creating all circle in database");
        throw new PotaliRuntimeException("Couldn't create circle, Please Try Again!");
      }

      UserVO userVO = new UserVO(user, null);
      List<Long> circleList = userVO.getCircleList();
      try {
        circleList.add(circleVO.getId());
      } catch (Exception e) {
        //e.printStackTrace();
        logger.error("Error ",e);
      }

      userVO.setCircleList(circleList);

      published = getBaseESService().put(userVO);
      if (!published) {
        logger.error("Error in creating all circle in database");
        throw new PotaliRuntimeException("Some Exception occurred in sign up! Please Try Again");
      }
    }


    HippoHttpUtils.sendVerificationToken(user.getVerificationToken(), user.getFirstName(), user.getEmail());


    return userResponse;
  }

  @Override
  public UserResponse getLoggedInUser() {
    Principal principal =(Principal) SecurityUtils.getSubject().getPrincipal();
    return findByEmail(principal.getEmail());
  }

  @Override
  @Transactional
  public UserProfileUpdateResponse updateProfile(UserProfileUpdateRequest userProfileUpdateRequest,FormDataBodyPart img) {
    if (!userProfileUpdateRequest.validate()) {
      throw new InValidInputException("INVALID INPUT!");
    }

    UserResponse userResponse = getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Action!");
    }

    User user = getUserDao().findById(userResponse.getId());
    if (StringUtils.isNotBlank(userProfileUpdateRequest.getFirstName())) {
      user.setFirstName(userProfileUpdateRequest.getFirstName());
    }
    if (StringUtils.isNotBlank(userProfileUpdateRequest.getLastName())) {
      user.setLastName(userProfileUpdateRequest.getLastName());
    }
    if (StringUtils.isNotBlank(userProfileUpdateRequest.getAccountName())) {
      user.setAccountName(userProfileUpdateRequest.getAccountName());
    }
    if (userProfileUpdateRequest.getYearOfGrad() != null) {
      user.setYearOfGraduation(userProfileUpdateRequest.getYearOfGrad());

    }

    if (img.getContentDisposition().getFileName() != null) {
      try {
        String serverFileName = userResponse.getId() +DefaultConstants.NAME_SEPARATOR +img.getContentDisposition().getFileName();
        String rootPath = getAppProperties().getUploadPicPath() + File.separator + DefaultConstants.PROFILE;
        String fullFileName = rootPath + File.separator + serverFileName;
        uploadImageToServer(fullFileName, img.getValueAs(InputStream.class));

        Map<String,Object> map = getUploadService().uploadProfImageToCloud(user.getId(), new File(fullFileName));
        Avatar avatar = new Avatar();
        Long version = (Long) map.get("version");
        String pubId = (String) map.get("public_id");
        String format = (String) map.get("format");

        avatar.setWidth(((Long)map.get("width")).intValue());
        avatar.setHeight(((Long) map.get("height")).intValue());
        avatar.setVersion(version);
        avatar.setPublicId(pubId);
        avatar.setFormat(EnumImageFormat.getImageFormatByString(format));
        String imageLink = getUploadService().getCanonicalPathOfCloudResource(pubId, version, format);
        avatar.setUrl(imageLink);
        avatar.setUserId(user.getId());
        avatar.setUserInstituteId(user.getInstituteId());

        getAvatarDao().save(avatar);

        user.setProfileImage(imageLink);

      } catch (Exception e) {
        logger.error("Error while uploading image",e);
        throw new PotaliRuntimeException("Something wrong occurred, please try again!");
      }

    }

    List<Circle> circleList = getCircleDao().getUserCircle(user.getId());
    List<Long> circleIdList = new ArrayList<Long>();
    for (Circle circle : circleList) {
      circleIdList.add(circle.getId());
    }
    UserVO userVO = new UserVO(user, circleIdList);
    user = (User) getUserDao().save(user);

    // join year club
    if (userProfileUpdateRequest.getYearOfGrad() != null) {

      AndFilterBuilder andFilterBuilder =
          FilterBuilders.andFilter(FilterBuilders.termFilter("name", userProfileUpdateRequest.getYearOfGrad()),
          FilterBuilders.termFilter("type", CircleType.YEAR.getId()));

      ESSearchFilter esSearchFilter =
          new ESSearchFilter().setFilterBuilder(andFilterBuilder);

      ESSearchResponse esSearchResponse = getBaseESService().search(esSearchFilter, CircleVO.class);

      List<BaseElasticVO> baseElasticVOs = esSearchResponse.getBaseElasticVOs();
      if (baseElasticVOs != null && !baseElasticVOs.isEmpty()) {
        CircleVO circleVO = (CircleVO) baseElasticVOs.get(0);

        // join circle first in DB
        UserCircleMapping userCircleMapping = getCircleDao().joinCircle(userResponse, circleVO.getId(), true,false);
        if (userCircleMapping == null) {
          throw new PotaliRuntimeException("Some Exception occurred in sign up! Please Try Again");
        }

        circleIdList = userVO.getCircleList();
        circleIdList.add(circleVO.getId());
        userVO.setCircleList(circleIdList);
      }

    }

    boolean published = getBaseESService().put(userVO);
    if (!published) {
      throw new PotaliRuntimeException("Some Exception occurred in sign up! Please Try Again");
    }

    UserProfileUpdateResponse userProfileUpdateResponse = new UserProfileUpdateResponse();
    userProfileUpdateResponse.setAccountName(user.getAccountName());
    userProfileUpdateResponse.setGradYear(user.getYearOfGraduation());
    userProfileUpdateResponse.setProfileImageLink(user.getProfileImage());
    userProfileUpdateResponse.setFirstName(user.getFirstName());
    userProfileUpdateResponse.setLastName(user.getLastName());
    userProfileUpdateResponse.setEmail(user.getEmail());

    return userProfileUpdateResponse;
  }

  @Override
  @Transactional
  public GenericSuccessResponse verifyUser(UserVerificationRequest userVerificationRequest) {
    boolean isVerified = false;
    UserResponse userResponse = getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Action!");
    }

    User user = getUserDao().findById(userResponse.getId());
    if (user.getVerificationToken() == userVerificationRequest.getToken()) {
      user.setVerified(true);
      getUserDao().save(user);
    }

    List<Circle> circleList = getCircleDao().getUserCircle(user.getId());
    List<Long> circleIdList = new ArrayList<Long>();
    for (Circle circle : circleList) {
      circleIdList.add(circle.getId());
    }
    UserVO userVO = new UserVO(user, circleIdList);
    boolean published = getBaseESService().put(userVO);
    if (!published) {
      throw new PotaliRuntimeException("Some Exception occurred in sign up! Please Try Again");
    }

    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    genericSuccessResponse.setSuccess(true);

    return genericSuccessResponse;
  }

  @Override
  @Transactional
  public GenericSuccessResponse reGenerateToken() {
    UserResponse userResponse = getLoggedInUser();
    if (userResponse == null) {
      throw new UnAuthorizedAccessException("UnAuthorized Action!");
    }

    User user = getUserDao().findById(userResponse.getId());
    user.setVerificationToken(BaseUtil.generateVerificationToken());
    getUserDao().save(user);


    HippoHttpUtils.sendReGenVerificationMail(user.getVerificationToken(), user.getFirstName(), user.getEmail());

    GenericSuccessResponse genericSuccessResponse = new GenericSuccessResponse();
    genericSuccessResponse.setSuccess(true);
    return genericSuccessResponse;
  }


  private void uploadImageToServer(String fullFileName, InputStream uploadedInputStream) throws Exception {
    try {

      File file = new File(fullFileName);
      if (!file.isDirectory() && file.exists()) {
        boolean isDeleted = file.delete();
        if (!isDeleted) {
          fullFileName = fullFileName + "_D";
          file = new File(fullFileName);
        }
      }

      OutputStream out = new FileOutputStream(file);
      int read = 0;
      byte[] bytes = new byte[1024];

      out = new FileOutputStream(file);
      while ((read = uploadedInputStream.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
      out.flush();
      out.close();
      uploadedInputStream.close();
    } catch (Exception e) {
      logger.error("Error occurred while uploading image",e);
    }
  }

  public UserDao getUserDao() {
    return userDao;
  }

  public UploadService getUploadService() {
    return uploadService;
  }

  public AvatarDao getAvatarDao() {
    return avatarDao;
  }

  public MemCacheService getMemCacheService() {
    return memCacheService;
  }

  public BaseESService getBaseESService() {
    return baseESService;
  }

  public InstituteReadService getInstituteReadService() {
    return instituteReadService;
  }

  public AppProperties getAppProperties() {
    return appProperties;
  }

  public CircleDao getCircleDao() {
    return circleDao;
  }
}
