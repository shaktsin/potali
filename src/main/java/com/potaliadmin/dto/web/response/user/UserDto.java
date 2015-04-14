package com.potaliadmin.dto.web.response.user;


/**
 * Created by Shakti Singh on 12/24/14.
 */
public class UserDto {

  private Long id;
  private String name;
  private String image;
  private String emailId;
  private int circles;
  private int yearOfGrad;
  private String memberSince;

  public UserDto() {}

  public UserDto(UserResponse userResponse) {
    this.id = userResponse.getId();
    this.name = userResponse.getName();
    this.image = userResponse.getImage();
    this.emailId = userResponse.getEmail();
    yearOfGrad = userResponse.getYearOfGrad();
    circles = userResponse.getCircleList().size();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public int getCircles() {
    return circles;
  }

  public void setCircles(int circles) {
    this.circles = circles;
  }

  public int getYearOfGrad() {
    return yearOfGrad;
  }

  public void setYearOfGrad(int yearOfGrad) {
    this.yearOfGrad = yearOfGrad;
  }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

  public String getMemberSince() {
    return memberSince;
  }

  public void setMemberSince(String memberSince) {
    this.memberSince = memberSince;
  }
}
