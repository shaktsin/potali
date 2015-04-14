package com.potaliadmin.dto.web.response.search;

import com.potaliadmin.dto.web.response.base.GenericBaseResponse;

import java.util.List;
import java.util.Set;

/**
 * Created by shaktsin on 4/12/15.
 */
public class SearchResponse extends GenericBaseResponse {

  private Set<SearchUserDto> users;
  private Set<CircleDto> circles;
  private Set<SearchPostDto> posts;

  public Set<SearchUserDto> getUsers() {
    return users;
  }

  public void setUsers(Set<SearchUserDto> users) {
    this.users = users;
  }

  public Set<CircleDto> getCircles() {
    return circles;
  }

  public void setCircles(Set<CircleDto> circles) {
    this.circles = circles;
  }

  public Set<SearchPostDto> getPosts() {
    return posts;
  }

  public void setPosts(Set<SearchPostDto> posts) {
    this.posts = posts;
  }
}
