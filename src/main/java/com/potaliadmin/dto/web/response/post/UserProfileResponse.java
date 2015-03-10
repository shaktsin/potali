package com.potaliadmin.dto.web.response.post;

import com.potaliadmin.dto.web.response.circle.CircleDto;
import com.potaliadmin.dto.web.response.user.UserResourceResponse;

import java.util.List;

/**
 * Created by shaktsin on 3/11/15.
 */
public class UserProfileResponse extends UserResourceResponse {

  private List<GenericPostResponse> posts;
  private long totalPosts;
  List<CircleDto> circleDtoList;
  private int totalCircle;


  private long pageNo;
  private long perPage;


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

  public List<CircleDto> getCircleDtoList() {
    return circleDtoList;
  }

  public void setCircleDtoList(List<CircleDto> circleDtoList) {
    this.circleDtoList = circleDtoList;
  }

  public int getTotalCircle() {
    return totalCircle;
  }

  public void setTotalCircle(int totalCircle) {
    this.totalCircle = totalCircle;
  }

  public long getPageNo() {
    return pageNo;
  }

  public void setPageNo(long pageNo) {
    this.pageNo = pageNo;
  }

  public long getPerPage() {
    return perPage;
  }

  public void setPerPage(long perPage) {
    this.perPage = perPage;
  }
}
