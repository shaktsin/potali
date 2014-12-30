package com.potaliadmin.dto.internal.hibernate.user;

import com.potaliadmin.constants.user.EnumGender;
import com.potaliadmin.dto.web.request.user.UserSignUpRequest;
import com.potaliadmin.util.BaseUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Shakti Singh on 12/6/14.
 */
public class UserSignUpQueryRequest extends AbstractUserRequest {

  public UserSignUpQueryRequest() {  }

  public UserSignUpQueryRequest(UserSignUpRequest userSignUpRequest) {
    this.firstName = userSignUpRequest.getFirstName();
    if (null== firstName || StringUtils.isBlank(firstName)) {
      firstName = userSignUpRequest.getEmail();
    }
    this.lastName = userSignUpRequest.getLastName();
    this.accountName = userSignUpRequest.getAccountName();
    if (null == accountName) {
      accountName = firstName;
    }
    this.gender =userSignUpRequest.getGender();
    this.email = userSignUpRequest.getEmail();
    this.verified = userSignUpRequest.getVerified();
    this.instituteId = userSignUpRequest.getInstituteId();
    this.hash = BaseUtil.passwordEncrypt(userSignUpRequest.getPassword());
  }

  public boolean verify() {
    Boolean isVerified = Boolean.TRUE;
    if ((null==firstName || StringUtils.isEmpty(firstName))){
      isVerified = Boolean.FALSE;
    }
    if (isVerified && (null == gender || !EnumGender.contains(gender))) {
      isVerified = Boolean.FALSE;
    }
    if (isVerified && (null == email)) {
      isVerified = Boolean.FALSE;
    }
    if (isVerified && (null == hash)) {
      isVerified = Boolean.FALSE;
    }
    if (isVerified && (null == instituteId)) {
      isVerified = Boolean.FALSE;
    }
    return isVerified;
  }


}
