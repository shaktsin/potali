package com.potaliadmin.dto.web.response.user;


import com.potaliadmin.dto.web.response.circle.CircleDto;

import java.util.List;

/**
 * Created by Shakti Singh on 12/24/14.
 */
public class UserDto {

  private Long id;
  private String name;
  private String image;
  private List<CircleDto> circleDtoList;

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

  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
  }
}
