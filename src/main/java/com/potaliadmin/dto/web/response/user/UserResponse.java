package com.potaliadmin.dto.web.response.user;

import com.potaliadmin.constants.json.DtoJsonConstants;
import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.vo.user.UserVO;

import java.util.List;

/**
 * Created by Shakti Singh on 10/6/14.
 */
public class UserResponse extends GenericBaseResponse {

  private Long id;
  private String name;
  private String email;
  private String passwordChecksum;
  private Long instituteId;
  private String image;
  private List<Long> circleList;
  public boolean verified;
  private String firstName;
  private String lastName;
  private int yearOfGrad;

  public UserResponse() {}

  public UserResponse(UserVO userVO) {
    id = userVO.getId();
    name = userVO.getAccountName();
    email = userVO.getEmail();
    passwordChecksum = userVO.getChecksum();
    instituteId = userVO.getInstitutionId();
    image = userVO.getImage();
    circleList = userVO.getCircleList();
    verified = userVO.isVerified();

  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPasswordChecksum() {
    return passwordChecksum;
  }

  public void setPasswordChecksum(String passwordChecksum) {
    this.passwordChecksum = passwordChecksum;
  }

  public Long getInstituteId() {
    return instituteId;
  }

  public void setInstituteId(Long instituteId) {
    this.instituteId = instituteId;
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

  /*@Override
  protected List<String> getKeys() {
    List<String> keys = super.getKeys();
    keys.add(DtoJsonConstants.ID);
    keys.add(DtoJsonConstants.NAME);
    keys.add(DtoJsonConstants.EMAIL);
    return keys;
  }

  @Override
  protected List<Object> getValues() {
    List<Object> values = super.getValues();
    values.add(id);
    values.add(name);
    values.add(email);
    return values;
  }*/
}
