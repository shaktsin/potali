package com.potaliadmin.dto.web.request.user;


import com.potaliadmin.constants.DefaultConstants;
import com.potaliadmin.constants.institute.EnumInstitute;
import com.potaliadmin.util.BaseUtil;

/**
 * Created by Shakti Singh on 10/6/14.
 */
public class UserSignUpRequest {

  private String firstName;
  private String lastName;
  private String accountName;
  private String email;
  private String password;
  private String rePassword;
  private Long instituteId;
  private Integer gender;
  private Boolean verified;
  private boolean thirdPartAuth;
  private String gcmId;

  public boolean validate() {
    boolean isValid = Boolean.TRUE;
    if (null == firstName) {
      isValid = Boolean.FALSE;
    }
    if (isValid && (null == email)) {
      isValid = Boolean.FALSE;
    }
    if (isValid && BaseUtil.isValidEmail(email)) {
      isValid = Boolean.FALSE;
    }
    if (isValid && (null == getPassword() || null == getRePassword())) {
      isValid = Boolean.FALSE;
    }
    if (isValid && (getPassword().equalsIgnoreCase(getRePassword()))) {
      isValid = Boolean.FALSE;
    }
    /*if (isValid && (null == instituteId)) {
      isValid = Boolean.FALSE;
    }
    if (isValid && !(EnumInstitute.contains(instituteId))) {
      isValid = Boolean.FALSE;
    }*/

    return isValid;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    if (thirdPartAuth) {
      return DefaultConstants.DEFAULT_PASSWORD+email;
    }
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRePassword() {
    if (thirdPartAuth) {
      return DefaultConstants.DEFAULT_PASSWORD+email;
    }
    return rePassword;
  }

  public void setRePassword(String rePassword) {
    this.rePassword = rePassword;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public Long getInstituteId() {
    return instituteId;
  }

  public void setInstituteId(Long instituteId) {
    this.instituteId = instituteId;
  }

  public Integer getGender() {
    return gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public Boolean getVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public boolean getThirdPartAuth() {
    return thirdPartAuth;
  }

  public void setThirdPartAuth(boolean thirdPartAuth) {
    this.thirdPartAuth = thirdPartAuth;
  }

  public String getGcmId() {
    return gcmId;
  }

  public void setGcmId(String gcmId) {
    this.gcmId = gcmId;
  }
}
