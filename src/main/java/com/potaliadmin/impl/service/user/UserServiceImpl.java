package com.potaliadmin.impl.service.user;

import com.potaliadmin.constants.cache.MemCacheNS;
import com.potaliadmin.constants.image.EnumBucket;
import com.potaliadmin.constants.image.EnumImageSize;
import com.potaliadmin.domain.image.Avatar;
import com.potaliadmin.domain.user.User;
import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.dto.internal.hibernate.user.UserSignUpQueryRequest;
import com.potaliadmin.dto.internal.image.ImageDto;
import com.potaliadmin.dto.web.request.user.UserProfileUpdateRequest;
import com.potaliadmin.dto.web.request.user.UserSignUpRequest;
import com.potaliadmin.dto.web.response.user.UserProfileUpdateResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.cache.institute.InstituteCache;
import com.potaliadmin.pact.dao.image.AvatarDao;
import com.potaliadmin.pact.dao.user.UserDao;
import com.potaliadmin.pact.framework.aws.UploadService;
import com.potaliadmin.pact.service.cache.MemCacheService;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.security.Principal;
import com.potaliadmin.util.BaseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Shakti Singh on 10/6/14.
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserDao userDao;

  @Autowired
  UploadService uploadService;

  @Autowired
  AvatarDao avatarDao;

  @Autowired
  MemCacheService memCacheService;

  @Override
  public UserResponse findById(Long id) {
    UserResponse userResponse =(UserResponse) getMemCacheService().get(MemCacheNS.USER_BY_ID, id.toString());
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

      // put in memcache
      getMemCacheService().put(MemCacheNS.USER_BY_ID, user.getId().toString(), userResponse);
    }
    return userResponse;
  }

  public UserResponse findByEmail(String email) {
    UserResponse userResponse = (UserResponse) getMemCacheService().get(MemCacheNS.USER_BY_EMAIL, email);

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

      // put in memcache
      getMemCacheService().put(MemCacheNS.USER_BY_ID, user.getId().toString(), userResponse);
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

    // put in mem cache
    getMemCacheService().put(MemCacheNS.USER_BY_ID, user.getId().toString(), userResponse);
    getMemCacheService().put(MemCacheNS.USER_BY_EMAIL, user.getEmail(), userResponse);

    return userResponse;
  }

  @Override
  public UserResponse getLoggedInUser() {
    Principal principal =(Principal) SecurityUtils.getSubject().getPrincipal();
    return findByEmail(principal.getEmail());
  }

  @Override
  @Transactional
  public UserProfileUpdateResponse updateProfile(UserProfileUpdateRequest userProfileUpdateRequest) {
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

    List<ImageDto> imageDtoList = userProfileUpdateRequest.getImageDtoList();
    boolean isImageUpdateSuccessful = false;
    boolean shouldUpdateImage  = imageDtoList != null && imageDtoList.size() > 0;
    if (shouldUpdateImage) {
      boolean uploaded = getUploadService().uploadProfileImageFiles(EnumBucket.PROFILE_BUCKET.getName(), imageDtoList, true);
      if (uploaded) {
        for (ImageDto imageDto : imageDtoList) {
          String url = getUploadService().getCanonicalPathOfResource(EnumBucket.PROFILE_BUCKET.getName(), imageDto.getFileName());
          getAvatarDao().createAvatar(EnumImageSize.getImageSizeById(imageDto.getSize()), userResponse, url);
        }
        isImageUpdateSuccessful = true;
      }
    }

    UserProfileUpdateResponse userProfileUpdateResponse = new UserProfileUpdateResponse();
    if (shouldUpdateImage) {
      if (isImageUpdateSuccessful) {
        Avatar thumbNail = getAvatarDao().findAvatar(EnumImageSize.XS_SMALL, userResponse);
        Avatar profileAvatar = getAvatarDao().findAvatar(EnumImageSize.MEDIUM, userResponse);
        if (thumbNail != null) {
          user.setProfileImage(thumbNail.getUrl());
        }
        user = (User)getUserDao().save(user);
        userProfileUpdateResponse.setAccountName(user.getAccountName());
        userProfileUpdateResponse.setGradYear(user.getYearOfGraduation());
        if (profileAvatar != null) {
          userProfileUpdateResponse.setProfileImageLink(profileAvatar.getUrl());
        }
      } else {
        userProfileUpdateResponse.setException(Boolean.TRUE);
        userProfileUpdateResponse.addMessage("Something went wrong,Please try again!");
      }
    } else {
      user = (User)getUserDao().save(user);
      Avatar avatar = getAvatarDao().findAvatar(EnumImageSize.MEDIUM, userResponse);
      userProfileUpdateResponse.setAccountName(user.getAccountName());
      userProfileUpdateResponse.setGradYear(user.getYearOfGraduation());
      if (avatar != null) {
        userProfileUpdateResponse.setProfileImageLink(avatar.getUrl());
      }
    }

    // update cache too
    // put in mem cache
    getMemCacheService().remove(MemCacheNS.USER_BY_ID, userResponse.getId().toString());
    getMemCacheService().remove(MemCacheNS.USER_BY_EMAIL, userResponse.getEmail());

    userResponse.setId(user.getId());
    userResponse.setName(user.getAccountName());
    userResponse.setEmail(user.getEmail());
    userResponse.setPasswordChecksum(user.getPasswordChecksum());
    userResponse.setInstituteId(user.getInstituteId());
    userResponse.setImage(user.getProfileImage());

    getMemCacheService().put(MemCacheNS.USER_BY_ID, user.getId().toString(), userResponse);
    getMemCacheService().put(MemCacheNS.USER_BY_EMAIL, user.getEmail(), userResponse);


    return userProfileUpdateResponse;
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
}
