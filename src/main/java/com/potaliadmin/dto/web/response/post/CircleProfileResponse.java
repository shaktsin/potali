package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;
import com.potaliadmin.dto.web.response.user.UserDto;

import java.util.List;

/**
 * Created by shaktsin on 3/11/15.
 */
public class CircleProfileResponse extends GenericBaseResponse {

  private Long id;
  private String name;
  private String desc;
  private boolean moderate;
  private boolean joined;
  private boolean hide;
  private List<GenericPostResponse> posts;
  private long totalPosts;

  private List<UserDto> userDtoList;
  private long members;


  private int pageNo;
  private int perPage;

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

  public boolean isModerate() {
    return moderate;
  }

  public void setModerate(boolean moderate) {
    this.moderate = moderate;
  }

  public boolean isJoined() {
    return joined;
  }

  public void setJoined(boolean joined) {
    this.joined = joined;
  }

  public List<GenericPostResponse> getPosts() {
    return posts;
  }

  public void setPosts(List<GenericPostResponse> posts) {
    this.posts = posts;
  }

  public long getTotalPosts() {
    return totalPosts;
  }

  public void setTotalPosts(long totalPosts) {
    this.totalPosts = totalPosts;
  }

  public List<UserDto> getUserDtoList() {
    return userDtoList;
  }

  public void setUserDtoList(List<UserDto> userDtoList) {
    this.userDtoList = userDtoList;
  }

  public long getMembers() {
    return members;
  }

  public void setMembers(long members) {
    this.members = members;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public int getPerPage() {
    return perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public String getDesc() {
        return desc;
    }

  public void setDesc(String desc) {
        this.desc = desc;
    }


  public boolean isHide() {
    return hide;
  }

  public void setHide(boolean hide) {
    this.hide = hide;
  }
}
