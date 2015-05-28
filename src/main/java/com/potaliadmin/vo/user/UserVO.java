package com.potaliadmin.vo.user;

import com.potaliadmin.domain.user.User;
import com.potaliadmin.dto.web.response.user.UserResponse;
import com.potaliadmin.framework.elasticsearch.annotation.ElasticEntity;
import com.potaliadmin.vo.BaseElasticVO;
import com.potaliadmin.vo.circle.CircleVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shakti on 28/1/15.
 */
@ElasticEntity(type = "user")
public class UserVO extends BaseElasticVO {

  private Long id;
  private String accountName;
  private String email;
  private String checksum;
  private Long institutionId;
  private String image;
  private List<Long> circleList;
  private boolean verified;
  private String firstName;
  private String lastName;
  private int yearOfGrad;
  private String gcmId;

  public UserVO() {
  }

  public UserVO(Long id) {
    this.id = id;
  }

  public UserVO(User user, List<Long> circleList) {
    id = user.getId();
    institutionId = user.getInstituteId();
    accountName = user.getAccountName();
    email = user.getEmail();
    checksum = user.getPasswordChecksum();
    image = user.getProfileImage();
    verified = user.getVerified();
    firstName = user.getFirstName();
    lastName = user.getLastName();
    yearOfGrad = user.getYearOfGraduation();
    gcmId = user.getGcmId();

    if (circleList == null || circleList.isEmpty()) {
      List<Long> emptyArrayList = new ArrayList<Long>();
      emptyArrayList.add(-1L);
      this.circleList = emptyArrayList;
    } else {
      this.circleList = circleList;
    }
  }

  public UserVO(UserResponse userResponse) {
    id = userResponse.getId();
    institutionId = userResponse.getInstituteId();
    accountName = userResponse.getName();
    email = userResponse.getEmail();
    checksum = userResponse.getPasswordChecksum();
    image = userResponse.getImage();
    verified = userResponse.isVerified();
    firstName = userResponse.getFirstName();
    lastName = userResponse.getLastName();
    yearOfGrad = userResponse.getYearOfGrad();
    circleList = userResponse.getCircleList();
    gcmId = userResponse.getGcmId();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getChecksum() {
    return checksum;
  }

  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  public Long getInstitutionId() {
    return institutionId;
  }

  public void setInstitutionId(Long institutionId) {
    this.institutionId = institutionId;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public List<Long> getCircleList() {
    return circleList;
  }

  public void setCircleList(List<Long> circleList) {
    this.circleList = circleList;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
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

  public int getYearOfGrad() {
    return yearOfGrad;
  }

  public void setYearOfGrad(int yearOfGrad) {
    this.yearOfGrad = yearOfGrad;
  }

  public String getGcmId() {
    return gcmId;
  }

  public void setGcmId(String gcmId) {
    this.gcmId = gcmId;
  }
}
