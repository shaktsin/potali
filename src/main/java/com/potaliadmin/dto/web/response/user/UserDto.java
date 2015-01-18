package com.potaliadmin.dto.web.response.user;


/**
 * Created by Shakti Singh on 12/24/14.
 */
public class UserDto {

  private Long id;
  private String name;
  private String image;

  public UserDto() {}

  public UserDto(UserResponse userResponse) {
    this.id = userResponse.getId();
    this.name = userResponse.getName();
    this.image = userResponse.getImage();
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
}
