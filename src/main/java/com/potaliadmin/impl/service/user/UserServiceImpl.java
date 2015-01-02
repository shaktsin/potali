package com.potaliadmin.impl.service.user;

import com.potaliadmin.domain.user.User;
import com.potaliadmin.dto.internal.cache.institute.InstituteVO;
import com.potaliadmin.dto.internal.hibernate.user.UserSignUpQueryRequest;
import com.potaliadmin.dto.web.request.user.UserProfileUpdateRequest;
import com.potaliadmin.dto.web.request.user.UserSignUpRequest;
import com.potaliadmin.dto.web.response.user.UserProfileUpdateResponse;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.exceptions.InValidInputException;
import com.potaliadmin.exceptions.PotaliRuntimeException;
import com.potaliadmin.exceptions.UnAuthorizedAccessException;
import com.potaliadmin.framework.cache.institute.InstituteCache;
import com.potaliadmin.pact.dao.user.UserDao;
import com.potaliadmin.pact.service.users.LoginService;
import com.potaliadmin.pact.service.users.UserService;
import com.potaliadmin.security.Principal;
import com.potaliadmin.util.BaseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Shakti Singh on 10/6/14.
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserDao userDao;

  @Override
  public UserResponse findById(Long id) {
    UserResponse userResponse = null;
    User user = getUserDao().findById(id);
    if (null != user) {
      userResponse = new UserResponse();
      userResponse.setId(user.getId());
      userResponse.setEmail(user.getEmail());
      userResponse.setName(user.getAccountName());
      userResponse.setPasswordChecksum(user.getPasswordChecksum());
      userResponse.setInstituteId(user.getInstituteId());
      userResponse.setImage(user.getProfileImage());
    }
    return userResponse;
  }

  public UserResponse findByEmail(String email) {
    UserResponse userResponse = null;
    User user = getUserDao().findByEmail(email);
    if (null != user) {
      userResponse = new UserResponse();
      userResponse.setId(user.getId());
      userResponse.setEmail(user.getEmail());
      userResponse.setName(user.getAccountName());
      userResponse.setPasswordChecksum(user.getPasswordChecksum());
      userResponse.setInstituteId(user.getInstituteId());
      userResponse.setImage(user.getProfileImage());
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

    return userResponse;
  }

  @Override
  public UserResponse getLoggedInUser() {
    Principal principal =(Principal) SecurityUtils.getSubject().getPrincipal();
    return findByEmail(principal.getEmail());
  }

  @Override
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



    return null;
  }

  public UserDao getUserDao() {
    return userDao;
  }
}
